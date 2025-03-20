<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <title>Notifications</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <link href="/css/styleHeader.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous"/>
        <link href="/css/styleFooter.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"/>
        <!--Customer Sidebar-->
        <link href="css/styleCustomerSidebar.css" rel="stylesheet">

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
    <body class="bg-gray-100 text-base">
        <!-- Header -->
        <div class="bg-orange-500 text-white p-4 flex justify-between items-center">
            <div class="flex items-center space-x-4">
                <img src="./img/logo.png" alt="Logo Wibooks" class="h-10">
                <h1 class="text-lg font-semibold">Deliver</h1>
            </div>
            <div class="flex items-center space-x-6">
<!--                <a href="notificationshipper?action=list&receiverID=${sessionScope.account.accountID}"><i class="fas fa-bell text-2xl"></i></a>-->
                <a href="notificationshipper?action=list&receiverID=${sessionScope.account.accountID}" class="relative">
                    <i class="fas fa-bell text-2xl"></i>
                    <c:set var="unreadCount" value="0" />
                    <c:forEach var="notification" items="${sessionScope.notifications}">
                        <c:if test="${!notification.isRead()}">
                            <c:set var="unreadCount" value="${unreadCount + 1}" />
                        </c:if>
                    </c:forEach>
                    <c:if test="${unreadCount > -1}">
                        <span class="absolute -top-2 -right-2 bg-red-500 text-white text-xs rounded-full h-5 w-5 flex items-center justify-center">${unreadCount}</span>
                    </c:if>
                </a>
                <a href="readAccount" class="fas fa-user-circle text-2xl"></a>
                <span class="text-xl">Shipper</span>
                <a href="logout" class="fas fa-sign-out-alt text-2xl"></a>
            </div>
        </div>
        <div class="flex flex-col md:flex-row">
            <main class="flex-1 bg-white p-4">
                <div class="flex justify-between items-center mb-4">
                    <h2 class="text-orange-500 text-lg font-bold">
                        <i class="fas fa-bell mr-2"></i>
                        Notifications
                    </h2>

                    <form action="notificationshipper" method="post" style="display:inline;" onsubmit="event.stopPropagation();">
                        <input type="hidden" name="action" value="markAsAllRead">
                        <input type="hidden" name="receiverID" value="${param.receiverID}">
                        <button type="submit" class="text-blue-500 hover:underline" onclick="event.stopPropagation();">Mark all as read</button>
                    </form>
                </div>

                <div class="notification-scroll space-y-4">
                    <c:choose>
                        <c:when test="${empty notifications}">
                            <p class="text-gray-600 p-4">There are no notifications.</p>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="notification" items="${notifications}">
                                <div class="flex items-start p-4 rounded-md notification-item ${notification.isRead() ? 'read' : 'unread'}">
                                    <!-- Wrap the entire item in an anchor tag pointing to the detail page -->
                                    <a href="notificationdetail?notificationID=${notification.notificationID}&receiverID=${notification.receiverID}" 
                                       class="flex items-start w-full">
                                        <img alt="Notification icon" class="mr-4" 
                                             src="https://i.pinimg.com/222x/5c/61/84/5c61840474f5e4f69ca6a03507c2a569.jpg" 
                                             width="100" height="100"/>
                                        <div class="flex-1">
                                            <h3 class="font-bold">
                                                <!-- Remove the inner <a> tag since the whole div is now clickable -->
                                                <span class="text-blue-600 hover:underline">${notification.notificationTitle}</span>
                                            </h3>
                                            <p class="text-gray-600">Click to see details.</p>
                                            <p class="text-gray-400 text-sm">
                                                <fmt:formatDate value="${notification.dateCreated}" pattern="dd/MM/yyyy"/>
                                            </p>
                                        </div>
                                    </a>
                                    <!-- Keep the buttons outside the <a> tag to avoid them triggering the link -->
                                    <div class="flex space-x-2">
                                        <c:if test="${not notification.read}">
                                            <!-- Form to mark notification as read -->
                                            <form action="notificationshipper" method="post" style="display:inline;" onsubmit="event.stopPropagation();">
                                                <input type="hidden" name="action" value="markAsRead">
                                                <input type="hidden" name="notificationID" value="${notification.notificationID}">
                                                <input type="hidden" name="receiverID" value="${notification.receiverID}">
                                                <button type="submit" class="text-blue-500 hover:underline" onclick="event.stopPropagation();">Mark as read</button>
                                            </form>
                                        </c:if>
                                        <!-- Form to delete notification -->
                                        <form action="notificationshipper" method="post" style="display:inline;" onsubmit="event.stopPropagation();">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="notificationID" value="${notification.notificationID}">
                                            <input type="hidden" name="receiverID" value="${param.receiverID}">
                                            <button type="submit" class="text-red-500 hover:underline" onclick="event.stopPropagation();">Delete</button>
                                        </form>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="flex justify space-x-4">
                    <a href="OrderListForShipperController" 
                       class="btn btn-primary">
                        <i class="fas fa-arrow-left mr-1"></i> Back
                    </a>
                </div>
            </main>
        </div>



        <script>
            document.addEventListener("DOMContentLoaded", function () {
            const notifications = document.querySelectorAll(".notification");
                    notifications.forEach(notification => {
                    // Lấy nội dung text và loại bỏ khoảng trắng thừa
                    const text = notification.textContent.trim();
                            console.log("Nội dung text:", `"${text}"`); // In chính xác nội dung của text

                            // Kiểm tra và tách Order ID
                            if (text.includes("Order ID:")) {
                    const parts = text.split("Order ID:");
                            console.log("Mảng sau split:", parts); // In mảng để kiểm tra

                            // Lấy phần sau "Order ID:" (nếu có)
                            const result = parts[1]?.trim() || "";
                            console.log("Order ID:", `"${result}"`); // In result để kiểm tra

                            // Tiếp tục xử lý với link
                            const div = notification.closest("div");
                            const link = div?.querySelector("a");
                            if (link && result) { // Chỉ cập nhật nếu result không rỗng
                    link.href = "OrderDetailForShipperController?id=" + result;
                            console.log("Đã cập nhật href:", link.href);
                    } else {
                    console.log("Không cập nhật href: link không tồn tại hoặc Order ID rỗng");
                    }
                    }
                    else {
                    console.log("Không tìm thấy 'Order ID:' trong:", `"${text}"`);
                    }
                    });
            });
        </script>
        <script src="https://kit.fontawesome.com/bfab6e6450.js" crossorigin="anonymous"></script>
        <script src="/js/scriptHeader.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.min.js"
                integrity="sha384-BBtl+eGJRgqQAUMxJ7pMwbEyER4l1g+O15P+16Ep7Q9Q+zqX6gSbd85u4mG4QzX+"
        crossorigin="anonymous"></script>
        <!--Customer sidebar script-->
        <script src="js/scriptCusSidebar.js"></script>
        <script src="js/scriptCusSideBarNOTDetails.js"></script>

    </body>
</html>