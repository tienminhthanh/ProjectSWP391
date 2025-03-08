<%@ page contentType="text/html; charset=UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
        <title>Add New Account</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"/>
    </head>
    <body class="bg-gray-100 min-h-screen flex">

        <!-- Sidebar -->
        <div class="w-64 bg-blue-900 text-white min-h-screen">
            <div class="p-4">
                <img alt="Company Logo" class="mb-4" height="50" src="./img/logo.png" width="220"/>
            </div>
            <nav class="space-y-2">
                <a class="flex items-center p-2 hover:bg-blue-800" href="#">
                    <i class="fas fa-tachometer-alt mr-2"></i>
                    Dashboard
                </a>
                <a class="flex items-center p-2 bg-blue-700 text-white hover:bg-blue-800 rounded-lg"  href="listAccount">
                    <i class="fas fa-users mr-2"></i>
                    Account List
                </a>
                <a class="flex items-center p-2 hover:bg-blue-800" href="#">
                    <i class="fas fa-calendar-alt mr-2"></i>
                    Event List
                </a>
                <a class="flex items-center p-2 hover:bg-blue-800" href="#">
                    <i class="fas fa-cogs mr-2"></i>
                    Product List
                </a>
                <a class="flex items-center p-2 hover:bg-blue-800" href="#">
                    <i class="fas fa-comments mr-2"></i>
                    Dialogue List
                </a>
                <a class="flex items-center p-2 hover:bg-blue-800" href="#">
                    <i class="fas fa-box mr-2"></i>
                    Order List
                </a>

                <a class="flex items-center p-2 hover:bg-blue-800" href="voucherList">
                    <i class="fas fa-gift mr-2"></i>
                    Voucher List
                </a>


                <a class="flex items-center p-2 hover:bg-blue-800" href="#">
                    <i class="fas fa-bell mr-2"></i>
                    Notification List
                </a>
                <a class="flex items-center p-2 hover:bg-blue-800" href="#">
                    <i class="fas fa-comment-dots mr-2"></i>
                    Chat
                </a>
                <div class="mt-4">
                    <h3 class="px-2 text-sm font-semibold"> SETTINGS </h3>
                    <a class="flex items-center p-2 hover:bg-blue-800" href="#">
                        <i class="fas fa-cogs mr-2"></i>
                        Configuration
                    </a>
                    <a class="flex items-center p-2 hover:bg-blue-800" href="#">
                        <i class="fas fa-users-cog mr-2"></i>
                        Management
                    </a>
                    <a class="flex items-center p-2 hover:bg-blue-800" href="logout">
                        <i class="fas fa-sign-out-alt mr-2"></i> 
                        Logout
                    </a>
                </div>
                <div class="mt-4">
                    <h3 class="px-2 text-sm font-semibold"> REPORTS </h3>
                    <a class="flex items-center p-2 hover:bg-blue-800" href="#">
                        <i class="fas fa-phone-alt mr-2"></i>
                        Call history
                    </a>
                    <a class="flex items-center p-2 hover:bg-blue-800" href="#">
                        <i class="fas fa-headset mr-2"></i>
                        Call queue
                    </a>
                    <a class="flex items-center p-2 hover:bg-blue-800" href="#">
                        <i class="fas fa-users mr-2"></i>
                        Agents performance
                    </a>
                    <a class="flex items-center p-2 hover:bg-blue-800" href="#">
                        <i class="fas fa-file-invoice-dollar mr-2"></i>
                        Commission report
                    </a>
                    <a class="flex items-center p-2 hover:bg-blue-800" href="#">
                        <i class="fas fa-calendar mr-2"></i>
                        Scheduled report
                    </a>
                    <a class="flex items-center p-2 hover:bg-blue-800" href="#">
                        <i class="fas fa-history mr-2"></i>
                        Chat history
                    </a>
                    <a class="flex items-center p-2 bg-blue-800" href="#">
                        <i class="fas fa-chart-line mr-2"></i>
                        Performance report
                    </a>
                </div>
            </nav>
        </div>

        <!-- Main Content -->
        <main class="flex-1 p-6">
            <div class="w-full max-w-full bg-white p-8 shadow-lg rounded-lg">

                <!-- Tiêu đề -->
                <h1 class="text-3xl font-bold text-gray-800 mb-6 flex items-center">
                    <i class="fas fa-user-plus text-blue-600 text-3xl mr-3"></i> Add New Account
                </h1>
                <hr class="mb-6 border-gray-300"/>

                <!-- Hiển thị thông báo lỗi nếu có -->
                <c:if test="${not empty message}">
                    <p class="text-red-600 text-center mt-4 text-sm font-semibold p-2 border border-red-500 rounded bg-red-100 w-full">
                        <i class="fas fa-exclamation-circle mr-2"></i>${message}
                    </p>
                </c:if>

                <!-- Form nhập thông tin tài khoản -->
                <form action="addAccount" method="post" class="space-y-6">
                    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                        <div>
                            <label class="text-gray-700 font-semibold">Username</label>
                            <input class="w-full p-4 border border-gray-300 rounded focus:ring-blue-500 focus:border-blue-500" 
                                   id="username" name="username" value="${username}" placeholder="Enter username" required type="text"/>
                        </div>
                        <div>
                            <label class="text-gray-700 font-semibold">Password</label>
                            <input class="w-full p-4 border border-gray-300 rounded focus:ring-blue-500 focus:border-blue-500" 
                                   id="password" name="password" placeholder="Enter password" required type="password"/>
                        </div>
                        <div>
                            <label class="text-gray-700 font-semibold">First Name</label>
                            <input class="w-full p-4 border border-gray-300 rounded focus:ring-blue-500 focus:border-blue-500" 
                                   id="firstName" name="firstName" value="${firstName}" placeholder="First Name" required type="text"/>
                        </div>
                        <div>
                            <label class="text-gray-700 font-semibold">Last Name</label>
                            <input class="w-full p-4 border border-gray-300 rounded focus:ring-blue-500 focus:border-blue-500" 
                                   id="lastName" name="lastName" value="${lastName}" placeholder="Last Name" required type="text"/>
                        </div>
                        <div>
                            <label class="text-gray-700 font-semibold">Email</label>
                            <input class="w-full p-4 border border-gray-300 rounded focus:ring-blue-500 focus:border-blue-500" 
                                   id="email" name="email" value="${email}" placeholder="Enter email" required type="email"/>
                        </div>
                        <div>
                            <label class="text-gray-700 font-semibold">Phone Number</label>
                            <input class="w-full p-4 border border-gray-300 rounded focus:ring-blue-500 focus:border-blue-500" 
                                   id="phoneNumber" name="phoneNumber" value="${phoneNumber}" placeholder="Enter phone number" required type="text"/>
                        </div>
                        <div class="mb-4">
                            <label class="sr-only" for="birthDate">Birth Date</label>
                            <input class="w-full p-3 border border-gray-300 rounded" id="birthDate" name="birthDate" placeholder="Birth Date" required type="date"/>
                        </div>
                        <div class="mb-4">
                            <label class="sr-only" for="role">Role</label>
                            <select class="w-full p-3 border border-gray-300 rounded" id="role" name="role">
                               
                                <option value="staff">Staff</option>
                                <option value="shipper">Shipper</option>
                            </select>
                        </div>
                    </div>

                    <button class="w-full bg-green-600 text-white p-4 rounded-lg hover:bg-green-700 flex items-center justify-center transition duration-300 ease-in-out transform hover:scale-105" type="submit">
                        <i class="fas fa-check-circle mr-2"></i> Add Account
                    </button>
                </form>


                <div class="mt-6 flex justify-start">
                    <a class="bg-blue-600 text-white px-6 py-3 rounded-lg hover:bg-blue-700 flex items-center transition duration-300 ease-in-out transform hover:scale-105" href="listAccount">
                        <i class="fas fa-arrow-left mr-2"></i> Back to Account List
                    </a>
                </div>
            </div>
        </main>

    </body>
</html>
