const express = require('express')
const session = require('express-session')
const app = express()
const bodyParser = require('body-parser')
const cors = require('cors')
const cookieParser = require('cookie-parser')
const db = require('./config/db')
const path = require('path');
const port = process.env.PORT || 3350

app.use(bodyParser.urlencoded({
    extended: false
}))
app.use(bodyParser.json());

// 쿠키 추출
app.use(cookieParser())

app.use(session({
    key: "user", // 저장될 키 값
    secret: "secret", // 서명에 필요한 값
    resave: false, // 수정이 되지 않아도 재저장 여부
    saveUninitialized: false, 
    cookie: { // 쿠키 지속
        maxAge: 60*60*24
    },
}))

// 기본 path
app.use(express.static(__dirname + '/public'))

// use routes
app.use("/api", require('./routes/main'))
app.use("/api", require('./routes/signup'))
app.use("/api", require('./routes/login'))

app.listen(port, () => {
    console.log(`Server Listening on ${port}`)
});


// app.set('view engine', 'ejs'); //'ejs'탬플릿을 엔진으로 한다.
// app.set('views', path.join(__dirname, 'views')); //폴더, 폴더경로 지정

// access control allow origin   
// app.use(cors({
//     origin: true, // - origin: 도메인 모두 허용
//     credentials: true // - credentials: 접근 통제 허용
// }))
