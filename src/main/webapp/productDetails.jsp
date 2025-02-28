<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" errorPage ="error.jsp"%>
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

        <!--Customer sidebar-->
        <link rel="stylesheet" href="css/styleCustomerSidebar.css">

    </head>

    <body>
        <jsp:include page="header.jsp"/>
        <jsp:include page="customerSidebar.jsp"/>

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
                <div class="root-container bg-gray-100">
                    <div class="bread-crumb-area text-sm ">
                        ${breadCrumb}
                    </div>
                    <div class="detail-container">
                        <div class="big-product-name bg-gray-50">
                            <div class="big-product-name-inner w-full">
                                <h2 class="w-full">${product.productName}</h2>
                                <p class="category w-full">${product.specificCategory.categoryName}</p>
                            </div>
                            <div class="creator-top text-xs">
                                <c:forEach var="creator" items ="${creatorMap}" varStatus="loopStatus">
                                    <c:out value="${creator.value.creatorName}"/>
                                    <c:if test="${!loopStatus.last}"> - </c:if>
                                </c:forEach>
                            </div>
                        </div>
                        <div class="main-area flex flex-row md:flex-wrap gap-2 items-center w-full md:items-start">
                            <div class="overview-area w-1/2 md:w-4/5 flex-grow">
                                <div class="overview-inner">
                                    <div class="image-area">
                                        <img src="${product.imageURL}" alt="${product.productName}">
                                    </div>
                                    <div class="description-area text-sm leading-loose">
                                        <h3 class="description-title text-lg">Description</h3>

                                        <p class="description-content">${product.description}</p>
                                        <p class="tags">
                                            <c:forTokens var="tag" items="${product.keywords}" delims=",">
                                                #${tag} 
                                            </c:forTokens>
                                        </p>
                                    </div>
                                </div>
                            </div>
                            <div class="purchase-area w-2/5 md:w-1/5 flex-grow">
                                <div class="business-info">

                                    <c:if test="${product.specialFilter == 'pre-order'}">
                                        <h4 class="fomo-info pre">Release Date: ${product.releaseDate}</h4>
                                    </c:if>

                                    <c:if test="${empty product.specialFilter}">
                                        <h4 class="fomo-info sale">Sale Ends Date: ${todayDate}</h4>
                                    </c:if>
                                    <div class="purchase-inner">
                                        <div class="price-area flex flex-row items-center">
                                            <c:if test="${empty product.specialFilter}">
                                                <p class="final-price w-full">${product.price}</p>
                                            </c:if>
                                            <c:if test="${not empty product.specialFilter}">
                                                <p class="final-price w-3/10 ">${product.price * 0.7}</p>
                                                <p class="initial-price w-2/10">${product.price}</p>
                                            </c:if>
                                        </div>
                                        <div class="ratings-area text-sm mt-2">
                                            <%--<c:if test="${product.numberOfRating > 0}">--%>
                                            <span class="avg-rating"><i class="fa-solid fa-star"></i> 4.5</span>
                                            <span class="ratings-count">(650 Reviews)</span>
                                            <%--</c:if>--%>
                                        </div>
                                    </div>
                                </div>
                                <div class="purchase-form">
                                    <div class="flex flex-col flex-row items-center mt-4">
                                        <p class="stock-count w-1/2 pl-5 text-left text-sm lg:text-base">Stock: ${product.stockCount}</p>
                                        <input type="number" name="purchaseQuantity" class="w-1/2 ml-5 mr-5 text-sm lg:text-base" id="quantityInput" value="1" min="1" max="${product.stockCount}" oninput="validity.valid || (value = ${product.stockCount})"/>
                                    </div>
                                    <c:choose>
                                        <c:when test="${product.specialFilter == 'pre-order'}">
                                            <form action="preorder" method="post">
                                                <input type="hidden" name="customerID" value="${sessionScope.account.accountID}"> <!-- Assuming account has customerID -->
                                                <input type="hidden" name="productID" value="${product.productID}"/>
                                                <input type="hidden" name="priceWithQuantity" value="${product.price}"/>
                                                <input type="hidden" name="currentURL" class="currentURL" value="${requestScope.currentURL}"/>
                                                <input type="hidden" name="quantity" class="quantity"/>
                                                <button name="action" value="preOrder" onclick="openLoginPopup()" class="pre-order">Pre-Order</button>
                                            </form>
                                        </c:when>
                                        <c:otherwise>
                                            <form action="cart" method="post">
                                                <input type="hidden" name="customerID" value="${sessionScope.account.accountID}"> <!-- Assuming account has customerID -->
                                                <input type="hidden" name="productID" value="${product.productID}"/>
                                                <input type="hidden" name="priceWithQuantity" value="${product.price}"/>
                                                <input type="hidden" name="currentURL" class="currentURL" value="${requestScope.currentURL}"/>
                                                <input type="hidden" name="quantity" class="quantity"/>
                                                <button name="action" value="add" onclick="openLoginPopup()" class="add-to-cart" type="submit">Add to Cart</button>
                                            </form>
                                            <form action="OrderController" method="get">
                                                <input type="hidden" name="customerID" value="${sessionScope.account.accountID}"> <!-- Assuming account has customerID -->
                                                <input type="hidden" name="productID" value="${product.productID}"/>
                                                <input type="hidden" name="priceWithQuantity" value="${product.price}"/>
                                                <input type="hidden" name="currentURL" class="currentURL" value="${requestScope.currentURL}"/>
                                                <input type="hidden" name="quantity" class="quantity"/>
                                                <button name="action" value="buyNow" onclick="openLoginPopup()" class="buy-now">Buy Now</button>
                                            </form>
                                        </c:otherwise>
                                    </c:choose>
                                </div>

                            </div>
                            <div class="description-area mobile-version text-sm leading-loose">
                                <h3 class="description-title text-lg">Description</h3>

                                <p class="description-content">${product.description}</p>
                                <p class="tags">
                                    <c:forTokens var="tag" items="${product.keywords}" delims=",">
                                        #${tag} 
                                    </c:forTokens>
                                </p>
                            </div>
                            <div class="details-area text-sm w-full">
                                <h3 class="description-title text-lg">Product Details</h3>
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
                    </div>
                </div>

            </main>
            <jsp:include page="chat.jsp"/>
            <!--Popup unauthorized users-->
            <c:if test="${empty sessionScope.account or sessionScope.account.getRole() != 'customer'}">
                <c:set var="currentURL" value="${currentURL}" scope="request"/>
                <jsp:include page="popuplogin.jsp"/>
            </c:if>

            <jsp:include page="footer.jsp"/>

            <!--Icon-->
            <script src="https://kit.fontawesome.com/bfab6e6450.js" crossorigin="anonymous"></script>

            <!--Header script-->
            <script src="js/scriptHeader.js"></script>

            <!--Footer script-->
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.min.js"
                    integrity="sha384-BBtl+eGJRgqQAUMxJ7pMwbEyER4l1g+O15P+16Ep7Q9Q+zqX6gSbd85u4mG4QzX+"
            crossorigin="anonymous"></script>

            <!--Product details-->
            <script src="js/scriptProductDetails.js"></script>

            <!--customer sidebar-->
            <script src="js/scriptCusSidebar.js"></script>

            <!--Tailwind-->
            <script src="https://cdn.tailwindcss.com">
            </script>
        </c:if>
    </body>

</html>
