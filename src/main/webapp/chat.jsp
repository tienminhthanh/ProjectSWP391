<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Chat</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
        <style>
            .chat-container {
                width: 45%;
                height: 80%;
                position: fixed;
                bottom: 10px;
                right: 10px;
                display: none;
                background: white;
                box-shadow: -2px -2px 15px rgba(0,0,0,0.1);
                border-radius: 10px 10px 0 0;
                z-index: 1000;
            }
            .chat-container.show {
                display: flex;
            }
            .chat-container h2 {
                text-align: center;
                display: block;
                margin: 0 auto;
                width: 100%;
            }

            .chat-input {
                width: 100%;
                padding: 10px;
                border-top: 1px solid #eee;
            }
            .chat-container {
                border-radius: 12px; /* Bo góc 12px */
                overflow: hidden; /* Đảm bảo nội dung bên trong không bị tràn */
            }

            @media (max-width: 768px) {
                .chat-container {
                    width: 100%;
                    height: 90%;
                }
            }
        </style>
    </head>
    <body>
        <div class="fixed bottom-4 right-4 z-50">
            <button id="chatButton" class="bg-yellow-500 text-white p-2 rounded-full shadow-lg">
                <i class="fas fa-comments"></i>
                <span class="ml-1">Chat</span>
            </button>
        </div>

        <div id="chatContainer" class="chat-container flex flex-col">
            <div class="bg-orange-500 p-1 border-b flex justify-between items-center">
                <h2 class="text-white font-bold">Chat with WiBook</h2>
                <button id="closeChat" class="text-white text-xl mr-2">x</button>
            </div>

            <div id="chatMessages" class="flex-1 p-4 overflow-y-auto bg-gray-100">
                <c:choose>
                    <c:when test="${empty chats}">
                        <p class="text-center text-gray-500">No messages yet.</p>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="chat" items="${chats}">
                            <div class="mb-4 ${chat.senderID == sessionScope.account.accountID ? 'text-right' : 'text-left'}">
                                <div class="inline-block p-3 rounded-lg ${chat.senderID == sessionScope.account.accountID ? 'bg-orange-500 text-white text-left' : 'bg-white'}">
                                    <p>${chat.messageContent}</p>
                                    <span class="text-xs">
                                        <fmt:formatDate value="${chat.sentAt}" pattern="HH:mm"/>
                                    </span>
                                </div>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="chat-input">
                <div class="flex">
                    <input type="text" id="messageContent" class="flex-1 p-2 border rounded-l-lg" 
                           placeholder="Enter message..." required>
                    <button id="sendMessage" class="bg-orange-500 text-white p-2 rounded-r-lg">
                        <i class="fas fa-paper-plane"></i>
                    </button>
                </div>
            </div>
        </div>

        <script>
            const contextPath = '<%= request.getContextPath()%>';

            // Open/close chat when clicking the chat button
            document.getElementById('chatButton').addEventListener('click', (e) => {
                e.stopPropagation();
                document.getElementById('chatContainer').classList.toggle('show');
                loadMessages();
            });

            // Close chat when clicking the close button
            document.getElementById('closeChat').addEventListener('click', (e) => {
                e.stopPropagation();
                document.getElementById('chatContainer').classList.remove('show');
            });

            // Send message when clicking the send button
            document.getElementById('sendMessage').addEventListener('click', function (e) {
                e.preventDefault();
                sendMessage();
            });

            // Send message when pressing Enter
            document.getElementById('messageContent').addEventListener('keypress', function (e) {
                if (e.key === 'Enter') {
                    e.preventDefault();
                    sendMessage();
                }
            });

            // Close chat when clicking outside
            document.addEventListener('click', (e) => {
                const chatContainer = document.getElementById('chatContainer');
                const chatButton = document.getElementById('chatButton');
                if (!chatContainer.contains(e.target) && e.target !== chatButton && chatContainer.classList.contains('show')) {
                    chatContainer.classList.remove('show');
                }
            });

            // Function to send message
            function sendMessage() {
                const messageContent = document.getElementById('messageContent').value.trim();
                if (!messageContent) {
                    alert('Please enter a message!');
                    return;
                }

                const data = new URLSearchParams();
                data.append('messageContent', messageContent);

                fetch(`${contextPath}/chat`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: data
                })
                        .then(response => {
                            if (!response.ok)
                                throw new Error('Network response was not ok');
                            return response.json();
                        })
                        .then(data => {
                            if (data.success) {
                                document.getElementById('messageContent').value = '';
                                loadMessages();
                            } else {
                                alert(data.message || 'Unable to send message!');
                            }
                        })
                        .catch(error => {
                            console.error('Error:', error);
                            alert('An error occurred while sending the message!');
                        });
            }

            // Function to scroll to the last message
            function scrollToBottom() {
                const chatMessages = document.getElementById('chatMessages');
                if (chatMessages) {
                    chatMessages.scrollTop = chatMessages.scrollHeight;
                }
            }

            // Function to load messages and scroll to the bottom
            function loadMessages() {
                fetch(`${contextPath}/chat`, {
                    method: 'GET'
                })
                        .then(response => response.text())
                        .then(data => {
                            const parser = new DOMParser();
                            const doc = parser.parseFromString(data, 'text/html');
                            const newMessages = doc.querySelector('#chatMessages');
                            if (newMessages) {
                                document.getElementById('chatMessages').innerHTML = newMessages.innerHTML;
                                scrollToBottom();
                            }
                        })
                        .catch(error => console.error('Error loading messages:', error));
            }
        </script>
    </body>
</html>
