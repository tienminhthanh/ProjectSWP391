<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
    <title>WIBOOKS - Unlock Account</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet"/>
</head>
<body class="bg-gray-100">

    <!-- Header -->
    <header class="bg-white shadow w-full">
        <a href="home" class="container mx-auto px-4 py-2 flex justify-between items-center">
            <img alt="WIBOOKS Logo" class="h-10" height="50" src="./img/logoWibooks-removebg-preview.png" width="200"/>
        </a>
    </header>

    <!-- Main Content -->
    <div class="max-w-3xl mx-auto p-6">
        <div class="w-full bg-white p-8 shadow-md rounded-lg">
            <h1 class="text-3xl font-bold text-blue-600 mb-6 text-center">Unlock Account</h1>
            <h2 class="text-xl font-semibold mb-4 text-center text-gray-700">Enter your verification code</h2>

            <hr class="mb-4"/>

            <!-- OTP Input Form -->
            <form action="removeEmailFromLockedAccount" method="post" id="otpForm">
                <div class="flex justify-center space-x-2 mb-4">
                    <input type="text" class="otp-input w-12 h-12 text-xl text-center border border-gray-300 rounded focus:ring-2 focus:ring-blue-600" maxlength="1" pattern="[0-9]" inputmode="numeric" required>
                    <input type="text" class="otp-input w-12 h-12 text-xl text-center border border-gray-300 rounded focus:ring-2 focus:ring-blue-600" maxlength="1" pattern="[0-9]" inputmode="numeric" required>
                    <input type="text" class="otp-input w-12 h-12 text-xl text-center border border-gray-300 rounded focus:ring-2 focus:ring-blue-600" maxlength="1" pattern="[0-9]" inputmode="numeric" required>
                    <input type="text" class="otp-input w-12 h-12 text-xl text-center border border-gray-300 rounded focus:ring-2 focus:ring-blue-600" maxlength="1" pattern="[0-9]" inputmode="numeric" required>
                    <input type="text" class="otp-input w-12 h-12 text-xl text-center border border-gray-300 rounded focus:ring-2 focus:ring-blue-600" maxlength="1" pattern="[0-9]" inputmode="numeric" required>
                    <input type="text" class="otp-input w-12 h-12 text-xl text-center border border-gray-300 rounded focus:ring-2 focus:ring-blue-600" maxlength="1" pattern="[0-9]" inputmode="numeric" required>
                </div>

                <input type="hidden" name="otp" id="otpHidden">

                <!-- Verify Button -->
                <div class="text-center mb-4 flex justify-center gap-4">
                    <button type="submit" class="bg-blue-600 text-white py-2 px-4 rounded-full w-full hover:bg-blue-700 transition duration-200">
                        Verify
                    </button>
                </div>
            </form>

            <!-- Resend OTP -->
            <div class="text-center mb-4">
                <a href="removeEmailFromLockedAccount" class="text-blue-600 hover:underline">
                    <i class="fas fa-redo-alt"></i> Resend OTP Code
                </a>
            </div>

            <div class="text-center mb-4">
                <p class="text-gray-600">Destination email address</p>
                <p class="border border-dotted border-gray-300 p-2 rounded">
                    <%= session.getAttribute("tempEmail") != null ? session.getAttribute("tempEmail") : "Your Email"%>
                </p>
            </div>

            <!-- Info Message -->
            <div id="errorMessage" class="text-center text-red-600 font-semibold mb-4 hidden">
                Invalid OTP. Please try again.
            </div>

            <p class="text-center text-gray-600 mb-4">
                Please check the spam folder in case you do not receive an email. If you cannot find it in the spam folder, you may have mistyped the email address you entered for registration.
            </p>

            <!-- Restart Registration -->
            <div class="text-center">
                <a href="register.jsp" class="bg-white border border-gray-300 text-gray-600 py-2 px-4 rounded-full flex items-center justify-center mx-auto hover:bg-gray-200 transition duration-200">
                    <i class="fas fa-arrow-left mr-2"></i> Restart the registration
                </a>
            </div>
        </div>
    </div>

    <!-- Footer -->
    <footer class="bg-gray-200 py-4 mt-8 w-full">
        <div class="container mx-auto px-4 text-center text-sm text-gray-600">
            <p>© WIBOOKS Co.,Ltd.</p>
        </div>
    </footer>

    <!-- JavaScript -->
    <script>
        document.addEventListener("DOMContentLoaded", function () {
            const otpInputs = document.querySelectorAll(".otp-input");
            const otpHidden = document.getElementById("otpHidden");
            const errorMessage = document.getElementById("errorMessage");

            otpInputs.forEach((input, index) => {
                input.addEventListener("input", (e) => {
                    let value = e.target.value;

                    // Chỉ cho phép nhập một số
                    if (!/^\d$/.test(value)) {
                        e.target.value = "";  // Nếu không phải số, xóa
                    } else if (index < otpInputs.length - 1) {
                        // Chuyển focus sang ô tiếp theo khi nhập xong 1 số
                        otpInputs[index + 1].focus();
                    }

                    // Cập nhật giá trị OTP ẩn
                    updateOtpValue();
                });

                input.addEventListener("keydown", (e) => {
                    if (e.key === "Backspace" && !e.target.value && index > 0) {
                        // Chuyển focus về ô trước khi nhấn Backspace
                        otpInputs[index - 1].focus();
                    }
                });
            });

            function updateOtpValue() {
                otpHidden.value = Array.from(otpInputs).map(input => input.value).join("");
            }

            // Kiểm tra OTP nhập vào và hiển thị thông báo lỗi nếu cần
            const otpForm = document.getElementById("otpForm");
            otpForm.addEventListener("submit", function (e) {
                const enteredOTP = otpHidden.value;
                const generatedOTP = '<%= session.getAttribute("otp") != null ? session.getAttribute("otp") : "" %>';
                if (enteredOTP !== generatedOTP) {
                    e.preventDefault();
                    errorMessage.classList.remove("hidden");
                }
            });
        });
    </script>

</body>
</html>
