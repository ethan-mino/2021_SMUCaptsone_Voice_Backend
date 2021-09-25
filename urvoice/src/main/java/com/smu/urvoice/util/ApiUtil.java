package com.smu.urvoice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.json.XML;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import static org.yaml.snakeyaml.util.UriEncoder.encode;

public class ApiUtil {
    public static Map getResultByResponse(HttpURLConnection conn) throws IOException {
        BufferedReader bufferedReader;

        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            bufferedReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuffer stringBuffer = new StringBuffer();
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            stringBuffer.append(line);
        }

        bufferedReader.close();
        conn.disconnect();

        int code = conn.getResponseCode();
        String result = stringBuffer.toString();
        Map<String, String> resultMap = new HashMap<>();

        resultMap.put("code", Integer.toString(code));
        resultMap.put("result", result);
        return resultMap;
    }

    public static Map sendRequest(String method, String url, Map<String, Object> params) throws IOException {	// API로부터 xml 형식으로 응답받는 경우 response에서 item를 추출하여 반환
        HttpURLConnection conn;

        if (method.equals("GET")) {
            StringBuilder getBuilder = new StringBuilder(url);

            int index = 0;

            for (Map.Entry<String, Object> param : params.entrySet()){
                if (index == 0) getBuilder.append('?'); else getBuilder.append('&');
                getBuilder.append(encode(param.getKey()) + "=" + encode(param.getValue().toString()));
                index++;
            }

            conn = (HttpURLConnection) new URL(getBuilder.toString()).openConnection();

            conn.setRequestMethod(method);
        }else if (method.equals("POST")) {
            conn = null;
        }else {
            conn = null;
        }

        return getResultByResponse(conn);	// API 요청
    }

    public static Map<String, Object> jsonToMap(String jsonStr) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonStr, Map.class);
    }

    public static String xmlToJson(String xmlString, boolean keepStrings){
        JSONObject xmlJSONObj = XML.toJSONObject(xmlString, keepStrings);
        return xmlJSONObj.toString(4);
    }

    public static Object mapToDto(Map<String, Object> map, Class<?> classType){	// https://zorba91.tistory.com/28 참조
        final ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
        return mapper.convertValue(map, classType);
    }
}
