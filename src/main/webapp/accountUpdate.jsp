<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
        <title>Update Account</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"/>
    </head>
    <body class="bg-gray-100 min-h-screen flex flex-col">
        <header class="bg-white shadow w-full">
            <a href="${sessionScope.account.role == 'admin' ? 'listAccount' : (sessionScope.account.role == 'shipper' ? 'dashboardShipper.jsp' : 'home')}"  class="container mx-auto px-4 py-2 flex justify-between items-center">
                <img alt="WIBOOKS Logo" class="h-10" height="50" src="./img/logoWibooks-removebg-preview.png" width="200"/>
                <div class="flex items-center space-x-4">
                    <i class="fas fa-globe text-xl"></i>
                </div>
            </a>
        </header>
        <main class="flex-grow flex items-center justify-center">
            <div class="w-full max-w-4xl bg-white p-8 shadow-md">
                <h1 class="text-2xl font-semibold mb-4">Update Account</h1>
                <hr class="mb-6"/>
                <c:if test="${not empty message}">
                    <div style="color: red;">
                        ${sessionScope.message}
                    </div>

                    <c:set var="message" value="${sessionScope.message}" scope="session" />
                    <c:remove var="message" scope="session" />
                </c:if>
                <form action="updateAccount" method="post" id="updateAccountForm">
                    <input type="hidden" name="username" value="${account.username}">
                    <input type="hidden" id="actionType" name="actionType" value="updateAccount">
                    <input type="hidden" id="selectedAddress" name="selectedAddress" value="">

                    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div class="mb-4">
                            <label class="sr-only" for="firstName">First Name</label>
                            <input class="w-full p-3 border border-gray-300 rounded" id="firstName" name="firstName"
                                   placeholder="First Name" required type="text" value="${account.firstName}"/>
                        </div>
                        <div class="mb-4">
                            <label class="sr-only" for="lastName">Last Name</label>
                            <input class="w-full p-3 border border-gray-300 rounded" id="lastName" name="lastName"
                                   placeholder="Last Name" required type="text" value="${account.lastName}"/>
                        </div>
                        <div class="mb-4">
                            <label class="sr-only" for="email">Email</label>
                            <input class="w-full p-3 border border-gray-300 rounded" id="email" name="email"
                                   placeholder="Email" required type="email" value="${account.email}"/>
                        </div>
                        <div class="mb-4">
                            <label class="sr-only" for="phoneNumber">Phone Number</label>
                            <input class="w-full p-3 border border-gray-300 rounded" id="phoneNumber" name="phoneNumber"
                                   placeholder="Phone Number" required type="text" value="${account.phoneNumber}"/>
                        </div>
                        <div class="mb-4">
                            <label class="sr-only" for="birthDate">Birth Date</label>
                            <input class="w-full p-3 border border-gray-300 rounded" id="birthDate" name="birthDate"
                                   placeholder="Birth Date" required type="date" value="${account.birthDate}"/>
                        </div>

                        <c:if test="${sessionScope.account.role == 'customer'}">
                            <div class="mb-4 relative">
                                <label class="sr-only" for="defaultDeliveryAddress">Default Delivery Address</label>

                                <div class="flex">
                                    <input class="w-full p-3 border border-gray-300 rounded-l" 
                                           id="defaultDeliveryAddress" name="defaultDeliveryAddress"
                                           placeholder="Default Delivery Address" type="text" 
                                           value="${sessionScope.account.defaultDeliveryAddress}" readonly />

                                    <button type="button" id="toggleDropdown" class="px-4 border border-gray-300 bg-gray-200 rounded-r">
                                        ▼
                                    </button>
                                </div>

                                <ul id="addressList" class="absolute left-0 mt-1 w-full bg-white border border-gray-300 rounded shadow-md hidden z-10">
                                    <c:forEach var="address" items="${addressList}">
                                        <li class="flex justify-between px-4 py-2 hover:bg-gray-200 cursor-pointer address-item">
                                            <span data-value="${address.addressDetails}" class="flex-1">${address.addressDetails}</span>

                                            <button type="button" class="delete-address text-red-500 hover:text-red-700" 
                                                    data-id="${address.addressID}">
                                                Delete
                                            </button>
                                        </li>
                                    </c:forEach>

                                    <li id="addNewAddress" class="px-4 py-2 text-blue-500 hover:bg-gray-200 cursor-pointer font-semibold">
                                        + Add New Address
                                    </li>
                                </ul>

                                <div id="newAddressContainer" class="hidden mt-2">
                                    <input type="text" id="newAddressInput" class="w-full p-2 border border-gray-300 rounded"
                                           placeholder="Enter new address..." />

                                    <button type="button" id="saveNewAddress" class="mt-2 px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">
                                        Save
                                    </button>
                                </div>
                            </div>
                        </c:if>

                        <c:if test="${sessionScope.account.role eq 'admin'}">
                            <div class="mb-4">
                                <label class="sr-only" for="role">Role</label>
                                <select class="w-full p-3 border border-gray-300 rounded" id="role" name="role">
                                    <option value="customer" ${account.role eq 'customer' ? 'selected' : ''}>Customer</option>
                                    <option value="staff" ${account.role eq 'staff' ? 'selected' : ''}>Staff</option>
                                    <option value="shipper" ${account.role eq 'shipper' ? 'selected' : ''}>Shipper</option>
                                    <option value="admin" ${account.role eq 'admin' ? 'selected' : ''}>Admin</option>
                                </select>
                            </div>
                        </c:if>
                    </div>

                    <button class="w-full bg-blue-600 text-white p-3 rounded hover:bg-blue-700 mt-4" type="submit">Update</button>
                </form>


                <div class="mt-6">
                    <a href="${sessionScope.account.role == 'admin' ? 'listAccount' : 'readAccount?username='}${sessionScope.account.username}"  
                       class="text-blue-600 hover:underline">
                        <i class="fas fa-arrow-left mr-2"></i> Back
                    </a>
                </div>


            </div>
        </main>
        <footer class="bg-gray-200 py-4 mt-8 w-full">
            <div class="container mx-auto px-4 text-center text-sm text-gray-600">
                <a class="mr-4" href="#">Privacy</a>
                <a href="#">Purchase Terms &amp; Conditions</a>
                <p class="mt-4">© WIBOOKS Co.,Ltd.</p>
            </div>
        </footer>
        <script>
            document.addEventListener("DOMContentLoaded", function () {
                const addressList = document.getElementById("addressList");
                const toggleDropdown = document.getElementById("toggleDropdown");
                const defaultAddressInput = document.getElementById("defaultDeliveryAddress");
                const newAddressContainer = document.getElementById("newAddressContainer");
                const newAddressInput = document.getElementById("newAddressInput");
                const saveNewAddress = document.getElementById("saveNewAddress");
                const updateForm = document.getElementById("updateAccountForm");
                const actionType = document.getElementById("actionType");
                const selectedAddress = document.getElementById("selectedAddress");

                toggleDropdown.addEventListener("click", function () {
                    addressList.classList.toggle("hidden");
                });

                document.querySelectorAll(".address-item span").forEach(item => {
                    item.addEventListener("click", function () {
                        defaultAddressInput.value = this.getAttribute("data-value");
                        addressList.classList.add("hidden");
                    });
                });

                document.getElementById("addNewAddress").addEventListener("click", function () {
                    newAddressContainer.classList.remove("hidden");
                    addressList.classList.toggle("hidden")
                    newAddressInput.focus();
                });

                saveNewAddress.addEventListener("click", function () {
                    if (newAddressInput.value.trim() !== "") {
                        actionType.value = "addAddress";
                        selectedAddress.value = newAddressInput.value;
                        updateForm.submit();
                    }
                });

                document.querySelectorAll(".delete-address").forEach(button => {
                    button.addEventListener("click", function () {
                        actionType.value = "deleteAddress";
                        selectedAddress.value = this.getAttribute("data-id");
                        updateForm.submit();
                    });
                });
            });
        </script>
    </body>
</html>
