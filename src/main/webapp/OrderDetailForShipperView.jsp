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
            .description {
                max-width: 200px;
                white-space: nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
            }
        </style>
    </head>

    <body class="bg-gray-100">
        <div class="bg-orange-600 text-white p-4 flex justify-between items-center">
            <div class="flex items-center space-x-4">
                <img src="./img/logo.png" alt="Wibooks Logo" class="h-10 w-25">
                <h1 class="text-xl font-bold">Delivery</h1>

            </div>
            <div class="flex items-center space-x-4">
                <i class="fas fa-bell"></i>
                <a href="readAccount" class="fas fa-user-circle"></a>
                <span>Shipper</span>
                <a href="logout" class="fas fa-sign-out-alt"></a>
            </div>
        </div>

        <div class="p-4">
            <div class="bg-white p-4 rounded shadow">
                <div class="flex justify-between items-center border-b pb-2 mb-4">
                    <h2 class="text-xl font-bold">Delivery Information / <span>${orderInfo.orderID}</span></h2>
                    <div class="space-x-2">
                        <form action="OrderListForShipperController" method="post" class="inline" id="orderForm">
                            <input type="hidden" name="orderID" value="${orderInfo.orderID}">
                            <input type="hidden" id="actionType" name="actionType" value="">

                            <c:if test="${ orderInfo.deliveryStatus eq 'shipped' }">      
                                <c:if test="${ orderInfo.paymentStatus ne 'paid' }">      
                                    <button type="button" class="bg-orange-500 text-white px-4 py-2 rounded"
                                            onclick="submitForm('cancel')">Cancel</button>
                                </c:if>
                                <button type="button" class="bg-orange-500 text-white px-4 py-2 rounded"
                                        onclick="submitForm('update')">Update Status</button>
                            </c:if>
                        </form>
                        <button onclick="history.back()" class="bg-blue-500 text-white px-4 py-2 rounded">BACK</button>
                    </div>

                </div>

                <div class="flex justify-between items-center mb-4">
                    <div class="space-y-2">
                        <h3 class="text-lg font-bold">Shipping Details</h3>
                        <p><strong>Order ID:</strong> ${orderInfo.orderID}</p>
                        <p><strong>Delivery Address:</strong> <span class="font-bold">${orderInfo.deliveryAddress}</span></p>
                        <p><strong>Recipient:</strong> <span class="font-bold">${customer.firstName} ${customer.lastName}</span></p>
                        <p><strong>Phone Number:</strong> <span class="font-bold">${customer.phoneNumber}</span></p>
                    </div>
                    <div class="space-y-2 text-right">

                        <p><strong>Delivery Deadline:</strong> 
                            <span class="font-bold">
                                <fmt:formatDate value="${orderInfo.expectedDeliveryDate}" pattern="dd/MM/yyyy"/>
                            </span>
                        </p>

                        <p><strong>Status:</strong> 
                            <span class="font-bold text-green-500">${orderInfo.deliveryStatus}</span>
                        </p>
                        <!--                        <p><strong>Notes:</strong> <span class="font-bold">Deliver before 5 PM</span></p>-->
                    </div>
                </div>

                <div class="border-b pb-2 mb-4">
                    <div class="flex space-x-4">
                        <button class="border-b-2 border-orange-500 pb-2">Products</button>

                    </div>
                </div>

                <table class="w-full text-left">
                    <thead>
                        <tr class="border-b">
                            <th class="py-2">Product</th>
                            <th class="py-2">Description</th>
                            <th class="py-2">Quantity</th>
                            <th class="py-2">Price</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="item" items="${orderInfo.orderProductList}">
                            <tr class="border-b">
                                <td class="py-2 flex items-center">
                                    <img src="${item.product.imageURL}" alt="${item.product.productName}" class="w-16 h-16 object-cover mr-2">
                                    <span>   ${item.product.productName}</span>
                                </td>
                                <td class="py-2 description">${item.product.description}</td>
                                <td class="py-2">${item.quantity}</td>
<!--                                <td class="py-2"><fmt:formatNumber value="${item.product.price}" pattern="#,##0"/> đ</td>-->
                                <td class="py-2"><fmt:formatNumber value="${item.priceWithQuantity}" pattern="#,##0"/> đ</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

                <div class="mt-4">
                    <p><strong>Discount / Voucher:</strong> <fmt:formatNumber value="${valueVoucher}" pattern="#,##0"/> đ</p>
                    <p><strong>Total Amount:</strong> <fmt:formatNumber value="${orderInfo.preVoucherAmount}" pattern="#,##0"/> đ</p>
                    <p><strong>Payment Status:</strong> ${orderInfo.paymentStatus}</p>
                </div>

            </div>
        </div>
        <script>
            function setAction(action) {
                document.getElementById("actionType").value = action;
            }

            function confirmUpdate() {
                return confirm("Are you sure you want to update this order?");
            }

            function confirmCancel() {
                return confirm("Are you sure you want to cancel this order?");
            }

            function submitForm(action) {
                if (confirm(action === 'cancel' ? "Are you sure you want to cancel this order?"
                        : "Are you sure you want to update the status?")) {
                    document.getElementById("actionType").value = action;
                    document.getElementById("orderForm").submit();
                }
            }

        </script>

    </body>
</html>
