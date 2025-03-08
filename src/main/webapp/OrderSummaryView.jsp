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
                <h2 class="text-2xl font-bold mb-4">Payment</h2>

                <!-- Trạng thái: 1 Cart, 2 Settlement, 3 Complete -->
                <div class="status-container">
                    <div class="status-item ">1 Cart</div>
                    <div class="status-item active">2 Settlement</div>
                    <div class="status-item">3 Complete</div>
                </div>
                <div class="title-custom">
                    <p>Please check your customer and cart information before placing an order.</p>
                </div>
                <div class="content">
                    <div class="left-content" style="width: 60%">
                        <h2>Shipping Information</h2> <hr>
                        <form action="OrderController" method="POST">
                            <div class="input-custom">
                                <label for="name">Full Name</label><br>
                                <input type="text" name="name" id="name" value="${fullName}" required/>
                            </div>
                            <div class="input-custom">
                                <label for="addr">Address</label><br>
                                <input type="text" name="addr" id="addr" value="${deliveryAddress}" required/>
                            </div>
                            <div class="input-custom">
                                <label for="phone">Phone</label><br>
                                <input type="tel" name="phone" id="phone" value="${phone}" required />
                            </div>
                            <div class="input-custom">
                                <label for="email">Email</label><br>
                                <input type="text" name="email" id="email" value="${email}" required/>
                            </div>

                            <div class="payment-custom">
                                <h2>Payment Method</h2>
                                <label><input type="radio" name="paymentMethod" value="COD" checked/> Cash on Delivery (COD)</label><br>  
                                <!--                        <label><input type="radio" name="paymentMethod" value="online" checked/> Cash on Delivery (COD)</label><br>  -->

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


                            <!-- Hidden fields to send shipping fee, discount, etc. -->
                            <input type="hidden" name="shippingFee" value="${optionCost}">
                            <input type="hidden" name="discount" value="${voucherValue}">
                            <input type="hidden" name="priceWithQuantity" value="${priceWithQuantity}">
                            <input type="hidden" name="orderTotalAmount" value="${orderTotalAmount}">

                            <div class="d-flex justify-content-between mt-4">
                                <button class="btn btn-secondary" onclick="history.back();">Back to Cart</button>
                                <button type="submit" class="btn btn-primary">Place Order</button>
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
                                    <span class="product-price"> <fmt:formatNumber value="${item.product.price}" pattern="#,##0 đ"/> </span>

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
        <jsp:include page="footer.jsp"/>s
    </body>
</html>
