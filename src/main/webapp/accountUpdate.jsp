<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
        <title>Update Account</title>
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
                <h1 class="text-2xl font-semibold mb-4">Update Account</h1>
                <hr class="mb-6"/>
                <c:if test="${not empty message}">
                    <p class="text-red-600 text-center mb-4">${message}</p>
                </c:if>
                <form action="updateAccount" method="post">
                    <input type="hidden" name="username" value="${account.username}">

                    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div class="mb-4">
                            <label class="sr-only" for="firstName">First Name</label>
                            <input class="w-full p-3 border border-gray-300 rounded" id="firstName" name="firstName"
                                   placeholder="First Name" required type="text" value="${account.firstName}"/>
                        </div>
                        <div class="mb-4">
                            <label class="sr-only" for="lastName">Last Name</label>
                            <input class="w-full p-3 border border-gray-300 rounded" id="lastName" name="lastName"
                                   placeholder="Last Name" required type="text" value="${account.lastName}"/>
                        </div>
                        <div class="mb-4">
                            <label class="sr-only" for="email">Email</label>
                            <input class="w-full p-3 border border-gray-300 rounded" id="email" name="email"
                                   placeholder="Email" required type="email" value="${account.email}"/>
                        </div>
                        <div class="mb-4">
                            <label class="sr-only" for="phoneNumber">Phone Number</label>
                            <input class="w-full p-3 border border-gray-300 rounded" id="phoneNumber" name="phoneNumber"
                                   placeholder="Phone Number" required type="text" value="${account.phoneNumber}"/>
                        </div>
                        <div class="mb-4">
                            <label class="sr-only" for="birthDate">Birth Date</label>
                            <input class="w-full p-3 border border-gray-300 rounded" id="birthDate" name="birthDate"
                                   placeholder="Birth Date" required type="date" value="${account.birthDate}"/>
                        </div>

                        <!-- Chỉ admin mới thấy phần chọn role -->
                        <c:if test="${sessionScope.account.role eq 'admin'}">
                            <div class="mb-4">
                                <label class="sr-only" for="role">Role</label>
                                <select class="w-full p-3 border border-gray-300 rounded" id="role" name="role">
                                    <option value="customer" ${account.role eq 'customer' ? 'selected' : ''}>Customer</option>
                                    <option value="staff" ${account.role eq 'staff' ? 'selected' : ''}>Staff</option>
                                    <option value="shipper" ${account.role eq 'shipper' ? 'selected' : ''}>Shipper</option>
                                    <option value="admin" ${account.role eq 'admin' ? 'selected' : ''}>Admin</option>
                                </select>
                            </div>
                        </c:if>
                    </div>

                    <button class="w-full bg-blue-600 text-white p-3 rounded hover:bg-blue-700 mt-4" type="submit">Update</button>
                </form>

                <div class="mt-6">
                    <a class="text-blue-600 hover:underline" href="listAccount">
                        <i class="fas fa-arrow-left mr-2"></i> Back to list
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