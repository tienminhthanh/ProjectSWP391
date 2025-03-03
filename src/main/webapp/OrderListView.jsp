<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Order List</title>      
        <script src="https://cdn.tailwindcss.com"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">    </head>
        <style>
        .order-card {
            position: relative;
            overflow: hidden;
            transition: all 0.3s ease;
        }
        .order-card::before {
            content: "";
            position: absolute;
            top: 0;
            left: 0;
            width: 4px;
            height: 100%;
            background: #EA580C; /* Tailwind orange-600 */
            transition: all 0.3s ease;
            opacity: 0.7;
        }

        .order-card:hover::before {
            width: 6px;
            opacity: 1;
        }

        .status-badge {
            padding: 4px 12px;
            border-radius: 20px;
            font-size: 0.875rem;
            font-weight: 500;
            text-transform: capitalize;
        }
    </style>


    <body class="bg-gray-50">
        <header class="bg-white shadow">
            <div class="container mx-auto px-4 py-2 flex justify-between items-center">
                <div class="logo">
                    <a href="home">
                        <img src="img/logo.png" alt="WIBOOKS" class="h-10"/>
                    </a> 
                </div>
                <a href="logout" class="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600 flex items-center">
                    <i class="fas fa-sign-out-alt mr-2"></i> Sign-out
                </a>
            </div>
        </header>
        <!--        <div class="w-64 flex-shrink-0">
                    <div class="bg-white p-6 rounded-xl shadow-sm">
                        <h3 class="text-lg font-semibold text-gray-800 mb-4">Filter Orders</h3>
                        <nav class="space-y-2">
                            <a href="OrderListController" 
                               class="block px-4 py-2 rounded-lg hover:bg-gray-50 ${empty param.status ? 'bg-orange-100 text-orange-700' : 'text-gray-600'} transition-colors">
                                All Orders
                                <span class="float-right">${fn:length(requestScope.list)}</span>
                            </a>
                            <a href="OrderListController?status=Processing" 
                               class="block px-4 py-2 rounded-lg hover:bg-gray-50 ${param.status == 'Processing' ? 'bg-orange-100 text-orange-700' : 'text-gray-600'} transition-colors">
                                Processing
        <c:set var="processingCount" value="${0}"/>
        <c:forEach items="${requestScope.list}" var="order">
            <c:if test="${order.orderStatus == 'Processing'}">
                <c:set var="processingCount" value="${processingCount + 1}"/>
            </c:if>
        </c:forEach>
        <span class="float-right">${processingCount}</span>
    </a>
    <a href="OrderListController?status=Delivered" 
       class="block px-4 py-2 rounded-lg hover:bg-gray-50 ${param.status == 'Delivered' ? 'bg-orange-100 text-orange-700' : 'text-gray-600'} transition-colors">
        Delivered
        <c:set var="deliveredCount" value="${0}"/>
        <c:forEach items="${requestScope.list}" var="order">
            <c:if test="${order.orderStatus == 'Delivered'}">
                <c:set var="deliveredCount" value="${deliveredCount + 1}"/>
            </c:if>
        </c:forEach>
        <span class="float-right">${deliveredCount}</span>
    </a>
    <a href="OrderListController?status=Cancelled" 
       class="block px-4 py-2 rounded-lg hover:bg-gray-50 ${param.status == 'Cancelled' ? 'bg-orange-100 text-orange-700' : 'text-gray-600'} transition-colors">
        Cancelled
        <c:set var="cancelledCount" value="${0}"/>
        <c:forEach items="${requestScope.list}" var="order">
            <c:if test="${order.orderStatus == 'Cancelled'}">
                <c:set var="cancelledCount" value="${cancelledCount + 1}"/>
            </c:if>
        </c:forEach>
        <span class="float-right">${cancelledCount}</span>
    </a>
</nav>
</div>
</div>-->

        <main class="container mx-auto px-4 py-8">
        <div class="mb-8 text-center">
            <h1 class="text-4xl font-bold text-gray-800 mb-2 relative inline-block">
                <span class="bg-clip-text text-transparent bg-gradient-to-r from-orange-600 to-green-500">
                    ORDER List
                </span>
            </h1>
            <p class="text-gray-600 mt-2">Total ${fn:length(requestScope.list)} orders found</p>
        </div>

<div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-3 gap-6">
            <c:forEach items="${requestScope.list}" var="c">
                <c:set var="id" value="${c.orderID}"/>
                <div class="order-card bg-white rounded-xl shadow-sm hover:shadow-md transition-shadow duration-300">
                    <div class="p-6">
                        <div class="flex justify-between items-start mb-4">
                            <div>
                                <span class="text-xs font-semibold text-orange-600 uppercase tracking-wide">ORDER ID</span>
                                <h3 class="text-xl font-bold text-gray-800">#${c.orderID}</h3>
                            </div>
                            <span class="status-badge
                                  ${c.orderStatus == 'Delivered' ? 'bg-green-100 text-green-800' : 
                                    c.orderStatus == 'Processing' ? 'bg-orange-100 text-orange-800' : 
                                    c.orderStatus == 'Cancelled' ?'bg-red-100 text-red-800' : 'bg-gray-100 text-gray-800'}}">
                                  ${c.orderStatus}
                              </span>
                        </div>

                        <div class="space-y-3 text-sm">
                            <div class="flex items-center">
                                <i class="fas fa-calendar-day text-gray-400 mr-2 w-5"></i>
                                <span class="font-medium text-gray-700">
                                    <fmt:formatDate value="${c.orderDate}" pattern="dd/MM/yyyy"/>
                                </span>
                            </div>

                            <div class="flex items-start">
                                <i class="fas fa-map-marker-alt text-gray-400 mr-2 w-5 mt-1"></i>
                                <span class="text-gray-600 break-words">${c.deliveryAddress}</span>
                            </div>

                            <div class="flex items-center">
                                <i class="fas fa-wallet text-gray-400 mr-2 w-5"></i>
                                <span class="text-gray-600">${c.paymentMethod}</span>
                            </div>

                            <div class="flex items-center">
                                <i class="fas fa-shipping-fast text-gray-400 mr-2 w-5"></i>
                                <span class="text-gray-600">${c.deliveryOption.optionName}</span>
                            </div>
                        </div>

                        <div class="mt-6 pt-4 border-t border-gray-100 flex justify-between items-center">
                            <div>
                                <p class="text-xs text-gray-500">Total amount</p>
                                <p class="text-xl font-bold text-gray-800">
                                    <fmt:formatNumber value="${c.preVoucherAmount}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                                </p>
                            </div>
                            <a href="OrderDetailController?id=${id}" 
                               class="flex items-center px-4 py-2 bg-green-600 hover:bg-green-700 text-white rounded-lg transition-colors duration-200">
                                <i class="fas fa-eye mr-2"></i>
                                Details
                            </a>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

        <c:if test="${empty requestScope.list}">
            <div class="text-center py-12">
                <div class="inline-block p-6 bg-white rounded-xl shadow">
                    <i class="fas fa-box-open text-4xl text-gray-400 mb-4"></i>
                    <h3 class="text-xl font-semibold text-gray-800 mb-2">No orders found</h3>
                    <p class="text-gray-600">You haven't placed any orders yet.</p>
                </div>
            </div>
        </c:if>
    </main>
</body>
</html>