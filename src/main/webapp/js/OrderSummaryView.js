document.addEventListener("DOMContentLoaded", function () {
    const voucherSelect = document.getElementById("voucherSelect"),
            discountElement = document.getElementById("discount"),
            totalAmountElement = document.getElementById("totalAmount"),
            subtotalElement = document.getElementById("subtotal"),
            shippingFeeElement = document.getElementById("shippingFee"),
            shippingOptions = document.querySelectorAll("input[name='shippingOption']");

    function getNumberFromElement(element) {
        return element
                ? parseFloat(element.innerText.replace(/[^\d]/g, "")) || 0
                : 0;
    }


    function formatCurrency(value) {
        return value.toLocaleString("vi-VN", {minimumFractionDigits: 0}).replace(/\./g, ",") + " đ";
    }

    function updateTotalAmount() {
        let subtotal = getNumberFromElement(subtotalElement),
                selectedVoucher = voucherSelect.options[voucherSelect.selectedIndex],
                discount = (selectedVoucher && selectedVoucher.value !== "0")
                ? parseFloat(selectedVoucher.dataset.discount) || 0
                : 0,
                selectedShipping = document.querySelector("input[name='shippingOption']:checked"),
                shippingFee = selectedShipping ? parseFloat(selectedShipping.dataset.cost) || 0 : 0,
                total = Math.max(0, subtotal + shippingFee - discount);

        if (discountElement)
            discountElement.innerText = formatCurrency(discount);
        if (shippingFeeElement)
            shippingFeeElement.innerText = formatCurrency(shippingFee);
        if (totalAmountElement)
            totalAmountElement.innerText = formatCurrency(total);
    }

    if (voucherSelect)
        voucherSelect.addEventListener("change", updateTotalAmount);

    shippingOptions.forEach(option => {
        option.addEventListener("change", updateTotalAmount);
    });

    const defaultShippingOption = document.querySelector("input[name='shippingOption'][value='1']");
    if (defaultShippingOption) {
        defaultShippingOption.checked = true;
    }

    updateTotalAmount();
   
});
