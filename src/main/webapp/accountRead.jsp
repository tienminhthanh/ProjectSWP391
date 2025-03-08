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
            <a href="${sessionScope.account.role == 'admin' ? 'listAccount' : (sessionScope.account.role == 'shipper' ? 'dashboardShipper.jsp' : 'home')}" 
               class="container mx-auto px-4 py-2 flex justify-between items-center">
                <img src="./img/logoWibooks-removebg-preview.png" alt="WIBOOKS Logo" class="h-10" height="50" width="200"/>
                <div class="flex items-center space-x-4">
                    <i class="fas ${sessionScope.account.role == 'admin' ? 'fa-user-cog' : (sessionScope.account.role == 'shipper' ? 'fa-truck' : 'fa-home')} text-xl"></i>
                </div>
            </a>
        </header>

        <!-- Main Content -->
        <main class="flex-grow flex items-center justify-center">
            <div class="w-full max-w-4xl bg-white p-8 shadow-md rounded-lg">

                <h1 class="text-2xl font-semibold mb-4">Account Information</h1>

                <c:if test="${not empty param.message}">
                    <p class="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded relative" role="alert">
                        <strong class="font-bold">Account update success!</strong>
                    </p>
                </c:if>

                <hr class="mb-6"/>

                <c:if test="${not empty message}">
                    <script>
                        Swal.fire('Notification', '${message}', 'success');
                    </script>
                </c:if>

                <div class="flex space-x-8">
                    <!-- Cột trái: Hiển thị thông tin tài khoản -->
                    <div class="space-y-4 w-1/2">
                        <p class="flex items-center"><i class="fas fa-user mr-2"></i> Username: ${account.username}</p>
                        <p class="flex items-center"><i class="fas fa-id-badge mr-2"></i> First Name: ${account.firstName}</p>
                        <p class="flex items-center"><i class="fas fa-id-badge mr-2"></i> Last Name: ${account.lastName}</p>
                        <p class="flex items-center"><i class="fas fa-envelope mr-2"></i> Email: ${account.email}</p>
                        <p class="flex items-center"><i class="fas fa-phone mr-2"></i> Phone Number: ${account.phoneNumber}</p>
                        <p class="flex items-center"><i class="fas fa-birthday-cake mr-2"></i> Birth Date: ${account.birthDate}</p>
                    </div>

                    <!-- Cột phải: Hiển thị ảnh và vai trò -->
                    <div class="w-1/2 flex flex-col items-center">
                        <c:choose>
                            <c:when test="${account.role == 'admin'}">
                                <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTnwguLPh2SY6Nfqwcz1Aw_3cSiElE3dQxNbQ&s" alt="Admin" class="w-40 h-40 rounded-full">
                                <p class="mt-4 font-semibold">Admin</p>
                            </c:when>
                            <c:when test="${account.role == 'shipper'}">
                                <img src="https://i.pinimg.com/222x/5c/61/84/5c61840474f5e4f69ca6a03507c2a569.jpg" alt="Shipper" class="w-40 h-40 rounded-full">
                                <p class="mt-4 font-semibold">Shipper</p>
                            </c:when>
                            <c:when test="${account.role == 'customer'}">
                                <img src="https://media.istockphoto.com/id/1316660437/vi/vec-to/bi%E1%BB%83u-t%C6%B0%E1%BB%A3ng-vector-tr%E1%BA%A3i-nghi%E1%BB%87m-nh%C3%A2n-vi%C3%AAn-bi%E1%BB%83u-t%C6%B0%E1%BB%A3ng-vector-x%E1%BA%BFp-h%E1%BA%A1ng-s%E1%BB%B1-h%C3%A0i-l%C3%B2ng-5-sao-bi%E1%BB%83u.jpg?s=612x612&w=0&k=20&c=yRnPWV6AK9GKTKNnAYU1ggn5kb_wLdhp4V6X9JJ0qy4=" alt="Customer" class="w-40 h-40 rounded-full">
                                <p class="mt-4 font-semibold">Customer</p>
                            </c:when>
                            <c:otherwise>
                                <img src="https://cdn-icons-png.flaticon.com/512/1946/1946429.png" alt="Unknown" class="w-40 h-40 rounded-full">
                                <p class="mt-4 font-semibold">Unknown Role</p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>



                <!-- Action Buttons -->
                <div class="mt-6 flex justify-between">
                    <a href="updateAccount?username=${account.username}" 
                       class="bg-blue-600 text-white p-3 rounded hover:bg-blue-700 flex items-center">
                        <i class="fas fa-edit mr-2"></i> Update Information
                    </a>
                    <a href="changePassword.jsp" 
                       class="bg-green-600 text-white p-3 rounded hover:bg-green-700 flex items-center">
                        <i class="fas fa-key mr-2"></i> Change Password
                    </a>
                    <c:choose>
                        <c:when test="${sessionScope.account.role eq 'customer'}">
                            <a class="bg-green-600 text-white p-3 rounded hover:bg-blue-700 flex items-center" href="OrderListController">
                                <i class="fas fa-shopping-cart mr-2"></i> Order List
                            </a>
                        </c:when>
                    </c:choose>
                    <c:if test="${sessionScope.account.role ne 'admin' or sessionScope.account.role eq 'staff'}">
                        <a href="deleteAccount?username=${account.username}" 
                           class="bg-red-600 text-white p-3 rounded hover:bg-red-700 flex items-center" 
                           onclick="return confirm('Are you sure you want to delete this account?');">
                            <i class="fas fa-trash mr-2"></i> Delete Account
                        </a>
                    </c:if>

                </div>

                <!-- Role-Specific Navigation -->
                <div class="mt-6">
                    <c:choose>
                        <c:when test="${sessionScope.account.role eq 'admin'}">
                            <a href="listAccount" class="bg-blue-600 text-white p-3 rounded-lg hover:bg-blue-700 flex items-center">
                                <i class="fas fa-users-cog mr-3"></i> Manage Accounts
                            </a>
                        </c:when>
                        <c:when test="${sessionScope.account.role eq 'shipper'}">
                            <a href="dashboardShipper.jsp" class="bg-green-600 text-white p-3 rounded-lg hover:bg-green-700 flex items-center">
                                <i class="fas fa-truck mr-3"></i> Shipper Dashboard
                            </a>
                        </c:when>
                        <c:otherwise>
                            <a href="home" class="text-blue-600 hover:underline flex items-center">
                                <i class="fas fa-home mr-3"></i> Return to Home
                            </a>
                        </c:otherwise>
                    </c:choose>
                </div>

            </div>
        </main>

        <!-- Footer -->
        <footer class="bg-gray-200 py-4 mt-8 w-full">
            <div class="container mx-auto px-4 text-center text-sm text-gray-600">
                <a href="#" class="mr-4">Privacy</a>
                <a href="#">Purchase Terms & Conditions</a>
                <p class="mt-4">&copy; WIBOOKS Co.,Ltd.</p>
            </div>
        </footer>

    </body>
</html>
