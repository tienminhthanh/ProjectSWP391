<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Verify Email</title>
</head>
<body>
    <h2>Verify Email</h2>
    <form action="emailAuthentication" method="post">
        <label for="otp">Enter the OTP sent to your email:</label>
        <input type="text" name="otp" id="otp" required/><br/><br/>
        <button type="submit">Verify</button>
    </form>

    <% 
        // Check if any message was set by the servlet and display it
        String message = (String) request.getAttribute("message");
        if (message != null) {
    %>
        <p style="color:red;"><%= message %></p>
    <% } %>
</body>
</html>
