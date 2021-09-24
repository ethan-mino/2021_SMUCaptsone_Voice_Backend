from django.shortcuts import render
from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from rest_framework.parsers import JSONParser
import json

@csrf_exempt
def transfer_service(request):
    if request.method == 'POST':
        input1 = JSONParser().parse(request) 
        input1 = input1['input']
        response = transfer_function(input1) # 함수이름입력
        output = dict()
        output['response'] = response
        return JsonResponse((output), status=200)
    else:
        JsonResponse(status=400)



