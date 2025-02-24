/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

document.addEventListener("DOMContentLoaded", function () {
    // Select all elements with prices
    let priceElements = document.querySelectorAll(".price-area p");

    priceElements.forEach(priceEl => {
        let priceText = priceEl.innerText.trim(); // Get the text inside span
        let price = parseFloat(priceText.replaceAll(" VND", "").replaceAll(",", ""));
        console.log("formatted price: ", price);

        if (!isNaN(price)) {
            // Format price with commas (e.g., 4,400 VND)
            priceEl.innerText = new Intl.NumberFormat("en-US").format(price) + " VND";
        }
    });
});

document.addEventListener("DOMContentLoaded", function () {
    let currentURLs = document.querySelectorAll(".currentURL");

    let url = window.location.href;
    if (currentURLs) {
        currentURLs.forEach(function (hiddenInput) {
            hiddenInput.value = url;
        });
    }

    console.log("url", url);
});
