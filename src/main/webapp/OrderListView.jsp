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


        </style>
    </head>


    <body class="bg-gray-50">
        <header>
            <div class="logo">
                <a href="home">
                    <img src="img/logo.png" alt="WIBOOKS" />
                </a>
            </div>
            <div class="top-bar">
                <div class="search-nav-container">
                    <h1 class="text-4xl font-bold text-transparent bg-clip-text bg-gradient-to-r from-orange-600 to-green-500
                        mb-2 relative inline-block ml-12"
                        style="-webkit-text-stroke: 1px white;">
                        Order List
                    </h1>
                </div>

                <c:if test="${not empty sessionScope.account && sessionScope.account.getRole() == 'customer'}">
                    <div class="customer-icons">


                        <!--Notification button-->
                        <a href="notification?action=list&receiverID=${sessionScope.account.accountID}">
                            <i class="fa-regular fa-bell"></i>
                        </a>

                        <!--Cart button-->
                        <a href="cart?customerID=${sessionScope.account.accountID}">
                            <i class="fa-solid fa-cart-shopping"></i>
                        </a>

                        <!--My Account-->
                        <a href="readAccount">
                            <i class="fa-regular fa-user"></i>
                        </a>

                        <!--Logout-->
                        <a href="logout">
                            <i class="fa-solid fa-arrow-right-from-bracket"></i>
                        </a>

                    </div>

                    <!--Toggle customer mobile menu-->
                    <div class="overlay" id="cus-menu-overlay" onclick="closeCustomerMenu()"></div>
                    <div class="toggle-customer-icons-mobile">
                        <button type="button" onclick="openCustomerMenu()">
                            <img src="img/header_icon_mobile/customerMenuIcon.png" alt="Customer Icons"/>
                        </button>
                    </div>

                    <!--Customer mobile menu-->
                    <div id="customer-menu-mobile" class="p-3 bg-gray-200">
                        <div class="close-icon">
                            <button type="button" onclick="closeCustomerMenu()">
                                <i class="fa-solid fa-xmark"></i>
                            </button>
                        </div>
                        <div class="mt-4 mb-4">
                            <ul class="list-disc list-inside">
                                <li>
                                    <!--Notification button-->
                                    <a href="notification.jsp">
                                        <i class="fa-regular fa-bell"></i>
                                        <span>Notification</span>
                                    </a>

                                </li>
                                <li>
                                    <!--Cart button-->
                                    <a href="cart?customerID=${sessionScope.account.accountID}">
                                        <i class="fa-solid fa-cart-shopping"></i>
                                        <span>Cart</span>
                                    </a>

                                </li>
                                <li>
                                    <!--My Account-->
                                    <a href="readAccount">
                                        <i class="fa-regular fa-user"></i>
                                        <span>My Account</span>
                                    </a>
                                </li>
                                <li>
                                    <!--Logout-->
                                    <a href="logout">
                                        <i class="fa-solid fa-arrow-right-from-bracket"></i>
                                        <span>Sign out</span>
                                    </a>
                                </li>
                            </ul>
                        </div>

                    </div>

                </c:if>

                <c:if test="${empty sessionScope.account}">
                    <div class="auth-buttons">

                        <a href="login" class="loginLinks">
                            <button class="sign-in"><i class="fa-solid fa-right-to-bracket"></i> Sign in</button>
                        </a>
                        <a href="register">
                            <button class="sign-up">Sign up</button>
                        </a>
                    </div>

                    <div class="auth-icon-mobile">
                        <a href="login" class="loginLinks">
                            <i class="fa-regular fa-user"></i>
                            <p>Sign in</p>
                        </a>
                    </div>
                </c:if>
            </div>
        </header>
        <nav class="bg-white shadow-md">
            <div class="container mx-auto px-4 py-2 flex justify-around">
                <a href="OrderListController?status=#" class="text-gray-700 hover:text-orange-600 font-semibold ${param.status == '#' ? 'active' : ''}">Payment Due</a>
                <a href="OrderListController?status=pending" class="text-gray-700 hover:text-orange-600 font-semibold ${param.status == 'pending' ? 'active' : ''}">Pending</a>
                <a href="OrderListController?status=Shipped" class="text-gray-700 hover:text-orange-600 font-semibold ${param.status == 'Shipped' ? 'active' : ''}">Shipping</a>
                <a href="OrderListController?status=completed" class="text-gray-700 hover:text-orange-600 font-semibold ${param.status == 'completed' ? 'active' : ''}">History</a>
                <a href="OrderListController?status=canceled" class="text-gray-700 hover:text-orange-600 font-semibold ${param.status == 'canceled' ? 'active' : ''}">Canceled</a>
            </div>
        </nav>
        <main class="container mx-auto px-4 py-8">
            <div class="mb-8 text-center">

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

                        <div class="mt-6 pt-4 border-t border-gray-100 flex justify-between items-center">
                            <div>
                                <p class="text-xs text-gray-500">Total amount</p>
                                <p class="text-xl font-bold text-gray-800">
                                                                    <fmt:formatNumber value="${c.preVoucherAmount}" type="number" groupingUsed="true"/> đ
                                </p>
                            </div>
                            <div class="relative inline-block">
                                <!-- Nút Details -->
                                <a href="OrderDetailController?id=${id}" 
                                   class="flex items-center px-4 py-2 bg-green-600 hover:bg-green-700 text-white rounded-lg transition-colors duration-200"
                                   onmouseover="showTooltip(this)" 
                                   onmouseleave="hideTooltip(this)">
                                    <i class="fas fa-eye mr-2"></i>
                                    Details
                                </a>

                                <!-- Tooltip hiển thị thông tin -->
                                <div class="absolute left-0 mt-2 w-48 p-2 bg-gray-800 text-white text-sm rounded shadow-lg opacity-0 transition-opacity duration-300"
                                     style="display: none;">
                                    Đơn hàng #${id}: Đang xử lý...
                                </div>
                            </div>
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

    </body>

</html>