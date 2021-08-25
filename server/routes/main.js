const express = require('express')
const router = express.Router()

// main router
// ejs를 이용해 확인했습니다.
router.get('/', (req,res) => {
    res.render('main.ejs')
})

module.exports = router