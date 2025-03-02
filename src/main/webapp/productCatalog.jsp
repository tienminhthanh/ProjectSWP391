<%-- 
    Document   : searchResult
    Created on : Feb 23, 2025, 3:17:53 PM
    Author     : anhkc
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" errorPage ="error.jsp"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>
            ${pageTitle} - WIBOOKS
        </title>

        <!--Header css-->
        <link href="css/styleHeader.css" rel="stylesheet">

        <!--Footer css-->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous" />
        <link href="css/styleFooter.css" rel="stylesheet">

        <!--Product card css-->
        <link rel="stylesheet" href="css/styleProductCard.css"/>

        <!--Search css-->
        <link rel="stylesheet" href="css/styleCatalog.css"/>

        <!--Customer Sidebar-->
        <link href="css/styleCustomerSidebar.css" rel="stylesheet">
    </head>
    <body>
        <!--Header-->
        <jsp:include page="header.jsp"/>

        <!--Breadcrumb-->
        <div class="bread-crumb-area pt-2 pl-4 pb-2 text-sm text-yellow-500 bg-gray-100">
            ${breadCrumb}
        </div>
        
        <!--Sidebar-->
        <div class="flex flex-col md:flex-row pt-4 bg-gray-50">
            <jsp:include page="customerSidebar.jsp"/>

            <!--Main section-->
            <main class="w-full md:w-5/6 p-3">

                <div class="bg-white">
                    <h2 class="text-xl font-bold relative pl-5 mb-3 pb-1">
                        <span class="absolute left-0 top-0 h-full w-2 bg-orange-500"></span>
                        <span class="absolute left-0 bottom-0 w-full h-0.5 bg-gray-300/50"></span>
                        ${pageTitle}
                    </h2>


                    <div class="overview-area flex mb-4">
                        <c:if test="${not empty productList}">
                            <div class="sort-area w-2/5">
                                <form action="search" method="get" id="sortFormSearch">
                                    <input type="hidden" name="query" value="${requestScope.query}">
                                    <input type="hidden" name="type" value="${requestScope.type}">
                                    <label for="sortCriteria" class="inline-flex items-center">
                                        <span class="w-32 pl-3">Sort by</span>
                                        <select class="form-select text-sm" id="sortCriteria" name="sortCriteria" onchange="submitSort()">
                                            <c:if test="${not empty query}">
                                                <option value="relevance">Relevance</option>
                                            </c:if>
                                            <option value="releaseDate">Release Date</option>
                                            <option value="hotDeal">Hot Deal</option>
                                            <option value="name">Name (A-Z)</option>
                                            <option value="rank">Rank</option>
                                        </select>
                                    </label>
                                </form>
                            </div>
                            <div class="page-num-top-area w-3/5 text-right">
                                ${productList.size()} result(s)
                            </div>
                        </c:if>
                        <c:if test="${empty productList && not empty message}">
                            ${message}
                        </c:if>
                    </div>

                    <div class="w-full">
                        <c:set var="currentURL" value="${currentURL}" scope="request"/>
                        <div class="grid grid-cols-2 md:grid-cols-3 gap-4 lg:grid-cols-5">
                            <c:forEach var="currentProduct" items="${productList}">
                                <c:set var="currentProduct" value="${currentProduct}" scope="request"/>
                                <jsp:include page="productCard.jsp"/>
                            </c:forEach>
                        </div>
                    </div>

                </div>
                <!--Popup unauthorized users-->
                <c:if test="${empty sessionScope.account or sessionScope.account.getRole() != 'customer'}">
                    <jsp:include page="popuplogin.jsp" />
                </c:if>
            </main>
        </div>


        <!--Footer-->
        <jsp:include page="footer.jsp"/>
        <jsp:include page="chat.jsp"/>

        <script>
            function submitSort() {
                document.getElementById("sortFormSearch").submit();
            }

            window.addEventListener("load", function () {
                var sortCriteria = "<%= request.getAttribute("sortCriteria") != null ? request.getAttribute("sortCriteria") : "releaseDate"%>";

                console.log("DEBUG: sort from JSP:", sortCriteria);

                var selectElement = document.getElementById("sortCriteria");
                if (selectElement) {
                    selectElement.value = sortCriteria;
                }
            });
        </script>

        <!--Script for include icons-->
        <script src="https://kit.fontawesome.com/bfab6e6450.js" crossorigin="anonymous"></script>

        <!--Header script-->
        <script src="js/scriptHeader.js"></script>

        <!--Footer script-->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.min.js"
                integrity="sha384-BBtl+eGJRgqQAUMxJ7pMwbEyER4l1g+O15P+16Ep7Q9Q+zqX6gSbd85u4mG4QzX+"
        crossorigin="anonymous"></script>

        <!--Tailwind-->
        <script src="https://cdn.tailwindcss.com">
        </script>

        <!--Product Card-->
        <script src="js/scriptProductCard.js"></script>

        <!--Customer sidebar script-->
        <script src="js/scriptCusSidebar.js"></script>
        <script src="js/scriptCusSideBarNOTDetails.js"></script>

    </body>
</html>
