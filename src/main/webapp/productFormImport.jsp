<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div id="title-group" class=" bg-orange-400 rounded-t-lg p-4">
    <h2 class="text-2xl font-bold text-white">Import Products</h2>
</div>

<div id="mergedForm" class="form-container active">
    <c:forEach var="entry" items="${importMap}" begin="0" end="0">
        <c:set var="name" value="${(entry.value)[0].product.productName}"/>
    </c:forEach>
    <form action="importProduct" method="post">
        <!--JSON data holder-->
        <div class="hidden" id="data-container" data-map="${fn:escapeXml(jsonMap)}"></div>
        
        <!--ID-->
        <div class="form-group">
            <label class="!w-1/5 !min-w-0 !flex-none" for="productIDBook">Product ID:</label>
            <input type="text" id="productIDBook" name="productID" maxlength="255" readonly required value="${param.id}">
        </div>

        <!--Name-->
        <div class="form-group">
            <label class="!w-1/5 !min-w-0 !flex-none" for="productNameBook">Product Name:</label>
            <input type="text" id="productNameBook" name="productName" maxlength="255" readonly value="${name}">
        </div>


        <!--Supplier - Import Details-->
        <div class="form-group flex flex-row">
            <label class="!w-1/5 !min-w-0 !flex-none">Import Details:</label>
            <div class="pair-group">
            <span class="supplier-wrapper w-1/2 !pl-0">
                <label class="w-fit" for="supplier">Supplier:</label>
                <select class="w-fit" id="supplier" name="supplier" required>
                    <c:forEach var="item" items="${importMap}">
                        <option value="${item.key.supplierID}">${item.key.supplierName}</option>
                    </c:forEach>
                </select>
            </span>
            <span class="import-details w-1/2">
            </span>
            </div>
        </div>


        <!--Submit-->
        <button class="add-product-btn" name="action" type="submit" value="import">
            Import
        </button>
    </form>
</div>