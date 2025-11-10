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
        <script src="js/OrderDetailView.js"></script>
        <link href="css/styleHeader.css" rel="stylesheet">
        <!--Script for include icons-->
        <script src="https://kit.fontawesome.com/bfab6e6450.js" crossorigin="anonymous"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">  
        <!--Header css-->
        <link href="css/styleHeader.css" rel="stylesheet">

        <!--Footer css-->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous" />
        <link href="css/styleFooter.css" rel="stylesheet">
        <!--Banner carousel-->
        <script src="https://cdn.jsdelivr.net/npm/alpinejs@3.x.x/dist/cdn.min.js" defer></script>
        <style>
            /* General Styles */
            body {
                font-family: 'Arial', sans-serif;
                background-color: #f7fafc; /* Light gray background */
                margin: 0; /* Remove default margin */
                padding: 0; /* Remove default padding */
            }

            .container {
                max-width: 1200px; /* Max width for the container */
                margin: auto; /* Center the container */
                padding: 1rem; /* Add padding for better spacing */
            }

            /* Header Styles */
            header {
                background-color: #ffffff; /* White header */
                box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* Subtle shadow */
            }

            .logo img {
                max-width: 150px; /* Logo size */
            }

            .top-bar {
                display: flex;
                justify-content: space-between;
                align-items: center;
                padding: 1rem 2rem; /* Padding for top bar */
            }

            /* Typography */
            h1 {
                font-size: 2rem;
                font-weight: bold;
                color: #EA580C; /* Tailwind orange */
            }

            h2, h3 {
                color: #2d3748; /* Dark gray for headings */
            }

            p {
                color: #4a5568; /* Medium gray for text */
            }

            /* Card Styles */
            .bg-white {
                background-color: #ffffff; /* White background for cards */
                border-radius: 0.5rem; /* Rounded corners */
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); /* Card shadow */
                padding: 1rem; /* Padding inside cards */
            }

            /* Button Styles */
            button {
                transition: background-color 0.3s ease; /* Smooth transition for buttons */
                border: none; /* Remove default border */
                cursor: pointer; /* Change cursor to pointer */
            }

            button:hover {
                opacity: 0.9; /* Slightly fade on hover */
            }

            /* Flexbox Utilities */
            .flex {
                display: flex;
            }

            .flex-wrap {
                flex-wrap: wrap; /* Allow buttons to wrap if necessary */
            }

            .flex-col {
                flex-direction: column; /* Stack buttons vertically */
            }

            .space-x-2 > *:not(:last-child) {
                margin-right: 0.5rem; /* Space between buttons */
            }

            /* Product Item Styles */
            .product-item {
                transition: transform 0.2s; /* Smooth scaling */
                display: flex;
                flex-direction: column; /* Stack content vertically */
                align-items: center; /* Center items */
                padding: 1rem; /* Padding inside product item */
            }

            .product-item:hover {
                transform: scale(1.05); /* Scale up on hover */
            }

            /* Popup Styles */
            .rating-popup, .review-popup {
                background-color: #ffffff; /* White background for popups */
                border-radius: 0.5rem; /* Rounded corners */
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2); /* Popup shadow */
                padding: 1rem; /* Padding inside popups */
            }

            .rating-popup h2, .review-popup h2 {
                color: #2d3748; /* Dark gray for popup headings */
            }

            /* Textarea Styles */
            textarea {
                border: 1px solid #cbd5e0; /* Light gray border */
                border-radius: 0.25rem; /* Rounded corners */
                padding: 0.5rem; /* Padding inside textarea */
                width: 100%; /* Full width */
            }

            textarea:focus {
                border-color: #EA580C; /* Orange border on focus */
                outline: none; /* Remove default outline */
            }

            /* Utility Classes */
            .mt-2 {
                margin-top: 0.5rem; /* Space between elements */
            }

            .hidden {
                display: none; /* Hide elements */
            }

            .breadcrumb-container {
                background-color: #f8f9fa;  /* Màu nền */
                padding: 10px 20px;
                font-size: 12px;

                color: #e3a100;  /* Màu chữ vàng giống ảnh */
            }

            .breadcrumb-container a {
                text-decoration: none;
                color: #e3a100; /* Màu chữ vàng */
                transition: color 0.3s ease-in-out;
            }

            .breadcrumb-container a:hover {
                color: #d38d00; /* Đổi màu khi hover */
            }

            .breadcrumb-container .active {
                color: #e3a100; /* Giữ màu vàng cho trang hiện tại */
            }
        </style>

    </head>
    <body class="bg-gray-100">

        <header>
            <div class="header-container">
                <jsp:include page="header.jsp" flush="true"/> 
            </div>

            <nav class="breadcrumb-container">
                <a href="/">Home</a> >
                <a href="/readAccount">Account</a> >
                <a href="/OrderListController">Order List</a> >
                <span href="/OrderDetailController?id=${orderInfo.orderID}" class="active">Order Detail (Order ID: ${orderInfo.orderID})  </span>
            </nav>

        </header>


        <div class="container mx-auto px-4 py-6">

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

                        <c:set var="count" value="1"/>                              
                        <c:forEach var="item" items="${orderInfo.orderProductList}">
                            <p><strong>${count} </strong> ${item.product.productName}</p>
                            <p><strong>Quantity:</strong> ${item.quantity}</p>
                            <p><strong>Price:</strong> 
                                <fmt:formatNumber value="${item.priceWithQuantity}" type="number" groupingUsed="true"/> đ
                            </p>

                            <hr class="my-2">
                            <c:set var="count" value="${count + 1}"/>
                        </c:forEach>
                        <p><strong>Shipping Fee: </strong>  
                            <fmt:formatNumber value="${delivery.optionCost}" type="currency" currencySymbol="" groupingUsed="true" maxFractionDigits="0"/> đ
                        </p>

                        <c:if test="${voucher > 0}">
                            <p><strong>Discount: </strong>  
                                <fmt:formatNumber value="${voucher}" type="currency" currencySymbol="" groupingUsed="true" maxFractionDigits="0"/> đ
                            </p>
                        </c:if>

                        <p class="mt-4 text-lg font-bold">
                            Total Order: 
                            <fmt:formatNumber value="${orderInfo.preVoucherAmount}" type="number" groupingUsed="true"/> đ
                        </p>

                        <div class="flex space-x-2 mt-4">
                            <c:if test="${orderInfo.orderStatus eq 'pending' or orderInfo.orderStatus eq 'paid'}">                                <button type="button" onclick="showUpdateForm()" 
                                    class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">
                                    Update
                                </button>
                            </c:if>
                            <c:if test="${orderInfo.orderStatus eq 'pending' or orderInfo.orderStatus eq 'paid'}">
                                <form action="DeleteOrderController" method="POST" 
                                      onsubmit="return confirm('Are you sure you want to delete item with ID = ${orderInfo.orderID}?')">
                                    <input type="hidden" name="id" value="${orderInfo.orderID}">
                                    <button type="submit" class="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600">
                                        Cancel
                                    </button>
                                </form>
                            </c:if>


                            <!-- Nút xác nhận đã nhận hàng (ẩn mặc định, chỉ hiển thị khi trạng thái là 'Đang giao hàng') -->
                            <c:if test="${orderInfo.deliveryStatus eq 'delivered' and orderInfo.orderStatus eq 'delivered'}">                              
                                <form action="OrderDetailController" method="POST">
                                    <input type="hidden" name="orderID" value="${orderInfo.orderID}">
                                    <input type="hidden" name="action" value="confirm"> 
                                    <button type="submit" class="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600 mt-4">
                                        Confirm Receipt
                                    </button>
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
                                    <c:if test="${ orderInfo.orderStatus eq 'completed'}">                               
                                        <input type="hidden" name="orderID" value="${orderInfo.orderID}">
                                        <input type="hidden" name="productID" value="${item.product.productID}">
                                        <c:if test="${ item.rating eq 0 }">

                                            <button type="submit" id="rateButton_${item.product.productID}" 
                                                    onclick="openRatingPopup(`${item.product.productID}`, `${orderInfo.orderID}`, `${item.product.productName}`, `${item.product.imageURL}`)"
                                                    class="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600 mt-4">
                                                Rate
                                            </button>
                                        </c:if>
                                    </c:if>
                                    <c:if test="${ orderInfo.orderStatus eq 'completed' and (item.comment eq null or item.comment eq '') }">      
                                        <button type="button" 
                                                id="reviewButton_${item.product.productID}"
                                                onclick="openReviewPopup(`${item.product.productID}`, `${orderInfo.orderID}`, `${item.product.productName}`, `${item.product.imageURL}`)"
                                                class="bg-yellow-500 text-white px-4 py-2 rounded hover:bg-yellow-600 mt-2">
                                            Review
                                        </button>
                                    </c:if>
                                </div>
                            </c:forEach>
                            <div id="ratingPopup" class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 hidden" onclick="closeRatingPopup(event)">
                                <div class="bg-white p-6 rounded-lg shadow-lg w-96" onclick="event.stopPropagation()">
                                    <h2 class="text-xl font-bold mb-4">Rate Your Order</h2>
                                    <div class="flex items-center space-x-4 border-b pb-3">
                                        <img id="popupProductImage" src="" alt="" class="w-24 h-24 object-cover rounded">
                                        <div class="flex-1">
                                            <p id="popupProductName" class="text-lg font-semibold"></p>
                                            <div class="flex space-x-1 text-2xl" id="starContainer">
                                                <span class="text-gray-400 text-2xl cursor-pointer" data-star="1">★</span>
                                                <span class="text-gray-400 text-2xl cursor-pointer" data-star="2">★</span>
                                                <span class="text-gray-400 text-2xl cursor-pointer" data-star="3">★</span>
                                                <span class="text-gray-400 text-2xl cursor-pointer" data-star="4">★</span>
                                                <span class="text-gray-400 text-2xl cursor-pointer" data-star="5">★</span>
                                            </div>
                                        </div>
                                    </div>
                                    <p id="error-message" class="text-red-500 text-sm mt-2 hidden">Please select at least one star.</p>
                                    <form action="OrderDetailController" method="POST" onsubmit="return validateRating()">
                                        <input type="hidden" name="orderID" id="popupOrderID">
                                        <input type="hidden" name="productID" id="popupProductID">
                                        <input type="hidden" name="orderID" value="${orderInfo.orderID}">
                                        <input type="hidden" name="productID" value="${item.product.productID}">
                                        <input type="hidden" name="action" value="rate"> 
                                        <input type="hidden" name="rating" id="rating" value="0">
                                        <button type="submit" class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">Submit</button>
                                        <button type="button" onclick="closeRatingPopup()" class="bg-gray-400 text-white px-4 py-2 rounded ml-2 hover:bg-gray-500">Cancel</button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- Popup Review -->
        <div id="reviewPopup" class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 hidden" onclick="closeReviewPopup(event)">
            <div class="bg-white p-6 rounded-lg shadow-lg w-2/3 flex" onclick="event.stopPropagation()">
                <div class="w-1/3 flex items-center justify-center">
                    <img id="reviewProductImage" src="" alt="" class="w-48 h-48 object-cover rounded">
                </div>
                <div class="w-2/3 p-4">
                    <h2 class="text-xl font-bold mb-4">Write a Review</h2>
                    <p id="reviewProductName" class="text-lg font-semibold mb-2"></p>
                    <form id="reviewForm" action="OrderDetailController" method="POST">
                        <input type="hidden" name="orderID" id="reviewOrderID">
                        <input type="hidden" name="productID" id="reviewProductID">
                        <input type="hidden" name="action" value="review"> 
                        <textarea name="reviewContent" id="reviewContent" rows="4" class="w-full border rounded p-2 mb-4" placeholder="Write your review here..."></textarea>
                        <button type="button" onclick="submitReview()" class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">Submit</button>
                        <button type="button" onclick="closeReviewPopup()" class="bg-gray-400 text-white px-4 py-2 rounded ml-2 hover:bg-gray-500">Cancel</button>
                    </form>
                </div>
            </div>
        </div>

        <!-- Form cập nhật địa chỉ giao hàng -->
        <div id="updateForm" class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 hidden">
            <div class="bg-white p-6 rounded-lg shadow-lg w-96">
                <h2 class="text-xl font-semibold mb-4">Update Shipping Address</h2>
                <form action="UpdateOrderController" method="POST">
                    <input type="hidden" id="selectedAddress" name="selectedAddress" value="">

                    <input type="hidden" name="orderID" value="${orderInfo.orderID}">
                    <label for="name">Address</label><br>
                    <div class="relative">
                        <div class="flex">
                            <input class="w-full p-3 border border-gray-300 rounded-l" 
                                   id="defaultDeliveryAddress" name="defaultDeliveryAddress"
                                   placeholder="Default Delivery Address" type="text" 
                                   value="${sessionScope.account.defaultDeliveryAddress}" readonly />

                            <button type="button" id="toggleDropdown" class="px-4 border border-gray-300 bg-gray-200 rounded-r">
                                ▼
                            </button>
                        </div>
                        <ul id="addressList">
                            <c:forEach var="address" items="${addressList}">
                                <li class="flex justify-between px-4 py-2 hover:bg-gray-200 cursor-pointer address-item">
                                    <span data-value="${address.addressDetails}" class="flex-1"  data-id="${address.addressID}">${address.addressDetails}</span>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                    <div class="flex justify-end space-x-2">
                        <button type="button" onclick="hideUpdateForm()" class="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600">Cancel</button>
                        <button type="submit" class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">Save</button>
                    </div>
                </form>
            </div>
        </div>
        <div id="thankYouPopup" class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 hidden">
            <div class="bg-white p-6 rounded-lg shadow-lg text-center">
                <h2 class="text-xl font-bold mb-4">Thank You!</h2>
                <p>Your rating has been submitted successfully.</p>
                <button onclick="closeThankYouPopup()" class="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600 mt-4">OK</button>
            </div>
        </div>
        <jsp:include page="footer.jsp" flush="true"/> 
        <script>
            document.addEventListener("DOMContentLoaded", function () {
                const addressList = document.getElementById("addressList");
                const toggleDropdown = document.getElementById("toggleDropdown");
                const defaultAddressInput = document.getElementById("defaultDeliveryAddress");
                const selectedAddress = document.getElementById("selectedAddress");
                toggleDropdown.addEventListener("click", function () {
                    addressList.classList.toggle("hidden");
                });
                document.getElementById("toggleDropdown").addEventListener("click", function () {
                    const dropdown = document.getElementById("addressList");
                    dropdown.style.display = dropdown.style.display === "none" ? "block" : "none";
                });
                // Đóng dropdown nếu click bên ngoài
                document.addEventListener("click", function (event) {
                    const dropdown = document.getElementById("addressList");
                    const toggleBtn = document.getElementById("toggleDropdown");
                    if (!toggleBtn.contains(event.target) && !dropdown.contains(event.target)) {
                        dropdown.style.display = "none";
                    }
                });
                document.querySelectorAll(".address-item span").forEach(item => {
                    item.addEventListener("click", function () {
                        defaultAddressInput.value = this.getAttribute("data-value");
                        addressList.classList.add("hidden");
                        selectedAddress.value = this.getAttribute("data-id");
                        document.getElementById("addressList").style.display = "none";
                    });
                });
            });
            function validateRating() {
                let rating = document.getElementById("rating").value;
                if (rating == "0") {
                    document.getElementById("error-message").classList.remove("hidden"); // Hiện thông báo lỗi nếu chưa chọn sao
                    return false; // Ngăn không cho gửi form
                }
                return true;
            }
            ;


        </script>
    </body>
</html>
