<%-- 
    Document   : navbar
    Created on : 27 Feb 2025, 09:40:37
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<style>
    .logo {
        box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1); /* Đổ bóng nhẹ */
        border-radius: 8px; /* Bo góc hình ảnh */
        transition: box-shadow 0.3s ease; /* Chỉ thay đổi bóng đổ khi hover */
    }

    .logo:hover {
        box-shadow: 0px 8px 20px rgba(0, 0, 0, 0.2); /* Bóng đổ đậm hơn khi hover */
    }
</style>

<div class="p-4">
    <img alt="Company Logo" class="mb-4 logo" height="100" src="./img/logo.png" width="300"/>
</div>
<nav class="space-y-2">
    <a class="flex items-center p-2 hover:bg-orange-800" href="#">
        <i class="fas fa-tachometer-alt mr-2"></i>
        Dashboard
    </a>
    <a class="flex items-center p-2 hover:bg-orange-800"  href="listAccount">
        <i class="fas fa-users mr-2"></i>
        Account List
    </a>
    <a class="flex items-center p-2 hover:bg-orange-800" href="#">
        <i class="fas fa-calendar-alt mr-2"></i>
        Event List
    </a>
    <a class="flex items-center p-2 hover:bg-orange-800" href="#">
        <i class="fas fa-cogs mr-2"></i>
        Product List
    </a>
    <a class="flex items-center p-2 hover:bg-orange-800" href="#">
        <i class="fas fa-comments mr-2"></i>
        Dialogue List
    </a>
    <a class="flex items-center p-2 hover:bg-orange-800" href="#">
        <i class="fas fa-box mr-2"></i>
        Order List
    </a>

    <a class="flex items-center p-2 hover:bg-orange-800" href="voucherList">
        <i class="fas fa-gift mr-2"></i>
        Voucher List
    </a>


    <a class="flex items-center p-2 hover:bg-orange-800" href="#">
        <i class="fas fa-bell mr-2"></i>
        Notification List
    </a>
    <a class="flex items-center p-2 hover:bg-orange-800" href="#">
        <i class="fas fa-comment-dots mr-2"></i>
        Chat
    </a>
    <div class="mt-4">
        <h3 class="px-2 text-sm font-semibold"> SETTINGS </h3>
        <a class="flex items-center p-2 hover:bg-orange-800" href="#">
            <i class="fas fa-cogs mr-2"></i>
            Configuration
        </a>
        <a class="flex items-center p-2 hover:bg-orange-800" href="#">
            <i class="fas fa-users-cog mr-2"></i>
            Management
        </a>
        <a class="flex items-center p-2 hover:bg-orange-800" href="logout">
            <i class="fas fa-sign-out-alt mr-2"></i> 
            Logout
        </a>
    </div>
    <div class="mt-4">
        <h3 class="px-2 text-sm font-semibold"> REPORTS </h3>
        <a class="flex items-center p-2 hover:bg-orange-800" href="#">
            <i class="fas fa-phone-alt mr-2"></i>
            Call history
        </a>
        <a class="flex items-center p-2 hover:bg-orange-800" href="#">
            <i class="fas fa-headset mr-2"></i>
            Call queue
        </a>
        <a class="flex items-center p-2 hover:bg-orange-800" href="#">
            <i class="fas fa-users mr-2"></i>
            Agents performance
        </a>
        <a class="flex items-center p-2 hover:bg-orange-800" href="#">
            <i class="fas fa-file-invoice-dollar mr-2"></i>
            Commission report
        </a>
        <a class="flex items-center p-2 hover:bg-orange-800" href="#">
            <i class="fas fa-calendar mr-2"></i>
            Scheduled report
        </a>
        <a class="flex items-center p-2 hover:bg-orange-800" href="#">
            <i class="fas fa-history mr-2"></i>
            Chat history
        </a>
        <a class="flex items-center p-2 hover:bg-orange-800" href="#">
            <i class="fas fa-chart-line mr-2"></i>
            Performance report
        </a>
    </div>
</nav>