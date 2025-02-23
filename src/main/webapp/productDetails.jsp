<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title><c:out value="${product.productName} - ${product.specificCategory.categoryName} - WIBOOKS"/></title>
        <!--Header css-->
        <link href="css/styleHeader.css" rel="stylesheet">

        <!--Footer css-->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous" />
        <link href="css/styleFooter.css" rel="stylesheet">
        
        <!--Product Details CSS-->
        <link rel="stylesheet" href="css/styleproductDetails.css">
        
    </head>

    <body>
        <jsp:include page="header.jsp"/>
        <c:if test="${not empty requestScope.exception}">
            <h3>
                An error occurred when retrieving product information!
            </h3>
            <p>
                ${requestScope.exception}
            </p>
        </c:if>

        <c:if test="${not empty requestScope.product}">

            <fmt:setLocale value="vi_vn"/> <!-- Set locale if needed -->
            <fmt:formatDate value="<%= new java.util.Date()%>" pattern="yyyy-MM-dd" var="todayDate"/>

            <main>
                <div class="root-container">
                    <div class="bread-crumb-area">
                        ${breadCrumb}
                    </div>
                    <div class="detail-container">
                        <div class="big-product-name">
                            <div class="big-product-name-inner">
                                <h2>${product.productName}</h2>
                                <p class="category"> - ${product.specificCategory.categoryName}</p>
                            </div>
                            <div class="creator-top">
                                <c:forEach var="creator" items ="${creatorMap}" varStatus="loopStatus">
                                    <c:out value="${creator.value.creatorName}"/>
                                    <c:if test="${!loopStatus.last}"> - </c:if>
                                </c:forEach>
                            </div>
                        </div>
                        <div class="main-area">
                            <div class="info-area">
                                <div class="overview-area">
                                    <div class="overview-inner">
                                        <div class="image-area">
                                            <img src="${product.imageURL}" alt="${product.productName}">
                                        </div>
                                        <div class="description-area">
                                            <h3 class="description-title">Description</h3>
                                            
                                            <p class="description-content">${product.description}</p>
                                            <p class="tags">
                                                <c:forTokens var="tag" items="${product.keywords}" delims=",">
                                                    #${tag} 
                                                </c:forTokens>
                                            </p>
                                        </div>
                                    </div>
                                </div>
                                <div class="details-area">
                                    <h3 class="description-title">Product Details</h3>
                                    <table>
                                        <tr><td>Title</td><td>${product.productName}</td></tr>
                                        <c:if test="${not empty creatorMap.author}">
                                            <tr><td>Author</td><td>${creatorMap.author.creatorName}</td></tr>
                                        </c:if>
                                        <c:if test="${not empty creatorMap.artist}">
                                            <tr><td>Artist</td><td>${creatorMap.artist}</td></tr>
                                        </c:if>
                                        <c:if test="${not empty creatorMap.sculptor}">
                                            <tr><td>Sculptor</td><td>${creatorMap.scupltor}</td></tr>
                                        </c:if>
                                        <tr><td>Publisher</td><td>${product.publisher.publisherName}</td></tr>
                                        <tr>
                                            <td>Genre</td>
                                            <td>
                                                <c:forEach var= "genre" items="${genreList}" varStatus="loopStatus">
                                                    ${genre.genreName}
                                                    <c:if test="${!loopStatus.last}">, </c:if>
                                                </c:forEach>
                                            </td>
                                        </tr>
                                        <tr><td>Release Date</td><td>${product.releaseDate}</td></tr>
                                        <c:if test="${not empty product.duration}">
                                            <tr><td>Duration</td><td>${product.duration}</td></tr>
                                        </c:if>
                                        <c:if test="${not empty ranking}">
                                            <tr><td>Ranking</td><td>${ranking}</td></tr>
                                        </c:if>
                                    </table>
                                </div>
                            </div>
                            <div class="purchase-area">
                                <c:if test="${product.specialFilter == 'pre-order'}">
                                    <h4 class="fomo-info pre">Release Date: ${product.releaseDate}</h4>
                                </c:if>

                                <c:if test="${empty product.specialFilter}">
                                    <h4 class="fomo-info sale">Sale Ends Date: ${todayDate}</h4>
                                </c:if>
                                <div class="purchase-inner">
                                    <div class="price-area">
                                        <c:if test="${empty product.specialFilter}">
                                            <p class="final-price">${product.price} VND</p>
                                        </c:if>
                                        <c:if test="${not empty product.specialFilter}">
                                            <p class="final-price">${product.price * 0.7} VND</p>
                                            <p class="initial-price">${product.price} VND</p>
                                        </c:if>
                                    </div>
                                    <div class="ratings-area">
                                        <%--<c:if test="${product.numberOfRating > 0}">--%>
                                        <span class="avg-rating"><i class="fa-solid fa-star"></i> 4.5</span>
                                        <span class="ratings-count">(650 Reviews)</span>
                                        <%--</c:if>--%>
                                    </div>
                                    <div class="purchase-form">
                                        <p class="stock-count">In Stock: ${product.stockCount}</p>
                                        <form action="productDetails" method="post">
                                            <input type="hidden" name="productID" value="${product.productID}"/>
                                            <input type="number" name="purchaseQuantity" id="quantityInput" value="1" min="1" oninput="validity.valid || (value = 1)"/>
                                            <c:choose>
                                                <c:when test="${product.specialFilter == 'pre-order'}">
                                                    <button name="action" value="preOrder" class="pre-order">Pre-Order</button>
                                                </c:when>
                                                <c:otherwise>
                                                    <button name="action" value="addToCart" class="add-to-cart" type="submit">Add to Cart</button>
                                                    <button name="action" value="buyNow" class="buy-now">Buy Now</button>
                                                </c:otherwise>
                                            </c:choose>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </main>
            <jsp:include page="footer.jsp"/>

            <script src="https://kit.fontawesome.com/bfab6e6450.js" crossorigin="anonymous"></script>
            <script>
                document.getElementById("quantityInput").max = "${product.stockCount}";
            </script>
            <!--Header script-->
            <script src="js/scriptHeader.js"></script>

            <!--Footer script-->
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.min.js"
                    integrity="sha384-BBtl+eGJRgqQAUMxJ7pMwbEyER4l1g+O15P+16Ep7Q9Q+zqX6gSbd85u4mG4QzX+"
            crossorigin="anonymous"></script>
            
            <!--Product details-->
            <script src="js/scriptProductDetails.js"></script>
        </c:if>
    </body>
</html>
