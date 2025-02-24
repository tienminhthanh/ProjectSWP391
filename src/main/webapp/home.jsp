<%@ page contentType="text/html; charset=UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html lang="en">
    <head>
        <meta charset="utf-8"/>
        <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
        <title>
            Book Walker
        </title>
        <script src="https://cdn.tailwindcss.com">
        </script>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet"/>
    </head>
    <body class="bg-gray-100">
        <header class="bg-blue-600 p-4 flex items-center justify-between">
            <div class="flex items-center space-x-4">

                <form action="search">
                    <input class="p-2 rounded w-96" placeholder="title, author or keywords" type="search"/>
                </form>
                <button class="bg-blue-700 text-white p-2 rounded">
                    Merch
                </button>
                <button class="bg-blue-700 text-white p-2 rounded">
                    Books
                </button>
                <button class="bg-blue-700 text-white p-2 rounded">
                    All Products
                </button>
            </div>
            <div class="flex items-center space-x-4">
                <c:choose>
                    <c:when test="${not empty sessionScope.account}">
                        <a href="readAccount" class="bg-green-500 text-white p-2 rounded hover:bg-green-600">
                            <i class="fas fa-user mr-2"></i> View Profile
                        </a>
                        <a href="logout" class="bg-red-500 text-white p-2 rounded hover:bg-red-600">
                            <i class="fas fa-sign-out-alt mr-2"></i> Logout
                        </a>
                    </c:when>
                    <c:otherwise>
                        <a href="login.jsp" class="text-white bg-blue-500 p-2 rounded hover:bg-blue-600">
                            Sign In
                        </a>
                        <a href="register.jsp" class="bg-orange-500 text-white p-2 rounded hover:bg-orange-600">
                            Register
                        </a>
                    </c:otherwise>
                </c:choose>
            </div>

        </header>
        <!-- Import Alpine.js -->
        <script src="https://cdn.jsdelivr.net/npm/alpinejs@3.x.x/dist/cdn.min.js" defer></script>

        <div x-data="{ 
             current: 0, 
             banners: [
             '/mavenproject7/img/banner_event/banner1.jpg',
             '/mavenproject7/img/banner_event/banner2.jpg',
             '/mavenproject7/img/banner_event/banner3.jpg',
             '/mavenproject7/img/banner_event/banner4.jpg'
             ],
             next() {
             this.current = (this.current + 1) % this.banners.length;
             },
             prev() {
             this.current = (this.current - 1 + this.banners.length) % this.banners.length;
             },
             autoSlide() {
             setInterval(() => { this.next(); }, 3000);
             }
             }" x-init="autoSlide()" class="relative w-full h-64 overflow-hidden">

            <!-- banner hiển thị -->
            <img :src="banners[current]" class="w-full h-full object-cover transition-opacity duration-500">

            <!-- nút điều hướng -->
            <button @click="prev()" class="absolute left-0 top-1/2 transform -translate-y-1/2 bg-gray-800 text-white p-2 rounded-full">
                ⬅
            </button>
            <button @click="next()" class="absolute right-0 top-1/2 transform -translate-y-1/2 bg-gray-800 text-white p-2 rounded-full">
                ➡
            </button>

            <!-- chỉ số -->
            <div class="absolute bottom-2 left-1/2 transform -translate-x-1/2 flex space-x-2">
                <template x-for="(banner, index) in banners" :key="index">
                    <div @click="current = index" :class="current === index ? 'bg-blue-500' : 'bg-gray-300'"
                          class="w-3 h-3 rounded-full cursor-pointer"></div>
                </template>
            </div>
        </div>




        <div class="flex">
            <aside class="w-1/4 p-4 bg-gray-200">
                <div class="bg-orange-500 text-white p-4 rounded mb-4">
                    <h2>
                        New to BOOK☆WALKER?
                    </h2>
                    <button class="bg-orange-600 text-white p-2 rounded mt-2">
                        <a href="register.jsp">Create Account</a>
                    </button>
                </div>
                <div class="mb-4">
                    <button class="text-blue-600">
                        <a href="login.jsp">Sign In</a>
                    </button>
                </div>
                <div class="mb-4">
                    <h3 class="font-bold">
                        Get Started
                    </h3>
                    <ul class="list-disc list-inside">
                        <li>
                            50% Point-Back for Newcomers
                        </li>
                        <li>
                            About BOOK☆WALKER
                        </li>
                        <li>
                            Redeem Coupons
                        </li>
                        <li>
                            Redeem Gifted eBooks
                        </li>
                        <li>
                            FAQ
                        </li>
                        <li>
                            Point Affiliate Program
                        </li>
                    </ul>
                </div>
                <div>
                    <h3 class="font-bold">
                        Start Reading On
                    </h3>
                    <ul class="list-disc list-inside">
                        <li>
                            PC
                        </li>
                        <li>
                            Android
                        </li>
                        <li>
                            iOS
                        </li>
                    </ul>
                </div>
            </aside>
            <main class="w-3/4 p-4">
                <div class="mb-4">
                    <h2 class="text-xl font-bold">
                        Popular Searches
                    </h2>
                    <div class="flex space-x-4">
                        <button class="bg-blue-500 text-white p-2 rounded">
                            On Sale Merch
                        </button>
                        <button class="bg-blue-500 text-white p-2 rounded">
                            Top Merch
                        </button>
                        <button class="bg-blue-500 text-white p-2 rounded">
                            On Sale Books
                        </button>
                        <button class="bg-blue-500 text-white p-2 rounded">
                            Top Books
                        </button>
                        <button class="bg-blue-500 text-white p-2 rounded">
                            Seasonal Anime - Merch
                        </button>
                        <button class="bg-blue-500 text-white p-2 rounded">
                            Seasonal Anime - Books
                        </button>
                    </div>
                </div>
                <div class="mb-4">
                    <h2 class="text-xl font-bold">
                        Voucher List
                    </h2>
                    <div class="flex flex-wrap gap-4">
                        <c:forEach var="voucher" items="${listVoucher}">
                            <button class="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600 transition"
                                    onclick="navigateToUpdate(${voucher.voucherID})">
                                ${voucher.voucherName} - ${voucher.voucherValue}
                            </button>
                        </c:forEach>

                        <c:if test="${empty listVoucher}">
                            <p class="text-gray-500 italic">No vouchers available.</p>
                        </c:if>
                    </div>
                </div>
                <!--
                <script>
                    function navigateToUpdate(voucherID) {
                        window.location = 'voucherDetails?voucherId=' + voucherID;
                    }
                </script>-->

                <div>
                    <h2 class="text-xl font-bold mb-4">
                        New Releases (Volumes)
                    </h2>
                    <div class="flex space-x-4 overflow-x-auto">
                        <div class="bg-white p-4 rounded shadow w-48">
                            <img alt="Book 1" class="mb-2" height="150" src="https://placehold.co/100x150?text=Book+1" width="100"/>
                            <h3 class="text-sm font-bold">
                                The Eminence in Shadow, Vol. 6 (light novel)
                            </h3>
                            <p class="text-orange-500 font-bold">
                                US $7.99
                            </p>
                            <button class="bg-blue-500 text-white p-2 rounded mt-2">
                                Preview
                            </button>
                            <button class="bg-orange-500 text-white p-2 rounded mt-2">
                                Cart
                            </button>
                        </div>
                        <div class="bg-white p-4 rounded shadow w-48">
                            <img alt="Book 2" class="mb-2" height="150" src="https://placehold.co/100x150?text=Book+2" width="100"/>
                            <h3 class="text-sm font-bold">
                                Crossplay Love: Otaku x Punk Vol. 11
                            </h3>
                            <p class="text-orange-500 font-bold">
                                US $9.99
                            </p>
                            <button class="bg-blue-500 text-white p-2 rounded mt-2">
                                Preview
                            </button>
                            <button class="bg-orange-500 text-white p-2 rounded mt-2">
                                Cart
                            </button>
                        </div>
                        <div class="bg-white p-4 rounded shadow w-48">
                            <img alt="Book 3" class="mb-2" height="150" src="https://placehold.co/100x150?text=Book+3" width="100"/>
                            <h3 class="text-sm font-bold">
                                The Too-Perfect Saint: Tossed Aside by My...
                            </h3>
                            <p class="text-orange-500 font-bold">
                                US $9.99
                            </p>
                            <button class="bg-blue-500 text-white p-2 rounded mt-2">
                                Preview
                            </button>
                            <button class="bg-orange-500 text-white p-2 rounded mt-2">
                                Cart
                            </button>
                        </div>
                        <div class="bg-white p-4 rounded shadow w-48">
                            <img alt="Book 4" class="mb-2" height="150" src="https://placehold.co/100x150?text=Book+4" width="100"/>
                            <h3 class="text-sm font-bold">
                                A Cozy Life in the Woods with the White Witch...
                            </h3>
                            <p class="text-orange-500 font-bold">
                                US $7.99
                            </p>
                            <button class="bg-blue-500 text-white p-2 rounded mt-2">
                                Preview
                            </button>
                            <button class="bg-orange-500 text-white p-2 rounded mt-2">
                                Cart
                            </button>
                        </div>
                        <div class="bg-white p-4 rounded shadow w-48">
                            <img alt="Book 5" class="mb-2" height="150" src="https://placehold.co/100x150?text=Book+5" width="100"/>
                            <h3 class="text-sm font-bold">
                                Even Though We're Adults Vol. 10
                            </h3>
                            <p class="text-orange-500 font-bold">
                                US $9.99
                            </p>
                            <button class="bg-blue-500 text-white p-2 rounded mt-2">
                                Preview
                            </button>
                            <button class="bg-orange-500 text-white p-2 rounded mt-2">
                                Cart
                            </button>
                        </div>
                        <div class="bg-white p-4 rounded shadow w-48">
                            <img alt="Book 6" class="mb-2" height="150" src="https://placehold.co/100x150?text=Book+6" width="100"/>
                            <h3 class="text-sm font-bold">
                                A Certain Scientific Railgun Vol. 19
                            </h3>
                            <p class="text-orange-500 font-bold">
                                US $9.99
                            </p>
                            <button class="bg-blue-500 text-white p-2 rounded mt-2">
                                Preview
                            </button>
                            <button class="bg-orange-500 text-white p-2 rounded mt-2">
                                Cart
                            </button>
                        </div>
                        <div class="bg-white p-4 rounded shadow w-48">
                            <img alt="Book 7" class="mb-2" height="150" src="https://placehold.co/100x150?text=Book+7" width="100"/>
                            <h3 class="text-sm font-bold">
                                Kurokiya-san Wants to Lead Him Around by the...
                            </h3>
                            <p class="text-orange-500 font-bold">
                                US $13.95
                            </p>
                            <button class="bg-blue-500 text-white p-2 rounded mt-2">
                                Preview
                            </button>
                            <button class="bg-orange-500 text-white p-2 rounded mt-2">
                                Cart
                            </button>
                        </div>
                        <div class="bg-white p-4 rounded shadow w-48">
                            <img alt="Book 8" class="mb-2" height="150" src="https://placehold.co/100x150?text=Book+8" width="100"/>
                            <h3 class="text-sm font-bold">
                                Pass the Monster Meat, Milady! 7
                            </h3>
                            <p class="text-orange-500 font-bold">
                                US $10.99
                            </p>
                            <button class="bg-blue-500 text-white p-2 rounded mt-2">
                                Preview
                            </button>
                            <button class="bg-orange-500 text-white p-2 rounded mt-2">
                                Cart
                            </button>
                        </div>
                        <div class="bg-white p-4 rounded shadow w-48">
                            <img alt="Book 9" class="mb-2" height="150" src="https://placehold.co/100x150?text=Book+9" width="100"/>
                            <h3 class="text-sm font-bold">
                                Lycoris Recoil: Ordinary Days
                            </h3>
                            <p class="text-orange-500 font-bold">
                                US $8.99
                            </p>
                            <button class="bg-blue-500 text-white p-2 rounded mt-2">
                                Preview
                            </button>
                            <button class="bg-orange-500 text-white p-2 rounded mt-2">
                                Cart
                            </button>
                        </div>
                        <div class="bg-white p-4 rounded shadow w-48">
                            <img alt="Book 10" class="mb-2" height="150" src="https://placehold.co/100x150?text=Book+10" width="100"/>
                            <h3 class="text-sm font-bold">
                                I Have a Crush at Work 8
                            </h3>
                            <p class="text-orange-500 font-bold">
                                US $10.99
                            </p>
                            <button class="bg-blue-500 text-white p-2 rounded mt-2">
                                Preview
                            </button>
                            <button class="bg-orange-500 text-white p-2 rounded mt-2">
                                Cart
                            </button>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    </body>

</html>