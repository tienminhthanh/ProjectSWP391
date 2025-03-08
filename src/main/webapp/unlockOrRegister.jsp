<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Account Locked</title>
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

        <!-- Main Content -->
        <div class="min-h-screen flex items-center justify-center bg-gray-50">

            <div class="w-full max-w-lg p-8 bg-white rounded-lg shadow-xl">

                <!-- Title -->
                <h2 class="text-3xl font-semibold text-center text-gray-800 mb-6">Your Account is Locked</h2>

                <!-- Message Section -->
                <p class="text-lg text-center text-gray-600 mb-8">${message}</p>

                <!-- Action Buttons -->
                <div class="flex flex-col gap-6">

                    <!-- Unlock Account Button -->
                    <a href="emailUnlock" class="bg-blue-600 text-white py-3 px-6 rounded-lg text-center text-xl hover:bg-blue-700 transition duration-300 ease-in-out shadow-md">
                        Unlock Account (Verify Email)
                    </a>

                    <!-- Remove Email Button -->
                    <a href="removeEmailFromLockedAccount" class="bg-red-600 text-white py-3 px-6 rounded-lg text-center text-xl hover:bg-red-700 transition duration-300 ease-in-out shadow-md">
                        Remove Email from Account
                    </a>

                </div>

                <!-- Footer -->
                <div class="text-center mt-8">
                    <p class="text-sm text-gray-500">&copy; 2025 WIBOOKS Co., Ltd. All Rights Reserved.</p>
                </div>
            </div>
        </div>

    </body>
</html>
