<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Performance Report</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet"/>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
        <style>
            body {
                height: 100vh;
                font-family: 'Arial', sans-serif;
                background-color: #f3f4f6; /* Màu nền sáng */
            }
            .sidebar {
                background-color: #4CAF50; /* Xanh lá */
                color: white;
                padding: 16px;
                min-height: 100vh;
            }
            .sidebar a {
                display: block;
                padding: 12px;
                border-radius: 5px;
                color: white;
                text-decoration: none;
                margin-bottom: 8px;
            }
            .sidebar a:hover {
                background-color: rgba(255, 255, 255, 0.2);
            }
            .card {
                background-color: white;
                border-radius: 10px;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
                padding: 20px;
                margin-bottom: 16px;
            }
            .button {
                background-color: #FF9800; /* Cam */
                color: white;
                padding: 12px 20px;
                border-radius: 8px;
                font-weight: 600;
                border: none;
                cursor: pointer;
                text-align: center;
                display: inline-block;
                margin-top: 8px;
            }
            .input, .select {
                border: 1px solid #D1D5DB;
                border-radius: 6px;
                padding: 10px;
                width: 200px;
                margin-right: 8px;
            }
            .input:focus, .select:focus {
                border-color: #4CAF50; /* Xanh lá */
                outline: none;
            }
            .order-info {
                display: flex;
                justify-content: space-between;
                align-items: center;
            }
            .order-details {
                flex: 1;
                margin-right: 20px; /* Space between details and buttons */
            }
            .update-section {
                display: flex;
                flex-direction: column;
            }
        </style>
    </head>
    <body>
        <div class="flex">
            <!-- Sidebar -->
            <div class="w-64 sidebar">
                <div class="p-4">
                    <a href="home"><img src="img/logo.png" alt="WIBOOKS" /></a> 
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
                    <a class="flex items-center p-2 hover:bg-blue-800" href="eventList">
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
                    <a class="flex items-center p-2 hover:bg-blue-800" href="OrderListForStaffController">
                        <i class="fas fa-box mr-2"></i>
                        Order List
                    </a>
                    <a class="flex items-center p-2 hover:bg-blue-800" href="#">
                        <i class="fas fa-gift mr-2"></i>
                        Voucher List
                    </a>
                    <a class="flex items-center p-2 hover:bg-blue-800" href="#">
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

            <!-- Main Content -->
            <div class="flex-1 p-6 overflow-y-auto">
                <div class="flex justify-between items-center mb-6">
                    <div class="flex items-center space-x-4">
                        <div class="flex-1 p-6 overflow-y-auto">
                            <div class="flex justify-between items-center mb-6">
                                <div class="flex items-center space-x-4">                     
                                    <div class="flex items-center space-x-2">
                                        <span>lọc</span>
                                        <input id="dateRangePicker" class="border rounded px-2 py-1" type="text" placeholder="Select date range" />
                                    </div>
                                </div>
                            </div>
                        </div>
                        <a href="readAccount" class="relative block">
                            <img alt="User  Avatar" class="rounded-full" height="40" src="https://storage.googleapis.com/a1aa/image/4V1-2KXMxoMa82g0Th8dvxQbS2qGQlXogtCiodNcjgE.jpg" width="40"/>
                            <span class="absolute bottom-0 right-0 bg-green-500 rounded-full w-3 h-3"></span>
                        </a>
                    </div>
                </div>

                <!-- Thanh Navbar trạng thái -->
                <div class="flex justify-between items-center mb-6 bg-white p-4 shadow rounded-lg">
                    <h1 class="text-xl font-semibold">Order List</h1>
                    <div class="flex space-x-4">
                        <p>Order Status:</p>
                        <a href="OrderListForStaffController" class="px-4 py-2 rounded-lg text-white
                           ${empty                       param.status ? 'bg-blue-600' : 'bg-gray-400'}">All</a>

                        <a href="OrderListForStaffController?status=Pending" class="px-4 py-2 rounded-lg text-white
                           ${param.status == 'Pending' ? 'bg-blue-600' : 'bg-gray-400'}">Pending</a>

                        <a href="OrderListForStaffController?status=Shipped" class="px-4 py-2 rounded-lg text-white
                           ${param.status == 'Shipped' ? 'bg-blue-600' : 'bg-gray-400'}">Shipped</a>

                        <a href="OrderListForStaffController?status=Completed" class="px-4 py-2 rounded-lg text-white
                           ${param.status == 'Completed' ? 'bg-blue-600' : 'bg-gray-400'}">Completed</a>

                        <a href="OrderListForStaffController?status=Canceled" class="px-4 py-2 rounded-lg text-white
                           ${param.status == 'Canceled' ? 'bg-blue-600' : 'bg-gray-400'}">Canceled</a>
                    </div>
                </div>
                
                <!-- Lặp danh sách đơn hàng -->         
                <c:forEach var="order" items="${orderList}">
                    <div class="card">
                        <div class="order-info flex">
                            <div class="order-details flex-1">
                                <h2 class="font-semibold">Order ID: ${order.orderID}</h2>
                                <p>User Name: ${customerMap[order.orderID].firstName} ${customerMap[order.orderID].lastName}</p>
                                <p>Address: ${order.deliveryAddress}</p>
                                <p>Status: ${order.orderStatus}</p>
                                <p>Created Date: ${order.orderDate}</p>
                                <p>Total: <fmt:formatNumber value="${order.preVoucherAmount}" pattern="#,##0"/> đ</p>
                                <p>Payment Method: ${order.paymentMethod}</p>
                                <p>Delivery Status: ${order.deliveryStatus}</p>
                                <p>Order Status: ${order.orderStatus}</p>
                            </div>

                        </div>
                        <div class="flex justify-between items-center mt-4">
                            <a href="OrderDetailForStaffController?id=${order.orderID}" class="button">View Details</a>
                            <div class="update-section flex items-center">
                                <!-- Update Shipper -->
                                <c:if test="${order.orderStatus ne 'Shipped'}">
                                    <form action="OrderListForStaffController" method="POST" class="flex items-center mb-2">
                                        <input type="hidden" name="orderID" value="${order.orderID}">
                                        <select name="shipperID" class="select" required>
                                            <option value="">Choose Shipper</option>
                                            <c:forEach var="shipper" items="${shipperList}">
                                                <option value="${shipper.shipperID}">${shipper.account.username} (${shipper.totalDeliveries})</option>
                                            </c:forEach>
                                        </select>
                                        <button type="submit" class="button">Update</button>
                                    </form>
                                </c:if>

                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
        <script>
            document.addEventListener("DOMContentLoaded", function () {
                flatpickr("#dateRangePicker", {
                    mode: "range",
                    dateFormat: "d/m/Y", // Định dạng ngày/tháng/năm
                    defaultDate: new Date(),
                    onClose: function (selectedDates, dateStr) {
                        console.log("Selected Date Range:", dateStr);
                    }
                });
            });
        </script>
    </body>
</html>