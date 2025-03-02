<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shipper Dashboard</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet"/>
</head>
<body class="bg-gray-100">

    <div class="bg-orange-600 text-white p-4 flex justify-between items-center">
        <div class="flex items-center space-x-4">
            <img src="./img/logo.png" alt="Logo Wibooks" class="h-10 w-25">
            <h1 class="text-xl font-bold">Giao hàng</h1>
            <nav class="space-x-4">
                <a href="#" class="hover:underline">Đơn hàng</a>
                <a href="#" class="hover:underline">Sản phẩm</a>
                <a href="#" class="hover:underline">Báo cáo</a>
                <a href="#" class="hover:underline">Cấu hình</a>
            </nav>
        </div>
        <div class="flex items-center space-x-4">
            <i class="fas fa-bell"></i>
            <a href="readAccount" class="fas fa-user-circle"></a>
            <span>Shipper</span>
            <a href="logout" class="fas fa-sign-out-alt"></a>
        </div>
    </div>

    <!-- Order Filter Navbar -->
    <div class="bg-white shadow-md">
        <div class="container mx-auto flex justify-around py-4">
            <button class="bg-green-500 text-white px-4 py-2 rounded-lg hover:bg-green-600 transition-colors duration-200">All Orders</button>
            <button class="bg-yellow-500 text-white px-4 py-2 rounded-lg hover:bg-yellow-600 transition-colors duration-200">Pending</button>
            <button class="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition-colors duration-200">In Progress</button>
            <button class="bg-purple-500 text-white px-4 py-2 rounded-lg hover:bg-purple-600 transition-colors duration-200">Completed</button>
            <button class="bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-600 transition-colors duration-200">Cancelled</button>
        </div>
    </div>

    <!-- Main Content Section -->
    <main class="container mx-auto my-8">
        <div class="bg-white p-6 rounded-lg shadow-md">
            <h2 class="text-2xl font-bold mb-4">Order List</h2>
            <div class="space-y-4">
                <!-- Order Detail -->
                <c:forEach var="order" items="${list}" varStatus="loop">
                    <div class="p-4 bg-gray-100 rounded-lg shadow-lg hover:shadow-xl transition-shadow duration-200">
                        <div class="flex justify-between items-center mb-2">
                            <p class="text-lg font-semibold">Order #${order.orderID}</p>
                            <div class="relative">
                                <button class="bg-orange-500 text-white px-4 py-2 rounded-lg">Update</button>
                                <div class="absolute right-0 mt-2 w-48 bg-white border rounded-lg shadow-lg hidden group-hover:block">
                                    <a href="#" class="block px-4 py-2 text-gray-800 hover:bg-gray-200">Update Status</a>
                                    <a href="#" class="block px-4 py-2 text-gray-800 hover:bg-gray-200">Update Payment</a>
                                    <a href="#" class="block px-4 py-2 text-gray-800 hover:bg-gray-200">Update Details</a>
                                </div>
                            </div>
                        </div>
                        <p class="text-sm">Address: ${order.deliveryAddress}</p>
                        <p class="text-sm">Fee: ${order.preVoucherAmount} vnd</p>

                        <!-- Lấy thông tin khách hàng từ danh sách accountList -->
                        <c:if test="${not empty accountList}">
                            <c:set var="acc" value="${accountList[loop.index]}" />
                            <p class="text-sm">User  Name: ${acc.username}</p>
                            <p class="text-sm">Contact: ${acc.phoneNumber}</p>
                        </c:if>
                        <p class="text-sm">Payment Method: ${order.paymentMethod}</p>
                        <a href="OrderDetailForShipper?id=${order.orderID}" 
                           class="flex items-center px-2 py-1 bg-green-600 hover:bg-green-700 text-white rounded-lg shadow-md transition-transform transform hover:scale-105 duration-200">
                            <i class="fas fa-eye mr-1"></i>
                            Details
                        </a>
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

    <script>
        document.querySelectorAll('.relative').forEach(function (dropdown) {
            dropdown.addEventListener('click', function () {
                this.querySelector('.absolute').classList.toggle('hidden');
            });
        });
    </script>

</body>
</html>