<%-- 
    Document   : DeliveryViewOfShipperView
    Created on : Mar 2, 2025, 12:10:14 PM
    Author     : Macbook
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order List</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
</head>
<body class="bg-gray-100">
    <div class="bg-orange-600 text-white p-4 flex justify-between items-center shadow-lg">
        <div class="flex items-center space-x-4">
            <img src="./img/logo.png" alt="Logo Wibooks" class="h-10 w-25">
            <h1 class="text-2xl font-bold">Giao hàng</h1>
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
    <div class="p-6">
        <div class="bg-white p-6 rounded-lg shadow-md">
            <h2 class="text-2xl font-bold mb-4">Orders</h2>
            <table class="w-full text-left border border-gray-300 rounded-lg overflow-hidden">
                <thead>
                    <tr class="bg-gray-200 border-b">
                        <th class="py-3 px-4 text-gray-700">Order ID</th>
                        <th class="py-3 px-4 text-gray-700">Supplier</th>
                        <th class="py-3 px-4 text-gray-700">Status</th>
                        <th class="py-3 px-4 text-gray-700">Total</th>
                        <th class="py-3 px-4 text-gray-700">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <tr class="border-b hover:bg-gray-100 transition duration-200">
                        <td class="py-3 px-4">P00013</td>
                        <td class="py-3 px-4">AFA</td>
                        <td class="py-3 px-4 text-green-500">In Progress</td>
                        <td class="py-3 px-4">750,000 đ</td>
                        <td class="py-3 px-4">
                            <a href="orderDetail.jsp?id=P00013" class="text-blue-500 hover:underline">View</a>
                        </td>
                    </tr>
                    <tr class="border-b hover:bg-gray-100 transition duration-200">
                        <td class="py-3 px-4">P00014</td>
                        <td class="py-3 px-4">XYZ Corp</td>
                        <td class="py-3 px-4 text-yellow-500">Pending</td>
                        <td class="py-3 px-4">1,200,000 đ</td>
                        <td class="py-3 px-4">
                            <a href="orderDetail.jsp?id=P00014" class="text-blue-500 hover:underline">View</a>
                        </td>
                    </tr>
                    <tr class="border-b hover:bg-gray-100 transition duration-200">
                        <td class="py-3 px-4">P00015</td>
                        <td class="py-3 px-4">DEF Ltd</td>
                        <td class="py-3 px-4 text-red-500">Canceled</td>
                        <td class="py-3 px-4">500,000 đ</td>
                        <td class="py-3 px-4">
                            <a href="orderDetail.jsp?id=P00015" class="text-blue-500 hover:underline">View</a>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>