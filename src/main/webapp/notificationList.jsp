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
            }
            .btn-primary {
                background-color: #3b82f6;
                border: none;
            }
            .btn-primary:hover {
                background-color: #2563eb;
            }
            .btn-secondary {
                background-color: #64748b;
                border: none;
            }
            .btn-secondary:hover {
                background-color: #475569;
            }
        </style>
    </head>
    <body>
        <div class="flex mt-4 mx-0">
            <div class="w-64 bg-blue-900 text-white min-h-screen">
                <div class="p-4">
                    <img alt="Company Logo" class="mb-4" height="50" src="https://storage.googleapis.com/a1aa/image/E7a1IopinJdFFD1b8uBNgeve-ZYaN4NirThMMa4AP40.jpg" width="150"/>
                </div>
                <nav class="space-y-2">
                    <a class="flex items-center p-2 hover:bg-blue-800" href="#">
                        <i class="fas fa-tachometer-alt mr-2"></i>
                        Dashboard
                    </a>
                    <a class="flex items-center p-2 hover:bg-blue-800" href="listAccount">
                        <i class="fas fa-users mr-2"></i>
                        Account List
                    </a>
                    <a class="flex items-center p-2 hover:bg-blue-800" href="#">
                        <i class="fas fa-calendar-alt mr-2"></i>
                        Event List
                    </a>
                    <a class="flex items-center p-2 hover:bg-blue-800" href="#">
                        <i class="fas fa-cogs mr-2"></i>
                        Product List
                    </a>
                    <a class="flex items-center p-2 hover:bg-blue-800" href="#">
                        <i class="fas fa-comments mr-2"></i>
                        Dialogue List
                    </a>
                    <a class="flex items-center p-2 hover:bg-blue-800" href="#">
                        <i class="fas fa-box mr-2"></i>
                        Order List
                    </a>
                    <a class="flex items-center p-2 hover:bg-blue-800" href="voucherList">
                        <i class="fas fa-gift mr-2"></i>
                        Voucher List
                    </a>
                    <a class="flex items-center p-2 bg-blue-700 text-white hover:bg-blue-800 rounded-lg" href="listnotification">
                        <i class="fas fa-bell mr-2"></i>
                        Notification List
                    </a>
                    <a class="flex items-center p-2 hover:bg-blue-800" href="#">
                        <i class="fas fa-comment-dots mr-2"></i>
                        Chat
                    </a>
                    <div class="mt-4">
                        <h3 class="px-2 text-sm font-semibold">SETTINGS</h3>
                        <a class="flex items-center p-2 hover:bg-blue-800" href="#">
                            <i class="fas fa-cogs mr-2"></i>
                            Configuration
                        </a>
                        <a class="flex items-center p-2 hover:bg-blue-800" href="#">
                            <i class="fas fa-users-cog mr-2"></i>
                            Management
                        </a>
                        <a class="flex items-center p-2 hover:bg-blue-800" href="logout">
                            <i class="fas fa-sign-out-alt mr-2"></i>
                            Logout
                        </a>
                    </div>
                    <div class="mt-4">
                        <h3 class="px-2 text-sm font-semibold">REPORTS</h3>
                        <a class="flex items-center p-2 hover:bg-blue-800" href="#">
                            <i class="fas fa-phone-alt mr-2"></i>
                            Call history
                        </a>
                        <a class="flex items-center p-2 hover:bg-blue-800" href="#">
                            <i class="fas fa-headset mr-2"></i>
                            Call queue
                        </a>
                        <a class="flex items-center p-2 hover:bg-blue-800" href="#">
                            <i class="fas fa-users mr-2"></i>
                            Agents performance
                        </a>
                        <a class="flex items-center p-2 hover:bg-blue-800" href="#">
                            <i class="fas fa-file-invoice-dollar mr-2"></i>
                            Commission report
                        </a>
                        <a class="flex items-center p-2 hover:bg-blue-800" href="#">
                            <i class="fas fa-calendar mr-2"></i>
                            Scheduled report
                        </a>
                        <a class="flex items-center p-2 hover:bg-blue-800" href="#">
                            <i class="fas fa-history mr-2"></i>
                            Chat history
                        </a>
                        <a class="flex items-center p-2 bg-blue-800" href="#">
                            <i class="fas fa-chart-line mr-2"></i>
                            Performance report
                        </a>
                    </div>
                </nav>
            </div>
            <main class="flex-1 p-4">
                <div class="admin-container">
                    <div class="flex justify-between items-center mb-4">
                        <h2 class="text-lg font-bold">
                            <a class="flex items-center text-red-400" href="listnotification">
                                <i class="fas fa-bell mr-2"></i> Notification Management
                            </a>
                        </h2>
                        <div class="space-x-4">
                            <c:set var="notification" value="${notifications[0]}" />
                            <!-- Form to create a new notification -->
                            <form action="createnotification" method="get">
                                <input type="hidden" name="senderID" value="${notification.senderID}">
                                <button type="submit" class="btn btn-primary">Create Notification</button>
                            </form>
                        </div>
                    </div>
                    <!-- Display error message if present -->
                    <% if (request.getAttribute("error") != null) {%>
                    <p class="text-red-400"><%= request.getAttribute("error")%></p>
                    <% }%>
                    <div class="notification-scroll">
                        <c:choose>
                            <c:when test="${empty notifications}">
                                <p class="text-gray-300 p-4">There are no notifications.</p>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="notification" items="${notifications}">
                                    <div class="notification-item">
                                        <div class="flex items-start">
                                            <img alt="Notification icon" class="mr-4" 
                                                 src="https://icon-library.com/images/icon-notification/icon-notification-3.jpg" 
                                                 width="80" height="80"/>
                                            <div class="flex-1">
                                                <h3 class="font-bold text-white">${notification.notificationTitle}</h3>
                                                <p class="text-gray-300">${notification.notificationDetails}</p>
                                                <p class="text-gray-400 text-sm">
                                                    <fmt:formatDate value="${notification.dateCreated}" pattern="dd/MM/yyyy"/>
                                                </p>
                                            </div>
                                            <div flex-4>
                                                <!-- Form to update notification -->
                                                <form action="updatenotification" method="get">
                                                    <input type="hidden" name="notificationID" value="${notification.notificationID}">
                                                    <button type="submit" class="btn btn-secondary">
                                                        <i class="fas fa-edit mr-1"></i>
                                                    </button>
                                                </form>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </main>
        </div>
        <script src="https://kit.fontawesome.com/bfab6e6450.js" crossorigin="anonymous"></script>
        <script src="/js/scriptHeader.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.min.js"
                integrity="sha384-BBtl+eGJRgqQAUMxJ7pMwbEyER4l1g+O15P+16Ep7Q9Q+zqX6gSbd85u4mG4QzX+"
        crossorigin="anonymous"></script>
    </body>
</html>