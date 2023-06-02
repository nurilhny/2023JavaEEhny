module.exports = {

    devServer: {
        proxy:'http://localhost:8080/worktwo/',
        host: 'localhost',
        port: 8181, // 端口
    }
    ,
    lintOnSave:false
}
