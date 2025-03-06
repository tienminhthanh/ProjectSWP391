<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>List Notification - Admin</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <link href="/css/styleHeader.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous"/>
        <link href="/css/styleFooter.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"/>
        <style>
            body {
                background-color: #f1f5f9;
                margin: 0;
                display: flex;
            }
            .admin-container {
                background-color: #1e293b;
                color: white;
                border-radius: 8px;
                padding: 20px;
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            }
            .notification-scroll {
                max-height: 600px;
                overflow-y: auto;
                padding-right: 10px;
            }
            .notification-scroll::-webkit-scrollbar {
                width: 8px;
            }
            .notification-scroll::-webkit-scrollbar-thumb {
                background-color: #64748b;
                border-radius: 4px;
            }
            .notification-scroll::-webkit-scrollbar-thumb:hover {
                background-color: #475569;
            }
            .notification-item {
                transition: background-color 0.3s;
                border-radius: 6px;
                padding: 16px;
                margin-bottom: 10px;
                background-color: #334155;
                cursor: pointer;
            }
            .notification-item:hover {
                background-color: #475569;
            }
            .btn-primary {
                background-color: #3b82f6;
                border: none;
            }
            .btn-primary:hover {
                background-color: #2563eb;
            }
        </style>
    </head>
    <body class="bg-gray-50 min-h-screen flex">
        <div class="w-64 bg-orange-400 text-white min-h-screen">
            <jsp:include page="navbarAdmin.jsp" flush="true"/> 
        </div>

        <!-- Main Content -->
        <div class="flex-1 p-6">
            <div class="admin-container">
                <div class="flex justify-between items-center mb-4">
                    <h2 class="text-lg font-bold">
                        <a class="flex items-center text-red-400" href="listnotification">
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
                    <p class="text-red-400">${error}</p>
                </c:if>
                <div class="notification-scroll">
                    <c:choose>
                        <c:when test="${empty notifications}">
                            <p class="text-gray-300 p-4">There are no notifications.</p>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="notification" items="${notifications}">
                                <a href="notificationdetail?notificationID=${notification.notificationID}&receiverID=${notification.receiverID}" 
                                   style="text-decoration: none;">
                                    <div class="notification-item">
                                        <div class="flex items-start">
                                            <img alt="Notification icon" class="mr-4" 
                                                 src="https://icon-library.com/images/icon-notification/icon-notification-3.jpg" 
                                                 width="80" height="80"/>
                                            <div class="flex-1">
                                                <h2 class="font-bold text-white">${notification.notificationTitle}</h2>
                                                <p class="text-gray-300">Click to see details.</p>
                                                <p class="text-gray-400 text-sm">
                                                    <fmt:formatDate value="${notification.dateCreated}" pattern="dd-MM-yyyy"/>
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
        <script src="https://kit.fontawesome.com/bfab6e6450.js" crossorigin="anonymous"></script>
        <script src="/js/scriptHeader.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.min.js"
                integrity="sha384-BBtl+eGJRgqQAUMxJ7pMwbEyER4l1g+O15P+16Ep7Q9Q+zqX6gSbd85u4mG4QzX+"
                crossorigin="anonymous"></script>
    </body>
</html>