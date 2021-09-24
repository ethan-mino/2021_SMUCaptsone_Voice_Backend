# _*_ coding: utf8 _*_
import tensorflow as tf
from webdemo.nmt.utils import misc_utils as utils
from webdemo.nmt import inference
import webdemo.string_utils as string_utils
import webdemo.forms as forms
from koalanlp import *
from koalanlp.api.tagger import SentenceSplitter

# 해요체 -> 합쇼체
yo_sho_model_dir = "/home/hkh/sources/adc-chatbot/models/char_yo_sho_full"
sho_yo_model_dir = "/home/hkh/sources/adc-chatbot/models/char_sho_yo_full"

# 해요체 -> 반말체
yo_ban_model_dir = "/home/hkh/sources/adc-chatbot/models/char_yo_ban"
ban_yo_model_dir = "/home/hkh/sources/adc-chatbot/models/char_ban_yo"

print("Initializing Sentence Splitter modules")

initialize(packages=[API.KKMA, API.EUNJEON, API.TWITTER], version="1.9.2", java_options="-Xmx4g -Dfile.encoding=utf-8")
sents_splitter = SentenceSplitter(splitter_type=API.TWITTER)

print("Loading tensorflow saved models...")
inference_fn = inference.inference

# Load hparams.
yo_sho_hparams = utils.load_hparams(yo_sho_model_dir)
sho_yo_hparams = utils.load_hparams(sho_yo_model_dir)
yo_ban_hparams = utils.load_hparams(yo_ban_model_dir)
ban_yo_hparams = utils.load_hparams(ban_yo_model_dir)

# Inference
yo_sho_ckpt = tf.train.latest_checkpoint(yo_sho_model_dir)
sho_yo_ckpt = tf.train.latest_checkpoint(sho_yo_model_dir)
yo_ban_ckpt = tf.train.latest_checkpoint(yo_ban_model_dir)
ban_yo_ckpt = tf.train.latest_checkpoint(ban_yo_model_dir)


# Forward 변환
def forward_transfer(sents, target_style):
  char_seq_list = []
  for sent in sents:
    char_seq = string_utils.convert_chars(sent)
    char_seq_list += [char_seq]

  if target_style == forms.STYLE_KEY_SHO:
    # yo -> sho transfer
    translations = inference_fn(yo_sho_ckpt, char_seq_list, yo_sho_hparams)
  elif target_style == forms.STYLE_KEY_BAN:
    # yo -> ban transfer
    translations = inference_fn(yo_ban_ckpt, char_seq_list, yo_ban_hparams)
  else:
    raise RuntimeError("No target style is selected!")

  # post-processing
  trans_sents = []
  for (trans, char_seq) in zip(translations, char_seq_list):
    input_str = string_utils.remove_space(trans.decode("utf8"))
    ref_str = string_utils.remove_space(char_seq)
    trans_sents = string_utils.apply_copy_rules(input_str, ref_str)

  return trans_sents


# 역-변환
def backward_transfer(trans_sents, target_style):
  char_seq_list = []
  for sent in trans_sents:
    char_seq = string_utils.convert_chars(sent)
    char_seq_list += [char_seq]

  if target_style == forms.STYLE_KEY_SHO:
    # sho -> yo transfer
    translations = inference_fn(sho_yo_ckpt, char_seq_list, sho_yo_hparams)
  elif target_style == forms.STYLE_KEY_BAN:
    # ban -> yo transfer
    translations = inference_fn(ban_yo_ckpt, char_seq_list, ban_yo_hparams)
  else:
    raise RuntimeError("No target style is selected!")

  # post-processing
  trans_sents = []
  for (trans, char_seq) in zip(translations, char_seq_list):
    input_str = string_utils.remove_space(trans.decode("utf8"))
    ref_str = string_utils.remove_space(char_seq)
    trans_sents = string_utils.apply_copy_rules(input_str, ref_str)

  return trans_sents


# 어체 변환 함수
def transfer_text(input_text, enable_backtrans=False, target_style=forms.STYLE_KEY_SHO):
  if not yo_sho_hparams or not sho_yo_hparams:
    raise RuntimeError("hparams should be loaded first!")
  if not yo_ban_hparams or not ban_yo_hparams:
    raise RuntimeError("hparams should be loaded first!")

  sents = string_utils.split_sentences(sents_splitter, input_text)
  trans_sents = forward_transfer(sents, target_style)

  final_results = []
  if enable_backtrans:
    # back-translation 적용
    backtrans_sents = backward_transfer(trans_sents, target_style)
    for (sent, trans_sent, backtrans_sent) in zip(sents, trans_sents, backtrans_sents):
      if sent == backtrans_sent:
        final_results.append({'input_sent': sent, 'tranfer_sent': trans_sent, 'is_transferred': True})
      else:
        final_results.append({'input_sent': sent, 'tranfer_sent': sent, 'is_transferred': False})
  else:
    # back-translation 적용 안함
    for (sent, trans_sent) in zip(sents, trans_sents):
      final_results.append({'input_sent': sent, 'tranfer_sent': trans_sent, 'is_transferred': True})

  final_sents = [result['tranfer_sent'] for result in final_results]
  output_text_newline, output_text_space = string_utils.join_results(final_sents)

  return output_text_newline, output_text_space, final_results
