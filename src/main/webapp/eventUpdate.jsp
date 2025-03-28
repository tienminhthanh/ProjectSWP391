<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Update Event</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"/>
    </head>
    <body class="bg-gray-100 min-h-screen flex flex-col">
        <header class="bg-white shadow w-full">
            <a href="home" class="container mx-auto px-4 py-2 flex justify-between items-center">
                <img alt="WIBOOKS Logo" class="h-10" src="./img/logoWibooks-removebg-preview.png"/>
                <div class="flex items-center space-x-4">
                    <i class="fas fa-globe text-xl"></i>
                </div>
            </a>
        </header>
        <main class="flex-grow flex items-center justify-center">
            <div class="w-full max-w-4xl bg-white p-8 shadow-md">
                <h1 class="text-2xl font-semibold mb-4">Update Event</h1>
                <hr class="mb-6"/>
                <form action="eventUpdate" method="post" enctype="multipart/form-data">
                    <div class="grid grid-cols-1 gap-4">
                        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div class="mb-4">
                                <label class="block text-sm font-medium text-gray-700">Event ID</label>
                                <input type="text" name="eventID" value="${EVENT_DETAILS.eventID}" class="w-full p-3 border border-gray-300 rounded bg-gray-100" readonly>
                            </div>
                            <div class="mb-4">
                                <label class="block text-sm font-medium text-gray-700">Event Name</label>
                                <input type="text" name="eventName" value="${EVENT_DETAILS.eventName}" class="w-full p-3 border border-gray-300 rounded" required>
                            </div>
                        </div>
                        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div class="mb-4">
                                <label class="block text-sm font-medium text-gray-700">Date Started</label>
                                <input type="date" name="dateStarted" id="dateStarted" value="${EVENT_DETAILS.dateStarted}" class="w-full p-3 border border-gray-300 rounded" required>
                            </div>
                            <div class="mb-4">
                                <label class="block text-sm font-medium text-gray-700">Duration (days)</label>
                                <input type="number" name="duration" value="${EVENT_DETAILS.duration}" class="w-full p-3 border border-gray-300 rounded" required>
                            </div>
                        </div>
                        <div class="mb-4">
                            <label class="block text-sm font-medium text-gray-700">Description</label>
                            <textarea name="description" class="w-full p-3 border border-gray-300 rounded" required rows="4">${EVENT_DETAILS.description}</textarea>
                        </div>
                        <div class="mb-4">
                            <label class="block text-sm font-medium text-gray-700">Banner</label>
                            <input type="file" id="bannerFile" name="bannerFile" accept="image/*" class="w-full p-3 border border-gray-300 rounded">
                            <!-- Hi?n th? ???ng d?n ?nh hi?n t?i -->
                            <p class="text-sm text-gray-500 mt-1">Current Banner: ${EVENT_DETAILS.banner}</p>
                            <!-- Hi?n th? ?nh hi?n t?i -->
                            <div class="mt-4 flex justify-center">
                                <img id="previewImage" 
                                     src="<%= request.getContextPath()%>/${EVENT_DETAILS.banner}" 
                                     class="max-w-xs md:max-w-sm lg:max-w-md rounded-md border border-gray-300 shadow-md">
                            </div>
                        </div>
                        <!-- Hidden input ?? l?u dateCreated -->
                        <input type="hidden" id="dateCreated" value="${EVENT_DETAILS.dateCreated}">
                    </div>
                    <button class="w-full bg-orange-400 text-white p-3 rounded hover:bg-orange-500 mt-4" type="submit">Update Event</button>
                </form>
                <div class="mt-6">
                    <a class="text-orange-400 hover:underline" href="eventList">
                        <i class="fas fa-arrow-left mr-2"></i> Back to Event List
                    </a>
                </div>
            </div>
        </main>
        <script>
            // X? lý preview ?nh
            document.getElementById("bannerFile").addEventListener("change", function (event) {
                let file = event.target.files[0];
                let previewImage = document.getElementById("previewImage");
                if (file) {
                    let reader = new FileReader();
                    reader.onload = function (e) {
                        previewImage.src = e.target.result;
                    };
                    reader.readAsDataURL(file);
                }
            });

            // ??t giá tr? min và ki?m tra Date Started
            document.addEventListener("DOMContentLoaded", function () {
                const dateStartedInput = document.getElementById("dateStarted");
                const dateCreated = document.getElementById("dateCreated").value;

                // ??t thu?c tính min là dateCreated
                dateStartedInput.setAttribute("min", dateCreated);

                // Ki?m tra khi submit form
                document.querySelector("form").addEventListener("submit", function (event) {
                    const dateStarted = dateStartedInput.value;

                    if (dateStarted < dateCreated) {
                        alert("Date Started cannot be earlier than Date Created (" + dateCreated + ")!");
                        event.preventDefault(); // Ng?n submit form
                    }
                });
            });
        </script>
    </body>
</html>