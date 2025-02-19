<%-- 
    Document   : productCard
    Created on : Feb 19, 2025, 11:22:14 AM
    Author     : anhkc
--%>

<!--<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Product Card</title>
        <style>
            .product-card {
                width: 220px;
                border: 1px solid #ddd;
                border-radius: 10px;
                overflow: hidden;
                background-color: white;
                position: relative;
                text-align: center;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                transition: transform 0.2s ease-in-out;
                padding-bottom: 0px; /* Ensure enough space */
            }

            .product-card:hover {
                transform: scale(1.05);
            }

            /* Sale Label (Polygon) */
            .sale-label {
                position: absolute;
                top: 10px;
                left: 10px;
                background: red;
                color: white;
                padding: 5px 10px;
                font-size: 14px;
                font-weight: bold;
                clip-path: polygon(0% 0%, 100% 0%, 80% 100%, 0% 100%);
                z-index: 3;
            }

            /* New/Pre-order Ribbon */
            .ribbon {
                position: absolute;
                color: white;
                font-weight: bold;
                transform: rotate(45deg);
                z-index: 3;
                text-align: center;
            }
            
            .ribbon-new{
                background: orange;
                top: 10px;
                right: -40px;
                padding: 10px 50px;
                font-size: 15px;
            }
            .ribbon-pre{
                background: blue;
                top: 13px;
                right: -30px;
                padding: 10px 30px;
                font-size: 10px;
            }
            
                

            /* Product Image */
            .product-image {
                display: block;
                position: relative;
            }

            .product-image img {
                width: 100%;
                height: auto;
                border-bottom: 1px solid #ddd;
            }

            .product-image .hover-name {
                display: none;
                position: absolute;
                bottom: 0;
                left: 0;
                width: 100%;
                background: rgba(0, 0, 0, 0.7);
                color: white;
                font-size: 14px;
                padding: 5px;
            }

            .product-image:hover .hover-name {
                display: block;
            }

            /* Category/Brand */
            .product-category {
                font-size: 12px;
                color: #555;
                margin: 5px 0;
            }

            /* Product Name (Spanning 3 lines max) */
            .product-title {
                font-size: 16px;
                font-weight: bold;
                color: #333;
                text-decoration: none;
                display: -webkit-box;
                -webkit-line-clamp: 3;
                -webkit-box-orient: vertical;
                overflow: hidden;
                text-overflow: ellipsis;
                padding: 0 10px;
                height: 60px; /* Maintain spacing for 3 lines */
            }

            /* Fomo info */
            .fomo-info {
                font-size: 13px;
                margin: 5px 0;
            }
            .sale-expiry{
                color: red;
                
            }
            .release-date{
                color:blue;
            }

            /* Price */
            .product-price {
                font-size: 18px;
                color: red;
                font-weight: bold;
            }

            .original-price {
                font-size: 16px;
                color: #888;
                margin-left: 5px;
                text-decoration: line-through;
            }

            /* Add to Cart Icon */
            .add-to-cart {
                display: none;
                position: absolute;
                top: 50%;
                right: 15px; /* Move closer to the right edge */
                transform: translateY(-50%);
                background: orange;
                color: white;
                border: none;
                padding: 12px;
                border-radius: 50%;
                cursor: pointer;
                font-size: 24px;
            }

            .product-card:hover .add-to-cart {
                display: block;
            }

            /* Out of Stock Overlay */
            .out-of-stock {
                position: absolute;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background: rgba(0, 0, 0, 0.5);
                color: white;
                font-size: 18px;
                font-weight: bold;
                display: flex;
                align-items: center;
                justify-content: center;
                z-index: 3;
                pointer-events: none; /* Allows clicks to go through */
            }

        </style>
    </head>
    <body>
        <div class="product-card">
             Sale label 
            <div class="sale-label" style="display: none;">SALE</div>

             New/Pre-order ribbon 
            <div class="ribbon ribbon-new" style="display: none;">NEW</div>
            <div class="ribbon ribbon-pre" style="display: none;">PRE-ORDER</div>

             Product Image (Clickable) 
            <a href="product-details.html" class="product-image">
                <img src="https://rimg.bookwalker.jp/37666b4a9b408da042427693fbaaf85/OWWPXNVne2Og5o9nA6tp3Q__.jpg" alt="The Hero and the Sage, Reincarnated and Engaged: Volume 1">
                <div class="hover-name">The Hero and the Sage, Reincarnated and Engaged: Volume 1</div>
            </a>

             Category or Brand 
            <p class="product-category">Category / Brand</p>

             Product Name (Clickable) 
            <a href="product-details.html" class="product-title" title="The Hero and the Sage, Reincarnated and Engaged: Volume 1">The Hero and the Sage, Reincarnated and Engaged: Volume 1</a>

             Sale Expiry Date (Only Shown if on Sale) 
            <p class="fomo-info sale-expiry" style="display: none;">Sale ends on: Feb 25</p>
            <p class="fomo-info release-date" style="display: none;">Release on: Feb 25</p>

             Price (With Discount if on Sale) 
            <p class="product-price">
                <span class="discount-price">$15.99</span>
                <span class="original-price">$19.99</span>
            </p>

             Cart Icon (Appears on Hover) 
            <a href="#">
            <button class="add-to-cart"><i class="fa-solid fa-cart-plus"></i></button>
                
            </a>

             Out of Stock Overlay 
            <div class="out-of-stock" style="display: none;">Out of Stock</div>
        </div>
        <script src="https://kit.fontawesome.com/bfab6e6450.js" crossorigin="anonymous"></script>

        <script>
            document.addEventListener("DOMContentLoaded", function () {
                const productCard = document.querySelector(".product-card");
                const cartButton = document.querySelector(".add-to-cart");

                // Example conditions
                let isOnSale = true;
                let isNew = false;
                let isPreOrder = true;
                let isOutOfStock = false;

                // Show sale label if on sale
                if (isOnSale) {
                    document.querySelector(".sale-label").style.display = "block";
                    document.querySelector(".sale-expiry").style.display = "block";
                }

                // Show ribbon if new/pre-order
                if (isNew) {
                    document.querySelector(".ribbon-new").style.display = "block";
                } else if (isPreOrder) {
                    document.querySelector(".ribbon-pre").style.display = "block";
                    document.querySelector(".release-date").style.display = "block";
                }

                // Handle out of stock
                if (isOutOfStock) {
                    document.querySelector(".out-of-stock").style.display = "flex";
                    productCard.style.opacity = "0.7";
                    cartButton.addEventListener("click", function (e) {
                        e.preventDefault();
                        window.location.href = "login.html";
                    });
                }
            });
        </script>
    </body>
</html>-->




<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="product-card w-48">
    
    <!-- Sale Label (Shown only if on sale) -->
    <!--Event related-->
    <c:if test="${currentProduct.onSale}">
        <div class="sale-label">SALE</div>
    </c:if>

    <!-- New / Pre-order Ribbon -->
    <!--Specify from server side-->
    <c:choose>
        <c:when test="${currentProduct.specialFilter == 'new'}">
            <div class="ribbon ribbon-new">NEW</div>
        </c:when>
        <c:when test="${currentProduct.specialFilter == 'pre-order'}">
            <div class="ribbon ribbon-pre">PRE-ORDER</div>
        </c:when>
    </c:choose>

    <!-- Product Image -->
    <a href="productDetails?productID=${currentProduct.productID}" class="product-image">
        <img src="${currentProduct.imageURLList[0]}" alt="${currentProduct.productName}">
        <div class="hover-name">${currentProduct.productName}</div>
    </a>

    <!-- Category / Brand -->
    <c:choose>
        <c:when test="${not empty brandName}">
            <p class="product-category">${brandName}</p>
        </c:when>
        <c:otherwise>
            <p class="product-category">Category</p>
        </c:otherwise>
    </c:choose>

    <!-- Product Name -->
    <a href="productDetails?productID=${currentProduct.productID}" class="product-title">${currentProduct.productName}</a>

    <!-- Sale Expiry Date or Release Date -->
    <c:if test="${currentProduct.onSale}">
        <p class="fomo-info sale-expiry">Sale ends on: ${currentProduct.saleEndDate}</p>
    </c:if>
    <c:if test="${currentProduct.specialFilter == 'pre-order'}">
        <p class="fomo-info release-date">Release on: ${currentProduct.releaseDate}</p>
    </c:if>
    
    <!--Stockcount section-->
    <p class="stock-count">Stock count: ${currentProduct.stockCount}</p>
    
    <!-- Price Section -->
    <p class="product-price">
        <c:choose>
            <c:when test="${currentProduct.onSale}">
                <span class="discount-price">${currentProduct.price * (100 - currentProduct.productDiscount)/100} VND</span>
                <span class="original-price">${currentProduct.price} VND</span>
            </c:when>
            <c:otherwise>
                <span class="discount-price">${currentProduct.price} VND</span>
            </c:otherwise>
        </c:choose>
    </p>
    <!-- Add to Cart Button (Hidden if Out of Stock) -->
    <!-- If out of stock -> overlay -->
    <c:choose>
        <c:when test="${empty sessionScope.account || sessionScope.account.getRole() != 'customer'}">
            <button class="add-to-cart" onclick="openLoginPopup()"><i class="fa-solid fa-cart-plus"></i></button>
        </c:when>
        <c:when test="${currentProduct.stockCount > 0}">
            <a href="addToCart?productID=${currentProduct.productID}">
                <button class="add-to-cart"><i class="fa-solid fa-cart-plus"></i></button>
            </a>
        </c:when>
        <c:otherwise>
            <div class="out-of-stock">Out of Stock</div>
        </c:otherwise>
    </c:choose>

</div>