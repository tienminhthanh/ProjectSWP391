<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Footer</title>
        <script src="https://kit.fontawesome.com/bfab6e6450.js" crossorigin="anonymous"></script>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous" />
        <style>
            * {
                font-family: sans-serif, Arial, Helvetica;
                box-sizing: border-box;
            }

            a {
                text-decoration: none;
            }

            footer {
                background-color: white; /* Changed to white */
                color: rgba(0, 0, 0, 0.8); /* Dark text for better readability */
                margin-bottom: 5vh;
            }

            .footer-detail {
                padding-top: 3%;
            }

            footer a:hover {
                color: #92c4ec;
            }

            footer a>p:hover {
                color: #92c4ec;
            }



            .contact-content {
                display: flex;
                color: black;
            }

            .contact-text {
                padding-left: 3%;
                color: rgba(0, 0, 0, 0.8);
                font-size: small;
            }

            .policy-service-title {
                font-size: small;
            }

            .policy-service-text {
                font-size: xx-small;
                color: rgba(0, 0, 0, 0.8);
                line-height: 10vh;
                display: block;
            }

            .footer-logo img {
                max-width: 50%; /* Set the max width to 50% for the logo */
                height: auto;   /* Ensure the height adjusts proportionally */
            }




            @media only screen and (min-width: 768px) {
                .policy-service-text {
                    line-height: 5vh;
                    font-size: smaller;
                }
            }

            .social-media {
                margin-bottom: 3vh;
            }

            .social-media-icon {
                display: flex;
                gap: 5%;
            }

            .icon-container {
                border: 1px solid white;
                border-radius: 3px;
                background-color: white;
                width: 10%;
            }

            .icon-container:hover {
                background-color: #92c4ec;
            }

            .social-media-icon .icon-container a {
                display: block;
                width: 100%;
                text-align: center;
            }

            .icon-container a i {
                color: #313131;
            }

            .footer-logo img {
                width: 100%; /* Ensure the logo fits the container */
                height: auto; /* Maintain aspect ratio */
            }
        </style>
    </head>

    <body>
        <header>
            <!-- place navbar here -->
        </header>
        <main></main>
        <footer>
            <div class="container">
                <div class="row footer-detail">
                    <div class="contact col-12 col-lg-4">
                        <ul class="nav flex-column">
                            <li class="nav-item footer-logo">
                                <h1>
                                    <a href="home" class="link logo-content">
                                        <img src="img/logo.png" alt="WIBOOKS"/>

                                    </a>
                                </h1>
                            </li>
                            <li class="nav-item address">
                                <h4>
                                    <a href="https://maps.app.goo.gl/8y7potYBMYpJ6E9Q8" class="link contact-content">
                                        <i class="fa-solid fa-location-dot"></i>
                                        <p class="contact-text">
                                            600 Nguyễn Văn Cừ Nối Dài, An Bình, Bình Thủy, Cần Thơ
                                        </p>
                                    </a>
                                </h4>
                            </li>
                            <li class="nav-item email">
                                <h4>
                                    <a href="mailto:anhk.ce191266@gmail.com" class="link contact-content">
                                        <i class="fa-regular fa-envelope"></i>
                                        <p class="contact-text">
                                            Email: anhk.ce191266@gmail.com
                                        </p>
                                    </a>
                                </h4>
                            </li>
                            <li class="nav-item phone-number">
                                <h4>
                                    <a href="tel:0384417960" class="link contact-content">
                                        <i class="fa-solid fa-phone"></i>
                                        <p class="contact-text">
                                            Hotline: 0384417960
                                        </p>
                                    </a>
                                </h4>
                            </li>
                        </ul>
                    </div>

                    <div class="customer-service col-6 col-lg-2">
                        <p class="policy-service-title">
                            HỖ TRỢ KHÁCH HÀNG
                        </p>
                        <div class="policy-service-content">
                            <a href="Cauhoithuonggap.jsp" class="link policy-service-text">
                                Câu hỏi thường gặp
                            </a>
                            <a href="QcHoatDong.jsp" class="link policy-service-text">
                                Điều khoản dịch vụ
                            </a>
                        </div>
                    </div>
                    <div class="policy col-6 col-lg-2">
                        <p class="policy-service-title">
                            CHÍNH SÁCH
                        </p>
                        <div class="policy-service-content">
                            <a href="CSbaoMat.jsp" class="link policy-service-text">
                                Chính sách bảo mật
                            </a>
                            <a href="CSvanChuyen.jsp" class="link policy-service-text">
                                Chính sách vận chuyển
                            </a>
                            <a href="CStraHang.jsp" class="link policy-service-text">
                                Chính sách đổi trả
                            </a>
                        </div>
                    </div>
                    <div class="social-media col-12 col-lg-4">
                        <p class="social-media-title">
                            KẾT NỐI VỚI CHÚNG TÔI
                        </p>
                        <div class="social-media-icon">
                            <div class="icon-container">
                                <a href="https://www.facebook.com/7053kireii">
                                    <i class="fa-brands fa-facebook-f"></i>
                                </a>
                            </div>
                            <div class="icon-container">
                                <a href="https://www.youtube.com/@kuritokenshutsu3986">
                                    <i class="fa-brands fa-youtube"></i>
                                </a>
                            </div>
                            <div class="icon-container">
                                <a href="https://www.twitch.tv/anhkct123123">
                                    <i class="fa-brands fa-twitch"></i>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </footer>

        <!-- Bootstrap JavaScript Libraries -->
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js"
                integrity="sha384-I7E8VVD/ismYTF4hNIPjVp/Zjvgyol6VFvRkX/vR+Vc4jQkC+hVqc2pM8ODewa9r"
        crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.min.js"
                integrity="sha384-BBtl+eGJRgqQAUMxJ7pMwbEyER4l1g+O15P+16Ep7Q9Q+zqX6gSbd85u4mG4QzX+"
        crossorigin="anonymous"></script>
        <!--        <script async defer crossorigin="anonymous"
                        src="https://connect.facebook.net/en_GB/sdk.js#xfbml=1&version=v21.0"></script>
                <script src="js/script.js"></script>-->
    </body>
</html>
