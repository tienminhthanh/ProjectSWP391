<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<div id="title-group" class=" bg-orange-400 rounded-t-lg p-4">
    <label for="generalCategory">
        <span class="text-2xl font-bold text-white">Add New</span>
    </label>
    <select class="type-selector rounded-md p-4 bg-orange-600 text-white" id="generalCategory" name="generalCategory" onchange="toggleForm()">
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
                <input type="number" id="stockCountBook" name="stockCount" min="0"  max="1000" value="0" required readonly>
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
                        <option value="upcoming">Upcoming</option>
                        <option value="new">New</option>
                    </select>
                </span>
            </div>
        </div>

        <!--Description-->
        <div class="form-group">
            <label for="descriptionBook">Description:</label>
            <textarea id="descriptionBook" name="description" required></textarea>
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
            <textarea id="keywordsBook" name="keywords" required></textarea>
        </div>

        <!--Image URL-->
        <div class="form-group">
            <label for="imageURLBook">Image URL:</label>
            <textarea id="imageURLBook" name="imageURL" maxlength="512" required></textarea>
        </div>

        <!-- ====================  CREATORS (checkboxes with Show All)  ==================== -->
        <div class="form-group">
            <label>Creators:</label>
            <a class="p-2 mb-4 md:mb-0 bg-blue-500 hover:bg-blue-700 rounded-lg text-white" 
               href="#" id="showAllCreators" onclick="toggleCreators(event)">Show All</a>
            <div class="pair-group checkbox-group" id="creatorContainer">
                <c:forEach var="creatorEntry" items="${applicationScope.creators}" varStatus="loop">
                    <c:set var="creator" value="${creatorEntry.key}" />
                    <c:set var="creatorRole" value="${fn:toLowerCase(creator.creatorRole)}" />
                    <c:set var="creatorClass">
                        hidden-creator
                        <c:if test="${creatorRole eq 'sculptor'}"> hidden-book</c:if>
                        <c:if test="${creatorRole eq 'author'}"> hidden-merch</c:if>
                    </c:set>

                    <div class="${creatorClass}">
                        <input type="checkbox"
                               id="creator${loop.index}"
                               name="creator"
                               value="${creator.creatorID}"
                               class="creator-checkbox" />
                        <label for="creator${loop.index}">
                            ${creator.creatorName} <em>(${creator.creatorRole})</em>
                        </label>
                    </div>
                </c:forEach>
            </div>
        </div>

        <!--Genres-->
        <div class="form-group book-gr">
            <label>Genre:</label>

            <a class="p-2 mb-4 md:mb-0 bg-blue-500 hover:bg-blue-700 rounded-lg text-white" href="#" id="showAllGenres" onclick="toggleGenres(event)">Show All</a>

            <div class="pair-group checkbox-group" id="genreContainer">
                <c:forEach var="genEntry" items="${applicationScope.genres}" varStatus="loopStatus">
                    <div class="hidden-genre">
                        <input type="checkbox" id="genre${loopStatus.index}" name="genre" value="${genEntry.key.genreID}"/>
                        <label for="genre${loopStatus.index}">${genEntry.key.genreName}</label>
                    </div>

                </c:forEach>
            </div>
        </div>

        <!--Publisher-->
        <div class="form-group book-gr">
            <label for="publisherIDBook">Publisher:</label>
            <select id="publisherIDBook" name="publisher" required>
                <option value="">-- Select Publisher --</option>
                <c:forEach var="pubEntry" items="${applicationScope.publishers}">
                    <option value="${pubEntry.key.publisherID}">${pubEntry.key.publisherName}</option>
                </c:forEach>
            </select>
        </div>



        <!--Duration-->
        <div class="form-group book-gr">
            <label for="durationBook">Duration:</label>
            <input type="text" id="durationBook" name="duration" maxlength="40" required>
        </div>

        <!--Scale - Mat-->
        <div class="form-group merch-gr">
            <label>Specs:</label>
            <div class="pair-group">
                <label for="scaleLevelMerch">Scale Level:</label>
                <input type="text" id="scaleLevelMerch" name="scaleLevel" maxlength="10" required>
                <label for="materialMerch">Material:</label>
                <input type="text" id="materialMerch" name="material" maxlength="60" required>
            </div>
        </div>

        <!--Size-->
        <div class="form-group merch-gr">
            <label for="sizeMerch">Size:</label>
            <input type="text" id="sizeMerch" name="size" maxlength="60" required>
        </div>

        <!--Series-->
        <div class="form-group merch-gr">
            <label for="seriesIDMerch">Series:</label>
            <select id="seriesIDMerch" name="series" required>
                <option value="">-- Select Series --</option>
                <c:forEach var="serEntry" items="${applicationScope.series}">
                    <option value="${serEntry.key.seriesID}">${serEntry.key.seriesName}</option>
                </c:forEach>
            </select>
        </div>
        <!--        <div class="form-group merch-gr">
                    <label for="seriesNameMerch">Series:</label>
                    <input type="text" id="seriesNameMerch" name="seriesName" required>
                </div>-->

        <!--Character-->
        <div class="form-group merch-gr">
            <label for="characterIDMerch">Character:</label>
            <select id="characterIDMerch" name="character" required>
                <option value="">-- Select Character --</option>
                <c:forEach var="charEntry" items="${applicationScope.characters}">
                    <option value="${charEntry.key.characterID}">${charEntry.key.characterName}</option>
                </c:forEach>
            </select>
        </div>
        <!--        <div class="form-group merch-gr">
                    <label for="characterNameMerch">Character:</label>
                    <input type="text" id="characterNameMerch" name="characterName" required>
                </div>-->

        <!--Brand-->
        <div class="form-group merch-gr">
            <label for="brandIDMerch">Brand:</label>
            <select id="brandIDMerch" name="brand" required>
                <option value="">-- Select Brand --</option>
                <c:forEach var="brandEntry" items="${applicationScope.brands}">
                    <option value="${brandEntry.key.brandID}">${brandEntry.key.brandName}</option>
                </c:forEach>
            </select>
        </div>
        <!--        <div class="form-group merch-gr">
                    <label for="brandNameMerch">Brand:</label>
                    <input type="text" id="brandNameMerch" name="brandName" required>
                </div>-->

        <!--Submit-->
        <button class="add-product-btn" name="action" type="submit" value="addBook">Save</button>
    </form>
</div>


