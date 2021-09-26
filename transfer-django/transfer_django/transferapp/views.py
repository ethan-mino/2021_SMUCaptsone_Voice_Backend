from django.shortcuts import render
from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from rest_framework.parsers import JSONParser
import json

from koalanlp.Util import initialize, finalize
from koalanlp.proc import SentenceSplitter
from koalanlp import API as API
import tensorflow

print(tensorflow.__version__)

finalize()
initialize(java_options="-Xmx4g -Dfile.encoding=utf-8", OKT="2.02")
sents_splitter = SentenceSplitter(API.OKT)

import sys
sys.path.append('/home/ubuntu/urvoice/2021_SMUCaptsone_Voice_Backend/transfer')
#from main import transfer_text
import main
@csrf_exempt
def transfer_service(request):
    if request.method == 'GET':
        input1 = request.GET['inputText']
        mode1 = request.GET['modeId']
        response = main.transfer_text(input1, mode1) # 함수이름입력
        output = dict()
        output['response'] = response
      #  main.finalize()
        return JsonResponse((output), json_dumps_params={'ensure_ascii':False}, status=200)
    else:
        JsonResponse(status=400)


