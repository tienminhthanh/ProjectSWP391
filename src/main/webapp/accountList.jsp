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

        <div class="w-64 bg-orange-400 text-white min-h-screen">
            <jsp:include page="navbarAdmin.jsp" flush="true"/> 
        </div>

        <!-- Main Content -->
        <div class="flex-1 p-6">
            <h1 class="text-3xl font-bold text-gray-800 mb-6">📌 Account List</h1>
            <hr class="mb-6 border-gray-300"/>

            <div class="mt-6 flex flex-col items-start">
                <!-- Add New Account Button -->
                <div class="flex items-center space-x-6">
                    <a class="bg-green-600 text-white p-4 rounded-lg hover:bg-orange-700 flex items-center justify-start w-auto transition duration-300 ease-in-out transform hover:scale-105 mb-4" href="accountAddNew.jsp">
                        <i class="fas fa-plus mr-2"></i> Add New Account
                    </a>

                    <!-- Role Filter Form -->
                    <form action="listAccount" method="get" class="flex items-center space-x-4">
                        <div class="flex items-center">
                            <label class="text-gray-700 font-semibold mr-2">Role</label>
                            <select class="p-4 border border-gray-300 rounded focus:ring-blue-500 focus:border-blue-500" name="role" onchange="this.form.submit()">
                                <option value="" ${empty param.role ? 'selected' : ''}>All Roles</option>
                                <option value="staff" ${param.role == 'staff' ? 'selected' : ''}>Staff</option>
                                <option value="shipper" ${param.role == 'shipper' ? 'selected' : ''}>Shipper</option>
                                <option value="admin" ${param.role == 'admin' ? 'selected' : ''}>Admin</option>
                            </select>
                        </div>
                    </form>
                  

                    <a class="bg-green-600 text-white p-4 rounded-lg hover:bg-orange-700 flex items-center justify-start w-auto transition duration-300 ease-in-out transform hover:scale-105 mb-4" href="accountStatic">
                         <i class="fas fa-tachometer-alt"></i> View Account Dashboard
                    </a>

                </div>


                <!-- Display Error Message if Any -->
                <c:if test="${not empty errorMessage}">
                    <p class="text-red-600 text-center mt-4 text-sm font-semibold p-2 border border-red-500 rounded bg-red-100 w-full">
                        <i class="fas fa-exclamation-circle mr-2"></i>${errorMessage}
                    </p>
                </c:if>
            </div>

            <!-- TABLE -->
            <div class="overflow-x-auto rounded-lg shadow-md">
                <table class="min-w-full bg-white border border-gray-200">
                    <thead class="bg-orange-400 text-white">
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
                                <td class="px-4 py-3 border-b text-center">${(currentPage - 1) * 3 + status.index + 1}</td>
                                <td class="px-4 py-3 border-b text-center">${acc.username}</td>
                                <td class="px-4 py-3 border-b text-center">${acc.firstName}</td>
                                <td class="px-4 py-3 border-b text-center">${acc.lastName}</td>
                                <td class="px-4 py-3 border-b text-center">${acc.email}</td>
                                <td class="px-6 py-3 border-b text-left">
                                    <span class="px-3 py-1 rounded text-white
                                          <c:choose>
                                              <c:when test="${acc.role == 'admin'}">bg-red-500</c:when>
                                              <c:when test="${acc.role == 'staff'}">bg-yellow-500</c:when>
                                              <c:when test="${acc.role == 'shipper'}">bg-green-500</c:when>
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
                                    <c:if test="${acc.role != 'admin'}">
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
                                    </c:if>
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

            <!-- Pagination Links -->
            <div class="flex justify-center mt-6">
                <nav aria-label="Page navigation">
                    <ul class="flex space-x-2">
                        <c:if test="${currentPage > 1}">
                            <li><a href="listAccount?role=${roleFilter}&page=1" class="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">First</a></li>
                            <li><a href="listAccount?role=${roleFilter}&page=${currentPage - 1}" class="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">Previous</a></li>
                            </c:if>

                        <c:forEach begin="${currentPage - 2 > 0 ? currentPage - 2 : 1}" 
                                   end="${currentPage + 2 < totalPages ? currentPage + 2 : totalPages}" 
                                   var="i">
                            <c:if test="${i > 0 && i <= totalPages}">
                                <li><a href="listAccount?role=${roleFilter}&page=${i}" 
                                       class="px-4 py-2 ${i == currentPage ? 'bg-blue-700' : 'bg-blue-500'} text-white rounded hover:bg-blue-600">${i}</a></li>
                                </c:if>
                            </c:forEach>

                        <c:if test="${currentPage < totalPages}">
                            <li><a href="listAccount?role=${roleFilter}&page=${currentPage + 1}" class="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">Next</a></li>
                            <li><a href="listAccount?role=${roleFilter}&page=${totalPages}" class="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">Last</a></li>
                            </c:if>
                    </ul>
                </nav>
            </div>

            <!-- Hiển thị thông tin phân trang -->
            <div class="text-center mt-4 text-gray-600">
                Page ${currentPage} of ${totalPages} 
                <c:if test="${not empty accounts}">
                    (Showing ${(currentPage - 1) * 3 + 1} - ${currentPage * 3 > totalAccounts ? totalAccounts : currentPage * 3} of ${totalAccounts} accounts)
                </c:if>
            </div>

        </div>

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
