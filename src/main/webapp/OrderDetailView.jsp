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
         <link href="css/styleHeader.css" rel="stylesheet">
   <!--Script for include icons-->
        <script src="https://kit.fontawesome.com/bfab6e6450.js" crossorigin="anonymous"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    </head>
    <body class="bg-gray-100">
        <div class="header-container">
            <jsp:include page="header.jsp" flush="true"/> 
        </div>
  

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

                        <p><strong>Order Date:</strong> 
                            <fmt:formatDate value="${orderInfo.orderDate}" pattern="dd/MM/yyyy"/>
                        </p>

                        <p><strong>Expected Delivery Date:</strong> 
                            <fmt:formatDate value="${orderInfo.expectedDeliveryDate}" pattern="dd/MM/yyyy"/>
                        </p>

                        <p><strong>Shipping Address:</strong> ${orderInfo.deliveryAddress}</p>
                        <p><strong>Payment Method:</strong> ${orderInfo.paymentMethod}</p>
                        <p><strong>Order Status:</strong> ${orderInfo.orderStatus}</p>

                        <h3 class="text-lg font-semibold mt-4">Products</h3>
                        <c:forEach var="item" items="${orderInfo.orderProductList}">
                            <p><strong>Product Name:</strong> ${item.product.productName}</p>
                            <p><strong>Quantity:</strong> ${item.quantity}</p>
                            <p><strong>Price:</strong> 
                                <fmt:formatNumber value="${item.priceWithQuantity}" type="number" groupingUsed="true"/> đ
                            </p>

                            <hr class="my-2">
                        </c:forEach>
                        <p><strong>Shipping Fee: </strong>  
                            <fmt:formatNumber value="${delivery.optionCost}" type="currency" currencySymbol="" groupingUsed="true" maxFractionDigits="0"/> đ
                        </p>

                        <p><strong>Discount: </strong>  
                            <fmt:formatNumber value="${voucher}" type="currency" currencySymbol="" groupingUsed="true" maxFractionDigits="0"/> đ
                        </p>
                        <p class="mt-4 text-lg font-bold">
                            Total Order: 
                            <fmt:formatNumber value="${orderInfo.preVoucherAmount}" type="number" groupingUsed="true"/> đ
                        </p>

                        <div class="flex space-x-2 mt-4">
                            <c:if test="${orderInfo.orderStatus eq 'pending'}">
                                <button type="button" onclick="showUpdateForm()" 
                                        class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">
                                    Update
                                </button>
                            </c:if>
                            <c:if test="${orderInfo.orderStatus eq 'pending'}">
                                <form action="DeleteOrderController" method="POST" 
                                      onsubmit="return confirm('Are you sure you want to delete item with ID = ${orderInfo.orderID}?')">
                                    <input type="hidden" name="id" value="${orderInfo.orderID}">
                                    <button type="submit" class="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600">
                                        Cancel
                                    </button>
                                </form>
                            </c:if>
                            <a href="OrderListController" class="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600">Back</a>
                            <!-- Nút xác nhận đã nhận hàng (ẩn mặc định, chỉ hiển thị khi trạng thái là 'Đang giao hàng') -->
                            <c:if test="${orderInfo.deliveryStatus eq 'delivered' and orderInfo.orderStatus eq 'shipped'}">                                <form action="OrderDetailController" method="POST">
                                    <input type="hidden" name="orderID" value="${orderInfo.orderID}">
                                    <button type="submit" class="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600 mt-4">
                                        Confirm Receipt
                                    </button>
                                </form>
                            </c:if>
                            <c:if test="${ orderInfo.orderStatus eq 'completed'}">                                <form action="OrderDetailController" method="POST">
                                    <input type="hidden" name="orderID" value="${orderInfo.orderID}">
                                </form>
                            </c:if>
                            <!-- Rate Button -->

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

                                    <!--                                    <p class="text-center text-sm font-bold">
                                    <fmt:formatNumber value="${item.priceWithQuantity}" type="number" groupingUsed="true"/> đ
                                </p>-->

                                    <c:if test="${ orderInfo.orderStatus eq 'completed'}">                                <form action="OrderDetailController" method="POST">
                                            <button type="button" onclick="openRatingPopup('${orderInfo.orderID}')" class="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600 mt-4">
                                                Rate
                                            </button>
                                        </c:if>

                                        <div id="ratingPopup" class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 hidden" onclick="closeRatingPopup(event)">
                                            <div class="bg-white p-6 rounded-lg shadow-lg w-96" onclick="event.stopPropagation()">
                                                <h2 class="text-xl font-bold mb-4">Rate Your Order</h2>
                                                <c:forEach var="item" items="${orderInfo.orderProductList}">
                                                    <div class="flex items-center space-x-4 border-b pb-3">
                                                        <img src="${item.product.imageURL}" alt="${item.product.productName}" class="w-24 h-24 object-cover rounded">
                                                        <div class="flex-1">
                                                            <p class="text-lg font-semibold">${item.product.productName}</p>

                                                            <!-- Hệ thống đánh giá sao cho từng sản phẩm -->
                                                            <div class="flex space-x-1 text-2xl" id="starContainer-${item.product.productID}">
                                                                <div class="flex justify-center mb-4" id="starContainer">
                                                                    <span class="text-gray-400 text-2xl cursor-pointer" data-star="1">★</span>
                                                                    <span class="text-gray-400 text-2xl cursor-pointer" data-star="2">★</span>
                                                                    <span class="text-gray-400 text-2xl cursor-pointer" data-star="3">★</span>
                                                                    <span class="text-gray-400 text-2xl cursor-pointer" data-star="4">★</span>
                                                                    <span class="text-gray-400 text-2xl cursor-pointer" data-star="5">★</span>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </c:forEach>
                                                <form action="OrderDetailController" method="POST">
                                                    <input type="hidden" name="orderID" id="orderID">
                                                    <input type="hidden" name="rating" id="rating" value="0">
                                                    <!-- Submit Button -->
                                                    <button type="submit" class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">Submit</button>

                                                    <!-- Close Button -->
                                                    <button type="button" onclick="closeRatingPopup()" class="bg-gray-400 text-white px-4 py-2 rounded ml-2 hover:bg-gray-500">Cancel</button>
                                                </form>
                                            </div>
                                        </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Form cập nhật địa chỉ giao hàng -->
        <div id="updateForm" class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 hidden">
            <div class="bg-white p-6 rounded-lg shadow-lg w-96">
                <h2 class="text-xl font-semibold mb-4">Update Shipping Address</h2>
                <form action="UpdateOrderController" method="POST">
                    <input type="hidden" name="orderID" value="${orderInfo.orderID}">
                    <label for="newAddress" class="block font-medium mb-2">New Address:</label>
                    <input type="text" name="newAddress" id="newAddress" class="w-full p-2 border rounded-lg mb-4" required>

                    <div class="flex justify-end space-x-2">
                        <button type="button" onclick="hideUpdateForm()" class="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600">Cancel</button>
                        <button type="submit" class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">Save</button>
                    </div>
                </form>
            </div>
        </div>

        <script>
            function showUpdateForm() {
                document.getElementById("updateForm").classList.remove("hidden");
            }

            function hideUpdateForm() {
                document.getElementById("updateForm").classList.add("hidden");
            }
        </script>
        <script>
            function openRatingPopup(orderID) {
                document.getElementById("ratingPopup").classList.remove("hidden");
                document.getElementById("orderID").value = orderID;
            }

            function closeRatingPopup(event) {
                if (!event || event.target.id === "ratingPopup") {
                    document.getElementById("ratingPopup").classList.add("hidden");
                }
            }

            document.querySelectorAll("#starContainer span").forEach(star => {
                star.addEventListener("click", function () {
                    let rating = this.getAttribute("data-star");
                    document.getElementById("rating").value = rating;

                    // Update star colors dynamically
                    document.querySelectorAll("#starContainer span").forEach(s => {
                        s.classList.toggle("text-yellow-500", s.getAttribute("data-star") <= rating);
                        s.classList.toggle("text-gray-400", s.getAttribute("data-star") > rating);
                    });
                });
            });
        </script>
    </body>
</html>