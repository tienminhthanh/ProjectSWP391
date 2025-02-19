<%@ page contentType="text/html; charset=UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<header>
    <div class="logo">
        <a href="home">
            <img src="img/logo.png" alt="WIBOOKS" />
        </a>
    </div>
    <div class="top-bar">
        <div class="search-nav-container">
            <div class="search-bar">
                <input type="text" placeholder="Search for products..." aria-label="Search">
                <button type="submit"><i class="fa-solid fa-magnifying-glass"></i></button>
            </div>
            <nav>
                <ul>
                    <li><a href="#">All Products</a></li>
                    <li><a href="#">Books</a></li>
                    <li><a href="#">Merch</a></li>
                </ul>
            </nav>
        </div>

        <c:if test="${sessionScope.account != null && sessionScope.account.getRole() == 'customer'}">
            <div class="customer-icons">
                <i class="fa-regular fa-comment"></i>
                <i class="fa-regular fa-bell"></i>
                <i class="fa-solid fa-cart-shopping"></i>

                <div class="account-wrapper">
                    <i class="fa-regular fa-user" onclick="toggleAccountMenu()"></i>
                    <div class="account-menu" id="accountMenu">
                        <a href="readAccount">
                            <button>My Account</button>
                        </a>
                        <a href="logout">
                            <button>Sign out</button>
                        </a>
                    </div>
                </div>
            </div>
        </c:if>

        <c:if test="${empty sessionScope.account}">
            <div class="auth-buttons">
                <a href="login">
                    <button class="sign-in"><i class="fa-solid fa-right-to-bracket"></i> Sign in</button>
                </a>
                <a href="register">
                    <button class="sign-up">Sign up</button>
                </a>
            </div>
        </c:if>
    </div>
</header>
