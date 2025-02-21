<%-- 
    Document   : OrderSummaryView
    Created on : Feb 21, 2025, 2:28:45 PM
    Author     : Macbook
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/OrderSumaryView.css"/>
        <title>Payment</title>
    </head>
    <body>
        <div class="title-custom">
            <h1>Payment</h1>
        </div>
        <div class="title-custom">
            <p>Please check your customer and cart information before placing an order.</p>
        </div>
        <div class="content">
            <div class="left-content" style="width: 60%">
                <h2>Shipping Information</h2> <hr>
                <form action="payment" method="POST">
                    <div class="input-custom">
                        <label for="name">Full Name</label><br>
                        <input type="text" name="name" id="name" required/>
                    </div>
                    <div class="input-custom">
                        <label for="addr">Address</label><br>
                        <input type="text" name="addr" id="addr" value="${deliveryAddress}" required/>
                    </div>
                    <div class="input-custom">
                        <label for="phone">Phone</label><br>
                        <input type="tel" name="phone" id="phone" required />
                    </div>
                    <div class="input-custom">
                        <label for="email">Email</label><br>
                        <input type="text" name="email" id="email" required/>
                    </div>
                    <div class="payment-custom">
                        <h2>Payment Method</h2>
                        <label><input type="radio" name="paymentMethod" value="PAY001" ${methodname == 'Bank Transfer' ? 'checked' : ''}/> Bank Transfer</label><br>
                        <label><input type="radio" name="paymentMethod" value="PAY002" ${methodname == 'COD' ? 'checked' : ''}/> Cash on Delivery (COD)</label><br>  
                    </div> <hr>
                    <div class="payment-method">
                        <h2>Delivery Method</h2>
                        <label><input type="radio" name="deliveryOption" value="DEL001" /> Express Delivery</label><br>
                        <label><input type="radio" name="deliveryOption" value="DEL002" /> Economy Delivery</label><br>  
                    </div> <hr>
                    <button class="back" onclick="history.back()">Back to Cart</button>
                    <button type="submit">Place Order</button>
                </form>
            </div>

            <div class="right-content" style="width: 40%">
                <h2>Order Summary</h2>
                <div class="product-list">
                    <c:forEach var="item" items="${cartItems}">
                        <div class="product-item">
                            <span class="product-infor"><img src="${item.image}" alt="${item.productName}" width="80px" height="80px"/></span>
                            <span class="product-infor">${item.productName}</span>
                            <span class="product-price">${item.priceWithQuantity} VND</span>
                        </div>
                        <hr>
                    </c:forEach>
                </div>
                <div class="order-summary">
                    <div class="price">
                        <div class="price-custom">
                            <p>Subtotal:</p>
                            <p>${priceWithQuantity} <span class="currency">VND</span></p>
                        </div>
                        <div class="price-custom">
                            <p>Shipping Fee:</p>
                            <p>${optionCost} <span class="currency">VND</span></p>
                        </div>
                        <div class="price-custom">
                            <p>Discount:</p>
                            <p>${voucherValue} <span class="currency">VND</span></p>
                        </div>
                    </div>
                    <hr>
                </div>
                <div class="total-price">
                    <strong>Total: ${orderTotalAmount} <span class="currency">VND</span></strong>
                </div>

            </div>
        </div>
    </body>
</html>
