import re
import tensorflow as tf
from webdemo.nmt.utils import misc_utils as utils
from webdemo.nmt import inference

# yo_sho_model_dir = "/home/hkh/sources/adc-chatbot/models/char_yo_sho"
yo_sho_model_dir = "/home/hkh/sources/adc-chatbot/models/char_yo_sho.test"
sho_yo_model_dir = "/home/hkh/sources/adc-chatbot/models/char_sho_yo"

yo_file = "/home/hkh/sources/adc-chatbot/data/raw/keti_yo_words.txt"
sho_file = "/home/hkh/sources/adc-chatbot/data/raw/keti_sho_words.txt"

SPACE_SYMBOL = "▁"

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


def convert_chars(sent):
    char_sequence = [SPACE_SYMBOL if ch == ' ' else ch for ch in sent.strip()]
    return " ".join(char_sequence)


def chars_to_sent(chars):
    sent = remove_space(chars)
    sent = sent.replace(SPACE_SYMBOL, " ")
    return sent


def forward_transfer(sents, unk_copy=True):
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
        if unk_copy:
            input_str = apply_left_right_rule(input_str, ref_str)
            input_str = apply_double_left_rule(input_str, ref_str)
            input_str = apply_double_right_rule(input_str, ref_str)

        trans_sents.append(input_str.replace(SPACE_SYMBOL, " "))
    return trans_sents


def backward_transfer(trans_sents, unk_copy=True):
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
        if unk_copy:
            input_str = apply_left_right_rule(input_str, ref_str)
            input_str = apply_double_left_rule(input_str, ref_str)
            input_str = apply_double_right_rule(input_str, ref_str)

        trans_sents.append(input_str.replace(SPACE_SYMBOL, " "))
    return trans_sents


# main
if not yo_sho_hparams or not sho_yo_hparams:
    raise RuntimeError("hparams should be loaded first!")

yo_fp = open(yo_file, "r")
sho_fp = open(sho_file, "r")

yo_sents = yo_fp.readlines()
sho_sents = sho_fp.readlines()
yo_sents = [sent.strip() for sent in yo_sents]
sho_sents = [sent.strip() for sent in sho_sents]

# trans_sents = forward_transfer(yo_sents)
trans_sents = forward_transfer(yo_sents)

# verify result using back-translation
final_sents = []
backtrans_sents = backward_transfer(trans_sents)

accurate_back_cnt = 0
accurate_cnt = 0
for (label_yo_sent, label_sho_sent, trans_sent, backtrans_sent) in zip(yo_sents, sho_sents, trans_sents, backtrans_sents):
    # label_yo_sent = chars_to_sent(label_yo_sent)
    # label_sho_sent = chars_to_sent(label_sho_sent)

    if label_yo_sent == backtrans_sent:
        final_sents.append(trans_sent)
    else:
        print("labelyo:", label_yo_sent)
        print("labelso:", label_sho_sent)
        print("forward:", trans_sent)
        print("bakward:", backtrans_sent)
        print()
        final_sents.append(label_yo_sent)

    if final_sents[-1] == label_sho_sent:
        accurate_back_cnt += 1
    # else:
    #     print("labelyo:", label_yo_sent)
    #     print("labelso:", label_sho_sent)
    #     print("forward:", trans_sent)
    #     print("bakward:", backtrans_sent)
    #     print()

    if trans_sent == label_sho_sent:
        accurate_cnt += 1
        # print("forward:", trans_sent)
        # print("bakward:", backtrans_sent)
        # print()
    # else:
    #     print("labelyo:", label_yo_sent)
    #     print("labelso:", label_sho_sent)
    #     print("forward:", trans_sent)
    #     print("bakward:", backtrans_sent)
    #     print()

print("tranfer wrong: %d" % (len(yo_sents)-accurate_cnt))
print("back transfer wrong: %d" % (len(yo_sents)-accurate_back_cnt))

print("accurate transfer: %.3f" % (accurate_cnt*1.0/len(yo_sents)))
print("accurate transfer(back): %.3f" % (accurate_back_cnt*1.0/len(yo_sents)))

