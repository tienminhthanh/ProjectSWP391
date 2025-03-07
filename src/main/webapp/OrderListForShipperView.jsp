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
    <body class="bg-gray-100 text-lg"> <!-- Tăng cỡ chữ toàn trang -->

        <!-- Header -->
        <div class="bg-orange-600 text-white p-6 flex justify-between items-center">
            <div class="flex items-center space-x-6">
                <img src="./img/logo.png" alt="Logo Wibooks" class="h-12">
                <h1 class="text-3xl font-bold">Giao hàng</h1> <!-- Tăng cỡ chữ -->
                <nav class="space-x-6">
                    <a href="#" class="hover:underline text-xl">Đơn hàng</a>
                    <a href="#" class="hover:underline text-xl">Sản phẩm</a>
                    <a href="#" class="hover:underline text-xl">Báo cáo</a>
                    <a href="#" class="hover:underline text-xl">Cấu hình</a>
                </nav>
            </div>
            <div class="flex items-center space-x-6">
                <i class="fas fa-bell text-2xl"></i>
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

        <!-- Main Content Section -->
        <main class="container mx-auto my-10">
            <div class="bg-white p-8 rounded-lg shadow-md">
                <h2 class="text-4xl font-bold mb-6">Order List</h2> <!-- Tiêu đề lớn hơn -->
                <div class="space-y-6">
                    <!-- Order Detail -->
                    <c:forEach var="order" items="${list}" varStatus="loop">
                        <div class="p-6 bg-gray-100 rounded-lg shadow-lg hover:shadow-xl transition-shadow duration-200">
                            <div class="flex justify-between items-center mb-3">
                                <p class="text-2xl font-semibold">Order #${order.orderID}</p>
                                <div class="relative group">
                                    <div class="absolute right-0 mt-2 w-52 bg-white border rounded-lg shadow-lg hidden group-hover:flex flex-col">
                                        <a href="#" class="block px-5 py-3 text-gray-800 hover:bg-gray-200 text-lg">Update Status</a>
                                        <a href="#" class="block px-5 py-3 text-gray-800 hover:bg-gray-200 text-lg">Update Payment</a>
                                        <a href="#" class="block px-5 py-3 text-gray-800 hover:bg-gray-200 text-lg">Update Details</a>
                                    </div>
                                </div>
                            </div>
                            <p class="text-lg">📍 Address: ${order.deliveryAddress}</p>
                            <p class="text-lg">💰 Fee: ${order.preVoucherAmount} VND</p>

                            <!-- Lấy thông tin khách hàng từ danh sách accountList -->
                            <c:if test="${not empty accountList}">
                                <c:set var="acc" value="${accountList[loop.index]}" />
                                <p class="text-lg">👤 User Name: ${acc.username}</p>
                                <p class="text-lg">📞 Contact: ${acc.phoneNumber}</p>
                            </c:if>
                            <p class="text-lg">💳 Payment Method: ${order.paymentMethod}</p>

                            <div class="mt-4">
                                <a href="OrderDetailForShipperController?id=${order.orderID}" 
                                   class="inline-block px-4 py-2 text-lg bg-green-600 hover:bg-green-700 text-white rounded-md shadow-sm transition-transform transform hover:scale-105 duration-200 mr-3">
                                    <i class="fas fa-eye mr-2"></i> Details
                                </a>
                                <button class="inline-block px-4 py-2 text-lg bg-orange-500 hover:bg-orange-600 text-white rounded-md shadow-sm transition-transform transform hover:scale-105 duration-200">
                                    Update
                                </button>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </main>

        <!-- Footer Section -->
        <footer class="bg-green-600 text-white py-6">
            <div class="container mx-auto text-center">
                <p class="text-xl">&copy; 2023 Shipper Dashboard. All rights reserved.</p>
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
