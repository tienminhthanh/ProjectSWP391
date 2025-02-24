<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
    <head>
<<<<<<< HEAD
        <meta charset="utf-8"/>
        <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
        <title>Register</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"/>
    </head>
    <body class="bg-gray-100 min-h-screen flex flex-col">
        <header class="bg-white shadow w-full">
            <div class="container mx-auto px-4 py-2 flex justify-between items-center">
                <img alt="Book Walker Logo" class="h-10" height="50" src="https://storage.googleapis.com/a1aa/image/eqONjY2PAhJPB-SS1k-WJ6Cn3CmR-ITt6O9vKa2fKhk.jpg" width="150"/>
                <div class="flex items-center space-x-4">
                    <i class="fas fa-globe text-xl"></i>
                </div>
            </div>
        </header>
        <main class="flex-grow flex items-center justify-center">
            <div class="w-full max-w-4xl bg-white p-8 shadow-md">
                <h1 class="text-2xl font-semibold mb-4">Register</h1>
                <hr class="mb-6"/>
                <form action="register" method="post">
                    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <!-- Username & Birth Date -->
                        <div class="mb-4">
                            <label class="sr-only" for="username">Username</label>
                            <input class="w-full p-3 border border-gray-300 rounded" id="username" name="username" placeholder="Username" required type="text"/>
                        </div>
                        <div class="mb-4">
                            <label class="sr-only" for="birthDate">Birth Date</label>
                            <input class="w-full p-3 border border-gray-300 rounded" id="birthDate" name="birthDate" required type="date"/>
                        </div>

                        <!-- Password & Confirm Password -->
                        <!-- Password & Confirm Password -->
                        <div class="mb-4 relative w-full">
                            <label class="sr-only" for="password">Password</label>
                            <input class="w-full p-3 border border-gray-300 rounded pr-10" id="password" name="password" placeholder="Password" required type="password"/>
                            <button type="button" onclick="togglePassword()" id="toggleIcon" class="absolute right-3 top-1/2 -translate-y-1/2 text-gray-500 text-lg">👁️</button>
                        </div>

                        <div class="mb-4 relative w-full">
                            <label class="sr-only" for="confirmPassword">Confirm Password</label>
                            <input class="w-full p-3 border border-gray-300 rounded pr-10" id="confirmPassword" name="confirmPassword" placeholder="Confirm Password" required type="password"/>
                            <button type="button" onclick="togglePassword()" id="toggleIcon" class="absolute right-3 top-1/2 -translate-y-1/2 text-gray-500 text-lg">👁️</button>
                        </div>






                        <!-- First Name & Last Name -->
                        <div class="mb-4">
                            <label class="sr-only" for="firstName">First Name</label>
                            <input class="w-full p-3 border border-gray-300 rounded" id="firstName" name="firstName" placeholder="First Name" required type="text"/>
                        </div>
                        <div class="mb-4">
                            <label class="sr-only" for="lastName">Last Name</label>
                            <input class="w-full p-3 border border-gray-300 rounded" id="lastName" name="lastName" placeholder="Last Name" required type="text"/>
                        </div>

                        <!-- Email & Phone Number -->
                        <div class="mb-4">
                            <label class="sr-only" for="email">Email</label>
                            <input class="w-full p-3 border border-gray-300 rounded" id="email" name="email" placeholder="Email" required type="email"/>
                        </div>
                        <div class="mb-4">
                            <label class="sr-only" for="phoneNumber">Phone Number</label>
                            <input class="w-full p-3 border border-gray-300 rounded" id="phoneNumber" name="phoneNumber" placeholder="Phone Number" required type="text"/>
                        </div>
                    </div>

                    <button class="w-full bg-blue-600 text-white p-3 rounded hover:bg-blue-700 mt-4" type="submit">Register</button>
                </form>

                <!-- Hiển thị thông báo lỗi -->
                <c:if test="${not empty message}">
                    <p class="text-red-600 text-center mt-4">${message}</p>
                </c:if>

                <script>
                    function togglePassword() {
                        const password = document.getElementById("password");
                        const confirmPassword = document.getElementById("confirmPassword");
                        const toggleIcons = document.querySelectorAll("#toggleIcon");
                        const isHidden = password.type === "password";
                        password.type = confirmPassword.type = isHidden ? "text" : "password";S
                        toggleIcons.forEach(icon => {
                            icon.textContent = isHidden ? "🙈" : "👁️";
                        });
                    }
                </script>
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
=======
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
>>>>>>> origin/ThanhMoi
