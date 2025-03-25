<%-- 
    Document   : manageProducts
    Created on : Mar 13, 2025, 12:25:38 PM
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
        <title>Product Management - WIBOOKS</title>

        <!-- Preload Fonts for Faster Icon Loading -->
        <link rel="preload" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/webfonts/fa-solid-900.woff2" as="font" type="font/woff2" crossorigin="anonymous">
        <link rel="preload" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/webfonts/fa-regular-400.woff2" as="font" type="font/woff2" crossorigin="anonymous">

        <!-- Load FontAwesome via CSS (Faster than JS Kit) -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" crossorigin="anonymous">


        <link href="https://cdn.jsdelivr.net/npm/sweetalert2@11.6.9/dist/sweetalert2.min.css" rel="stylesheet">
        
        <!--Tailwind-->
        <script src="https://cdn.tailwindcss.com"></script>

        <!-- FontAwesome Kit (Optional, but defer it) -->
        <script src="https://kit.fontawesome.com/bfab6e6450.js" crossorigin="anonymous" defer></script>

        <!-- SweetAlert2 -->
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11" defer></script>
    </head>
    <body class="bg-gray-50 min-h-screen flex">

        <div class="w-64 bg-orange-400 text-white min-h-screen">
            <jsp:include page="navbarAdmin.jsp" flush="true"/> 
        </div>

        <!-- Main Content -->
        <div class="flex-1 p-6">
            <h1 class="text-3xl font-bold text-gray-800 mb-6">📌 Product List</h1>
            <hr class="mb-6 border-gray-300"/>
            <div class="mt-6 flex flex-col items-start">
                <!-- Add New Product Button -->
                <div class="flex items-center space-x-6">
                    <c:if test="${not empty sessionScope.account and sessionScope.account.role eq 'admin'}">

                        <a class="bg-green-600 text-white p-4 rounded-lg hover:bg-orange-700 flex items-center justify-start w-auto transition duration-300 ease-in-out transform hover:scale-105 mb-4" href="addProduct">
                            <i class="fas fa-plus mr-2"></i> Add New Product
                        </a>
                        <a class="bg-yellow-500 text-white p-4 rounded-lg hover:bg-orange-700 flex items-center justify-start w-auto transition duration-300 ease-in-out transform hover:scale-105 mb-4" href="queueImport">
                            <i class="fas fa-plus mr-2"></i> Queue New Import
                        </a>
                    </c:if>

                    <!--Search Field--> 
                    <form action="manageProductList" method="get" class="flex items-center space-x-4 mb-4 ">
                        <div class="flex items-stretch justify-center">
                            <select class="p-4 border border-gray-300 rounded-l focus:ring-blue-500 focus:border-blue-500" name="type">
                                <option value="" ${empty param.type ? 'selected' : ''}>All Products</option>
                                <option value="book" ${param.type == 'book' ? 'selected' : ''}>Books</option>
                                <option value="merch" ${param.type == 'merch' ? 'selected' : ''}>Merchandise</option>
                            </select>
                            <input class="p-4 border border-gray-300 focus:ring-blue-500 focus:border-blue-500" type="text" placeholder="Search for products..." aria-label="Search" name="query" value="${requestScope.query}">
                            <button class="p-4 border border-gray-300 rounded-r focus:ring-blue-500 focus:border-blue-500 bg-orange-500 text-white font-bold" type="submit"><i class="fa-solid fa-magnifying-glass"></i></button>
                        </div>
                    </form>

                </div>

            </div>

            <!-- TABLE -->
            <div class="overflow-x-auto rounded-lg shadow-md">
                <table class="min-w-full bg-white border border-gray-200 text-md">
                    <thead class="bg-orange-400 text-white">
                        <tr>
                            <th class="px-2 py-3 border-l">ID</th>
                            <th class="px-2 py-3 border-l">Product</th>
                            <th class="px-2 py-3 border-l">Price</th>
                            <th class="px-2 py-3 border-l">Stock Count</th>
                            <th class="px-2 py-3 border-l">Category</th>
                            <th class="px-2 py-3 border-l">Release Date</th>
                            <th class="px-2 py-3 border-l">Special Filter</th>
                            <th class="px-2 py-3 border-l">General Category</th>
                            <th class="px-2 py-3 border-l">Ratings</th>
                            <th class="px-2 py-3 border-l">Last Modified</th>
                            <th class="px-2 py-3 border-l">Status</th>
                                <c:if test="${sessionScope.account ne null and sessionScope.account.role eq 'admin'}">
                                <th class="px-2 py-3 border-l">Actions</th>
                                </c:if>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="product" items="${productList}" varStatus="status">
                            <tr class="hover:bg-gray-100 transition duration-300">
                                <!--ID-->
                                <td class="px-2 py-3 border-b text-center">${product.productID}</td>

                                <!--Image - Name-->
                                <td class="px-2 py-3 border-b text-center w-[15%]">
                                    <a href="manageProductDetails?id=${product.productID}&type=${product.generalCategory}">
                                        <figure>
                                            <img loading="lazy" class="object-contain w-full" src="${product.imageURL}" alt="${product.productName}" title="${product.productName}" />
                                            <figcaption class="font-bold text-xs md:text-lg py-2">${product.productName}</figcaption>
                                        </figure>
                                    </a>
                                </td>

                                <!--Price-->
                                <td class="px-2 py-3 border-b text-center">
                                    <span><fmt:formatNumber value="${product.price}" type="number" groupingUsed="true" /></span>
                                    <span>đ</span>
                                </td>
                                <!--Stockcount-->
                                <td class="px-2 py-3 border-b text-center">${ product.specialFilter eq 'pre-order' ? 'N/A' : product.stockCount}</td>
                                <!--Category-->
                                <td class="px-2 py-3 border-b text-center">${product.specificCategory.categoryName}</td>
                                <!--Release Date-->
                                <td class="release-date px-2 py-3 border-b text-center">${product.releaseDate}</td>
                                <!--Special Filter-->
                                <td class="px-2 py-3 border-b text-center">
                                    <span class="px-3 py-1 rounded text-white ${product.specialFilter eq 'new' ? 'bg-yellow-500' : product.specialFilter eq 'pre-order' ?  'bg-blue-500' : 'bg-black'}">
                                    ${ product.specialFilter eq 'new' ? 'New' : product.specialFilter eq 'pre-order' ? 'Pre-Order' : 'Unset'}
                                    </span>
                                </td>
                                <!--Type-->
                                <td class="px-6 py-3 border-b text-center w-[7%]">
                                    <span class="px-3 py-1 rounded text-white ${product.generalCategory eq 'book' ? 'bg-orange-500' : product.generalCategory eq 'merch' ?  'bg-yellow-500' : 'bg-gray-500'}">
                                        ${empty product.generalCategory ? 'Uncategorized' : product.generalCategory eq 'book' ? 'Books' :  product.generalCategory eq 'merch' ? 'Merchandise' : ''}
                                    </span>
                                </td>
                                <!--Ratings-->
                                <td class="px-2 py-3 border-b text-center text-md text-yellow-400 w-[7%]">
                                    <div class="flex flex-row items-center gap-1 font-bold w-fit mx-auto">
                                        <span>${product.averageRating}</span><i class="fa-solid fa-star"></i>
                                    </div>
                                    <div class="text-black ">
                                        ${product.numberOfRating} reviews
                                    </div>
                                </td>
                                <!--Last Modified Time-->
                                <td class="modified-time px-2 py-3 border-b text-center">${product.lastModifiedTime}</td>
                                <!--Status-->
                                <td class="px-6 py-3 border-b text-center">
                                    <c:choose>
                                        <c:when test="${product.isActive}">
                                            <span class="bg-green-500 text-white py-1 px-3 rounded">Active</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="bg-red-500 text-white py-1 px-3 rounded">Inactive</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <!--Actions-->
                                <c:if test="${sessionScope.account ne null and sessionScope.account.role eq 'admin'}">
                                    <td class="px-2 py-3 border-b text-left w-[10%] text-md">
                                        <a href="updateProduct?id=${product.productID}&type=${product.generalCategory}" class="text-blue-500 hover:text-blue-700">
                                            <i class="fas fa-edit"></i> Update
                                        </a>
                                        <br/>
                                        <br/>
                                        
                                        <a href="importProduct?id=${product.productID}" class="text-yellow-500 hover:text-yellow-700">
                                            <i class="fa-solid fa-warehouse"></i> Import
                                        </a>
                                        <br/>
                                        <br/>
                                        <c:choose>
                                            <c:when test="${product.isActive}">
                                                <a class="text-red-500 hover:text-red-700 action-btn" href="javascript:void(0);" onclick="confirmAction('Do you really want to deactivate this product?', 'changeProductStatus?id=${product.productID}&action=deactivate')">
                                                    <i class="fas fa-lock"></i><span> Deactivate</span>
                                                </a>
                                            </c:when>
                                            <c:otherwise>
                                                <a class="text-green-500 hover:text-green-700 action-btn" href="javascript:void(0);" onclick="confirmAction('Do you really want to activate this product?', 'changeProductStatus?id=${product.productID}&action=activate')">
                                                    <i class="fas fa-unlock"></i><span> Activate</span>
                                                </a>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </c:if>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty productList}">
                            <tr>
                                <td class="px-2 py-3 border-b text-center text-gray-500 italic" colspan="10">No products found.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>

            <!-- Pagination Links -->
            <div class="flex justify-center mt-6">
                <nav aria-label="Page navigation">
                    <ul class="flex space-x-2">
                        <c:if test="${currentPage > 1}">
                            <li><a href="manageProductList?type=${type}&query=${query}&page=1" class="px-2 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">First</a></li>
                            <li><a href="manageProductList?type=${type}&query=${query}&page=${currentPage - 1}" class="px-2 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">Previous</a></li>
                            </c:if>

                        <c:forEach begin="${currentPage - 2 > 0 ? currentPage - 2 : 1}" 
                                   end="${currentPage + 2 < totalPages ? currentPage + 2 : totalPages}" 
                                   var="i">
                            <c:if test="${i > 0 && i <= totalPages}">
                                <li><a href="manageProductList?type=${type}&query=${query}&page=${i}" 
                                       class="px-2 py-2 ${i == currentPage ? 'bg-blue-700' : 'bg-blue-500'} text-white rounded hover:bg-blue-600">${i}</a></li>
                                </c:if>
                            </c:forEach>

                        <c:if test="${currentPage < totalPages}">
                            <li><a href="manageProductList?type=${type}&query=${query}&page=${currentPage + 1}" class="px-2 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">Next</a></li>
                            <li><a href="manageProductList?type=${type}&query=${query}&page=${totalPages}" class="px-2 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">Last</a></li>
                            </c:if>
                    </ul>
                </nav>
            </div>

            <!-- Hiển thị thông tin phân trang -->
            <div class="text-center mt-4 text-gray-600">
                Page ${currentPage} of ${totalPages} 
                <c:if test="${not empty productList}">
                    (Showing ${(currentPage - 1) * pageSize + 1} - ${currentPage * pageSize > totalProducts ? totalProducts : currentPage * pageSize} of ${totalProducts} products)
                </c:if>
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

            //            Pop-up message
            document.addEventListener('DOMContentLoaded', function () {
                const reqMessage = `${requestScope.message}`;
                const successfulMessage = `${requestScope.successfulMessage}`;
                const failedMessage = `${requestScope.failedMessage}`;
                if (reqMessage) {
                    Swal.fire({
                        icon: 'error',
                        text: reqMessage
                    });
                }
                if (successfulMessage) {
                    Swal.fire({
                        icon: 'success',
                        text: successfulMessage
                    });
                }
                if (failedMessage) {
                    Swal.fire({
                        icon: 'warning',
                        text: failedMessage
                    });
                }
            });

        </script>
        
    </body>
</html>
