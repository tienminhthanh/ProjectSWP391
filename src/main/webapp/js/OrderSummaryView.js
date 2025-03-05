/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


document.addEventListener("DOMContentLoaded", function () {
    // Lấy các phần tử cần thao tác
    const voucherSelect = document.getElementById("voucherSelect");
    const shippingOptions = document.querySelectorAll("input[name='shippingOption']");
    const subtotalElement = document.getElementById("subtotal");
    const discountElement = document.getElementById("discount");
    const shippingFeeElement = document.getElementById("shippingFee");
    const totalAmountElement = document.getElementById("totalAmount");

    // Chuyển đổi giá trị từ chuỗi thành số
    let subtotal = parseFloat(subtotalElement.innerText.replace(/,/g, "")) || 0;
    let discount = 0;
    let shippingFee = 50000; // Mặc định Express Delivery (30,000 VND)

    // Hàm cập nhật tổng tiền
    function updateTotal() {
        let total = subtotal - discount + shippingFee;
        if (total < 0)
            total = 0; // Không cho tổng tiền âm

        // Cập nhật giao diện
        discountElement.innerText = discount.toLocaleString();
        shippingFeeElement.innerText = shippingFee.toLocaleString();
        totalAmountElement.innerText = total.toLocaleString();
    }

    // Sự kiện khi chọn voucher
    voucherSelect.addEventListener("change", function () {
        discount = parseFloat(this.options[this.selectedIndex].dataset.discount) || 0;
        updateTotal();
    });

    // Sự kiện khi chọn phương thức vận chuyển
    shippingOptions.forEach(option => {
        option.addEventListener("change", function () {
            shippingFee = parseFloat(this.dataset.cost) || 0;
            updateTotal();
        });
    });

    // Cập nhật tổng tiền lần đầu khi trang tải
    updateTotal();
});
