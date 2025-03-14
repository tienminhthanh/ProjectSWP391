<%-- 
    Document   : banner
    Created on : Mar 8, 2025, 11:11:02 AM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
    <div class="relative w-full h-64 overflow-hidden">
        <!-- Hiển thị banner -->
        <form id="banner-form" action="#" method="GET">
            <input type="hidden" name="action" value="home">
            <a id="banner-link" href="eventDetails">
                <img id="banner-img" class="w-full h-full object-cover transition-opacity duration-500 cursor-pointer">
            </a>
        </form>

        <!-- Nút điều hướng -->
        <button id="prev-btn" class="absolute left-0 top-1/2 transform -translate-y-1/2 bg-gray-800 text-white p-2 rounded-full">
            ⬅
        </button>
        <button id="next-btn" class="absolute right-0 top-1/2 transform -translate-y-1/2 bg-gray-800 text-white p-2 rounded-full">
            ➡
        </button>

        <!-- Chỉ số trạng thái (dot indicators) -->
        <div id="dots-container" class="absolute bottom-2 left-1/2 transform -translate-x-1/2 flex space-x-2"></div>
    </div>
</head>
</html>
