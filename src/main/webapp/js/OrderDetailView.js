
/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */
document.addEventListener("DOMContentLoaded", function () {
    // Hiển thị form cập nhật
    window.showUpdateForm = function () {
        document.getElementById("updateForm").classList.remove("hidden");
    };

    // Ẩn form cập nhật
    window.hideUpdateForm = function () {
        document.getElementById("updateForm").classList.add("hidden");
    };

    // Hiển thị popup đánh giá
    window.openRatingPopup = function (productID, orderID, productName, imageURL) {
        document.getElementById("ratingPopup").classList.remove("hidden");

        // Cập nhật thông tin sản phẩm trong popup
        document.getElementById("popupProductID").value = productID;
        document.getElementById("popupOrderID").value = orderID;
        document.getElementById("popupProductName").textContent = productName;
        document.getElementById("popupProductImage").src = imageURL;

        // Reset đánh giá sao
        document.querySelectorAll("#starContainer span").forEach(star => {
            star.classList.remove("text-yellow-500");
            star.classList.add("text-gray-400");
        });

        document.getElementById("rating").value = 0;
    };

    // Đóng popup đánh giá
    window.closeRatingPopup = function (event) {
        if (!event || event.target.id === "ratingPopup") {
            document.getElementById("ratingPopup").classList.add("hidden");
        }
    };

    // Xử lý chọn sao đánh giá
    document.querySelectorAll("#starContainer span").forEach(star => {
        star.addEventListener("click", function () {
            let rating = this.getAttribute("data-star");
            document.getElementById("rating").value = rating;

            // Cập nhật màu sao
            document.querySelectorAll("#starContainer span").forEach(s => {
                s.classList.toggle("text-yellow-500", s.getAttribute("data-star") <= rating);
                s.classList.toggle("text-gray-400", s.getAttribute("data-star") > rating);
            });
        });
    });


    // Hiển thị popup cảm ơn sau khi đánh giá
    window.showThankYouPopup = function (productID) {
        document.getElementById("ratingPopup").classList.add("hidden"); // Ẩn popup đánh giá
        document.getElementById("thankYouPopup").classList.remove("hidden"); // Hiển thị popup cảm ơn

        // Sau 2 giây, popup cảm ơn sẽ tự động ẩn
        setTimeout(() => {
            document.getElementById("thankYouPopup").classList.add("hidden");
        }, 2000);

        return false; // Ngăn form submit ngay lập tức
    };
    // Mở popup Review
    window.openReviewPopup = function (productID, orderID, productName, imageURL) {
        document.getElementById("reviewPopup").classList.remove("hidden");

        // Cập nhật thông tin sản phẩm trong popup Review
        document.getElementById("reviewProductID").value = productID;
        document.getElementById("reviewOrderID").value = orderID;
        document.getElementById("reviewProductName").textContent = productName;
        document.getElementById("reviewProductImage").src = imageURL;
    };

    // Đóng popup Review
    window.closeReviewPopup = function (event) {
        if (!event || event.target.id === "reviewPopup") {
            document.getElementById("reviewPopup").classList.add("hidden");
        }
    };

    // Xử lý form gửi Review
    window.submitReview = function () {
        let reviewContent = document.getElementById("reviewContent").value;

        if (reviewContent.trim() === "") {
            alert("Please write a review.");
            return false;
        }

        // Có thể thực hiện gửi form hoặc AJAX để gửi thông tin review
        document.getElementById("reviewForm").submit();
        return false;
    };

});
