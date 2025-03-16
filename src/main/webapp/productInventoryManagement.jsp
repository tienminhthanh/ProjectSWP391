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
                    </c:choose>
                </div>
            </div>
        </div>


        <!--Tailwind-->
        <script src="https://cdn.tailwindcss.com"></script>

        <!-- SweetAlert2 -->
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>


        <!--Forms-->
        <script src="js/scriptProductForms.js"></script>
        <script>
            //Initial display on load
            document.addEventListener('DOMContentLoaded', function () {
                const formAction = `${requestScope.formAction}`;

                if (formAction === 'add') {
                    toggleForm();
                } else if (formAction === 'update') {
                    const activeForm = `${requestScope.type}` === 'book' ? document.getElementById("bookForm")
                            : `${requestScope.type}` === 'merch' ? document.getElementById("merchForm")
                            : '';
                    if (activeForm) {
                        activeForm.classList.add('active');
                    }
                }


            });
            
            //            Pop-up message
            document.addEventListener('DOMContentLoaded',function(){
                const reqMessage = `${requestScope.message}`;
                const errMessage = `${requestScope.errorMessage}`;
                if(reqMessage){
                    Swal.fire({
                        icon: 'success',
                        html: reqMessage,
                        confirmButtonText:'Back to Product List'
                    }).then((result) => {
                        if (result.isConfirmed) {
                            window.location.href = 'manageProductList';
                        }
                    });
                }
                if(errMessage){
                    Swal.fire({
                        icon: 'error',
                        html: errMessage,
                        confirmButtonText:'Back to Product List'
                    }).then((result) => {
                        if (result.isConfirmed) {
                            window.location.href = 'manageProductList';
                        }
                    });
                }
            });
        </script>

    </body>

</html>