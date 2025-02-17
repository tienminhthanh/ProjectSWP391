<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
        <title>Account List</title>
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
            <div class="w-full max-w-6xl bg-white p-8 shadow-md">
                <h1 class="text-2xl font-semibold mb-4">Account List</h1>
                <hr class="mb-6"/>
                <div class="overflow-x-auto">
                    <table class="min-w-full bg-white border border-gray-300">
                        <thead>
                            <tr>
                                <th class="px-4 py-2 border-b">Username</th>
                                <th class="px-4 py-2 border-b">First Name</th>
                                <th class="px-4 py-2 border-b">Last Name</th>
                                <th class="px-4 py-2 border-b">Email</th>
                                <th class="px-4 py-2 border-b">Role</th>
                                <th class="px-4 py-2 border-b">Phone Number</th>
                                <th class="px-4 py-2 border-b">Birthday</th>
                                <th class="px-4 py-2 border-b">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="acc" items="${accounts}">
                                <tr>
                                    <td class="px-4 py-2 border-b">${acc.username}</td>
                                    <td class="px-4 py-2 border-b">${acc.firstName}</td>
                                    <td class="px-4 py-2 border-b">${acc.lastName}</td>
                                    <td class="px-4 py-2 border-b">${acc.email}</td>
                                    <td class="px-4 py-2 border-b">${acc.role}</td>
                                    <td class="px-4 py-2 border-b">${acc.phoneNumber}</td>
                                    <td class="px-4 py-2 border-b">${acc.birthDate}</td>
                                    <td class="px-4 py-2 border-b">
                                        <a class="text-blue-600 hover:underline mr-2" href="updateAccount?username=${acc.username}">
                                            <i class="fas fa-edit"></i> Update
                                        </a>
                                        <a class="text-red-600 hover:underline" href="deleteAccount?username=${acc.username}">
                                            <i class="fas fa-trash"></i> Delete
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty accounts}">
                                <tr>
                                    <td class="px-4 py-2 border-b text-center" colspan="8">No accounts found.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
                <div class="mt-6">
                    <a class="bg-green-600 text-white p-3 rounded hover:bg-green-700 flex items-center" href="accountAddNew.jsp">
                        <i class="fas fa-plus mr-2"></i> Add New Account
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