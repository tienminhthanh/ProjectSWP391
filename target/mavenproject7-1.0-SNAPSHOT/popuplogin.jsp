<style>
    /*Popup Styling*/
    .popup {
        display: none;
        position: fixed;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        background: white;
        padding: 20px;
        box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
        z-index: 1100;
        border-radius: 8px;
        text-align: center;
    }

    .overlay {
        display: none;
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(0, 0, 0, 0.5);
        z-index: 1099;
    }

    .popup button {
        margin-top: 10px;
        padding: 5px 10px;
        color: white;
        border: none;
        cursor: pointer;
    }
    .popup button.proceed{
        background: orange;
    }
    .popup button.cancel{
        background: grey;
    }
</style>
<!-- Pop-Up Box -->
<div class="overlay" id="login-popup-overlay" onclick="closeLoginPopup()"></div>
<div class="popup" id="login-popup">
    <p>Please login with a <strong>'Customer'</strong> account first!</p>
    <a href="login">
        <button onclick="closeLoginPopup()" class="proceed">Login</button>
    </a>
    <button onclick="closeLoginPopup()" class="cancel">Cancel</button>
</div>

<script>
    function openLoginPopup() {
        document.getElementById("login-popup").style.display = "block";
        document.getElementById("login-popup-overlay").style.display = "block";
    }

    function closeLoginPopup() {
        document.getElementById("login-popup").style.display = "none";
        document.getElementById("login-popup-overlay").style.display = "none";
    }
</script>
