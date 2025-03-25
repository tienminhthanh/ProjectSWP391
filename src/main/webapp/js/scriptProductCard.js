/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */
document.addEventListener("DOMContentLoaded", function () {
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

        dateEl.innerText = new Intl.DateTimeFormat('vi-VN', {day: '2-digit', month: '2-digit', year: 'numeric'}).format(date);
    });

});



//Map final price to forms
document.addEventListener("DOMContentLoaded", function () {
    const finalPriceElements = document.querySelectorAll(".discount-price");
    let pricesToSubmit = document.querySelectorAll("input[name='priceWithQuantity']");

    if (!finalPriceElements) {
        console.log("Cannot retrieve product price!");
        return;
    }
    
    if(!pricesToSubmit){
        console.log("Fail to retrieve hidden price input!");
        return;
        
    }

    for (var i = 0; i < finalPriceElements.length; i++) {
        let priceText = finalPriceElements[i].innerText;
        let priceNumber = parseFloat(priceText.replace(/[^0-9]/g, ""));
        if (isNaN(priceNumber)) {
            console.log("Price is not a number");
            return;
        }
        pricesToSubmit[i].value = priceNumber;
    }


});


function checkStockCard(cartQuantity, stockCount, event) {
    let quantityToAdd = 1; 
    if (cartQuantity + quantityToAdd > stockCount) {
        Swal.fire({
            icon: 'error',
            title: 'Stock Limit Reached',
            text: `The quantity in your cart has reached the stock limit. The selected quantity cannot be added to the cart because it exceeds your purchasing limit.`
        });
        event.preventDefault();
        return false;
    }
    return true;
}