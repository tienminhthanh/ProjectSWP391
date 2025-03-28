<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<div class="product-card w-36 md:w-48 p-2">

    <!-- Sale Label (Shown only if on sale) -->
    <c:if test="${currentProduct.discountPercentage != 0}">
        <div class="sale-label">${currentProduct.discountPercentage}%</div>
    </c:if>

    <!-- New / UPCOMING Ribbon -->
    <c:choose>
        <c:when test="${currentProduct.specialFilter == 'new'}">
            <div class="ribbon ribbon-new">NEW</div>
        </c:when>
        <c:when test="${currentProduct.specialFilter == 'upcoming' && currentProduct.discountPercentage == 0}">
            <div class="ribbon ribbon-pre">UPCOMING</div>
        </c:when>
    </c:choose>

    <!-- Product Image -->
    <c:choose>
        <c:when test="${sessionScope.account.role eq 'admin' or sessionScope.account.role eq 'staff'}">
            <a href="manageProductDetails?id=${currentProduct.productID}&type=${currentProduct.generalCategory}" class="product-image" title="${currentProduct.productName}">
                <img src="${currentProduct.imageURL}" alt="${currentProduct.productName}" loading="lazy">
                <div class="hover-name">${currentProduct.productName}</div>
            </a>
            
        </c:when>
        <c:otherwise>
            <a href="productDetails?id=${currentProduct.productID}&type=${currentProduct.generalCategory}" class="product-image" title="${currentProduct.productName}">
                <img src="${currentProduct.imageURL}" alt="${currentProduct.productName}" loading="lazy">
                <div class="hover-name">${currentProduct.productName}</div>
            </a>
        </c:otherwise>
    </c:choose>

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

    <!--Stock count & price for NON UPCOMING -->
    <c:choose>
        <c:when test="${currentProduct.specialFilter ne 'upcoming'}">
            <!-- Stock count section -->
            <p class="stock-count">Stock count: ${currentProduct.stockCount}</p>

            <!-- Price Section -->
            <p class="product-price">
                <c:choose>
                    <c:when test="${currentProduct.discountPercentage != 0}">
                        <span class="discount-price">
                            <fmt:formatNumber value="${currentProduct.price * (100-currentProduct.discountPercentage)/100}" type="number" groupingUsed="true" /> Ä
                        </span>
                        <span class="original-price">
                            <fmt:formatNumber value="${currentProduct.price}" type="number" groupingUsed="true" /> Ä
                        </span>
                    </c:when>
                    <c:otherwise>
                        <span class="discount-price">
                            <fmt:formatNumber value="${currentProduct.price}" type="number" groupingUsed="true" /> Ä
                        </span>
                    </c:otherwise>
                </c:choose>
            </p>
        </c:when>
        <c:otherwise>

            <!-- Price Section -->
            <p class="product-price">
                <span class="discount-price text-blue-600"><fmt:formatNumber value="${currentProduct.price}" type="number" groupingUsed="true" /> Ä</span>
            </p>
        </c:otherwise>
    </c:choose>

    <!-- Sale Expiry Date or Release Date -->
    <c:choose>
        <c:when test="${currentProduct.discountPercentage != 0}">
            <p class="fomo-info sale-expiry">Sale ends on: <span>${currentProduct.eventEndDate}</span></p>
        </c:when>
        <c:when test="${currentProduct.specialFilter == 'upcoming'}">
            <p class="fomo-info release-date">Release on: <span>${currentProduct.releaseDate}</span></p>
        </c:when>
    </c:choose>

    <!-- Add to Cart Button Logic -->
    <c:set var="cartQuantity" value="0" />
    <c:forEach var="cartItem" items="${sessionScope.cartItems}">
        <c:if test="${cartItem.productID == currentProduct.productID}">
            <c:set var="cartQuantity" value="${cartItem.quantity}" />
        </c:if>
    </c:forEach>
    <!-- Add to Cart Button (Hidden if Out of Stock) -->
    <form action="cart" method="post" onsubmit="return checkStockCard(${cartQuantity}, ${currentProduct.stockCount}, event)">
        <input type="hidden" name="customerID" value="${sessionScope.account.accountID}"> <!-- Assuming account has customerID -->
        <input type="hidden" name="productID" value="${currentProduct.productID}">
        <input type="hidden" name="currentURL" value="${requestScope.currentURL}">
        <input type="hidden" name="quantity" value="1"> <!-- Default quantity of 1 -->
        <input type="hidden" name="priceWithQuantity"/>
        <c:if test="${currentProduct.stockCount gt 0 and currentProduct.specialFilter ne 'upcoming' 
                      && (pageContext.request.servletPath eq '/home.jsp' || pageContext.request.servletPath eq '/productCatalog.jsp' || pageContext.request.servletPath eq '/eventDetailsCus.jsp')}">
              <button name="action" value="add" ${empty sessionScope.account || sessionScope.account.getRole() ne 'customer' ? 'onclick=openLoginPopup()' : '' } type="submit" class="add-to-cart"><i class="fa-solid fa-cart-plus"></i></button>
            </c:if>
    </form>

    <!-- If out of stock -> overlay -->
    <c:if test="${currentProduct.stockCount == 0 and currentProduct.specialFilter ne 'upcoming'}">
        <div class="out-of-stock">Out of Stock</div>
    </c:if>

</div>



