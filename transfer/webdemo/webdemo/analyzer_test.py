import re
import tensorflow as tf
from webdemo.nmt.utils import misc_utils as utils
from webdemo.nmt import inference
from koalanlp import *
from koalanlp.api.tagger import SentenceSplitter

yo_sho_model_dir = "/home/hkh/sources/adc-chatbot/models/char_yo_sho_full"
sho_yo_model_dir = "/home/hkh/sources/adc-chatbot/models/char_sho_yo_full"

SPACE_SYMBOL = "▁"
SENT_BOUND_NEWLINE_SYMBOL = "\n"
SENT_BOUND_SPACE_SYMBOL = " "

print("Initializing Sentence Splitter modules")

initialize(packages=[API.KKMA, API.EUNJEON, API.TWITTER], version="1.9.2", java_options="-Xmx4g -Dfile.encoding=utf-8")
sents_splitter = SentenceSplitter(splitter_type=API.TWITTER)

print("Loading tensorflow saved models...")
inference_fn = inference.inference

# Load hparams.
yo_sho_hparams = utils.load_hparams(yo_sho_model_dir)
sho_yo_hparams = utils.load_hparams(sho_yo_model_dir)

# Inference
yo_sho_ckpt = tf.train.latest_checkpoint(yo_sho_model_dir)
sho_yo_ckpt = tf.train.latest_checkpoint(sho_yo_model_dir)


def remove_space(string):
    return re.sub("\s+", "", string)


def apply_left_right_rule(input_str, ref_str):
    # apply left-right rule
    for m in re.finditer(r'(<unk>)+', input_str):
        # print(m.start(), m.end())
        match_cnt = m.group().count("<unk>")
        if m.start() == 0 and m.end() != len(input_str):
            left_char = ""
            right_char = input_str[m.end()]
        elif m.end() == len(input_str) and m.start() != 0:
            left_char = input_str[m.start() - 1]
            right_char = ""
        else:
            left_char = input_str[m.start() - 1]
            right_char = input_str[m.end()]

        # print(left_char, right_char)
        pattern = '%s(?P<unk_char>.){%d}%s' % (left_char, match_cnt, right_char)
        # print(pattern)
        in_match = re.search(pattern, ref_str)
        if in_match is not None:
            input_str = input_str.replace("<unk>" * match_cnt, in_match.group('unk_char') * match_cnt, 1)
            # print(input_str)
            return apply_left_right_rule(input_str, ref_str)

    return input_str


def apply_double_left_rule(input_str, ref_str):
    # apply double-left rule
    for m in re.finditer(r'(<unk>)+', input_str):
        # print(m.start(), m.end())
        match_cnt = m.group().count("<unk>")
        first_char = second_char = ""

        if m.start() > 1:
            # ex) 삼계<unk> -> first_char=삼, second_char=계
            first_char = input_str[m.start() - 2]
            second_char = input_str[m.start() - 1]

        pattern = '%s%s(?P<unk_char>.){%d}' % (first_char, second_char, match_cnt)
        # print(pattern)
        in_match = re.search(pattern, ref_str)
        if in_match is not None:
            input_str = input_str.replace("<unk>" * match_cnt, in_match.group('unk_char') * match_cnt, 1)
            # print(input_str)
            return apply_double_left_rule(input_str, ref_str)

    return input_str


def apply_double_right_rule(input_str, ref_str):
    # apply double-left rule
    for m in re.finditer(r'(<unk>)+', input_str):
        # print(m.start(), m.end())
        match_cnt = m.group().count("<unk>")
        first_char = second_char = ""

        if m.end() < len(input_str) - 1:
            # ex) <unk>양식 -> first_char=양, second_char=식
            first_char = input_str[m.end() - 1]
            second_char = input_str[m.end()]

        pattern = '(?P<unk_char>.){%d}%s%s' % (match_cnt, first_char, second_char)
        # print(pattern)
        in_match = re.search(pattern, ref_str)
        if in_match is not None:
            input_str = input_str.replace("<unk>" * match_cnt, in_match.group('unk_char') * match_cnt, 1)
            # print(input_str)
            return apply_double_right_rule(input_str, ref_str)

    return input_str


def split_sentences(input_text):
    sents = sents_splitter.sentences(input_text.strip())
    return sents


def convert_chars(sent):
    char_sequence = [SPACE_SYMBOL if ch == ' ' else ch for ch in sent.strip()]
    return " ".join(char_sequence)


def forward_transfer(sents):
    char_seq_list = []
    for sent in sents:
        char_seq = convert_chars(sent)
        char_seq_list += [char_seq]

    # yo -> sho transfer
    translations = inference_fn(yo_sho_ckpt, char_seq_list, yo_sho_hparams)

    # post-processing
    trans_sents = []
    for (trans, char_seq) in zip(translations, char_seq_list):
        input_str = remove_space(trans.decode("utf8"))
        ref_str = remove_space(char_seq)
        input_str = apply_left_right_rule(input_str, ref_str)
        input_str = apply_double_left_rule(input_str, ref_str)
        input_str = apply_double_right_rule(input_str, ref_str)

        trans_sents.append(input_str.replace(SPACE_SYMBOL, " "))
    return trans_sents


def backward_transfer(trans_sents):
    char_seq_list = []
    for sent in trans_sents:
        char_seq = convert_chars(sent)
        char_seq_list += [char_seq]

    # yo -> sho transfer
    translations = inference_fn(sho_yo_ckpt, char_seq_list, sho_yo_hparams)

    # post-processing
    trans_sents = []
    for (trans, char_seq) in zip(translations, char_seq_list):
        input_str = remove_space(trans.decode("utf8"))
        ref_str = remove_space(char_seq)
        input_str = apply_left_right_rule(input_str, ref_str)
        input_str = apply_double_left_rule(input_str, ref_str)
        input_str = apply_double_right_rule(input_str, ref_str)

        trans_sents.append(input_str.replace(SPACE_SYMBOL, " "))
    return trans_sents


def transfer_text(input_text):
    if not yo_sho_hparams or not sho_yo_hparams:
        raise RuntimeError("hparams should be loaded first!")

    sents = split_sentences(input_text)
    trans_sents = forward_transfer(sents)

    final_results = []
    for (sent, trans_sent) in zip(sents, trans_sents):
            final_results.append({'input_sent': sent, 'tranfer_sent': trans_sent, 'is_transferred': True})

    final_sents = [result['tranfer_sent'] for result in final_results]
    output_text_newline = SENT_BOUND_NEWLINE_SYMBOL.join(final_sents)
    output_text_space = SENT_BOUND_SPACE_SYMBOL.join(final_sents)

    return output_text_newline, output_text_space, final_results

