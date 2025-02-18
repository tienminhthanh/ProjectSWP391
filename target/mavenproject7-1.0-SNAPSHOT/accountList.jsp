<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
        <title>Account List</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"/>
    </head>
    <body class="bg-gray-50 min-h-screen flex flex-col">

        <!-- HEADER -->
        <header class="bg-white shadow w-full">
            <div class="container mx-auto px-6 py-3 flex justify-between items-center">
                <img alt="Book Walker Logo" class="h-12" src="https://storage.googleapis.com/a1aa/image/eqONjY2PAhJPB-SS1k-WJ6Cn3CmR-ITt6O9vKa2fKhk.jpg"/>
                <i class="fas fa-globe text-xl text-gray-600"></i>
            </div>
        </header>

        <!-- CONTENT -->
        <main class="flex-grow flex justify-center items-start py-10">
            <div class="w-full max-w-6xl bg-white p-8 shadow-lg rounded-lg">
                <h1 class="text-3xl font-bold text-gray-800 mb-6">📌 Account List</h1>
                <hr class="mb-6 border-gray-300"/>

                <!-- TABLE -->
                <div class="overflow-x-auto rounded-lg shadow-md">
                    <table class="min-w-full bg-white border border-gray-200">
                        <thead class="bg-blue-600 text-white">
                            <tr>
                                <th class="px-4 py-3 border-b">Username</th>
                                <th class="px-4 py-3 border-b">First Name</th>
                                <th class="px-4 py-3 border-b">Last Name</th>
                                <th class="px-4 py-3 border-b">Email</th>
                                <th class="px-6 py-3 border-b text-left">Role</th> <!-- Căn trái -->
                                <th class="px-4 py-3 border-b">Phone Number</th>
                                <th class="px-6 py-3 border-b w-40">Birthday</th> <!-- Rộng hơn -->
                                <th class="px-6 py-3 border-b w-40">Actions</th> <!-- Rộng hơn -->
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="acc" items="${accounts}">
                                <tr class="hover:bg-gray-100 transition duration-300">
                                    <td class="px-4 py-3 border-b text-center">${acc.username}</td>
                                    <td class="px-4 py-3 border-b text-center">${acc.firstName}</td>
                                    <td class="px-4 py-3 border-b text-center">${acc.lastName}</td>
                                    <td class="px-4 py-3 border-b text-center">${acc.email}</td>
                                    <td class="px-6 py-3 border-b text-left">
                                        <span class="px-3 py-1 rounded text-white
                                              <c:choose>
                                                  <c:when test="${acc.role == 'admin'}">bg-red-500</c:when>
                                                  <c:when test="${acc.role == 'customer'}">bg-green-500</c:when>
                                                  <c:when test="${acc.role == 'staff'}">bg-yellow-500</c:when>
                                                  <c:otherwise>bg-gray-500</c:otherwise>
                                              </c:choose>
                                              ">${acc.role}</span>
                                    </td>
                                    <td class="px-4 py-3 border-b text-center">${acc.phoneNumber}</td>
                                    <td class="px-6 py-3 border-b text-center">${acc.birthDate}</td> <!-- Rộng hơn -->
                                    <td class="px-6 py-3 border-b text-left">
                                        <a class="text-blue-500 hover:text-blue-700 mr-3" href="updateAccount?username=${acc.username}">
                                            <i class="fas fa-edit"></i> Update
                                        </a>
                                        <a class="text-red-500 hover:text-red-700" href="deleteAccount?username=${acc.username}">
                                            <i class="fas fa-trash-alt"></i> Delete
                                        </a>
                                    </td>

                                </tr>
                            </c:forEach>
                            <c:if test="${empty accounts}">
                                <tr>
                                    <td class="px-4 py-3 border-b text-center text-gray-500 italic" colspan="8">No accounts found.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>

                <!-- ADD NEW BUTTON -->
                <div class="mt-6">
                    <a class="bg-green-600 text-white p-3 rounded hover:bg-green-700 flex items-center justify-center w-48" href="accountAddNew.jsp">
                        <i class="fas fa-plus mr-2"></i> Add New Account
                    </a>
                </div>
            </div>
        </main>

        <!-- FOOTER -->
        <footer class="bg-gray-200 py-4 mt-8 w-full">
            <div class="container mx-auto px-4 text-center text-sm text-gray-600">
                <a class="mr-4 hover:text-gray-800" href="#">Privacy</a>
                <a class="hover:text-gray-800" href="#">Terms & Conditions</a>
                <p class="mt-2">© BOOK WALKER Co.,Ltd.</p>
            </div>
        </footer>

    </body>
</html>
