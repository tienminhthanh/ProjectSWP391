<%-- 
    Document   : productManagementForm
    Created on : Mar 13, 2025, 12:32:45 PM
    Author     : anhkc
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Product Manangement - ${empty requestScope.formAction ? '' : requestScope.formAction eq 'add' ? 'Add New Products' : requestScope.formAction eq 'update' ? 'Update Products' : ''} WIBOOKS</title>

        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"/>
        <link href="https://cdn.jsdelivr.net/npm/sweetalert2@11.6.9/dist/sweetalert2.min.css" rel="stylesheet">

        <!--Forms-->
        <link rel="stylesheet" href="css/styleProductForms.css"/>

        <!--Tailwind-->
        <script src="https://cdn.tailwindcss.com"></script>

        <!-- SweetAlert2 -->
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11.6.9/dist/sweetalert2.all.min.js" defer></script>


        <!--Forms-->
        <script src="js/scriptProductForms.js" defer></script>
    </head>

    <body class="bg-gray-50 min-h-screen flex">
        <div class="w-64 bg-orange-400 text-white min-h-screen">
            <jsp:include page="navbarAdmin.jsp" flush="true"/> 
        </div>

        <!-- Main Content -->
        <div class="flex-1 p-6">
            <h1 class="text-3xl font-bold text-gray-800 mb-6">📌 Product Inventory Management</h1>
            <hr class="mb-6 border-gray-300"/>
            <div class="root-container bg-gray-100 w-full">
                <div class="bg-white w-full md:w-3/5 mx-4 md:mx-auto my-4 rounded-b-lg shadow-lg">
                    <c:choose>
                        <c:when test="${requestScope.formAction eq 'add'}">
                            <jsp:include page="productFormAdd.jsp" flush="true"/>
                        </c:when>
                        <c:when test="${requestScope.formAction eq 'update'}">
                            <jsp:include page="productFormUpdate.jsp" flush="true"/>
                        </c:when>
                        <c:when test="${requestScope.formAction eq 'import'}">
                            <jsp:include page="productFormImport.jsp" flush="true"/>
                        </c:when>
                        <c:when test="${requestScope.formAction eq 'queue'}">
                            <jsp:include page="productFormImportQueue.jsp" flush="true"/>
                        </c:when>
                    </c:choose>
                </div>
            </div>
        </div>



        <script>
            //Initial display on load
            document.addEventListener('DOMContentLoaded', function () {
                const formAction = `${requestScope.formAction}`;
                if (formAction === 'add') {
                    toggleForm();
                } else if (formAction === 'update') {
                    const creatorSection = document.querySelector(".creator-section");
                    if (creatorSection && creatorSection.children.length > 0) {
                        [...creatorSection.children].forEach(child => {
                            const options = [...child.querySelectorAll('select option')].filter(option => option.value !== 'artist');
                            if (options) {
                                options.forEach(option => {
                                    option.classList.toggle("hidden", (`${requestScope.type}` === "book" && option.value === 'sculptor') || (`${requestScope.type}` === 'merch' && option.value === 'author'));
                                    option.selected = option.classList.contains('hidden') ? false : true;
                                });
                            }
                        });
                    }
                } 
            });


            //            Pop-up message
            document.addEventListener('DOMContentLoaded', function () {
                const reqMessage = `${requestScope.message}`;
                const errMessage = `${requestScope.errorMessage}`;
                if (reqMessage) {
                    Swal.fire({
                        icon: 'success',
                        html: reqMessage,
                        confirmButtonText: 'Back to Product List'
                    }).then((result) => {
                        if (result.isConfirmed) {
                            window.location.href = 'manageProductList';
                        }
                    });
                }
                if (errMessage) {
                    Swal.fire({
                        icon: 'error',
                        html: errMessage,
                        confirmButtonText: 'Back to Product List'
                    }).then((result) => {
                        if (result.isConfirmed) {
                            window.location.href = 'manageProductList';
                        }
                    });
                }
            });

            //Handle import form
            document.addEventListener('DOMContentLoaded', function () {
                if (`${requestScope.formAction}` !== 'import') {
                    return;
                }

                //Get json data
                const jsonMap = document.getElementById("data-container").getAttribute("data-map");
                const jsonObject = JSON.parse(jsonMap);
                if (!jsonObject) {
                    console.log("Import items not found!");
                    return;
                }


                const details = document.querySelector('.import-details');
                const supplierSelector = document.getElementById('supplier');

                if (!details || !supplierSelector) {
                    return;
                }


                function handleItems() {
                    const selected = supplierSelector.value;
                    if (!selected) {
                        console.error("Cannot retrieve value from supplierSelector!");
                        return;
                    }
                    const selectedItems = jsonObject[selected];
                    details.innerHTML = '';
                    selectedItems.forEach((item, index) => {

                        //Format importDate
                        let impDate = item['importDate'];
                        let year = impDate['year'];
                        let month = String(impDate['month']).padStart(2, '0');
                        let date = String(impDate['day']).padStart(2, '0');
                        item['importDate'] = year + "-" + month + "-" + date;

                        //Format releaseDate
                        let rlsDate = item['product']['releaseDate'];
                        year = rlsDate['year'];
                        month = String(rlsDate['month']).padStart(2, '0');
                        date = String(rlsDate['day']).padStart(2, '0');
                        item['product']['releaseDate'] = year + "-" + month + "-" + date;


                        //Create checkbox and assign JSON data to its value
                        const detailEl = document.createElement('div');
                        const inputEl = document.createElement('input');
                        inputEl.type = 'checkbox';
                        inputEl.name = 'importItem';
                        inputEl.id = "checkbox-item" + index;
                        inputEl.value = encodeURIComponent(JSON.stringify(item));

                        //Format price display for label
                        let price = item['importPrice'];
                        price = parseFloat(price);
                        price = !isNaN(price) ? Math.round(price).toLocaleString() + " đ" : '';

                        //Create label for checkbox
                        const labelEl = document.createElement('label');
                        labelEl.htmlFor = "checkbox-item" + index;
                        labelEl.textContent = "Price: " + price + " -  Quantity: " + item['importQuantity'];

                        //Append to container
                        detailEl.append(inputEl, labelEl);
                        details.appendChild(detailEl);
                    });
                }
                ;

                //Onload
                handleItems();
                //Onchange of select tag
                supplierSelector.addEventListener("change", handleItems);


            });
        </script>

    </body>

</html>