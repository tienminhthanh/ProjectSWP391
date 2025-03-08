<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Update Notification - Admin</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous"/>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"/>
        <style>
            body {
                background-color: #f1f5f9;
            }
            .form-container {
                background-color: #1e293b;
                color: white;
                border-radius: 8px;
                padding: 20px;
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                max-width: 600px;
                margin: 20px auto;
            }
            .btn-primary {
                background-color: #3b82f6;
                border: none;
            }
            .btn-primary:hover {
                background-color: #2563eb;
            }
            .btn-secondary {
                background-color: #64748b;
                border: none;
            }
            .btn-secondary:hover {
                background-color: #475569;
            }
        </style>
    </head>
    <body>
        <div class="form-container">
            <h2 class="text-lg font-bold mb-4">
                <i class="fas fa-edit mr-2"></i> Update Notification
            </h2>

            <!-- Form to update notification details -->
            <form action="updatenotification" method="post">
                <input type="hidden" name="notificationID" value="${param.notificationID}">
                <input type="hidden" name="oldNotificationTitle" value="${notification.notificationTitle}">
                <input type="hidden" name="oldNotificationDetails" value="${notification.notificationDetails}">

                <div class="mb-4">
                    <label class="block text-gray-300 mb-2" for="notificationTitle">Title</label>
                    <input type="text" 
                           name="notificationTitle" 
                           id="notificationTitle" 
                           value="${notification.notificationTitle}" 
                           class="w-full p-2 rounded bg-gray-700 text-white border border-gray-600"
                           required>
                </div>

                <div class="mb-4">
                    <label class="block text-gray-300 mb-2" for="notificationDetails">Description</label>
                    <textarea name="notificationDetails" 
                              id="notificationDetails" 
                              class="w-full p-2 rounded bg-gray-700 text-white border border-gray-600" 
                              rows="5" 
                              required>${notification.notificationDetails}</textarea>
                </div>

                <div class="flex justify-end space-x-2">
                    <a href="listnotification" class="btn btn-secondary">Cancel</a>
                    <button type="submit" class="btn btn-primary">Save Changes</button>
                </div>
            </form>

            <!-- Display error message if present -->
            <% if (request.getAttribute("error") != null) {%>
            <p class="text-red-400 mt-4"><%= request.getAttribute("error")%></p>
            <% }%>
        </div>

        <script src="https://kit.fontawesome.com/bfab6e6450.js" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.min.js"
                integrity="sha384-BBtl+eGJRgqQAUMxJ7pMwbEyER4l1g+O15P+16Ep7Q9Q+zqX6gSbd85u4mG4QzX+"
        crossorigin="anonymous"></script>
    </body>
</html>