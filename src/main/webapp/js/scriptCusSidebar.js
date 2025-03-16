/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


function openMobileMenu() {
    const mobileMenu = document.getElementById('cus-sidebar');
    const overlay = document.getElementById('cus-sidebar-overlay');
    mobileMenu.style.display = 'block';
    overlay.style.display = 'block';
}

function closeMobileMenu() {
    const mobileMenu = document.getElementById('cus-sidebar');
    const overlay = document.getElementById('cus-sidebar-overlay');
    mobileMenu.style.display = 'none';
    overlay.style.display = 'none';
}



function updateHrefOnHover(anchorElement) {

    //    Paths to productCatalog, ensure the function only applies to productCatalog
    const paths = ["/search", "/category", "/genre", "/publisher", "/creator",
        "/series", "/brand", "/character", "/new", "/sale"];
    const url = new URL(window.location.href);
    const pathname = url.pathname.replace(/\/$/, ""); // Remove trailing slash if any
    if (!paths.includes(pathname)) {
        return;
    }
    
    const params = new URLSearchParams(url.search);
    try {
        // Get filter from data-filter attribute
        const filter = anchorElement.dataset.filter;
        if (!filter) {
            throw new Error("No filter found in data-filter attribute");
        }

        const filterParts = filter.split('-');
        if (filterParts.length !== 2) {
            throw new Error("Invalid filter format: " + filter);
        }

        const filterName = decodeURIComponent(filterParts[0]);
        const filterId = decodeURIComponent(filterParts[1]);
        
        //Ensure no duplicate of pathname and filter
        //Only apply to these filter
        if(filterName === 'ftPbl' && pathname === '/publisher'){
            return;
        }
        if(filterName === 'ftCtg' && pathname === '/category'){
            return;
        }
        if(filterName === 'ftSrs' && pathname === '/series'){
            return;
        }
        if(filterName === 'ftChr' && pathname === '/character'){
            return;
        }
        if(filterName === 'ftBrn' && pathname === '/brand'){
            return;
        }
        
        //Get the whole list of the filter group from url params
        let filterList = params.get(filterName) ? params.get(filterName).split(',') : [];
        
        //Remove if present
        if (filterList.includes(filterId)) {
            filterList.splice(filterList.indexOf(filterId), 1);
        } else if(filterList.length === 0 || !(filterName === 'ftPbl' || filterName === 'ftCtg' || filterName === 'ftSrs' || filterName === 'ftChr' || filterName ==='ftBrn')){
            //If the list is empty, add the current filter
            //In addition, if the current filter are not those above, add it regardless of list size (notice the small '!') 
            filterList.push(filterId);
        }

        if (filterList.length > 0) {
            params.set(filterName, filterList.join(','));
        } else {
            params.delete(filterName);
        }

        url.search = params.toString();
        anchorElement.href = url.toString();
    } catch (error) {
        console.error("Error in somefunc:", error.message);
    }
}

document.addEventListener("DOMContentLoaded", function () {
    
    //Toggle for popular searches section
    const labels = document.querySelectorAll(".pop-label");
        if (labels) {

            labels.forEach(label => {
                label.addEventListener("click", function () {
                    const links = this.nextElementSibling;
                    if (links) {
                        links.classList.toggle("hidden");
                    }
                });
            });
        }
    
    
    //    Toggle ShowAll
    function toggleShowAllLess(listSelector) {
        let hiddenItems = document.querySelectorAll(`#${listSelector} li:nth-child(n+4)`);
        let isExpanded = hiddenItems[0]?.classList.contains("hidden") === false;
        hiddenItems.forEach(item => item.classList.toggle("hidden"));
        event.currentTarget.textContent = isExpanded ? "Show All" : "Show Less";
    }
    document.getElementById("toggleBtnCat")?.addEventListener("click", () => toggleShowAllLess("catList"));
    document.getElementById("toggleBtnCre")?.addEventListener("click", () => toggleShowAllLess("creList"));
    document.getElementById("toggleBtnGen")?.addEventListener("click", () => toggleShowAllLess("genList"));
    document.getElementById("toggleBtnPub")?.addEventListener("click", () => toggleShowAllLess("pubList"));
    document.getElementById("toggleBtnSer")?.addEventListener("click", () => toggleShowAllLess("serList"));
    document.getElementById("toggleBtnBra")?.addEventListener("click", () => toggleShowAllLess("braList"));
    document.getElementById("toggleBtnCha")?.addEventListener("click", () => toggleShowAllLess("chaList"));
    

    //    Paths to productCatalog, ensure the function only applies to productCatalog
    const paths = ["/search", "/category", "/genre", "/publisher", "/creator",
        "/series", "/brand", "/character", "/new", "/sale"];
    const url = new URL(window.location.href);
    const pathname = url.pathname.replace(/\/$/, ""); // Remove trailing slash if any
    if (!paths.includes(pathname)) {
        return;
    }

    //Display the price range filter in product catalog only
    document.querySelector('.ft-price-range-area').classList.remove("hidden");
    const params = new URLSearchParams(url.search);
    
    //Display the prices on reload
    //Display the "X" icon to deselect the filter
    const minPrice = document.querySelector('#ftprc-min');
    const maxPrice = document.querySelector('#ftprc-max');
    if (params.has("ftPrc")) {
        const closeLink = document.querySelector('#remove-ftprc-link');
        const priceValues = params.getAll("ftPrc");
        const formContainer = document.querySelector(".form-ftprc-container");
        console.log("Deselect", priceValues);
        minPrice.value = priceValues[0] || "";
        maxPrice.value = priceValues[1] || "";
        closeLink.classList.remove("hidden");
        formContainer.classList.add("bg-gray-200", "hover:bg-gray-300");
        params.delete("ftPrc");
        url.search = params.toString();
        closeLink.href = url.toString();
    }
    

    // Loop through existing params and create hidden inputs
    //Append hidden inputs to price range form
    const hiddenInputsContainerSide = document.querySelector(".hidden-input-ftprice");
    params.forEach((value, key) => {
        if (key !== "ftPrc") {  // Exclude inputs already in the form
            const input = document.createElement("input");
            input.type = "hidden";
            input.name = key;
            input.value = value;
            hiddenInputsContainerSide.appendChild(input);
        }

    });
    
    //Ensure only valid price range is submitted
    const form = document.querySelector('#ftprc-form');
    form.addEventListener("submit", function (event) {
        event.preventDefault();
        const minPriceOnSubmit = document.querySelector('#ftprc-min');
        const maxPriceOnSubmit = document.querySelector('#ftprc-max');
        let min = parseFloat(minPriceOnSubmit.value) || 0;
        let max = parseFloat(maxPriceOnSubmit.value) || 9999999;
        if (min > max) {
            let tempVal = min;
            min = max;
            max = tempVal;
        }
        minPriceOnSubmit.value = min;
        maxPriceOnSubmit.value = max;
        form.submit();
    });
    
    
    //Move selected filter to top of the list
    function moveSelectedToTop(filterName, listSelector) {
        if (!params.has(filterName)) {
            return;
        }
        
        
        const list = document.querySelector(listSelector);
        if (!list) {
            console.warn("Filter Group not found!");
            return;
        }
        
        const selectedValues = params.get(filterName).split(",");
        const items = Array.from(list.querySelectorAll("li"));

        // Find selected item by compare its data-filter with filterName and filterValue
        selectedValues.forEach(value => {
            const selectedItem = items.find(item => 
                item.querySelector("a")?.getAttribute("data-filter") === `${filterName}-${value}`
            );
            
            //If found and not on top -> Move to top
            if (selectedItem && list.firstChild !== selectedItem) {
                list.insertBefore(selectedItem, list.firstChild);
                selectedItem.classList.remove("hidden"); // Ensure it's visible
            }
        });

        // Update visibility of remaining items
        const allItems = Array.from(list.querySelectorAll("li"));
        let visibleCount = 3;
        
        //For each group below, only 1 filter can be applied at a time
        if(filterName === 'ftPbl' || filterName === 'ftCtg' || filterName === 'ftSrs' || filterName === 'ftChr' || filterName ==='ftBrn'){
            visibleCount = 1;
            const button = list.closest('div')?.querySelector('button');
            if (button) {
                button.classList.add('hidden');
            }
        }
        
        allItems.slice(visibleCount).forEach((item, index) => {
            if (!selectedValues.some(val => 
                item.querySelector("a")?.getAttribute("data-filter") === `${filterName}-${val}`
            )) {
                item.classList.add("hidden");
            }
        });
    }
    moveSelectedToTop("ftGnr", "#genList");
    moveSelectedToTop("ftCrt", "#creList");
    moveSelectedToTop("ftCtg", "#catList");
    moveSelectedToTop("ftPbl", "#pubList");
    moveSelectedToTop("ftBrn", "#braList");
    moveSelectedToTop("ftSrs", "#serList");
    moveSelectedToTop("ftChr", "#chaList");
    
    
    
    //Update display of selected filter
    function updateFilterAppearance(filterName, listID) {
        
    if (!params.has(filterName)) {
        return;
        
    }
    
    const list = document.querySelector(`#${listID}`);
    if (!list) {
        console.warn("Filter Group not found!");
        return;
    }
    
    const values = params.get(filterName).split(",");

    values.forEach(value => {
        const anchor = list.querySelector(`li a[data-filter="${filterName}-${value}"]`);
        if (!anchor) {
            return;
        }

        let parent = anchor.parentElement;
        let closeIcon = anchor.querySelector(`span.hidden`);
        if (!parent || !closeIcon) {
            return;
        }

        parent.classList.add("bg-gray-300", "hover:bg-gray-400");
        closeIcon.classList.remove("hidden");
    });
}
    updateFilterAppearance("ftGnr", "genList");
    updateFilterAppearance("ftCrt", "creList");
    updateFilterAppearance("ftCtg", "catList");
    updateFilterAppearance("ftBrn", "braList");
    updateFilterAppearance("ftSrs", "serList");
    updateFilterAppearance("ftChr", "chaList");
    updateFilterAppearance("ftPbl", "pubList");
    
    
    
});

//    Move this to a separate .js file if we dont need side bar on large view on all screen
window.addEventListener('resize', () => {
    const clientWidth = document.documentElement.clientWidth;
    const sidebar = document.getElementById('cus-sidebar');
    if (clientWidth > 768) {
        sidebar.style.display = 'block';
    } else {
        sidebar.style.display = 'none';
    }
});