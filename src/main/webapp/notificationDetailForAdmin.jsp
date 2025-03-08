<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <title>Notification Details</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous"/>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"/>
        <style>
            body {
                background-color: #f1f5f9;
            }
            .detail-container {
                background-color: #1e293b;
                border-radius: 8px;
                padding: 20px;
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                max-width: 800px;
                margin: 20px auto;
                color: white;
            }
            .btn-primary {
                background-color: rgb(251, 146, 60);
                border: none;
            }
            .btn-primary:hover {
                background-color: #fd7e14;
            }
        </style>
    </head>
    <body class="bg-gray-50 min-h-screen flex">
        <div class="w-64 bg-orange-400 text-white min-h-screen">
            <jsp:include page="navbarAdmin.jsp" flush="true"/> 
        </div>

        <main class="flex-1 p-6">
            <div class="detail-container">
                <h2 class="font-bold mb-4 text-orange-400">
                    <i class="fas fa-bell mr-2"></i> Notification
                </h2>
                <!-- Display error message if present -->
                <c:if test="${not empty error}">
                    <div class="alert alert-danger" role="alert">
                        ${error}
                    </div>
                </c:if>
               
                <c:choose>
                    <c:when test="${empty notification}">
                        <p class="text-gray-300">No notification found.</p>
                    </c:when>
                    <c:otherwise>
                        <div class="mb-4">
                            <h3 class="font-bold text-lg">${notification.notificationTitle}</h3><br>
                            <p class="text-gray-300">${notification.notificationDetails}</p><br>
                            <p class="text-gray-400 text-sm mt-2">
                                Date Created: <fmt:formatDate value="${notification.dateCreated}" pattern="dd/MM/yyyy"/>
                            </p>
                        </div>

                        <div class="flex justify-end space-x-4">
                            <a href="listnotification?receiverID=${param.receiverID}" 
                               class="btn btn-primary">
                                <i class="fas fa-arrow-left mr-1"></i> Back
                            </a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </main>
    </div>

    <script src="https://kit.fontawesome.com/bfab6e6450.js" crossorigin="anonymous"></script>
    <script src="/js/scriptHeader.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.min.js"
            integrity="sha384-BBtl+eGJRgqQAUMxJ7pMwbEyER4l1g+O15P+16Ep7Q9Q+zqX6gSbd85u4mG4QzX+"
    crossorigin="anonymous"></script>
</body>
</html>