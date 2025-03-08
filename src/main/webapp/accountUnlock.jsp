<%-- 
    Document   : accountUnlock
    Created on : 8 Mar 2025, 17:10:30
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <form action="unlockAccount" method="get">
    <label for="username">Nhap mail cua ban:</label>
    <input type="text" id="username" name="email" required>
    <button type="submit">Mở khóa</button>
</form>

    </body>
</html>
