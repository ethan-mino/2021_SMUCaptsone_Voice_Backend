import tensorflow as tf
from webdemo.webdemo.nmt.utils import misc_utils as utils
from webdemo.webdemo.nmt import inference
import webdemo.webdemo.string_utils as string_utils
import webdemo.webdemo.forms as forms
import warnings
warnings.filterwarnings(action='ignore')
#from koalanlp.Util import initialize, finalize
#from koalanlp.proc import SentenceSplitter
#from koalanlp import API as API
import sys
sys.path.append('/home/ubuntu/urvoice/2021_SMUCaptsone_Voice_Backend/transfer-django/transfer_django/transferapp/')

from transferapp.views import sents_splitter
yo_ban_model_dir = "/home/ubuntu/urvoice/2021_SMUCaptsone_Voice_Backend/transfer/models/char_yo_ban"

print("Initializing Sentence Splitter module")
print("Loading tensorflow saved models")

SPACE_SYMBOL = "▁" 
SENT_BOUND_NEWLINE_SYMBOL = "\n"
SENT_BOUND_SPACE_SYMBOL = " "
inference_fn = inference.inference

yo_ban_hparams = utils.load_hparams(yo_ban_model_dir)

yo_ban_ckpt = tf.train.latest_checkpoint(yo_ban_model_dir)

def forward_transfer(sents, unk_copy=True):
    char_seq_list=[]
    for sent in sents:
        char_seq = string_utils.convert_chars(sent)
        char_seq_list += [char_seq]

    translations = inference_fn(yo_ban_ckpt, char_seq_list, yo_ban_hparams)
     

    trans_sents = []
    for (trans, char_seq) in zip(translations, char_seq_list):
        input_str = string_utils.remove_space(trans.decode("utf8"))
        ref_str = string_utils.remove_space(char_seq)

        if unk_copy:
            input_str = string_utils.apply_left_right_rule(input_str, ref_str)
            input_str = string_utils.apply_double_left_rule(input_str, ref_str)
            input_str = string_utils.apply_double_right_rule(input_str, ref_str)

        trans_sents.append(input_str.replace(SPACE_SYMBOL, " "))

    return trans_sents

def transfer_text(input_text, modeId):
    if not yo_ban_hparams:
        raise RuntimeError("hparams should be loaded first")

    sents = sents_splitter(input_text)
    # sents = string_utils.split_sentences(sents_splitter, input_text)
    trans_sents = forward_transfer(sents)

    final_results = []
    if modeId=='0':
        for sent in sents:
            final_results.append({'transfer_sent': sent})
    elif modeId=='2':
        for trans_sent in trans_sents:
            final_results.append({'transfer_sent':trans_sent})

    final_sents = [result['transfer_sent'] for result in final_results]
    
    output_text_newline = SENT_BOUND_NEWLINE_SYMBOL.join(final_sents)
    output_text_space = SENT_BOUND_SPACE_SYMBOL.join(final_sents)
    # output_text_newline, output_text_space = string_utils.join_results(final_sents)

    return output_text_space





