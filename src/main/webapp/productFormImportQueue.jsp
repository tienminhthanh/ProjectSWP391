<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div id="title-group" class=" bg-orange-400 rounded-t-lg p-4">
    <h2 class="text-2xl font-bold text-white">Queue Imports</h2>
</div>

<div id="mergedForm" class="form-container active">
    <c:forEach var="entry" items="${importMap}" begin="0" end="0">
        <c:set var="name" value="${(entry.value)[0].product.productName}"/>
    </c:forEach>
    <form action="queueImport" method="post">

        <!--Product & Supplier-->
        <div class="form-group mb-8">
            <label class="!block !text-lg !w-1/5 !flex-none !min-w-0">Import Item:</label>
            <div class="pair-group !flex !flex-col !w-4/5 !items-baseline gap-8 pl-0">
                <span class="!w-full !pl-0 ">
                    <label class="!block !text-left !w-full" for="product-selector">Product:</label>
                    <select class="!w-full" id="product-selector" name="product" required>
                        <c:forEach var="pro" items="${productList}">
                            <option value="${pro.productID}">${pro.productName} (In stock: ${pro.stockCount} - Price: ${pro.price} - Release: ${pro.releaseDate})</option>
                        </c:forEach>
                    </select>
                </span>
                <span class="!w-full !pl-0 !flex-col">
                    <label class="!block !text-left " for="supplier-selector">Supplier:</label>
                    <select class="w-1/3" id="supplier-selector" name="supplier" required>
                        <c:forEach var="sup" items="${supplierList}">
                            <option value="${sup.supplierID}">${sup.supplierName}</option>
                        </c:forEach>
                    </select>
                </span>
            </div>
        </div>

        <!--Price - Quantity-->
        <div class="form-group">
            <label class="!block !text-lg !w-1/5 !flex-none !min-w-0">Import Details:</label>
            <div class="pair-group !items-baseline !flex !flex-col !items-cemter !w-4/5 gap-8 pl-0">
                <span class="!w-1/3 !pl-0 !flex-col">
                    <label class="!block !text-left" for="priceBook">Price:</label>
                    <input class="!w-full" type="number" id="priceBook" name="price" step="0.5" min="1" max="10000" required>
                </span>
                <span class="!w-1/3 !pl-0">
                    <label class="!block !text-left" for="stockCountBook">Quantity:</label>
                    <input class="!w-full" type="number" id="stockCountBook" name="quantity" min="1"  max="1000" required>
                </span>
            </div>
        </div>



        <!--Submit-->
        <button class="add-product-btn" name="action" type="submit" value="queue">
            Save
        </button>
    </form>
</div>