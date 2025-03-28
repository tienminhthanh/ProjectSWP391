<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
        <title>Add New Voucher</title>
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
                <h1 class="text-2xl font-semibold mb-4">Add New Voucher</h1>
                <hr class="mb-6"/>
                <c:if test="${not empty message}">
                    <p class="text-red-600 text-center mb-4">${message}</p>
                </c:if>
                <form action="voucherAddNew" method="post">
                    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div class="mb-4">
                            <label class="block text-sm font-medium text-gray-700">Voucher Name</label>
                            <input class="w-full p-3 border border-gray-300 rounded" name="voucherName" placeholder="Enter voucher name" required type="text"/>
                        </div>
                        <div class="mb-4">
                            <label class="block text-sm font-medium text-gray-700">Voucher Type</label>
                            <select class="w-full p-3 border border-gray-300 rounded" name="voucherType" id="voucherType" required onchange="toggleMaxDiscount()">
                                <option value="PERCENTAGE">Percentage Discount</option>
                                <option value="FIXED_AMOUNT">Fixed Amount Discount</option>
                            </select>
                        </div>
                        <div class="mb-4">
                            <label class="block text-sm font-medium text-gray-700">Voucher Value</label>
                            <div class="relative">
                                <input class="w-full p-3 border border-gray-300 rounded pr-12" name="voucherValue" id="voucherValue" placeholder="Enter value" required type="number" step="0.01"/>
                                <span id="unit" class="absolute inset-y-0 right-3 flex items-center text-gray-500">%</span>
                            </div>
                        </div>
                        <div class="mb-4">
                            <label class="block text-sm font-medium text-gray-700">Quantity</label>
                            <input class="w-full p-3 border border-gray-300 rounded" name="quantity" placeholder="Enter quantity" required type="number"/>
                        </div>
                        <div class="mb-4" id="maxDiscountDiv" style="display: none;">
                            <label class="block text-sm font-medium text-gray-700">Max Discount Amount</label>
                            <div class="relative">
                                <input class="w-full p-3 border border-gray-300 rounded pr-12" name="maxDiscountAmount" placeholder="Enter max discount amount" type="number" step="0.01"/>
                                <span class="absolute inset-y-0 right-3 flex items-center text-gray-500">?</span>
                            </div>
                        </div>
                        <div class="mb-4">
                            <label class="block text-sm font-medium text-gray-700">Minimum Purchase Amount</label>
                            <div class="relative">
                                <input class="w-full p-3 border border-gray-300 rounded pr-12" name="minimumPurchaseAmount" placeholder="Enter minimum purchase" required type="number"/>
                                <span class="absolute inset-y-0 right-3 flex items-center text-gray-500">?</span>
                            </div>
                        </div>
                        <div class="mb-4">
                            <label class="block text-sm font-medium text-gray-700">Date Started</label>
                            <input class="w-full p-3 border border-gray-300 rounded" name="dateStarted" id="dateStarted" placeholder="Enter Date Started" required type="date"/>
                        </div>
                        <div class="mb-4">
                            <label class="block text-sm font-medium text-gray-700">Duration (days)</label>
                            <input class="w-full p-3 border border-gray-300 rounded" name="duration" placeholder="Enter duration" required type="number"/>
                        </div>
                    </div>
                    <button class="w-full bg-orange-400 text-white p-3 rounded hover:bg-orange-600 mt-4" type="submit">Add Voucher</button>
                </form>
                <div class="mt-6">
                    <a class="text-orange-400 hover:underline" href="voucherList">
                        <i class="fas fa-arrow-left mr-2"></i> Back to Voucher List
                    </a>
                </div>
            </div>
        </main>
        <script>
            // ??t giá tr? min cho Date Started là ngày hôm nay
            document.addEventListener("DOMContentLoaded", function () {
                const today = new Date().toISOString().split("T")[0]; // L?y ngày hi?n t?i d?ng YYYY-MM-DD
                document.getElementById("dateStarted").setAttribute("min", today);
            });

            function validateForm(event) {
                let voucherType = document.getElementById("voucherType").value;
                let voucherValue = document.querySelector("[name='voucherValue']").value;
                let dateStarted = document.getElementById("dateStarted").value;
                let today = new Date().toISOString().split("T")[0];

                // Ki?m tra Date Started không nh? h?n ngày hôm nay
                if (dateStarted < today) {
                    alert("Date Started cannot be earlier than today!");
                    event.preventDefault();
                    return false;
                }

                // Ki?m tra giá tr? ph?n tr?m
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

            function updateUnit() {
                let voucherType = document.getElementById("voucherType").value;
                let unitSpan = document.getElementById("unit");
                unitSpan.textContent = (voucherType === "PERCENTAGE") ? "%" : "VND";
            }

            document.addEventListener("DOMContentLoaded", function () {
                document.querySelector("form").addEventListener("submit", validateForm);
                document.getElementById("voucherType").addEventListener("change", updateUnit);
                toggleMaxDiscount();
                updateUnit();
            });
        </script>
    </body>
</html>