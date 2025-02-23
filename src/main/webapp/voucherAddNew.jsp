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
            <div class="container mx-auto px-4 py-2 flex justify-between items-center">
                <img alt="Book Walker Logo" class="h-10" src="https://storage.googleapis.com/a1aa/image/eqONjY2PAhJPB-SS1k-WJ6Cn3CmR-ITt6O9vKa2fKhk.jpg" width="150"/>
                <div class="flex items-center space-x-4">
                    <i class="fas fa-gift text-xl"></i>
                </div>
            </div>
        </header>
        <main class="flex-grow flex items-center justify-center">
            <div class="w-full max-w-4xl bg-white p-8 shadow-md">
                <h1 class="text-2xl font-semibold mb-4">Add New Voucher</h1>
                <hr class="mb-6"/>
                <form action="voucherAddNew" method="post">
                    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div class="mb-4">
                            <label class="block text-sm font-medium text-gray-700">Voucher Name</label>
                            <input class="w-full p-3 border border-gray-300 rounded" name="voucherName" placeholder="Enter voucher name" required type="text"/>
                        </div>
                        <div class="mb-4">
                            <label class="block text-sm font-medium text-gray-700">Voucher Value</label>
                            <input class="w-full p-3 border border-gray-300 rounded" name="voucherValue" placeholder="Enter value" required type="number" step="0.01"/>
                        </div>
                        <div class="mb-4">
                            <label class="block text-sm font-medium text-gray-700">Quantity</label>
                            <input class="w-full p-3 border border-gray-300 rounded" name="quantity" placeholder="Enter quantity" required type="number"/>
                        </div>
                        <div class="mb-4">
                            <label class="block text-sm font-medium text-gray-700">Minimum Purchase Amount</label>
                            <input class="w-full p-3 border border-gray-300 rounded" name="minimumPurchaseAmount" placeholder="Enter minimum purchase" required type="number"/>
                        </div>
                        <div class="mb-4">
                            <label class="block text-sm font-medium text-gray-700">Duration (days)</label>
                            <input class="w-full p-3 border border-gray-300 rounded" name="duration" placeholder="Enter duration" required type="number"/>
                        </div>
                    </div>
                    <button class="w-full bg-blue-600 text-white p-3 rounded hover:bg-blue-700 mt-4" type="submit">Add Voucher</button>
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
                <a href="#">Terms & Conditions</a>
                <p class="mt-4">© BOOK WALKER Co.,Ltd.</p>
            </div>
        </footer>
    </body>
</html>
