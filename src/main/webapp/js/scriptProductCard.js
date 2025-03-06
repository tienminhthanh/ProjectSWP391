/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */
document.addEventListener("DOMContentLoaded", function () {
    // Select all elements with prices
    let priceElements = document.querySelectorAll(".product-price span");

    priceElements.forEach(priceEl => {
        let priceText = priceEl.innerText.trim(); // Get the text inside span
        let price = parseFloat(priceText.replaceAll(" VND", "").replaceAll(",", ""));
        console.log("formatted price: ", price);

        if (!isNaN(price)) {
            // Format price with commas (e.g., 4,400 VND)
            priceEl.innerText = new Intl.NumberFormat("en-US").format(price) + " đ";
        }
    });

    const dateElements = document.querySelectorAll('.fomo-info>span');
    if (!dateElements) {
        console.log("date elements not found!");
        return;
    }

    dateElements.forEach(function (dateEl) {
        const date = new Date(dateEl.innerText);

        if (date === null) {
            console.log("invalid date format");
            return;
        }

        dateEl.innerText = date.toLocaleDateString("vi-VN");
        console.log(dateEl.innerText);
    });

});




