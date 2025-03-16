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
                /*                margin-left: 250px;  Dịch nội dung chính sang phải, tránh bị sidebar che 
                                padding: 20px;*/
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

        <div class="w-64 bg-orange-400 text-white min-h-screen">
            <jsp:include page="navbarAdmin.jsp" flush="true"/> 
        </div>
        <div class="main-content">
            <div class="header">
                <div class="flex items-center">

                    <h1 class="text-xl font-bold ml-4">Order List</h1>

                </div>
                <div class="flex items-center">
                    <i class="fas fa-bell mr-4"></i>
                    <a href="readAccount" class="fas fa-user-circle mr-4"></a>
                    <span>${account.role}</span>
                    <a href="logout" class="fas fa-sign-out-alt ml-4"></a>
                </div>
            </div>

            <div >
                <div class="bg-white rounded shadow">
                    <div class="flex-1 max-h-[630px] overflow-y-auto">

                        <!-- Thanh Navbar trạng thái -->
                        <div class="flex justify-between items-center mb-6 bg-white p-4 shadow rounded-lg">
                            <div class="flex items-center space-x-2">
                                <!--                                <input id="dateRangePicker" class="border rounded px-2 py-1" type="text" placeholder="Select date range" />-->
                            </div>
                            <div class="flex space-x-4">                              
                                <a href="OrderListForStaffController?status=pending" class="px-4 py-2 rounded-lg text-white
                                   ${currentStatus == 'pending' ? 'bg-blue-600' : 'bg-gray-400'}">Pending</a>

                                <a href="OrderListForStaffController?status=shipped" class="px-4 py-2 rounded-lg text-white
                                   ${currentStatus == 'shipped' ? 'bg-blue-600' : 'bg-gray-400'}">Shipped</a>

                                <a href="OrderListForStaffController?status=completed" class="px-4 py-2 rounded-lg text-white
                                   ${currentStatus == 'completed' ? 'bg-blue-600' : 'bg-gray-400'}">Completed</a>

                                <a href="OrderListForStaffController?status=canceled" class="px-4 py-2 rounded-lg text-white
                                   ${currentStatus == 'canceled' ? 'bg-blue-600' : 'bg-gray-400'}">Canceled</a>
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
                                        <p>
                                            <i class="fas fa-calendar-alt icon"></i> Order Date: 
                                            <fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy"/>
                                        </p>                                        <p>
                                            <i class="fas fa-calendar-alt icon"></i> Expected Delivery Date: 
                                            <fmt:formatDate value="${order.expectedDeliveryDate}" pattern="dd/MM/yyyy"/>
                                        </p>
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
        </div>
    </body>
</html>
