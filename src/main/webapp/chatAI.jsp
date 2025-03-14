<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Chat với Gemini AI</title>
        <style>
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
                background-color: #f37015;
                color: white;
                border: none;
                border-radius: 5px;
                cursor: pointer;
            }
            button.ai-button:hover {
                background-color: #0056b3;
            }
            .typing {
                display: inline-block;
                font-size: 16px;
                color: #666;
                margin: 5px 0;
            }

            .typing span {
                display: inline-block;
                animation: blink 1.5s infinite;
            }

            .typing span:nth-child(1) {
                animation-delay: 0s;
            }
            .typing span:nth-child(2) {
                animation-delay: 0.3s;
            }
            .typing span:nth-child(3) {
                animation-delay: 0.6s;
            }

            @keyframes blink {
                0% {
                    opacity: 0.3;
                }
                50% {
                    opacity: 1;
                }
                100% {
                    opacity: 0.3;
                }
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

                // Hiển thị tin nhắn người dùng
                chatBox.innerHTML += "<div class='message user'><b>Bạn:</b> " + userMessage + "</div><br style='clear:both;'>";

                // Hiển thị hiệu ứng "đang gõ..."
                var typingIndicator = document.createElement("div");
                typingIndicator.className = "typing bot";
                typingIndicator.id = "typingIndicator";
                typingIndicator.innerHTML = "<span>.</span><span>.</span><span>.</span>";
                chatBox.appendChild(typingIndicator);

                // Cuộn xuống cuối
                chatBox.scrollTop = chatBox.scrollHeight;

                // Gửi request đến Servlet
                fetch("ChatServletAI", {
                    method: "POST",
                    headers: {"Content-Type": "application/x-www-form-urlencoded"},
                    body: "message=" + encodeURIComponent(userMessage)
                })
                        .then(response => response.json())
                        .then(data => {
                            // Xóa dấu ba chấm "đang gõ..."
                            document.getElementById("typingIndicator").remove();

                            // Hiển thị phản hồi từ chatbot
                            chatBox.innerHTML += "<div class='message bot'><b>AI:</b> " + data.response + "</div><br style='clear:both;'>";

                            // Xóa ô nhập tin nhắn
                            document.getElementById("userMessage").value = "";

                            // Cuộn xuống cuối
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
