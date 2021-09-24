# -*- coding: utf-8 -*-
# Created by hkh at 2018-12-17
import re

SPACE_SYMBOL = "▁"
SENT_BOUND_NEWLINE_SYMBOL = "\n"
SENT_BOUND_SPACE_SYMBOL = " "


def convert_chars(sent):
  char_sequence = [SPACE_SYMBOL if ch == ' ' else ch for ch in sent.strip()]
  return " ".join(char_sequence)


def remove_space(string):
  return re.sub("\s+", "", string)


def apply_copy_rules(input_str, ref_str):
  trans_sents = []
  input_str = apply_left_right_rule(input_str, ref_str)
  input_str = apply_double_left_rule(input_str, ref_str)
  input_str = apply_double_right_rule(input_str, ref_str)
  trans_sents.append(input_str.replace(SPACE_SYMBOL, " "))
  return trans_sents


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


def split_sentences(sents_splitter, input_text):
  sents = sents_splitter.sentences(input_text.strip())
  return sents


def join_results(final_sents):
  output_text_newline = SENT_BOUND_NEWLINE_SYMBOL.join(final_sents)
  output_text_space = SENT_BOUND_SPACE_SYMBOL.join(final_sents)
  return output_text_newline, output_text_space