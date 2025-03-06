<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
        <title>Update Voucher</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"/>
    </head>
    <body class="bg-gray-100 min-h-screen flex flex-col">
        <header class="bg-white shadow w-full">
            <a href="home" class="container mx-auto px-4 py-2 flex justify-between items-center">
                <img alt="WIBOOKS Logo" class="h-10" height="50" src="./img/logoWibooks-removebg-preview.png" width="200"/>
                <div class="flex items-center space-x-4">
                    <i class="fas fa-globe text-xl"></i>
                </div>
            </a>
        </header>
        <main class="flex-grow flex items-center justify-center">
            <div class="w-full max-w-4xl bg-white p-8 shadow-md">
                <h1 class="text-2xl font-semibold mb-4">Update Voucher</h1>
                <hr class="mb-6"/>
                <form action="voucherUpdate" method="post" class="grid grid-cols-1 md:grid-cols-3 gap-4 p-6 bg-white shadow-lg rounded-lg">
                    <!-- Voucher ID (Readonly) -->
                    <div class="mb-4">
                        <label class="block text-lg font-semibold text-gray-700">Voucher ID</label>
                        <input type="text" name="voucherID" value="${VOUCHER_DETAILS.voucherID}" 
                               class="w-full p-3 border border-gray-300 rounded bg-gray-100" readonly>
                    </div>

                    <!-- Voucher Name -->
                    <div class="mb-4">
                        <label class="block text-lg font-semibold text-gray-700">Voucher Name</label>
                        <input type="text" name="voucherName" value="${VOUCHER_DETAILS.voucherName}" 
                               class="w-full p-3 border border-gray-300 rounded" required>
                    </div>

                    <!-- Voucher Type (Dropdown) -->
                    <div class="mb-4">
                        <label class="block text-lg font-semibold text-gray-700">Voucher Type</label>
                        <select name="voucherType" id="voucherType" class="w-full p-3 border border-gray-300 rounded" required>
                            <option value="PERCENTAGE" ${VOUCHER_DETAILS.voucherType == 'PERCENTAGE' ? 'selected' : ''}>Percentage</option>
                            <option value="FIXED_AMOUNT" ${VOUCHER_DETAILS.voucherType == 'FIXED_AMOUNT' ? 'selected' : ''}>Fixed Amount</option>
                        </select>
                    </div>

                    <!-- Voucher Value -->
                    <div class="mb-4">
                        <label class="block text-lg font-semibold text-gray-700">Voucher Value</label>
                        <div class="relative">
                            <input type="number" step="0.01" name="voucherValue" value="${VOUCHER_DETAILS.voucherValue}" 
                                   class="w-full p-3 border border-gray-300 rounded pr-7" required>
                            <span id="unit" class="absolute inset-y-0 right-3 flex items-center text-gray-500"></span>
                        </div>
                    </div>

                    <!-- Max Discount Amount (Only for Percentage) -->
                    <div class="mb-4" id="maxDiscountDiv" style="display: none;">
                        <label class="block text-lg font-semibold text-gray-700">Max Discount Amount</label>
                        <div class="relative">
                            <input type="number" step="0.01" name="maxDiscountAmount" id="maxDiscountAmount"
                                   value="${VOUCHER_DETAILS.maxDiscountAmount}" 
                                   class="w-full p-3 border border-gray-300 rounded pr-12">
                            <span class="absolute inset-y-0 right-3 flex items-center text-gray-500">đ</span>
                        </div>
                    </div>

                    <!-- Minimum Purchase Amount -->
                    <div class="mb-4">
                        <label class="block text-lg font-semibold text-gray-700">Minimum Purchase</label>
                        <div class="relative">
                            <input type="number" name="minimumPurchaseAmount" id="minimumPurchaseAmount"
                                   value="${VOUCHER_DETAILS.minimumPurchaseAmount}" 
                                   class="w-full p-3 border border-gray-300 rounded pr-12" required>
                            <span class="absolute inset-y-0 right-3 flex items-center text-gray-500">đ</span>
                        </div>
                    </div>


                    <!-- Quantity -->
                    <div class="mb-4">
                        <label class="block text-lg font-semibold text-gray-700">Quantity</label>
                        <input type="number" name="quantity" value="${VOUCHER_DETAILS.quantity}" 
                               class="w-full p-3 border border-gray-300 rounded" required>
                    </div>

                    <!-- Date Started -->    
                    <div class="mb-4">
                        <label class="block text-lg font-semibold text-gray-700">Date Started</label>
                        <input type="date" name="dateStarted" value="${VOUCHER_DETAILS.dateStarted}" 
                               class="w-full p-3 border border-gray-300 rounded" required>
                    </div>

                    <!-- Duration -->
                    <div class="mb-4">
                        <label class="block text-lg font-semibold text-gray-700">Duration (days)</label>
                        <input type="number" name="duration" value="${VOUCHER_DETAILS.duration}" 
                               class="w-full p-3 border border-gray-300 rounded" required>
                    </div>

                    <!-- Submit Button -->
                    <div class="col-span-3 text-center">
                        <button type="submit" class="w-full bg-orange-400 text-white p-3 rounded text-lg hover:bg-orange-500">
                            Update Voucher
                        </button>
                    </div>
                </form>
                <div class="mt-6">
                    <a class="text-orange-400 hover:underline" href="voucherList">
                        <i class="fas fa-arrow-left mr-2"></i> <strong>Back to Voucher List</strong>
                    </a>
                </div>
            </div>
        </main>
        <footer class="bg-gray-200 py-4 mt-8 w-full">
            <div class="container mx-auto px-4 text-center text-sm text-gray-600">
                <a class="mr-4" href="#">Privacy</a>
                <a href="#">Purchase Terms &amp; Conditions</a>
                <p class="mt-4">© BOOK WALKER Co.,Ltd.</p>
            </div>
        </footer>
        <script>
// Show/Hide Max Discount Amount based on Voucher Type
            document.getElementById('voucherType').addEventListener('change', function () {
                document.getElementById('maxDiscountDiv').style.display =
                        this.value === 'PERCENTAGE' ? 'block' : 'none';
            });

// Initialize visibility on page load
            window.onload = function () {
                if (document.getElementById('voucherType').value === 'PERCENTAGE') {
                    document.getElementById('maxDiscountDiv').style.display = 'block';
                }
            };
        </script>
        <script>
            function validateForm(event) {
                let voucherType = document.getElementById("voucherType").value;
                let voucherValue = document.querySelector("[name='voucherValue']").value;

                if (voucherType === "PERCENTAGE") {
                    let value = parseFloat(voucherValue);
                    if (value <= 0 || value > 100) {
                        alert("Percentage discount must be between 1% and 100%.");
                        event.preventDefault();
                        return false;
                    }
                }
                return true;
            }

            function toggleMaxDiscount() {
                let voucherType = document.getElementById("voucherType").value;
                let maxDiscountDiv = document.getElementById("maxDiscountDiv");
                maxDiscountDiv.style.display = voucherType === "PERCENTAGE" ? "block" : "none";
            }

            document.addEventListener("DOMContentLoaded", function () {
                document.querySelector("form").addEventListener("submit", validateForm);
                toggleMaxDiscount();
            });
        </script>
        <script>
            function updateUnit() {
                let voucherType = document.getElementById("voucherType").value;
                let unitSpan = document.getElementById("unit");
                unitSpan.textContent = (voucherType === "PERCENTAGE") ? "%" : "VND";
            }

            document.addEventListener("DOMContentLoaded", function () {
                document.getElementById("voucherType").addEventListener("change", updateUnit);
                updateUnit(); // Cập nhật ngay khi trang tải xong
            });
        </script>

    </body>
</html>