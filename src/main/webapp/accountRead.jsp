<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <title>View Account Information</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"/>
        <link href="https://cdn.jsdelivr.net/npm/sweetalert2@11.6.9/dist/sweetalert2.min.css" rel="stylesheet">
    </head>
    <body class="bg-gray-100 min-h-screen flex flex-col">

        <!-- Header -->
        <header class="bg-white shadow w-full">
            <a href="home" class="container mx-auto px-4 py-2 flex justify-between items-center">
                <img alt="WIBOOKS Logo" class="h-10" src="./img/logoWibooks-removebg-preview.png"/>
                <div class="flex items-center space-x-4">
                    <i class="fas fa-globe text-xl"></i>
                </div>
            </a>
        </header>

        <!-- Main Content -->
        <main class="flex-grow flex items-center justify-center">
            <div class="w-full max-w-4xl bg-white p-8 shadow-md rounded-lg">
                <h1 class="text-2xl font-semibold mb-4">Account Information</h1>

                <hr class="mb-6"/>

                <div class="flex space-x-8">
                    <!-- Left Column: General Account Info -->
                    <div class="space-y-4 w-1/2">
                        <p class="flex items-center"><i class="fas fa-user mr-2"></i> Username: ${account.username}</p>
                        <p class="flex items-center"><i class="fas fa-id-badge mr-2"></i> First Name: ${account.firstName}</p>
                        <p class="flex items-center"><i class="fas fa-id-badge mr-2"></i> Last Name: ${account.lastName}</p>
                        <p class="flex items-center"><i class="fas fa-envelope mr-2"></i> Email: ${account.email}</p>
                        <p class="flex items-center"><i class="fas fa-phone mr-2"></i> Phone Number: ${account.phoneNumber}</p>
                        <p class="flex items-center"><i class="fas fa-birthday-cake mr-2"></i> Birth Date: ${account.birthDate}</p>

                        <!-- Role-Specific Information -->
                        <c:choose>
                            <c:when test="${account.role eq 'admin'}">
                                <p class="flex items-center"><i class="fas fa-calendar-alt mr-2"></i> Total Events: ${account.totalEvents}</p>
                                <p class="flex items-center"><i class="fas fa-ticket-alt mr-2"></i> Total Vouchers: ${account.totalVouchers}</p>
                            </c:when>

                            <c:when test="${account.role eq 'customer'}">
                                <p class="flex items-center"><i class="fas fa-map-marker-alt mr-2"></i> Default Delivery Address: ${account.defaultDeliveryAddress}</p>
                                <p class="flex items-center"><i class="fas fa-coins mr-2"></i> Purchase Points: ${account.totalPurchasePoints}</p>
                            </c:when>

                            <c:when test="${account.role eq 'staff'}">
                                <p class="flex items-center"><i class="fas fa-clock mr-2"></i> Work Shift: ${account.workShift}</p>
                                <p class="flex items-center"><i class="fas fa-box mr-2"></i> Total Orders Processed: ${account.totalOrders}</p>
                            </c:when>

                            <c:when test="${account.role eq 'shipper'}">
                                <p class="flex items-center"><i class="fas fa-map-marked-alt mr-2"></i> Delivery Areas: ${account.deliveryAreas}</p>
                                <p class="flex items-center"><i class="fas fa-truck-moving mr-2"></i> Total Deliveries: ${account.totalDeliveries}</p>
                            </c:when>
                        </c:choose>
                    </div>

                    <!-- Right Column: Avatar -->
                    <div class="w-1/2 flex flex-col items-center">
                        <c:choose>
                            <c:when test="${account.role eq 'admin'}">
                                <img src="https://www.shutterstock.com/image-vector/admin-sign-on-laptop-icon-260nw-1583341891.jpg" alt="Admin" class="w-150 rounded-full">
                            </c:when>
                            <c:when test="${account.role eq 'shipper'}">
                                <img src="https://file.hstatic.net/1000191021/file/6-icon-shipper_a9bf93829efb4de4865f698fb443338e_grande.png" alt="Shipper" class="w-150 rounded-full">
                            </c:when>
                            <c:when test="${account.role eq 'customer'}">
                                <img src="https://leapcontent.vn/wp-content/uploads/2021/05/khach-hang-feature.png" alt="Customer" class="w-150 rounded-full">
                            </c:when>
                            <c:otherwise>
                                <img src="https://i.pinimg.com/736x/65/ac/dc/65acdc3f5eed2875b4beb827d05611aa.jpg" alt="Unknown" class="w-150 rounded-full">
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <!-- Action Buttons -->
                <div class="mt-6 flex justify-between">
                    <a href="updateAccount?username=${account.username}" class="bg-blue-600 text-white p-3 rounded hover:bg-blue-700 flex items-center">
                        <i class="fas fa-edit mr-2"></i> Update Information
                    </a>
                    <a href="changePassword.jsp" class="bg-green-600 text-white p-3 rounded hover:bg-green-700 flex items-center">
                        <i class="fas fa-key mr-2"></i> Change Password
                    </a>
                    <c:if test="${sessionScope.account.role eq 'customer'}">
                        <a href="OrderListController" class="bg-green-600 text-white p-3 rounded hover:bg-blue-700 flex items-center">
                            <i class="fas fa-shopping-cart mr-2"></i> Order List
                        </a>
                    </c:if>
                    <a href="deleteAccount?username=${account.username}" class="bg-red-600 text-white p-3 rounded hover:bg-red-700 flex items-center" 
                       onclick="return confirm('Are you sure you want to delete this account?');">
                        <i class="fas fa-trash mr-2"></i> Delete Account
                    </a>
                </div>
            </div>
        </main>

        <!-- Footer -->
        <footer class="bg-gray-200 py-4 mt-8 w-full">
            <div class="container mx-auto px-4 text-center text-sm text-gray-600">
                <p>&copy; WIBOOKS Co.,Ltd.</p>
            </div>
        </footer>

    </body>
</html>
