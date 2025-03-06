<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Performance Report</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet"/>
        <style>
            body {
                height: 100vh;
                font-family: 'Arial', sans-serif;
                background-color: #f3f4f6;
            }
            .sidebar {
                background-color: #1E3A8A;
                color: white;
                padding: 16px;
                min-height: 100vh;
            }
            .sidebar a {
                display: block;
                padding: 12px;
                border-radius: 5px;
                color: white;
                text-decoration: none;
                margin-bottom: 8px;
            }
            .sidebar a:hover {
                background-color: rgba(255, 255, 255, 0.2);
            }
            .card {
                background-color: white;
                border-radius: 10px;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
                padding: 20px;
                margin-bottom: 16px;
            }
            .button {
                background-color: #3B82F6;
                color: white;
                padding: 12px 20px;
                border-radius: 8px;
                font-weight: 600;
                border: none;
                cursor: pointer;
                text-align: center;
                display: inline-block;
                margin-top: 8px;
            }
            .input, .select {
                border: 1px solid #D1D5DB;
                border-radius: 6px;
                padding: 10px;
                width: 200px;
                margin-right: 8px;
            }
            .input:focus, .select:focus {
                border-color: #3B82F6;
                outline: none;
            }
            .order-info {
                display: flex;
                justify-content: space-between;
                align-items: center;
            }
            .order-details {
                flex: 1;
                margin-right: 20px; /* Space between details and buttons */
            }
            .update-section {
                display: flex;
                flex-direction: column;
            }
        </style>
    </head>
    <body>
        <div class="flex">
            <!-- Sidebar -->
            <div class="w-64 sidebar">
                <div class="p-4">
                    <img alt="Company Logo" class="mb-4" height="50" src="https://storage.googleapis.com/a1aa/image/E7a1IopinJdFFD1b8uBNgeve-ZYaN4NirThMMa4AP40.jpg" width="150"/>
                </div>
                <nav class="space-y-2">
                    <a class="flex items-center p-2 hover:bg-blue-800" href="#">
                        <i class="fas fa-tachometer-alt mr-2"></i>
                        Dashboard
                    </a>
                    <a class="flex items-center p-2 hover:bg-blue-800" href="listAccount">
                        <i class="fas fa-users mr-2"></i>
                        Account List
                    </a>
                    <a class="flex items-center p-2 hover:bg-blue-800" href="eventList">
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
                    <a class="flex items-center p-2 hover:bg-blue-800" href="OrderListForStaffController">
                        <i class="fas fa-box mr-2"></i>
                        Order List
                    </a>
                    <a class="flex items-center p-2 hover:bg-blue-800" href="#">
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
                        <h3 class="px-2 text-sm font-semibold">SETTINGS</h3>
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
                        <h3 class="px-2 text-sm font-semibold">REPORTS</h3>
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
            <div class="flex-1 p-6 overflow-y-auto">
                <div class="flex justify-between items-center mb-6">
                    <h1 class="text-2xl font-semibold">
                        Order List
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
                <!-- Lặp danh sách đơn hàng -->
                <div class="card">
                    <div class="order-info">
                        <div class="order-details">
                            <h2 class="font-semibold">Order ID: #001</h2>
                            <p>User Name: Sample Product</p>
                            <p>Address: 123 Main St, City</p>
                            <p>Status: Pending</p>
                            <p>Created Date: 2025-02-17</p>
                            <p>Payment method: COD</p>

                        </div>
                        <div class="update-section">
                            <!-- Update Shipper -->
                            <div class="flex items-center mb-2">
                                <select class="select">
                                    <option value="" disabled selected>Choose Shipper</option>
                                    <option value="1">Shipper A</option>
                                    <option value="2">Shipper B</option>
                                    <option value="3">Shipper C</option>
                                </select>
                                <button class="button">Update</button>
                            </div>

                            <!-- Update Status -->
                            <div class="flex items-center">
                                <select class="select">
                                    <option value="Pending">Pending</option>
                                    <option value="In Progress">In Progress</option>
                                    <option value="Completed">Completed</option>
                                    <option value="Cancelled">Cancelled</option>
                                </select>
                                <button class="button">Update</button>
                            </div>
                        </div>
                    </div>
                    <a href="orderDetail?id=001" class="button mt-4">View Details</a>
                </div>

                <c:forEach var="order" items="${orderList}">
                    <div class="card">
                        <div class="order-info">
                            <div class="order-details">
                                <h2 class="font-semibold">Order ID: #${order.id}</h2>
                                <p>User Name: ${order.productName}</p>
                                <p>Address: ${order.address}</p>
                                <p>Status: ${order.status}</p>
                                <p>Created Date: ${order.createdDate}</p>
                                <p>Payment Method: ${order.createdDate}</p>


                            </div>
                            <div class="update-section">
                                <!-- Update Shipper -->
                                <div class="flex items-center mb-2">
                                    <select class="select">
                                        <option value="" disabled selected>Choose Shipper</option>
                                        <c:forEach var="shipper" items="${shipperList}">
                                            <option value="${shipper.id}">${shipper.name}</option>
                                        </c:forEach>
                                    </select>
                                    <button class="button">Update</button>
                                </div>

                                <!-- Update Status -->
                                <div class="flex items-center">
                                    <select class="select">
                                        <option value="Pending">Pending</option>
                                        <option value="In Progress">In Progress</option>
                                        <option value="Completed">Completed</option>
                                        <option value="Cancelled">Cancelled</option>
                                    </select>
                                    <button class="button">Update</button>
                                </div>
                            </div>
                        </div>
                        <a href="orderDetail?id=${order.id}" class="button mt-4">View Details</a>
                    </div>
                </c:forEach>

            </div>
        </div>
    </body>
</html>