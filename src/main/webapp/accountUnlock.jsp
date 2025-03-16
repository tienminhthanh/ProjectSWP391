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
        <title>Unlock Account</title>
    </head>
    <body>
        <h2>Unlock Your Account</h2>
        <form action="emailUnlock" method="get">
            <label for="email">Enter your registered email:</label>
            <input type="text" id="email" name="email" required>
            <button type="submit">Unlock Account</button>
        </form>
        <br>
        <% 
            String message = (String) request.getAttribute("message");
            if (message != null) {
        %>
            <div style="color: red;"><%= message %></div>
        <% } %>
    </body>
</html>
