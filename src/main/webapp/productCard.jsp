<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="product-card w-32 md:w-48">

    <!-- Sale Label (Shown only if on sale) -->
    <c:if test="${currentProduct.discountPercentage != 0}">
        <div class="sale-label">${currentProduct.discountPercentage}%</div>
    </c:if>

    <!-- New / Pre-order Ribbon -->
    <c:choose>
        <c:when test="${currentProduct.specialFilter == 'new'}">
            <div class="ribbon ribbon-new">NEW</div>
        </c:when>
        <c:when test="${currentProduct.specialFilter == 'pre-order' && currentProduct.discountPercentage == 0}">
            <div class="ribbon ribbon-pre">PRE-ORDER</div>
        </c:when>
    </c:choose>

    <!-- Product Image -->
    <a href="productDetails?id=${currentProduct.productID}&type=${currentProduct.generalCategory}" class="product-image" title="${currentProduct.productName}">
        <img src="${currentProduct.imageURL}" alt="${currentProduct.productName}" >
        <div class="hover-name">${currentProduct.productName}</div>
    </a>

    <!-- Category -->
    <c:choose>
        <c:when test="${not empty brandName}">
            <p class="product-category">Brand</p>
        </c:when>
        <c:when test="${not empty currentProduct.specificCategory}">
            <p class="product-category">${currentProduct.specificCategory.categoryName}</p>
        </c:when>
        <c:otherwise>
            <p class="product-category">Uncategorized</p>
        </c:otherwise>
    </c:choose>

    <!-- Product Name -->
    <a href="productDetails?id=${currentProduct.productID}&type=${currentProduct.generalCategory}" class="product-title" title="${currentProduct.productName}">${currentProduct.productName}</a>

    <!-- Stock count section -->
    <p class="stock-count">Stock count: ${currentProduct.stockCount}</p>

    <!-- Price Section -->
    <p class="product-price">
        <c:choose>
            <c:when test="${currentProduct.discountPercentage != 0}">
                <span class="discount-price">${currentProduct.price * (100-currentProduct.discountPercentage)/100}</span>
                <span class="original-price">${currentProduct.price}</span>
            </c:when>
            <c:otherwise>
                <span class="discount-price">${currentProduct.price}</span>
            </c:otherwise>
        </c:choose>
    </p>

    <!-- Sale Expiry Date or Release Date -->
    <c:choose>
        <c:when test="${currentProduct.discountPercentage != 0}">
            <p class="fomo-info sale-expiry">Sale ends on: <span>${currentProduct.eventEndDate}</span></p>
        </c:when>
        <c:when test="${currentProduct.specialFilter == 'pre-order'}">
            <p class="fomo-info release-date">Release on: <span>${currentProduct.releaseDate}</span></p>
        </c:when>
        <c:otherwise>
            <p class="fomo-info release-date hidden">Release on: <span>${currentProduct.releaseDate}</span></p>
        </c:otherwise>
    </c:choose>

    <!-- Add to Cart Button Logic -->
    <c:set var="cartQuantity" value="0" />
    <c:forEach var="cartItem" items="${sessionScope.cartItems}">
        <c:if test="${cartItem.productID == currentProduct.productID}">
            <c:set var="cartQuantity" value="${cartItem.quantity}" />
        </c:if>
    </c:forEach>

    <c:if test="${currentProduct.stockCount > 0 && currentProduct.specialFilter != 'pre-order'}">
        <form action="cart" method="post" onsubmit="return checkStock(${cartQuantity}, ${currentProduct.stockCount}, event)">
            <input type="hidden" name="customerID" value="${sessionScope.account.accountID}">
            <input type="hidden" name="productID" value="${currentProduct.productID}">
            <input type="hidden" name="currentURL" value="${requestScope.currentURL}">
            <input type="hidden" name="quantity" value="1">
            <input type="hidden" name="priceWithQuantity" value="${currentProduct.price}">
            <button name="action" value="add" type="submit" class="add-to-cart"><i class="fa-solid fa-cart-plus"></i></button>
        </form>
    </c:if>

    <!-- If out of stock -> overlay -->
    <c:if test="${currentProduct.stockCount == 0}">
        <div class="out-of-stock">Out of Stock</div>
    </c:if>

</div>

<!-- JavaScript for stock check and popup -->
<script>
    function checkStock(cartQuantity, stockCount, event) {
        let quantityToAdd = 1; // Default quantity from the form
        if (cartQuantity + quantityToAdd > stockCount) {
            // Show popup error
            alert("Error: Cannot add to cart. The quantity in your cart (" + cartQuantity + ") plus this addition would exceed the available stock (" + stockCount + ").");
            event.preventDefault(); // Prevent form submission
            return false;
        }
        return true; // Allow form submission if stock is sufficient
    }
</script>