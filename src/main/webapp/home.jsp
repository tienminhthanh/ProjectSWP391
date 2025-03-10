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
                <!--Div1-->
                <div class="mb-4 bg-white popular-search-area">
                    <h2 class="text-xl font-bold relative pt-4 pb-4 text-center">
                        Popular Searches
                    </h2>
                    <div class="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-3 p-2">
                        <button class="w-48 h-12 bg-orange-500 text-white p-2 rounded">
                            On Sale Merch
                        </button>
                        <button class="w-48 h-12 bg-orange-500 text-white p-2 rounded">
                            Top Merch
                        </button>
                        <button class="w-48 h-12 bg-orange-500 text-white p-2 rounded">
                            On Sale Books
                        </button>
                        <button class="w-48 h-12 bg-orange-500 text-white p-2 rounded">
                            Top Books
                        </button>
                        <button class="w-48 h-12 bg-orange-500 text-white p-2 rounded">
                            Seasonal Anime - Merch
                        </button>
                        <button class="w-48 h-12 bg-orange-500 text-white p-2 rounded">
                            Seasonal Anime - Books
                        </button>
                    </div>
                </div>

                <!--Div2-->
                <div class="mb-4 bg-white voucher-area">
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

                <!--Random Pick-->
                <div class="bg-white">
                    <h2 class="text-xl font-bold relative pt-4 pb-4 text-center border-t-4 border-orange-300">
                        Lucky Books
                    </h2>
                    <!--Loop through product list-->
                    <div class="w-full">
                        <div class="gap-4 w-full overflow-x-auto">
                            <div class="grid grid-flow-col auto-cols-max gap-4 min-w-max">
                                <c:forEach var="currentProduct" items="${productList}">
                                    <c:set var="currentProduct" value="${currentProduct}" scope="request"/>
                                    <jsp:include page="productCard.jsp"/>
                                </c:forEach>
                            </div>
                        </div>
                    </div>

                </div>
                <!--Popup unauthorized users-->
                <c:if test="${empty sessionScope.account or sessionScope.account.getRole() != 'customer'}">
                    <jsp:include page="popuplogin.jsp"/>
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
        <div id="chatPopup" class="z-50 fixed bottom-16 left-4 bg-white rounded-lg shadow-xl border w-[400px] hidden">
            <div class="flex justify-between items-center bg-blue-500 text-white px-4 py-2 rounded-t-lg">
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

                const prev_btn = document.getElementById("prev-btn");
                if (prev_btn !== null) {
                    prev_btn.addEventListener("click", prev);

                    setInterval(next, 3000);
                }
                const next_btn = document.getElementById("next-btn");
                if (next_btn !== null) {
                    next_btn.addEventListener("click", next);

                    setInterval(next, 3000);
                }

                updateBanner();
            });

        </script>

    </body>
</html>
