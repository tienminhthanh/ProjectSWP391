<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Forgot Password</title>
        <script src="https://cdn.tailwindcss.com"></script>
    </head>

    <body class="bg-gray-100 font-sans leading-normal tracking-wider">

        <!-- Header -->
        <!-- Header -->
        <header class="bg-white shadow w-full mb-4">
            <div class="container mx-auto px-4 py-2 flex justify-between items-center">
                <a href="home.jsp">
                    <img alt="WIBOOKS Logo" class="h-12" src="./img/logoWibooks-removebg-preview.png" />
                </a>
                <div class="flex items-center space-x-4">
                    <a href="login.jsp" class="text-orange-600 font-semibold hover:underline">Login</a>
                    <a href="home.jsp" class="text-orange-600 font-semibold hover:underline">Home</a>
                </div>
            </div>
        </header>

        <!-- Centered Form Content -->
        <div class="min-h-screen flex items-center justify-center bg-gray-50">

            <div class="w-full max-w-md p-8 bg-white rounded-lg shadow-md">
                <!-- Title -->
                <h2 class="text-3xl font-semibold text-center text-gray-800 mb-6">Forgot Password</h2>

                <!-- Forgot Password Form -->
                <form action="forgotPassword" method="post">
                    <div class="mb-6">
                        <label for="email" class="block text-gray-700 text-lg mb-2">Enter your email:</label>
                        <input type="email" id="email" name="email" class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-600" required>
                    </div>
                    <button type="submit" class="w-full bg-blue-600 text-white py-2 rounded-lg text-lg hover:bg-blue-700 transition duration-300 ease-in-out">
                        Submit
                    </button>
                </form>

                <!-- Error/Message Display -->
                <c:if test="${not empty errorMessage}">
                    <p class="text-red-600 text-center mt-4">${errorMessage}</p>
                </c:if>
                <c:if test="${not empty message}">
                    <p class="text-green-600 text-center mt-4">${message}</p>
                </c:if>

            </div>

        </div>

        <!-- Footer -->
        <footer class="bg-gray-200 py-4 w-full">
            <div class="container mx-auto px-4 text-center text-sm text-gray-600">
                <p>&copy; 2025 WIBOOKS Co.,Ltd. All rights reserved.</p>
            </div>
        </footer>

    </body>

</html>