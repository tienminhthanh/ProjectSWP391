<%@page import="java.io.FilenameFilter"%>
<%@page import="java.io.File"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" errorPage ="error.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>                                                    
<fmt:setLocale value="en_US"/>
<html lang="en">
    <head>
        <meta charset="utf-8"/>
        <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
        <title>
            WIBOOKS - More Than Just Books
        </title>

        <!--Script for include icons-->
        <script src="https://kit.fontawesome.com/bfab6e6450.js" crossorigin="anonymous"></script>


        <!--Unknown import-->
        <!--<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet"/>-->

        <link rel="stylesheet" href="css/styleHome.css"/>
        <!--Header css-->
        <link href="css/styleHeader.css" rel="stylesheet">

        <!--Footer css-->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous" />
        <link href="css/styleFooter.css" rel="stylesheet">

        <!--Customer Sidebar-->
        <link href="css/styleCustomerSidebar.css" rel="stylesheet">

        <!--Product card css-->
        <link rel="stylesheet" href="css/styleProductCard.css"/>

        <!--Banner carousel-->
        <script src="https://cdn.jsdelivr.net/npm/alpinejs@3.x.x/dist/cdn.min.js" defer></script>
        
        <!--Sweet Alert-->
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11" defer></script>
    </head>
    <body class="bg-gray-100">
        <div class="header-container">
            <jsp:include page="header.jsp" flush="true"/> 
        </div>



        <div class="flex flex-col md:flex-row">
            <jsp:include page="customerSidebar.jsp"/>

            <!--Main section-->
            <main class="w-full md:w-5/6 p-3 flex flex-col">
                <div class="flex flex-nowrap gap-4 overflow-x-auto pb-4">
                    <c:choose>
                        <c:when test="${not empty eventDetails}">
                            <div class="container mx-auto p-6">
                                <div class="voucher-card bg-white rounded-lg p-6 ">
                                    <img src="${eventDetails.banner}" alt="Banner" class="w-full h-auto object-cover">

                                    <div class="voucher-info text-gray-700 text-left space-y-3">
                                        <p><strong>Duration: </strong><span id="duration">${eventDetails.duration} days</span>
                                            (Until <span id="dateEnd">${dateEnd})</span>
                                        </p>
                                        <p><strong>Description: </strong> <span id="description">${eventDetails.description}</span></p>

                                    </div>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="text-center text-gray-500 italic p-4">No event found.</div>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="bg-white">
                    <h2 class="text-xl font-bold relative pt-4 pb-4 text-center border-t-4 border-orange-300">
                        Products On Sales
                    </h2>
                    <!--Loop through product list-->
                    <div class="w-full">
                        <div class="gap-4 w-full overflow-x-auto">
                            <div class="grid grid-flow-col auto-cols-max gap-4 min-w-max">
                                <c:set var="currentURL" value="${currentURL}" scope="request"/>
                                <c:forEach var="currentProduct" items="${productList}">
                                    <c:set var="currentProduct" value="${currentProduct}" scope="request"/>
                                    <jsp:include page="productCard.jsp"/>
                                </c:forEach>
                            </div>
                        </div>
                    </div>

                </div>
                <!--Popup unauthorized users-->
                <c:if test="${empty sessionScope.account or sessionScope.account.getRole() != 'customer'}">
                    <jsp:include page="popuplogin.jsp"/>
                </c:if>
            </main>
        </div>
        <jsp:include page="footer.jsp"/>
        <jsp:include page="chat.jsp"/>




        <!--Header script-->
        <script src="js/scriptHeader.js"></script>

        <!--Footer script-->

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.min.js"
                integrity="sha384-BBtl+eGJRgqQAUMxJ7pMwbEyER4l1g+O15P+16Ep7Q9Q+zqX6gSbd85u4mG4QzX+"
        crossorigin="anonymous"></script>

        <!--Customer sidebar script-->
        <script src="js/scriptCusSidebar.js"></script>
        <script src="js/scriptCusSideBarNOTDetails.js"></script>

        <!--Unknown import-->
        <!--        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js"
                        integrity="sha384-I7E8VVD/ismYTF4hNIPjVp/Zjvgyol6VFvRkX/vR+Vc4jQkC+hVqc2pM8ODewa9r"
                crossorigin="anonymous"></script>-->

        <!--Tailwind-->
        <script src="https://cdn.tailwindcss.com">
        </script>

        <!--Product Card-->
        <script src="js/scriptProductCard.js"></script>

        <script>
            document.addEventListener("DOMContentLoaded", function () {
                document.querySelectorAll(".voucher").forEach(function (voucher) {
                    let startDate = new Date(voucher.dataset.start);
                    let duration = parseInt(voucher.dataset.duration);
                    let dateEnd = new Date(startDate);
                    dateEnd.setDate(startDate.getDate() + duration);

                    voucher.querySelector(".date-end").textContent = dateEnd.toISOString().split("T")[0];
                });
            });
        </script>

        

    </body>
</html>