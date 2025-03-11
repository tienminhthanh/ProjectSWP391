<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" errorPage ="error.jsp"%>
<%@ page import="dao.EventDAO" %>
<%@ page import="java.util.List" %>
<%
    EventDAO eventDAO = new EventDAO(); // Khởi tạo đối tượng DAO
    List<String> banners = eventDAO.getBannerEvent(); // Lấy danh sách banner từ SQL
%>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        let banners = [
    <% for (String banner : banners) {%>
            {img: "<%= banner%>"},
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

        setInterval(next, 3000);

        updateBanner();
    });
</script>
<!-- Phần HTML giữ nguyên -->
<div class="relative w-full h-64 overflow-hidden">
    <form id="banner-form" action="#" method="GET">
        <input type="hidden" name="action" value="home">
        <a id="banner-link" href="eventDetails">
            <img id="banner-img" class="w-full h-full object-cover transition-opacity duration-500 cursor-pointer">
        </a>
    </form>
    <button id="prev-btn" class="absolute left-0 top-1/2 transform -translate-y-1/2 bg-gray-800 text-white p-2 rounded-full">
        ⬅
    </button>
    <button id="next-btn" class="absolute right-0 top-1/2 transform -translate-y-1/2 bg-gray-800 text-white p-2 rounded-full">
        ➡
    </button>
    <div id="dots-container" class="absolute bottom-2 left-1/2 transform -translate-x-1/2 flex space-x-2"></div>
</div>