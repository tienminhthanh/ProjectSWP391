<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/OrderSumaryView.css"/>
        <script src="https://cdn.tailwindcss.com"></script>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet"/>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous" />
        <link href="css/styleFooter.css" rel="stylesheet">
        <!--Script for include icons-->
        <script src="https://kit.fontawesome.com/bfab6e6450.js" crossorigin="anonymous"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">  
        <!--Header css-->
        <link href="css/styleHeader.css" rel="stylesheet">

        <script src="js/OrderSummaryView.js"></script>


        <title>Payment</title>
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
            .relative {
                position: relative; /* Để dropdown bám theo phần cha */
            }

            #addressList {
                position: absolute;
                top: 100%; /* Đẩy xuống ngay dưới button */
                right: 0; /* Đẩy về bên phải theo button */
                min-width: 200px; /* Độ rộng tối thiểu */
                background-color: white;
                border: 1px solid #ccc;
                border-radius: 4px;
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                display: none; /* Mặc định ẩn */
                z-index: 10;
            }

            #addressList li {
                padding: 10px;
                cursor: pointer;
            }

            #addressList li:hover {
                background-color: #f3f4f6;
            }

        </style>
    </head>
    <body class="bg-gray-100">
        <header>
            <div class="header-container">
                <jsp:include page="header.jsp" flush="true"/> 
                <!--
                                <nav class="breadcrumb-container">
                                    <a href="/">Home</a> >
                                    <a href="/cart">Cart</a> >
                                    <span class="active">Payment</span>
                                </nav>-->
            </div>
        </header>
        <main class="container mx-auto px-4 py-6">
            <div class="bg-white shadow rounded-lg p-4">
                <h2 class="text-2xl font-bold mb-4">Payment</h2>
                <!-- Hiển thị thông báo lỗi nếu có -->
                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger" role="alert">
                        <i class="fas fa-exclamation-circle"></i> ${errorMessage}
                    </div>
                </c:if>

                <div class="content">
                    <div class="left-content" style="width: 60%">
                        <h2>Shipping Information</h2> <hr>
                        <form id="orderForm" action="OrderController" method="POST" 
                              onsubmit="updateOrderTotal(); updatePaymentAction();">
                            <input type="hidden" name="orderTotal" id="hiddenOrderTotal">
                            <input type="hidden" id="selectedAddress" name="selectedAddress" value="">
                            <input type="hidden" id="userName" name="userName" value="">
                            <div class="input-custom">
                                <label for="name">Full Name</label><br>
                                <input type="text" name="name" id="name" 
                                       value="${sessionScope.account.lastName} ${sessionScope.account.firstName}" 
                                       readonly required/>
                            </div>

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

                            <div class="input-custom">
                                <label for="phone">Phone</label><br>
                                <input type="tel" name="phone" id="phone" value="${phone}" readonly required />
                            </div>

                            <div class="input-custom">
                                <label for="email">Email</label><br>
                                <input type="text" name="email" id="email" value="${email}" readonly/>
                            </div>

                            <div class="payment-custom">
                                <h2>Payment Method</h2>
                                <label><input type="radio" name="paymentMethod" value="COD" checked/> Cash on Delivery (COD)</label><br>  
                                <label><input type="radio" name="paymentMethod" value="online" /> Pay via VNPay </label><br>
                            </div> <hr>

                            <!-- Voucher Selection -->
                            <label class="form-label fw-bold">Select voucher:</label>
                            <select class="form-select border-2 border-primary" id="voucherSelect" name="voucherID">
                                <option value="" data-discount="0">-- Select voucher --</option>  
                                <c:forEach var="voucher" items="${listVoucher}">
                                    <option value="${voucher.voucherID}" 
                                            data-discount="${computedValues[voucher.voucherID]}" 
                                            ${voucher.voucherID eq bestVoucherID ? 'selected' : ''}>  
                                        ${voucher.voucherName}  
                                        <c:if test="${voucher.voucherType eq 'PERCENTAGE'}">
                                        <p>Up to <span><fmt:formatNumber value="${voucher.maxDiscountAmount}" type="number" groupingUsed="true"/> đ</span></p>
                                    </c:if>
                                    </option>
                                </c:forEach>
                            </select>

                            <div class="payment-method">
                                <h2>Delivery Method</h2>
                                <c:forEach var="option" items="${deliveryOptions}">
                                    <label>
                                        <input type="radio" name="shippingOption" 
                                               value="${option.deliveryOptionID}" 
                                               data-cost="${option.optionCost}"
                                               onclick="updateShippingCost(${option.optionCost})"
                                               ${option.deliveryOptionID == 1 ? 'checked' : ''} />
                                        ${option.optionName} (<fmt:formatNumber value="${option.optionCost}" pattern="#,##0"/> đ)
                                    </label><br>
                                </c:forEach>
                            </div> <hr>
                            <div class="d-flex justify-content-between mt-4">
                                <a href="home" class="btn btn-secondary">Back to Home</a>
                                <input type="hidden" name="orderTotal" id="hiddenOrderTotal">
                                <button type="submit" class="btn btn-primary" id="placeOrderBtn" onclick="disableButton()">Place Order</button>

                            </div>
                        </form>
                    </div>

                    <div class="right-content" style="width: 40%">
                        <h2>Order Summary</h2>
                        <div class="product-list">
                            <c:forEach var="item" items="${cartItems}">
                                <div class="product-item">
                                    <span class="product-infor">
                                        <img src="${item.product.imageURL}" alt="${item.product.productName}" width="80px" height="80px"/>
                                    </span>
                                    <span class="product-infor">${item.product.productName}</span>
                                    <span class="product-price"> <fmt:formatNumber value="${item.priceWithQuantity}" pattern="#,##0 đ"/> </span>
                                </div>
                                <span class="product-quantity">Quantity: ${item.quantity}</span>
                                <hr>
                            </c:forEach>
                        </div>
                        <div class="order-summary">
                            <div class="price">
                                <div class="price-custom">
                                    <div class="price-item">
                                        <p>Subtotal: 
                                            <span id="subtotal">
                                                <fmt:formatNumber value="${priceWithQuantity}" type="number" pattern="#,##0 đ"/>
                                            </span>
                                        </p>
                                    </div> 
                                    <div class="price-item">
                                        <p>Shipping Fee: 
                                            <span id="shippingFee">
                                                <c:forEach var="option" items="${deliveryOptions}">
                                                    <c:if test="${option.deliveryOptionID == 1}">
                                                        <fmt:formatNumber value="${option.optionCost}" pattern="#,##0 đ"/>
                                                    </c:if>
                                                </c:forEach>
                                            </span> 

                                        </p>
                                    </div>

                                    <div class="price-item">
                                        <p>Discount: <span id="discount"><fmt:formatNumber value="${voucherValue}" pattern="#,##0 đ"/></span></p>
                                    </div>
                                    <div class="price-item">
                                        <p>Total: <span id="totalAmount"><fmt:formatNumber value="${orderTotalAmount}" pattern="#,##0 đ"/></span></p>
                                    </div>
                                </div>
                            </div>
                            <hr>
                        </div>

                    </div>
                </div>
        </main>
        <jsp:include page="footer.jsp"/>
        <script>
            function updateOrderTotal() {
                let totalAmountText = document.getElementById("totalAmount").innerText;
                let totalAmount = totalAmountText.replace(/[^\d]/g, ""); // Lọc chỉ lấy số
                document.getElementById("hiddenOrderTotal").value = totalAmount; // Cập nhật vào input hidden
                return true; // Đảm bảo form tiếp tục submit
            }

            document.addEventListener("DOMContentLoaded", function () {
                updateOrderTotal(); // Cập nhật ngay khi tải trang
            });

            document.getElementById("voucherSelect").addEventListener("change", updateOrderTotal);
            document.querySelectorAll('input[name="shippingOption"]').forEach(radio => {
                radio.addEventListener("change", updateOrderTotal);
            });
        </script>
        <script>
            function updatePaymentAction() {
                var form = document.getElementById("orderForm");
                var paymentMethod = document.querySelector('input[name="paymentMethod"]:checked').value;

                if (paymentMethod === "online") {
                    form.action = "ajaxServlet"; // Chuyển hướng sang VNPayController
                } else {
                    form.action = "OrderController"; // Giữ nguyên cho COD
                }
            }

        </script>
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
        </script>

    </body>
</html>
