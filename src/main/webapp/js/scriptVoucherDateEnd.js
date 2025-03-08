/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */
document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".voucher").forEach(function (voucher) {
        let startDate = new Date(voucher.dataset.start);
        let duration = parseInt(voucher.dataset.duration);
        let dateEnd = new Date(startDate);
        dateEnd.setDate(startDate.getDate() + duration);

        voucher.querySelector(".date-end").textContent = dateEnd.toISOString().split("T")[0];
    });
});


