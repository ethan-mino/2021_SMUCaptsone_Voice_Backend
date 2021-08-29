const express = require('express')
const router = express.Router()
const db = require('../config/db')

// db 보안
const bcrypt = require('bcrypt')
const saltRounds = 10

// 보기 쉽게 ejs 를 이용해 확인하느라 생성 - 필요 없음
// router.get('/login', (req, res) => {
//     res.render('login.ejs', {user:req.session})
// })

// 로그인
router.post('/login', (req, res) => {
    const param = [req.body.id, req.body.password]

    db.query('SELECT * FROM urvoice WHERE id=?', param[0], (err, row) => {
        if(err) return res.json({success: false, err})
        if(row.length > 0) { // ID exists
            bcrypt.compare(param[1], row[0].password, (error, result) => {
                // 비밀번호 불일치
                if(!result) {
                    return res.json({
                        loginSuccess: false, 
                        message: 'Wrong pw'
                    })
                }
                // 비밀번호 일치
                req.session.loggedin = true;
                req.session.id = param[0]
                res.status(200).json({loginSuccess: true})
                // res.redirect('/api')    
            })
        } else { // ID not exists
            return res.json({loginSuccess: false, message: 'ID not exists'})
        }
    })
})

router.get('/logout', (req, res) => {
    req.session.loggedin = false
    req.session.destroy((err) => {
        // req.session
        console.log('세션삭제')
    })
    res.status(200).json({logoutSucess: true})
    // res.redirect('/api')
})

module.exports = router