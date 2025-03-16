package utils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

public class VNPayService {
    public static String generatePaymentUrl(int orderId, double amount, String returnUrl) {
        try {
            // Lấy thông tin từ file cấu hình
            String vnp_TmnCode = VNPayConfig.getVnpTmnCode();
            String vnp_HashSecret = VNPayConfig.getVnpHashSecret();
            String vnp_Url = VNPayConfig.getVnpUrl();

            // Khởi tạo danh sách tham số gửi lên VNPay
            Map<String, String> params = new HashMap<>();
            params.put("vnp_Version", "2.1.0");
            params.put("vnp_Command", "pay");
            params.put("vnp_TmnCode", vnp_TmnCode);
            params.put("vnp_Amount", String.valueOf(amount * 100)); // Chuyển sang đơn vị VNĐ x100
            params.put("vnp_CurrCode", "VND");
            params.put("vnp_TxnRef", String.valueOf(orderId)); // Mã đơn hàng
            params.put("vnp_OrderInfo", "Thanh toán đơn hàng #" + orderId);
            params.put("vnp_Locale", "vn");
            params.put("vnp_ReturnUrl", returnUrl);
            params.put("vnp_CreateDate", new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

            // Sắp xếp tham số theo thứ tự alphabet
            List<String> sortedKeys = new ArrayList<>(params.keySet());
            Collections.sort(sortedKeys);
            StringBuilder query = new StringBuilder();
            StringBuilder hashData = new StringBuilder();

            for (String key : sortedKeys) {
                String value = params.get(key);
                if (query.length() > 0) {
                    query.append("&");
                    hashData.append("&");
                }
                query.append(URLEncoder.encode(key, StandardCharsets.UTF_8)).append("=")
                     .append(URLEncoder.encode(value, StandardCharsets.UTF_8));
                hashData.append(key).append("=").append(value);
            }

            // Tạo chữ ký bảo mật HMAC-SHA512
            String secureHash = hmacSHA512(vnp_HashSecret, hashData.toString());
            query.append("&vnp_SecureHash=").append(secureHash);

            return vnp_Url + "?" + query.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String hmacSHA512(String key, String data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(key.getBytes(StandardCharsets.UTF_8));
        byte[] hashedBytes = md.digest(data.getBytes(StandardCharsets.UTF_8));

        StringBuilder hexString = new StringBuilder();
        for (byte b : hashedBytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
}
