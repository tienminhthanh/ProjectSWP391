<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <title>Notification Details</title>

        <!--Customer Sidebar-->
        <link href="css/styleCustomerSidebar.css" rel="stylesheet">

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
            .detail-container {
                background-color: white;
                border-radius: 8px;
                padding: 20px;
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                max-width: 800px;
                margin: 20px auto;
            }
            .btn-primary {
                background-color: #3b82f6;
                border: none;
            }
            .btn-primary:hover {
                background-color: #2563eb;
            }
            .btn-danger {
                background-color: #ef4444;
                border: none;
            }
            .btn-danger:hover {
                background-color: #dc2626;
            }
        </style>
    </head>
    <body class="bg-gray-100">


        <div class="bg-orange-600 text-white p-6 flex justify-between items-center">
            <div class="flex items-center space-x-6">
                <img src="./img/logo.png" alt="Logo Wibooks" class="h-12">
                <h1 class="text-xl font-bold">Giao hàng</h1> <!-- Tăng cỡ chữ -->

            </div>
            <div class="flex items-center space-x-6">
                <a href="notificationshipper?action=list&receiverID=${sessionScope.account.accountID}"><i class="fas fa-bell text-2xl"></i></a>
                <a href="readAccount" class="fas fa-user-circle text-2xl"></a>
                <span class="text-xl">Shipper</span>
                <a href="logout" class="fas fa-sign-out-alt text-2xl"></a>
            </div>
        </div>

        <!-- Order Filter Navbar -->
        <div class="bg-white shadow-md">
            <div class="container mx-auto flex justify-around py-6">
                <button class="bg-green-500 text-white px-6 py-3 rounded-lg text-xl hover:bg-green-600">All Orders</button>
                <button class="bg-yellow-500 text-white px-6 py-3 rounded-lg text-xl hover:bg-yellow-600">Pending</button>
                <button class="bg-blue-500 text-white px-6 py-3 rounded-lg text-xl hover:bg-blue-600">In Progress</button>
                <button class="bg-purple-500 text-white px-6 py-3 rounded-lg text-xl hover:bg-purple-600">Completed</button>
                <button class="bg-red-500 text-white px-6 py-3 rounded-lg text-xl hover:bg-red-600">Cancelled</button>
            </div>
        </div>


        <main class="flex-1 p-4">
            <div class="detail-container">
                <h2 class="text-2xl font-bold mb-4">
                    <i class="fas fa-bell mr-2"></i> Notification Details
                </h2>

                <!-- Display error message if present -->
                <c:if test="${not empty error}">
                    <div class="alert alert-danger" role="alert">
                        ${error}
                    </div>
                </c:if>

                <c:choose>
                    <c:when test="${empty notification}">
                        <p class="text-gray-600">No notification found.</p>
                    </c:when>
                    <c:otherwise>
                        <div class="mb-4">
                            <h3 class="font-bold text-lg">${notification.notificationTitle}</h3>
                            <p class="text-gray-600">${notification.notificationDetails}</p>
                            <p class="text-gray-400 text-sm mt-2">
                                Date Created: <fmt:formatDate value="${notification.dateCreated}" pattern="dd/MM/yyyy"/>
                            </p>
                            <p class="text-gray-400 text-sm">
                                From: WIBOOK | To: ${sessionScope.account.firstName}
                            </p>
                        </div>

                        <div class="flex justify-end space-x-4">
                            <!-- Form to delete notification -->
                            <form action="notificationdetail" method="post" style="display:inline;">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="notificationID" value="${notification.notificationID}">
                                <input type="hidden" name="receiverID" value="${param.receiverID}">
                                <button type="submit" class="btn btn-danger">
                                    <i class="fas fa-trash mr-1"></i> Delete
                                </button>
                            </form>
                            <a href="notification?action=list&receiverID=${param.receiverID}" 
                               class="btn btn-primary">
                                <i class="fas fa-arrow-left mr-1"></i> Back
                            </a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </main>
    </div>

  
    <!--Customer sidebar script-->
    <script src="js/scriptCusSidebar.js"></script>
    <script src="js/scriptCusSideBarNOTDetails.js"></script><!-- comment -->

    <script src="https://kit.fontawesome.com/bfab6e6450.js" crossorigin="anonymous"></script>
    <script src="/js/scriptHeader.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.min.js"
            integrity="sha384-BBtl+eGJRgqQAUMxJ7pMwbEyER4l1g+O15P+16Ep7Q9Q+zqX6gSbd85u4mG4QzX+"
    crossorigin="anonymous"></script>
</body>
</html>