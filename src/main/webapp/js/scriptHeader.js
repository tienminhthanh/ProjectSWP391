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



document.addEventListener("DOMContentLoaded", function () {
    let loginLinks = document.querySelectorAll(".loginLinks");
    if (loginLinks) {
        loginLinks.forEach(link => {
            let currentURL = encodeURIComponent(window.location.href);
            link.href = "login?currentURL=" + currentURL;
        });
    }
});


function openCustomerMenu() {
    const customerMobileMenu = document.getElementById('customer-menu-mobile');
    const menuOverlay = document.getElementById('cus-menu-overlay');
    customerMobileMenu.style.display = 'block';
    menuOverlay.style.display = 'block';


}
function closeCustomerMenu() {
    const customerMobileMenu = document.getElementById('customer-menu-mobile');
    const menuOverlay = document.getElementById('cus-menu-overlay');
    customerMobileMenu.style.display = 'none';
    menuOverlay.style.display = 'none';

}


window.addEventListener('resize', () => {
    const clientWidth = document.documentElement.clientWidth;
    const menu = document.getElementById('customer-menu-mobile');
    const overlay1 = document.querySelector('#cus-menu-overlay');
    const overlay2 = document.querySelector('#cus-sidebar-overlay');
    if (overlay1) {
        overlay1.style.display = 'none';
    }
    if (overlay2) {
        overlay2.style.display = 'none';
    }
    if (menu) {
        menu.style.display = 'none';

    }
});
