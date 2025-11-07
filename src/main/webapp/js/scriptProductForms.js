
//Toggle based on product type - Only for Add New Products
function toggleForm() {
    const generalCategory = document.getElementById("generalCategory");
    if (!generalCategory) {
        console.log("toggleForm: generalCategory element not found");
        return;
    }
    
//    Categories
    const allCtgr = document.querySelectorAll(".cat-select-gr select option");
    const bookCtgr = document.querySelectorAll(".cat-select-gr select option.cat-opt-book");
    const merchCtgr = document.querySelectorAll(".cat-select-gr select option.cat-opt-merch");
    if (allCtgr) {
        bookCtgr.forEach(opt=>{
            opt.classList?.toggle("hidden",generalCategory.value !== "book");
        });
        merchCtgr.forEach(opt=>{
            opt.classList?.toggle("hidden", generalCategory.value !== "merch");
        });
        allCtgr.forEach(opt=>{
            opt.selected = opt.classList?.contains('hidden') ? false : true;
        });
        
    }
    
    // === Filter Creators by Product Type ===
    const isBook = generalCategory.value === "book";
    const isMerch = generalCategory.value === "merch";

    // Hide all first
    document.querySelectorAll('.creator-checkbox').forEach(cb => {
        const div = cb.closest('div');
        if (div.classList.contains('hidden-book')) div.style.display = isBook ? 'none' : 'flex';
        if (div.classList.contains('hidden-merch')) div.style.display = isMerch ? 'none' : 'flex';
        if (!div.classList.contains('hidden-book') && !div.classList.contains('hidden-merch')) {
            div.style.display = 'flex';
        }
    });

    // Reset "Show All" state on toggle
    document.querySelectorAll('.hidden-creator').forEach(el => {
        el.style.display = 'none';
    });
    const showAllLink = document.getElementById('showAllCreators');
    if (showAllLink) showAllLink.textContent = 'Show All';
    
    
//    Type-specific fields
    const bookFields = document.querySelectorAll('.form-group.book-gr');
    if (bookFields) {
        bookFields.forEach(field=>{
            field.classList?.toggle('hidden',generalCategory.value !== "book");
            
            const bookInputs = field.querySelectorAll('input');
            if(bookInputs){
                bookInputs.forEach(inp=>{
                    inp.disabled = field.classList?.contains('hidden') ? true : false;
                });
            }
            
            const bookSelectors = field.querySelectorAll('select');
            if(bookSelectors){
                bookSelectors.forEach(inp=>{
                    inp.disabled = field.classList?.contains('hidden') ? true : false;
                });
            }
        });
    }
    const merchFields = document.querySelectorAll('.form-group.merch-gr');
    if (merchFields) {
        merchFields.forEach(field=>{
            field.classList?.toggle('hidden',generalCategory.value !== "merch");
            
            const merchInputs = field.querySelectorAll('input');
            if(merchInputs){
                merchInputs.forEach(inp=>{
                    inp.disabled = field.classList?.contains('hidden') ? true : false;
                });
            }
            
            
            const merchSelectors = field.querySelectorAll('select');
            if(merchSelectors){
                merchSelectors.forEach(inp=>{
                    inp.disabled = field.classList?.contains('hidden') ? true : false;
                });
            }
            
        });
            
    }
    
// Submit button
    const submitBtn = document.querySelector('.add-product-btn');
    if (submitBtn) {
        submitBtn.value = generalCategory.value === "merch" ? "addMerch" : generalCategory.value === "book" ? "addBook" : "";

    }
    
    
}

//Display price on input
document.addEventListener('DOMContentLoaded',function(){
    // Get the price input element and the label element
    const priceInput = document.querySelector('input[name="price"]');
    const priceLabel = document.querySelector('label[for="priceBook"]');
    
    if(!priceInput || !priceLabel){
        return;
    }

    // Function to format number with commas and multiply by 1000
    function formatPrice(value) {
        // Convert to number, multiply by 1000
        const multipliedValue = parseFloat(value) * 1000;
        if (isNaN(multipliedValue)) return '0 đ'; // Handle invalid input
        // Format with commas and add ' đ'
        return multipliedValue.toLocaleString('en-US') + ' đ';
    }

    // Update the label text on input
    priceInput.oninput = function() {
        // Get the current input value
        const value = this.value;
        // Update the label, preserving "Price(" and ")" around priceInput
        priceLabel.textContent = `Price (${formatPrice(value)}):`;
    };

    // Trigger initial update if there's a default value
    priceInput.dispatchEvent(new Event('input'));
});



// Toggle Show All Creators (respects book/merch filtering)
function toggleCreators(event,type = '') {
    if (!event) return;
    event.preventDefault();

    const hiddenCreators   = document.querySelectorAll('.hidden-creator');
    const showAllLink      = document.getElementById('showAllCreators');
    const generalCategory  = document.getElementById('generalCategory');
    if (!hiddenCreators.length || !showAllLink) return;
    
    const productType = generalCategory ? generalCategory.value : type;
    
    const isBook  = productType === 'book';
    const isMerch = productType === 'merch';
    
    const hiddenCreatorsMerch = document.querySelectorAll('.hidden-creator.hidden-book');
    const hiddenCreatorsBook = document.querySelectorAll('.hidden-creator.hidden-merch');

    // Determine current visibility of the "extra" creators
    const firstHidden = isBook 
    ? hiddenCreatorsBook[0]
    : isMerch
    ? hiddenCreatorsMerch[0]
    : hiddenCreators[0];
    
    const areHidden = firstHidden.style.display === 'none' || firstHidden.style.display === '';

    hiddenCreators.forEach(div => {
        // 1. Keep type-filtered items hidden
        const isBookItem   = div.classList.contains('hidden-merch');
        const isMerchItem  = div.classList.contains('hidden-book');

        if ((isBook && isMerchItem) || (isMerch && isBookItem)) {
            // This creator does NOT belong to the current product type → stay hidden
            div.style.display = 'none';
            return;
        }

        // 2. For creators that *do* belong to the current type, toggle visibility
        div.style.display = areHidden ? 'flex' : 'none';
    });

    // Update button text
    showAllLink.textContent = areHidden ? 'Hide' : 'Show All';
}

//Toggle Show All Genres
function toggleGenres(event) {
    if (!event) {
        console.log("toggleGenres: event not found");
        return;
    }
    event.preventDefault(); // Prevent link from jumping
    
    const hiddenGenres = document.querySelectorAll('.hidden-genre');
    const showAllLink = document.getElementById('showAllGenres');
    
    if (!hiddenGenres.length || !showAllLink) {
        console.log("toggleGenres: hiddenGenres or showAllGenres element not found");
        return;
    }
    
    const areHidden = hiddenGenres[0].style.display === 'none' || hiddenGenres[0].style.display === '';
    hiddenGenres.forEach(genre => {
        if (genre) genre.style.display = areHidden ? 'flex' : 'none';
    });

    showAllLink.textContent = areHidden ? 'Hide' : 'Show All';
}


//Validation on input
document.addEventListener("input", function (event) {
    if (!event || !event.target) {
        console.log("input event: target not found");
        return;
    }
    
    let target = event.target;
    
    // Stock count validation (Whole numbers only)
    if (target.matches("[name='stockCount'], [name='quantity']")) {
        if (target.value.includes(".")) {
            target.setCustomValidity("Stock count must be a whole number.");
        } else {
            target.setCustomValidity("");
        }
    }
    
    // Apply to all text inputs & textareas (no leading/trailing spaces)
    if (target.matches("input[type='text'], textarea")) {
        target.value = target.value.replace(/^\s+/g, ""); // Trim leading spaces
        
        // Prevent empty input
        if (target.value.trim() === "") {
            target.setCustomValidity("This field cannot be empty.");
        } else {
            target.setCustomValidity("");
        }
    }
});


// Append a new creator to the form
function addCreator(formId) {
    const form = document.getElementById(formId);
    if (!form) {
        console.log(`addCreator: form with id ${formId} not found`);
        return;
    }

    const creatorSection = form.querySelector(".creator-section");
    if (!creatorSection || creatorSection.children.length === 0) {
        console.log("addCreator: creator-section element not found or empty");
        return;
    }
    
    const roles = [...creatorSection.children[0].querySelectorAll('select option')];

    if (roles.length === 0) {  // Check if options exist
        console.log('addCreator: role options not found');
        return;
    }

    const lastCrtGroup = creatorSection.lastElementChild;

    let newIndex = 0; // Default index if no elements exist

    if (lastCrtGroup) {
        const lastId = lastCrtGroup.id.match(/\d+$/); // Extract last digits from the ID
        newIndex = lastId ? parseInt(lastId[0], 10) + 1 : 0; // Increment index
    }

    creatorSection.appendChild(createCreatorElement(newIndex,roles, formId === 'bookForm' ? 'Book' : formId === 'merchForm' ? 'Merch' : ''));
}

function createCreatorElement(index, roles = [], type = "", creatorName = "", creatorRole = "") {
    const fragment = document.createDocumentFragment(); // Improves performance

    const creatorGroup = document.createElement("div");
    creatorGroup.className = "creator-group";
    creatorGroup.id = `cre-gr-${index}`;

    // Name Label
    const nameLabel = document.createElement("label");
    nameLabel.htmlFor = `creatorName${type}${index}`;
    nameLabel.textContent = "Creator Name:";

    // Name Input
    const nameInput = document.createElement("input");
    nameInput.type = "text";
    nameInput.id = `creatorName${type}${index}`;
    nameInput.name = "creatorName";
    nameInput.maxLength = 100;
    nameInput.value = creatorName;

    // Role Label
    const roleLabel = document.createElement("label");
    roleLabel.htmlFor = `creatorRole${type}${index}`;
    roleLabel.textContent = "Creator Role:";

    // Role Select
    const roleSelect = document.createElement("select");
    roleSelect.id = `creatorRole${type}${index}`;
    roleSelect.name = "creatorRole";

    // Options
    roles.forEach(role => {
        const option = document.createElement("option");
        option.value = role.value;
        option.textContent = role.value.charAt(0).toUpperCase() + role.value.slice(1);
        option.className = role.className;
        option.selected = option.classList?.contains('hidden') ? false : true;
        roleSelect.appendChild(option);
    });

    // Remove Button
    const removeBtn = document.createElement("button");
    removeBtn.type = "button";
    removeBtn.className = "remove-btn";
    removeBtn.textContent = "Remove";
    removeBtn.onclick = () => removeCreator(removeBtn);

    // Append to Creator Group
    creatorGroup.append(nameLabel, nameInput, roleLabel, roleSelect, removeBtn);
    
    fragment.appendChild(creatorGroup);
    return fragment;
}



//Remove a creator if needed
function removeCreator(button) {
    if (!button) {
        console.log("removeCreator: button not found");
        return;
    }
    
    const creatorGroup = button.parentElement;
    if (!creatorGroup) {
        console.log("removeCreator: creator-group element not found");
        return;
    }
    
    const creatorSection = creatorGroup.parentElement;
    if (!creatorSection) {
        console.log("removeCreator: creator-section element not found");
        return;
    }
    
    
    if (creatorSection.querySelectorAll('.creator-group').length > 1 ) {
        creatorSection.removeChild(creatorGroup);
    } else {
        Swal.fire({
            icon: 'error',
            text: 'Cannot remove the last creator group!'
        });
    }
}
