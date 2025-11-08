<%@ page contentType="text/html; charset=UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<header>
    <div class="logo">
        <a href="home">
            <img src="img/logo.png" alt="WIBOOKS" />
        </a>
    </div>
    <div class="top-bar">

        <!--Mobile menu toggle sidebar-->
        <div class="toggle-menu" id="toggle-menu-id">
            <button type="button" onclick="openMobileMenu()">
                <i class="fa-solid fa-bars"></i>
            </button> 
            <p>Menu</p>
        </div>

        <!--Search-->
        <div class="search-nav-container">
            <div class="search-bar">
                <form action="search" method="get">
                    <select name="type" id="searchType">
                        <option value="book">Books</option>
                        <option value="merch">Merch</option>
                    </select>
                    <input type="text" placeholder="Search for products..." aria-label="Search" name="query" value="${requestScope.query}">
                    <button type="submit"><i class="fa-solid fa-magnifying-glass"></i></button>
                </form>
            </div>

            <nav> 
                <ul>
                    <c:forEach var="cat" items="${applicationScope.categories.keySet()}">
                        <li><a href="category?id=${cat.categoryID}">${cat.categoryName}</a></li>
                        </c:forEach>
                </ul>
            </nav>
        </div>

        <c:if test="${not empty sessionScope.account && sessionScope.account.getRole() == 'customer'}">
            <div class="customer-icons">
                <!--Notification button with unread count-->
                <a href="notification?action=list&receiverID=${sessionScope.account.accountID}" class="relative">
                    <i class="fa-regular fa-bell"></i>
                    <c:set var="unreadCount" value="0" />
                    <c:forEach var="notification" items="${sessionScope.notifications}">
                        <c:if test="${!notification.isRead()}">
                            <c:set var="unreadCount" value="${unreadCount + 1}" />
                        </c:if>
                    </c:forEach>
                    <c:if test="${unreadCount > 0}">
                        <span class="absolute -top-2 -right-2 bg-red-500 text-white text-xs rounded-full h-5 w-5 flex items-center justify-center">${unreadCount}</span>
                    </c:if>
                </a>

                <!--Cart button with unique item count-->
                <a href="viewCart?customerID=${sessionScope.account.accountID}" class="relative">
                    <i class="fa-solid fa-cart-shopping"></i>
                    <c:if test="${not empty sessionScope.cartItems}">
                        <span class="absolute -top-2 -right-2 bg-red-500 text-white text-xs rounded-full h-5 w-5 flex items-center justify-center">${fn:length(sessionScope.cartItems)}</span>
                    </c:if>
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
            <div class="overlay" id="cus-menu-overlay"></div>
            <div class="toggle-customer-icons-mobile">
                <button type="button" onclick="openCustomerMenu()">
                    <img src="img/header_icon_mobile/customerMenuIcon.png" alt="Customer Icons"/>
                </button>
            </div>

            <!--Customer mobile menu-->
            <div id="customer-menu-mobile" class="bg-yellow-500 p-4">
                <div class="close-icon text-white top-5 right-5">
                    <button type="button" onclick="closeCustomerMenu()">
                        <i class="fa-solid fa-xmark"></i>
                    </button>
                </div>
                <div class="mt-4 mb-4 text-white text-md">
                    <ul>
                        <li class="border-b-2 border-white p-4 rounded-b-lg">
                            <!-- Notification -->
                            <a class="flex items-center gap-3" href="notification?action=list&receiverID=${sessionScope.account.accountID}">
                                <div class="w-6 h-6 flex relative items-center justify-center">
                                    <i class="fa-regular fa-bell text-lg"></i>
                                    <c:set var="unreadCount" value="0" />
                                    <c:forEach var="notification" items="${sessionScope.notifications}">
                                        <c:if test="${!notification.isRead()}">
                                            <c:set var="unreadCount" value="${unreadCount + 1}" />
                                        </c:if>
                                    </c:forEach>
                                    <c:if test="${unreadCount > 0}">
                                        <span class="absolute top-0 right-0 transform translate-x-1/2 -translate-y-1/2
                                              bg-red-500 text-white text-xs font-bold rounded-full h-5 w-5
                                              flex items-center justify-center">${unreadCount}</span>
                                    </c:if>
                                </div>
                                <span>Notification</span>

                            </a>
                        </li>

                        <li class="border-b-2 border-white p-4 rounded-b-lg">
                            <!-- Cart button -->
                            <a href="cart?customerID=${sessionScope.account.accountID}" class="relative flex items-center gap-3">
                                <div class="relative w-6 h-6 flex items-center justify-center">
                                    <i class="fa-solid fa-cart-shopping text-lg"></i>
                                    <c:if test="${not empty sessionScope.cartItems and fn:length(sessionScope.cartItems) > 0}">
                                        <span class="absolute top-0 right-0 transform translate-x-1/2 -translate-y-1/2
                                              bg-red-500 text-white text-xs font-bold rounded-full h-5 w-5
                                              flex items-center justify-center">
                                            ${fn:length(sessionScope.cartItems)}
                                        </span>
                                    </c:if>
                                </div>
                                <span>Cart</span>

                            </a>
                        </li>

                        <li class="border-b-2 border-white p-4 rounded-b-lg">
                            <!-- My Account -->
                            <a class="flex items-center gap-3" href="readAccount">
                                <div class="w-6 h-6 flex items-center justify-center">
                                    <i class="fa-regular fa-user text-lg"></i>
                                </div>
                                <span>My Account</span>
                            </a>
                        </li>

                        <li class="border-b-2 border-white p-4 rounded-b-lg">
                            <!-- Logout -->
                            <a class="flex items-center gap-3" href="logout">
                                <div class="w-6 h-6 flex items-center justify-center">
                                    <i class="fa-solid fa-arrow-right-from-bracket text-lg"></i>
                                </div>
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

<script>
    window.addEventListener("load", function () {
        var type = "<%= request.getAttribute("type") != null ? request.getAttribute("type") : "book"%>";
        console.log("DEBUG: type from JSP:", type);

        var selectElement = document.getElementById("searchType");
        if (selectElement) {
            selectElement.value = type;
        }

        // Fetch notifications if not already in session
    <c:if test="${empty sessionScope.notifications && not empty sessionScope.account}">
        fetch('notification?action=list&receiverID=${sessionScope.account.accountID}')
                .then(response => response.text())
                .then(data => {
                    // Assuming the JSP sets sessionScope.notifications
                    console.log("Notifications fetched");
                });
    </c:if>
    });
</script>

