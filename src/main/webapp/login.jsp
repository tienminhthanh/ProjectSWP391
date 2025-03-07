<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" errorPage="error.jsp" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html lang="en">
    <head>
        <meta charset="utf-8"/>
        <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
        <title>Login</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"/>
    </head>
    <body class="bg-gray-100 min-h-screen flex flex-col" onload="checkFailedAttempts()">
        <!-- Header -->
        <header class="bg-white shadow w-full">
            <a href="home" class="container mx-auto px-4 py-2 flex justify-between items-center">
                <img alt="WIBOOKS Logo" class="h-10" src="./img/logoWibooks-removebg-preview.png"/>
                <div class="flex items-center space-x-4">
                    <i class="fas fa-globe text-xl"></i>
                </div>
            </a>
        </header>

        <!-- Main Content -->
        <main class="flex-grow flex items-center justify-center">
            <div class="w-full max-w-4xl bg-white p-8 shadow-md">
                <h1 class="text-2xl font-semibold mb-4">Sign-in</h1>
                <hr class="mb-6"/>

                <div class="flex flex-col md:flex-row justify-between">
                    <!-- Login Form -->
                    <div class="w-full md:w-1/2 md:pr-4 mb-6 md:mb-0">
                        <h2 class="text-lg font-semibold mb-4">Sign-in with your username</h2>
                        <form action="login" method="post" onsubmit="return checkLockStatus()">
                            <input type="hidden" name="currentURL" value="${requestScope.currentURL}">

                            <!-- Username -->
                            <div class="mb-4">
                                <label class="sr-only" for="username">Username</label>
                                <input class="w-full p-3 border border-gray-300 rounded"
                                       id="username" name="username"
                                       value="${username}" placeholder="Username" required type="text"/>
                            </div>

                            <!-- Password + Toggle Visibility -->
                            <div class="mb-4 relative">
                                <label class="sr-only" for="password">Password</label>
                                <input class="w-full p-3 border border-gray-300 rounded pr-10"
                                       id="password" name="password"
                                       placeholder="Password" required type="password"/>
                                <button type="button" onclick="togglePassword('password', 'toggleIcon')" 
                                        class="absolute right-3 top-3 text-gray-500">
                                    <i id="toggleIcon" class="fas fa-eye"></i>
                                </button>
                            </div>

                            <!-- Submit Button -->
                            <button class="w-full bg-blue-600 text-white p-3 rounded hover:bg-blue-700" type="submit">
                                Sign-in
                            </button>

                            <!-- Display Error Messages -->
                            <c:if test="${not empty errorMessage}">
                                <script>handleFailedLogin();</script>
                                <p class="text-red-600 text-center mt-4">${errorMessage}</p>
                            </c:if>

                            <c:if test="${not empty message}">
                                <script>resetFailedAttempts();</script>
                                <p class="text-green-600 text-center mt-4">${message}</p>
                            </c:if>
                            <div class="mt-4 text-right">
                                <a href="emailUnlock" class="text-blue-600 text-sm mr-4">
                                    Unlock Account
                                </a>
                                <a class="text-blue-600 text-sm" href="forgotPassword.jsp">Forgot your password?</a>
                            </div>

                        </form>
                    </div>

                    <!-- Social Login -->
                    <div class="w-full md:w-1/2 md:pl-4">
                        <h2 class="text-lg font-semibold mb-4">Sign-in with below accounts</h2>
                        <div class="space-y-4">
                            <a href="https://accounts.google.com/o/oauth2/auth?scope=email profile openid&redirect_uri=http://localhost:8080/loginGoogle&response_type=code&client_id=103840178226-4ev8f05cv55sr4l86jchjtvfd5hvscjb.apps.googleusercontent.com&approval_prompt=force"
                               class="w-full bg-red-600 text-white p-3 rounded flex items-center justify-center hover:bg-red-700">
                                <i class="fab fa-google mr-2"></i> Login with Google
                            </a>

                        </div>
                    </div>
                </div>

                <!-- Register Prompt -->
                <div class="mt-8 p-4 border border-orange-500 bg-orange-100 text-center">
                    <p class="text-lg font-semibold mb-2">If you don't have an account</p>
                    <a class="bg-orange-500 text-white p-3 rounded hover:bg-orange-600" href="register.jsp">Register with your email address</a>
                </div>
            </div>
        </main>

        <!-- Footer -->
        <footer class="bg-gray-200 py-4 mt-8 w-full">
            <div class="container mx-auto px-4 text-center text-sm text-gray-600">
                <a class="mr-4" href="#">Privacy</a>
                <a href="#">Purchase Terms &amp; Conditions</a>
                <p class="mt-4">© WIBOOKS Co.,Ltd.</p>
            </div>
        </footer>

        <!-- JavaScript -->
        <script>
            let failedAttempts = localStorage.getItem("failedAttempts") || 0;

            function checkFailedAttempts() {
                if (failedAttempts >= 5) {
                    alert("Your account has been locked due to multiple failed login attempts.");
                    window.location.href = "deleteAccount?username=" + document.getElementById("username").value;
                }
            }

            function handleFailedLogin() {
                failedAttempts++;
                localStorage.setItem("failedAttempts", failedAttempts);
                if (failedAttempts >= 5) {
                    alert("Your account has been locked.");
                    window.location.href = "deleteAccount?username=" + document.getElementById("username").value;
                }
            }

            function resetFailedAttempts() {
                localStorage.setItem("failedAttempts", 0);
            }

            function togglePassword(inputId, iconId) {
                const input = document.getElementById(inputId);
                const icon = document.getElementById(iconId);
                if (input.type === "password") {
                    input.type = "text";
                    icon.classList.replace("fa-eye", "fa-eye-slash");
                } else {
                    input.type = "password";
                    icon.classList.replace("fa-eye-slash", "fa-eye");
                }
            }

            function checkLockStatus() {
                if (failedAttempts >= 5) {
                    alert("Your account has been locked.");
                    return false;
                }
                return true;
            }
        </script>
    </body>
</html>
