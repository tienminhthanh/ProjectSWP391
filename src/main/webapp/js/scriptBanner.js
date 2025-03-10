/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */
document.addEventListener("DOMContentLoaded", function () {
    let current = 0;
    const bannerImg = document.getElementById("banner-img");
    const dotsContainer = document.getElementById("dots-container");

    function updateBanner() {
        bannerImg.src = banners[current].img;
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


