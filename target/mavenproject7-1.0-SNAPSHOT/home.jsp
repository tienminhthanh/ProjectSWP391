<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
        <title>Home Page</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"/>
    </head>
    <body class="bg-gray-100 min-h-screen flex flex-col">
        <header class="bg-white shadow w-full">
            <div class="container mx-auto px-4 py-2 flex justify-between items-center">
                <img alt="Book Walker Logo" class="h-10" height="50" src="https://storage.googleapis.com/a1aa/image/eqONjY2PAhJPB-SS1k-WJ6Cn3CmR-ITt6O9vKa2fKhk.jpg" width="150"/>
                <div class="flex items-center space-x-4">
                    <a class="text-blue-600 hover:underline" href="login.jsp">Login</a>
                    <a class="text-blue-600 hover:underline" href="register.jsp">Register</a>
                </div>
            </div>
        </header>
        <main class="flex-grow flex items-center justify-center">
            <div class="w-full max-w-4xl bg-white p-8 shadow-md text-center">
                <h1 class="text-2xl font-semibold mb-4">
                    Welcome <%= session.getAttribute("account") != null ? ((model.Account) session.getAttribute("account")).getUsername() : "Guest" %>
                </h1>
                <hr class="mb-6"/>
                <% if(session.getAttribute("account") != null) { %>
                    <div class="space-y-4">
                        <a class="bg-blue-600 text-white p-3 rounded hover:bg-blue-700 flex items-center justify-center" href="readAccount">
                            <i class="fas fa-user mr-2"></i> View Account Info
                        </a>
                        <a class="bg-red-600 text-white p-3 rounded hover:bg-red-700 flex items-center justify-center" href="logout">
                            <i class="fas fa-sign-out-alt mr-2"></i> Logout
                        </a>
                        <form action="emailAuthentication" method="post" class="mt-4">
                            <button class="bg-yellow-500 text-white p-3 rounded hover:bg-yellow-600 w-full flex items-center justify-center" type="submit">
                                <i class="fas fa-envelope mr-2"></i> Verify Email
                            </button>
                        </form>
                        <form action="emailAuthentication" method="post" class="mt-4">
                            <label class="block text-left mb-2" for="otp">Enter the OTP sent to your email:</label>
                            <input class="w-full p-3 border border-gray-300 rounded mb-4" id="otp" name="otp" required type="text"/>
                            <button class="bg-green-600 text-white p-3 rounded hover:bg-green-700 w-full flex items-center justify-center" type="submit">
                                <i class="fas fa-check mr-2"></i> Verify OTP
                            </button>
                        </form>
                    </div>
                <% } else { %>
                    <p class="text-lg">Please <a class="text-blue-600 hover:underline" href="login.jsp">Login</a> to access your account.</p>
                <% } %>
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