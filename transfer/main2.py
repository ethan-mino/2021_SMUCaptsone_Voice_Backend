import re
import tensorflow as tf
from webdemo.webdemo.nmt.utils import misc_utils as utils
from webdemo.webdemo.nmt import inference

yo_ban_model_dir = "/home/ubuntu/urvoice/2021_SMUCaptsone_Voice_Backend/transfer/models/char_yo_ban"

yo_file = "/home/ubuntu/urvoice/2021_SMUCaptsone_Voice_Backend/transfer/data/raw/test.yo.txt"
ban_file = "/home/ubuntu/urvoice/2021_SMUCaptsone_Voice_Backend/transfer/data/raw/test.ban.txt"

SPACE_SYMBOL = "▁"
print("Loading tensorflow saved models...")
inference_fn = inference.inference

# Load hparams.
yo_ban_hparams = utils.load_hparams(yo_ban_model_dir)
print(yo_ban_hparams)

yo = "/home/ubuntu/urvoice/2021_SMUCaptsone_Voice_Backend/transfer/models/char_yo_ban"
# Inference
yo_ban_ckpt = tf.train.latest_checkpoint(yo_ban_model_dir)

# print(yo_ban_ckpt)
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
    translations = inference_fn(yo_ban_ckpt, char_seq_list, yo_ban_hparams, yo_ban_hparams)

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
if not yo_ban_hparams:
    raise RuntimeError("hparams should be loaded first!")
yo_fp = open(yo_file, "r", encoding="UTF_8")
ban_fp = open(ban_file, "r", encoding="UTF_8")
yo_sents = yo_fp.readlines()
ban_sents = ban_fp.readlines()
yo_sents = [sent.strip() for sent in yo_sents]
ban_sents = [sent.strip() for sent in ban_sents]
trans_sents = forward_transfer(yo_sents)
# verify result using back-translation
final_sents = []
accurate_cnt = 0
for (label_yo_sent, label_ban_sent, trans_sent) in zip(yo_sents, ban_sents, trans_sents):
    # label_yo_sent = chars_to_sent(label_yo_sent)
    # label_sho_sent = chars_to_sent(label_sho_sent)
    if trans_sent == label_ban_sent:
        accurate_cnt += 1
        # print("forward:", trans_sent)
        # print("bakward:", backtrans_sent)
        # print()
    else:
        print("labelyo:", label_yo_sent)
        print("labelban:", label_ban_sent)
        print("forward:", trans_sent)
        print()
print("accurate transfer: %.3f" % (accurate_cnt*1.0/len(yo_sents)))