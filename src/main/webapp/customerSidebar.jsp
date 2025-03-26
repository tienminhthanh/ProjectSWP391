<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" errorPage ="error.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<div class="overlay" id="cus-sidebar-overlay"></div>
<aside id="cus-sidebar" class="md:w-1/6 px-3 pb-3 pt-12 md:pt-3 bg-gray-100">
    <div class="close-icon">
        <button type="button" onclick="closeMobileMenu()">
            <i class="fa-solid fa-xmark"></i>
        </button>
    </div>
    <div class="w-full border-t-4 border-orange-500"></div> 
    <c:if test="${empty sessionScope.account}">
        <div class="bg-orange-400 text-white p-4 rounded mt-4 mb-4">
            <h2>
                New to WIBOOKS?
            </h2>
            <button class="bg-orange-600 text-white p-2 rounded mt-2 hover:bg-orange-500 hover:font-bold">
                <a href="register">Create Account</a>
            </button>
        </div>
        <div class="mb-4 bg-white">
            <h2 class="text-lg font-bold relative pl-5 mb-3 pb-1 text-black">
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
        <c:if test="${not empty sessionScope.account}">
            <h3 class="text-lg font-bold relative mb-3 pb-1 text-black text-center">
                Popular Searches
            </h3>
        </c:if>
        <c:if test="${empty sessionScope.account}">
            <h3 class="text-lg font-bold relative pl-5 mb-3 pb-1 text-black">
                <span class="absolute left-0 top-0 h-full w-2 bg-orange-500"></span>
                <span class="absolute left-0 bottom-0 w-full h-0.5 bg-gray-300/50"></span>
                Popular Searches
            </h3>
        </c:if>
        <ul id="popList" class="text-xs leading-loose">
            <li class="p-2 mb-2 rounded-md mx-2 cursor-pointer">
                <div class="pop-label font-semibold p-2 rounded-md bg-gray-100 hover:bg-gray-200">On Sale</div>
                <div class="hidden links pl-4">
                    <a href="sale?type=book" class="flex flex-row items-center hover:font-bold">
                        <span class="w-[calc(90%)]">Books</span>
                        <span class="hidden w-[calc(10%)] hover:text-white text-base px-1"><i class="fa-solid fa-xmark"></i></span>
                    </a>
                    <a href="sale?type=merch" class="flex flex-row items-center hover:font-bold">
                        <span class="w-[calc(90%)]">Merchandise</span>
                        <span class="hidden w-[calc(10%)] hover:text-white text-base px-1"><i class="fa-solid fa-xmark"></i></span>
                    </a>
                </div>
            </li>
            <li class="p-2 mb-2 rounded-md mx-2 cursor-pointer">
                <div class="pop-label font-semibold p-2 rounded-md bg-gray-100 hover:bg-gray-200">Ranking</div>
                <div class="hidden links pl-4">
                    <a href="ranking?type=book" class="flex flex-row items-center hover:font-bold">
                        <span class="w-[calc(90%)]">Books</span>
                        <span class="hidden w-[calc(10%)] hover:text-white text-base px-1"><i class="fa-solid fa-xmark"></i></span>
                    </a>
                    <a href="ranking?type=merch" class="flex flex-row items-center hover:font-bold">
                        <span class="w-[calc(90%)]">Merchandise</span>
                        <span class="hidden w-[calc(10%)] hover:text-white text-base px-1"><i class="fa-solid fa-xmark"></i></span>
                    </a>
                    
                </div>
            </li>
            <li class="p-2 mb-2 rounded-md mx-2 cursor-pointer">
                <div class="pop-label font-semibold p-2 rounded-md bg-gray-100 hover:bg-gray-200">Trending</div>
                <div class="hidden links pl-4">
                    <a href="genre?id=18" class="flex flex-row items-center hover:font-bold">
                        <span class="w-[calc(90%)]">Animated Series</span>
                        <span class="hidden w-[calc(10%)] hover:text-white text-base px-1"><i class="fa-solid fa-xmark"></i></span>
                    </a>
                    <a href="series?id=1" class="flex flex-row items-center hover:font-bold">
                        <span class="w-[calc(90%)]">Hololive Merchandise</span>
                        <span class="hidden w-[calc(10%)] hover:text-white text-base px-1"><i class="fa-solid fa-xmark"></i></span>
                    </a>
                    
                </div>
            </li>
        </ul>
    </div>
    <div class="mb-4 bg-white">
        <h3 class="text-lg font-bold relative pl-5 mb-3 pb-1 text-black">
            <span class="absolute left-0 top-0 h-full w-2 bg-orange-500"></span>
            <span class="absolute left-0 bottom-0 w-full h-0.5 bg-gray-300/50"></span>
            Category
        </h3>
        <ul id="catList" class="text-xs leading-loose">
            <c:forEach var="catEntry" items="${applicationScope.categories}">
                <c:set var="key" value="${catEntry.key}"/>
                <c:set var="val" value="${catEntry.value}"/>
                <c:if test="${val > 0 and key.generalCategory eq requestScope.type}">
                    <li class="p-2 mb-2 rounded-md mx-2">
                        <a href="category?id=${key.categoryID}" data-filter="ftCtg-${key.categoryID}" onmouseover="updateHrefOnHover(this,`${key.generalCategory}`,`${type}`)" class="flex flex-row items-center hover:font-bold">
                            <span class="w-[calc(90%)]">${key.categoryName} (${val})</span>
                            <span class="hidden w-[calc(10%)] hover:text-white text-base px-1"><i class="fa-solid fa-xmark"></i></span>
                        </a>
                    </li>
                </c:if>
            </c:forEach>
        </ul>
    </div>

    <div class="mb-4 bg-white">
        <h3 class="text-lg font-bold relative pl-5 mb-3 pb-1 text-black">
            <span class="absolute left-0 top-0 h-full w-2 bg-orange-500"></span>
            <span class="absolute left-0 bottom-0 w-full h-0.5 bg-gray-300/50"></span>
            Creator
        </h3>
        <ul id="creList" class="text-xs leading-loose">
            <c:set var="i" value="0"/>
            <c:forEach var="creEntry" items="${applicationScope.creators}" varStatus="status">
                <c:set var="key" value="${creEntry.key}"/>
                <c:set var="val" value="${creEntry.value}"/>
                <c:if test="${val> 0 and key.generalCategory eq requestScope.type}">
                    <c:set var="i" value="${i + 1}"/>
                    <li class="${i >= 4 ? 'hidden' : ''} p-2 mb-2 rounded-md mx-2">
                        <a href="creator?id=${key.creatorID}" data-filter="ftCrt-${key.creatorID}" onmouseover="updateHrefOnHover(this,`${key.generalCategory}`,`${type}`)" class="flex flex-row items-center hover:font-bold">
                            <span class="w-[calc(90%)]">
                                ${key.creatorName} - ${key.creatorRole} (${val})
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
    <c:if test="${(not empty requestScope.type and requestScope.type eq 'book') or pageContext.request.servletPath ne '/productCatalog.jsp'}">
        <div class="my-2">
            <h3 class="text-center text-xl bg-orange-500 text-white mb-4 p-2 font-bold">Books</h3>

            <div class="mb-4 bg-white">
                <h3 class="text-lg font-bold relative pl-5 mb-3 pb-1 text-black">
                    <span class="absolute left-0 top-0 h-full w-2 bg-orange-500"></span>
                    <span class="absolute left-0 bottom-0 w-full h-0.5 bg-gray-300/50"></span>
                    Genre
                </h3>
                <ul id="genList" class="text-xs leading-loose">
                    <c:forEach var="genEntry" items="${applicationScope.genres}" varStatus="status">
                        <c:if test="${genEntry.value > 0}">
                            <li class="${status.index >= 3 ? 'hidden' : ''} p-2 mb-2 rounded-md mx-2 cursor-pointer">
                                <a href="genre?id=${genEntry.key.genreID}" data-filter="ftGnr-${genEntry.key.genreID}" onmouseover="updateHrefOnHover(this,'book',`${type}`)" class="flex flex-row items-center hover:font-bold">
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
                <h3 class="text-lg font-bold relative pl-5 mb-3 pb-1 text-black">
                    <span class="absolute left-0 top-0 h-full w-2 bg-orange-500"></span>
                    <span class="absolute left-0 bottom-0 w-full h-0.5 bg-gray-300/50"></span>
                    Publisher
                </h3>
                <ul id="pubList" class="text-xs leading-loose">
                    <c:forEach var="pubEntry" items="${applicationScope.publishers}" varStatus="status">
                        <c:if test="${pubEntry.value > 0}">
                            <li class="${status.index >= 3 ? 'hidden' : ''} p-2 mb-2 rounded-md mx-2 cursor-pointer">
                                <a href="publisher?id=${pubEntry.key.publisherID}" data-filter="ftPbl-${pubEntry.key.publisherID}" onmouseover="updateHrefOnHover(this,'book',`${type}`)" class="flex flex-row items-center hover:font-bold">
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
        </div>
    </c:if>
    <!--End Books-->

    <!--Start Merch-->
    <c:if test="${(not empty requestScope.type and requestScope.type eq 'merch') or pageContext.request.servletPath ne '/productCatalog.jsp'}">
        <div class="my-2">
            <h3 class="text-center text-xl bg-orange-500 text-white mb-4 p-2 font-bold">Merchandise</h3>
            <div class="mb-4 bg-white">
                <h3 class="text-lg font-bold relative pl-5 mb-3 pb-1 text-black">
                    <span class="absolute left-0 top-0 h-full w-2 bg-orange-500"></span>
                    <span class="absolute left-0 bottom-0 w-full h-0.5 bg-gray-300/50"></span>
                    Brand
                </h3>
                <ul id="braList" class="text-xs leading-loose">
                    <c:forEach var="braEntry" items="${applicationScope.brands}" varStatus="status">
                        <c:if test="${braEntry.value > 0}">
                            <li class="${status.index >= 3 ? 'hidden' : ''} p-2 mb-2 rounded-md mx-2 cursor-pointer">
                                <a href="brand?id=${braEntry.key.brandID}" data-filter="ftBrn-${braEntry.key.brandID}" onmouseover="updateHrefOnHover(this,'merch',`${type}`)" class="flex flex-row items-center hover:font-bold">
                                    <span class="w-[calc(90%)]">${braEntry.key.brandName} (${braEntry.value})</span>
                                    <span class="hidden w-[calc(10%)] hover:text-white text-base px-1"><i class="fa-solid fa-xmark"></i></span>
                                </a>
                            </li>
                        </c:if>
                    </c:forEach>
                </ul>
                <c:if test="${applicationScope.brands.size() > 3}">
                    <button id="toggleBtnBra" class="text-orange-500 text-sm text-center py-2 mt-2 w-full border-1 border-gray-200 hover:text-orange-300">Show All</button>
                </c:if>
            </div>
            <div class="mb-4 bg-white">
                <h3 class="text-lg font-bold relative pl-5 mb-3 pb-1 text-black">
                    <span class="absolute left-0 top-0 h-full w-2 bg-orange-500"></span>
                    <span class="absolute left-0 bottom-0 w-full h-0.5 bg-gray-300/50"></span>
                    Series
                </h3>
                <ul id="serList" class="text-xs leading-loose">
                    <c:forEach var="serEntry" items="${applicationScope.series}" varStatus="status">
                        <c:if test="${serEntry.value > 0}">
                            <li class="${status.index >= 3 ? 'hidden' : ''} p-2 mb-2 rounded-md mx-2 cursor-pointer">
                                <a href="series?id=${serEntry.key.seriesID}" data-filter="ftSrs-${serEntry.key.seriesID}" onmouseover="updateHrefOnHover(this,'merch',`${type}`)" class="flex flex-row items-center hover:font-bold">
                                    <span class="w-[calc(90%)]">${serEntry.key.seriesName} (${serEntry.value})</span>
                                    <span class="hidden w-[calc(10%)] hover:text-white text-base px-1"><i class="fa-solid fa-xmark"></i></span>
                                </a>
                            </li>
                        </c:if>
                    </c:forEach>
                </ul>
                <c:if test="${applicationScope.series.size() > 3}">
                    <button id="toggleBtnSer" class="text-orange-500 text-sm text-center py-2 mt-2 w-full border-1 border-gray-200 hover:text-orange-300">Show All</button>
                </c:if>
            </div>
            <div class="mb-4 bg-white">
                <h3 class="text-lg font-bold relative pl-5 mb-3 pb-1 text-black">
                    <span class="absolute left-0 top-0 h-full w-2 bg-orange-500"></span>
                    <span class="absolute left-0 bottom-0 w-full h-0.5 bg-gray-300/50"></span>
                    Character
                </h3>
                <ul id="chaList" class="text-xs leading-loose">
                    <c:forEach var="chaEntry" items="${applicationScope.characters}" varStatus="status">
                        <c:if test="${chaEntry.value > 0}">
                            <li class="${status.index >= 3 ? 'hidden' : ''} p-2 mb-2 rounded-md mx-2 cursor-pointer">
                                <a href="character?id=${chaEntry.key.characterID}" data-filter="ftChr-${chaEntry.key.characterID}" onmouseover="updateHrefOnHover(this,'merch',`${type}`)" class="flex flex-row items-center hover:font-bold">
                                    <span class="w-[calc(90%)]">${chaEntry.key.characterName} (${chaEntry.value})</span>
                                    <span class="hidden w-[calc(10%)] hover:text-white text-base px-1"><i class="fa-solid fa-xmark"></i></span>
                                </a>
                            </li>
                        </c:if>
                    </c:forEach>
                </ul>
                <c:if test="${applicationScope.characters.size() > 3}">
                    <button id="toggleBtnCha" class="text-orange-500 text-sm text-center py-2 mt-2 w-full border-1 border-gray-200 hover:text-orange-300">Show All</button>
                </c:if>
            </div>
        </div>
    </c:if>
    <!--End Merch-->

    <!--Show only in catalog-->
    <div class="ft-price-range-area mb-4 hidden bg-white pb-2">
        <h3 class="text-lg font-bold relative pl-5 pb-1 mb-3 text-black">
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
