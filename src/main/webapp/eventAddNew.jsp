<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
        <title>Add New Event</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"/>
    </head>
    <body class="bg-gray-100 min-h-screen flex flex-col">
        <header class="bg-white shadow w-full">
            <a href="home" class="container mx-auto px-4 py-2 flex justify-between items-center">
                <img alt="WIBOOKS Logo" class="h-10" height="50" src="./img/logoWibooks-removebg-preview.png" width="200"/>
                <div class="flex items-center space-x-4">
                    <i class="fas fa-globe text-xl"></i>
                </div>
            </a>
        </header>
        <main class="flex-grow flex items-center justify-center">
            <div class="w-full max-w-4xl bg-white p-8 shadow-md">
                <h1 class="text-2xl font-semibold mb-4">Add New Event</h1>
                <hr class="mb-6"/>
                <c:if test="${not empty message}">
                    <p class="text-red-600 text-center mb-4">${message}</p>
                </c:if>
                <form action="eventAddNew" method="post" enctype="multipart/form-data">
                    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <!-- Hàng 1: Event Name và Banner -->
                        <div class="mb-4">
                            <label class="block text-sm font-medium text-gray-700">Event Name</label>
                            <input class="w-full p-3 border border-gray-300 rounded" name="eventName" placeholder="Enter event name" required type="text"/>
                        </div>
                        <div class="mb-4">
                            <label class="block text-sm font-medium text-gray-700">Banner</label>
                            <input type="file" name="bannerFile" accept="image/*" class="w-full p-3 border border-gray-300 rounded" required>
                        </div>

                        <!-- Hàng 2: Date Started và Duration -->
                        <div class="mb-4">
                            <label class="block text-sm font-medium text-gray-700">Date Started</label>
                            <input class="w-full p-3 border border-gray-300 rounded" name="dateStarted" placeholder="Enter Date Started" required type="date"/>
                        </div>
                        <div class="mb-4">
                            <label class="block text-sm font-medium text-gray-700">Duration (days)</label>
                            <input class="w-full p-3 border border-gray-300 rounded" name="duration" placeholder="Enter duration" required type="number"/>
                        </div>

                        <!-- Hàng 3: Description -->
                        <div class="mb-4 col-span-2">
                            <label class="block text-sm font-medium text-gray-700">Description</label>
                            <textarea class="w-full p-3 border border-gray-300 rounded" name="description" placeholder="Enter event description" required rows="4"></textarea>
                        </div>
                    </div>
                    <button class="w-full bg-orange-400 text-white p-3 rounded hover:bg-orange-500 mt-4" type="submit">Add Event</button>
                </form>
                <div class="mt-6">
                    <a class="text-orange-400 hover:underline" href="eventList">
                        <i class="fas fa-arrow-left mr-2"></i> Back to Event List
                    </a>
                </div>
            </div>
        </main>
    </body>
</html>