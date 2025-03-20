<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="title-group" class=" bg-orange-400 rounded-t-lg p-4">
    <label for="generalCategory">
        <span class="text-2xl font-bold text-white">Add New</span>
    </label>
    <select class=" rounded-md p-4 bg-orange-600 text-white" id="generalCategory" name="generalCategory" onchange="toggleForm()">
        <option value="book">Books</option>
        <option value="merch">Merch</option>
    </select>
</div>


<div id="mergedForm" class="form-container active">
    <form action="addProduct" method="post">

        <!--Name-->
        <div class="form-group">
            <label for="productNameBook">Product Name:</label>
            <input type="text" id="productNameBook" name="productName" maxlength="255" required>
        </div>

        <!--Price - Stock count-->
        <div class="form-group">
            <label>Details:</label>
            <div class="pair-group">
                <label for="priceBook">Price:</label>
                <input type="number" id="priceBook" name="price" step="0.5" min="1" max="10000" required>
                <label for="stockCountBook">Stock Count:</label>
                <input type="number" id="stockCountBook" name="stockCount" min="0"  max="1000" required>
            </div>
        </div>

        <!--Category - Special Filter-->
        <div class="form-group">
            <label>Filters:</label>
            <div class="pair-group">
                <span class="flex cat-select-gr">
                    <label for="categoryBook">Category:</label>
                    <select id="categoryBook" name="category" required>
                        <c:forEach var="catEntry" items="${applicationScope.categories}">
                            <c:if test="${catEntry.key.generalCategory eq 'book'}">
                                <option class="cat-opt-book" value="${catEntry.key.categoryID}">${catEntry.key.categoryName}</option>
                            </c:if>
                             <c:if test="${catEntry.key.generalCategory eq 'merch'}">
                                <option class="cat-opt-merch" value="${catEntry.key.categoryID}">${catEntry.key.categoryName}</option>
                            </c:if>
                                
                        </c:forEach>
                    </select>
                </span>
                <span class="flex">
                    <label for="specialFilterBook">Special Filter:</label>
                    <select id="specialFilterBook" name="specialFilter" required>
                        <option value="unset">Unset</option>
                        <option value="pre-order">Pre-Order</option>
                        <option value="new">New</option>
                    </select>
                </span>
            </div>
        </div>

        <!--Description-->
        <div class="form-group">
            <label for="descriptionBook">Description:</label>
            <textarea id="descriptionBook" name="description"></textarea>
        </div>

        <!--ReleaseDate - Status-->
        <div class="form-group">
            <label>Availability:</label>
            <div class="pair-group">
                <label for="releaseDateBook">Release Date:</label>
                <input type="date" id="releaseDateBook" name="releaseDate" required>
                <label for="isActiveBook">Status:</label>
                <select id="isActiveBook" name="isActive" required>
                    <option value="true" selected>Active</option>
                    <option value="false">Inactive</option>
                </select>
            </div>
        </div>

        <!--Keywords-->
        <div class="form-group">
            <label for="keywordsBook">Keywords:</label>
            <textarea id="keywordsBook" name="keywords"></textarea>
        </div>

        <!--Image URL-->
        <div class="form-group">
            <label for="imageURLBook">Image URL:</label>
            <textarea id="imageURLBook" name="imageURL" maxlength="512"></textarea>
        </div>

        <!--Creators-->
        <div class="creator-wrapper">
            <div class="creator-section">
                <div class="creator-group" id="cre-gr-0">
                    <label for="creatorNameBook0">Creator Name:</label>
                    <input type="text" id="creatorNameBook0" name="creatorName" maxlength="100" required>
                    <label for="creatorRoleBook0">Creator Role:</label>
                    <select id="creatorRoleBook0" name="creatorRole" required>
                        <option class="hidden" value="author">Author</option>
                        <option class="hidden" value="sculptor">Sculptor</option>
                        <option value="artist">Artist</option>
                    </select>
                    <button type="button" class="remove-btn" onclick="removeCreator(this)">Remove</button>
                </div>
            </div>
            <button type="button" class="add-btn" onclick="addCreator('mergedForm')">Add Creator</button>
        </div>

        <!--Genres-->
        <div class="form-group book-gr">
            <label>Genre:</label>

            <a class="p-2 mb-4 md:mb-0 bg-blue-500 hover:bg-blue-700 rounded-lg text-white" href="#" id="showAllGenres" onclick="toggleGenres(event)">Show All</a>

            <div class="pair-group checkbox-group" id="genreContainer">
                <c:forEach var="genEntry" items="${applicationScope.genres}" varStatus="loopStatus">
                    <c:choose>
                        <c:when test="${loopStatus.index < 3}">
                            <div>
                                <input type="checkbox" id="genre${loopStatus.index}" name="genre" value="${genEntry.key.genreID}"/>
                                <label for="genre${loopStatus.index}">${genEntry.key.genreName}</label>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="hidden-genre">
                                <input type="checkbox" id="genre${loopStatus.index}" name="genre" value="${genEntry.key.genreID}"/>
                                <label for="genre${loopStatus.index}">${genEntry.key.genreName}</label>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </div>
        </div>

        <!--Publisher-->
        <div class="form-group book-gr">
            <label for="publisherNameBook">Publisher:</label>
            <input type="text" id="publisherNameBook" name="publisherName" maxlength="50">
        </div>

        <!--Duration-->
        <div class="form-group book-gr">
            <label for="durationBook">Duration:</label>
            <input type="text" id="durationBook" name="duration" maxlength="40">
        </div>
        
         <!--Scale - Mat-->
        <div class="form-group merch-gr">
            <label>Specs:</label>
            <div class="pair-group">
                <label for="scaleLevelMerch">Scale Level:</label>
                <input type="text" id="scaleLevelMerch" name="scaleLevel" maxlength="10">
                <label for="materialMerch">Material:</label>
                <input type="text" id="materialMerch" name="material" maxlength="60">
            </div>
        </div>

        <!--Size-->
        <div class="form-group merch-gr">
            <label for="sizeMerch">Size:</label>
            <input type="text" id="sizeMerch" name="size" maxlength="60">
        </div>

        <!--Series-->
        <div class="form-group merch-gr">
            <label for="seriesNameMerch">Series:</label>
            <input type="text" id="seriesNameMerch" name="seriesName">
        </div>

        <!--Character-->
        <div class="form-group merch-gr">
            <label for="characterNameMerch">Character:</label>
            <input type="text" id="characterNameMerch" name="characterName">
        </div>

        <!--Brand-->
        <div class="form-group merch-gr">
            <label for="brandNameMerch">Brand:</label>
            <input type="text" id="brandNameMerch" name="brandName">
        </div>

        <!--Submit-->
        <button class="add-product-btn" name="action" type="submit" value="addBook">Save</button>
    </form>
</div>


