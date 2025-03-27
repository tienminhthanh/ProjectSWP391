<%-- 
    Document   : navbar
    Created on : 27 Feb 2025, 09:40:37
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<style>
    html {
        font-size: 13px;
    }
    .logo {
        box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1); /* Đổ bóng nhẹ */
        border-radius: 8px; /* Bo góc hình ảnh */
        transition: box-shadow 0.3s ease; /* Chỉ thay đổi bóng đổ khi hover */
    }
    .logo:hover {
        box-shadow: 0px 8px 20px rgba(0, 0, 0, 0.2); /* Bóng đổ đậm hơn khi hover */
    }
    .side-nav a {
        transition: all 0.3s ease; /* Hiệu ứng mượt mà */
    }
    .side-nav a:hover {
        background-color: rgb(154 52 18 / var(--tw-bg-opacity, 1)); /* Màu nền khi hover (orange-800) */
    }
    .side-nav a.active {
        background-color: rgb(154 52 18 / var(--tw-bg-opacity, 1)) !important; /* Màu nền đậm khi active */
        box-shadow: 0px 8px 20px rgba(0, 0, 0, 0.2); /* Giữ bóng đổ đậm khi active */
    }
</style>

<div class="p-4">
    <img alt="Company Logo" class="mb-4 logo" height="100" src="./img/logo.png" width="300"/>
</div>
<nav class="space-y-2 side-nav">
    <c:if test="${not empty sessionScope.account and sessionScope.account.getRole() == 'admin'}">
        <a class="flex items-center p-2 hover:bg-orange-800 ${pageContext.request.requestURI.contains('readAccount') ? 'active' : ''}" href="readAccount">
            <i class="fas fa-user-circle mr-2"></i> 
            My information
        </a>
        <a class="flex items-center p-2 hover:bg-orange-800 ${pageContext.request.requestURI.contains('dashboard') ? 'active' : ''}" href="dashboard">
            <i class="fas fa-tachometer-alt mr-2"></i>
            Dashboard
        </a>
        <a class="flex items-center p-2 hover:bg-orange-800 ${pageContext.request.requestURI.contains('listAccount') ? 'active' : ''}" href="listAccount">
            <i class="fas fa-users mr-2"></i>
            Account List
        </a>
        <a class="flex items-center p-2 hover:bg-orange-800 ${pageContext.request.requestURI.contains('eventList') ? 'active' : ''}" href="eventList">
            <i class="fas fa-calendar-alt mr-2"></i>
            Event List
        </a>
        <a class="flex items-center p-2 hover:bg-orange-800 ${pageContext.request.requestURI.contains('voucherList') ? 'active' : ''}" href="voucherList">
            <i class="fas fa-gift mr-2"></i>
            Voucher List
        </a>
    </c:if>
    <c:if test="${not empty sessionScope.account and (sessionScope.account.getRole() == 'admin' or sessionScope.account.getRole() == 'staff')}">
        <a class="flex items-center p-2 hover:bg-orange-800 ${pageContext.request.requestURI.contains('manageProductList') ? 'active' : ''}" href="manageProductList">
            <i class="fas fa-cogs mr-2"></i>
            Product List
        </a>
        <a class="flex items-center p-2 hover:bg-orange-800 ${pageContext.request.requestURI.contains('OrderListForStaffController') ? 'active' : ''}" href="OrderListForStaffController">
            <i class="fas fa-box mr-2"></i>
            Order List
        </a>
        <a class="flex items-center p-2 hover:bg-orange-800 ${pageContext.request.requestURI.contains('listnotification') ? 'active' : ''}" href="listnotification">
            <i class="fas fa-bell mr-2"></i>
            Notification List
        </a>
        <a class="flex items-center p-2 hover:bg-orange-800 ${pageContext.request.requestURI.contains('chat') ? 'active' : ''}" href="chat">
            <i class="fas fa-comment-dots mr-2"></i>
            Chat
        </a>
    </c:if>
    <a class="flex items-center p-2 hover:bg-orange-800 ${pageContext.request.requestURI.contains('logout') ? 'active' : ''}" href="logout">
        <i class="fas fa-sign-out-alt mr-2"></i> 
        Logout
    </a>
</nav>