import tensorflow as tf
from webdemo.webdemo.nmt.utils import misc_utils as utils
from webdemo.webdemo.nmt import inference
import webdemo.webdemo.string_utils as string_utils
import webdemo.webdemo.forms as forms

from koalanlp.Util import initialize, finalize
from koalanlp.proc import SentenceSplitter
from koalanlp import API as API

yo_ban_model_dir = "/home/ubuntu/urvoice/2021_SMUCaptsone_Voice_Backend/transfer/models/char_yo_ban"

print("Initializing Sentence Splitter module")

initialize(java_options="-Xmx4g -Dfile.encoding=utf-8", OKT="2.02")

sents_splitter = SentenceSplitter(API.OKT)

print("Loading tensorflow saved models")
inference_fn = inference.inference

yo_ban_hparams = utils.load_hparams(yo_ban_model_dir)

yo_ban_ckpt = tf.train.latest_checkpoint(yo_ban_model_dir)

def forward_transfer(sents, target_style):
    char_seq_list=[]
    for sent in sents:
        char_seq = string_utils.convert_chars(sent)
        char_seq_list += [char_seq]

    if target_style == forms.STYLE_KEY_BAN:
        translations = inferece_fn(yo_ban_ckpt, char_seq_list, yo_ban_hparams, yo_ban_hparams)
    else:
        raise RuntimeError("No target style is selected")

    trans_sents = []
    for (trans, char_seq) in zip(translations, char_seq_list):
        input_str = string_utils.remove_space(trans.decode("utf8"))
        ref_str = string_utils.remove_space(char_seq)
        trans_sents = string_utils.apply_copy_rules(input_str, ref_str)

    return trans_sents

def transfer_text(input_text, enable_bactrans=False, target_style=forms.STYLE_KEY_BAN):
    if not yo_ban_hparams:
        raise RuntimeError("hparams should be loaded first")

    sents = string_utils.split_sentences(sents_splitter, input_text)
    trans_sents = forward_transfer(sents, target_style)

    final_results = []

    for (sent, trans_sent) in zip(sents, trans_sents):
        final_results.append({'input_sent':sent, 'transfer_sent': trans_sent, 'is_transferred': True})

    final_results = [result['transfer_sent'] for result in final_results]
    output_text_newline, output_text_space = string_utils.join_results(final_sents)

    return output_text_newline, output_text_space, final_results

