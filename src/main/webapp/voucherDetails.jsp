<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="en_US"/>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
        <title>Voucher Details</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"/>
        <link href="https://cdn.jsdelivr.net/npm/sweetalert2@11.6.9/dist/sweetalert2.min.css" rel="stylesheet">
    </head>
    <body class="bg-gray-50 min-h-screen flex">
        <!-- Sidebar -->
        <div class="w-64 bg-orange-400 text-white min-h-screen">
            <jsp:include page="navbarAdmin.jsp" flush="true"/> 
        </div>

        <!-- Main Content -->
        <div class="flex-1 p-4">
            <div class="w-full max-w-full bg-white p-6 shadow-lg rounded-lg h-[calc(100vh-2rem)] flex flex-col">
                <h1 class="text-2xl font-bold text-gray-800 mb-4">📌 Voucher Details</h1>
                <hr class="mb-4 border-gray-300"/>
                <div class="flex flex-col items-start"> 
                    <c:if test="${not empty errorMessage}">
                        <p class="text-red-600 text-center text-sm font-semibold p-2 border border-red-500 rounded bg-red-100 w-full">
                            <i class="fas fa-exclamation-circle mr-2"></i>${errorMessage}
                        </p>
                    </c:if>
                </div>

                <div class="flex-1 overflow-hidden">
                    <c:choose>
                        <c:when test="${!empty VOUCHER_DETAILS}">
                            <div class="container mx-auto p-4 pt-0 h-full flex items-center justify-center">
                                <div class="voucher-card bg-white rounded-2xl shadow-xl p-8 max-w-5xl w-full mx-auto border border-gray-100 hover:shadow-2xl transition-shadow duration-300 flex flex-col items-center">
                                    <h2 id="voucherName" class="text-4xl font-extrabold mb-6 uppercase text-center text-gray-800 tracking-widest">
                                        ${VOUCHER_DETAILS.voucherName}
                                    </h2>
                                    <div class="voucher-info text-gray-700 text-lg space-y-4 w-full max-w-4xl">
                                        <div class="grid grid-cols-2 gap-6">
                                            <p><strong class="text-gray-900 font-semibold">Voucher ID:</strong></p>
                                            <p class="text-gray-600 text-right">${VOUCHER_DETAILS.voucherID}</p>

                                            <p><strong class="text-gray-900 font-semibold">Voucher Type:</strong></p>
                                            <p class="text-gray-600 text-right">${VOUCHER_DETAILS.voucherType}</p>

                                            <p><strong class="text-gray-900 font-semibold">Value:</strong></p>
                                            <p class="font-medium text-blue-600 text-right">
                                                <c:choose>
                                                    <c:when test="${VOUCHER_DETAILS.voucherType eq 'PERCENTAGE'}">
                                                        ${VOUCHER_DETAILS.voucherValue} %
                                                    </c:when>
                                                    <c:otherwise>
                                                        <fmt:formatNumber value="${VOUCHER_DETAILS.voucherValue}" type="number" groupingUsed="true"/> đ
                                                    </c:otherwise>
                                                </c:choose>
                                            </p>

                                            <p><strong class="text-gray-900 font-semibold">Quantity Available:</strong></p>
                                            <p class="text-gray-600 text-right">${VOUCHER_DETAILS.quantity}</p>

                                            <p><strong class="text-gray-900 font-semibold">Minimum Purchase:</strong></p>
                                            <p class="text-gray-600 text-right">
                                                <fmt:formatNumber value="${VOUCHER_DETAILS.minimumPurchaseAmount}" type="number" groupingUsed="true"/> đ
                                            </p>

                                            <c:if test="${VOUCHER_DETAILS.voucherType eq 'PERCENTAGE'}">
                                                <p><strong class="text-gray-900 font-semibold">Max Discount:</strong></p>
                                                <p class="text-gray-600 text-right"><fmt:formatNumber value="${VOUCHER_DETAILS.maxDiscountAmount}" type="number" groupingUsed="true" pattern="#,##0"/> đ</p>
                                            </c:if>

                                            <p><strong class="text-gray-900 font-semibold">Date Created:</strong></p>
                                            <p class="text-gray-600 text-right">${VOUCHER_DETAILS.dateCreated}</p>

                                            <p><strong class="text-gray-900 font-semibold">Date Started:</strong></p>
                                            <p class="text-gray-600 text-right">${VOUCHER_DETAILS.dateStarted}</p>

                                            <p><strong class="text-gray-900 font-semibold">Duration:</strong></p>
                                            <p class="text-gray-600 text-right">${VOUCHER_DETAILS.duration} days <span class="text-sm text-gray-500">(Until ${dateEnd})</span></p>

                                            <p><strong class="text-gray-900 font-semibold">Expiry:</strong></p>
                                            <p class="text-right">
                                                <span id="expiry" class="font-semibold">
                                                    <c:choose>
                                                        <c:when test="${VOUCHER_DETAILS.expiry}">
                                                            <span class="text-green-600 bg-green-100 px-3 py-1 rounded-full text-sm">Available</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="text-red-600 bg-red-100 px-3 py-1 rounded-full text-sm">Expired</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </span>
                                            </p>

                                            <p><strong class="text-gray-900 font-semibold">Status:</strong></p>
                                            <p class="text-right">
                                                <span id="isActive" class="font-semibold">
                                                    <c:choose>
                                                        <c:when test="${VOUCHER_DETAILS.isActive}">
                                                            <span class="text-green-600 bg-green-100 px-3 py-1 rounded-full text-sm">Active</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="text-red-600 bg-red-100 px-3 py-1 rounded-full text-sm">Deactivate</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </span>
                                            </p>

                                            <p><strong class="text-gray-900 font-semibold">Admin ID:</strong></p>
                                            <p class="text-gray-600 text-right">${VOUCHER_DETAILS.adminID}</p>
                                        </div>
                                    </div>
                                    <div class="mt-8 flex justify-center space-x-4">
                                        <a href="voucherUpdate?voucherID=${VOUCHER_DETAILS.voucherID}" class="bg-green-600 text-white py-2 px-6 rounded-lg hover:bg-green-700 transition duration-200 shadow-md text-lg font-medium">
                                            Update
                                        </a>
                                        <c:choose>
                                            <c:when test="${VOUCHER_DETAILS.isActive}">
                                                <a class="bg-red-600 text-white py-2 px-6 rounded-lg hover:bg-red-700 transition duration-200 shadow-md text-lg font-medium" href="javascript:void(0);" onclick="confirmAction('Are you sure you want to delete this voucher?', 'voucherDelete?id=${VOUCHER_DETAILS.voucherID}&action=delete')">
                                                    Delete
                                                </a>
                                            </c:when>
                                            <c:otherwise>
                                                <a class="bg-blue-600 text-white py-2 px-6 rounded-lg hover:bg-blue-700 transition duration-200 shadow-md text-lg font-medium" href="javascript:void(0);" onclick="confirmAction('Are you sure you want to unlock this voucher?', 'voucherDelete?id=${VOUCHER_DETAILS.voucherID}&action=unlock')">
                                                    Unlock
                                                </a>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="text-center text-gray-500 italic p-4">No voucher found.</div>
                        </c:otherwise>
                    </c:choose>
                </div>
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
            function navigateToUpdate(voucherId) {
                window.location = 'listAccount?voucherId=' + voucherId;
            }
        </script>
    </body>
</html>