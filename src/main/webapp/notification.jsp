<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="utf-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <title>Thông Báo Đơn Hàng</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <link href="/css/styleHeader.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous"/>
        <link href="/css/styleFooter.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"/>
        <style>
            .chat-container {
                width: 60%;
                height: 60%;
                position: fixed;
                bottom: 0;
                right: 0;
                display: none;
                background-color: white;
                box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                border-radius: 10px;
            }
            .chat-container.show {
                display: flex;
            }
            .notification-scroll {
                max-height: 500px;
                overflow-y: auto;
                padding-right: 10px;
            }
            .notification-scroll::-webkit-scrollbar {
                width: 8px;
            }
            .notification-scroll::-webkit-scrollbar-thumb {
                background-color: #888;
                border-radius: 4px;
            }
            .notification-scroll::-webkit-scrollbar-thumb:hover {
                background-color: #555;
            }
            .notification-item {
                transition: background-color 0.3s;
            }
            .notification-item.unread {
                background-color: #f0f9ff;
            }
            .notification-item.read {
                background-color: #e5e7eb;
            }
        </style>
    </head>
    <body class="bg-gray-100">
        <c:import url="header.jsp"/>

        <!-- Main Content -->
        <div class="flex mt-4 mx-0">
            <!-- Sidebar -->
            <jsp:include page="customerSidebar.jsp"/>

            <!-- Notification Content -->
            <main class="flex-1 bg-white p-4"> <!-- Sử dụng flex-1 để chiếm phần còn lại -->
                <div class="flex justify-between items-center mb-4">
                    <h2 class="text-lg font-bold">
                        <a class="flex items-center text-red-500" href="/notification?action=list&receiverID=${param.receiverID}">
                            <i class="fas fa-bell mr-2"></i>
                            Thông Báo
                        </a>
                    </h2>
                    <a class="text-blue-500 hover:underline" href="#">Đánh dấu Đã đọc tất cả</a>
                </div>

                <div class="notification-scroll space-y-4">
                    <c:choose>
                        <c:when test="${empty notifications}">
                            <p class="text-gray-600 p-4">Không có thông báo nào.</p>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="notification" items="${notifications}">
                                <div class="flex items-start p-4 rounded-md notification-item ${notification.read ? 'read' : 'unread'}">
                                    <img alt="Notification icon" class="mr-4" 
                                         src="https://icon-library.com/images/icon-notification/icon-notification-3.jpg" 
                                         width="100" height="100"/>
                                    <div class="flex-1">
                                        <h3 class="font-bold">${notification.notificationTitle}</h3>
                                        <p class="text-gray-600">${notification.notificationDetails}</p>
                                        <p class="text-gray-400 text-sm">
                                            <fmt:formatDate value="${notification.dateCreated}" pattern="dd/MM/yyyy HH:mm:ss"/>
                                        </p>
                                    </div>
                                    <div class="flex space-x-2">
                                        <c:if test="${not notification.read}">
                                            <form action="notification" method="post" style="display:inline;">
                                                <input type="hidden" name="action" value="markAsRead">
                                                <input type="hidden" name="notificationID" value="${notification.notificationID}">
                                                <input type="hidden" name="receiverID" value="${param.receiverID}">
                                                <button type="submit" class="text-blue-500 hover:underline">Đánh dấu đã đọc</button>
                                            </form>
                                        </c:if>
                                        <form action="notification" method="post" style="display:inline;">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="notificationID" value="${notification.notificationID}">
                                            <input type="hidden" name="receiverID" value="${param.receiverID}">
                                            <button type="submit" class="text-red-500 hover:underline">Xóa</button>
                                        </form>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </div>
            </main>
        </div>

        <jsp:include page="chat.jsp"/>
        <c:import url="footer.jsp"/>

        <!-- Scripts -->
        <script src="https://kit.fontawesome.com/bfab6e6450.js" crossorigin="anonymous"></script>
        <script src="/js/scriptHeader.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.min.js"
                integrity="sha384-BBtl+eGJRgqQAUMxJ7pMwbEyER4l1g+O15P+16Ep7Q9Q+zqX6gSbd85u4mG4QzX+"
        crossorigin="anonymous"></script>
    </body>
</html>