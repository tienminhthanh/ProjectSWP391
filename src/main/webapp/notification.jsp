<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="utf-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <title>Thông Báo Đơn Hàng</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <!--Header css-->
        <link href="css/styleHeader.css" rel="stylesheet">

        <!--Footer css-->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous" />
        <link href="css/styleFooter.css" rel="stylesheet">
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
        <jsp:include page="chat.jsp"/>
        <c:import url="footer.jsp"/>


        <!--Script for include icons-->
        <script src="https://kit.fontawesome.com/bfab6e6450.js" crossorigin="anonymous"></script>

        <!--Header script-->
        <script src="js/scriptHeader.js"></script>

        <!--Footer script-->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.min.js"
                integrity="sha384-BBtl+eGJRgqQAUMxJ7pMwbEyER4l1g+O15P+16Ep7Q9Q+zqX6gSbd85u4mG4QzX+"
        crossorigin="anonymous"></script>

    </body>
</html>