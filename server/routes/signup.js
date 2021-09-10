const express = require('express')
const router = express.Router()
const db = require('../config/db')

// db 보안
const bcrypt = require('bcrypt')
const saltRounds = 10

// 회원가입
router.post('/signup', (req, res) => {
    var param = [req.body.id, req.body.password]

    // id 중복 확인 코드 (명세서에 없어서 일단 주석처리)
    // db.query('select * from urvoice where id=?',[req.body.id], (err, data) => {
    //     if(data.length == 0) {
    //     }
    // })

    // 비밀번호 암호화
    bcrypt.hash(param[1], saltRounds, (error, hash) => {
        param[1] = hash
        // 아이디와 비밀번호 db에 추가
        db.query('INSERT INTO user(`id`,`password`) VALUES (?,?)', param, (err, row) => {
            if(err) return res.json({success: false, err})
            res.status(200).json({success: true})
            // console.log('회원가입 성공')
        })
    })
})


module.exports = router