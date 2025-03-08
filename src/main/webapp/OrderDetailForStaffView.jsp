<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Order Detail</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <style>
            body {
                font-family: Arial, sans-serif;
                background-color: #f3f4f6;

                margin: 0;
                display: flex;  /*Sử dụng Flexbox cho body */
            }
            .sidebar {
                background-color: #4CAF50; /* Xanh lá */
                color: white;
                padding: 16px;
                min-height: 100vh;
                width: 250px; /* Đặt chiều rộng cho sidebar */
                box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1); /* Thêm bóng cho sidebar */
            }
            .container {
                display: flex; /* Sử dụng Flexbox cho container */
                flex: 1; /* Chiếm toàn bộ không gian còn lại */
                margin: 20px 50px 50px 50px; /* Khoảng cách giữa sidebar và container */
            }
            .info {
                flex: 1; /* Chiếm không gian bên trái */
                margin-right: 20px; /* Khoảng cách giữa thông tin và sản phẩm */
            }
            .product-info {
                flex: 1; /* Chiếm không gian bên phải */
            }
            .info-card {
                background-color: #ffffff; /* Màu nền trắng */
                padding: 20px; /* Padding cho card */
                border-radius: 8px; /* Bo góc */
                box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1); /* Bóng đổ */
                margin-bottom: 20px; /* Khoảng cách giữa các phần */
            }

            .info-card p {
                font-size: 1.1rem; /* Tăng kích thước font chữ */
                line-height: 1.5; /* Tăng khoảng cách giữa các dòng */
                margin-bottom: 10px; /* Khoảng cách giữa các đoạn */
            }



            .button {
                background-color: #FF9800; /* Cam */
                color: white; /* Màu chữ cho nút */
                padding: 12px 20px; /* Tăng padding cho nút */
                border-radius: 6px; /* Bo góc cho nút */
                text-decoration: none; /* Bỏ gạch chân */
                display: inline-block; /* Hiển thị inline-block */
                margin-top: 15px; /* Khoảng cách trên nút */
                transition: background-color 0.3s; /* Hiệu ứng chuyển màu */
                font-size: 1.1rem; /* Kích thước font cho nút */
            }
            .button:hover {
                background-color: #2563eb;
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
                font-size: 0.95rem; /* Kích thước font cho bảng */
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

        <div class="flex flex-col items-center mb-6 bg-white p-4 shadow rounded-lg">           
            <div>
             <h1 class="text-2xl font-bold mb-2">Order Detail</h1>
            </div>
            <div class="container">
                <div class="info">
                    <div class="info-card">
                        <h1 class="text-2xl font-bold mb-2">Order Infomation</h1>
                        <p><i class="fas fa-receipt icon"></i><strong>Order ID:</strong> ${orderInfo.orderID}</p>
                        <p><i class="fas fa-user icon"></i><strong>Customer:</strong> ${customer.firstName} ${customer.lastName}</p>
                        <p><i class="fas fa-phone icon"></i><strong>Phone:</strong> ${customer.phoneNumber}</p>
                        <p><i class="fas fa-map-marker-alt icon"></i><strong>Address:</strong> ${orderInfo.deliveryAddress}</p>
                        <p><i class="fas fa-truck icon"></i><strong>Status:</strong> ${orderInfo.orderStatus}</p>
                        <p><i class="fas fa-calendar-alt icon"></i><strong>Order Date:</strong> ${orderInfo.orderDate}</p>
                        <p><i class="fas fa-credit-card icon"></i><strong>Payment Method:</strong> ${orderInfo.paymentMethod}</p>                    
                        <p><i class="fas fa-tags icon"></i><strong>Discount:</strong> <fmt:formatNumber value="${valueVoucher}" pattern="#,##0"/> đ</p>
                        <p><i class="fas fa-coins icon"></i><strong>Total Amount:</strong> <fmt:formatNumber value="${orderInfo.preVoucherAmount}" pattern="#,##0"/> đ</p>
                        <p><i class="fas fa-check-circle icon"></i><strong>Payment Status:</strong> ${orderInfo.paymentStatus}</p>
                    </div>
                    <a href="OrderListForStaffController" class="button">Back to Order List</a>
                </div>

                <div class="product-info">
                    <div class="card cart-card">
                        <h2 class="text-lg font-semibold mb-2">Order Items</h2>
                        <table>
                            <thead>
                                <tr>
                                    <th>Product Image</th>
                                    <th>Product Name</th>
                                    <th>Quantity</th>
                                    <th>Price With Quantity</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${orderInfo.orderProductList}">
                                    <tr>
                                        <td class="text-center">
                                            <img src="${item.product.imageURL}" alt="${item.product.productName}">
                                        </td>
                                        <td>${item.product.productName}</td>
                                        <td>${item.quantity}</td>
                                        <td><fmt:formatNumber value="${item.priceWithQuantity}" pattern="#,##0"/> đ</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

    </body>
</html>