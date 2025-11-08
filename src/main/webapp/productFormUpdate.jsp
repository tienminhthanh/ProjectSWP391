<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

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
            <textarea id="descriptionBook" name="description" required>${product.description}</textarea>
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
            <textarea id="keywordsBook" name="keywords" required>${product.keywords}</textarea>
        </div>

        <!--Image URL-->
        <div class="form-group">
            <label for="imageURLBook">Image URL:</label>
            <textarea id="imageURLBook" name="imageURL" maxlength="512" required>${product.imageURL}</textarea>
        </div>

        <!--Creators-->
        <c:forEach var="cre" items="${product.creatorList}" varStatus="loopStatus">
            <input type="hidden" name="associatedCreatorID" value="${cre.creatorID}">
        </c:forEach>

        <div class="form-group">
            <label>Creators:</label>
            <a class="p-2 mb-4 md:mb-0 bg-blue-500 hover:bg-blue-700 rounded-lg text-white" 
               href="#" id="showAllCreators" onclick="toggleCreators(event, `${requestScope.type}`)">Show All</a>
            <div class="pair-group checkbox-group" id="creatorContainer">
                <c:forEach var="creatorEntry" items="${applicationScope.creators}" varStatus="loop">
                    <c:set var="creator" value="${creatorEntry.key}" />
                    <c:set var="isCheckedCrt" value="${not empty product.creatorList and product.creatorList.contains(creator)}" />
                    <c:set var="creatorRole" value="${fn:toLowerCase(creator.creatorRole)}" />
                    <c:set var="creatorClass">
                        <c:if test="${not isCheckedCrt}"> hidden-creator</c:if>
                        <c:if test="${creatorRole eq 'sculptor'}"> hidden-book</c:if>
                        <c:if test="${creatorRole eq 'author'}"> hidden-merch</c:if>
                    </c:set>


                    <div class="${creatorClass}">
                        <input type="checkbox"
                               id="creator${loop.index}"
                               name="creator"
                               value="${creator.creatorID}"
                               class="creator-checkbox" 
                               ${isCheckedCrt ? 'checked' : ''} />
                        <label for="creator${loop.index}">
                            ${creator.creatorName} <em>(${creator.creatorRole})</em>
                        </label>
                    </div>
                </c:forEach>
            </div>
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
                            <c:set var="isChecked" value="${not empty product.genreList and product.genreList.contains(genEntry.key)}" />
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
                    <label for="publisherIDBook">Publisher:</label>
                    <select id="publisherIDBook" name="publisher" required>
                        <option value="">-- Select Publisher --</option>
                        <c:forEach var="pubEntry" items="${applicationScope.publishers}">
                            <option value="${pubEntry.key.publisherID}" ${product.publisher.publisherID eq pubEntry.key.publisherID ? 'selected' : ''}>${pubEntry.key.publisherName}</option>
                        </c:forEach>
                    </select>
                </div>

                <!--Duration-->
                <div class="form-group">
                    <label for="durationBook">Duration:</label>
                    <input type="text" id="durationBook" name="duration" maxlength="40" value="${product.duration}" required>
                </div>

            </c:when>
            <c:when test="${requestScope.type eq 'merch'}">
                <!--Scale - Mat-->
                <div class="form-group">
                    <label>Specs:</label>
                    <div class="pair-group">
                        <label for="scaleLevelMerch">Scale Level:</label>
                        <input type="text" id="scaleLevelMerch" name="scaleLevel" maxlength="10" value="${product.scaleLevel}" required>
                        <label for="materialMerch">Material:</label>
                        <input type="text" id="materialMerch" name="material" maxlength="60" value="${product.material}" required>
                    </div>
                </div>

                <!--Size-->
                <div class="form-group">
                    <label for="sizeMerch">Size:</label>
                    <input type="text" id="sizeMerch" name="size" maxlength="60" value="${product.size}" required>
                </div>


                <!--Series-->
                <div class="form-group">
                    <label for="seriesIDMerch">Series:</label>
                    <select id="seriesIDMerch" name="series" required>
                        <option value="">-- Select Series --</option>
                        <c:forEach var="serEntry" items="${applicationScope.series}">
                            <option value="${serEntry.key.seriesID}" ${product.series.seriesID eq serEntry.key.seriesID ? 'selected' : ''}>${serEntry.key.seriesName}</option>
                        </c:forEach>
                    </select>
                </div>


                <!--Character-->
                <div class="form-group">
                    <label for="characterIDMerch">Character:</label>
                    <select id="characterIDMerch" name="character" required>
                        <option value="">-- Select Character --</option>
                        <c:forEach var="charEntry" items="${applicationScope.characters}">
                            <option value="${charEntry.key.characterID}" ${product.character.characterID eq charEntry.key.characterID ? 'selected' : ''}>${charEntry.key.characterName}</option>
                        </c:forEach>
                    </select>
                </div>



                <!--Brand-->
                <div class="form-group">
                    <label for="brandIDMerch">Brand:</label>
                    <select id="brandIDMerch" name="brand" required>
                        <option value="">-- Select Brand --</option>
                        <c:forEach var="brandEntry" items="${applicationScope.brands}">
                            <option value="${brandEntry.key.brandID}" ${product.brand.brandID eq brandEntry.key.brandID ? 'selected' : ''}>${brandEntry.key.brandName}</option>
                        </c:forEach>
                    </select>
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
