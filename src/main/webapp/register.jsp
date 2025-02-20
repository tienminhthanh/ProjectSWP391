<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
    <head>
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