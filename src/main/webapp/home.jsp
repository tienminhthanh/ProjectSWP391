<%@page import="java.io.FilenameFilter"%>
<%@page import="java.io.File"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" errorPage ="error.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>                                                    
<fmt:setLocale value="en_US"/>
<%
    String bannerPath = application.getRealPath("/img/banner_event/");
    File folder = new File(bannerPath);
    String[] files = folder.list(new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            return name.matches(".*\\.(jpg|jpeg|png|gif)");
        }
    });
%>
<html lang="en">
    <head>
        <meta charset="utf-8"/>
        <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
        <title>
            WIBOOKS - More Than Just Books
        </title>

        <!--Script for include icons-->
        <script src="https://kit.fontawesome.com/bfab6e6450.js" crossorigin="anonymous"></script>


        <!--Unknown import-->
        <!--<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet"/>-->

        <link rel="stylesheet" href="css/styleHome.css"/>
        <!--Header css-->
        <link href="css/styleHeader.css" rel="stylesheet">

        <!--Footer css-->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous" />
        <link href="css/styleFooter.css" rel="stylesheet">

        <!--Customer Sidebar-->
        <link href="css/styleCustomerSidebar.css" rel="stylesheet">

        <!--Product card css-->
        <link rel="stylesheet" href="css/styleProductCard.css"/>

        <!--Banner carousel-->
        <script src="https://cdn.jsdelivr.net/npm/alpinejs@3.x.x/dist/cdn.min.js" defer></script>
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
                <!--Popup message from servlet -->
                <c:if test="${not empty message}">
                    <div class="fixed top-0 left-0 w-full h-full bg-black opacity-50 z-49" id="warning-popup-overlay" onclick="closeMessagePopup()"></div>
                    <div class="fixed top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 bg-white p-4 shadow-md border-2 text-center z-50 rounded-lg" id="warning-popup-container">
                        <p>
                            ${message}
                        </p>
                        <button onclick="closeMessagePopup()" class="mt-2 p-2 mx-4 text-white border-0 cursor-pointer bg-gray-300 hover:bg-gray-400 rounded-lg">Close</button>
                    </div>
                </c:if>
                    
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

                <!--Popup message from servlet -->
                <c:if test="${not empty message}">
                    <div class="fixed top-0 left-0 w-full h-full bg-black opacity-50 z-49" id="warning-popup-overlay" onclick="closeMessagePopup()"></div>
                    <div class="fixed top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 bg-white p-4 shadow-md border-2 text-center z-50 rounded-lg" id="warning-popup-container">
                        <p>
                            ${message}
                        </p>
                        <button onclick="closeMessagePopup()" class="mt-2 p-2 mx-4 text-white border-0 cursor-pointer bg-gray-300 hover:bg-gray-400 rounded-lg">Close</button>
                    </div>
                </c:if>
            </main>
        </div>
        <jsp:include page="footer.jsp"/>
        <jsp:include page="chat.jsp"/>
        <!-- Nút mở chat -->
        <!-- Chat Button -->
        <div class="fixed bottom-4 left-4 z-50">
            <button id="openChat" class="bg-yellow-500 text-white px-4 py-2 rounded-full shadow-lg hover:bg-yellow-600 transition">
                🤖 WIBOOKS AI
            </button>
        </div>

        <!-- Chat Popup -->
        <div id="chatPopup" class="fixed bottom-16 left-4 bg-white rounded-lg shadow-xl border w-[400px] hidden">
            <div class="flex justify-between items-center bg-orange-500 text-white px-4 py-2 rounded-t-lg">
                <span>WIBOOKS AI</span>
                <span id="closeChatAI" class="cursor-pointer text-xl">❌</span>
            </div>
            <div class="flex-grow overflow-y-auto border p-2 bg-gray-200 rounded-lg shadow-lg">
                <jsp:include page="chatAI.jsp"/>
            </div>
        </div>

        <script>
            document.getElementById("openChat").addEventListener("click", function () {
                document.getElementById("chatPopup").classList.remove("hidden");
            });

            document.getElementById("closeChatAI").addEventListener("click", function () {
                document.getElementById("chatPopup").classList.add("hidden");
            });
        </script>


        
        <!--Script for include icons-->
        <script src="https://kit.fontawesome.com/bfab6e6450.js" crossorigin="anonymous"></script>

        <!--Header script-->
        <script src="js/scriptHeader.js"></script>

        <!--Footer script-->

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.min.js"
                integrity="sha384-BBtl+eGJRgqQAUMxJ7pMwbEyER4l1g+O15P+16Ep7Q9Q+zqX6gSbd85u4mG4QzX+"
        crossorigin="anonymous"></script>

        <!--Customer sidebar script-->
        <script src="js/scriptCusSidebar.js"></script>
        <script src="js/scriptCusSideBarNOTDetails.js"></script>

        <!--Unknown import-->
        <!--        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js"
                        integrity="sha384-I7E8VVD/ismYTF4hNIPjVp/Zjvgyol6VFvRkX/vR+Vc4jQkC+hVqc2pM8ODewa9r"
                crossorigin="anonymous"></script>-->

        <!--Tailwind-->
        <script src="https://cdn.tailwindcss.com">
        </script>

        <!--Product Card-->
        <script src="js/scriptProductCard.js"></script>

        <!--Voucher Date End-->
        <script src="js/scriptVoucherDateEnd.js"></script>



        <script>
            document.addEventListener("DOMContentLoaded", function () {
                let banners = [
            <% for (String file : files) {%>
                    {img: "/img/banner_event/<%= file%>"},
            <% }%>
                ];

                let current = 0;
                const bannerImg = document.getElementById("banner-img");
                const bannerLink = document.getElementById("banner-link");
                const dotsContainer = document.getElementById("dots-container");
                const prev_btn = document.getElementById("prev-btn");
                const next_btn = document.getElementById("next-btn");

                function updateBanner() {
                    bannerImg.src = banners[current].img;
                    bannerLink.href = "/eventDetails?banner=" + encodeURIComponent(banners[current].img) + "&action=home";

                    dotsContainer.innerHTML = "";
                    banners.forEach((_, index) => {
                        const dot = document.createElement("div");
                        dot.className = "w-3 h-3 rounded-full cursor-pointer transition-all duration-300 " +
                                (index === current ? "bg-blue-500 scale-125" : "bg-gray-300");
                        dot.onclick = () => {
                            current = index;
                            updateBanner();
                        };
                        dotsContainer.appendChild(dot);
                    });
                }

                function next() {
                    current = (current + 1) % banners.length;
                    updateBanner();
                }

                function prev() {
                    current = (current - 1 + banners.length) % banners.length;
                    updateBanner();
                }

                if (prev_btn)
                    prev_btn.addEventListener("click", prev);
                if (next_btn)
                    next_btn.addEventListener("click", next);

                // Chỉ gọi setInterval một lần duy nhất
                setInterval(next, 3000);

                updateBanner();
            });

            function closeMessagePopup() {
                document.getElementById('warning-popup-overlay').classList.add("hidden");
                document.getElementById('warning-popup-container').classList.add("hidden");
            }

        </script>

    </body>
</html>
