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

        <link href="css/styleHeader.css" rel="stylesheet">
        <!--Script for include icons-->
        <script src="https://kit.fontawesome.com/bfab6e6450.js" crossorigin="anonymous"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">  
        <!--Header css-->
        <link href="css/styleHeader.css" rel="stylesheet">

        <!--Footer css-->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous" />
        <link href="css/styleFooter.css" rel="stylesheet">

        <!--Banner carousel-->
        <script src="https://cdn.jsdelivr.net/npm/alpinejs@3.x.x/dist/cdn.min.js" defer></script>
        <style>
            .order-card {
                position: relative;
                overflow: hidden;
                transition: all 0.3s ease;
                box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* Thêm hiệu ứng bóng đổ nhẹ */
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

            .order-card:hover {
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15); /* Thêm hiệu ứng bóng đổ khi hover */
                transform: translateY(-2px); /* Nâng nhẹ lên khi hover */
            }

            .status-badge {
                padding: 4px 12px;
                border-radius: 20px;
                font-size: 0.875rem;
                font-weight: 500;
                text-transform: capitalize;
                transition: all 0.3s ease; /* Thêm transition để mượt mà */
            }

            .text-gray-700 {
                color: #4a5568; /* Màu mặc định */
                transition: color 0.3s ease; /* Thêm transition cho hiệu ứng chuyển màu */
            }

            .text-gray-700.active {
                color: #dd6b20; /* Change this to your desired active color */
            }

            .breadcrumb-container {
                background-color: #f8f9fa;  /* Màu nền */
                padding: 10px 20px;
                font-size: 12px;

                color: #e3a100;  /* Màu chữ vàng giống ảnh */
            }

            .breadcrumb-container a {
                text-decoration: none;
                color: #e3a100; /* Màu chữ vàng */
                transition: color 0.3s ease-in-out;
            }

            .breadcrumb-container a:hover {
                color: #d38d00; /* Đổi màu khi hover */
            }

            .breadcrumb-container .active {
                color: #e3a100; /* Giữ màu vàng cho trang hiện tại */
            }

        </style>
    </head>

    <body class="bg-gray-50">
        <header>
            <div class="header-container">
                <jsp:include page="header.jsp" flush="true"/> 
            </div>    
            <nav class="breadcrumb-container">
                <a href="/">Home</a> >
                <a href="/readAccount">Account</a> >
                <span class="active">Order List</span>
            </nav>



        </header>
        <nav class="bg-white shadow-md">

            <div class="container mx-auto px-4 py-2 flex justify-around">

                <a href="OrderListController?status=#" class="text-gray-700 hover:text-orange-600 font-semibold ${currentStatus == '#' ? 'active' : ''}">Payment Due</a>
                <a href="OrderListController?status=pending" class="text-gray-700 hover:text-orange-600 font-semibold ${currentStatus == 'pending' ? 'active' : ''}">Pending</a>
                <a href="OrderListController?status=Shipped" class="text-gray-700 hover:text-orange-600 font-semibold ${currentStatus == 'Shipped' ? 'active' : ''}">Shipping</a>
                <a href="OrderListController?status=delivered" class="text-gray-700 hover:text-orange-600 font-semibold ${currentStatus == 'delivered' ? 'active' : ''}">Delivered</a>
                <a href="OrderListController?status=completed" class="text-gray-700 hover:text-orange-600 font-semibold ${currentStatus == 'completed' ? 'active' : ''}">History</a>
                <a href="OrderListController?status=canceled" class="text-gray-700 hover:text-orange-600 font-semibold ${currentStatus == 'canceled' ? 'active' : ''}">Canceled</a>
            </div>
        </nav>
        <main class="container mx-auto px-4 py-8">
            <div class="mb-8 text-center">

                <p class="text-gray-600 mt-2">Total ${fn:length(requestScope.list)} orders found</p>
            </div>

            <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-2 lg:grid-cols-2 gap-6">
                <c:forEach items="${requestScope.list}" var="c">
                    <c:set var="id" value="${c.orderID}"/>
                    <div class="order-card bg-white rounded-xl shadow-sm hover:shadow-md transition-shadow duration-300 relative">
                        <div class="p-6">
                            <div class="flex justify-between items-start mb-4">
                                <div>
                                    <span class="text-xs font-semibold text-orange-600 uppercase tracking-wide">ORDER ID</span>
                                    <h3 class="text-xl font-bold text-gray-800">#${c.orderID}</h3>
                                </div>
                                <span class="status-badge px-2 py-1 rounded text-xs font-semibold
                                      ${c.orderStatus == 'Delivered' ? 'bg-green-100 text-green-800' : 
                                        c.orderStatus == 'Processing' ? 'bg-orange-100 text-orange-800' : 
                                        c.orderStatus == 'Cancelled' ?'bg-red-100 text-red-800' : 'bg-gray-100 text-gray-800'}">
                                          ${c.orderStatus}
                                      </span>
                                </div>

                                <div class="flex space-x-4">
                                    <!-- Khu vực bên trái -->
                                    <div class="w-1/2 space-y-3 text-sm">
                                        <div class="flex items-center">
                                            <i class="fas fa-calendar-day text-gray-400 mr-2 w-5"></i>
                                            <span class="font-medium text-gray-700">
                                                <fmt:formatDate value="${c.orderDate}" pattern="dd/MM/yyyy"/> - 
                                                <fmt:formatDate value="${c.expectedDeliveryDate}" pattern="dd/MM/yyyy"/>
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

                                    <!-- Khu vực bên phải: Danh sách sản phẩm -->
                                    <div class="w-1/2 space-y-3 text-sm">
                                       
                                        <c:set var="count" value="0"/>
                                        <c:forEach var="item" items="${c.orderProductList}">
                                            <c:set var="count" value="${count + 1}"/>
                                        </c:forEach>
                                         <span class="truncate w-3/4 font-medium"> List item (${count})  </span>
                                        <c:set var="count" value="0"/>
                                        <c:forEach var="item" items="${c.orderProductList}">
                                            <c:if test="${count < 2}">
                                                <div class="flex items-center">
                                                    <span class="text-gray-600 truncate w-3/4" title="${item.product.productName}">
                                                        # ${item.product.productName} (${item.quantity})
                                                    </span>
                                                </div>
                                            </c:if>
                                            <c:set var="count" value="${count + 1}"/>
                                        </c:forEach>

                                        <c:if test="${count > 2}">
                                            <button onclick="toggleProductList(${c.orderID})" 
                                                    class="text-blue-600 hover:underline text-sm">
                                                ...
                                            </button>
                                        </c:if>
                                    </div>
                                </div>

                                <div class="mt-6 pt-4 border-t border-gray-100 flex justify-between items-center">
                                    <div>
                                        <p class="text-xs text-gray-500">Total amount</p>
                                        <p class="text-xl font-bold text-gray-800">
                                            <fmt:formatNumber value="${c.preVoucherAmount}" type="number" groupingUsed="true"/> đ
                                        </p>
                                    </div>
                                    <a href="OrderDetailController?id=${id}" 
                                       class="px-4 py-2 bg-green-600 hover:bg-green-700 text-white rounded-lg transition duration-200 flex items-center">
                                        <i class="fas fa-eye mr-2"></i> Details
                                    </a>
                                </div>
                            </div>
                        </div>

                        <!-- Danh sách sản phẩm đầy đủ -->
                        <div id="product-list-${c.orderID}" class="fixed inset-0 bg-black bg-opacity-50 hidden flex items-center justify-center z-50">
                            <div class="bg-white p-6 rounded-lg shadow-lg w-96">
                                <h3 class="text-lg font-bold mb-4">Product List</h3>
                                <c:forEach var="item" items="${c.orderProductList}">
                                    <p class="text-gray-700"># ${item.product.productName} (${item.quantity})</p>
                                    <hr class="my-2 border-gray-200">
                                </c:forEach>
                                <button onclick="toggleProductList(${c.orderID})" 
                                        class="mt-4 px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700">
                                    Close
                                </button>
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
            <script>
                let tooltipTimeout;

                function showTooltip(element) {
                    tooltipTimeout = setTimeout(() => {
                        const tooltip = element.nextElementSibling; // Tooltip kế bên
                        tooltip.style.display = "block";
                        tooltip.style.opacity = "1";
                    }, 3000); // Hiển thị sau 3 giây
                }

                function hideTooltip(element) {
                    clearTimeout(tooltipTimeout); // Hủy timeout nếu lướt ra trước 3s
                    const tooltip = element.nextElementSibling;
                    tooltip.style.opacity = "0";
                    setTimeout(() => tooltip.style.display = "none", 300); // Ẩn sau khi mờ dần
                }

                document.addEventListener("DOMContentLoaded", function () {
                    // Lấy tất cả các liên kết
                    const links = document.querySelectorAll('.container a');

                    // Lặp qua từng liên kết và thêm sự kiện click
                    links.forEach(link => {
                        link.addEventListener('click', function () {
                            // Loại bỏ class 'active' khỏi tất cả các liên kết
                            links.forEach(l => l.classList.remove('active'));

                            // Thêm class 'active' vào liên kết hiện tại
                            link.classList.add('active');
                        });
                    });
                });


            </script>
            <script>
                function toggleProductList(orderID) {
                    let productList = document.getElementById('product-list-' + orderID);
                    productList.classList.toggle('hidden');
                }
            </script>
        </body>
        <div>
            <jsp:include page="footer.jsp" flush="true"/> 

        </div>

    </html>