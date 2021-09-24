import json
from rest_framework import status
from rest_framework.decorators import api_view
from rest_framework.response import Response
from django.shortcuts import render
from webdemo.forms import StyleTransferForm
from webdemo.analyzer import transfer_text


def style_transfer(request):
  if request.method == "POST":
    form = StyleTransferForm(request.POST)
    if form.is_valid():
      input_text = form.cleaned_data['input_text']
      enable_backtrans = form.cleaned_data["enable_backtrans"]
      target_style = form.cleaned_data["target_style"]

      output_text, _, _ = transfer_text(input_text, enable_backtrans, target_style)
      result = {"result_text": output_text}
      print("변환 결과:", result["result_text"])

      return render(request, 'home.html', {'form': form, 'result': result})
    else:
      result = {"result_text": "Error: Form is not valid!"}
      return render(request, 'home.html', {'form': form, 'result': result})
  else:
    form = StyleTransferForm()
    print("start")

  return render(request, 'home.html', {'form': form})


@api_view(['POST'])
def transfer_api(request):
  if request.method == 'POST':
    input_text = request.data["input_text"]
    enable_backtrans = request.data["enable_backtrans"]
    target_style = request.data["target_style"]

    try:
      output_text_newline, output_text_space, final_results = transfer_text(input_text, enable_backtrans, target_style)
      sents_str = json.dumps(final_results, ensure_ascii=False).replace("\"", "'")
      result = {'result_text_newline': output_text_newline,
                'result_text_space': output_text_space,
                'details': sents_str}
      return Response(result, status=status.HTTP_200_OK)

    except:
      return Response(status=status.HTTP_500_INTERNAL_SERVER_ERROR)

  else:
    return Response(status=status.HTTP_400_BAD_REQUEST)
