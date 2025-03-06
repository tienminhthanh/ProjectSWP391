/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


function updateVoucher() {
    const select = document.getElementById('voucherSelect');
    const selectedOption = select.options[select.selectedIndex];
    
    const voucherCode = selectedOption.value;
    const discountValue = selectedOption.getAttribute('data-value') || 0;
    
    // Cập nhật hiển thị
    document.getElementById('selectedVoucher').textContent = voucherCode 
        ? `${voucherCode} (-${new Intl.NumberFormat().format(discountValue)}₫)`
        : "Không có";
    
    // Cập nhật giá trị ẩn
    document.getElementById('voucherCode').value = voucherCode;
    document.getElementById('voucherValue').value = discountValue;
    
    // Optional: Tính toán lại tổng tiền
    updateOrderTotal();
}

function updateOrderTotal() {
    // Thêm logic tính toán lại tổng tiền ở đây nếu cần
}
function submitVoucher() {
    document.getElementById("voucherForm").submit();
}
