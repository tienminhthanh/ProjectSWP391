/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

//Format price display
document.addEventListener("DOMContentLoaded", function () {
    // Select all elements with prices
    let priceElements = document.querySelectorAll(".price-area p");

    priceElements.forEach(priceEl => {
        let priceText = priceEl.innerText.trim(); // Get the text inside span
        let price = parseFloat(priceText.replaceAll(" VND", "").replaceAll(",", ""));
        console.log("formatted price: ", price);

        if (!isNaN(price)) {
            // Format price with commas (e.g., 4,400 VND)
            priceEl.innerText = new Intl.NumberFormat("en-US").format(price) + " đ";
        }
    });
});

//Get purchaseQuanity onload
document.addEventListener("DOMContentLoaded", function () {
    let numberValue = document.getElementById("quantityInput"); // Get the value from the number input
    let hiddenInputs = document.querySelectorAll(".quantity"); // Select all inputs with class "quantity"

    // Loop through all hidden inputs and update their values
    hiddenInputs.forEach(function (hiddenInput) {
        hiddenInput.value = numberValue.value;
    });

    // Optional: Display the values for verification
    let displayValues = Array.from(hiddenInputs).map(input => input.value).join(", ");
    console.log("quantity:", displayValues);
});

//Get purchaseQuantity oninput
document.getElementById("quantityInput").addEventListener("input", function (event) {
    let numberValue = event.target.value; // Get the value from the number input
    let hiddenInputs = document.querySelectorAll(".quantity"); // Select all inputs with class "quantity"

    // Loop through all hidden inputs and update their values
    hiddenInputs.forEach(function (hiddenInput) {
        hiddenInput.value = numberValue;
    });

    // Optional: Display the values for verification
    let displayValues = Array.from(hiddenInputs).map(input => input.value).join(", ");
    console.log("quantity:", displayValues);
});




////Close sidebar on resize
//window.addEventListener('resize', () => {
//    const clientWidth = document.documentElement.clientWidth;
//    const sidebar = document.getElementById('cus-sidebar');
//    sidebar.style.display = 'none';
//});
