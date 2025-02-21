<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Payment Confirmation</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <div class="container">
        <h2>Payment Confirmation</h2>
        <div class="steps">
            <span class="step completed">1 Cart</span>
            <span class="step active">2 Payment Confirmation</span>
            <span class="step">3 Complete</span>
        </div>

        <div class="payment-wrapper">
            <!-- Total Price Section -->
            <div class="box total-price">
                <h3>Total Price</h3>
                <p>Subtotal (<a href="#">*Details</a>) <span class="price">USD 9.99</span></p>
                <label>
                    <input type="checkbox"> Use Coupon <span class="discount">USD -0.00</span>
                </label>
                <p>Subtotal After Coupon Discount <span class="price">USD 9.99</span></p>
                <p>Tax <span class="price">USD 0.00</span></p>
                <div class="granted-points">
                    <span>⬇ Granted Point</span>
                    <span class="points">+5.09 Point(s)</span>
                </div>
            </div>

            <!-- Total Payment Section -->
            <div class="box total-payment">
                <h3>Total Payment <span class="price">USD 9.99</span></h3>
                <p>Please select payment method.</p>
                <label class="payment-option">
                    <input type="radio" name="payment" checked> Credit Card
                </label>
                <label class="payment-option">
                    <input type="radio" name="payment"> PayPal
                </label>

                <div class="credit-card-info">
                    <label>
                        <input type="checkbox" checked> Save Number
                    </label>
                    <p class="note">
                        ※ By selecting "Save Number", you may omit typing in credit card number in next transaction.
                        "Pre-order" and "Buying as Gift" become even more convenient to use.
                    </p>
                    <p class="note">
                        ※ You may not select this payment method when the total amount is less than 0.5 USD.
                    </p>
                </div>

                <button class="pay-button">Pay with Selected Method</button>
                <p class="note">※ Selecting the button will lead you to an external site.</p>
            </div>
        </div>
    </div>
</body>
</html>
