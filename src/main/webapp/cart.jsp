<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8"/>
        <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
        <title>Cart</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet"/>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous" />
        <link href="css/styleFooter.css" rel="stylesheet">
        <style>
            .logo {
                display: flex;
                justify-content: center;
                margin: 10px 0;
            }

            .logo img {
                max-width: 200px;
                height: auto;
            }

            /*            @media (max-width: 768px) {
                            .logo img {
                                max-width: 140px;
                            }
                        }*/

            /* Custom styles for the cart */
            .status-container {
                display: flex;
                justify-content: center;
                margin-bottom: 20px;
            }

            .status-item {
                width: 120px; /* Giảm độ rộng */
                text-align: center;
                padding: 10px;
                border: 2px solid #ddd;
                border-radius: 8px;
                margin: 0 10px;
                font-weight: bold;
                background-color: #f9fafb;
            }

            .status-item.active {
                border-color: #3b82f6;
                background-color: #3b82f6;
                color: white;
            }

            .items-in-cart {
                text-align: center;
                background-color: #1e3a8a;
                color: white;
                padding: 10px;
                border-radius: 8px 8px 0 0;
                margin-bottom: 0;
            }

            .quantity-input {
                width: 60px;
                text-align: center;
                padding: 5px;
                border: 1px solid #ddd;
                border-radius: 4px;
                margin-right: 10px;
            }
        </style>
    </head>
    <body class="bg-gray-100">
        <header class="bg-white shadow">
            <div class="container mx-auto px-4 py-2 flex justify-between items-center">
                <div class="logo">
                    <a href="home"><img src="img/logo.png" alt="WIBOOKS" /></a> 
                </div>
                <a href="logout" class="bg-red-500 text-white p-2 rounded hover:bg-red-600">
                    <i class="fas fa-sign-out-alt mr-2"></i> Sign-out
                </a>
            </div>
        </header>
        <main class="container mx-auto px-4 py-6">
            <div class="bg-white shadow rounded-lg p-4">
                <h2 class="text-2xl font-bold mb-4">Cart</h2>

                <!-- Trạng thái: 1 Cart, 2 Settlement, 3 Complete -->
                <div class="status-container">
                    <div class="status-item active">1 Cart</div>
                    <div class="status-item">2 Settlement</div>
                    <div class="status-item">3 Complete</div>
                </div>

                <!-- Kiểm tra nếu giỏ hàng rỗng -->
                <c:if test="${empty sessionScope.cartItems}">
                    <div class="p-4 text-center text-gray-500">
                        *Your cart is empty.
                    </div>
                    <div class="text-center text-sm text-gray-600">
                        ※Please make sure to check <span class="font-bold">the recommended environment</span> before purchasing.<br>
                        eBook(s) you are about to purchase can be viewed only on the WIBOOK app (for iOS and Android) or the Browser Viewer. Please check if you can view the eBook with a free title before purchasing.
                    </div>
                </c:if>

                <!-- Nếu có CartItem -->
                <c:if test="${not empty sessionScope.cartItems}">
                    <div class="items-in-cart">Items in Cart</div>
                    <div class="border border-gray-300 rounded-b-lg">
                        <c:forEach var="item" items="${sessionScope.cartItems}">
                            <div class="flex items-center p-4 border-b border-gray-300">
                                <!-- Hiển thị hình ảnh sản phẩm -->
                                <div class="w-13 h-19 bg-gray-200 flex items-center justify-center mr-4">
                                    <a href="productDetails?productID=${item.productID}" class="product-image">
                                        <img src="${item.product.imageURL}" alt="${item.product.productName}" width="50">
                                    </a>
                                </div>
                                <div class="flex-1">
                                    <h3 class="text-lg font-bold">${item.product.productName}</h3>
                                    <p>Stock: ${item.product.stockCount}</p>
                                </div>
                                <p> ${item.priceWithQuantity} VND</p>
                                <!-- Form cập nhật CartItem -->
                                <form action="cart" method="post" class="ml-4">
                                    <input type="hidden" name="action" value="update" />
                                    <input type="hidden" name="itemID" value="${item.itemID}" />
                                    <input type="hidden" name="customerID" value="${item.customerID}" />
                                    <input type="hidden" name="productID" value="${item.productID}" />
                                    <input type="number" name="quantity" value="${item.quantity}" min="1" required class="quantity-input"/>
                                    <input type="hidden" name="priceWithQuantity" value="${item.priceWithQuantity}" />
                                    <button type="submit" class="bg-blue-500 text-white px-3 py-1 rounded">
                                        <i class="fas fa-sync-alt"></i>
                                    </button>
                                </form>
                                <!-- Form xóa CartItem -->
                                <form action="cart" method="post" class="ml-4">
                                    <input type="hidden" name="itemID" value="${item.itemID}" />
                                    <input type="hidden" name="customerID" value="${item.customerID}" />
                                    <button type="submit" name="action" value="delete" class="bg-red-500 text-white px-3 py-1 rounded">
                                        <i class="fas fa-trash-alt"></i>
                                    </button>
                                </form>
                            </div>
                        </c:forEach>
                    </div>
                </c:if>

                <!-- Tổng cộng và nút thanh toán -->
                <div class="flex justify-between items-center mt-4">
                    <button onclick="history.back()" class="bg-gray-200 hover:bg-gray-300 text-gray-700 px-4 py-2 rounded">Back</button>
                    <div class="text-lg font-bold">
                        Total: 
                        <span class="text-red-500">
                            <c:out value="${fn:length(sessionScope.cartItems)}"/> Item(s) - 
                            <c:set var="total" value="0" />
                            <c:forEach var="item" items="${sessionScope.cartItems}">
                                <c:set var="total" value="${total + item.priceWithQuantity * item.quantity}" />
                            </c:forEach>
                            ${total} VND
                        </span>
                    </div>
                    <form action="orderSummary" method="get">
                        <input type="hidden" name="totalAmount" value="${total}" />
                        <button type="submit" class="bg-orange-500 hover:bg-orange-600 text-white px-4 py-2 rounded">
                            <i class="fas fa-credit-card"></i> Checkout
                        </button>
                    </form>
                </div>
            </div>
        </main>
        <jsp:include page="footer.jsp"/>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.min.js"
                integrity="sha384-BBtl+eGJRgqQAUMxJ7pMwbEyER4l1g+O15P+16Ep7Q9Q+zqX6gSbd85u4mG4QzX+"
        crossorigin="anonymous"></script>

        <!--Script for include icons-->
        <script src="https://kit.fontawesome.com/bfab6e6450.js" crossorigin="anonymous"></script>

    </body>
</html>
