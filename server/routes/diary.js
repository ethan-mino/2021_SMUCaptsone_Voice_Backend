const express = require('express')
const router = express.Router()
const db = require('../config/db')
const multer = require("multer");
const path = require("path");

var storage = multer.diskStorage({
    destination: function (req, file, cb) {
      cb(null, "assests/save/");
    },
    filename: function (req, file, cb) {
      const ext = path.extname(file.originalname);
      cb(null, path.basename(file.originalname, ext) + "-" + Date.now() + ext);
    },
  });

var upload = multer({ storage: storage}).single("emoji")

router.get('/list', (req, res) => {
    db.query('SELECT * FROM diary', (err, row) => {
        if(err) console.log(err);
        res.status(200).json({list: row})
    })
})

// 글쓰기
router.post('/write', upload, (req, res) => {
    const emoji = `/save/${req.file.filename}`;
    const param = [req.body.diary_id, req.body.emojiText, emoji, req.body.content, req.body.user_id]

    db.query('INSERT INTO diary(`diary_id`,`date`, `emojiText`, `emoji`, `content`, `user_id`) VALUES (?,now(),?,?,?,?)', param, (err, row) => {
        if(err) return res.json({diarysuccess: false, err})
        res.status(200).json({diarysuccess: true})
        // console.log('글쓰기 성공')
    })
})

module.exports = router