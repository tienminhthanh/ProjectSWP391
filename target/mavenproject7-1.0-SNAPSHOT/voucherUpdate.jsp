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
            <div class="container mx-auto px-4 py-2 flex justify-between items-center">
                <img alt="Book Walker Logo" class="h-10" height="50" src="https://storage.googleapis.com/a1aa/image/eqONjY2PAhJPB-SS1k-WJ6Cn3CmR-ITt6O9vKa2fKhk.jpg" width="150"/>
                <div class="flex items-center space-x-4">
                    <i class="fas fa-globe text-xl"></i>
                </div>
            </div>
        </header>
        <main class="flex-grow flex items-center justify-center">
            <div class="w-full max-w-4xl bg-white p-8 shadow-md">
                <h1 class="text-2xl font-semibold mb-4">Update Voucher</h1>
                <hr class="mb-6"/>
                <c:if test="${not empty message}">
                    <p class="text-red-600 text-center mb-4">${message}</p>
                </c:if>
                <form action="voucherUpdate" method="post" class="grid grid-cols-1 md:grid-cols-3 gap-4">
                    <!-- Voucher ID (Không chỉnh sửa) -->
                    <div class="mb-4">
                        <label class="block text-sm font-medium text-gray-700">Voucher ID</label>
                        <input type="text" name="voucherID" value="${VOUCHER_DETAILS.voucherID}" 
                               class="w-full p-3 border border-gray-300 rounded bg-gray-100" readonly>
                    </div>

                    <!-- Voucher Name -->
                    <div class="mb-4">
                        <label class="block text-sm font-medium text-gray-700">Voucher Name</label>
                        <input type="text" name="voucherName" value="${VOUCHER_DETAILS.voucherName}" 
                               class="w-full p-3 border border-gray-300 rounded" required>
                    </div>

                    <!-- Voucher Value -->
                    <div class="mb-4">
                        <label class="block text-sm font-medium text-gray-700">Voucher Value</label>
                        <input type="number" name="voucherValue" value="${VOUCHER_DETAILS.voucherValue}" 
                               class="w-full p-3 border border-gray-300 rounded" required>
                    </div>

                    <!-- Quantity -->
                    <div class="mb-4">
                        <label class="block text-sm font-medium text-gray-700">Quantity</label>
                        <input type="number" name="quantity" value="${VOUCHER_DETAILS.quantity}" 
                               class="w-full p-3 border border-gray-300 rounded" required>
                    </div>

                    <!-- Minimum Purchase Amount -->
                    <div class="mb-4">
                        <label class="block text-sm font-medium text-gray-700">Minimum Purchase</label>
                        <input type="number" name="minimumPurchaseAmount" value="${VOUCHER_DETAILS.minimumPurchaseAmount}" 
                               class="w-full p-3 border border-gray-300 rounded" required>
                    </div>

                    <!-- Duration -->
                    <div class="mb-4">
                        <label class="block text-sm font-medium text-gray-700">Duration (days)</label>
                        <input type="number" name="duration" value="${VOUCHER_DETAILS.duration}" 
                               class="w-full p-3 border border-gray-300 rounded" required>
                    </div>
                               
<!--                     Date Created (Hidden, để giữ nguyên giá trị cũ) 
                    <input type="hidden" name="dateCreated" value="${VOUCHER_DETAILS.dateCreated}">

                     Admin ID (Hidden, lấy từ session của admin đang đăng nhập) 
                    <input type="hidden" name="adminID" value="${sessionScope.account.adminID}">-->

                    <!-- Submit Button -->
                    
                    <div class="col-span-3">
                        <button type="submit" class="w-full bg-blue-600 text-white p-3 rounded hover:bg-blue-700">
                            Update Voucher
                        </button>
                    </div>
                </form>




                <div class="mt-6">
                    <a class="text-blue-600 hover:underline" href="voucherList">
                        <i class="fas fa-arrow-left mr-2"></i> Back to Voucher List
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
    </body>
</html>