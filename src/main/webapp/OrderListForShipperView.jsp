<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Shipper Dashboard</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet"/>
        <style>
            .status-badge {
                padding: 4px 10px;
                border-radius: 15px;
                font-size: 0.875rem;
                font-weight: 500;
                text-transform: capitalize;
                transition: all 0.3s ease;
            }

            .text-gray-700 {
                color: #4a5568;
                transition: color 0.3s ease;
            }

            .text-gray-700.active {
                color: #dd6b20;
            }
        </style>
    </head>
    <body class="bg-gray-100 text-sm"> 
        <!-- Header -->
        <div class="bg-orange-600 text-white p-4 flex justify-between items-center">
            <div class="flex items-center space-x-4">
                <img src="./img/logo.png" alt="Logo Wibooks" class="h-10">
                <h1 class="text-lg font-semibold">Deliver</h1>
            </div>
            <div class="flex items-center space-x-4">
                <i class="fas fa-bell text-lg"></i>
                <a href="readAccount" class="fas fa-user-circle text-lg"></a>
                <span class="text-base">Shipper</span>
                <a href="logout" class="fas fa-sign-out-alt text-lg"></a>
            </div>
        </div>

        <!-- Order Filter Navbar -->
        <!-- Order Filter Navbar -->
        <nav class="bg-white shadow-md">
            <div class="container mx-auto px-4 py-2 flex justify-around">
                <a href="OrderListForShipperController?status=Shipped" 
                   class="text-gray-700 hover:text-orange-600 font-semibold ${currentStatus == 'Shipped' ? 'text-orange-600' : ''}">
                    Shipping
                </a>
                <a href="OrderListForShipperController?status=delivered" 
                   class="text-gray-700 hover:text-orange-600 font-semibold ${currentStatus == 'delivered' ? 'text-orange-600' : ''}">
                    Delivered
                </a>
            </div>
        </nav>


        <!-- Main Content Section -->
        <main class="container mx-auto my-6">
            <div class="bg-white p-6 rounded-lg shadow-md">
                <h2 class="text-lg font-bold mb-4">Order List</h2>
                <div class="space-y-4">
                    <c:forEach var="order" items="${list}" varStatus="loop">
                        <div class="p-4 bg-gray-100 rounded-lg shadow-sm hover:shadow-md transition-shadow duration-200">
                            <div class="flex justify-between items-center mb-2">
                                <p class="text-base font-semibold">Order #${order.orderID}</p>
                            </div>
                            <p><i class="fas fa-calendar-alt"></i> Expected Delivery Date: <fmt:formatDate value="${order.expectedDeliveryDate}" pattern="dd/MM/yyyy"/></p>
                            <p>📍 Address: ${order.deliveryAddress}</p>
                            <p>💰 Fee: <fmt:formatNumber value="${order.preVoucherAmount}" type="number" groupingUsed="true" pattern="#,###"/> đ</p>
                            <c:if test="${not empty accountList}">
                                <c:set var="acc" value="${accountList[loop.index]}" />
                                <p>👤 User Name: ${acc.username}</p>
                                <p>📞 Contact: ${acc.phoneNumber}</p>
                            </c:if>
                            <p>💳 Payment Method: ${order.paymentMethod}</p>

                            <div class="mt-3">
                                <a href="OrderDetailForShipperController?id=${order.orderID}" 
                                   class="inline-block px-3 py-1 bg-green-600 hover:bg-green-700 text-white rounded-md shadow-sm">Details</a>
                                <form action="OrderListForShipperController" method="post" class="inline">
                                    <input type="hidden" name="orderID" value="${order.orderID}">
                                    <c:if test="${ orderInfo.deliveryStatus eq 'Shipped'}">      
                                        <button type="submit" class="px-3 py-1 bg-orange-500 hover:bg-orange-600 text-white rounded-md shadow-sm">Update</button>
                                    </c:if>

                                </form>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </main>

        <!-- Footer Section -->
        <footer class="bg-green-600 text-white py-4">
            <div class="container mx-auto text-center">
                <p>&copy; 2023 Shipper Dashboard. All rights reserved.</p>
            </div>
        </footer>
    </body>
</html>
