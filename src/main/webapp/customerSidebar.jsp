<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" errorPage ="error.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="overlay" id="cus-sidebar-overlay" onclick="closeMobileMenu()"></div>
<aside id="cus-sidebar" class="md:w-1/6 p-3 bg-gray-100">
    <div class="close-icon">
        <button type="button" onclick="closeMobileMenu()">
            <i class="fa-solid fa-xmark"></i>
        </button>
    </div>
    <c:if test="${empty sessionScope.account}">
        <div class="bg-orange-400 text-white p-4 rounded mt-4 mb-4">
            <h2>
                New to WIBOOKS?
            </h2>
            <button class="bg-orange-600 text-white p-2 rounded mt-2">
                <a href="register">Create Account</a>
            </button>
        </div>
        <div class="mb-4 bg-white">
            <h2 class="text-lg font-bold relative pl-5 mb-3 pb-1">
                <span class="absolute left-0 top-0 h-full w-2 bg-orange-500"></span>
                <span class="absolute left-0 bottom-0 w-full h-0.5 bg-gray-300/50"></span>
                Get Started
            </h2>
            <ul class=" text-sm leading-loose">
                <li class="px-2">
                    <a href="#" class="hover:font-bold">About WIBOOKS</a>
                </li>
                <li class="px-2">

                    <a href="#" class="hover:font-bold">FAQ</a>
                </li>
            </ul>
        </div>
    </c:if>
    
    <div class="mb-4 bg-white">
        <h3 class="text-lg font-bold relative pl-5 mb-3 pb-1">
            <span class="absolute left-0 top-0 h-full w-2 bg-orange-500"></span>
            <span class="absolute left-0 bottom-0 w-full h-0.5 bg-gray-300/50"></span>
            Popular Searches
        </h3>
        <ul id="popList" class="text-xs leading-loose">
            <li class=" p-2 mb-2 rounded-md mx-2 cursor-pointer">
                <div class="pop-label">
                    New Release
                </div>
                <a href="#" class="flex flex-row items-center hover:font-bold">
                    <span class="w-[calc(90%)]">Books</span>
                    <span class="hidden w-[calc(10%)] hover:text-white text-base px-1"><i class="fa-solid fa-xmark"></i></span>
                </a>
                <a href="#" class="flex flex-row items-center hover:font-bold">
                    <span class="w-[calc(90%)]">Merchandise</span>
                    <span class="hidden w-[calc(10%)] hover:text-white text-base px-1"><i class="fa-solid fa-xmark"></i></span>
                </a>
            </li>
            <li class=" p-2 mb-2 rounded-md mx-2 cursor-pointer">
                <div class="pop-label">
                    On Sale
                </div>
                <a href="#" class="flex flex-row items-center hover:font-bold">
                    <span class="w-[calc(90%)]">Books</span>
                    <span class="hidden w-[calc(10%)] hover:text-white text-base px-1"><i class="fa-solid fa-xmark"></i></span>
                </a>
                <a href="#" class="flex flex-row items-center hover:font-bold">
                    <span class="w-[calc(90%)]">Merchandise</span>
                    <span class="hidden w-[calc(10%)] hover:text-white text-base px-1"><i class="fa-solid fa-xmark"></i></span>
                </a>
            </li>
            <li class=" p-2 mb-2 rounded-md mx-2 cursor-pointer">
                <div class="pop-label">
                    Special Choices
                </div>
                <a href="#" class="flex flex-row items-center hover:font-bold">
                    <span class="w-[calc(90%)]">Animated Series</span>
                    <span class="hidden w-[calc(10%)] hover:text-white text-base px-1"><i class="fa-solid fa-xmark"></i></span>
                </a>
                <a href="#" class="flex flex-row items-center hover:font-bold">
                    <span class="w-[calc(90%)]">Hololive Merchandise</span>
                    <span class="hidden w-[calc(10%)] hover:text-white text-base px-1"><i class="fa-solid fa-xmark"></i></span>
                </a>
            </li>
           
        </ul>
    </div>
    
    <div class="mb-4 bg-white">
        <h3 class="text-lg font-bold relative pl-5 mb-3 pb-1">
            <span class="absolute left-0 top-0 h-full w-2 bg-orange-500"></span>
            <span class="absolute left-0 bottom-0 w-full h-0.5 bg-gray-300/50"></span>
            Category
        </h3>
        <ul class="text-xs leading-loose">
            <c:forEach var="catEntry" items="${applicationScope.categories}">
                <c:if test="${catEntry.value > 0}">
                    <li class="p-2 mb-2 rounded-md mx-2">
                        <a href="category?id=${catEntry.key.categoryID}" class="flex flex-row items-center hover:font-bold">
                            <span class="w-[calc(90%)]">${catEntry.key.categoryName} (${catEntry.value})</span>
                            <span class="hidden w-[calc(10%)] hover:text-white text-base px-1"><i class="fa-solid fa-xmark"></i></span>
                        </a>
                    </li>
                </c:if>
            </c:forEach>
        </ul>
    </div>

    <div class="mb-4 bg-white">
        <h3 class="text-lg font-bold relative pl-5 mb-3 pb-1">
            <span class="absolute left-0 top-0 h-full w-2 bg-orange-500"></span>
            <span class="absolute left-0 bottom-0 w-full h-0.5 bg-gray-300/50"></span>
            Creator
        </h3>
        <ul id="creList" class="text-xs leading-loose">
            <c:forEach var="creEntry" items="${applicationScope.creators}" varStatus="status">
                <c:if test="${creEntry.value > 0}">
                    <li class="${status.index >= 3 ? 'hidden' : ''} p-2 mb-2 rounded-md mx-2">
                        <a href="creator?id=${creEntry.key.creatorID}" data-filter="ftCrt-${creEntry.key.creatorID}" onmouseover="updateHrefOnHover(this)" class="flex flex-row items-center hover:font-bold">
                            <span class="w-[calc(90%)]">
                                ${creEntry.key.creatorName} (${creEntry.value})
                            </span>
                            <span class="hidden w-[calc(10%)] hover:text-white text-base px-1"><i class="fa-solid fa-xmark"></i></span>
                        </a>
                    </li>
                </c:if>
            </c:forEach>
        </ul>
        <c:if test="${applicationScope.creators.size() > 3}">
            <button id="toggleBtnCre" class="text-orange-500 text-sm text-center py-2 mt-2 w-full border-1 border-gray-200 hover:text-orange-300">Show All</button>
        </c:if>
    </div>

    <!--Start Books-->
    <c:if test="${requestScope.type == 'book'}">

        <div class="mb-4 bg-white">
            <h3 class="text-lg font-bold relative pl-5 mb-3 pb-1">
                <span class="absolute left-0 top-0 h-full w-2 bg-orange-500"></span>
                <span class="absolute left-0 bottom-0 w-full h-0.5 bg-gray-300/50"></span>
                Genre
            </h3>
            <ul id="genList" class="text-xs leading-loose">
                <c:forEach var="genEntry" items="${applicationScope.genres}" varStatus="status">
                    <c:if test="${genEntry.value > 0}">
                        <li class="${status.index >= 3 ? 'hidden' : ''} p-2 mb-2 rounded-md mx-2 cursor-pointer">
                            <a href="genre?id=${genEntry.key.genreID}" data-filter="ftGnr-${genEntry.key.genreID}" onmouseover="updateHrefOnHover(this)" class="flex flex-row items-center hover:font-bold">
                                <span class="w-[calc(90%)]">${genEntry.key.genreName} (${genEntry.value})</span>
                                <span class="hidden w-[calc(10%)] hover:text-white text-base px-1"><i class="fa-solid fa-xmark"></i></span>
                            </a>
                        </li>
                    </c:if>
                </c:forEach>
            </ul>
            <c:if test="${applicationScope.genres.size() > 3}">
                <button id="toggleBtnGen" class="text-orange-500 text-sm text-center py-2 mt-2 w-full border-1 border-gray-200 hover:text-orange-300">Show All</button>
            </c:if>
        </div>
        <div class="mb-4 bg-white">
            <h3 class="text-lg font-bold relative pl-5 mb-3 pb-1">
                <span class="absolute left-0 top-0 h-full w-2 bg-orange-500"></span>
                <span class="absolute left-0 bottom-0 w-full h-0.5 bg-gray-300/50"></span>
                Publisher
            </h3>
            <ul id="pubList" class="text-xs leading-loose">
                <c:forEach var="pubEntry" items="${applicationScope.publishers}" varStatus="status">
                    <c:if test="${pubEntry.value > 0}">
                        <li class="${status.index >= 3 ? 'hidden' : ''} p-2 mb-2 rounded-md mx-2 cursor-pointer">
                            <a href="publisher?id=${pubEntry.key.publisherID}" data-filter="ftPbl-${pubEntry.key.publisherID}" onmouseover="updateHrefOnHover(this)" class="flex flex-row items-center hover:font-bold">
                                <span class="w-[calc(90%)]">${pubEntry.key.publisherName} (${pubEntry.value})</span>
                                <span class="hidden w-[calc(10%)] hover:text-white text-base px-1"><i class="fa-solid fa-xmark"></i></span>
                            </a>
                        </li>
                    </c:if>
                </c:forEach>
            </ul>
            <c:if test="${applicationScope.genres.size() > 3}">
                <button id="toggleBtnPub" class="text-orange-500 text-sm text-center py-2 mt-2 w-full border-1 border-gray-200 hover:text-orange-300">Show All</button>
            </c:if>
        </div>
    </c:if>
    <!--End Books-->

    <!--Start Merch-->
    <c:if test="${requestScope.type == 'merch'}">
        <div class="mb-4">
            <h3 class="font-bold">
                Series
            </h3>
            <ul class="list-disc list-inside">
                <li>
                    <a href="#">Series 1</a>
                </li>
                <li>
                    <a href="#">Series 2</a>
                </li>
                <li>
                    <a href="#">See more...</a>
                </li>
            </ul>
        </div>
        <div class="mb-4">
            <h3 class="font-bold">
                Character
            </h3>
            <ul class="list-disc list-inside">
                <li>
                    <a href="#">Character 1</a>
                </li>
                <li>
                    <a href="#">Character 2</a>
                </li>
                <li>
                    <a href="#">See more...</a>
                </li>
            </ul>
        </div>
        <div class="mb-4">
            <h3 class="font-bold">
                Brand
            </h3>
            <ul class="list-disc list-inside">
                <li>
                    <a href="#">Brand 1</a>
                </li>
                <li>
                    <a href="#">Brand 2</a>
                </li>
                <li>
                    <a href="#">See more...</a>
                </li>
            </ul>
        </div>
    </c:if>
    <!--End Merch-->

    <!--Show only in catalog-->
    <div class="ft-price-range-area mb-4 hidden bg-white pb-2">
        <h3 class="text-lg font-bold relative pl-5 pb-1 mb-3">
            <span class="absolute left-0 top-0 h-full w-2 bg-orange-500"></span>
            <span class="absolute left-0 bottom-0 w-full h-0.5 bg-gray-300/50"></span>
            Price range (đ)
        </h3>
        <div class="form-ftprc-container mx-2 rounded-md">
            <a href="#" id="remove-ftprc-link" class="hidden w-full text-right block px-2 pt-1 hover:text-white">
                <i class="fa-solid fa-xmark"></i>
            </a>
            <form class="flex flex-row flex-wrap justify-center p-2 text-sm" action="" id="ftprc-form" method="get">
                <div class="hidden-input-ftprice"></div>
                <input name="ftPrc" class="w-2/5 border-1 border-black" id="ftprc-min" type="number" min="0" />
                <span class=" px-1 text-center w-1/5"> - </span>
                <input name="ftPrc" class="w-2/5 border-1 border-black" id="ftprc-max" type="number" min="0" />
                <button type="submit" class="bg-orange-400 w-full rounded-md mt-2 hover:bg-orange-300"><i class="fa-solid fa-magnifying-glass text-white"></i></button>
            </form>
        </div>
    </div>


    

</aside>
