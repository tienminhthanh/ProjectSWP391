<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Verify Email</title>
    <style>
        #whale {
            position: absolute;
            width: 100px; /* Kích thước cá voi */
            height: 100px;
            background: url('whale.png') no-repeat center center; /* Hình ảnh cá voi */
            background-size: contain;
            top: 50%;
            left: 50%;
            transition: transform 0.1s linear; /* Hiệu ứng mượt */
        }
    </style>
</head>
<body>
    <h2>Verify Email</h2>
    <form action="emailAuthentication" method="post">
        <label for="otp">Enter the OTP sent to your email:</label>
        <input type="text" name="otp" id="otp" required/><br/><br/>
        <button type="submit">Verify</button>
    </form>

    <%-- Hiển thị thông báo từ servlet --%>
    <%
        String message = (String) request.getAttribute("message");
        if (message != null) {
    %>
    <p style="color:red;"><%= message %></p>
    <% } %>

    <!-- Phần tử đại diện cho con cá voi -->
    <div id="whale"></div>

    <script>
        var whale = document.getElementById("whale");

        // Kiểm tra nếu tìm thấy phần tử whale
        if (whale) {
            document.addEventListener("mousemove", function(event) {
                var x = event.clientX;
                var y = event.clientY;
                
                // Cập nhật vị trí của cá voi theo con chuột
                whale.style.transform = `translate(${x}px, ${y}px)`;
            });
        } else {
            console.error("Không tìm thấy phần tử #whale!");
        }
    </script>
</body>
</html>
