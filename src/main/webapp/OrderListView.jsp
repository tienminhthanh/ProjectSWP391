<%-- 
    Document   : OrderListView
    Created on : Feb 24, 2025, 5:28:20 PM
    Author     : Macbook
--%>

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
            <div class="container mx-auto px-4 py-2 flex justify-between items-center">
                <div class="logo">
                    <a href="home">
                        <a href="home"><img src="img/logo.png" alt="WIBOOKS" /></a> 
                    </a> 
                </div>
                <a href="logout" class="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600 flex items-center">
                    <i class="fas fa-sign-out-alt mr-2"></i> Sign-out
                </a>
            </div>
        </header>
        <div class="container mx-auto px-4 py-6">
            <div class="mb-4">
                <h1 class="text-2xl font-bold">ORDER LIST</h1>
            </div>
            <div class="space-y-4">


                <c:forEach items="${requestScope.list}" var="c"> 
                    <c:set var="id" value="${c.orderID}"/>
                    <div class="bg-white shadow-md rounded-lg p-4">
                        <p><strong>Order ID:</strong> ${c.orderID}</p>
                        <p><strong>Order Date:</strong> ${c.orderDate}</p>
                        <p><strong>Delivery Address:</strong> ${c.deliveryAddress}</p>
                        <p><strong>Order Status:</strong> ${c.orderStatus}</p>                 
                        <p><strong>Payment Method:</strong> ${c.paymentMethod}</p>
                        <p><strong>Delivery Option:</strong> ${c.deliveryOption.optionName}</p>
                        <p><strong>Total Amount:</strong> ${c.preVoucherAmount}</p>
                        <div class="mt-4">
                            <a href="OrderDetailController?id=${id}" class="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600">View Details</a>
                        </div>                  
                    </div>            
                </c:forEach>
            </div>
        </div>
    </body>
</html>
