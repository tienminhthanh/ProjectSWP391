
<%@ page contentType="text/html; charset=UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html lang="en">
    <head>
        <meta charset="utf-8"/>
        <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
        <title>
            WIBOOKS - More Than Just Books
        </title>


        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet"/>

        <link rel="stylesheet" href="css/styleHome.css"/>
        <!--Header css-->
        <link href="css/styleHeader.css" rel="stylesheet">

        <!--Footer css-->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous" />
        <link href="css/styleFooter.css" rel="stylesheet">

        <!--Product card css-->
        <link rel="stylesheet" href="css/styleProductCard.css"/>

    </head>
    <body class="bg-gray-100">
        <div class="header-container">
            <jsp:include page="header.jsp" flush="true"/> 
        </div>
        <div class="bg-white p-4">
            <div class="flex space-x-4 overflow-x-auto">
                <img alt="Banner 1" class="w-1/3" height="100" src="https://placehold.co/300x100?text=Banner+1" width="300"/>
                <img alt="Banner 2" class="w-1/3" height="100" src="https://placehold.co/300x100?text=Banner+2" width="300"/>
                <img alt="Banner 3" class="w-1/3" height="100" src="https://placehold.co/300x100?text=Banner+3" width="300"/>
            </div>
        </div>
        <div class="flex">
            <aside class="w-1/6 p-3 bg-gray-200">
                <div class="bg-orange-500 text-white p-4 rounded mb-4">
                    <h2>
                        New to BOOK☆WALKER?
                    </h2>
                    <button class="bg-orange-600 text-white p-2 rounded mt-2">
                        <a href="register.jsp">Create Account</a>
                    </button>
                </div>
                <div class="mb-4">
                    <button class="text-blue-600">
                        <a href="login.jsp">Sign In</a>
                    </button>
                </div>
                <div class="mb-4">
                    <h3 class="font-bold">
                        Get Started
                    </h3>
                    <ul class="list-disc list-inside">
                        <li>
                            50% Point-Back for Newcomers
                        </li>
                        <li>
                            About BOOK☆WALKER
                        </li>
                        <li>
                            Redeem Coupons
                        </li>
                        <li>
                            Redeem Gifted eBooks
                        </li>
                        <li>
                            FAQ
                        </li>
                        <li>
                            Point Affiliate Program
                        </li>
                    </ul>
                </div>
                <div>
                    <h3 class="font-bold">
                        Start Reading On
                    </h3>
                    <ul class="list-disc list-inside">
                        <li>
                            PC
                        </li>
                        <li>
                            Android
                        </li>
                        <li>
                            iOS
                        </li>
                    </ul>
                </div>
            </aside>
            <main class="w-5/6 p-3">
                <div class="mb-4">
                    <h2 class="text-xl font-bold">
                        Popular Searches
                    </h2>
                    <div class="flex space-x-4">
                        <button class="bg-blue-500 text-white p-2 rounded">
                            On Sale Merch
                        </button>
                        <button class="bg-blue-500 text-white p-2 rounded">
                            Top Merch
                        </button>
                        <button class="bg-blue-500 text-white p-2 rounded">
                            On Sale Books
                        </button>
                        <button class="bg-blue-500 text-white p-2 rounded">
                            Top Books
                        </button>
                        <button class="bg-blue-500 text-white p-2 rounded">
                            Seasonal Anime - Merch
                        </button>
                        <button class="bg-blue-500 text-white p-2 rounded">
                            Seasonal Anime - Books
                        </button>
                    </div>
                </div>

                <!--Loopppppppppppppppppppppppppppppp-->
                <div>
                    <h2 class="text-xl font-bold mb-4">
                        Feeling Lucky ?
                    </h2>
                    <!--<div class="flex space-x-4 overflow-x-auto">-->
                    <div class="w-full">
                        <div class="grid grid-cols-2 md:grid-cols-3 gap-4 lg:hidden">
                            <c:forEach var="currentProduct" items="${productList}">
                                <c:set var="currentProduct" value="${currentProduct}" scope="request"/>
                                <jsp:include page="productCard.jsp"/>
                            </c:forEach>
                        </div>

                        <!-- Horizontal scrolling at lg (1024px+) -->
                        <div class="hidden lg:block w-full overflow-x-auto">
                            <div class="grid grid-flow-col auto-cols-max gap-4 min-w-max">
                                <c:forEach var="currentProduct" items="${productList}">
                                    <c:set var="currentProduct" value="${currentProduct}" scope="request"/>
                                    <jsp:include page="productCard.jsp"/>
                                </c:forEach>
                            </div>
                        </div>
                    </div>

                    <!--Popup unauthorized users-->
                    <jsp:include page="popuplogin.jsp"/>

                </div>
            </main>
        </div>
        <jsp:include page="footer.jsp"/>


        <!--Script for include icons-->
        <script src="https://kit.fontawesome.com/bfab6e6450.js" crossorigin="anonymous"></script>

        <!--Header script-->
        <script src="js/scriptHeader.js"></script>

        <!--Footer script-->
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js"
                integrity="sha384-I7E8VVD/ismYTF4hNIPjVp/Zjvgyol6VFvRkX/vR+Vc4jQkC+hVqc2pM8ODewa9r"
        crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.min.js"
                integrity="sha384-BBtl+eGJRgqQAUMxJ7pMwbEyER4l1g+O15P+16Ep7Q9Q+zqX6gSbd85u4mG4QzX+"
        crossorigin="anonymous"></script>

        <script src="https://cdn.tailwindcss.com">
        </script>
    </body>
</html>
