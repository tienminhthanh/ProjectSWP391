<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="en_US"/>
<html>
    <head>
        <meta charset="UTF-8">
        <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
        <title>List Product Available For Event</title>
        <link rel="stylesheet" href="css/styleProductCard.css"/>
        <script src="https://cdn.tailwindcss.com"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"/>
        <link href="https://cdn.jsdelivr.net/npm/sweetalert2@11.6.9/dist/sweetalert2.min.css" rel="stylesheet">
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11.6.9/dist/sweetalert2.all.min.js"></script>
    </head>
    <body class="bg-gray-100 min-h-screen flex">
        <div class="w-64 bg-orange-400 text-white min-h-screen">
            <jsp:include page="navbarAdmin.jsp" flush="true"/> 
        </div>

        <div class="flex-1 p-6">
            <div class="mb-8 w-full max-w-full bg-white p-8 shadow-lg rounded-lg">
                <h1 class="text-3xl font-bold text-gray-800 mb-6">📌 Available Products for Event</h1>
                <hr class="mb-6 border-gray-300"/>
                <h1 class="text-3xl font-bold uppercase pt-4 pb-4 text-center">
                    ${eventName}
                </h1>
                <div class="mt-6 flex flex-col items-start"> 
                    <c:if test="${not empty message}">
                        <script>
                            Swal.fire({
                                icon: 'error',
                                title: 'Error',
                                text: '${message}',
                            });
                        </script>
                    </c:if>
                    <form action="eventProductAddNew" method="get" class="mb-0 flex flex-wrap items-center gap-4 bg-gray-100 p-4 rounded-lg shadow-md w-full">
                        <!-- Status Filter -->
                        <input type="hidden" name="eventId" value="${param.eventId}" />
                        <div class="flex items-center space-x-2">
                            <label class="text-gray-700 font-semibold">Category</label>
                            <select name="category" onchange="this.form.submit()" 
                                    class="p-2 border border-gray-300 rounded focus:ring-blue-500 focus:border-blue-500">
                                <option value="" ${empty param.category ? 'selected' : ''}>All</option>
                                <option value="Light Novel" ${param.category == 'Light Novel' ? 'selected' : ''}>Light Novel</option>
                                <option value="Manga" ${param.category == 'Manga' ? 'selected' : ''}>Manga</option>
                                <option value="Figure" ${param.category == 'Figure' ? 'selected' : ''}>Figure</option>
                                <option value="Nendoroid" ${param.category == 'Nendoroid' ? 'selected' : ''}>Nendoroid</option>
                            </select>
                        </div>

                        <!-- Search Input -->
                        <div class="flex-grow">
                            <input type="text" name="search" placeholder="Search products..." value="${param.search}" 
                                   class="w-full p-2 border border-gray-300 rounded focus:ring-blue-500 focus:border-blue-500"/>
                        </div>

                        <!-- Search Button -->
                        <button type="submit" 
                                class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition duration-200 flex items-center">
                            🔍 Search
                        </button>
                    </form>
                </div>

                <div class="sticky top-0 bg-white p-4 z-10 flex justify-end">
                    <div class="flex items-center space-x-2">
                        <label class="text-gray-700 font-semibold">Enter Discount for all selected</label>
                        <input type="number" id="globalDiscount" class="p-2 border rounded w-20 text-center" 
                               placeholder="%" min="1" max="99" step="1">
                        <button type="button" onclick="applyDiscountToAll()" 
                                class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition duration-200">
                            Apply to All
                        </button>
                    </div>
                    <div class="ml-2 flex items-center space-x-2">
                        <button type="button" onclick="selectAll()" 
                                class="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600 transition duration-200">
                            Select All
                        </button>
                        <button type="button" onclick="deselectAll()" 
                                class="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600 transition duration-200">
                            Deselect All
                        </button>
                    </div>
                </div>


                <div class="overflow-x-auto rounded-lg shadow-md">
                    <form id="productForm" action="eventProductAddNew?eventId=${param.eventId}" method="post">
                        <table class="table-fixed min-w-full bg-white border border-gray-200">
                            <thead class="bg-orange-400 text-white">
                                <tr>
                                    <th class="border border-gray-300 px-4 py-2">ID</th>
                                    <th class="px-4 py-3 border">Image</th>
                                    <th class="border border-gray-300 px-4 py-2">Product Name</th>
                                    <th class="px-4 py-3 border">Category</th>
                                    <th class="px-4 py-3 border">Stock</th>
                                    <th class="border border-gray-300 px-4 py-2">Price</th>
                                    <th class="border border-gray-300 px-4 py-2">Choose</th>
                                    <th class="border border-gray-300 px-4 py-2">Discount</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="product" items="${productList}">
                                    <tr class="hover:bg-gray-100 cursor-pointer" onclick="toggleCheckbox('${product.productID}', ${product.stockCount})">
                                        <td class="border border-gray-300 px-4 py-2">${product.productID}</td>
                                        <td class="px-4 py-3 border border-gray-300 text-center">
                                            <img src="${product.imageURL}" alt="${product.productName}" class="w-16 h-16 object-cover rounded"/>
                                        </td>
                                        <td class="border border-gray-300 px-4 py-2">${product.productName}</td>
                                        <td class="px-4 py-3 border border-gray-300 text-center">${product.specificCategory.categoryName}</td>
                                        <td class="px-4 py-3 border border-gray-300 text-center">${product.stockCount}</td>
                                        <td class="border border-gray-300 px-4 py-2 text-right">
                                            <fmt:formatNumber value="${product.price}" type="number" groupingUsed="true" /> đ
                                        </td>
                                        <td class="border border-gray-300 px-4 py-2 text-center">
                                            <input type="checkbox" id="checkbox_${product.productID}" name="selectedProducts" value="${product.productID}" class="w-6 h-6"
                                                   <c:if test="${product.stockCount == 0}">disabled</c:if> 
                                                   onclick="event.stopPropagation(); toggleDiscount('${product.productID}')">
                                        </td>
                                        <td class="border border-gray-300 px-4 py-2 text-center">
                                            <input type="number" id="discount_${product.productID}"
                                                   name="discountPercent_${product.productID}" 
                                                   class="w-20 p-1 border rounded text-center" 
                                                   min="1" max="99" step="1"
                                                   disabled
                                                   onclick="event.stopPropagation();">
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>

                        </table>

                        <!-- Nút Add bên dưới bảng -->
                        <div class="mt-4 text-center">
                            <button type="button" class="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 w-48 ease-in-out transform hover:scale-105 items-center justify-start transition duration-300" 
                                    onclick="addSelectedProducts()">
                                Add Product To Event
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <script>
            function toggleCheckbox(productID, stockCount) {
                if (stockCount === 0)
                    return; // Không làm gì nếu hết hàng

                let checkbox = document.getElementById("checkbox_" + productID);
                let discountInput = document.getElementById("discount_" + productID);

                // Đảo trạng thái checkbox
                checkbox.checked = !checkbox.checked;

                // Bật/tắt ô nhập discount
                if (checkbox.checked) {
                    discountInput.disabled = false;
                } else {
                    discountInput.disabled = true;
                    discountInput.value = ""; // Reset discount nếu bỏ chọn
                }
            }

// Ngăn việc nhấn vào checkbox làm kích hoạt sự kiện click trên hàng
            function toggleDiscount(productID) {
                let checkbox = document.getElementById("checkbox_" + productID);
                let discountInput = document.getElementById("discount_" + productID);

                if (checkbox.checked) {
                    discountInput.disabled = false;
                } else {
                    discountInput.disabled = true;
                    discountInput.value = "";
                    discountInput.classList.remove("border-red-500");
                }
            }

        </script>
        <script>
            function validateDiscount(input) {
                let value = input.value.trim();
                if (value === "" || isNaN(value) || value < 1 || value > 99) {
                    input.classList.add("border-red-500");
                    input.classList.remove("border-gray-300");
                    input.value = "";
                    Swal.fire({
                        icon: 'error',
                        title: 'Invalid Discount',
                        text: 'Discount must be between 1 and 99.',
                    });
                } else {
                    input.classList.remove("border-red-500");
                    input.classList.add("border-gray-300");
                }
            }

            function addSelectedProducts() {
                let selectedProducts = [];
                let discountData = {};
                let errors = []; // 🔹 Thêm dòng này để tránh lỗi "errors is not defined"

                let checkboxes = document.querySelectorAll('input[name="selectedProducts"]:checked');
                for (let checkbox of checkboxes) {
                    let productID = checkbox.value;
                    let discountInput = document.getElementById("discount_" + productID);
                    let discount = discountInput.value.trim();

                    if (discount === "" || isNaN(discount) || discount < 1 || discount > 99) {
                        discountInput.classList.add("border-red-500");
                        errors.push(`Product ID ${productID}: Discount must be between 1% and 99%`);
                    } else {
                        discountInput.classList.remove("border-red-500");
                        selectedProducts.push(productID);
                        discountData[productID] = discount;
                    }
                }

                if (errors.length > 0) {
                    Swal.fire({
                        icon: 'error',
                        title: 'Validation Error',
                        html: 'Some selected products have invalid discounts.<br>Please enter a discount between 1% and 99%.'
                    });
                    return;
                }

                if (selectedProducts.length === 0) {
                    Swal.fire({icon: 'warning', title: 'No Products Selected', text: 'At least 1 product must be selected.'});
                    return;
                }

                let form = document.getElementById("productForm");

                // 🔹 Xóa input hidden cũ (tránh lỗi chỉ gửi 1 sản phẩm)
                let oldInputs = form.querySelectorAll('input[type="hidden"]');
                oldInputs.forEach(input => input.remove());

                // 🔹 Tạo input hidden mới để gửi dữ liệu lên server
                selectedProducts.forEach(productID => {
                    let inputProduct = document.createElement("input");
                    inputProduct.type = "hidden";
                    inputProduct.name = "selectedProducts"; // 🔹 Định dạng mảng cho server
                    inputProduct.value = productID;
                    form.appendChild(inputProduct);
                });

                let inputDiscounts = document.createElement("input");
                inputDiscounts.type = "hidden";
                inputDiscounts.name = "discountData";
                inputDiscounts.value = JSON.stringify(discountData);
                form.appendChild(inputDiscounts);

                console.log("Submitting form with selectedProducts:", selectedProducts);
                console.log("Submitting form with discountData:", discountData);

                form.submit();
                localStorage.removeItem("selectedProducts");
            }



            function toggleDiscount(productID) {
                let checkbox = document.getElementById("checkbox_" + productID);
                let discountInput = document.getElementById("discount_" + productID);

                if (checkbox.checked) {
                    discountInput.disabled = false;
                } else {
                    discountInput.disabled = true;
                    discountInput.value = "";
                    discountInput.classList.remove("border-red-500");
                }
            }
        </script>
        <script>
            function applyDiscountToAll() {
                let globalDiscount = document.getElementById("globalDiscount").value.trim();

                // Kiểm tra nếu discount hợp lệ (1 - 99)
                if (globalDiscount === "" || isNaN(globalDiscount) || globalDiscount < 1 || globalDiscount > 99) {
                    Swal.fire({
                        icon: 'error',
                        title: 'Invalid Discount',
                        text: 'Discount must be between 1% and 99%.'
                    });
                    return;
                }

                // Áp dụng discount cho tất cả checkbox đã tick
                let checkboxes = document.querySelectorAll('input[name="selectedProducts"]:checked');
                if (checkboxes.length === 0) {
                    Swal.fire({
                        icon: 'warning',
                        title: 'No Products Selected',
                        text: 'Please select at least one product before applying the discount.'
                    });
                    return;
                }

                checkboxes.forEach(checkbox => {
                    let productID = checkbox.value;
                    let discountInput = document.getElementById("discount_" + productID);
                    discountInput.value = globalDiscount;
                    discountInput.disabled = false; // Bật ô nhập discount nếu chưa bật
                });

                Swal.fire({
                    icon: 'success',
                    title: 'Discount Applied',
                    text: `Applied ${globalDiscount}% discount to selected products.`
                });
            }
        </script>
        <script>
            function deselectAll() {
                let checkboxes = document.querySelectorAll('input[name="selectedProducts"]:checked');

                if (checkboxes.length === 0) {
                    Swal.fire({
                        icon: 'warning',
                        title: 'No Products Selected',
                        text: 'There are no selected products to deselect.'
                    });
                    return;
                }

                checkboxes.forEach(checkbox => {
                    checkbox.checked = false;
                    let discountInput = document.getElementById("discount_" + checkbox.value);
                    discountInput.disabled = true;
                    discountInput.value = "";
                });

                updateSelectedCount(); // Cập nhật số lượng sản phẩm đã chọn
            }

            function selectAll() {
                let checkboxes = document.querySelectorAll('input[name="selectedProducts"]:not(:checked)');
                let selectable = Array.from(checkboxes).filter(checkbox => !checkbox.disabled);

                if (selectable.length === 0) {
                    Swal.fire({
                        icon: 'warning',
                        title: 'No Products Available',
                        text: 'There are no available products to select.'
                    });
                    return;
                }

                selectable.forEach(checkbox => {
                    checkbox.checked = true;
                    let discountInput = document.getElementById("discount_" + checkbox.value);
                    discountInput.disabled = false;
                });

                updateSelectedCount(); // Cập nhật số lượng sản phẩm đã chọn
            }

        </script>
    </body>
</html>