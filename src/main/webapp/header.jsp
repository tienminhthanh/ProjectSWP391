<%@ page contentType="text/html; charset=UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
                <!--style this shit properly man, it's killing me-->
                <form action="search" method="get">
                    <select name="type" id="searchType">
                        <option value="book">Books</option>
                        <option value="merch">Merch</option>
                    </select>
                    <input type="text" placeholder="Search for products..." aria-label="Search" name="query" value="${requestScope.query}">
                    <button type="submit"><i class="fa-solid fa-magnifying-glass"></i></button>
                </form>
            </div>

            <!--Hide this shit on mobile view bro -->
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

<script>
    window.addEventListener("load", function () {
        var type = "<%= request.getAttribute("type") != null ? request.getAttribute("type") : "book"%>";
        console.log("DEBUG: type from JSP:", type);

        var selectElement = document.getElementById("searchType");
        if (selectElement) {
            selectElement.value = type;
        }
    });
</script>
