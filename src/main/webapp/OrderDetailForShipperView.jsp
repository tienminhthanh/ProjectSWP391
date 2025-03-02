<%-- 
    Document   : OrderDetailForShipper
    Created on : 27 Feb 2025, 11:14:28
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Giao hàng</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    </head>

    <body class="bg-gray-100">
        <div class="bg-orange-600 text-white p-4 flex justify-between items-center">
            <div class="flex items-center space-x-4">
                <img src="./img/logo.png" alt="Logo Wibooks" class="h-10 w-25">
                <h1 class="text-xl font-bold">Giao hàng</h1>
                <nav class="space-x-4">
                    <a href="#" class="hover:underline">Đơn hàng</a>
                    <a href="#" class="hover:underline">Sản phẩm</a>
                    <a href="#" class="hover:underline">Báo cáo</a>
                    <a href="#" class="hover:underline">Cấu hình</a>
                </nav>
            </div>
            <div class="flex items-center space-x-4">
                <i class="fas fa-bell"></i>
                <a href="readAccount" class="fas fa-user-circle"></a>
                <span>Shipper</span>
                <a href="logout" class="fas fa-sign-out-alt"></a>
            </div>
        </div>
        <div class="p-4">
            <div class="bg-white p-4 rounded shadow">
                <div class="flex justify-between items-center border-b pb-2 mb-4">
                    <h2 class="text-xl font-bold">Thông tin giao hàng / <p> ${orderInfo.orderID}</p></h2>
                    <div class="space-x-2">
                        <button class="bg-orange-500 text-white px-4 py-2 rounded">CẬP NHẬT TRẠNG THÁI</button>
                        <button class="bg-gray-200 text-black px-4 py-2 rounded">HỦY ĐƠN</button>
                    </div>
                </div>
                <div class="flex justify-between items-center mb-4">
                    <div class="space-y-2">
                        <h3 class="text-lg font-bold">Thông tin giao hàng</h3>
                        <p class="text-xl font-bold"> ${orderInfo.orderID}</p>
                      
                        <p>Địa chỉ giao hàng: <span class="font-bold"> ${orderInfo.deliveryAddress}</span></p>
                        <p>Người nhận: <span class="font-bold">Nguyễn Văn A</span></p>
                    </div>
                    <div class="space-y-2 text-right">
                        <p>Hạn chót giao: <span class="font-bold">17/02/2022 07:00:00</span></p>
                        <p>Trạng thái: <span class="font-bold text-green-500">Đang giao</span></p>
                        <p>Ghi chú: <span class="font-bold">Giao hàng trước 5 giờ chiều</span></p>
                    </div>
                </div>
                <div class="border-b pb-2 mb-4">
                    <div class="flex space-x-4">
                        <button class="border-b-2 border-orange-500 pb-2">Sản phẩm</button>
                        <button class="pb-2">Thông tin khác</button>
                    </div>
                </div>
                <table class="w-full text-left">
                    <thead>
                        <tr class="border-b">
                            <th class="py-2">Sản phẩm</th>
                            <th class="py-2">Mô tả</th>
                            <th class="py-2">Số lượng</th>
                            <th class="py-2">Đơn vị</th>
                            <th class="py-2">Đơn giá</th>
                              <th class="py-2">Sản phẩm</th>
                            <th class="py-2">Mô tả</th>
                            <th class="py-2">Số lượng</th>
                            <th class <th class="py-2">Thành tiền</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr class="border-b">
                            <td class="py-2">Sách "Học lập trình"</td>
                            <td class="py-2">Sách hướng dẫn lập trình cơ bản</td>
                            <td class="py-2">5</td>
                            <td class="py-2">Cuốn</td>
                            <td class="py-2">150.000,00</td>
                            <td class="py-2">750.000 đ</td>
                        </tr>
                    </tbody>
                </table>
                <div class="flex justify-end mt-4">
                    <div class="w-1/3">
                        <div class="flex justify-between border-b py-2">
                            <span>Tổng:</span>
                            <span>750.000 đ</span>
                        </div>
                    </div>
                </div>
                <div class="mt-4 p-2 bg-yellow-100 border-l-4 border-yellow-500 text-yellow-700">
                    <p class="font-bold">Thông báo:</p>
                    <p>Vui lòng kiểm tra kỹ thông tin giao hàng trước khi xuất phát. Nếu có bất kỳ vấn đề nào, hãy liên hệ
                        với bộ phận hỗ trợ.</p>
                </div>
            </div>
        </div>
    </body>

</html>