<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
        <title>Register - WIBOOKS</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet"/>
    </head>
    <body class="bg-gray-100 min-h-screen flex flex-col">

        <!-- Header -->
        <header class="bg-white shadow w-full">
            <div class="container mx-auto px-4 py-2 flex justify-between items-center">
                <img alt="WIBOOKS Logo" class="h-12" src="./img/logoWibooks-removebg-preview.png"/>
                <div class="flex items-center space-x-4">
                    <a href="login.jsp" class="text-orange-600 font-semibold hover:underline">Login</a>
                    <a href="home.jsp" class="text-orange-600 font-semibold hover:underline">Home</a>
                </div>
            </div>
        </header>

        <!-- Main Content -->
        <main class="flex-grow flex items-center justify-center">
            <div class="w-full max-w-3xl bg-white p-8 shadow-md rounded-lg">
                <h1 class="text-3xl font-bold text-orange-600 mb-6 text-center">Create Your Account</h1>
                <div class="flex items-center justify-center mb-6">
                    <!-- Step 1: Enter (Active) -->
                    <div class="flex-1 text-center">
                        <div class="w-8 h-8 bg-orange-600 text-white rounded-full inline-flex items-center justify-center">1</div>
                        <p class="text-sm font-bold text-orange-600">Enter</p>
                    </div>

                    <!-- Step 2: Verify Account (Inactive) -->
                    <div class="flex-1 text-center">
                        <div class="w-8 h-8 bg-gray-300 rounded-full inline-flex items-center justify-center">2</div>
                        <p class="text-sm">Verify Account</p>
                    </div>

                    <!-- Step 3: Complete (Inactive) -->
                    <div class="flex-1 text-center">
                        <div class="w-8 h-8 bg-gray-300 rounded-full inline-flex items-center justify-center">3</div>
                        <p class="text-sm">Complete</p>
                    </div>
                </div>

                <form action="register" method="post">
                    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <!-- Username & Birth Date -->
                        <div class="mb-4">
                            <label class="text-gray-700 font-semibold">Username</label>
                            <input class="w-full p-3 border border-gray-300 rounded" id="username" name="username" placeholder="Enter your username" required type="text"/>
                        </div>
                        <div class="mb-4">
                            <label class="text-gray-700 font-semibold">Birth Date</label>
                            <input class="w-full p-3 border border-gray-300 rounded" id="birthDate" name="birthDate" required type="date"/>
                        </div>

                        <!-- Password & Confirm Password -->
                        <div class="mb-4 relative">
                            <label class="text-gray-700 font-semibold">Password</label>
                            <input class="w-full p-3 border border-gray-300 rounded pr-10" id="password" name="password" placeholder="Enter password" required type="password"/>
                            <button type="button" onclick="togglePassword('password', 'toggleIcon1')" class="absolute right-3 top-10 text-gray-500">
                                <i id="toggleIcon1" class="fas fa-eye"></i>
                            </button>
                        </div>
                        <div class="mb-4 relative">
                            <label class="text-gray-700 font-semibold">Confirm Password</label>
                            <input class="w-full p-3 border border-gray-300 rounded pr-10" id="confirmPassword" name="confirmPassword" placeholder="Confirm password" required type="password"/>
                            <button type="button" onclick="togglePassword('confirmPassword', 'toggleIcon2')" class="absolute right-3 top-10 text-gray-500">
                                <i id="toggleIcon2" class="fas fa-eye"></i>
                            </button>
                        </div>

                        <!-- First Name & Last Name -->
                        <div class="mb-4">
                            <label class="text-gray-700 font-semibold">First Name</label>
                            <input class="w-full p-3 border border-gray-300 rounded" id="firstName" name="firstName" placeholder="Your first name" required type="text"/>
                        </div>
                        <div class="mb-4">
                            <label class="text-gray-700 font-semibold">Last Name</label>
                            <input class="w-full p-3 border border-gray-300 rounded" id="lastName" name="lastName" placeholder="Your last name" required type="text"/>
                        </div>

                        <!-- Email & Phone Number -->
                        <div class="mb-4">
                            <label class="text-gray-700 font-semibold">Email</label>
                            <input class="w-full p-3 border border-gray-300 rounded" id="email" name="email" placeholder="Enter your email" required type="email"/>
                        </div>
                        <div class="mb-4">
                            <label class="text-gray-700 font-semibold">Phone Number</label>
                            <input class="w-full p-3 border border-gray-300 rounded" id="phoneNumber" name="phoneNumber" placeholder="Enter your phone number" required type="text"/>
                        </div>
                    </div>

                    <!-- Register Button -->
                    <button class="w-full bg-orange-600 text-white py-3 rounded-lg hover:bg-orange-700 mt-6 transition duration-200" type="submit">
                        Register
                    </button>
                </form>

                <!-- Display Error Message -->
                <c:if test="${not empty message}">
                    <p class="text-red-600 text-center mt-4">${message}</p>
                </c:if>
            </div>
        </main>

        <!-- Footer -->
        <footer class="bg-gray-200 py-4 mt-8 w-full">
            <div class="container mx-auto px-4 text-center text-sm text-gray-600">
                <p>© WIBOOKS Co.,Ltd. | <a href="#" class="text-orange-600 hover:underline">Privacy Policy</a></p>
            </div>
        </footer>

        <!-- JavaScript -->
        <script>
            function togglePassword(inputId, iconId) {
                const input = document.getElementById(inputId);
                const icon = document.getElementById(iconId);
                if (input.type === "password") {
                    input.type = "text";
                    icon.classList.remove("fa-eye");
                    icon.classList.add("fa-eye-slash");
                } else {
                    input.type = "password";
                    icon.classList.remove("fa-eye-slash");
                    icon.classList.add("fa-eye");
                }
            }
        </script>

    </body>
</html>
