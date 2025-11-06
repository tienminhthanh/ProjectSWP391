<%-- 
    Document   : queueTest
    Created on : Oct 31, 2025, 2:40:58 PM
    Author     : anhkc
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<div id="title-group" class=" bg-orange-400 rounded-t-lg p-4">
    <h2 class="text-2xl font-bold text-white">Queue Imports</h2>
</div>
<div class="overflow-x-auto rounded-lg shadow-md">
    <form id="queueImportForm" action="queueImport" method="post" >
        <table class="table-fixed min-w-full bg-white border border-gray-200">
            <thead class="bg-orange-400 text-white">
                <tr>
                    <th class="border border-gray-300 px-4 py-2">Supplier</th>
                    <th class="px-4 py-3 border">Image</th>
                    <th class="border border-gray-300 px-4 py-2">Product</th>
                    <th class="px-4 py-3 border">Stock</th>
                    <th class="border border-gray-300 px-4 py-2">Price</th>
                    <th class="border border-gray-300 px-4 py-2">Import Price</th>
                    <th class="border border-gray-300 px-4 py-2">Choose</th>
                    <th class="border border-gray-300 px-4 py-2">Minimum Import Quantity</th>
                    <th class="border border-gray-300 px-4 py-2">Import Quantity</th>
                    <th class="border border-gray-300 px-4 py-2">Maximum Import Quantity</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="ps" items="${productSupplyList}">
                    <tr class="hover:bg-gray-100 cursor-pointer" onclick="toggleCheckbox('${ps.hashedID}')">
                        <td class="border border-gray-300 px-4 py-2">${ps.supplier.supplierName}</td>
                        <td class="px-4 py-3 border border-gray-300 text-center">
                            <img src="${ps.product.imageURL}" alt="${ps.product.productName}" class="w-16 h-16 object-cover rounded"/>
                        </td>
                        <td class="border border-gray-300 px-4 py-2">${ps.product.productName}</td>
                        <td class="px-4 py-3 border border-gray-300 text-center">${ps.product.stockCount}</td>
                        <td class="border border-gray-300 px-4 py-2 text-right">
                            <fmt:formatNumber value="${ps.product.price}" type="number" groupingUsed="true" /> đ
                        </td>
                        <td class="border border-gray-300 px-4 py-2 text-right">
                            <fmt:formatNumber value="${ps.defaultImportPrice}" type="number" groupingUsed="true" /> đ
                        </td>
                        <td class="border border-gray-300 px-4 py-2 text-center">
                            <input type="checkbox" id="checkbox_${ps.hashedID}" name="selectedImports" value="${ps.hashedID}" class="w-6 h-6"
                                   <c:if test="${ps.product.stockCount == 0}"></c:if> 
                                   onclick="event.stopPropagation(); toggleQuant('${ps.hashedID}')">
                        </td>
                        <td class="border border-gray-300 px-4 py-2 text-center">
                            ${ps.minImportQuant}
                        </td>
                        <td class="border border-gray-300 px-4 py-2 text-center">
                            <input type="number" id="importQuant_${ps.hashedID}"
                                   name="importQuant_${ps.hashedID}" 
                                   class="w-20 p-1 border rounded text-center" 
                                   min="${ps.minImportQuant}" max="${ps.maxImportQuant}" step="1"
                                   disabled
                                   onclick="event.stopPropagation();">
                        </td>
                        <td class="border border-gray-300 px-4 py-2 text-center">
                            ${ps.maxImportQuant}
                        </td>
                    </tr>
                </c:forEach>
            </tbody>


        </table>

    </form>

</div>
<!-- Nút Add bên dưới bảng -->
<div class="mt-4 text-center fixed top-1/2 right-0 -translate-y-1/2">
    <button type="button" 
            class="bg-green-600 text-white px-4 py-2 rounded-l-3xl hover:bg-green-700 w-48 ease-in-out transform hover:scale-105 items-center justify-start transition duration-300 text-xl" 
            onclick="queueSelectedImports()">
        Queue Selected Imports
    </button>
</div>
<script>
    function toggleCheckbox(hashed) {


        let checkbox = document.getElementById("checkbox_" + hashed);
        let importQuantInput = document.getElementById("importQuant_" + hashed);

        // Đảo trạng thái checkbox
        checkbox.checked = !checkbox.checked;

        // Bật/tắt ô nhập discount
        if (checkbox.checked) {
            importQuantInput.disabled = false;
        } else {
            importQuantInput.disabled = true;
            importQuantInput.value = ""; // Reset discount nếu bỏ chọn
        }
    }

    // Ngăn việc nhấn vào checkbox làm kích hoạt sự kiện click trên hàng
    function toggleQuant(hashed) {
        let checkbox = document.getElementById("checkbox_" + hashed);
        let importQuantInput = document.getElementById("importQuant_" + hashed);

        if (checkbox.checked) {
            importQuantInput.disabled = false;
        } else {
            importQuantInput.disabled = true;
            importQuantInput.value = "";
            importQuantInput.classList.remove("border-red-500");
        }
    }


    function queueSelectedImports() {
        const productSupplyList = JSON.parse('<c:out value="${productSupplyListJSON}" escapeXml="false"/>');
        let selectedImports = [];
        let importData = {};
        let errors = []; // 🔹 Thêm dòng này để tránh lỗi "errors is not defined"

        let checkboxes = document.querySelectorAll('input[name="selectedImports"]:checked');

        const selectedProductSupplies = getSelectedProductSupplies(productSupplyList, checkboxes);
        for (let i = 0; i < checkboxes.length; i++) {
            let checkbox = checkboxes[i];
            let productSupply = selectedProductSupplies[i];
            let minQuant = productSupply.minImportQuant;
            let maxQuant = productSupply.maxImportQuant;
            let psHasedID = checkbox.value;
            let importQuantInput = document.getElementById("importQuant_" + psHasedID);
            let importQuant = importQuantInput.value.trim();

            if (importQuant === "" || isNaN(importQuant) || importQuant < minQuant || importQuant > maxQuant) {
                importQuantInput.classList.add("border-red-500");
                errors.push(`Product Supply ID ${psHasedID}: importQuant must be between ${minQuant} and ${maxQuant}`);
            } else {
                importQuantInput.classList.remove("border-red-500");
                selectedImports.push(psHasedID);

                const {productID, supplierID, defaultImportPrice} = productSupply;
                const importItem = {
                    productID,
                    supplierID,
                    importPrice: defaultImportPrice,
                    importQuantity: importQuant,
                    importDate: new Date().toISOString().split('T')[0],
                    isImported: false
                };


                importData[psHasedID] = importItem;
            }
        }

        if (errors.length > 0) {
            Swal.fire({
                icon: 'error',
                title: 'Validation Error',
                html: 'Some selected imports have invalid import quantity.'
            });
            return;
        }

        if (selectedImports.length === 0) {
            Swal.fire({icon: 'warning', title: 'No Imports Selected', text: 'At least 1 import must be selected.'});
            return;
        }

        let form = document.getElementById("queueImportForm");

        // 🔹 Xóa input hidden cũ (tránh lỗi chỉ gửi 1 sản phẩm)
        let oldInputs = form.querySelectorAll('input[type="hidden"]');
        oldInputs.forEach(input => input.remove());

        // 🔹 Tạo input hidden mới để gửi dữ liệu lên server
        selectedImports.forEach(psHasedID => {
            let inputImport = document.createElement("input");
            inputImport.type = "hidden";
            inputImport.name = "selectedImports"; // 🔹 Định dạng mảng cho server
            inputImport.value = psHasedID;
            form.appendChild(inputImport);
        });

        let inputImport = document.createElement("input");
        inputImport.type = "hidden";
        inputImport.name = "importData";
        inputImport.value = JSON.stringify(importData);
        form.appendChild(inputImport);

        console.log("Submitting form with selectedImports:", selectedImports);
        console.log("Submitting form with importData", importData);

        form.submit();
        localStorage.removeItem("selectedImports");
    }

    function getSelectedProductSupplies(productSupplyList, checkboxes) {
        const selectedProductSupplies = [];
        checkboxes.forEach(checkbox => {
            const selectedPsHashedID = checkbox.value;
            const matched = productSupplyList.find(ps => ps.hashedID.toString() === selectedPsHashedID);
            if (matched) {
                selectedProductSupplies.push(matched);
            }
        });
        return selectedProductSupplies;
    }

</script>