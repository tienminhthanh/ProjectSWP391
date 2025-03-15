<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" errorPage ="error.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <c:if test="${not empty product}">
            <title><c:out value="${product.productName} - ${product.specificCategory.categoryName} - WIBOOKS"/></title>
        </c:if>
        <c:if test="${empty product}">
            <title>Unavailable Product - WIBOOKS</title>
        </c:if>

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
                                        <c:choose>
                                            <c:when test="${product.specialFilter == 'pre-order'}">
                                                <h4 class="fomo-info pre p-2 w-full text-center ">Release Date: <span>${product.releaseDate}</span></h4>
                                            </c:when>

                                            <c:when test="${product.discountPercentage != 0}">
                                                <h4 class="fomo-info sale p-2  w-full text-center ">Sale Ends Date: <span>${product.eventEndDate}</span></h4>
                                            </c:when>
                                        </c:choose>

                                        <div class="purchase-inner">

                                            <!--Price-->
                                            <div class="price-area flex flex-row justify-center md:justify-start items-center font-bold pl-2 mt-4">
                                                <c:choose>
                                                    <c:when test="${product.discountPercentage == 0}">
                                                        <p class="final-price w-full text-orange-500 text-3xl">${product.price}</p>
                                                    </c:when>
                                                    <c:when test="${product.specialFilter != 'pre-order'}">
                                                        <p class="final-price w-3/10 text-orange-500 text-3xl md:text-base lg:text-3xl">${product.price * (100-product.discountPercentage)/100}</p>
                                                        <p class="initial-price w-2/10 text-base md:text-xs lg:text-base">${product.price}</p>
                                                    </c:when>
                                                </c:choose>
                                            </div>

                                            <!--Hide ratings if pre-order-->
                                            <c:if test="${product.specialFilter != 'pre-order'}">
                                                <!--Ratings-->
                                                <div class="ratings-area text-lg mt-4 pl-2 md:text-left">
                                                    <span class="avg-rating"><i class="fa-solid fa-star"></i> ${product.averageRating}</span>
                                                    <span class="ratings-count">(${product.numberOfRating} Reviews)</span>
                                                </div>
                                            </c:if>
                                        </div>
                                    </div>

                                    <!--Purchase form-->
                                    <div class="purchase-form w-90% md:w-full bg-white mx-auto">
                                        <div class="flex flex-row items-center mt-4 w-3/5 md:w-full self-center">
                                            <c:choose>
                                                <c:when test="${product.specialFilter != 'pre-order'}">
                                                    <p class="stock-count w-1/2 pl-5 text-left text-xl md:text-sm lg:text-xl">Stock: ${product.stockCount}</p>
                                                    <input type="number" name="purchaseQuantity" class="w-1/2 ml-5 mr-5 text-lg md:text-sm lg:text-lg" id="quantityInput" value="1" min="1" max="${product.stockCount}"/>
                                                </c:when>
                                                <c:when test="${product.stockCount <= 10}">
                                                    <p class="stock-count w-full pl-5 text-center text-xl md:text-sm lg:text-xl font-bold">Remaining slots: <span class="text-3xl text-blue-500 font-bold">${product.stockCount}</span></p>
                                                </c:when>

                                            </c:choose>
                                        </div>

                                        <c:choose>
                                            <c:when test="${product.specialFilter == 'pre-order'}">

                                                <!--Quantity = 1 for pre-order-->
                                                <form action="preorder" method="post">
                                                    <input type="hidden" name="customerID" value="${sessionScope.account.accountID}"> <!-- Assuming account has customerID -->
                                                    <input type="hidden" name="productID" value="${product.productID}"/>
                                                    <input type="hidden" name="priceWithQuantity"/>
                                                    <input type="hidden" name="currentURL" class="currentURL" value="${requestScope.currentURL}"/>
                                                    <input type="hidden" name="quantity" value="1"/>
                                                    <button name="action" value="preOrder" onclick="openLoginPopup()" class="pre-order">Pre-Order</button>
                                                </form>
                                            </c:when>
                                            <c:otherwise>
                                                <!-- Calculate cart quantity from session -->
                                                <c:set var="cartQuantity" value="0" />
                                                <c:forEach var="cartItem" items="${sessionScope.cartItems}">
                                                    <c:if test="${cartItem.productID == product.productID}">
                                                        <c:set var="cartQuantity" value="${cartItem.quantity}" />
                                                    </c:if>
                                                </c:forEach>

                                                <!-- Add to Cart form with stock check -->
                                                <form action="cart" method="post" onsubmit="return checkStock(${cartQuantity}, ${product.stockCount}, event)">
                                                    <input type="hidden" name="customerID" value="${sessionScope.account.accountID}">
                                                    <input type="hidden" name="productID" value="${product.productID}"/>
                                                    <input type="hidden" name="priceWithQuantity"/>
                                                    <input type="hidden" name="currentURL" class="currentURL" value="${requestScope.currentURL}"/>
                                                    <input type="hidden" name="quantity" class="quantity"/>
                                                    <button name="action" value="add" onclick="openLoginPopup()" class="add-to-cart" type="submit">Add to Cart</button>
                                                </form>
                                                <form action="OrderController" method="get">
                                                    <input type="hidden" name="customerID" value="${sessionScope.account.accountID}"> <!-- Assuming account has customerID -->
                                                    <input type="hidden" name="productID" value="${product.productID}"/>
                                                    <input type="hidden" name="priceWithQuantity"/>
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

                                                <tr><td>Release Date</td><td class="release-date">${product.releaseDate}</td></tr>
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


                                                <tr><td>Release Date</td><td class="release-date">${product.releaseDate}</td></tr>
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
        
<div class="fixed bottom-4 left-4 z-50">
            <button id="openChat" class="bg-yellow-500 text-white px-4 py-2 rounded-full shadow-lg hover:bg-yellow-600 transition">
                🤖 WIBOOKS AI
            </button>
        </div>

        <!-- Chat Popup -->
        <div id="chatPopup" class="fixed bottom-16 left-4 bg-white rounded-lg shadow-xl border w-[400px] hidden">
            <div class="flex justify-between items-center bg-orange-500 text-white px-4 py-2 rounded-t-lg">
                <span>WIBOOKS AI</span>
                <span id="closeChatAI" class="cursor-pointer text-xl">❌</span>
            </div>
            <div class="flex-grow overflow-y-auto border p-2 bg-gray-200 rounded-lg shadow-lg">
                <jsp:include page="chatAI.jsp"/>
            </div>
        </div>

        <script>
            document.getElementById("openChat").addEventListener("click", function () {
                document.getElementById("chatPopup").classList.remove("hidden");
            });

            document.getElementById("closeChatAI").addEventListener("click", function () {
                document.getElementById("chatPopup").classList.add("hidden");
            });
        </script>

        <!--Icon-->
        <script src="https://kit.fontawesome.com/bfab6e6450.js" crossorigin="anonymous"></script>

        <!--Header script-->
        <script src="js/scriptHeader.js"></script>

        <!--Footer script-->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.min.js"
                integrity="sha384-BBtl+eGJRgqQAUMxJ7pMwbEyER4l1g+O15P+16Ep7Q9Q+zqX6gSbd85u4mG4QzX+"
        crossorigin="anonymous"></script>


        <!--customer sidebar-->
        <script src="js/scriptCusSidebar.js"></script>

        <!--Tailwind-->
        <script src="https://cdn.tailwindcss.com">
        </script>

        <!-- SweetAlert2 -->
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

        <script>
                                                        //Map input quant to purchase quant in forms
                                                        document.addEventListener("DOMContentLoaded", function () {

                                                            // Replace purchase forms with 'OUT OF STOCK' if stockcount == 0
                                                            if (${product.stockCount == 0 && product.specialFilter != 'pre-order'}) {
                                                                document.querySelector('.purchase-form').innerHTML = `<p>OUT OF STOCK</p>`;
                                                                const stockOut = document.querySelector('.purchase-form p');
                                                                stockOut.classList.add('text-center', 'text-xl', 'text-gray-400', 'py-8', 'md:pr-2', 'bg-gray-100', 'my-16', 'md:mr-4', 'max-w-full');
                                                                return;
                                                            }

                                                            let numberValue = document.getElementById("quantityInput"); // Get the value from the number input
                                                            let hiddenInputs = document.querySelectorAll(".quantity"); // Select all inputs with class "quantity"

                                                            if (!hiddenInputs) {
                                                                console.log("Hidden quantity not found!");
                                                                return;
                                                            }

                                                            if (!numberValue) {
                                                                console.log("Quantity input not found!");
                                                                return;
                                                            }


                                                            // Loop through all hidden inputs and update their values
                                                            hiddenInputs.forEach(function (hiddenInput) {
                                                                hiddenInput.value = numberValue.value;
                                                            });

                                                            // Optional: Display the values for verification
                                                            let displayValues = Array.from(hiddenInputs).map(input => input.value).join(", ");
                                                            console.log("quantity:", displayValues);


//                Map quant on input
                                                            document.getElementById("quantityInput").addEventListener("input", function (event) {
                                                                const inputElement = document.getElementById("quantityInput");
                                                                let numberValue = event.target.value; // Get the value from the number input
                                                                let hiddenInputs = document.querySelectorAll(".quantity"); // Select all inputs with class "quantity"

                                                                if (!numberValue || !`${product.stockCount}`) {
                                                                    return;
                                                                }
                                                                if (numberValue < 1) {
                                                                    alert("Purchase quantity must be greater than 0!");
                                                                    inputElement.value = numberValue = 1;
                                                                } else if (numberValue > ${product.stockCount}) {
                                                                    alert("Purchase quantity cannot exceed ${product.stockCount}!");
                                                                    inputElement.value = numberValue = 1;
                                                                }


                                                                // Loop through all hidden inputs and update their values
                                                                hiddenInputs.forEach(function (hiddenInput) {
                                                                    hiddenInput.value = numberValue;
                                                                });

                                                                // Optional: Display the values for verification
                                                                let displayValues = Array.from(hiddenInputs).map(input => input.value).join(", ");
                                                                console.log("quantity:", displayValues);
                                                            });
                                                        });

//            Adjust layout based product type
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


                                                        //Format price display
                                                        document.addEventListener("DOMContentLoaded", function () {
                                                            // Select all elements with prices
                                                            let priceElements = document.querySelectorAll(".price-area p");

                                                            priceElements.forEach(priceEl => {
                                                                let priceText = priceEl.innerText.trim(); // Get the text inside span
                                                                let price = parseFloat(priceText.replaceAll(" VND", "").replaceAll(",", ""));
                                                                console.log("formatted price: ", price);
                                                                price = Math.round(price);
                                                                console.log("Rounded price: ", price);

                                                                if (!isNaN(price)) {
                                                                    // Format price with commas (e.g., 4,400 VND)
                                                                    priceEl.innerText = new Intl.NumberFormat("en-US").format(price) + " đ";
                                                                }
                                                            });
                                                        });


                                                        //Map final price to forms
                                                        document.addEventListener("DOMContentLoaded", function () {
                                                            const finalPriceElement = document.querySelector(".final-price");
                                                            let pricesToSubmit = document.querySelectorAll("input[name='priceWithQuantity']");

                                                            //Check if the forms are there
                                                            if (!pricesToSubmit) {
                                                                return;
                                                            }

                                                            if (!finalPriceElement) {
                                                                alert("Cannot retrieve product price!");
                                                                return;
                                                            }


                                                            let priceText = finalPriceElement.innerText;
                                                            let priceNumber = parseFloat(priceText.replace(/[^0-9]/g, ""));

                                                            if (isNaN(priceNumber)) {
                                                                console.log("Price is not a number");
                                                                return;
                                                            }

                                                            pricesToSubmit.forEach(function (price) {
                                                                price.value = priceNumber;
                                                                console.log('Price is', price.value);
                                                            });
                                                        });

                                                        //Format date
                                                        document.addEventListener("DOMContentLoaded", function () {
                                                            const fomoDate = document.querySelector('.fomo-info>span');
                                                            const releaseDate = document.querySelector('.release-date');

                                                            if (!fomoDate) {
                                                                console.log("Fomo element not found!");
                                                            } else {
                                                                const fomoText = new Date(fomoDate.innerText);
                                                                if (fomoText === null) {
                                                                    console.log("invalid date format");
                                                                }
                                                                fomoDate.innerText = fomoText.toLocaleDateString("vi-VN");
                                                            }

                                                            if (!releaseDate) {
                                                                console.log("Date element not found!");
                                                                return;
                                                            }

                                                            const dateText = new Date(releaseDate.innerText);
                                                            if (dateText === null) {
                                                                console.log("invalid date format");
                                                                return;
                                                            }

                                                            releaseDate.innerText = dateText.toLocaleDateString("vi-VN");
                                                        });


////Close sidebar on resize
//window.addEventListener('resize', () => {
//    const clientWidth = document.documentElement.clientWidth;
//    const sidebar = document.getElementById('cus-sidebar');
//    sidebar.style.display = 'none';
//});

// Stock check function for Add to Cart
                                                        function checkStock(cartQuantity, stockCount, event) {
                                                            let quantityToAdd = parseInt(document.querySelector("input[name='quantity']").value) || 1; // Get quantity from form
                                                            if (cartQuantity + quantityToAdd > stockCount) {
                                                                Swal.fire({
                                                                    icon: 'error',
                                                                    title: 'Stock Limit Reached',
                                                                    text: `The quantity in your cart (${cartQuantity}). The selected quantity cannot be added to the cart because it exceeds your purchasing limit.`
                                                                });
                                                                event.preventDefault();
                                                                return false;
                                                            }
                                                            return true;
                                                        }

        </script>
    </body>

</html>
