<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" errorPage ="error.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title><c:out value="${product.productName} - ${product.specificCategory.categoryName} - WIBOOKS"/></title>

        <script src="https://kit.fontawesome.com/bfab6e6450.js" crossorigin="anonymous"></script>
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

        <fmt:setLocale value="vi_vn"/> <!-- Set locale if needed -->
        <fmt:formatDate value="<%= new java.util.Date()%>" pattern="yyyy-MM-dd" var="todayDate"/>

        <!--header-->
        <jsp:include page="header.jsp"/>

        <!--Breadcrumb-->
        <div class="bread-crumb-area pt-2 pl-4 pb-2 text-sm text-yellow-500 bg-gray-100">
            ${breadCrumb}
        </div>
        <div class="flex flex-col md:flex-row pt-4 bg-gray-50">
            <jsp:include page="customerSidebar.jsp"/>

            <main class="w-full md:w-5/6 p-3">
                <c:if test="${not empty requestScope.exception}">
                    <h3>
                        An error occurred when retrieving product information!
                    </h3>
                    <p>
                        ${requestScope.exception}
                    </p>
                </c:if>

                <c:if test="${not empty requestScope.product}">
                    <div class="root-container bg-gray-100">

                        <div class="detail-container">

                            <div class="main-area flex flex-row md:flex-wrap items-center w-full md:items-stretch">

                                <!--Change to equal sign later-->
                                <!--Overview-->
                                <div class="overview-area w-full min-w-3/5 flex-grow mb-4 bg-white">
                                    <div class="overview-inner md:flex md:flex-row">
                                        <!--Image-->
                                        <div class="image-area">
                                            <img class="mx-auto" src="${product.imageURL}" alt="${product.productName}">
                                        </div>

                                        <c:if test="${type =='book'}">
                                            <!--Book-only description desktop-->
                                            <div class="description-area text-md leading-loose pl-4 hidden md:block">
                                                <h3 class="description-title text-lg">Description</h3>

                                                <p class="description-content m-2">${product.description}</p>
                                                <p class="tags">
                                                    <c:forTokens var="tag" items="${product.keywords}" delims=",">
                                                        #${tag} 
                                                    </c:forTokens>
                                                </p>
                                            </div>
                                        </c:if>
                                    </div>
                                </div>


                                <!--Title - purchase-->
                                <div class="purchase-area w-full flex-grow md:pl-4 mb-4 bg-white border-l-2 border-solid border-black/10">
                                    <!--Title-->
                                    <div class="big-product-name bg-white p-2 border-b-2 border-solid border-black/10">
                                        <div class="big-product-name-inner w-full text-base md:text-sm lg:text-base">
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



                                    <!--Business info-->
                                    <div class="business-info mt-4 w-full md:w-full mx-auto bg-white rounded-t-lg">

                                        <!--Fomo info-->
                                        <c:if test="${product.specialFilter == 'pre-order'}">
                                            <h4 class="fomo-info pre p-2 w-full text-center ">Release Date: ${product.releaseDate}</h4>
                                        </c:if>

                                        <c:if test="${empty product.specialFilter}">
                                            <h4 class="fomo-info sale p-2  w-full text-center ">Sale Ends Date: ${todayDate}</h4>
                                        </c:if>
                                        <div class="purchase-inner">

                                            <!--Price-->
                                            <div class="price-area flex flex-row justify-center md:justify-start items-center font-bold pl-2 mt-4">
                                                <c:if test="${empty product.specialFilter}">
                                                    <p class="final-price w-full text-orange-500 text-3xl">${product.price}</p>
                                                </c:if>
                                                <c:if test="${not empty product.specialFilter}">
                                                    <p class="final-price w-3/10 text-orange-500 text-3xl md:text-base lg:text-3xl">${product.price * 0.7}</p>
                                                    <p class="initial-price w-2/10 text-base md:text-xs lg:text-base">${product.price}</p>
                                                </c:if>
                                            </div>

                                            <!--Ratings-->
                                            <div class="ratings-area text-lg mt-4 pl-2 md:text-left">
                                                <%--<c:if test="${product.numberOfRating > 0}">--%>
                                                <span class="avg-rating"><i class="fa-solid fa-star"></i> 4.5</span>
                                                <span class="ratings-count">(650 Reviews)</span>
                                                <%--</c:if>--%>
                                            </div>
                                        </div>
                                    </div>

                                    <!--Purchase form-->
                                    <div class="purchase-form w-90% md:w-full bg-white mx-auto">
                                        <div class="flex flex-row items-center mt-4 w-3/5 md:w-full self-center">
                                            <p class="stock-count w-1/2 pl-5 text-left text-xl md:text-sm lg:text-xl">Stock: ${product.stockCount}</p>
                                            <input type="number" name="purchaseQuantity" class="w-1/2 ml-5 mr-5 text-lg md:text-sm lg:text-lg" id="quantityInput" value="1" min="1" max="${product.stockCount}" oninput="validity.valid || (value = ${product.stockCount})"/>
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

                                <!--Description-->

                                <div class="description-area text-md leading-loose mb-4 block desc-common">
                                    <h3 class="description-title text-lg">Description</h3>

                                    <p class="description-content p-2 m-2">${product.description}</p>
                                    <p class="tags p-2">
                                        <c:forTokens var="tag" items="${product.keywords}" delims=",">
                                            #${tag} 
                                        </c:forTokens>
                                    </p>
                                </div>

                                <!--Details-->
                                <div class="details-area text-sm w-full mb-4">
                                    <h3 class="description-title text-lg">Product Details</h3>
                                    <c:choose>
                                        <c:when test="${type=='book'}">
                                            <table class="m-2">
                                                <tr><td>Title</td><td>${product.productName}</td></tr>
                                                <c:if test="${not empty creatorMap.author}">
                                                    <tr><td>Author</td><td>${creatorMap.author.creatorName}</td></tr>
                                                </c:if>
                                                <c:if test="${not empty creatorMap.artist}">
                                                    <tr><td>Artist</td><td>${creatorMap.artist.creatorName}</td></tr>
                                                </c:if>
                                                <tr><td>Publisher</td><td>${product.publisher.publisherName}</td></tr>
                                                <tr>
                                                    <td>Genre</td>
                                                    <td>
                                                        ${product.specificCategory.categoryName}, 
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
                                        </c:when>
                                        
                                        <c:when test= "${type=='merch'}">
                                            <table class="m-2">
                                                <tr><td>Product Name</td><td>${product.productName}</td></tr>
                                                 <c:if test="${not empty creatorMap.sculptor}">
                                                    <tr><td>Sculptor</td><td>${creatorMap.scupltor.creatorName}</td></tr>
                                                </c:if>
                                                <c:if test="${not empty creatorMap.artist}">
                                                    <tr><td>Artist</td><td>${creatorMap.artist.creatorName}</td></tr>
                                                </c:if>
                                                <tr><td>Brand</td><td>${product.brand.brandName}</td></tr>
                                                <tr><td>Series</td><td>${product.series.seriesName}</td></tr>
                                                <tr><td>Character</td><td>${product.character.characterName}</td></tr>
                                                
                                                
                                                <tr>
                                                    <td>Specification</td>
                                                    <td>
                                                        <ul>
                                                            <li>Category: ${product.specificCategory.categoryName}</li>
                                                            <li>Scale level: ${product.scaleLevel}</li>
                                                            <li>Size: ${product.size}</li>
                                                            <li>Material: ${product.material}</li>
                                                        </ul>
                                                        
                                                    </td>
                                                </tr>
                                                
                                                
                                                <tr><td>Release Date</td><td>${product.releaseDate}</td></tr>
                                                <c:if test="${not empty ranking}">
                                                    <tr><td>Ranking</td><td>${ranking}</td></tr>
                                                </c:if>
                                            </table>
                                        </c:when>
                                    </c:choose>
                                </div>

                            </div>
                        </div>
                    </div>

                </c:if>
            </main>
        </div>
        <!--Popup unauthorized users-->
        <c:if test="${empty sessionScope.account or sessionScope.account.getRole() != 'customer'}">
            <c:set var="currentURL" value="${currentURL}" scope="request"/>
            <jsp:include page="popuplogin.jsp"/>
        </c:if>

        <jsp:include page="footer.jsp"/>
        <jsp:include page="chat.jsp"/>

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
        <script>

            document.addEventListener("DOMContentLoaded", function () {
                const type = "${requestScope.type}";
                const purchase = document.querySelector(".purchase-area");
                const overview = document.querySelector(".overview-area");
                const image = document.querySelector(".image-area");
                const desc = document.querySelector(".desc-common");
                if (!type) {
                    return;
                }

                if (type === 'book') {
                    overview.classList.add('md:w-3/4');
                    image.classList.add('md:w-1/3');

                    purchase.classList.add('md:w-1/4');
                    desc.classList.add('md:hidden');
                } else if (type === 'merch') {
                    overview.classList.add('md:w-2/3');
                    purchase.classList.add('md:w-1/3');

                }

//                if (type === 'book') {
//                    overview.classList.add('md:w-2/3');
//                    purchase.classList.add('md:w-1/3');
//
//                } else if (type === 'merch') {
//                    overview.classList.add('md:w-3/4');
//                    image.classList.add('md:w-1/3');
//                    purchase.classList.add('md:w-1/4');
//                    desc.classList.add('md:hidden');
//
//                }
            });


        </script>
    </body>

</html>
