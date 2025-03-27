<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Order List</title>
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
                background-color: #f6ad55;

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
/*                width: 25px;*/
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
    <body class="bg-gray-50 min-h-screen flex">



        <div class="w-64 bg-orange-400 text-white min-h-screen">
            <jsp:include page="navbarAdmin.jsp" flush="true"/> 
        </div>
        <div class="main-content overflow-y-auto">
            <div class="header">
                <div class="flex items-center   ">
                    <!-- Main Content -->
                    <div class="flex-1 overflow-y-auto ">
                        <div class="w-full max-w-full bg-white p-8 shadow-lg rounded-lg">
                            <h1 class="text-3xl font-bold text-gray-800 mb-3">📦 Order List</h1>
                            <hr class="mb-6 border-gray-300"/>
                            <div class="flex justify-between items-center mb-6">
                                <div class="flex space-x-4">                              
                                    <a href="OrderListForStaffController?status=pending" class="px-4 py-2 rounded-lg text-white ${currentStatus == 'pending' ? 'bg-blue-600' : 'bg-gray-400'}">Pending</a>
                                    <a href="OrderListForStaffController?status=shipped" class="px-4 py-2 rounded-lg text-white ${currentStatus == 'shipped' ? 'bg-blue-600' : 'bg-gray-400'}">Shipped</a>
                                    <a href="OrderListForStaffController?status=completed" class="px-4 py-2 rounded-lg text-white ${currentStatus == 'completed' ? 'bg-blue-600' : 'bg-gray-400'}">Completed</a>
                                    <a href="OrderListForStaffController?status=canceled" class="px-4 py-2 rounded-lg text-white ${currentStatus == 'canceled' ? 'bg-blue-600' : 'bg-gray-400'}">Canceled</a>
                                </div>
                            </div>
                            <!-- TABLE -->
                            <div class="overflow-x-auto rounded-lg shadow-md">
                                <table class="table-fixed min-w-full bg-white border border-gray-200">
                                    <thead class="bg-orange-400 text-white">
                                        <tr>
                                            <th class="px-4 py-3 border border-b w-[80px]">Order ID</th>
                                            <th class="px-4 py-3 border border-b w-[200px]">Customer Name</th>
                                            <th class="px-4 py-3 border border-b w-[150px]">Address</th>
                                            <th class="px-4 py-3 border border-b w-[100px]">Status</th>
                                            <th class="px-4 py-3 border border-b w-[120px]">Order Date</th>
                                            <th class="px-4 py-3 border border-b w-[120px]">Total</th>
                                                <c:if test="${currentStatus eq 'pending'}">
                                                <th class="px-4 py-3 border border-b w-[150px]">Actions</th>
                                                </c:if>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="order" items="${orderList}">
                                            <tr class="hover:bg-gray-100 transition duration-300 cursor-pointer"
                                                onclick="window.location.href = 'OrderDetailForStaffController?id=${order.orderID}';">
                                                <td class="px-4 py-3 border border-b text-center">${order.orderID}</td>
                                                <td class="px-4 py-3 border border-b text-left">${customerMap[order.orderID].firstName} ${customerMap[order.orderID].lastName}</td>
                                                <td class="px-4 py-3 border border-b text-left">${order.deliveryAddress}</td>
                                                <td class="px-4 py-3 border border-b text-center">${order.orderStatus}</td>
                                                <td class="px-4 py-3 border border-b text-center">
                                                    <fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy"/>
                                                </td>
                                                <td class="px-4 py-3 border border-b text-right">
                                                    <fmt:formatNumber value="${order.preVoucherAmount}" pattern="#,##0"/> đ
                                                </td>

                                                <%-- Chỉ hiển thị Action khi orderStatus là "pending" --%>
                                                <c:if test="${order.orderStatus eq 'pending'}">
                                                    <td class="px-4 py-3 border border-b text-center" onclick="event.stopPropagation();">
                                                        <form action="OrderListForStaffController" method="POST" class="inline" onsubmit="return confirmUpdate()" onclick="event.stopPropagation();">
                                                            <input type="hidden" name="orderID" value="${order.orderID}"/>
                                                            <select name="shipperID" class="select" required onclick="event.stopPropagation();">
                                                                <option value="">Choose Shipper</option>
                                                                <c:forEach var="shipper" items="${shipperList}">
                                                                    <option value="${shipper.accountID}">${shipper.username} (${shipper.totalDeliveries})</option>
                                                                </c:forEach>
                                                            </select>
                                                            <button type="submit" class="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700" onclick="event.stopPropagation();">Assign</button>
                                                        </form>
                                                    </td>
                                                </c:if>
                                            </tr>

                                        </c:forEach>

                                        <c:if test="${empty orderList}">
                                            <tr>
                                                <td class="px-4 py-3 border-b text-center text-gray-500 italic" colspan="7">No orders found.</td>
                                            </tr>
                                        </c:if>
                                    </tbody>
                                </table>
                            </div>

                            <!-- Pagination -->
                            <c:if test="${totalPage > 1}">
                                <div class="flex justify-center mt-6">
                                    <nav class="flex space-x-2">
                                        <!-- Previous Button -->
                                        <c:if test="${currentPage > 1}">
                                            <a href="OrderListForStaffController?page=${currentPage - 1}"
                                               class="px-4 py-2 bg-gray-300 text-gray-700 rounded hover:bg-gray-400 transition">
                                                &laquo; Previous
                                            </a>
                                        </c:if>

                                        <!-- Page Numbers -->
                                        <c:forEach var="i" begin="1" end="${totalPage}">
                                            <a href="OrderListForStaffController?page=${i}"
                                               class="px-4 py-2 rounded ${i == currentPage ? 'bg-orange-400 text-white' : 'bg-gray-200 text-gray-800 hover:bg-gray-300 transition'}">
                                                ${i}
                                            </a>
                                        </c:forEach>

                                        <!-- Next Button -->
                                        <c:if test="${currentPage < totalPage}">
                                            <a href="OrderListForStaffController?page=${currentPage + 1}"
                                               class="px-4 py-2 bg-gray-300 text-gray-700 rounded hover:bg-gray-400 transition">
                                                Next &raquo;
                                            </a>
                                        </c:if>
                                    </nav>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script>
            function confirmUpdate() {
                return confirm("Are you sure you want to assign this shipper?");
            }
        </script>
    </body>
</html>
