<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="title-group" class=" bg-orange-400 rounded-t-lg p-4">
    <h2 class="text-2xl font-bold text-white">Update ${formTitle}</h2>
</div>

<div id="mergedForm" class="form-container active">
    <form action="updateProduct" method="post">

        <!--ID-->
        <div class="form-group">
            <label for="productIDBook">Product ID:</label>
            <input type="text" id="productIDBook" name="productID" maxlength="255" readonly required value="${product.productID}">
        </div>

        <!--Name-->
        <div class="form-group">
            <label for="productNameBook">Product Name:</label>
            <input type="text" id="productNameBook" name="productName" maxlength="255" required value="${product.productName}">
        </div>

        <!--Price - Stock count-->
        <div class="form-group">
            <label>Details:</label>
            <div class="pair-group">
                <label for="priceBook">Price(VND):</label>
                <input type="number" id="priceBook" name="price" step="500" min="1000" max="10000000" required value="${product.price}">
                <label for="stockCountBook">Stock Count:</label>
                <input type="number" id="stockCountBook" name="stockCount" min="0"  max="1000" required value="${product.stockCount}">
            </div>
        </div>

        <!--Category - Special Filter-->
        <div class="form-group">
            <label>Filters:</label>
            <div class="pair-group">
                <label for="categoryBook">Category:</label>
                <select id="categoryBook" name="category" required>
                    <c:forEach var="catEntry" items="${applicationScope.categories}">
                        <c:if test="${catEntry.key.generalCategory eq requestScope.type}">
                            <option value="${catEntry.key.categoryID}" ${product.specificCategory.categoryID eq catEntry.key.categoryID ? 'selected' : ''}>${catEntry.key.categoryName}</option>
                        </c:if>
                    </c:forEach>
                </select>
                <label for="specialFilterBook">Special Filter:</label>
                <select id="specialFilterBook" name="specialFilter" required>
                    <option value="unset" ${empty product.specialFilter or (product.specialFilter ne 'pre-order' and product.specialFilter ne 'new') ? 'selected' : ''}>Unset</option>
                    <option value="pre-order ${product.specialFilter eq 'pre-order' ? 'selected' : ''}">Pre-Order</option>
                    <option value="new" ${product.specialFilter eq 'new' ? 'selected' : ''}>New</option>
                </select>
            </div>
        </div>

        <!--Description-->
        <div class="form-group">
            <label for="descriptionBook">Description:</label>
            <textarea id="descriptionBook" name="description">${product.description}</textarea>
        </div>

        <!--ReleaseDate - Status-->
        <div class="form-group">
            <label>Availability:</label>
            <div class="pair-group">
                <label for="releaseDateBook">Release Date:</label>
                <input type="date" id="releaseDateBook" name="releaseDate" required value="${product.releaseDate}">
                <label for="isActiveBook">Status:</label>
                <select id="isActiveBook" name="isActive" required>
                    <option value="true" ${product.isActive ?  'selected' : ''}>Active</option>
                    <option value="false" ${!product.isActive ?  'selected' : ''}>Inactive</option>
                </select>
            </div>
        </div>

        <!--Keywords-->
        <div class="form-group">
            <label for="keywordsBook">Keywords:</label>
            <textarea id="keywordsBook" name="keywords">${product.keywords}</textarea>
        </div>

        <!--Image URL-->
        <div class="form-group">
            <label for="imageURLBook">Image URL:</label>
            <textarea id="imageURLBook" name="imageURL" maxlength="512">${product.imageURL}</textarea>
        </div>

        <!--Creators-->
        <div class="creator-wrapper">
            <div class="creator-section">
                <c:forEach var="cre" items="${creatorMap}" varStatus="loopStatus">
                    <div class="creator-group" id="creGrBook${loopStatus.index}">
                        <label for="creatorNameBook${loopStatus.index}">Creator Name:</label>
                        <input type="text" id="creatorNameBook${loopStatus.index}" name="creatorName[]" maxlength="100" value="${cre.value.creatorName}">
                        <label for="creatorRoleBook${loopStatus.index}">Creator Role:</label>
                        <select class="cre-role-select" id="creatorRoleBook${loopStatus.index}" name="creatorRole[]">
                            <c:choose>
                                <c:when test="${requestScope.type eq 'book'}">
                                    <option value="author" ${cre.key eq 'author' ? 'selected' : ''}>Author</option>
                                </c:when>
                                <c:when test="${requestScope.type eq 'merch'}">
                                    <option value="sculptor" ${cre.key eq 'sculptor' ? 'selected' : ''}>Sculptor</option>
                                </c:when>
                            </c:choose>
                            <option value="artist" ${cre.key eq 'artist' ? 'selected' : ''}>Artist</option>
                        </select>
                        <button type="button" class="remove-btn" onclick="removeCreator(this)">Remove</button>
                    </div>
                </c:forEach>
            </div>
            <button type="button" class="add-btn" onclick="addCreator('mergedForm')">Add Creator</button>
        </div>

        <!--Type specific-->
        <c:choose>
            <c:when test="${requestScope.type eq 'book'}">


                <!--Genres-->
                <div class="form-group">
                    <label>Genre:</label>

                    <a class="p-2 mb-4 md:mb-0 bg-blue-500 hover:bg-blue-700 rounded-lg text-white" href="#" id="showAllGenres" onclick="toggleGenres(event)">Show All</a>

                    <div class="pair-group checkbox-group" id="genreContainer">
                        <c:forEach var="genEntry" items="${applicationScope.genres}" varStatus="loopStatus">
                            <c:choose>
                                <c:when test="${loopStatus.index < 3}">
                                    <div>
                                        <input type="checkbox" id="genre${loopStatus.index}" name="genre[]" value="${genEntry.key.genreID}"/>
                                        <label for="genre${loopStatus.index}">${genEntry.key.genreName}</label>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="hidden-genre">
                                        <input type="checkbox" id="genre${loopStatus.index}" name="genre[]" value="${genEntry.key.genreID}"/>
                                        <label for="genre${loopStatus.index}">${genEntry.key.genreName}</label>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </div>
                </div>

                <!--Publisher-->
                <div class="form-group">
                    <label for="publisherNameBook">Publisher:</label>
                    <input type="text" id="publisherNameBook" name="publisherName" maxlength="50">
                </div>

                <!--Duration-->
                <div class="form-group">
                    <label for="durationBook">Duration:</label>
                    <input type="text" id="durationBook" name="duration" maxlength="40">
                </div>

            </c:when>
            <c:when test="${requestScope.type eq 'merch'}">
                <!--Scale - Mat-->
                <div class="form-group">
                    <label>Specs:</label>
                    <div class="pair-group">
                        <label for="scaleLevelMerch">Scale Level:</label>
                        <input type="text" id="scaleLevelMerch" name="scaleLevel" maxlength="10">
                        <label for="materialMerch">Material:</label>
                        <input type="text" id="materialMerch" name="material" maxlength="60">
                    </div>
                </div>

                <!--Size-->
                <div class="form-group">
                    <label for="sizeMerch">Size:</label>
                    <input type="text" id="sizeMerch" name="size" maxlength="60">
                </div>

                <!--Series-->
                <div class="form-group">
                    <label for="seriesNameMerch">Series:</label>
                    <input type="text" id="seriesNameMerch" name="seriesName">
                </div>

                <!--Character-->
                <div class="form-group">
                    <label for="characterNameMerch">Character:</label>
                    <input type="text" id="characterNameMerch" name="characterName">
                </div>

                <!--Brand-->
                <div class="form-group">
                    <label for="brandNameMerch">Brand:</label>
                    <input type="text" id="brandNameMerch" name="brandName">
                </div>
            </c:when>
        </c:choose>

        <!--Submit-->
        <button class="add-product-btn" name="action" type="submit" value="${requestScope.type eq 'book' ? 'updateBook'
                                                                             :requestScope.type eq 'merch' ? 'updateMerch' : ''}">
            Save
        </button>
    </form>
</div>
<!--End Form 1-->

<!--//-->
<!--//-->
<!--//-->

<!-- Form 2: Merch 
<div id="merchForm" class="form-container">
    <form action="updateProduct" method="post">
        
         ID
        <div class="form-group">
            <label for="productIDBook">Product ID:</label>
            <input type="text" id="productIDBook" name="productID" maxlength="255" readonly required>
        </div>

        Name
        <div class="form-group">
            <label for="productNameMerch">Product Name:</label>
            <input type="text" id="productNameMerch" name="productName" maxlength="255" pattern=".*\S.*" required>
        </div>

        Price - stock
        <div class="form-group">
            <label>Details:</label>
            <div class="pair-group">
                <label for="priceMerch">Price(VND):</label>
                <input type="number" id="priceMerch" name="price" step="500" min="1000" max="10000000" required>
                <label for="stockCountMerch">Stock Count:</label>
                <input type="number" id="stockCountMerch" name="stockCount" min="0" max="1000" required>
            </div>
        </div>

        Category -  Special Filter
        <div class="form-group">
            <label>Filters:</label>
            <div class="pair-group">
                <label for="categoryMerch">Category:</label>
                <select id="categoryMerch" name="category" required>
                </select>
                <label for="specialFilterMerch">Special Filter:</label>
                <select id="specialFilterMerch" name="specialFilter" required>
                    <option value="unset">Unset</option>
                    <option value="pre-order">Pre-order</option>
                    <option value="new">New</option>
                </select>
            </div>
        </div>

        Description
        <div class="form-group">
            <label for="descriptionMerch">Description:</label>
            <textarea id="descriptionMerch" name="description"></textarea>
        </div>

        Release date - status
        <div class="form-group">
            <label>Availability:</label>
            <div class="pair-group">
                <label for="releaseDateMerch">Release Date:</label>
                <input type="date" id="releaseDateMerch" name="releaseDate" required>
                <label for="isActiveMerch">Status:</label>
                <select id="isActiveMerch" name="isActive" required>
                    <option value="true" selected>Active</option>
                    <option value="false">Inactive</option>
                </select>
            </div>
        </div>

        Keywords
        <div class="form-group">
            <label for="keywordsMerch">Keywords:</label>
            <textarea id="keywordsMerch" name="keywords"></textarea>
        </div>

        Image URL
        <div class="form-group">
            <label for="imageURLMerch">Image URL:</label>
            <textarea id="imageURLMerch" name="imageURL" maxlength="512"></textarea>
        </div>

        Creators
        <div class="creator-wrapper">

            <div class="creator-section">
                <div class="creator-group">
                    <label for="creatorNameMerch1">Creator Name:</label>
                    <input type="text" id="creatorNameMerch1" name="creatorName[]" maxlength="100">
                    <label for="creatorRoleMerch1">Creator Role:</label>
                    <select class="cre-role-select" id="creatorRoleMerch1" name="creatorRole[]">
                        <option value="sculptor">Sculptor</option>
                        <option value="artist">Artist</option>
                    </select>
                    <button type="button" class="remove-btn" onclick="removeCreator(this)">Remove</button>
                </div>
            </div>
            <button type="button" class="add-btn" onclick="addCreator('merchForm')">Add Creator</button>
        </div>

        Scale - Mat
        <div class="form-group">
            <label>Specs:</label>
            <div class="pair-group">
                <label for="scaleLevelMerch">Scale Level:</label>
                <input type="text" id="scaleLevelMerch" name="scaleLevel" maxlength="10">
                <label for="materialMerch">Material:</label>
                <input type="text" id="materialMerch" name="material" maxlength="60">
            </div>
        </div>

        Size
        <div class="form-group">
            <label for="sizeMerch">Size:</label>
            <input type="text" id="sizeMerch" name="size" maxlength="60">
        </div>

        Series
        <div class="form-group">
            <label for="seriesNameMerch">Series:</label>
            <input type="text" id="seriesNameMerch" name="seriesName">
        </div>

        Character
        <div class="form-group">
            <label for="characterNameMerch">Character:</label>
            <input type="text" id="characterNameMerch" name="characterName">
        </div>

        Brand
        <div class="form-group">
            <label for="brandNameMerch">Brand:</label>
            <input type="text" id="brandNameMerch" name="brandName">
        </div>

        Submit
        <button class="add-product-btn" name="action" type="submit" value="updateMerch">Save</button>
    </form>
</div>-->