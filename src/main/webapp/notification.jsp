<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Thông Báo Đơn Hàng</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"/>
    <style>
        .chat-container {
            width: 60%; /* 3/5 màn hình */
            height: 60%; /* 3/5 màn hình */
            position: fixed;
            bottom: 0;
            right: 0;
            display: none; /* Ẩn khung chat ban đầu */
            background-color: white; /* Đặt màu nền cho khung chat */
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); /* Thêm shadow cho khung chat */
            border-radius: 10px; /* Bo góc cho khung chat */
        }
        .chat-container.show {
            display: flex; /* Hiển thị khung chat khi nhấn nút */
        }
    </style>
</head>
<body class="bg-gray-100">
   <c:import url="header.jsp"/>

    <!-- Main Content -->
    <div class="container mx-auto flex mt-4">
        <!-- Sidebar -->
        <aside class="w-1/4 bg-white p-4">
            <div class="items-center mb-4">
                <img alt="avatar" class="rounded-full mr-2" height="50" src="#" width="50"/>
                <div>
                    <span class="font-bold">Nhattruong</span>
                </div>
                <br>
                <div>
                    <a class="text-blue-500 hover:underline" href="readAccount">Edit Profile</a>
                </div>
            </div>
            <nav class="space-y-2">
                <a class="flex items-center text-red-500" href="#">
                    <i class="fas fa-bell mr-2"></i>
                    Thông Báo
                </a>
                <!-- Thêm các menu khác tại đây -->
            </nav>
        </aside>

        <!-- Notification Content -->
        <main class="w-3/4 bg-white p-4">
            <div class="flex justify-between items-center mb-4">
                <h2 class="text-lg font-bold">Cập Nhật Đơn Hàng</h2>
                <a class="text-blue-500 hover:underline" href="#">Đánh dấu Đã đọc tất cả</a>
            </div>
            
            <div class="space-y-4">
                <c:choose>
                    <c:when test="${empty notifications}">
                        <p class="text-gray-600 p-4">Không có thông báo nào.</p>
                    </c:when>
                    
                    <c:otherwise>
                        <c:forEach var="notification" items="${notifications}">
                            <div class="flex items-start bg-gray-100 p-4 rounded-md">
                                <img alt="Product image" class="mr-4" 
                                     src="${notification.productImage}" 
                                     width="100" height="100"/>
                                <div class="flex-1">
                                    <h3 class="font-bold">${notification.title}</h3>
                                    <p class="text-gray-600">
                                        ${notification.notificationDetails}
                                        <span class="font-bold">${notification.orderId}</span>
                                        ${notification.additionalInfo}
                                        <span class="font-bold">${notification.expiryDate}</span>
                                    </p>
                                    <p class="text-gray-400 text-sm">${notification.timestamp}</p>
                                </div>
                                <a class="text-blue-500 hover:underline" 
                                   href="/orders/${notification.orderId}">
                                    ${notification.actionText}
                                </a>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>
        </main>
    </div>

    <c:import url="footer.jsp"/>

    <!-- Chat Button -->
    <div class="fixed bottom-4 right-4">
        <button id="chatButton" class="bg-orange-500 text-white p-2 rounded-full shadow-lg">
            <i class="fas fa-comments"></i>
            <span class="ml-1">Chat</span>
        </button>
    </div>

    <!-- Chat Container -->
    <div id="chatContainer" class="chat-container">
        <!-- Sidebar -->
        <div class="w-1/4 bg-white border-r p-4">
            <div class="flex justify-between items-center mb-4">
                <h2 class="text-lg font-bold">Chat</h2>
                <button id="closeChat" class="text-red-500">&times;</button>
            </div>
            <div class="p-4 border-b">
                <input type="text" placeholder="Tìm theo tên" class="w-full p-2 border rounded">
            </div>
            <div class="overflow-y-auto h-full">
                <!-- Repeat this block for each chat -->
                <c:forEach var="chat" items="${chats}">
                    <div class="p-4 flex items-center border-b">
                        <img src="${chat.image}" alt="${chat.name}" class="w-10 h-10 rounded-full mr-4">
                        <div class="flex-1">
                            <div class="flex justify-between">
                                <span class="font-bold">${chat.name}</span>
                                <span class="text-sm text-gray-500">${chat.date}</span>
                            </div>
                            <p class="text-sm text-gray-500">${chat.message}</p>
                        </div>
                    </div>
                </c:forEach>
                <!-- End of chat block -->
            </div>
        </div>
        <!-- Main Content -->
        <div class="flex-1 flex flex-col items-center justify-center bg-gray-100">
            <div class="text-center">
                <img src="https://placehold.co/150x150" alt="Laptop with chat bubble" class="mx-auto mb-4">
                <h2 class="text-xl font-semibold text-gray-700">Chào mừng bạn đến với Shopee Chat</h2>
                <p class="text-gray-500">Bắt đầu trả lời người mua!</p>
            </div>
        </div>
    </div>

    <script>
        // Xử lý sự kiện khi nhấn nút "Chat"
        document.getElementById("chatButton").addEventListener("click", function() {
            var chatContainer = document.getElementById("chatContainer");
            chatContainer.classList.toggle("show"); // Thêm/xóa lớp show
        });

        // Xử lý sự kiện khi nhấn nút đóng chat
        document.getElementById("closeChat").addEventListener("click", function() {
            var chatContainer = document.getElementById("chatContainer");
            chatContainer.classList.remove("show"); // Xóa lớp show
        });
    </script>
</body>
</html>