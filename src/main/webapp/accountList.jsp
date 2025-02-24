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
        <link href="https://cdn.jsdelivr.net/npm/sweetalert2@11.6.9/dist/sweetalert2.min.css" rel="stylesheet">
    </head>
    <body class="bg-gray-50 min-h-screen flex">

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
        <div class="flex-1 p-6">
            <div class="w-full max-w-full bg-white p-8 shadow-lg rounded-lg">
                <h1 class="text-3xl font-bold text-gray-800 mb-6">📌 Account List</h1>
                <hr class="mb-6 border-gray-300"/>
                <div class="mt-6 flex flex-col items-start"> 
                    <a class="bg-green-600 text-white p-4 rounded-lg hover:bg-green-700 flex items-center justify-start w-48 transition duration-300 ease-in-out transform hover:scale-105 mb-4" href="accountAddNew.jsp">
                        <i class="fas fa-plus mr-2"></i> Add New Account
                    </a>
                    <c:if test="${not empty errorMessage}">
                        <p class="text-red-600 text-center mt-4 text-sm font-semibold p-2 border border-red-500 rounded bg-red-100 w-full">
                            <i class="fas fa-exclamation-circle mr-2"></i>${errorMessage}
                        </p>
                    </c:if>
                </div>

                <!-- TABLE -->
                <div class="overflow-x-auto rounded-lg shadow-md">
                    <table class="min-w-full bg-white border border-gray-200">
                        <thead class="bg-blue-600 text-white">
                            <tr>
                                <th class="px-4 py-3 border-b">#</th>
                                <th class="px-4 py-3 border-b">Username</th>
                                <th class="px-4 py-3 border-b">First Name</th>
                                <th class="px-4 py-3 border-b">Last Name</th>
                                <th class="px-4 py-3 border-b">Email</th>
                                <th class="px-6 py-3 border-b text-left">Role</th>
                                <th class="px-4 py-3 border-b">Phone Number</th>
                                <th class="px-6 py-3 border-b w-40">Birthday</th>
                                <th class="px-6 py-3 border-b">Account Status</th>
                                <th class="px-6 py-3 border-b w-40">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="acc" items="${accounts}" varStatus="status">
                                <tr class="hover:bg-gray-100 transition duration-300">
                                    <td class="px-4 py-3 border-b text-center">${status.index + 1}</td>
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
                                    <td class="px-6 py-3 border-b text-center">${acc.birthDate}</td>
                                    <td class="px-6 py-3 border-b text-center">
                                        <c:choose>
                                            <c:when test="${acc.isActive}">
                                                <span class="bg-green-500 text-white py-1 px-3 rounded">Active</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="bg-red-500 text-white py-1 px-3 rounded">Deactivated</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="px-6 py-3 border-b text-left">
                                        <a href="updateAccount?username=${acc.username}" class="text-blue-500 hover:text-blue-700">
                                            <i class="fas fa-edit"></i> Update
                                        </a>

                                        <br>

                                        <c:choose>
                                            <c:when test="${acc.isActive}">
                                                <a class="text-red-500 hover:text-red-700 mr-3 action-btn" href="javascript:void(0);" onclick="confirmAction('Are you sure you want to delete this account?', 'deleteAccount?username=${acc.username}')">
                                                    <i class="fas fa-trash-alt"></i> Delete
                                                </a>
                                            </c:when>
                                            <c:otherwise>
                                                <a class="text-green-500 hover:text-green-700 action-btn" href="javascript:void(0);" onclick="confirmAction('Are you sure you want to unlock this account?', 'unlockAccount?username=${acc.username}')">
                                                    <i class="fas fa-unlock"></i> Unlock
                                                </a>
                                            </c:otherwise>
                                        </c:choose>

                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty accounts}">
                                <tr>
                                    <td class="px-4 py-3 border-b text-center text-gray-500 italic" colspan="10">No accounts found.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- FOOTER -->
        <footer class="bg-gray-200 py-4 mt-8 w-full">
            <div class="container mx-auto px-4 text-center text-sm text-gray-600">
                <a class="mr-4 hover:text-gray-800" href="#">Privacy</a>
                <a class="hover:text-gray-800" href="#">Terms & Conditions</a>
                <p class="mt-2">© BOOK WALKER Co.,Ltd.</p>
            </div>
        </footer>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11.6.9/dist/sweetalert2.all.min.js"></script>
        <script>
                                                    function confirmAction(message, url) {
                                                        Swal.fire({
                                                            title: 'Are you sure?',
                                                            text: message,
                                                            icon: 'warning',
                                                            showCancelButton: true,
                                                            confirmButtonText: 'Yes, do it!',
                                                            cancelButtonText: 'Cancel',
                                                            reverseButtons: true
                                                        }).then((result) => {
                                                            if (result.isConfirmed) {
                                                                window.location.href = url;
                                                            }
                                                        });
                                                    }
        </script>

</body>
</html>
