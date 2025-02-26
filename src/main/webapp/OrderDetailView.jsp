<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Order Detail</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    </head>
    <body class="bg-gray-100">
        <header class="bg-white shadow">
            <div class="container mx-auto px-4 py-2 flex justify-between items-center">
                <div class="logo">
                    <a href="home">
                        <img src="img/logo.png" alt="WIBOOKS" class="h-10"/>
                    </a> 
                </div>
                <a href="logout" class="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600 flex items-center">
                    <i class="fas fa-sign-out-alt mr-2"></i> Sign-out
                </a>
            </div>
        </header>

        <div class="container mx-auto px-4 py-6">
            <div class="mb-4">
                <h1 class="text-2xl font-bold">Order Detail</h1>
            </div>
            <div class="bg-white shadow-md rounded-lg p-6">
                <div class="flex flex-wrap -mx-4">
                    <!-- Thông tin đơn hàng bên trái -->
                    <div class="w-full md:w-1/2 px-4">
                        <h2 class="text-xl font-semibold mb-4">Order Information</h2>
                        <p><strong>Order ID:</strong> ${orderInfo.orderID}</p>
                        <p><strong>Order Date:</strong> ${orderInfo.orderDate}</p>
                        <p><strong>Shipping Address:</strong> ${orderInfo.deliveryAddress}</p>
                        <p><strong>Payment Method:</strong> ${orderInfo.paymentMethod}</p>
                        <p><strong>Order Status:</strong> ${orderInfo.orderStatus}</p>

                        <h3 class="text-lg font-semibold mt-4">Products</h3>
                        <c:forEach var="item" items="${orderInfo.orderProductList}">
                            <p><strong>Product Name:</strong> ${item.product.productName}</p>
                            <p><strong>Quantity:</strong> ${item.quantity}</p>
                            <p><strong>Price:</strong> ${item.priceWithQuantity} VND</p>
                            <hr class="my-2">
                        </c:forEach>

                        <p class="mt-4 text-lg font-bold">Total Order: ${orderInfo.preVoucherAmount} VND</p>
                        <p><strong>Payment Method:</strong> ${methodName}</p>

                        <div class="flex space-x-2 mt-4">
                            <a href="updateOrder?id=${orderID}" class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">Update</a>
                            <a href="deleteOrder?id=${orderID}" class="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600" onclick="return confirm('Are you sure you want to delete item with ID = ${orderID}?')">Cancel</a>
                        </div>
                    </div>

                    <!-- Hình ảnh sản phẩm bên phải -->
                    <div class="w-full md:w-1/2 px-4">
                        <h3 class="text-lg font-semibold mb-4">Product Images</h3>
                        <div class="product-list grid grid-cols-2 md:grid-cols-3 lg:grid-cols-3 gap-4">
                            <c:forEach var="item" items="${orderInfo.orderProductList}">
                                <div class="product-item bg-white shadow-md rounded-lg p-4 flex flex-col items-center">
                                    <img src="${item.product.imageURL}" alt="${item.product.productName}" class="w-24 h-24 object-cover rounded">
                                    <p class="mt-2 text-center text-sm font-semibold">${item.product.productName}</p>
                                    <p class="text-center text-sm">Quantity: ${item.quantity}</p>
                                    <p class="text-center text-sm font-bold">${item.priceWithQuantity} VND</p>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </body>
</html>
