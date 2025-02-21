<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="styles.css"> <!-- Link to your CSS file -->
        <title>Search Bar Example</title>
        <style>
            body {
                margin: 0;
                font-family: Arial, sans-serif;
            }

            .logo {
                display: flex;
                justify-content: center;
                margin: 10px 0;
            }

            .logo img {
                max-width: 200px;
                height: auto;
            }

            .top-bar {
                display: flex;
                justify-content: space-between;
                align-items: center;
                background-color: #87CEEB;
                padding: 10px;
                flex-wrap: wrap;
            }

            .search-nav-container {
                display: flex;
                align-items: center;
                gap: 15px;
            }

            .search-bar {
                display: flex;
                align-items: center;
            }

            .search-bar input {
                padding: 10px;
                border: none;
                border-radius: 5px;
                width: 250px;
            }

            .search-bar button {
                background-color: #00BFFF;
                border: none;
                color: white;
                padding: 10px 15px;
                border-radius: 5px;
                cursor: pointer;
            }

            .search-bar button i {
                font-size: 18px; /* Larger icon size */
                color: white;
            }

            nav ul {
                display: flex;
                list-style-type: none;
                gap: 15px;
                margin: 0;
                padding: 0;
            }

            nav ul li a {
                text-decoration: none;
                color: white;
            }

            nav ul li a:hover {
                color: #f0f0f0;
            }


            .auth-buttons,
            .customer-icons {
                display: flex;
                gap: 10px;
                align-items: center;
            }

            .customer-icons {
                display: flex;
                justify-content: space-around;
                width: 200px;
            }
            .customer-icons i {
                font-size: 22px;
                color: white;
                cursor: pointer;
            }

            .auth-buttons button {
                display: flex;
                align-items: center;
                gap: 5px;
                background-color: #00BFFF;
                color: white;
                border: none;
                padding: 8px 12px;
                border-radius: 5px;
                cursor: pointer;
            }

            .account-wrapper {
                position: relative;
            }

            .account-menu {
                display: none;
                flex-direction: column;
                position: absolute;
                background-color: white;
                border: 1px solid #ccc;
                width: 160px;
                right: 10px;
                z-index: 10;
            }

            .account-menu button {
                width: 100%;
                border: none;
                background: none;
                text-align: left;
                padding: 8px 10px;
                cursor: pointer;
            }

            .account-menu button:hover {
                background-color: #f0f0f0;
            }

            .customer-icons i {
                font-size: 20px;
                color: white;
                cursor: pointer;
            }

            @media (max-width: 768px) {
                .logo img {
                    max-width: 140px;
                }

                .top-bar {
                    flex-direction: column;
                    align-items: center;
                }

                .search-nav-container {
                    flex-direction: column;
                    align-items: center;
                    width: 100%;
                    margin-bottom: 10px;
                }

                .search-bar {
                    display: flex;
                    width: 100%;
                    max-width: 400px;
                }

                .search-bar input {
                    flex: 1;
                    width: 100%;
                    padding: 10px;
                    border: 1px solid #ccc;
                    border-radius: 5px 0 0 5px;
                }

                .search-bar button {
                    padding: 10px 15px;
                    border-radius: 0 5px 5px 0;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                }

                nav ul {
                    flex-direction: column;
                    gap: 10px;
                    text-align: center;
                }

                .customer-icons {
                    position: fixed;
                    bottom: 0;
                    left: 0;
                    width: 100%;
                    background-color: #87CEEB;
                    padding: 10px 0;
                    justify-content: space-around;
                }

                .auth-buttons {
                    margin-top: 10px;
                }
                /* New styles for account menu positioning in mobile view */
                .account-wrapper {
                    position: relative;
                }

                .account-menu {
                    display: none;
                    flex-direction: column;
                    position: absolute;
                    background-color: white;
                    border: 1px solid #ccc;
                    width: 160px;
                    right: 10px;
                    left: -300%;
                    z-index: 10;
                    top: -300%; /* Position it above the icon */
                }

            }
        </style>
    </head>

    <body>
        <div class="logo">
            <img src="img/logo.png" alt="WIBOOKS" />
        </div>
        <header>
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

                <div class="customer-icons">
                    <i class="fa-regular fa-comment"></i>
                    <i class="fa-regular fa-bell"></i>
                    <i class="fa-solid fa-cart-shopping"></i>

                    <div class="account-wrapper">
                        <i class="fa-regular fa-user" onclick="toggleAccountMenu()"></i>
                        <div class="account-menu" id="accountMenu">
                            <button>My Account</button>
                            <button>Sign out</button>
                        </div>
                    </div>
                </div>
                <c:if test="${not empty sessionScope.customerAccount}">
                </c:if>

                <c:if test="${empty sessionScope.customerAccount}">
                    <!--                <div class="auth-buttons">
                                        <button class="sign-in"><i class="fa-solid fa-right-to-bracket"></i> Sign in</button>
                                        <button class="sign-up">Sign up</button>
                                    </div>-->
                </c:if>
            </div>
        </header>

        <script src="https://kit.fontawesome.com/bfab6e6450.js" crossorigin="anonymous"></script>
        <script>
                                function toggleAccountMenu() {
                                    const menu = document.getElementById('accountMenu');
                                    menu.style.display = (menu.style.display === 'none' || menu.style.display === '') ? 'flex' : 'none';
                                }
        </script>
    </body>

</html>
