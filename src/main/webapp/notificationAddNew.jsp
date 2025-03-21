<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create New Notification - Admin</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <link href="/css/styleHeader.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous"/>
        <link href="/css/styleFooter.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"/>
        <style>
            body {
                background-color: #f1f5f9; /* Nền xám nhạt */
            }
            .admin-container {
                background-color: #ffffff; /* Nền trắng thay vì #1e293b */
                color: black; /* Đổi từ trắng sang đen để khớp với nền trắng */
                border-radius: 8px;
                padding: 20px;
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            }
           
            .btn-primary {
                border: none;
                color: #ffffff; /* Chữ trắng */
            }
            .btn-primary:hover {
                background-color: #f97316; /* Orange-500 khi hover */
            }
            .btn-secondary {
                background-color: #fb923c; /* Orange-400 thay vì #64748b */
                border: none;
                color: #ffffff; /* Chữ trắng */
            }
            .btn-secondary:hover {
                background-color: #f97316; /* Orange-500 khi hover */
            }
            .btn-success {
                background-color: #fb923c; /* Orange-400 thay vì #10b981 */
                border: none;
                color: #ffffff; /* Chữ trắng */
            }
            .btn-success:hover {
                background-color: #f97316; /* Orange-500 khi hover */
            }
            .customer-row {
                cursor: pointer;
                padding: 10px;
                border-radius: 5px;
                transition: background-color 0.2s;
            }
            .customer-row:hover {
                background-color: #cccccc; /* Xám nhạt như notification-item */
            }
            .customer-list-container {
                position: relative;
            }
            .customer-list {
                max-height: 0;
                overflow-y: auto;
                transition: max-height 0.3s ease-out;
                background-color: #ffffff; /* Nền trắng thay vì #1e293b */
                border: 1px solid #fb923c; /* Viền orange-400 thay vì #475569 */
                border-radius: 5px;
                margin-top: 5px;
            }
            .customer-list.expanded {
                max-height: 300px; /* Giữ nguyên */
            }
            .customer-list::-webkit-scrollbar {
                width: 8px;
            }
            .customer-list::-webkit-scrollbar-thumb {
                background-color: #cccccc; /* Xám nhạt thay vì #64748b */
                border-radius: 4px;
            }
            .customer-list::-webkit-scrollbar-thumb:hover {
                background-color: #f97316; /* Orange-500 khi hover */
            }
            .text-orange-400 {
                color: #fb923c; /* Thêm để dùng nếu cần */
            }
            .text-orange-500 {
                color: #f97316; /* Thêm để dùng nếu cần */
            }
        </style>
    </head>
    <body>
        <div class="flex mt-4 mx-0">
            <!-- Sidebar (unchanged) -->
            <div class="w-64 bg-orange-400 text-white min-h-screen">
                <jsp:include page="navbarAdmin.jsp" flush="true"/> 
            </div> 
            <main class="flex-1 p-4">
                <div class="admin-container">
                    <h1 class="text-2xl font-bold mb-4 text-orange-400">Create New Notification</h1> <!-- Đổi màu tiêu đề -->
                    <!-- Display error message if present -->
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger text-orange-500" role="alert"> <!-- Đổi màu chữ lỗi -->
                            ${error}
                        </div>
                    </c:if>

                    <!-- Form to create a new notification -->
                    <form action="createnotification" method="post" class="space-y-4" id="notificationForm">
                        <div>
                            <label for="senderID" class="form-label">Sender ID</label>
                            <c:choose>
                                <c:when test="${not empty sessionScope.account.accountID}">
                                    <input type="number" class="form-control bg-gray-200" id="senderID" name="senderID" 
                                           value="${sessionScope.account.accountID}" readonly>
                                </c:when>
                                <c:otherwise>
                                    <input type="number" class="form-control" id="senderID" name="senderID" 
                                           required min="1">
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="customer-list-container">
                            <label class="form-label" onclick="toggleCustomerList()">Receivers <i class="fas fa-caret-down ml-2"></i></label>
                            <div class="customer-list" id="customerList">
                                <button type="button" class="btn btn-success mt-2" onclick="selectAll()">Select All</button> 
                                <c:forEach var="customer" items="${customers}">
                                    <div class="customer-row">
                                        <input type="checkbox" name="receiverID" value="${customer.accountID}" 
                                               class="mr-2">
                                        <span>${customer.lastName} ${customer.firstName} (ID: ${customer.accountID})</span>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                        <div>
                            <label for="notificationTitle" class="form-label">Notification Title</label>
                            <input type="text" class="form-control" id="notificationTitle" 
                                   name="notificationTitle" required maxlength="100">
                        </div>
                        <div>
                            <label for="notificationDetails" class="form-label">Description</label>
                            <textarea class="form-control" id="notificationDetails" 
                                      name="notificationDetails" rows="4" required maxlength="500"></textarea>
                        </div>
                        <div class="flex space-x-4">
                            <button type="submit" class="btn btn-primary">Send</button>
                            <a href="listnotification" class="btn btn-secondary">Back</a>
                        </div>
                    </form>
                </div>
            </main>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.min.js"
                integrity="sha384-BBtl+eGJRgqQAUMxJ7pMwbEyER4l1g+O15P+16Ep7Q9Q+zqX6gSbd85u4mG4QzX+"
        crossorigin="anonymous"></script>
        <script src="https://kit.fontawesome.com/bfab6e6450.js" crossorigin="anonymous"></script>
        <script src="/js/scriptHeader.js"></script>
        <script>
                                    function toggleCheckbox(row) {
                                        const checkbox = row.querySelector('input[type="checkbox"]');
                                        if (event.target !== checkbox) { // Avoid double toggle if clicking checkbox directly
                                            checkbox.checked = !checkbox.checked;
                                        }
                                    }

                                    function toggleCustomerList() {
                                        const customerList = document.getElementById('customerList');
                                        customerList.classList.toggle('expanded');
                                    }

                                    function selectAll() {
                                        const checkboxes = document.querySelectorAll('input[name="receiverID"]');
                                        checkboxes.forEach(checkbox => checkbox.checked = true);
                                    }
        </script>
    </body>
</html>