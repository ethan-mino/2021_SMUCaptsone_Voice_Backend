const mysql = require('mysql')

const db = mysql.createConnection({
    // aws 용 
    // host: 'urvoice.cir16abmh0e7.us-east-2.rds.amazonaws.com',
    // user: 'admin',
    // password: 'teamproject',
    // port: 3307,
    // database: 'urvoice'

    // local test 용 - heejin
    host: 'localhost',
    user: 'root',
    password: '',
    port: 3306,
    database: 'urvoice'
})

db.connect();

// MySQL Terminal -> DB 생성 create table
// CREATE TABLE urvoice (
//     id varchar(50) NOT NULL,
//     password varchar(100) NOT NULL,
//     PRIMARY KEY (id)
// );

module.exports = db;