const express = require('express')
const router = express.Router()
const db = require('../config/db')
const multer = require("multer");
const path = require("path");
const bodyParser = require('body-parser');
const fs = require("fs")

const settings = require("../config/settings")

const downloads = express.Router();
downloads.use(bodyParser.json());

downloads.post("/", async function(req, res){ // (https://luckyyowu.tistory.com/346 req, res 관련 참조)
    const downloads_path = settings.WAVS_PATH;

    const file_path = downloads_path +  "/" + req.body.file_name;
    let file_name = path.basename(file_path);

    console.log(file_name + " 파일 요청!")

    let is_exist = fs.existsSync(file_path)   // 해당 파일이 downloads 디렉토리에 존재하는지 확인.

    if(is_exist === false){  // 해당 파일이 downloads 디렉토리에 존재하지 않으면 ERR_FILE_NOT_EXIST 파일을 보내고, 클라이언트에서는 파일명이 "ERR_FILE_NOT_EXIST"일 때 alert를 띄움.(responseType이 arraybuffer일 때는 responseText를 사용할 수 없기 때문에 실제로 파일을 보내는 대신 파일명을 달리하여, 에러 발생 사실을 클라이언트에 알림.)
        file_name = "ERR_FILE_NOT_EXIST";
    }

    const readStream = fs.createReadStream(file_path);

    let encoded_file_name = encodeURI(file_name); // Content-Disposition에 한글을 사용하면 오류가 발생해서 인코딩된 문자열을 사용함.

    res.set({  
        "Content-Type" : 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
        "Content-Disposition" : "attachment; filename=" + encoded_file_name
    }); // res.setHeader(), res.header() 차이 - https://stackoverflow.com/questions/40840852/difference-between-res-setheader-and-res-header-in-node-js참조 (헤더를 여러개 지정할 수 있느냐 없느냐 차이인듯.)

    try{    // 요청한 파일을 전송한 다음에 파일을 삭제하기 위해 Promise를 사용함 (카카오톡 사진 참조)
        await new Promise(function(resolve, reject){
            readStream  // 출력스트림.pipe(입력스트림)
                .on('Error', Error) // ReadStream
                .pipe(res)
                .on('Error', Error) // WriteStream
                .on('finish', finish);  // WriteStream
            
            function finish(){
                console.log(`${file_name} 전송 완료`);   // 전송 완료되면 resolve
                return resolve();
            }
            
            function Error(err){
                return reject(err);
            }
        })
    }catch(err){
        console.log(err)
    }
    
    /*
    if(file_name !== "ERR_FILE_NOT_EXIST"){
        fs.unlink(file_path, err => { // 요청한 파일을 전송한 뒤 파일을 삭제함.
            if(err){
                console.log(`${file_name} 파일 삭제 실패!`)
            }
            else{
                console.log(`${file_name} 파일 삭제 완료!`)
            }
        }); 
    }*/
});


module.exports = downloads;