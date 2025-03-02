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

        <!-- Footer -->
        <footer class="bg-gray-200 py-4 w-full">
            <div class="container mx-auto px-4 text-center text-sm text-gray-600">
                <p>© WIBOOKS Co.,Ltd.</p>
            </div>
        </footer>

    </body>
</html>
