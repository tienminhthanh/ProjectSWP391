<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" errorPage="error.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<li class="product-item">
    <section class="o-card-wrap">
        <div class="o-product-img-box">
            <div class="product-img w-2/5 md:w-1/5">
                <!-- Ranking Badge -->
                <c:if test="${currentProduct.salesRank > 0}">
                    <c:set var="rank" value="${currentProduct.salesRank}" />
                    <c:set var="suffix" value="${(rank % 10 == 1 and rank % 100 != 11) ? 'st' : (rank % 10 == 2 and rank % 100 != 12) ? 'nd' : (rank % 10 == 3 and rank % 100 != 13) ? 'rd' : 'th'}" />
                    <div class="m-product-ranking text-${rank == 1 ? 'yellow-400 font-bold text-lg' : rank == 2 ? 'gray-400 font-bold text-lg' : rank == 3 ? 'amber-700 font-bold text-lg' : 'orange-300'}">
                        <span class="a-product-crown-${rank}"><i class="fa-solid fa-crown"></i></span>
                        <span class="a-product-rank-${rank}">
                            ${rank}<span>${suffix}</span>
                        </span>
                    </div>
                </c:if>

                <!-- Product Image -->
                <div class="m-product-img">
                    <a href="productDetails?id=${currentProduct.productID}&type=${currentProduct.generalCategory}">
                        <img loading="lazy" src="${currentProduct.imageURL}" alt="${currentProduct.productName}">
                    </a>
                </div>

                <!-- Star Rating -->
                <div class="m-product-review-item">
                    <div class="m-star-box">
                        <c:forEach var="i" begin="1" end="5">
                            <i class="fa-${i <= currentProduct.averageRating ? 'solid' : 'regular'} fa-star "></i>
                        </c:forEach>
                    </div>
                </div>
            </div>
            <div class="product-info w-3/5 md:w-4/5 pt-1">
                <!-- Product Title -->
                <h2 class="o-card-ttl">
                    <a href="productDetails?id=${currentProduct.productID}&type=${currentProduct.generalCategory}">
                        ${currentProduct.productName}
                    </a>
                </h2>

                <!-- Category Tags -->
                <div class="o-card-category-tag-box">
                    <span class="m-card-category-tag">${currentProduct.specificCategory.categoryName}</span>
                </div>
            </div>
        </div>

        <div class="o-card-detail-box">
            <!-- Price Section -->
            <div class="o-card-price text-lg md:text-xl">
                <span><fmt:formatNumber value="${currentProduct.discountPercentage > 0 ? currentProduct.price * (100-currentProduct.discountPercentage)/100 : currentProduct.price}" type="number" groupingUsed="true" /></span>
                <span>đ</span>
            </div>
            <c:if test="${currentProduct.stockCount gt 0 and currentProduct.specialFilter ne 'pre-order'}">
                <!-- Buttons (Buy Now & Cart) -->
                <div class="m-card-btn-box">
                    <form action="OrderController" method="get">
                        <input type="hidden" name="customerID" value="${sessionScope.account.accountID}"> <!-- Assuming account has customerID -->
                        <input type="hidden" name="productID" value="${currentProduct.productID}">
                        <input type="hidden" name="currentURL" value="${requestScope.currentURL}">
                        <input type="hidden" name="quantity" value="1"> <!-- Default quantity of 1 -->
                        <input type="hidden" name="priceWithQuantity" value="${currentProduct.discountPercentage > 0 ? currentProduct.price * (100-currentProduct.discountPercentage)/100 : currentProduct.price}">
                        <button name="action" value="buyNow" onclick="openLoginPopup()" type="submit" class="a-buy-now-btn">
                            <i class="fa-solid fa-forward"></i>
                            <span class="a-buy-now-btn-txt">Buy Now</span>
                        </button>
                    </form>

                    <form action="cart" method="post">
                        <input type="hidden" name="customerID" value="${sessionScope.account.accountID}"> <!-- Assuming account has customerID -->
                        <input type="hidden" name="productID" value="${currentProduct.productID}">
                        <input type="hidden" name="currentURL" value="${requestScope.currentURL}">
                        <input type="hidden" name="quantity" value="1"> <!-- Default quantity of 1 -->
                        <input type="hidden" name="priceWithQuantity" value="${currentProduct.discountPercentage > 0 ? currentProduct.price * (100-currentProduct.discountPercentage)/100 : currentProduct.price}">
                        <button name="action" value="add" onclick="openLoginPopup()" type="submit" class="a-cart-btn">
                            <i class="fa-solid fa-cart-plus"></i>
                            <span class="a-cart-btn-txt">Cart</span>
                        </button>
                    </form>
                </div>
            </c:if>
        </div>
    <!-- If out of stock -> overlay -->
    <c:if test="${currentProduct.stockCount == 0 and currentProduct.specialFilter ne 'pre-order'}">
        <div class="out-of-stock rounded-lg">Out of Stock</div>
    </c:if>
    </section>
</li>
