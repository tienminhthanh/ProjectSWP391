<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Admin Chat - WIBOOKS</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"/>
        <link href="https://cdn.jsdelivr.net/npm/sweetalert2@11.6.9/dist/sweetalert2.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous"/>
       <link rel="stylesheet" href="css/styleAdminList.css">

    </head>
    <body class="bg-gray-50 min-h-screen flex">
        <!-- Thanh điều hướng bên trái -->
        <div class="w-64 bg-orange-400 text-white min-h-screen">
            <jsp:include page="navbarAdmin.jsp" flush="true"/>
        </div>

        <!-- Khu vực chat -->
        <div class="chat-container">
            <!-- Danh sách khách hàng -->
            <div class="customer-list">
                <h2>Customers</h2>
                <c:choose>
                    <c:when test="${empty customerIDs}">
                        <p class="text-center text-gray-500 p-4">No customers have chatted yet.</p>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="customerID" items="${customerIDs}">
                            <div class="customer-item ${customerID == selectedCustomerID ? 'active' : ''}">
                                <a href="${pageContext.request.contextPath}/chat?customerID=${customerID}">
                                    ${customerNames[customerID]}
                                </a>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Khu vực chat -->
            <div class="chat-area">
                <div class="chat-header">
                    <c:choose>
                        <c:when test="${selectedCustomerID != null}">
                            Chat with ${customerNames[selectedCustomerID]}
                        </c:when>
                        <c:otherwise>
                            Select a customer to start chatting
                        </c:otherwise>
                    </c:choose>
                </div>
                <div id="chatMessages" class="chat-messages">
                    <c:choose>
                        <c:when test="${empty chats}">
                            <p class="text-center text-gray-500">No messages yet.</p>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="chat" items="${chats}">
                                <div class="mb-4 ${chat.senderID == 1 ? 'text-right' : 'text-left'}">
                                    <div class="inline-block">
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
                    <div class="flex" id="inputContainer" ${selectedCustomerID == null ? 'style="display: none;"' : ''}>
                        <input type="text" id="messageContent" placeholder="Enter message..." required>
                        <button id="sendMessage">
                            <i class="fas fa-paper-plane"></i>
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <script>
            const contextPath = '<%= request.getContextPath()%>';
            let selectedCustomerID = '${selectedCustomerID}' || null;
            // Hàm cuộn tới tin nhắn cuối cùng
            function scrollToBottom() {
            const chatMessages = document.getElementById('chatMessages');
            chatMessages.scrollTop = chatMessages.scrollHeight;
            }

            // Hàm gửi tin nhắn
            function sendMessage() {
            const messageContent = document.getElementById('messageContent').value.trim();
            if (!messageContent) {
            Swal.fire({
            icon: 'warning',
                    title: 'Oops...',
                    text: 'Please enter a message!'
            });
            return;
            }

            const data = new URLSearchParams();
            data.append('messageContent', messageContent);
            data.append('customerID', selectedCustomerID);
            fetch(`${contextPath}/chat`, {
            method: 'POST',
                    headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: data
            })
                    .then(response => {
                    if (!response.ok)
                            throw new Error('Network response was not ok: ' + response.status);
                    return response.json();
                    })
                    .then(data => {
                    if (data.success) {
                    document.getElementById('messageContent').value = '';
                    fetch(`${contextPath}/chat?customerID=${selectedCustomerID}`, {
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
                                        });
                                } else {
                                Swal.fire({
                                icon: 'error',
                                        title: 'Error',
                                        text: data.message || 'Failed to send message!'
                                });
                                }
                                })
                                .catch(error => {
                                console.error('Error sending message:', error);
                                Swal.fire({
                                icon: 'error',
                                        title: 'Error',
                                        text: 'An error occurred while sending the message!'
                                });
                                });
                        }

                        // Gửi tin nhắn khi nhấp nút gửi
                        document.getElementById('sendMessage')?.addEventListener('click', function (e) {
                        e.preventDefault();
                        sendMessage();
                        });
                        // Gửi tin nhắn khi nhấn Enter
                        document.getElementById('messageContent')?.addEventListener('keypress', function (e) {
                        if (e.key === 'Enter') {
                        e.preventDefault();
                        sendMessage();
                        }
                        });
                        // Tự động hiển thị ô nhập và cuộn tới tin nhắn cuối nếu đã chọn khách hàng
                        if (selectedCustomerID) {
                        document.getElementById('inputContainer').style.display = 'flex';
                        scrollToBottom();
                        }
        </script>
    </body>
</html>