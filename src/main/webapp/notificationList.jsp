<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>List Notification - Admin</title>

        <script src="https://cdn.tailwindcss.com"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"/>
        <link href="https://cdn.jsdelivr.net/npm/sweetalert2@11.6.9/dist/sweetalert2.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous"/>
        <link rel="stylesheet" href="css/styleNotiList.css">
    </head>
    <body class="bg-gray-50 min-h-screen flex">
        <!-- Sidebar -->
        <div class="w-64 bg-orange-400 text-white min-h-screen">
            <jsp:include page="navbarAdmin.jsp" flush="true"/> 
        </div>

        <!-- Main Content -->
        <div class="flex-1 p-6">
            <div class="admin-container bg-gray-50">
                <div class="flex justify-between items-center mb-1 px-5 pt-3 pb-3 rounded-t-lg bg-orange-400">
                    <h2 class="text-lg font-bold">
                        <a class="flex items-center text-white hover:text-gray-100" href="listnotification">
                            <i class="fas fa-bell mr-2"></i> Notification Management
                        </a>
                    </h2>
                    <div class="space-x-4">
                        <c:set var="notification" value="${notifications[0]}" />
                        <form action="createnotification" method="get">
                            <input type="hidden" name="senderID" value="${notification.senderID}">
                            <button type="submit" class="btn btn-primary">Create Notification</button>
                        </form>
                    </div>
                </div>
                <!-- Display error message if present -->
                <c:if test="${not empty error}">
                    <p class="text-orange-500">${error}</p> <!-- Màu lỗi orange-500 -->
                </c:if>
                <div class="notification-scroll">
                    <c:choose>
                        <c:when test="${empty notifications}">
                            <p class="p-4 text-orange-400">There are no notifications.</p>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="notification" items="${notifications}">
                                <a href="notificationdetail?notificationID=${notification.notificationID}&receiverID=${notification.receiverID}" 
                                   style="text-decoration: none;">
                                    <div class="notification-item ">
                                        <div class="flex items-start">
                                            <img alt="Notification icon" class="mr-4" 
                                                 src="https://icon-library.com/images/icon-notification/icon-notification-3.jpg" 
                                                 width="80" height="80"/>
                                            <div class="flex-1">
                                                <h2 class="font-bold">${notification.notificationTitle}</h2>
                                                <p>Click to see details.</p>
                                                <p class="text-sm">
                                                    <fmt:formatDate value="${notification.dateCreated}" pattern="dd/MM/yyyy"/>
                                                </p>
                                            </div>
                                        </div>
                                    </div>
                                </a>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
         <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    </body>
</html>