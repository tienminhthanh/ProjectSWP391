
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
            @media (max-width: 768px) {
                .logo img {
                    max-width: 140px;
                }
            }
        </style>
    </head>
    <body class="bg-gray-100">
        <header class="bg-white shadow">
            <div class="container mx-auto px-4 py-2 flex justify-between items-center">
                <div class="logo">
                    <img src="img/logo.png" alt="WIBOOKS" />
                </div>
                <a href="logout" class="bg-red-500 text-white p-2 rounded hover:bg-red-600">
                    <i class="fas fa-sign-out-alt mr-2"></i> Sign-out
                </a>
            </div>
        </header>
        <main class="container mx-auto px-4 py-6">
            <div class="bg-white shadow rounded-lg p-4">
                <h2 class="text-2xl font-bold mb-4">Cart</h2>
                <div class="flex items-center mb-4">
                    <div class="flex-1 text-center border-b-2 border-blue-500 pb-2">1 Cart</div>
                    <div class="flex-1 text-center border-b-2 border-gray-300 pb-2">2 Settlement</div>
                    <div class="flex-1 text-center border-b-2 border-gray-300 pb-2">3 Complete</div>
                </div>

                <!-- Kiểm tra nếu giỏ hàng rỗng -->
                <c:if test="${empty cartItems}">
                    <div class="p-4 text-center text-gray-500">
                        *Your cart is empty.
                    </div>
                    <div class="text-center text-sm text-gray-600">
                        ※Please make sure to check <span class="font-bold">the recommended environment</span> before purchasing.<br>
                        eBook(s) you are about to purchase can be viewed only on the WIBOOK app (for iOS and Android) or the Browser Viewer. Please check if you can view the eBook with a free title before purchasing.
                    </div>
                </c:if>

                <!-- Nếu có CartItem -->
                <c:if test="${not empty cartItems}">
                    <div class="bg-blue-900 text-white p-2 rounded-t-lg">Items in Cart</div>
                    <div class="border border-gray-300 rounded-b-lg">
                        <c:forEach var="item" items="${cartItems}">

                            <div class="flex items-center p-4 border-b border-gray-300">
                                <!-- Hiển thị hình ảnh tạm (hoặc hiển thị ID sản phẩm) -->
                                <div class="w-16 h-16 bg-gray-200 flex items-center justify-center mr-4">
                                    <a href="productDetails?productID=${item.productID}" class="product-image">
                                        <img src="${item.product.imageList[0].imageURL}" alt="${item.product.getProductName()}">
                                        <div class="hover-name">${item.product.productName}</div>
                                    </a>
                                </div>
                                <div class="flex-1">
                                    <h3 class="text-lg font-bold">Name: ${item.product.getProductName()}</h3>
                                    <p>Stock: ${item.product.stockCount}</p>
                                </div>
                                    <p>Price: ${item.product.price} VND</p>
                                <!-- Form cập nhật CartItem -->
                                <form action="cart" method="post" class="ml-4">
                                    <input type="hidden" name="action" value="update" />
                                    <input type="hidden" name="itemID" value="${item.itemID}" />
                                    <input type="hidden" name="customerID" value="${item.customerID}" />
                                    <input type="hidden" name="productID" value="${item.productID}" />
                                    <input type="number" name="quantity" value="${item.quantity}" min="1" required/>
                                    <input type="hidden" name="priceWithQuantity" value="${item.priceWithQuantity}" />
                                    <button type="submit" class="bg-blue-500 text-white px-3 py-1 rounded">
                                        <i class="fas fa-sync-alt"></i>
                                    </button>
                                </form>
                                <!-- Form xóa CartItem -->
                                <form action="cart" method="post" class="ml-4">
                                    <input type="hidden" name="action" value="delete" />
                                    <input type="hidden" name="itemID" value="${item.itemID}" />
                                    <input type="hidden" name="customerID" value="${item.customerID}" />
                                    <button type="submit" class="bg-red-500 text-white px-3 py-1 rounded">
                                        <i class="fas fa-trash-alt"></i>
                                    </button>
                                </form>
                            </div>
                        </c:forEach>
                    </div>
                </c:if>
                <div class="flex justify-between items-center mt-4">
                    <button onclick="history.back()" class="bg-gray-200 hover:bg-gray-300 text-gray-700 px-4 py-2 rounded">Back</button>
                    <div class="text-lg font-bold">
                        Total: 
                        <span class="text-red-500">
                            <c:out value="${fn:length(cartItems)}"/> Item(s) - 
                            <c:set var="total" value="0" />
                            <c:forEach var="item" items="${cartItems}">
                                <c:set var="total" value="${total + item.priceWithQuantity*item.quantity}" />
                            </c:forEach>
                            ${total} VND
                        </span>
                    </div>

                    <form action="cart" method="post">
                        <c:forEach var="item" items="${cartItems}">
                            <input type="hidden" name="action" value="pay" />
                        </c:forEach>
                        <button type="submit" class="bg-orange-500 hover:bg-orange-600 text-white px-4 py-2 rounded">
                            <i class="fas fa-credit-card"></i> Checkout
                        </button>
                    </form>

                </div>
            </div>
        </main>
        <c:import url="footer.jsp"/>
    </body>
</html>
