<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Chat với Gemini AI</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                text-align: center;
            }
            #chatContainer1 {
                width: 100%;
                margin: auto;
            }
            #chatBox {
                width: 100%;
                height: 400px;
                overflow-y: auto;
                border: 1px solid #ccc;
                padding: 10px;
                text-align: left;
                background-color: #f9f9f9;
            }
            .message {
                margin: 5px 0;
                padding: 8px;
                border-radius: 8px;
                display: inline-block;
                max-width: 80%;
            }
            .user {
                background-color: #007bff;
                color: white;
                float: right;
            }
            .bot {
                background-color: #e0e0e0;
                color: black;
                float: left;
            }
            #inputContainer {
                margin-top: 10px;
                display: flex;
                justify-content: center;
            }
            input.ai-input {
                width: 70%;
                padding: 10px;
                font-size: 16px;
                border: 1px solid #ccc;
                border-radius: 5px;
            }
            button.ai-button {
                padding: 10px;
                font-size: 16px;
                background-color: #007bff;
                color: white;
                border: none;
                border-radius: 5px;
                cursor: pointer;
            }
            button.ai-button:hover {
                background-color: #0056b3;
            }
        </style>
        <script>
            function sendMessage() {
                var userMessage = document.getElementById("userMessage").value;
                if (userMessage.trim() === "") {
                    alert("Vui lòng nhập tin nhắn!");
                    return;
                }

                var chatBox = document.getElementById("chatBox");

                // Hiển thị tin nhắn người dùng trên giao diện
                chatBox.innerHTML += "<div class='message user'><b>Bạn:</b> " + userMessage + "</div><br style='clear:both;'>";

                // Gửi request đến Servlet
                fetch("ChatServlet", {
                    method: "POST",
                    headers: {"Content-Type": "application/x-www-form-urlencoded"},
                    body: "message=" + encodeURIComponent(userMessage)
                })
                        .then(response => response.json())
                        .then(data => {
                            // Hiển thị phản hồi từ Gemini AI
                            chatBox.innerHTML += "<div class='message bot'><b></b> " + data.response + "</div><br style='clear:both;'>";

                            // Xóa ô nhập tin nhắn
                            document.getElementById("userMessage").value = "";

                            // Cuộn xuống để xem tin nhắn mới nhất
                            chatBox.scrollTop = chatBox.scrollHeight;
                        })
                        .catch(error => console.error("Lỗi khi gửi yêu cầu:", error));
            }
        </script>
    </head>
    <body>
       
        <div id="chatContainer1">
            <div id="chatBox"></div>
            <div id="inputContainer">
                <input type="text" id="userMessage" class="ai-input" placeholder="Enter the message..." onkeypress="if (event.keyCode == 13)
                            sendMessage()">
                <button class="ai-button" onclick="sendMessage()">Send</button>
            </div>
        </div>
    </body>
</html>
