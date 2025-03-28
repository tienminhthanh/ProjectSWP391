<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

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
                font-weight: normal; /* Tắt in đậm cho tiêu đề bảng */
            }
            img {
                max-width: 70px; /* Tăng kích thước hình ảnh */
                max-height: 70px;
                object-fit: cover;
                border-radius: 4px;
            }
            .order-info {
                display: flex; /* Sử dụng Flexbox cho thông tin đơn hàng */
                justify-content: space-between; /* Chia đều không gian giữa hai bên */
                margin-top: 20px; /* Khoảng cách trên */
            }
            .order-info div {
                flex: 1; /* Mỗi phần chiếm 1/2 chiều rộng */
                margin-right: 20px; /* Khoảng cách giữa hai phần */
            }
            .order-info div:last-child {
                margin-right: 0; /* Không có khoảng cách bên phải cho phần cuối */
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
                    <h1 class="text-xl font-bold ml-4">Order Detail</h1>
                </div>

            </div>  
            <div class="p-4">
                <div class="bg-white p-4 rounded shadow">
                    <div class="flex justify-between items-center border-b pb-2 mb-4">
                        <div class="space-x-2">
                            <c:if test="${ account.role eq 'admin'}">
                                <form action="OrderDetailForStaffController" method="POST" onsubmit="return confirmCancel(event);">
                                    <input type="hidden" name="orderID" value="${orderInfo.orderID}">
                                    <input type="hidden" name="customerID" value="${customer.accountID}">
                                    <input type="hidden" name="action" value="cancel">
                                    <c:if test="${ orderInfo.orderStatus eq 'pending'}">
                                        <button type="submit" class="bg-gray-200 text-black px-4 py-2 rounded">CANCEL ORDER</button>
                                    </c:if>
                                </form>
                            </c:if>
                            <c:if test="${order.orderStatus eq 'pending'}">
                                <form action="OrderListForStaffController" method="POST" class="inline" onsubmit="return confirmUpdate()">
                                    <input type="hidden" name="orderID" value="${order.orderID}"/>
                                    <select name="shipperID" class="select" required>
                                        <option value="">Choose Shipper</option>
                                        <c:forEach var="shipper" items="${shipperList}">
                                            <option value="${shipper.accountID}">${shipper.username} (${shipper.totalDeliveries})</option>
                                        </c:forEach>
                                    </select>
                                    <button type="submit" class="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700">Assign</button>
                                </form>
                            </c:if>

                        </div>
                    </div>

                    <div class="order-info">
                        <div>
                            <h1 class="text-2xl font-bold mb-2">Order Information (orderID: ${orderInfo.orderID}) </h1>
                            <p><i class="fas fa-user icon"></i> Customer: ${customer.firstName} ${customer.lastName}</p>
                            <p><i class="fas fa-phone icon"></i> Phone: ${customer.phoneNumber}</p>
                            <p><i class="fas fa-map-marker-alt icon"></i> Address: ${orderInfo.deliveryAddress}</p>
                            <p><i class="fas fa-truck icon"></i> Status: ${orderInfo.orderStatus}</p>

                            <p>
                                <i class="fas fa-calendar-alt icon"></i> Order Date: 
                                <fmt:formatDate value="${orderInfo.orderDate}" pattern="dd/MM/yyyy"/>
                            </p>

                            <p>
                                <i class="fas fa-calendar-alt icon"></i> Expected Delivery Date: 
                                <fmt:formatDate value="${orderInfo.expectedDeliveryDate}" pattern="dd/MM/yyyy"/>
                            </p>

                            <p><i class="fas fa-credit-card icon"></i> Payment Method: ${orderInfo.paymentMethod}</p>                    
                        </div>
                        <div>
                            <h1 class="text-2xl font-bold mb-2"> Payment Information</h1>
                            <p><i class="fas fa-tags icon"></i> Discount: <fmt:formatNumber value="${valueVoucher}" pattern="#,##0"/> đ</p>
                            <p><i class="fas fa-coins icon"></i> Total Amount: <fmt:formatNumber value="${orderInfo.preVoucherAmount}" pattern="#,##0"/> đ</p>
                            <p><i class="fas fa-check-circle icon mb-2""></i> Payment Status: ${orderInfo.paymentStatus}</p>

                            <c:if test="${account.role eq 'admin' and orderInfo.orderStatus ne 'pending' }">
                                <h1 class="text-2xl font-bold ">Order Processing Information</h1>   
                                <c:forEach var="handler" items="${handlerList}">
                                    <c:if test="${orderInfo.orderStatus ne 'pending' and orderInfo.orderStatus ne 'canceled'  }">
                                        <c:if test="${handler.role eq 'admin' or handler.role eq 'staff'}">
                                            <p class="text-lg">Order Processor (${handler.role}): <span>${handler.lastName} ${handler.firstName}</span></p>
                                            <p class="text-lg">${handler.role} ID: <span>${handler.accountID}</span></p>
                                        </c:if>
                                        <c:if test="${handler.role eq 'shipper'}">
                                            <p class="text-lg">Assigned Shipper: <span>${handler.lastName} ${handler.firstName}</span></p>
                                            <p class="text-lg">Shipper ID: <span>${handler.accountID}</span></p>
                                        </c:if>
                                    </c:if>                 
                                    <c:if test="${orderInfo.orderStatus eq 'canceled' and fn:length(handlerList) == 1 and handler.role eq 'customer' }">
                                        <p class="text-lg">Order Processor (${handler.role}): <span>${handler.lastName} ${handler.firstName}</span></p>
                                        <p class="text-lg">${handler.role} ID: <span>${handler.accountID}</span></p>

                                    </c:if>   
                                    <c:if test="${orderInfo.orderStatus eq 'canceled' and fn:length(handlerList) > 1 and (handler.role eq 'staff' or handler.role eq 'admin' )}">
                                        <p class="text-lg">Order Processor  (${handler.role}): <span>${handler.lastName} ${handler.firstName}</span></p>
                                        <p class="text-lg">${handler.role} ID: <span>${handler.accountID}</span></p>                           

                                    </c:if>
                                    <c:if test="${orderInfo.orderStatus eq 'canceled' and handler.role eq 'shipper'}">
                                        <p class="text-lg">Order refunded by (${handler.role}): <span>${handler.lastName} ${handler.firstName}</span></p>
                                        <p class="text-lg">${handler.role} ID: <span>${handler.accountID}</span></p>
                                    </c:if>
                                </c:forEach>

                            </c:if>

                        </div>
                    </div>
                    <table class="w-full text-left">
                        <thead>
                            <tr>
                                <th>Product Image</th>
                                <th>Product Name</th>
                                <th>Quantity</th>
                                <th>Price </th>
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
        <script>
            function confirmCancel(event) {
                if (confirm("Are you sure you want to cancel this order?")) {
                    alert("Order has been successfully canceled!"); // Hiển thị thông báo sau khi bấm OK
                    return true; // Tiếp tục submit form
                } else {
                    event.preventDefault(); // Ngăn không gửi form nếu chọn Cancel
                    return false;
                }
            }
        </script>
    </body>
</html>
