<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
        <title>WIBOOKS - Registration Successful</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet"/>
    </head>
    <body class="bg-gray-100 flex flex-col min-h-screen">

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
        <main class="flex-grow flex items-center justify-center">
            <div class="w-full max-w-3xl bg-white p-8 shadow-md rounded-lg text-center">
                <h1 class="text-3xl font-bold text-green-600 mb-6 text-center">Create Account</h1>
                <h2 class="text-xl font-semibold text-gray-700 mb-4">Verification Completed</h2>

                <!-- Step Progress -->
                <div class="flex items-center justify-center mb-6">
                    <div class="flex-1 text-center">
                        <div class="w-8 h-8 bg-gray-300 rounded-full inline-flex items-center justify-center">1</div>
                        <p class="text-sm">Enter</p>
                    </div>
                    <div class="flex-1 text-center">
                        <div class="w-8 h-8 bg-gray-300 rounded-full inline-flex items-center justify-center">2</div>
                        <p class="text-sm">Verify Account</p>
                    </div>
                    <div class="flex-1 text-center">
                        <div class="w-8 h-8 bg-green-600 text-white rounded-full inline-flex items-center justify-center">3</div>
                        <p class="text-sm font-bold text-green-600">Complete</p>
                    </div>
                </div>

                <!-- Success Message -->
                <i class="fas fa-check-circle text-green-500 text-6xl mb-4 animate-bounce"></i>
                <h1 class="text-2xl font-semibold text-gray-800 mb-4">Congratulations!</h1>
                <p class="text-gray-700 mb-6">
                    Your email has been successfully verified, and your account is now active.
                </p>

                <!-- Buttons -->
                <div class="flex justify-center space-x-4">
                    <a href="home.jsp" class="bg-gray-500 text-white py-2 px-4 rounded-full flex items-center hover:bg-gray-600 transition duration-200">
                        <i class="fas fa-home mr-2"></i> Back to Home
                    </a>
                    <a href="logout" class="bg-blue-600 text-white py-2 px-4 rounded-full flex items-center hover:bg-blue-700 transition duration-200">
                        <i class="fas fa-sign-in-alt mr-2"></i> Logout
                    </a>
                </div>

                <p class="mt-6 text-gray-600 text-sm">
                    If you are not redirected automatically, click one of the buttons above.
                </p>
            </div>
        </main>

        <!-- Footer -->
        <footer class="bg-gray-200 py-4 w-full">
            <div class="container mx-auto px-4 text-center text-sm text-gray-600">
                <p>© WIBOOKS Co.,Ltd.</p>
            </div>
        </footer>

    </body>
</html>
