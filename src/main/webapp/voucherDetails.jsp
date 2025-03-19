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
        <div class="flex-1 p-6">
            <div class="w-full max-w-full bg-white p-8 shadow-lg rounded-lg">
                <h1 class="text-3xl font-bold text-gray-800 mb-6">📌 Voucher Details</h1>
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
                        <c:when test="${!empty VOUCHER_DETAILS}">
                            <div class="container mx-auto p-6">
                                <div class="voucher-card bg-white rounded-lg p-6 ">
                                    <h2 id="voucherName" class="text-2xl font-bold mb-4 uppercase text-center">
                                        ${VOUCHER_DETAILS.voucherName}
                                    </h2>
                                    <div class="voucher-info text-gray-700 text-left space-y-3">
                                        <p><strong>Voucher ID:</strong> <span id="voucherID">${VOUCHER_DETAILS.voucherID}</span></p>
                                        <p><strong>Voucher Type:</strong> <span id="voucherType">${VOUCHER_DETAILS.voucherType}</span></p>
                                        <p><strong>Value:</strong> 
                                            <span id="voucherValue">
                                                <c:choose>
                                                    <c:when test="${VOUCHER_DETAILS.voucherType eq 'PERCENTAGE'}">
                                                        ${VOUCHER_DETAILS.voucherValue} %
                                                    </c:when>
                                                    <c:otherwise>
                                                        <fmt:formatNumber value="${VOUCHER_DETAILS.voucherValue}" type="number" groupingUsed="true"/> đ
                                                    </c:otherwise>
                                                </c:choose>
                                            </span>
                                        </p>
                                        <p><strong>Quantity Available:</strong> <span id="quantity">${VOUCHER_DETAILS.quantity}</span></p>
                                        <p><strong>Minimum Purchase Amount:</strong> 
                                            <span id="minimumPurchaseAmount">
                                                <fmt:formatNumber value="${VOUCHER_DETAILS.minimumPurchaseAmount}" type="number" groupingUsed="true"/> đ
                                            </span>
                                        </p>
                                        <c:if test="${VOUCHER_DETAILS.voucherType eq 'PERCENTAGE'}">
                                            <p><strong>Max Discount Amount:</strong> 
                                                <span id="maxDiscountAmount"><fmt:formatNumber value="${VOUCHER_DETAILS.maxDiscountAmount}" type="number" groupingUsed="true" pattern="#,##0"/> đ</span>
                                            </p>
                                        </c:if>
                                        <p><strong>Date Created:</strong> <span id="dateCreated">${VOUCHER_DETAILS.dateCreated}</span></p>
                                        <p><strong>Date Started:</strong> <span id="dateStarted">${VOUCHER_DETAILS.dateStarted}</span></p>
                                        <p><strong>Duration:</strong> <span id="duration">${VOUCHER_DETAILS.duration} days</span> 
                                            (Until <span id="dateEnd">${dateEnd}</span>)
                                        </p>
                                        <p><strong>Expiry:</strong> 
                                            <span id="expiry" class="font-semibold">
                                                <c:choose>
                                                    <c:when test="${VOUCHER_DETAILS.expiry}">
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
                                                    <c:when test="${VOUCHER_DETAILS.isActive}">
                                                        <span class="text-green-600">Active</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="text-red-600">Deactivate</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </span>
                                        </p>
                                        <p><strong>Admin ID:</strong> <span id="adminID">${VOUCHER_DETAILS.adminID}</span></p>
                                    </div>
                                    <div class="mt-6 flex justify-center space-x-4">
                                        <a href="voucherUpdate?voucherID=${VOUCHER_DETAILS.voucherID}" class="bg-green-500 text-white py-2 px-4 rounded hover:bg-green-600 transition duration-200">
                                            Update
                                        </a>
                                        <c:choose>
                                            <c:when test="${VOUCHER_DETAILS.isActive}">
                                                <a class="bg-red-500 text-white py-2 px-4 rounded hover:bg-red-600 transition duration-200" href="javascript:void(0);" onclick="confirmAction('Are you sure you want to delete this voucher?', 'voucherDelete?id=${VOUCHER_DETAILS.voucherID}&action=delete')">
                                                    Delete
                                                </a>
                                            </c:when>
                                            <c:otherwise>
                                                <a class="bg-blue-500 text-white py-2 px-4 rounded hover:bg-blue-600 transition duration-200" href="javascript:void(0);" onclick="confirmAction('Are you sure you want to unlock this voucher?', 'voucherDelete?id=${VOUCHER_DETAILS.voucherID}&action=unlock')">
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
        </main>

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
