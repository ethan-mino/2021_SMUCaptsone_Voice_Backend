const mysql = require('mysql')

const db = mysql.createConnection({
    // aws
    host: '',
    user: '',
    password: '',
    port: 0,
    database: ''
})

db.connect();

module.exports = db;