<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Order List</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    </head>
    <body class="bg-gray-100">
        <header class="bg-white shadow">
            <div class="container mx-auto px-4 py-4 flex justify-between items-center">
                <div class="logo">
                    <a href="home">
                        <img src="img/logo.png" alt="WIBOOKS" class="h-10" /> 
                    </a> 
                </div>
                <a href="logout" class="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600 flex items-center">
                    <i class="fas fa-sign-out-alt mr-2"></i> Sign-out
                </a>
            </div>
        </header>

        <main class="container mx-auto px-4 py-6">
            <div class="mb-6">
                <h1 class="text-4xl font-bold text-center text-gray-800">ORDER LIST</h1>
            </div>
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">

                <c:forEach items="${requestScope.list}" var="c"> 
                    <c:set var="id" value="${c.orderID}"/>
                    <div class="bg-white shadow-lg rounded-lg p-6 transition-transform transform hover:scale-105">
                        <p class="text-lg font-semibold"><strong>Order ID:</strong> ${c.orderID}</p>
                        <p><strong>Order Date:</strong> ${c.orderDate}</p>
                        <p><strong>Delivery Address:</strong> ${c.deliveryAddress}</p>
                        <p><strong>Order Status:</strong> ${c.orderStatus}</p>                 
                        <p><strong>Payment Method:</strong> ${c.paymentMethod}</p>
                        <p><strong>Delivery Option:</strong> ${c.deliveryOption.optionName}</p>
                        <p class="text-xl font-bold text-green-600"><strong>Total Amount:</strong> ${c.preVoucherAmount}</p>
                        <div class="mt-4">
                            <a href="OrderDetailController?id=${id}" class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">View Details</a>
                        </div>                  
                    </div>            
                </c:forEach>
            </div>
        </main>
    </body>
</html>