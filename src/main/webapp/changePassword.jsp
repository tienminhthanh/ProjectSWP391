<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
        <title>Change Password</title>
        <script src="https://cdn.tailwindcss.com"></script>
    </head>
    <body class="bg-gray-100">

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
        <div class="max-w-3xl mx-auto p-6">
            <div class="w-full bg-white p-8 shadow-md rounded-lg">
                <h1 class="text-3xl font-bold text-blue-600 mb-6 text-center">Change Password</h1>

                <form action="changePassword" method="post">

                    <!-- Current Password -->
                    <div class="mb-4">
                        <label for="currentPassword" class="block text-gray-700 font-semibold">Current Password</label>
                        <input type="password" name="currentPassword" id="currentPassword" class="w-full p-3 border border-gray-300 rounded" required placeholder="Enter your current password">
                    </div>

                    <c:if test="${not empty errorMessage}">
                        <p class="text-red-600 text-center mt-4">${errorMessage}</p>
                    </c:if>

                    <!-- Submit Button -->
                    <div class="text-center">
                        <button type="submit" class="bg-blue-600 text-white py-2 px-4 rounded-full w-full hover:bg-blue-700 transition duration-200">
                            Change Password
                        </button>
                    </div>

                </form>
            </div>
        </div>

       
    </body>
</html>
