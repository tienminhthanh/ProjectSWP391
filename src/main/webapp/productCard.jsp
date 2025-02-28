
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="product-card w-32 md:w-48">

    <!-- Sale Label (Shown only if on sale) -->
    <!--Event related-->
    <%--<c:if test="${onSale}">--%>
    <c:if test="${empty currentProduct.specialFilter}">
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
    <a href="productDetails?id=${currentProduct.productID}&type=${currentProduct.generalCategory}" class="product-image" title="${currentProduct.productName}">
        <img src="${currentProduct.imageURL}" alt="${currentProduct.productName}" >
        <div class="hover-name">${currentProduct.productName}</div>
    </a>

    <!-- Category / Brand -->
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

    

    <!--Stockcount section-->
    <p class="stock-count">Stock count: ${currentProduct.stockCount}</p>

    <!-- Price Section -->
    <p class="product-price">
        <c:choose>
            <%--<c:when test="${onSale}">--%>
            <c:when test="${empty currentProduct.specialFilter}">
                <span class="discount-price">${currentProduct.price * 0.7}</span>
                <span class="original-price">${currentProduct.price}</span>
            </c:when>
            <c:otherwise>
                <span class="discount-price">${currentProduct.price}</span>
            </c:otherwise>
        </c:choose>
    </p>
    
    <!-- Sale Expiry Date or Release Date -->
    <%--<c:if test="${onSale}">--%>
    <c:choose>
        
    <c:when test="${empty currentProduct.specialFilter}">
        <p class="fomo-info sale-expiry">Sale ends on: 2025-01-01</p>
    </c:when>
    <c:when test="${currentProduct.specialFilter == 'pre-order'}">
        <p class="fomo-info release-date">Release on: ${currentProduct.releaseDate}</p>
    </c:when>
    <c:otherwise>
        <p class="fomo-info release-date hidden">Release on: ${currentProduct.releaseDate}</p>
    </c:otherwise>
    </c:choose>


    <!-- Add to Cart Button (Hidden if Out of Stock) -->

    <c:if test="${currentProduct.stockCount > 0 && currentProduct.specialFilter != 'pre-order'}">
        <form action="cart" method="post">
            <input type="hidden" name="customerID" value="${sessionScope.account.accountID}"> <!-- Assuming account has customerID -->
            <input type="hidden" name="productID" value="${currentProduct.productID}">
            <input type="hidden" name="currentURL" value="${requestScope.currentURL}">
            <input type="hidden" name="quantity" value="1"> <!-- Default quantity of 1 -->
            <input type="hidden" name="priceWithQuantity" value="${currentProduct.price}">
            <button name="action" value="add" onclick="openLoginPopup()" type="submit" class="add-to-cart"><i class="fa-solid fa-cart-plus"></i></button>
        </form>
    </c:if>

    <!-- If out of stock -> overlay -->
    <c:if test="${currentProduct.stockCount == 0}">
        <div class="out-of-stock">Out of Stock</div>
    </c:if>

</div>
