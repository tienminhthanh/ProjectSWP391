<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Delivery</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
        <style>
            body {
                display: flex; /* Sử dụng Flexbox cho body */
                background-color: #f7fafc; /* Màu nền cho body */
                margin: 0;
            }
            .sidebar {
                position: fixed; /* Giữ sidebar cố định */
                top: 0;
                left: 0; /* Đưa navbar về bên trái */
                height: 100vh; /* Chiều cao 100% */
                width: 250px; /* Chiều rộng cho sidebar */
                background-color: #4CAF50; /* Xanh lá */
                color: white;
                padding-top: 20px;
                overflow-y: auto; /* Thêm cuộn nếu nội dung quá dài */
            }
            .sidebar nav a {
                display: block;
                padding: 10px 20px;
                color: white;
                text-decoration: none;
                transition: background-color 0.3s; /* Hiệu ứng chuyển màu */
            }
            .sidebar nav a:hover {
                background-color: #2d3748; /* Màu nền khi hover */
            }
            .main-content {
                margin-left: 250px; /* Dịch nội dung chính sang phải, tránh bị sidebar che */
                padding: 20px;
                flex: 1; /* Chiếm toàn bộ không gian còn lại */
            }
            .header {
                background-color: #f6ad55; /* Màu nền cho header */
                color: white;
                padding: 10px 20px;
                display: flex;
                justify-content: space-between;
                align-items: center;
            }
            .header img {
                height: 40px; /* Chiều cao logo */
            }
            .header nav a {
                margin-left: 20px;
                color: white;
                text-decoration: none;
                transition: text-decoration 0.3s; /* Hiệu ứng gạch chân */
            }
            .header nav a:hover {
                text-decoration: underline; /* Gạch chân khi hover */
            }
            .info-card {
                background-color: white;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
                margin-bottom: 20px; /* Khoảng cách giữa các phần */
            }
            table {
                width: 100%;
                border-collapse: collapse;
                margin-top: 10px;
            }
            th, td {
                border: 1px solid #d1d5db;
                padding: 10px; /* Tăng padding cho ô bảng */
                text-align: left;
            }
            th {
                background-color: #f9fafb;
                font-weight: bold; /* Đậm tiêu đề bảng */
            }
            img {
                max-width: 70px; /* Tăng kích thước hình ảnh */
                max-height: 70px;
                object-fit: cover;
                border-radius: 4px;
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
        </style>
    </head>

    <body>
        <div class="sidebar">
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
                </a>            <a class="flex items-center p-2 hover:bg-blue-800" href="eventList">
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

        <div class="main-content">
            <div class="header">
                <div class="flex items-center">

                    <h1 class="text-xl font-bold ml-4">Order List</h1>

                </div>
                <div class="flex items-center">
                    <i class="fas fa-bell mr-4"></i>
                    <a href="readAccount" class="fas fa-user-circle mr-4"></a>
                    <span>Shipper</span>
                    <a href="logout" class="fas fa-sign-out-alt ml-4"></a>
                </div>
            </div>

            <div >
                <div class="bg-white rounded shadow">
                    <div class="flex-1  overflow-y-auto">

                        <!-- Thanh Navbar trạng thái -->
                        <div class="flex justify-between items-center mb-6 bg-white p-4 shadow rounded-lg">
                            <div class="flex items-center space-x-2">
                                <input id="dateRangePicker" class="border rounded px-2 py-1" type="text" placeholder="Select date range" />
                            </div>
                            <div class="flex space-x-4">
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
                                        <!-- Chỉ hiển thị khi trạng thái đơn hàng là 'Pending' -->
                                        <c:if test="${order.orderStatus eq 'pending'}">
                                            <form action="OrderListForStaffController" method="POST" class="flex items-center mb-2">
                                                <input type="hidden" name="orderID" value="${order.orderID}"/>
                                                <select name="shipperID" class="select" required>
                                                    <option value="">Choose Shipper</option>
                                                    <c:forEach var="shipper" items="${shipperList}">
                                                        <option value="${shipper.accountID}">${shipper.username} (${shipper.totalDeliveries})</option>
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
            </div>

    </body>
</html>