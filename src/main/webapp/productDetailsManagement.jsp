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
        
        <!-- Preload Fonts for Faster Icon Loading -->
        <link rel="preload" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/webfonts/fa-solid-900.woff2" as="font" type="font/woff2" crossorigin="anonymous">
        <link rel="preload" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/webfonts/fa-regular-400.woff2" as="font" type="font/woff2" crossorigin="anonymous">

        <!-- Load FontAwesome via CSS (Faster than JS Kit) -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" crossorigin="anonymous">

        
        <link href="https://cdn.jsdelivr.net/npm/sweetalert2@11.6.9/dist/sweetalert2.min.css" rel="stylesheet">

        <!--Product Details CSS-->
        <link rel="stylesheet" href="css/styleproductDetails.css">
        
        <!--Tailwind-->
        <script src="https://cdn.tailwindcss.com"></script>
        
        <!-- FontAwesome Kit (Optional, but defer it) -->
        <script src="https://kit.fontawesome.com/bfab6e6450.js" crossorigin="anonymous" defer></script>


        <!-- SweetAlert2 -->
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11" defer></script>

    </head>
    <body class="bg-gray-50 min-h-screen flex">

        <div class="side-nav-container w-64 bg-orange-400 text-white min-h-screen">
            <jsp:include page="navbarAdmin.jsp" flush="true"/> 
        </div>

        <!-- Main Content -->
        <div class="flex-1 p-6">
            <h1 class="text-3xl font-bold text-gray-800 mb-6">📌 Product Details</h1>
            <hr class="mb-6 border-gray-300"/>
            <div class="mt-6 flex flex-col items-start"> 
                <c:if test="${not empty message}">
                    <script>
                        Swal.fire({
                            icon: 'error',
                            title: 'Error',
                            text: '${message}',
                        });
                    </script>
                </c:if>
            </div>
            <div class="root-container bg-gray-100">

                <div class="detail-container">

                    <div class="main-area flex flex-row md:flex-wrap items-center w-full md:items-stretch">
                        
                        <!--Change to equal sign later-->
                        <!--Overview-->
                        <div class="overview-area w-full ${type == 'merch' ? 'md:w-2/3' : 'md:w-3/4'} min-w-3/5 flex-grow mb-4 bg-white">
                            <div class="overview-inner md:flex md:flex-row">
                                <!--Image-->
                                <div class="image-area ${type == 'merch' ? 'w-full': 'md:w-1/3'}">
                                    <img class="mx-auto object-contain" src="${product.imageURL}" alt="${product.productName}">
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
                        <div class="purchase-area w-full ${type == 'merch' ? 'md:w-1/3' : 'md:w-1/4'} flex-grow md:px-4 mb-4 bg-white border-l-2 border-solid border-black/10">
                            <!--Title-->
                            <div class="big-product-name bg-white p-2 border-b-2 border-solid border-black/10">
                                <div class="big-product-name-inner w-full text-base md:text-sm lg:text-base">
                                    <h2 class="w-full leading-normal">${product.productName}</h2>
                                    <p class="category w-full">${product.specificCategory.categoryName}</p>
                                </div>
                                <div class="creator-top text-xs">
                                    <c:forEach var="creator" items ="${creatorList}" varStatus="loopStatus">
                                        <c:out value="${creator.creatorName}"/>
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

                            <div class="w-[90%] md:w-full bg-white mx-auto mb-8">
                                <!--Stock count-->
                                <div class="my-4 w-3/5 md:w-full">
                                    <p class="stock-count w-1/2 pl-5 text-left text-xl md:text-sm lg:text-xl">Stock: ${product.stockCount}</p>
                                </div>

                                <!-- Add to Event form with availability check - Admin only -->
                                <c:if test="${not empty sessionScope.account and sessionScope.account.getRole() eq 'admin'}">
                                    <c:if test="${not isSoldOrPreOrder}">
                                        <c:set var="p_e_status" value="${productEventStatus}"/>
                                        <form class="flex flex-col items-stretch px-2 space-y-2" 
                                              action="eventProductAddNew" 
                                              method="post" 
                                              onsubmit="return validateForm() && prepareDiscountData()" novalidate>

                                            <!-- Ẩn ID sản phẩm -->
                                            <input type="hidden" name="selectedProducts" value="${product.productID}" />

                                            <!-- Ẩn URL hiện tại -->
                                            <input type="hidden" name="currentURL" class="currentURL" value="${requestScope.currentURL}" />

                                            <c:choose>
                                                <c:when test="${not empty event}">
                                                    <span class="w-full p-4 border border-gray-300 rounded-lg bg-green-100 text-green-800 text-md text-center">
                                                        Now Sale On: ${event.eventName}
                                                    </span>
                                                </c:when>
                                                <c:otherwise>
                                                    <!-- Chọn sự kiện -->
                                                    <select class="w-full p-4 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500 text-md" name="eventId">
                                                        <c:forEach var="eventItem" items="${eventList}">
                                                            <option value="${eventItem.eventID}">${eventItem.eventName}</option>
                                                        </c:forEach>
                                                    </select>

                                                    <!-- Nhập Discount -->
                                                    <input type="number" id="discountInput" placeholder="Discount %" min="1" max="99" step="1" required
                                                           class="w-full p-4 border border-gray-300 rounded-lg text-center focus:ring-blue-500 focus:border-blue-500 text-md"/>

                                                    <!-- Trường ẩn để gửi discount dạng JSON -->
                                                    <input type="hidden" id="discountData" name="discountData" />

                                                    <!-- Nút Submit -->
                                                    <button class="w-full p-4 rounded-lg bg-orange-400 text-center text-white text-md" type="submit">
                                                        Add to Event
                                                    </button>
                                                </c:otherwise>
                                            </c:choose>
                                        </form>
                                    </c:if>
                                </c:if>
                            </div>

                        </div>

                        <!--Common Description-->

                        <div class="description-area text-md leading-loose mb-4 block desc-common ${type == 'book' ? 'md:hidden' : ''}">
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
                                        <tr><td>Title</td><td>${not empty product.productName ? product.productName : 'Unknown'}</td></tr>
                                        <!--Fill by javascript-->
                                        <tr class="cre-details-gr"><td>Author</td>
                                            <td></td>
                                        </tr>

                                        <!--Fill by javascript-->
                                        <tr class="cre-details-gr"><td>Artist</td>
                                            <td></td>
                                        </tr>

                                        <tr><td>Publisher</td>
                                            <td>${not empty product.publisher && not empty product.publisher.publisherName ? product.publisher.publisherName : 'Unknown'}</td>
                                        </tr>

                                        <!--Fill by javascript-->
                                        <tr class="gen-details-gr">
                                            <td>Genre</td>
                                            <td></td>
                                        </tr>

                                        <tr><td>Release Date</td><td class="release-date">${product.releaseDate}</td></tr>

                                        <tr><td>Duration</td><td>${not empty product.duration ? product.duration : 'Unknown'}</td></tr>

                                        <c:set var="rank" value="${product.salesRank}" />
                                        <c:set var="suffix" value="${rank == 0 ? '' :(rank % 10 == 1 and rank % 100 != 11) ? 'st' : (rank % 10 == 2 and rank % 100 != 12) ? 'nd' : (rank % 10 == 3 and rank % 100 != 13) ? 'rd' : 'th'}" />
                                        <tr>
                                            <td>Monthly Ranking</td>
                                            <td class=" text-${rank == 0 ? 'black' : rank == 1 ? 'yellow-400 font-bold text-lg' : rank == 2 ? 'gray-400 font-bold text-lg' : rank == 3 ? 'amber-700 font-bold text-lg' : 'orange-300'}">
                                                <c:if test="${product.salesRank > 0}">
                                                    <span class="a-product-crown-${rank}"><i class="fa-solid fa-crown"></i></span>
                                                    </c:if>
                                                <span class="a-product-rank-${rank}">
                                                    ${rank > 0 ? rank : 'Unranked'}<span>${suffix}</span>
                                                </span>
                                            </td>
                                        </tr>

                                    </table>
                                </c:when>

                                <c:when test= "${type=='merch'}">
                                    <table class="m-2">
                                        <tr><td>Product Name</td><td>${not empty product.productName ? product.productName : 'Unknown'}</td></tr>
                                        <!--Fill by javascript-->
                                        <tr class="cre-details-gr"><td>Sculptor</td>
                                            <td></td>
                                        </tr>

                                        <!--Fill by javascript-->
                                        <tr class="cre-details-gr"><td>Artist</td>
                                            <td></td>
                                        </tr>

                                        <tr><td>Brand</td><td>${not empty product.brand and not empty product.brand.brandName ? product.brand.brandName : 'Unknown'}</td></tr>
                                        <tr><td>Series</td><td>${not empty product.series and not empty product.series.seriesName ? product.series.seriesName : 'Unknown'}</td></tr>
                                        <tr><td>Character</td><td>${not empty product.character and not empty product.character.characterName ? product.character.characterName : 'Unknown'}</td></tr>

                                        <tr>
                                            <td>Specification</td>
                                            <td>
                                                <ul>
                                                    <li>${product.specificCategory.categoryName}</li>
                                                    <li>Scale level: ${not empty product.scaleLevel ? product.scaleLevel : 'Unknown'}</li>
                                                    <li>Size: ${not empty product.size ? product.size : 'Unknown'}</li>
                                                    <li>Material: ${not empty product.material ? product.material : 'Unknown'}</li>
                                                </ul>

                                            </td>
                                        </tr>


                                        <tr><td>Release Date</td><td class="release-date">${product.releaseDate}</td></tr>
                                            <c:set var="rank" value="${product.salesRank}" />
                                            <c:set var="suffix" value="${rank == 0 ? '' :(rank % 10 == 1 and rank % 100 != 11) ? 'st' : (rank % 10 == 2 and rank % 100 != 12) ? 'nd' : (rank % 10 == 3 and rank % 100 != 13) ? 'rd' : 'th'}" />
                                        <tr>
                                            <td>Monthly Ranking</td>
                                            <td class=" text-${rank == 0 ? 'black' : rank == 1 ? 'yellow-400 font-bold text-lg' : rank == 2 ? 'gray-400 font-bold text-lg' : rank == 3 ? 'amber-700 font-bold text-lg' : 'orange-300'}">
                                                <c:if test="${product.salesRank > 0}">
                                                    <span class="a-product-crown-${rank}"><i class="fa-solid fa-crown"></i></span>
                                                    </c:if>
                                                <span class="a-product-rank-${rank}">
                                                    ${rank > 0 ? rank : 'Unranked'}<span>${suffix}</span>
                                                </span>
                                            </td>
                                        </tr>
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
                                    <div class="flex justify-center items-center h-40 w-full text-xl">
                                        <p class="text-gray-500 italic">No reviews yet.</p>
                                    </div>
                                </c:otherwise>

                            </c:choose>
                        </div>

                    </div>
                </div>
            </div>


        </div>

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
                                                      const fomoDate = document.querySelector('.fomo-info>span');

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
                                                      if (fomoDate) {
                                                          const dateObj = new Date(fomoDate.innerText.trim());
                                                          if (!isNaN(dateObj)) {
                                                              const today = new Date();
                                                              // Remove time from today's date for accurate comparison
                                                              today.setHours(0, 0, 0, 0);

                                                              // Show time if today, otherwise show formatted date
                                                              const formattedDate = dateObj.toDateString() === today.toDateString()
                                                                      ? formatTime(dateObj)
                                                                      : formatDate(dateObj);
                                                              fomoDate.innerText = formattedDate;
                                                          }
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

                                                if (!isNaN(price)) {
                                                    price = Math.round(price);
                                                    // Format price with commas (e.g., 4,400 VND)
                                                    priceEl.innerText = price.toLocaleString() + " đ";
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
                                                  ;


                                                  document.addEventListener('DOMContentLoaded', function () {
                                                      const creators = document.querySelectorAll('.cre-details-gr');
                                                      if (creators) {
                                                          creators.forEach(cre => {
                                                              let content = cre.querySelector('td:nth-child(2)');
                                                              if (content) {
                                                                  let text = content.innerText.trim();
                                                                  text = text !== '' ? text : 'Unknown';
                                                                  content.innerText = text;
                                                              }
                                                          });
                                                      }
                                                  });


        </script>
        <script>
            function validateForm() {
                let discountInput = document.getElementById('discountInput');
                let discountValue = discountInput.value.trim();
                if (discountValue === "" || isNaN(discountValue) || discountValue < 1 || discountValue > 99) {
                    Swal.fire({
                        icon: 'error',
                        title: 'Validation Error',
                        html: 'Product have invalid discounts.<br>Please enter a discount between 1% and 99%.'
                    });
                    return false; // Ngăn form submit nếu giá trị không hợp lệ
                }
                return true; // Cho phép submit nếu hợp lệ
            }

        </script>
        <script>
            function prepareDiscountData() {
                const discountInput = document.getElementById("discountInput");
                const discountDataField = document.getElementById("discountData");
                const productId = document.querySelector("input[name='selectedProducts']").value;
                if (!discountInput.value) {
                    alert("Please enter a valid discount.");
                    return false;
                }

                // Tạo JSON: { "productID": discount }
                const discountData = {}
                ;
                discountData[productId] = parseInt(discountInput.value);
                // Gán vào input ẩn
                discountDataField.value = JSON.stringify(discountData);
                return true; // Cho phép gửi form
            }

        </script>
        
    </body>
</html>
