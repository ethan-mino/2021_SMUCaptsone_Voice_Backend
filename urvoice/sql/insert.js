const request = require("request")
const iconv  = require('iconv-lite');
const cheerio = require("cheerio");

const headers = {
    'Authorization' : `BearereyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1cnZvaWNlIiwiZXhwIjoxNjMxNTU5MTAxfQ.yKyK5sbr5yeSBQlU4IV9KNKyS7Oy92G-kXYGdkpVBvwmsviTDr2byb96gI3ERkQkjf7xJG7OGtKdAPJ_0dEGvw`,
    'Content-Type' : `application/json`
}

function getRandomIntInclusive(min, max) {
    min = Math.ceil(min);
    max = Math.floor(max);
    return Math.floor(Math.random() * (max - min + 1)) + min; //최댓값도 포함, 최솟값도 포함
}

function requestInit(body, format, encoding, caller){
    const strContents = Buffer.from(body);
    body = iconv.decode(strContents, encoding).toString()

    const $ = cheerio.load(body);   // cheerio 객체를 반환

    if(format === "JSON"){ // format이 JSON인 경우 Error를 throw
        try{
            body = JSON.parse(body)
            return body;    // 형식이 JSON일 때는 cheerio 객체가 필요 없으므로 parsing된 body만 반환
        }
        catch(err){ // body는 json 형식의 string임. string을 json으로 변환시켜서 요소에 접근이 쉽도록 함. (body가 JSON 형식이 아닌 경우 Error를 throw)
            // 안심번호를 추출할 때 비정상적인 트래픽을 감지하여 서비스 이용이 일시 제한 될 수 있기 때문에, Temporary Redirect로 인해 안심번호를 포함하는 JSON 형식이 아닌 HTML이 넘어올 수 있어서 error가 발생할 수 있음 (Error가 발생했을 때, html에는 http://guard.naver.com/NetworkBlock.nhn이라는 url이 포함되어 있음)
            throw new Error(`Fail to Parse To Json(${caller})`)  // error 출력 확인 완료
        } 
    }else{
        return [$, body]    // 형식이 JSON이 아닐 때는 parsing 되지 않은 body와 cheerio 객체를 반환
    }
}

const m20 = [31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]
const m21 = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]

for (i = 0; i < 1000; i++){
    const year = getRandomIntInclusive(2020, 2021)
    
    const emoji = getRandomIntInclusive(1, 5)

    let date, month;

    if (year === 2020) {
        month = getRandomIntInclusive(1, 12)
        date = getRandomIntInclusive(0, m20[month - 1])
    }
    else {
        month = getRandomIntInclusive(1, 8)
        date = getRandomIntInclusive(0, m21[month - 1])
    }

    const dateObj = new Date()
    dateObj.setFullYear(year)
    dateObj.setMonth(month)
    dateObj.setDate(date)
    
    const getMethodOption = {    // request 방식이 "GET"인 경우 option의 url만 변경하여 사용
        method: 'POST',
        url : "http://localhost:8080/diary",    //
        headers : headers,
        body : JSON.stringify({
            "content" : "",
            "emojiId" : `${emoji}`,
            "createDate" : dateObj
        }),
        encoding: null // 해당 값을 null로 해주어야 제대로 iconv가 제대로 decode 해준다.
    }

    request(getMethodOption, async function(err, res, body){
        try{conv_body = requestInit(body, "JSON", "UTF-8", "INSERT");}catch(err){return reject(err)} 
        console.log(conv_body)
    })
}

