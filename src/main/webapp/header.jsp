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

            <!--Hide this shit on mobile view bro-->
            <nav> 
                <ul>
                    <li><a href="#">Manga</a></li>
                    <li><a href="#">Light Novel</a></li>
                    <li><a href="#">Figure</a></li>
                    <li><a href="#">Nendoroid</a></li>
                </ul>
            </nav>
        </div>

        <c:if test="${not empty sessionScope.account && sessionScope.account.getRole() == 'customer'}">
            <div class="customer-icons">
                <i class="fa-regular fa-bell"></i>
                <a href="cart">
                    <i class="fa-solid fa-cart-shopping"></i>
                </a>

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

                <a href="login" id="loginLink">
                    <button class="sign-in"><i class="fa-solid fa-right-to-bracket"></i> Sign in</button>
                </a>
                <a href="register">
                    <button class="sign-up">Sign up</button>
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