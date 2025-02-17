<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
        <title>View Account Information</title>
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
                <h1 class="text-2xl font-semibold mb-4">Account Information</h1>
                <hr class="mb-6"/>
                <div class="space-y-4">
                    <p class="flex items-center"><i class="fas fa-user mr-2"></i> Username: ${account.username}</p>
                    <p class="flex items-center"><i class="fas fa-id-badge mr-2"></i> First Name: ${account.firstName}</p>
                    <p class="flex items-center"><i class="fas fa-id-badge mr-2"></i> Last Name: ${account.lastName}</p>
                    <p class="flex items-center"><i class="fas fa-envelope mr-2"></i> Email: ${account.email}</p>
                    <p class="flex items-center"><i class="fas fa-phone mr-2"></i> Phone Number: ${account.phoneNumber}</p>
                    <p class="flex items-center"><i class="fas fa-birthday-cake mr-2"></i> Birth Date: ${account.birthDate}</p>
                </div>
                <div class="mt-6 flex justify-between">
                    <a class="bg-blue-600 text-white p-3 rounded hover:bg-blue-700 flex items-center" href="updateAccount?username=${account.username}">
                        <i class="fas fa-edit mr-2"></i> Update Information
                    </a>
                    <a class="bg-red-600 text-white p-3 rounded hover:bg-red-700 flex items-center" href="deleteAccount?username=${account.username}">
                        <i class="fas fa-trash mr-2"></i> Delete Account
                    </a>
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