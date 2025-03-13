<%-- 
    Document   : manageProductDetails
    Created on : Mar 13, 2025, 12:25:56 PM
    Author     : anhkc
--%>


<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
        <title>Product Management - ${product.productName} - WIBOOKS</title>

        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"/>
        <link href="https://cdn.jsdelivr.net/npm/sweetalert2@11.6.9/dist/sweetalert2.min.css" rel="stylesheet">

        <!--Product Details CSS-->
        <link rel="stylesheet" href="css/styleproductDetails.css">

    </head>
    <body class="bg-gray-50 min-h-screen flex">

        <div class="w-64 bg-orange-400 text-white min-h-screen">
            <jsp:include page="navbarAdmin.jsp" flush="true"/> 
        </div>

        <!-- Main Content -->
        <div class="flex-1 p-3">
            <h1 class="text-3xl font-bold text-gray-800 mb-6">📌 Product Details</h1>
            <hr class="mb-6 border-gray-300"/>
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
                                        <p class="tags p-2">
                                            <c:forTokens var="tag" items="${product.keywords}" delims=",">
                                                <a data-tag="${tag}" class="tags-link text-orange-500 hover:underline px-1" href="#">
                                                    <span>#</span>${tag} 
                                                </a>
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
                                    <h2 class="w-full leading-normal">${product.productName}</h2>
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
                            <div class="w-90% md:w-full bg-white mx-auto mb-8">
                                <div class="my-4 w-3/5 md:w-full">
                                    <p class="stock-count w-1/2 pl-5 text-left text-xl md:text-sm lg:text-xl">Stock: ${product.stockCount}</p>
                                </div>
                                
                                <c:set var="p_e_status" value="${productEventStatus}"/>
                                <!-- Add to Event form with availability check -->
                                <form  class="flex flex-row items-stretch px-2" action="event" method="post" onsubmit="return checkAvailability(`${p_e_status}`, event)">
                                    <input type="hidden" name="productID" value="${product.productID}"/>
                                    <input type="hidden" name="currentURL" class="currentURL" value="${requestScope.currentURL}"/>
                                    <select class="w-3/5 p-4 border border-gray-300 rounded-l-lg focus:ring-blue-500 focus:border-blue-500 text-lg" name="event">
                                        <c:forEach var="event" items="${eventList}">
                                            <option value="${event.eventID}">${event.eventName}</option>
                                        </c:forEach>
                                    </select>
                                    <button name="action" value="add" class="w-2/5 p-4 rounded-r-lg bg-orange-400 text-center text-white text-lg" type="submit">Add to Event</button>
                                </form>
                            </div>

                        </div>

                        <!--Description-->

                        <div class="description-area text-md leading-loose mb-4 block desc-common">
                            <h3 class="description-title text-lg">Description</h3>

                            <p class="description-content p-2 m-2">${product.description}</p>
                            <p class="tags p-2">
                                <c:forTokens var="tag" items="${product.keywords}" delims=",">
                                    <a data-tag="${tag}" class="tags-link text-orange-500 hover:underline px-1" href="#">
                                        <span>#</span>${tag} 
                                    </a>
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
                                        <c:if test="${product.salesRank > 0}">
                                            <c:set var="rank" value="${product.salesRank}" />
                                            <c:set var="suffix" value="${(rank % 10 == 1 and rank % 100 != 11) ? 'st' : (rank % 10 == 2 and rank % 100 != 12) ? 'nd' : (rank % 10 == 3 and rank % 100 != 13) ? 'rd' : 'th'}" />
                                            <tr>
                                                <td>Monthly Ranking</td>
                                                <td class=" text-${rank == 1 ? 'yellow-400 font-bold text-lg' : rank == 2 ? 'gray-400 font-bold text-lg' : rank == 3 ? 'amber-700 font-bold text-lg' : 'orange-300'}">
                                                    <span class="a-product-crown-${rank}"><i class="fa-solid fa-crown"></i></span>
                                                    <span class="a-product-rank-${rank}">
                                                        ${rank}<span>${suffix}</span>
                                                    </span>
                                                </td>
                                            </tr>
                                        </c:if>
                                    </table>
                                </c:when>

                                <c:when test= "${type=='merch'}">
                                    <table class="m-2">
                                        <tr><td>Product Name</td><td>${product.productName}</td></tr>
                                        <c:if test="${not empty creatorMap.sculptor and not empty creatorMap.scupltor.creatorName}">
                                            <tr><td>Sculptor</td><td>${creatorMap.scupltor.creatorName}</td></tr>
                                        </c:if>
                                        <c:if test="${not empty creatorMap.artist and not empty creatorMap.artist.creatorName}">
                                            <tr><td>Artist</td><td>${creatorMap.artist.creatorName}</td></tr>
                                        </c:if>

                                        <c:if test="${not empty product.brand and not empty product.brand.brandName}">
                                            <tr><td>Brand</td><td>${product.brand.brandName}</td></tr>
                                        </c:if>
                                        <c:if test="${not empty product.series and not empty product.series.seriesName}">
                                            <tr><td>Series</td><td>${product.series.seriesName}</td></tr>
                                        </c:if>
                                        <c:if test="${not empty product.character and not empty product.character.characterName}">
                                            <tr><td>Character</td><td>${product.character.characterName}</td></tr>
                                        </c:if>

                                        <tr>
                                            <td>Specification</td>
                                            <td>
                                                <ul>
                                                    <li>${product.specificCategory.categoryName}</li>
                                                        <c:if test="${not empty product.scaleLevel}">
                                                        <li>Scale level: ${product.scaleLevel}</li>
                                                        </c:if>
                                                        <c:if test="${not empty product.size}">
                                                        <li>Size: ${product.size}</li>
                                                        </c:if>
                                                        <c:if test="${not empty product.material}">
                                                        <li>Material: ${product.material}</li>
                                                        </c:if>
                                                </ul>

                                            </td>
                                        </tr>


                                        <tr><td>Release Date</td><td class="release-date">${product.releaseDate}</td></tr>
                                            <c:if test="${product.salesRank > 0}">
                                                <c:set var="rank" value="${product.salesRank}" />
                                                <c:set var="suffix" value="${(rank % 10 == 1 and rank % 100 != 11) ? 'st' : (rank % 10 == 2 and rank % 100 != 12) ? 'nd' : (rank % 10 == 3 and rank % 100 != 13) ? 'rd' : 'th'}" />
                                            <tr>
                                                <td>Monthly Ranking</td>
                                                <td class=" text-${rank == 1 ? 'yellow-400 font-bold text-lg' : rank == 2 ? 'gray-400 font-bold text-lg' : rank == 3 ? 'amber-700 font-bold text-lg' : 'orange-300'}">
                                                    <span class="a-product-crown-${rank}"><i class="fa-solid fa-crown"></i></span>
                                                    <span class="a-product-rank-${rank}">
                                                        ${rank}<span>${suffix}</span>
                                                    </span>
                                                </td>
                                            </tr>
                                        </c:if>
                                    </table>
                                </c:when>
                            </c:choose>
                        </div>

                        <!--Reviews section-->
                        <div class="review-area text-sm w-full mb-4 bg-white">
                            <h3 class="description-title text-lg">Reviews</h3>
                            <c:choose>

                                <c:when test="${not empty requestScope.reviewMap}">
                                    <c:forEach var="review" items="${requestScope.reviewMap}">
                                        <div class="customer-review p-2 m-2 border-b-4 rounded-lg flex flex-col items-start gap-2">
                                            <h4 class="customer-name text-md font-bold">${review.key}</h4>
                                            <p class="avg-rating">
                                                <c:forEach var="i" begin="1" end="${review.value[0]}">
                                                    <i class="fa-solid fa-star"></i>
                                                </c:forEach>
                                            </p>
                                            <p class="review-content">
                                                ${review.value[1] ne null ? review.value[1] : ''}
                                            </p>
                                        </div> 
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <div class="flex justify-center items-center h-40 w-full">
                                        <p class="text-gray-500 italic">No reviews yet. Be the first to share your thoughts about the purchase!</p>
                                    </div>
                                </c:otherwise>

                            </c:choose>
                        </div>

                    </div>
                </div>
            </div>


        </div>

        <script src="https://cdn.tailwindcss.com"></script>       
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11.6.9/dist/sweetalert2.all.min.js"></script>
        <script>
                                    function confirmAction(message, url) {
                                        Swal.fire({
                                            title: 'Confirmation',
                                            text: message,
                                            icon: 'warning',
                                            showCancelButton: true,
                                            confirmButtonText: 'Yes, do it!',
                                            cancelButtonText: 'Cancel',
                                            reverseButtons: true
                                        }).then((result) => {
                                            if (result.isConfirmed) {
                                                window.location.href = url;
                                            }
                                        });
                                    }
                                    ;

                                    //Format date display
                                    document.addEventListener('DOMContentLoaded', function () {
                                        const releaseDates = document.querySelectorAll('.release-date');
                                        const modifiedTimes = document.querySelectorAll('.modified-time');

                                        // Formatting functions for Vietnam locale
                                        const formatDate = (date) =>
                                            new Intl.DateTimeFormat('vi-VN', {day: '2-digit', month: '2-digit', year: 'numeric'}).format(date);
                                        const formatTime = (date) =>
                                            new Intl.DateTimeFormat('vi-VN', {hour: '2-digit', minute: '2-digit', hour12: true}).format(date);

                                        //ReleaseDate
                                        if (releaseDates) {
                                            releaseDates.forEach(rlsDate => {
                                                const rlsDateObj = new Date(rlsDate.innerText.trim());
                                                if (!isNaN(rlsDateObj)) {
                                                    rlsDate.innerText = formatDate(rlsDateObj);
                                                }
                                            });
                                        }

                                        //Last modified time
                                        if (modifiedTimes) {
                                            modifiedTimes.forEach(strDate => {
                                                const dateObj = new Date(strDate.innerText.trim());
                                                if (!isNaN(dateObj)) {
                                                    const today = new Date();
                                                    // Remove time from today's date for accurate comparison
                                                    today.setHours(0, 0, 0, 0);

                                                    // Show time if today, otherwise show formatted date
                                                    const formattedDate = dateObj.toDateString() === today.toDateString()
                                                            ? formatTime(dateObj)
                                                            : formatDate(dateObj);
                                                    console.log('DATE', formattedDate);
                                                    strDate.innerText = formattedDate;
                                                }
                                            });
                                        }

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


                                    //Format tags-link
                                    document.addEventListener('DOMContentLoaded', function () {
                                        const tagsLink = document.querySelectorAll('.tags-link');
                                        if (tagsLink) {
                                            const type = `${requestScope.type}`;
                                            if (type !== '') {
                                                tagsLink.forEach(link => {
                                                    const tag = link.dataset.tag ? link.dataset.tag : "";
                                                    let params = `type=${type}`;
                                                    params += tag !== type ? "&query=" + tag : "";
                                                    link.href = decodeURIComponent("manageProductList?" + encodeURIComponent(params));
                                                });
                                            }
                                        }
                                    });
                                    
                                    // Availability check function for Add to Event
                                    function checkAvailability(productEventStatus, event) {
                                        if (productEventStatus && productEventStatus === 'inEvent') {
                                            Swal.fire({
                                                icon: 'error',
                                                title: 'Event limit reached',
                                                text: `This product is already assigned to an currently active event. Please remove it from the event before proceeding!`
                                            });
                                            event.preventDefault();
                                            return false;
                                        }
                                        return true;
                                    }


        </script>
        <!--Script for include icons-->
        <script src="https://kit.fontawesome.com/bfab6e6450.js" crossorigin="anonymous"></script>
    </body>
</html>
