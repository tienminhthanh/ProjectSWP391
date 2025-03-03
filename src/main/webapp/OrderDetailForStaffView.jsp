<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Order Detail</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <style>
            body {
                font-family: Arial, sans-serif;
                background-color: #f3f4f6;
                padding: 20px;
            }
            .card {
                background-color: white;
                padding: 20px;
                border-radius: 10px;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
                margin-bottom: 16px;
            }
            .button {
                background-color: #3B82F6;
                color: white;
                padding: 10px 15px;
                border-radius: 6px;
                text-decoration: none;
                display: inline-block;
                margin-top: 10px;
            }
        </style>
    </head>
    <body>
        <div class="card">
            <h1 class="text-xl font-bold mb-4">Order Detail</h1>
            <p><strong>Order ID:</strong> ${order.orderID}</p>
            <p><strong>Customer Name:</strong> ${customer.firstName} ${customer.lastName}</p>
            <p><strong>Customer Phone:</strong> ${customer.phone}</p>
            <p><strong>Delivery Address:</strong> ${order.deliveryAddress}</p>
            <p><strong>Order Status:</strong> ${order.orderStatus}</p>
            <p><strong>Order Date:</strong> ${order.orderDate}</p>
            <p><strong>Payment Method:</strong> ${order.paymentMethod}</p>
            <p><strong>Total Amount:</strong> ${order.totalAmount}</p>
            <p><strong>Discount / Voucher:</strong> ${order.voucherCode}</p>
            <p><strong>Payment Status:</strong> ${order.paymentStatus}</p>
            <p><strong>Order Notes:</strong> ${order.orderNotes}</p>
        </div>

        <div class="card">
            <h2 class="text-lg font-semibold mb-3">Order Items</h2>
            <table class="w-full border-collapse border border-gray-300">
                <thead>
                    <tr class="bg-gray-100">
                        <th class="border border-gray-300 px-4 py-2">Product Image</th>
                        <th class="border border-gray-300 px-4 py-2">Product Name</th>
                        <th class="border border-gray-300 px-4 py-2">Quantity</th>
                        <th class="border border-gray-300 px-4 py-2">Price</th>
                        <th class="border border-gray-300 px-4 py-2">Stock Available</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${orderItems}">
                        <tr>
                            <td class="border border-gray-300 px-4 py-2 text-center">
                                <img src="${item.productImage}" alt="${item.productName}" class="w-16 h-16 object-cover rounded">
                            </td>
                            <td class="border border-gray-300 px-4 py-2">${item.productName}</td>
                            <td class="border border-gray-300 px-4 py-2">${item.quantity}</td>
                            <td class="border border-gray-300 px-4 py-2">${item.price}</td>
                            <td class="border border-gray-300 px-4 py-2">${item.stockAvailable}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <a href="OrderListForStaffController" class="button">Back to Order List</a>
    </body>
</html>
