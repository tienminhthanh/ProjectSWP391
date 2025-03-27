<%-- 
    Document   : eventDetailsAdmin
    Created on : Mar 10, 2025, 3:15:18 PM
    Author     : ADMIN
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="en_US"/>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
        <title>Event Details</title>
        <link href="css/styleCustomerSidebar.css" rel="stylesheet">
        <link rel="stylesheet" href="css/styleProductCard.css"/>
        <script src="https://cdn.tailwindcss.com"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"/>
        <link href="https://cdn.jsdelivr.net/npm/sweetalert2@11.6.9/dist/sweetalert2.min.css" rel="stylesheet">
        <!--Script for include icons-->
        <script src="https://kit.fontawesome.com/bfab6e6450.js" crossorigin="anonymous"></script>
    </head>
    <body class="bg-gray-50 min-h-screen flex">
        <!-- Sidebar -->
        <div class="w-64 bg-orange-400 text-white min-h-screen">
            <jsp:include page="navbarAdmin.jsp" flush="true"/> 
        </div>

        <!-- Main Content -->
        <div class="w-full md:w-5/6 p-6 flex flex-col">
            <div class="w-full max-w-full bg-white pt-8 pl-8 pr-8 shadow-lg rounded-lg">
                <h1 class="text-3xl font-bold text-gray-800 mb-6">📌 Event Details</h1>
                <hr class="mb-6 border-gray-300"/>
                <div class="mt-6 flex flex-col items-start"> 
                    <c:if test="${not empty errorMessage}">
                        <p class="text-red-600 text-center mt-4 text-sm font-semibold p-2 border border-red-500 rounded bg-red-100 w-full">
                            <i class="fas fa-exclamation-circle mr-2"></i>${errorMessage}
                        </p>
                    </c:if>
                </div>

                <div>
                    <c:choose>
                        <c:when test="${!empty EVENT_DETAILS}">
                            <div class="container mx-auto p-6">
                                <div class="voucher-card bg-white rounded-lg pt-6 pr-6 pl-6">
                                    <h2 id="voucherName" class="text-2xl font-bold mb-4 uppercase text-center">
                                        ${EVENT_DETAILS.eventName}
                                    </h2>

                                    <div class="voucher-info text-gray-700 text-left space-y-3">
                                        <img src="${EVENT_DETAILS.banner}" alt="Banner" class="w-full h-auto object-cover">
                                        <p><strong>Event ID:</strong> <span id="voucherID">${EVENT_DETAILS.eventID}</span></p>
                                        <p><strong>Date Created:</strong> <span id="dateCreated">${EVENT_DETAILS.dateCreated}</span></p>
                                        <p><strong>Date Started:</strong> <span id="dateCreated">${EVENT_DETAILS.dateStarted}</span></p>
                                        <p><strong>Duration:</strong> <span id="duration">${EVENT_DETAILS.duration}</span>
                                            (Until <span id="dateEnd">${dateEnd}</span>)
                                        </p>
                                        <p><strong>Description:</strong> <span id="description">${EVENT_DETAILS.description}</span></p>
                                        <p><strong>Expiry:</strong> 
                                            <span id="expiry" class="font-semibold">
                                                <c:choose>
                                                    <c:when test="${EVENT_DETAILS.expiry}">
                                                        <span class="text-green-600">Available</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="text-red-600">Expired</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </span>
                                        </p>
                                        <p><strong>Status:</strong> 
                                            <span id="isActive" class="font-semibold">
                                                <c:choose>
                                                    <c:when test="${EVENT_DETAILS.isActive}">
                                                        <span class="text-green-600">Active</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="text-red-600">Deactivate</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </span>
                                        </p>
                                        <p><strong>Admin ID:</strong> <span id="adminID">${EVENT_DETAILS.adminID}</span></p>
                                    </div>
                                    <div class="mt-6 flex justify-center space-x-4">
                                        <a href="eventUpdate?eventID=${EVENT_DETAILS.eventID}" class="bg-green-500 text-white py-2 px-4 rounded hover:bg-green-600 transition duration-200">
                                            Update
                                        </a>
                                        <c:choose>
                                            <c:when test="${EVENT_DETAILS.isActive}">
                                                <a class="bg-red-500 text-white py-2 px-4 rounded hover:bg-red-600 transition duration-200" href="javascript:void(0);" onclick="confirmAction('Are you sure you want to delete this voucher?', 'eventDelete?id=${EVENT_DETAILS.eventID}&action=delete')">
                                                    Delete
                                                </a>
                                            </c:when>
                                            <c:otherwise>
                                                <a class="bg-blue-500 text-white py-2 px-4 rounded hover:bg-blue-600 transition duration-200" href="javascript:void(0);" onclick="confirmAction('Are you sure you want to unlock this voucher?', 'eventDelete?id=${EVENT_DETAILS.eventID}&action=unlock')">
                                                    Unlock
                                                </a>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>
                        </c:when>
                    </c:choose>
                </div>
            </div>
            <div class="w-full max-w-full bg-white mt-8 mb-4 shadow-lg rounded-lg">
                <div class="w-full flex justify-between items-center px-6 mt-4">
                    <h1 class="text-2xl font-bold text-right flex-grow pr-8 mr-8">Products On Sales</h1>
                    <input type="text" id="searchInput" class="pl-8 ml-6 border border-gray-300 rounded-lg px-4 py-2 w-1/3" 
                           placeholder="Search products" onkeyup="filterProducts()">
                </div>

                <div class="mt-6 mb-8 flex items-center space-x-4 ml-10">
                    <a class="bg-green-500 text-white p-4 rounded-lg hover:bg-green-500 flex items-center justify-start transition duration-300 ease-in-out transform hover:scale-105"
                       href="javascript:void(0);"
                       onclick="checkEventStatus('${EVENT_DETAILS.isActive}', '${EVENT_DETAILS.eventID}')">
                        <i class="fas fa-plus mr-2"></i> Add New Product on Event
                    </a>

                    <a class="bg-red-500 text-white p-4 rounded-lg hover:bg-red-600 flex items-center justify-start transition duration-300 ease-in-out transform hover:scale-105"
                       href="javascript:void(0);"
                       onclick="checkDeleteStatus('${EVENT_DETAILS.isActive}', '${empty listEventProduct}', '${EVENT_DETAILS.eventID}')">
                        <i class="fas fa-trash mr-2"></i> Delete Product from Event
                    </a>
                </div>
                <!--Loop through product list-->
                <div class="w-full">
                    <div class="gap-4 w-full overflow-x-auto">
                        <div class="grid grid-flow-col auto-cols-max gap-4 min-w-max">
                            <c:forEach var="currentProduct" items="${listEventProduct}">
                                <c:set var="currentProduct" value="${currentProduct}" scope="request"/>
                                <jsp:include page="productCard.jsp"/>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11.6.9/dist/sweetalert2.all.min.js"></script>
        <script>
                           function filterProducts() {
                               let input = document.getElementById("searchInput").value.toLowerCase();
                               let productCards = document.querySelectorAll(".grid.grid-flow-col.auto-cols-max > div");

                               productCards.forEach(product => {
                                   let productName = product.innerText.toLowerCase();  // Lấy văn bản hiển thị của sản phẩm
                                   if (productName.includes(input)) {  // Kiểm tra xem tên sản phẩm có chứa chuỗi tìm kiếm không
                                       product.style.display = "block";  // Nếu có, hiển thị sản phẩm
                                   } else {
                                       product.style.display = "none";  // Nếu không, ẩn sản phẩm
                                   }
                               });
                           }
        </script>

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
            function navigateToUpdate(eventId) {
                window.location = 'eventList?eventId=' + eventId;
            }
        </script>
        <script>
            function checkEventStatus(isActive, eventId) {
                window.location.href = 'eventProductAddNew?eventId=' + eventId;
            }
        </script>
        <script>
            function checkDeleteStatus(isActive, isEmpty, eventId) {
                if (isEmpty === 'true') {
                    Swal.fire({
                        title: 'No products found!',
                        text: 'There are no products to delete from this event.',
                        icon: 'info',
                        confirmButtonText: 'OK'
                    });
                } else {
                    window.location.href = 'eventProductDelete?eventId=' + eventId;
                }
            }
        </script>
        <c:if test="${not empty sessionScope.message}">
            <div id="popupMessage" class="fixed top-5 right-5 bg-green-500 text-white px-4 py-2 rounded shadow-lg transition-opacity duration-500">
                <strong>${sessionScope.message}</strong>
            </div>
            <c:remove var="message" scope="session"/>
            <c:remove var="messageType" scope="session"/>

            <script>
                // Tự động biến mất sau 3 giây
                setTimeout(() => {
                    let popup = document.getElementById("popupMessage");
                    if (popup) {
                        popup.style.opacity = "0";
                        setTimeout(() => popup.remove(), 500); // Xóa khỏi DOM sau khi animation kết thúc
                    }
                }, 3000);
            </script>
        </c:if>
        <script src="js/scriptProductCard.js"></script>
    </body>
</html>