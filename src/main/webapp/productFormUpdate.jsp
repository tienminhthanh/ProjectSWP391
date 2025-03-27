<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="title-group" class=" bg-orange-400 rounded-t-lg p-4">
    <h2 class="text-2xl font-bold text-white">Update ${type eq 'book' ? 'Books' : type eq 'merch' ? 'Merchandise' : ''}</h2>
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
                <label for="priceBook">Price:</label>
                <input type="number" id="priceBook" name="price" step="0.5" min="1" max="10000" required value="${product.price/1000}">
                <label for="stockCountBook">Stock Count:</label>
                <input type="number" id="stockCountBook" name="stockCount" min="0"  max="1000" required value="${product.stockCount}" readonly>
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
                    <option value="unset" ${empty product.specialFilter or (product.specialFilter ne 'upcoming' and product.specialFilter ne 'new') ? 'selected' : ''}>Unset</option>
                    <option value="upcoming" ${product.specialFilter eq 'upcoming' ? 'selected' : ''}">Upcoming</option>
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
        <c:forEach var="cre" items="${creatorList}" varStatus="loopStatus">
            <input type="hidden" name="associatedCreatorID" value="${cre.creatorID}">
        </c:forEach>
        <div class="creator-wrapper">
            <div class="creator-section">
                <c:choose>
                    <c:when test="${empty creatorList}">
                        <div class="creator-group" id="cre-gr-0">
                            <label for="creatorNameBook0">Creator Name:</label>
                            <input type="text" id="creatorNameBook0" name="creatorName" maxlength="100" required>
                            <label for="creatorRoleBook0">Creator Role:</label>
                            <select id="creatorRoleBook0" name="creatorRole">
                                <option class="hidden" value="author">Author</option>
                                <option class="hidden" value="sculptor">Sculptor</option>
                                <option value="artist">Artist</option>
                            </select>
                            <button type="button" class="remove-btn" onclick="removeCreator(this)">Remove</button>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="cre" items="${creatorList}" varStatus="loopStatus">
                            <div class="creator-group" id="cre-gr-${loopStatus.index}">
                                <label for="creatorNameBook${loopStatus.index}">Creator Name:</label>
                                <input type="text" id="creatorNameBook${loopStatus.index}" name="creatorName" maxlength="100" required value="${cre.creatorName}">
                                <label for="creatorRoleBook${loopStatus.index}">Creator Role:</label>
                                <select class="cre-role-select" id="creatorRoleBook${loopStatus.index}" name="creatorRole" required>
                                    <option value="author" ${cre.creatorRole eq 'author' ? 'selected' : ''}>Author</option>
                                    <option value="sculptor" ${cre.creatorRole eq 'sculptor' ? 'selected' : ''}>Sculptor</option>
                                    <option value="artist" ${cre.creatorRole eq 'artist' ? 'selected' : ''}>Artist</option>
                                </select>
                                <button type="button" class="remove-btn" onclick="removeCreator(this)">Remove</button>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>

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
                            <c:set var="isChecked" value="${not empty genreList and genreList.contains(genEntry.key)}" />
                            <c:choose>
                                <c:when test="${isChecked}">
                                    <input type="hidden" name="associatedGenreID" value="${genEntry.key.genreID}">
                                    <div>
                                        <input type="checkbox" id="genre${loopStatus.index}" name="genre" 
                                               value="${genEntry.key.genreID}" ${isChecked ? 'checked' : ''} />
                                        <label for="genre${loopStatus.index}">${genEntry.key.genreName}</label>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="hidden-genre">
                                        <input type="checkbox" id="genre${loopStatus.index}" name="genre" 
                                               value="${genEntry.key.genreID}" ${isChecked ? 'checked' : ''} />
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
                    <input type="hidden" name="associatedPublisherID" value="${product.publisher.publisherID}">
                    <input type="text" id="publisherNameBook" name="publisherName" maxlength="50" value="${product.publisher.publisherName}">
                </div>

                <!--Duration-->
                <div class="form-group">
                    <label for="durationBook">Duration:</label>
                    <input type="text" id="durationBook" name="duration" maxlength="40" value="${product.duration}">
                </div>

            </c:when>
            <c:when test="${requestScope.type eq 'merch'}">
                <!--Scale - Mat-->
                <div class="form-group">
                    <label>Specs:</label>
                    <div class="pair-group">
                        <label for="scaleLevelMerch">Scale Level:</label>
                        <input type="text" id="scaleLevelMerch" name="scaleLevel" maxlength="10" value="${product.scaleLevel}">
                        <label for="materialMerch">Material:</label>
                        <input type="text" id="materialMerch" name="material" maxlength="60" value="${product.material}">
                    </div>
                </div>

                <!--Size-->
                <div class="form-group">
                    <label for="sizeMerch">Size:</label>
                    <input type="text" id="sizeMerch" name="size" maxlength="60" value="${product.size}">
                </div>

                <!--Series-->
                <div class="form-group">
                    <label for="seriesNameMerch">Series:</label>
                    <input type="hidden" name="associatedSeriesID" value="${product.series.seriesID}">
                    <input type="text" id="seriesNameMerch" name="seriesName"  value="${product.series.seriesName}">
                </div>

                <!--Character-->
                <div class="form-group">
                    <label for="characterNameMerch">Character:</label>
                    <input type="hidden" name="associatedCharacterID" value="${product.character.characterID}">
                    <input type="text" id="characterNameMerch" name="characterName" value="${product.character.characterName}">
                </div>

                <!--Brand-->
                <div class="form-group">
                    <label for="brandNameMerch">Brand:</label>
                    <input type="hidden" name="associatedBrandID" value="${product.brand.brandID}">
                    <input type="text" id="brandNameMerch" name="brandName" value="${product.brand.brandName}">
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
