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
            <aside class="w-1/4 p-4 bg-gray-200">
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
            <main class="w-3/4 p-4">
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
                <div>
                    <h2 class="text-xl font-bold mb-4">
                        New Releases (Volumes)
                    </h2>
                    <div class="flex space-x-4 overflow-x-auto">
                        <div class="bg-white p-4 rounded shadow w-48">
                            <img alt="Book 1" class="mb-2" height="150" src="https://placehold.co/100x150?text=Book+1" width="100"/>
                            <h3 class="text-sm font-bold">
                                The Eminence in Shadow, Vol. 6 (light novel)
                            </h3>
                            <p class="text-orange-500 font-bold">
                                US $7.99
                            </p>
                            <button class="bg-blue-500 text-white p-2 rounded mt-2">
                                Preview
                            </button>
                            <button class="bg-orange-500 text-white p-2 rounded mt-2">
                                Cart
                            </button>
                        </div>
                        <div class="bg-white p-4 rounded shadow w-48">
                            <img alt="Book 2" class="mb-2" height="150" src="https://placehold.co/100x150?text=Book+2" width="100"/>
                            <h3 class="text-sm font-bold">
                                Crossplay Love: Otaku x Punk Vol. 11
                            </h3>
                            <p class="text-orange-500 font-bold">
                                US $9.99
                            </p>
                            <button class="bg-blue-500 text-white p-2 rounded mt-2">
                                Preview
                            </button>
                            <button class="bg-orange-500 text-white p-2 rounded mt-2">
                                Cart
                            </button>
                        </div>
                        <div class="bg-white p-4 rounded shadow w-48">
                            <img alt="Book 3" class="mb-2" height="150" src="https://placehold.co/100x150?text=Book+3" width="100"/>
                            <h3 class="text-sm font-bold">
                                The Too-Perfect Saint: Tossed Aside by My...
                            </h3>
                            <p class="text-orange-500 font-bold">
                                US $9.99
                            </p>
                            <button class="bg-blue-500 text-white p-2 rounded mt-2">
                                Preview
                            </button>
                            <button class="bg-orange-500 text-white p-2 rounded mt-2">
                                Cart
                            </button>
                        </div>
                        <div class="bg-white p-4 rounded shadow w-48">
                            <img alt="Book 4" class="mb-2" height="150" src="https://placehold.co/100x150?text=Book+4" width="100"/>
                            <h3 class="text-sm font-bold">
                                A Cozy Life in the Woods with the White Witch...
                            </h3>
                            <p class="text-orange-500 font-bold">
                                US $7.99
                            </p>
                            <button class="bg-blue-500 text-white p-2 rounded mt-2">
                                Preview
                            </button>
                            <button class="bg-orange-500 text-white p-2 rounded mt-2">
                                Cart
                            </button>
                        </div>
                        <div class="bg-white p-4 rounded shadow w-48">
                            <img alt="Book 5" class="mb-2" height="150" src="https://placehold.co/100x150?text=Book+5" width="100"/>
                            <h3 class="text-sm font-bold">
                                Even Though We're Adults Vol. 10
                            </h3>
                            <p class="text-orange-500 font-bold">
                                US $9.99
                            </p>
                            <button class="bg-blue-500 text-white p-2 rounded mt-2">
                                Preview
                            </button>
                            <button class="bg-orange-500 text-white p-2 rounded mt-2">
                                Cart
                            </button>
                        </div>
                        <div class="bg-white p-4 rounded shadow w-48">
                            <img alt="Book 6" class="mb-2" height="150" src="https://placehold.co/100x150?text=Book+6" width="100"/>
                            <h3 class="text-sm font-bold">
                                A Certain Scientific Railgun Vol. 19
                            </h3>
                            <p class="text-orange-500 font-bold">
                                US $9.99
                            </p>
                            <button class="bg-blue-500 text-white p-2 rounded mt-2">
                                Preview
                            </button>
                            <button class="bg-orange-500 text-white p-2 rounded mt-2">
                                Cart
                            </button>
                        </div>
                        <div class="bg-white p-4 rounded shadow w-48">
                            <img alt="Book 7" class="mb-2" height="150" src="https://placehold.co/100x150?text=Book+7" width="100"/>
                            <h3 class="text-sm font-bold">
                                Kurokiya-san Wants to Lead Him Around by the...
                            </h3>
                            <p class="text-orange-500 font-bold">
                                US $13.95
                            </p>
                            <button class="bg-blue-500 text-white p-2 rounded mt-2">
                                Preview
                            </button>
                            <button class="bg-orange-500 text-white p-2 rounded mt-2">
                                Cart
                            </button>
                        </div>
                        <div class="bg-white p-4 rounded shadow w-48">
                            <img alt="Book 8" class="mb-2" height="150" src="https://placehold.co/100x150?text=Book+8" width="100"/>
                            <h3 class="text-sm font-bold">
                                Pass the Monster Meat, Milady! 7
                            </h3>
                            <p class="text-orange-500 font-bold">
                                US $10.99
                            </p>
                            <button class="bg-blue-500 text-white p-2 rounded mt-2">
                                Preview
                            </button>
                            <button class="bg-orange-500 text-white p-2 rounded mt-2">
                                Cart
                            </button>
                        </div>
                        <div class="bg-white p-4 rounded shadow w-48">
                            <img alt="Book 9" class="mb-2" height="150" src="https://placehold.co/100x150?text=Book+9" width="100"/>
                            <h3 class="text-sm font-bold">
                                Lycoris Recoil: Ordinary Days
                            </h3>
                            <p class="text-orange-500 font-bold">
                                US $8.99
                            </p>
                            <button class="bg-blue-500 text-white p-2 rounded mt-2">
                                Preview
                            </button>
                            <button class="bg-orange-500 text-white p-2 rounded mt-2">
                                Cart
                            </button>
                        </div>
                        <div class="bg-white p-4 rounded shadow w-48">
                            <img alt="Book 10" class="mb-2" height="150" src="https://placehold.co/100x150?text=Book+10" width="100"/>
                            <h3 class="text-sm font-bold">
                                I Have a Crush at Work 8
                            </h3>
                            <p class="text-orange-500 font-bold">
                                US $10.99
                            </p>
                            <button class="bg-blue-500 text-white p-2 rounded mt-2">
                                Preview
                            </button>
                            <button class="bg-orange-500 text-white p-2 rounded mt-2">
                                Cart
                            </button>
                        </div>
                    </div>
                </div>
            </main>
        </div>
        <jsp:include page="footer.jsp" flush="true"/>

        <script src="https://cdn.tailwindcss.com">
        </script>

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
    </body>
</html>