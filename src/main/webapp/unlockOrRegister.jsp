<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Xử lý tài khoản bị khóa</title>
    </head>
    <body>
        <h2>Tài khoản của bạn đã bị khóa</h2>
        <p>${message}</p>



        <a href="emailUnlock" type="submit">Mở khóa tài khoản (Xác thực lại email)</a>

        <a href="removeEmailFromLockedAccount" type="submit">Xóa email khỏi tài khoản cũ</a>
       
    </body>
</html>
