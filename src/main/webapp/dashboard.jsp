<%-- 
    Document   : dashboard
    Created on : 17 Feb 2025, 17:07:02
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>
            Performance Report
        </title>
        <script src="https://cdn.tailwindcss.com">
        </script>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet"/>
    </head>
    <body class="bg-gray-100">
        <div class="flex">
            <!-- Sidebar -->
            <div class="w-64 bg-blue-900 text-white min-h-screen">
                <div class="p-4">
                    <img alt="Company Logo" class="mb-4" height="50" src="https://storage.googleapis.com/a1aa/image/E7a1IopinJdFFD1b8uBNgeve-ZYaN4NirThMMa4AP40.jpg" width="150"/>
                </div>
                <nav class="space-y-2">
                    <a class="flex items-center p-2 hover:bg-blue-800" href="#">
                        <i class="fas fa-tachometer-alt mr-2"></i>
                        Dashboard
                    </a>
                    <a class="flex items-center p-2 hover:bg-blue-800" href="listAccount">
                        <i class="fas fa-users mr-2"></i> <!-- Cập nhật icon đây -->
                        Account List
                    </a>
                    <a class="flex items-center p-2 hover:bg-blue-800" href="eventList">
                        <i class="fas fa-calendar-alt mr-2"></i> <!-- Cập nhật icon đây -->
                        Even List
                    </a>
                    <a class="flex items-center p-2 hover:bg-blue-800" href="#">
                        <i class="fas fa-cogs mr-2"></i> <!-- Cập nhật icon đây -->
                        Product List
                    </a>
                    <a class="flex items-center p-2 hover:bg-blue-800" href="#">
                        <i class="fas fa-comments mr-2"></i> <!-- Cập nhật icon đây -->
                        Dialogue List
                    </a>
                    <a class="flex items-center p-2 hover:bg-blue-800" href="#">
                        <i class="fas fa-box mr-2"></i> <!-- Cập nhật icon đây -->
                        Order List
                    </a>
                    <a class="flex items-center p-2 hover:bg-blue-800" href="#">
                        <i class="fas fa-gift mr-2"></i> <!-- Cập nhật icon đây -->
                        Voucher List
                    </a>
                    <a class="flex items-center p-2 hover:bg-blue-800" href="listnotification">
                        <i class="fas fa-bell mr-2"></i> <!-- Cập nhật icon đây -->
                        Notification List
                    </a>
                    <a class="flex items-center p-2 hover:bg-blue-800" href="#">
                        <i class="fas fa-comment-dots mr-2"></i> <!-- Cập nhật icon đây -->
                        Chat
                    </a>
                    <div class="mt-4">
                        <h3 class="px-2 text-sm font-semibold">
                            SETTINGS
                        </h3>
                        <a class="flex items-center p-2 hover:bg-blue-800" href="#">
                            <i class="fas fa-cogs mr-2"></i> <!-- Cập nhật icon đây -->
                            Configuration
                        </a>
                        <a class="flex items-center p-2 hover:bg-blue-800" href="#">
                            <i class="fas fa-users-cog mr-2"></i> <!-- Cập nhật icon đây -->
                            Management
                        </a>
                        <a class="flex items-center p-2 hover:bg-blue-800" href="logout">
                            <i class="fas fa-sign-out-alt mr-2"></i> 
                            Logout
                        </a>
                    </div>
                    <div class="mt-4">
                        <h3 class="px-2 text-sm font-semibold">
                            REPORTS
                        </h3>
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
            <div class="flex-1 p-6">
                <div class="flex justify-between items-center mb-6">
                    <h1 class="text-2xl font-semibold">
                        Performance Report
                    </h1>
                    <div class="flex items-center space-x-4">
                        <button class="bg-orange-500 text-white px-4 py-2 rounded">
                            + Add Chart
                        </button>
                        <div class="flex items-center space-x-2">
                            <span>
                                Period
                            </span>
                            <input class="border rounded px-2 py-1" type="text" value="03/27/21 - 04/07/21"/>
                        </div>
                        <a href="readAccount" class="relative block">
                            <img alt="User Avatar" class="rounded-full" height="40" src="https://storage.googleapis.com/a1aa/image/4V1-2KXMxoMa82g0Th8dvxQbS2qGQlXogtCiodNcjgE.jpg" width="40"/>
                            <span class="absolute bottom-0 right-0 bg-green-500 rounded-full w-3 h-3"></span>
                        </a>

                    </div>
                </div>
                <div class="grid grid-cols-3 gap-4 mb-6">
                    <div class="bg-white p-4 rounded shadow">
                        <div class="flex justify-between items-center">
                            <div>
                                <h2 class="text-xl font-semibold">
                                    100
                                </h2>
                                <p>
                                    Total leads
                                </p>
                            </div>
                            <div class="text-green-500">
                                <i class="fas fa-arrow-up">
                                </i>
                                +3.5%
                            </div>
                        </div>
                    </div>
                    <div class="bg-white p-4 rounded shadow">
                        <div class="flex justify-between items-center">
                            <div>
                                <h2 class="text-xl font-semibold">
                                    80
                                </h2>
                                <p>
                                    Total called leads
                                </p>
                            </div>
                            <div class="text-red-500">
                                <i class="fas fa-arrow-down">
                                </i>
                                -15%
                            </div>
                        </div>
                    </div>
                    <div class="bg-white p-4 rounded shadow">
                        <div class="flex justify-between items-center">
                            <div>
                                <h2 class="text-xl font-semibold">
                                    120
                                </h2>
                                <p>
                                    Total applications
                                </p>
                            </div>
                        </div>
                    </div>
                    <div class="bg-white p-4 rounded shadow">
                        <div class="flex justify-between items-center">
                            <div>
                                <h2 class="text-xl font-semibold">
                                    $18,000
                                </h2>
                                <p>
                                    Total sales
                                </p>
                            </div>
                            <div class="text-green-500">
                                <i class="fas fa-arrow-up">
                                </i>
                                +2%
                            </div>
                        </div>
                    </div>
                </div>
                <div class="grid grid-cols-2 gap-4">
                    <div class="bg-white p-4 rounded shadow">
                        <h2 class="text-lg font-semibold mb-4">
                            Costs
                        </h2>
                        <div class="flex justify-center mb-4">
                            <img alt="Cost distribution chart" height="200" src="https://storage.googleapis.com/a1aa/image/fpCvVAl_5IrBGS2BqKT2BN7CIP5ukougqUqx8NaJ_nY.jpg" width="200"/>
                        </div>
                        <div class="text-center">
                            <h3 class="text-2xl font-semibold">
                                $120,640.50
                            </h3>
                            <p>
                                Total costs
                            </p>
                        </div>
                    </div>
                    <div class="bg-white p-4 rounded shadow">
                        <h2 class="text-lg font-semibold mb-4">
                            Leads
                        </h2>
                        <div class="flex justify-center mb-4">
                            <img alt="Leads bar chart" height="200" src="https://storage.googleapis.com/a1aa/image/3WmEUcccCIwjT15Ui0dG-0bzORRQJuOVjp2gKGXW-40.jpg" width="200"/>
                        </div>
                        <div class="text-center">
                            <p>
                                8,203 Total leads / 3,587 bad leads
                            </p>
                        </div>
                    </div>
                </div>
                <div class="bg-white p-4 rounded shadow mt-6">
                    <h2 class="text-lg font-semibold mb-4">
                        Total leads
                    </h2>
                    <div class="flex justify-center mb-4">
                        <img alt="Total leads bar chart" height="200" src="https://storage.googleapis.com/a1aa/image/YXehzCC4rWXbXqDPrPJX0cA3D7odzZXy8sKt9rKlk0Q.jpg" width="600"/>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
