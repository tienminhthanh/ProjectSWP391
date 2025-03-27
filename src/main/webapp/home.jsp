<%@page import="java.io.FilenameFilter"%>
<%@page import="java.io.File"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" errorPage ="error.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>                                                    
<fmt:setLocale value="en_US"/>

<html lang="en">
    <head>
        <meta charset="utf-8"/>
        <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
        <title>
            WIBOOKS - More Than Just Books
        </title>

        <!-- Preload Fonts for Faster Icon Loading -->
        <link rel="preload" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/webfonts/fa-solid-900.woff2" as="font" type="font/woff2" crossorigin="anonymous">
        <link rel="preload" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/webfonts/fa-regular-400.woff2" as="font" type="font/woff2" crossorigin="anonymous">

        <!-- Load FontAwesome via CSS (Faster than JS Kit) -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" crossorigin="anonymous">


        <!--Tailwind-->
        <script src="https://cdn.tailwindcss.com"></script>
        
        <!-- Stylesheets -->
        <link rel="stylesheet" href="css/styleHome.css"/>
        <link rel="stylesheet" href="css/styleHeader.css"/>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" crossorigin="anonymous">
        <link rel="stylesheet" href="css/styleFooter.css"/>
        <link rel="stylesheet" href="css/styleCustomerSidebar.css"/>
        <link rel="stylesheet" href="css/styleProductCard.css"/>
        
        
         <!-- Alpine.js for interactive UI -->
        <script src="https://cdn.jsdelivr.net/npm/alpinejs@3.x.x/dist/cdn.min.js" defer></script>

        <!-- Custom Scripts (Defer to avoid render blocking) -->
        <script src="js/scriptProductCard.js" defer></script>
        <script src="js/scriptVoucherDateEnd.js" defer></script>
        <script src="js/scriptCusSidebar.js" defer></script>
        <script src="js/scriptHeader.js" defer></script>
        <script src="js/scriptCusSideBarNOTDetails.js" defer></script>
        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.min.js" crossorigin="anonymous" defer></script>
        <!-- FontAwesome Kit (Optional, but defer it) -->
        <script src="https://kit.fontawesome.com/bfab6e6450.js" crossorigin="anonymous" defer></script>
         <!-- SweetAlert2 -->
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11" defer></script>

    </head>
    <body class="bg-gray-100">
        <div class="header-container">
            <jsp:include page="header.jsp" flush="true"/> 
        </div>

        <div class="banner-container">
            <jsp:include page="banner.jsp" flush="true"/> 
        </div>

        <div class="flex flex-col md:flex-row">
            <jsp:include page="customerSidebar.jsp"/>

            <!--Main section-->
            <main class="w-full md:w-5/6 p-3 flex flex-col">

                <!--Popular Searches-->
                <div class="my-4 bg-white popular-search-area border-t-4 border-orange-500">

                    <div class="w-full flex flex-col lg:flex-row items-center justify-center p-8 gap-8">
                        <div class="w-full lg:w-1/3 h-64">
                            <h2 class="text-xl font-bold relative pb-4 text-center text-red-500">
                                On Sale
                            </h2>
                            <div class="w-full rounded-lg border-4 border-red-500 flex flex-row items-center justify-center gap-2 px-2">
                                <a href="sale?type=merch" class="w-1/3 max-h-full hover:shadow-xl hover:bg-gray-200 rounded-lg">
                                    <img  class="max-h-36 w-full object-contain" src="img/popular_search_img/sale-merch.png" alt="On-Sale Merch"/>
                                </a>
                                <span class=" w-1/3">
                                    <img class="h-40 w-full object-contain lg:object-cover" src="img/popular_search_img/sale-label.png" alt="On-Sale Label"/>
                                </span>
                                <a href="sale?type=book" class="w-1/3 max-h-full hover:shadow-xl hover:bg-gray-200 rounded-lg">
                                    <img class="max-h-36 w-full object-contain" src="img/popular_search_img/sale-book.png" alt="On-Sale Books"/>
                                </a>
                            </div>
                        </div>
                        <div class="w-full lg:w-1/3 h-64">
                            <h2 class="text-xl font-bold relative pb-4 text-center text-amber-500">
                                Monthly Ranking
                            </h2>
                            <div class="w-full rounded-lg border-4 border-amber-500 flex flex-row items-center justify-center gap-2  px-2">
                                <a href="ranking?type=merch" class="w-1/3 max-h-full hover:shadow-xl hover:bg-gray-200 rounded-lg">
                                    <img class="max-h-36 w-full object-contain" src="img/popular_search_img/top-merch.png" alt="Top Merch"/>
                                </a>
                                <span class=" w-1/3">
                                    <img class="h-40 w-full object-contain lg:object-cover" src="img/popular_search_img/top-label.png" alt="Top Label"/>
                                </span>
                                <a href="ranking?type=book" class="w-1/3 max-h-full hover:shadow-xl hover:bg-gray-200 rounded-lg">
                                    <img class="max-h-36 w-full object-contain" src="img/popular_search_img/top-book.png" alt="Top Books"/>
                                </a>
                            </div>
                        </div>
                        <div class="w-full lg:w-1/3 h-64">
                            <h2 class="text-xl font-bold relative pb-4 text-center text-sky-400">
                                Trending
                            </h2>
                            <div class="w-full rounded-lg border-4 border-sky-300 flex flex-row items-center justify-center gap-2 px-2 ">
                                <a href="series?id=1" class="w-1/3 max-h-full hover:shadow-xl hover:bg-gray-200 rounded-lg">
                                    <img class="max-h-36 w-full object-contain"src="img/popular_search_img/special-merch.png" alt="Special Merch"/>
                                </a>
                                <span class=" w-1/3">
                                    <img class="max-h-40 w-full object-contain" src="img/popular_search_img/special-label.png" alt="Special Label"/>
                                </span>
                                <a href="genre?id=18" class="w-1/3 max-h-full hover:shadow-xl hover:bg-gray-200 rounded-lg">
                                    <img class="max-h-36 w-full object-contain"src="img/popular_search_img/special-book.png" alt="Special Books"/>
                                </a>
                            </div>
                        </div>
                    </div>

                </div>

                <!--Voucher-->
                <div class="mb-4 bg-white voucher-area border-t-4 border-orange-500">
                    <h2 class="text-xl font-bold relative pt-4 pb-4 text-center">
                        Available Now
                    </h2>
                    <div class="flex flex-nowrap gap-4 overflow-x-auto pb-4">
                        <c:forEach var="voucher" items="${listVoucher}">
                            <div class="voucher-card relative flex-shrink-0 w-[458px] h-[159px] p-4"
                                 style="background-image: url('/img/background_voucher/discount_voucher.jpg'); background-size: cover; background-position: center;">
                                <div class="absolute top-0 left-[30%] w-[70%] h-full flex flex-col justify-center px-4">
                                    <!-- Tên Voucher -->
                                    <p class="font-bold text-lg text-orange-600">${voucher.voucherName}</p>
                                    <!-- Giá trị giảm -->
                                    <p>Sale
                                        <span>
                                            <c:choose>
                                                <c:when test="${voucher.voucherType eq 'PERCENTAGE'}">
                                                    ${voucher.voucherValue} %
                                                </c:when>
                                                <c:otherwise>
                                                    <fmt:formatNumber value="${voucher.voucherValue}" type="number" groupingUsed="true"/> đ
                                                </c:otherwise>
                                            </c:choose>
                                        </span>
                                    </p>

                                    <!-- Hiển thị Max Discount nếu là Percentage -->
                                    <c:if test="${voucher.voucherType eq 'PERCENTAGE'}">
                                        <p>Up to
                                            <span><fmt:formatNumber value="${voucher.maxDiscountAmount}" type="number" groupingUsed="true"/> đ</span>
                                        </p>
                                    </c:if>

                                    <!-- Điều kiện sử dụng -->
                                    <p>For orders from
                                        <span>
                                            <fmt:formatNumber value="${voucher.minimumPurchaseAmount}" type="number" groupingUsed="true"/> đ
                                        </span>
                                    </p>

                                    <!-- Hạn sử dụng -->
                                    <div class="voucher" data-start="${voucher.dateStarted}" data-duration="${voucher.duration}">
                                        <p><strong>EXP:</strong> <span class="date-end"></span></p>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>

                        <c:if test="${empty listVoucher}">
                            <div class="flex justify-center items-center h-40 w-full">
                                <p class="text-gray-500 italic">No vouchers available.</p>
                            </div>
                        </c:if>
                    </div>
                    <h2 class="text-xl font-bold relative pt-4 pb-4 text-center">
                        Coming Soon
                    </h2>
                    <div class="flex flex-nowrap gap-4 overflow-x-auto pb-4">

                        <c:forEach var="voucherComeSoon" items="${listVoucherComeSoon}">
                            <div class="voucher-card relative flex-shrink-0 w-[458px] h-[159px] p-4"
                                 style="background-image: url('/img/background_voucher/discount_voucher.jpg'); background-size: cover; background-position: center;">
                                <div class="absolute top-0 left-[30%] w-[70%] h-full flex flex-col justify-center px-4">

                                    <!-- Tên Voucher -->
                                    <p class="font-bold text-lg text-orange-600">${voucherComeSoon.voucherName}</p>

                                    <!-- Giá trị giảm -->
                                    <p>Sale
                                        <span>
                                            <c:choose>
                                                <c:when test="${voucherComeSoon.voucherType eq 'PERCENTAGE'}">
                                                    ${voucherComeSoon.voucherValue} %
                                                </c:when>
                                                <c:otherwise>
                                                    <fmt:formatNumber value="${voucherComeSoon.voucherValue}" type="number" groupingUsed="true"/> đ
                                                </c:otherwise>
                                            </c:choose>
                                        </span>
                                    </p>

                                    <!-- Hiển thị Max Discount nếu là Percentage -->
                                    <c:if test="${voucherComeSoon.voucherType eq 'PERCENTAGE'}">
                                        <p>Up to
                                            <span><fmt:formatNumber value="${voucherComeSoon.maxDiscountAmount}" type="number" groupingUsed="true"/> đ</span>
                                        </p>
                                    </c:if>

                                    <!-- Điều kiện sử dụng -->
                                    <p>For orders from
                                        <span>
                                            <fmt:formatNumber value="${voucherComeSoon.minimumPurchaseAmount}" type="number" groupingUsed="true"/> đ
                                        </span>
                                    </p>

                                    <!-- Hạn sử dụng -->
                                    <div class="voucher" data-start="${voucherComeSoon.dateStarted}" data-duration="${voucherComeSoon.duration}">
                                        <p><strong>Started on: </strong>${voucherComeSoon.dateStarted}</p>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>

                        <c:if test="${empty listVoucherComeSoon}">
                            <div class="flex justify-center items-center h-40 w-full">
                                <p class="text-gray-500 italic">No vouchers available.</p>
                            </div>
                        </c:if>
                    </div>
                </div>

                <!--Trending-->
                <div class="bg-white mb-8">
                    <c:if test="${not empty sessionScope.animeBookHome}">
                        <h2 class="text-xl font-bold relative py-3 text-center bg-gradient-to-r from-yellow-500 via-amber-500 to-orange-500 text-white ">
                            Anime Adaptation
                        </h2>
                        <!--Loop through product list-->
                        <div class="w-full mt-4">
                            <div class="gap-4 w-full overflow-x-auto">
                                <div class="grid grid-flow-col auto-cols-max gap-4 min-w-max">
                                    <c:forEach var="currentProduct" items="${sessionScope.animeBookHome}">
                                        <c:set var="currentProduct" value="${currentProduct}" scope="request"/>
                                        <jsp:include page="productCard.jsp"/>
                                    </c:forEach>
                                </div>
                            </div>
                            <div class="w-full">
                                <a href="genre?id=18">
                                    <button type="button" class="w-1/5 mx-auto block text-md md:text-lg bg-orange-500 rounded-lg text-white py-3 my-4 hover:bg-orange-400">See more</button>
                                </a>
                            </div>
                        </div>
                    </c:if>

                    <c:if test="${not empty sessionScope.holoMerchHome}">
                        <h2 class="text-xl font-bold relative py-3 text-center bg-gradient-to-r from-yellow-500 via-amber-500 to-orange-500 text-white ">
                            Hololive
                        </h2>
                        <!--Loop through product list-->
                        <div class="w-full mt-4">
                            <div class="gap-4 w-full overflow-x-auto">
                                <div class="grid grid-flow-col auto-cols-max gap-4 min-w-max">
                                    <c:forEach var="currentProduct" items="${sessionScope.holoMerchHome}">
                                        <c:set var="currentProduct" value="${currentProduct}" scope="request"/>
                                        <jsp:include page="productCard.jsp"/>
                                    </c:forEach>
                                </div>
                            </div>
                            <div class="w-full">
                                <a href="series?id=1">
                                    <button type="button" class="w-1/5 mx-auto block text-md md:text-lg bg-orange-500 rounded-lg text-white py-3 my-4 hover:bg-orange-400">See more</button>
                                </a>
                            </div>
                        </div>
                    </c:if>

                    <c:if test="${empty sessionScope.animeBookHome && empty sessionScope.holoMerchHome}">
                        <h2 class="text-xl font-bold relative py-3 text-center bg-gradient-to-r from-yellow-500 via-amber-500 to-orange-500 text-white ">
                            Trending
                        </h2>
                        <div class="flex justify-center items-center h-40 w-full">
                            <p class="text-gray-500 italic"> Our trending products are sold out! Check back soon!</p>
                        </div>
                    </c:if>

                </div>

                <!--New Release-->
                <div class="bg-white mb-8">
                    <h2 class="text-xl font-bold relative py-3 text-center bg-gradient-to-r from-yellow-500 via-amber-500 to-orange-500 text-white ">
                        New Release
                    </h2>

                    <!--Loop through product list-->
                    <c:if test="${not empty sessionScope.newBookHome}">
                        <div class="w-full mt-4">
                            <div class="gap-4 w-full overflow-x-auto">
                                <div class="grid grid-flow-col auto-cols-max gap-4 min-w-max">
                                    <c:forEach var="currentProduct" items="${sessionScope.newBookHome}">
                                        <c:set var="currentProduct" value="${currentProduct}" scope="request"/>
                                        <jsp:include page="productCard.jsp"/>
                                    </c:forEach>
                                </div>
                            </div>
                            <div class="w-full">
                                <a href="new?type=book">
                                    <button type="button" class="w-1/5 mx-auto block text-md md:text-lg bg-orange-500 rounded-lg text-white py-3 my-4 hover:bg-orange-400">See more</button>
                                </a>
                            </div>
                        </div>
                    </c:if>

                    <c:if test="${not empty sessionScope.newMerchHome && not empty sessionScope.newBookHome}">
                        <div class="h-1 w-full mb-8 bg-gradient-to-r from-yellow-500 via-amber-500 to-orange-500"></div>
                    </c:if>

                    <!--Loop through product list-->
                    <c:if test="${not empty sessionScope.newMerchHome}">
                        <div class="w-full">
                            <div class="gap-4 w-full overflow-x-auto">
                                <div class="grid grid-flow-col auto-cols-max gap-4 min-w-max">
                                    <c:forEach var="currentProduct" items="${sessionScope.newMerchHome}">
                                        <c:set var="currentProduct" value="${currentProduct}" scope="request"/>
                                        <jsp:include page="productCard.jsp"/>
                                    </c:forEach>
                                </div>
                            </div>
                            <div class="w-full">
                                <a href="new?type=merch">
                                    <button type="button" class="w-1/5 mx-auto block text-md md:text-lg bg-orange-500 rounded-lg text-white py-3 my-4 hover:bg-orange-400">See more</button>
                                </a>
                            </div>
                        </div>
                    </c:if>

                    <c:if test="${empty sessionScope.newMerchHome && empty sessionScope.newBookHome}">
                        <div class="flex justify-center items-center h-40 w-full">
                            <p class="text-gray-500 italic">No new releases available right now. More exciting products coming soon!</p>
                        </div>
                    </c:if>

                </div>

                <!--On Sale-->
                <div class="bg-white mb-8">
                    <h2 class="text-xl font-bold relative py-3 text-center bg-gradient-to-r from-yellow-500 via-amber-500 to-orange-500 text-white ">
                        On Sale
                    </h2>
                    <!--Loop through product list-->
                    <c:if test="${not empty sessionScope.saleBookHome}">
                        <div class="w-full mt-4">
                            <div class="gap-4 w-full overflow-x-auto">
                                <div class="grid grid-flow-col auto-cols-max gap-4 min-w-max">
                                    <c:forEach var="currentProduct" items="${sessionScope.saleBookHome}">
                                        <c:set var="currentProduct" value="${currentProduct}" scope="request"/>
                                        <jsp:include page="productCard.jsp"/>
                                    </c:forEach>
                                </div>
                            </div>
                            <div class="w-full">
                                <a href="sale?type=book">
                                    <button type="button" class="w-1/5 mx-auto block text-md md:text-lg bg-orange-500 rounded-lg text-white py-3 my-4 hover:bg-orange-400">See more</button>
                                </a>
                            </div>
                        </div>
                    </c:if>

                    <c:if test="${not empty sessionScope.saleMerchHome && not empty sessionScope.saleBookHome}">
                        <div class="h-1 w-full mb-8 bg-gradient-to-r from-yellow-500 via-amber-500 to-orange-500"></div>
                    </c:if>

                    <!--Loop through product list-->
                    <c:if test="${not empty sessionScope.saleMerchHome}">
                        <div class="w-full">
                            <div class="gap-4 w-full overflow-x-auto">
                                <div class="grid grid-flow-col auto-cols-max gap-4 min-w-max">
                                    <c:forEach var="currentProduct" items="${sessionScope.saleMerchHome}">
                                        <c:set var="currentProduct" value="${currentProduct}" scope="request"/>
                                        <jsp:include page="productCard.jsp"/>
                                    </c:forEach>
                                </div>
                            </div>
                            <div class="w-full">
                                <a href="sale?type=merch">
                                    <button type="button" class="w-1/5 mx-auto block text-md md:text-lg bg-orange-500 rounded-lg text-white py-3 my-4 hover:bg-orange-400">See more</button>
                                </a>
                            </div>
                        </div>
                    </c:if>

                    <c:if test="${empty sessionScope.saleMerchHome && empty sessionScope.saleBookHome}">
                        <div class="flex justify-center items-center h-40 w-full">
                            <p class="text-gray-500 italic">All discounted items are sold out! Stay tuned for the next sale.</p>
                        </div>
                    </c:if>

                </div>


                <!--Popup unauthorized users-->
                <c:if test="${empty sessionScope.account or sessionScope.account.getRole() ne 'customer'}">
                    <jsp:include page="popuplogin.jsp"/>
                </c:if>


            </main>
        </div>
        <jsp:include page="footer.jsp"/>
        <jsp:include page="chat.jsp"/>
        <!-- Nút mở chat -->
        <!-- Chat Button -->
        <div class="fixed bottom-4 left-4 z-50">
            <button id="openChat1" class="bg-yellow-500 text-white px-4 py-2 rounded-full shadow-lg hover:bg-yellow-600 transition">
                🤖 WIBOOKS AI
            </button>
        </div>

        <!-- Chat Popup -->
        <div id="chatPopup1" class="fixed bottom-16 left-4 bg-white rounded-lg shadow-xl border w-[400px] hidden zindex-10000">
            <div class="flex justify-between items-center bg-orange-500 text-white px-4 py-2 rounded-t-lg">
                <span>WIBOOKS AI</span>
                <span id="closeChatAI1" class="cursor-pointer text-xl">❌</span>
            </div>
            <div class="flex-grow overflow-y-auto border p-2 bg-gray-200 rounded-lg shadow-lg">
                <jsp:include page="chatAI.jsp"/>
            </div>
        </div>


        <script>

            document.addEventListener('DOMContentLoaded', function () {

                document.getElementById("openChat1").addEventListener("click", function () {
                    document.getElementById("chatPopup1").classList.remove("hidden");
                });

                document.getElementById("closeChatAI1").addEventListener("click", function () {
                    document.getElementById("chatPopup1").classList.add("hidden");
                });

//                document.getElementById("openChat").addEventListener("click", function () {
//                    document.getElementById("chatPopup").classList.remove("hidden");
//                });
//
//                document.getElementById("closeChatAI").addEventListener("click", function () {
//                    document.getElementById("chatPopup").classList.add("hidden");
//                });

            });


//            Pop-up message
            document.addEventListener('DOMContentLoaded', function () {
                const reqMessage = `${requestScope.message}`;
                if (reqMessage) {
                    Swal.fire({
                        icon: 'warning',
                        text: reqMessage
                    });
                }
            });



        </script>

    </body>
</html>