<html lang="en">
    <head>
        <meta charset="utf-8"/>
        <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
        <title>Login</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"/>
    </head>
    <body class="bg-gray-100 min-h-screen flex flex-col">
        <header class="bg-white shadow w-full">
            <div class="container mx-auto px-4 py-2 flex justify-between items-center">
                <img alt="WIBOOKS Logo" class="h-10" height="50" src="./img/logoWibooks-removebg-preview.png" width="200"/>
                <div class="flex items-center space-x-4">
                <i class="fas fa-globe text-xl"></i>
                </div>
            </div>
        </header>
        <main class="flex-grow flex items-center justify-center">
            <div class="w-full max-w-4xl bg-white p-8 shadow-md">
                <h1 class="text-2xl font-semibold mb-4">Sign-in</h1>
                <hr class="mb-6"/>
                <div class="flex flex-col md:flex-row justify-between">
                    <div class="w-full md:w-1/2 md:pr-4 mb-6 md:mb-0">
                        <h2 class="text-lg font-semibold mb-4">Sign-in with your username</h2>
                        <form action="login" method="post">
                            <div class="mb-4">
                                <label class="sr-only" for="username">Username</label>
                                <input class="w-full p-3 border border-gray-300 rounded" id="username" name="username" placeholder="Username" required type="text"/>
                            </div>
                            <div class="mb-4">
                                <label class="sr-only" for="password">Password</label>
                                <input class="w-full p-3 border border-gray-300 rounded" id="password" name="password" placeholder="Password" required type="password"/>
                            </div>
                            <button class="w-full bg-blue-600 text-white p-3 rounded hover:bg-blue-700" type="submit">Sign-in</button>
                            <c:if test="${not empty errorMessage}">
                                <p class="text-red-600 text-center mt-4">${errorMessage}</p>
                            </c:if>
                            <div class="mt-4 text-right">
                                <a class="text-blue-600 text-sm" href="forgotPassword.jsp">Forgot your password?</a>
                            </div>
                        </form>
                    </div>
                    <div class="w-full md:w-1/2 md:pl-4">
                        <h2 class="text-lg font-semibold mb-4">Sign-in with below accounts</h2>
                        <div class="space-y-4">
                            <button class="w-full bg-red-600 text-white p-3 rounded flex items-center justify-center hover:bg-red-700">
                                <i class="fab fa-google mr-2"></i> Login with Google
                            </button>
                            <button class="w-full bg-black text-white p-3 rounded flex items-center justify-center hover:bg-gray-800">
                                <i class="fab fa-apple mr-2"></i> Sign in with Apple
                            </button>
                            <button class="w-full bg-black text-white p-3 rounded flex items-center justify-center hover:bg-gray-800">
                                <img alt="Niconico Logo" class="mr-2" height="20" src="https://storage.googleapis.com/a1aa/image/Fa8wNYak4irMw53nH9r4AMpIqKZO7Ew2TEyJMJ9vVsU.jpg" width="20"/> Login with niconico
                            </button>
                            <button class="w-full bg-black text-white p-3 rounded flex items-center justify-center hover:bg-gray-800">
                                <i class="fab fa-twitter mr-2"></i> Login with X (formerly Twitter)
                            </button>
                            <button class="w-full bg-blue-600 text-white p-3 rounded flex items-center justify-center hover:bg-blue-700">
                                <i class="fab fa-facebook-f mr-2"></i> Login with Facebook
                            </button>
                        </div>
                    </div>
                </div>
                <div class="mt-8 p-4 border border-orange-500 bg-orange-100 text-center">
                    <p class="text-lg font-semibold mb-2">If you don't have an account</p>
                    <a class="bg-orange-500 text-white p-3 rounded hover:bg-orange-600" href="register.jsp">Register with your email address</a>
                </div>
            </div>
        </main>
        <footer class="bg-gray-200 py-4 mt-8 w-full">
            <div class="container mx-auto px-4 text-center text-sm text-gray-600">
                <a class="mr-4" href="#">Privacy</a>
                <a href="#">Purchase Terms &amp; Conditions</a>
                <p class="mt-4">© BOOK WALKER Co.,Ltd.</p>
            </div>
        </footer>

    </body>
</html>