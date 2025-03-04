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
                height: 75%;
                position: fixed;
                bottom: 0;
                right: 0;
                display: none;
                background: white;
                box-shadow: -2px -2px 15px rgba(0,0,0,0.1);
                border-radius: 10px 10px 0 0;
                z-index: 1000;
            }
            .chat-container.show {
                display: flex;
            }
            .chat-input {
                width: 100%;
                padding: 10px;
                border-top: 1px solid #eee;
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
        <div class="fixed bottom-4 right-4">
            <form action="chat" method="get">
                <button id="chatButton" class="bg-yellow-500 text-white p-2 rounded-full shadow-lg">
                    <i class="fas fa-comments"></i>
                    <span class="ml-1">Chat</span>
                </button>
            </form>
        </div>

        <div id="chatContainer" class="chat-container flex flex-col">
            <div class="bg-white p-4 border-b flex justify-between items-center">
                <h2 class="text-lg font-bold">
                    <c:choose>
                        <c:when test="${sessionScope.account.accountID == 1}">Chat with Customer</c:when>
                        <c:otherwise>Chat with Shop WIBOOK</c:otherwise>
                    </c:choose>
                </h2>
                <button id="closeChat" class="text-red-500 text-xl hover:text-red-700">×</button>
            </div>

            <c:if test="${sessionScope.account.accountID == 1}">
                <div class="p-4 bg-gray-100">
                    <form action="chat" method="get">
                        <select name="customerID" onchange="this.form.submit()" class="w-full p-2 border rounded">
                            <option value="">Select a customer</option>
                            <c:forEach var="customerID" items="${customerIDs}">
                                <option value="${customerID}" ${customerID == selectedCustomerID ? 'selected' : ''}>
                                    Customer ${customerID}
                                </option>
                            </c:forEach>
                        </select>
                    </form>
                </div>
            </c:if>

            <div class="flex-1 p-4 overflow-y-auto bg-gray-100">
                <c:choose>
                    <c:when test="${empty chats}">
                        <p class="text-center text-gray-500">No messages yet.</p>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="chat" items="${chats}">
                            <div class="mb-4 ${chat.senderID == sessionScope.account.accountID ? 'text-right' : 'text-left'}">
                                <div class="inline-block p-3 rounded-lg ${chat.senderID == sessionScope.account.accountID ? 'bg-blue-500 text-white' : 'bg-white'}">
                                    <p>${chat.messageContent}</p>
                                    <span class="text-xs">
                                        <fmt:formatDate value="${chat.sentAt}" pattern="dd/MM/yyyy HH:mm"/>
                                    </span>
                                </div>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>

            <form action="chat" method="post" class="chat-input">
                <c:if test="${sessionScope.aaccount.accountID == 1}">
                    <input type="hidden" name="customerID" value="${selectedCustomerID}">
                </c:if>
                <div class="flex">
                    <input type="text" name="messageContent" class="flex-1 p-2 border rounded-l-lg" 
                           placeholder="Nhập tin nhắn..." required>
                    <button type="submit" class="bg-blue-500 text-white p-2 rounded-r-lg">
                        <i class="fas fa-paper-plane"></i>
                    </button>
                </div>
            </form>
        </div>

        <script>
            document.getElementById('chatButton').addEventListener('click', () => {
                document.getElementById('chatContainer').classList.toggle('show');
            });
            document.getElementById('closeChat').addEventListener('click', () => {
                document.getElementById('chatContainer').classList.remove('show');
            });
        </script>
    </body>
</html>