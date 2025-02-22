/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


function toggleAccountMenu() {
    const menu = document.getElementById('accountMenu');
    menu.style.display = (menu.style.display === 'none' || menu.style.display === '') ? 'flex' : 'none';
}

document.addEventListener("DOMContentLoaded", function () {
    const topBar = document.querySelector(".top-bar");
    const logo = document.querySelector(".logo");

    function handleScroll() {
        if (window.innerWidth > 768) { // Only apply for desktop screens
            if (window.scrollY > logo.offsetHeight) {
                topBar.style.position = "fixed";
                topBar.style.top = "0";
                topBar.style.width = "100%";
                topBar.style.zIndex = "1000";
                topBar.style.boxShadow = "0px 4px 6px rgba(0, 0, 0, 0.1)";
            } else {
                topBar.style.position = "relative";
                topBar.style.boxShadow = "none";
            }
        } else {
            // Reset styles for smaller screens (mobile/tablet)
            topBar.style.position = "relative";
            topBar.style.boxShadow = "none";
        }
    }

    window.addEventListener("scroll", handleScroll);
    window.addEventListener("resize", handleScroll); // Adjust when resizing
});

