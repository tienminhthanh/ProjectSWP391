package utils;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class VNPayService {

    public static String generatePaymentUrl(String txnRef, double amount, HttpServletRequest request) {
        try {
            // Lấy thông tin cấu hình từ VNPayConfig
            String vnp_TmnCode = VNPayConfig.getVnpTmnCode();
            String vnp_HashSecret = VNPayConfig.getVnpHashSecret();
            String vnp_Url = VNPayConfig.getVnpUrl();
            String returnUrl = "https://111a-2402-800-6343-c8-600c-80cf-8620-be3d.ngrok-free.app/VNPayReturn";

            // Khởi tạo danh sách tham số gửi lên VNPay
            Map<String, String> params = new HashMap<>();
            params.put("vnp_Version", "2.1.0");
            params.put("vnp_Command", "pay");
            params.put("vnp_TmnCode", vnp_TmnCode);
            params.put("vnp_Amount", String.valueOf((long) (amount * 100))); // VNPay yêu cầu x100
            params.put("vnp_CurrCode", "VND");
            params.put("vnp_TxnRef", txnRef);
            params.put("vnp_OrderInfo", "Thanh toan GD: " + txnRef);
            params.put("vnp_Locale", "vn");
            params.put("vnp_ReturnUrl", returnUrl);

            // Thêm thời gian tạo giao dịch
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
            params.put("vnp_CreateDate", sdf.format(new Date()));

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

            // Debug: In ra các tham số trước khi tạo hash
            System.out.println("=== Debug VNPay Params ===");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                System.out.println(entry.getKey() + " = " + entry.getValue());
            }

            // Tạo chữ ký bảo mật HMAC-SHA512
            String secureHash = hmacSHA512(vnp_HashSecret, hashData.toString());
            query.append("&vnp_SecureHash=").append(secureHash);

            System.out.println("Dữ liệu trước khi mã hóa: " + hashData.toString());
            System.out.println("Chữ ký tạo ra: " + secureHash);

            return vnp_Url + "?" + query.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String hmacSHA512(String key, String data) throws Exception {
        Mac sha512_HMAC = Mac.getInstance("HmacSHA512");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        sha512_HMAC.init(secret_key);
        byte[] hashedBytes = sha512_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));

        // Convert byte array to hex
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashedBytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    /**
     * Kiểm tra chữ ký vnp_SecureHash từ VNPay
     */
    public static boolean validateResponse(Map<String, String[]> params) {
        try {
            String vnp_HashSecret = VNPayConfig.getVnpHashSecret();

            // Lọc và sắp xếp tham số (loại bỏ vnp_SecureHash)
            SortedMap<String, String> sortedParams = new TreeMap<>();
            for (Map.Entry<String, String[]> entry : params.entrySet()) {
                if (!"vnp_SecureHash".equals(entry.getKey()) && !"vnp_SecureHashType".equals(entry.getKey())) {
                    sortedParams.put(entry.getKey(), entry.getValue()[0]);
                }
            }

            // Tạo chuỗi dữ liệu để hash
            StringBuilder hashData = new StringBuilder();
            for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
                if (hashData.length() > 0) {
                    hashData.append("&");
                }
                hashData.append(entry.getKey()).append("=").append(entry.getValue());
            }

            // Debug: In dữ liệu trước khi tạo hash
            System.out.println("Dữ liệu cần kiểm tra hash: " + hashData.toString());

            // Tạo hash từ dữ liệu nhận được
            String generatedHash = hmacSHA512(vnp_HashSecret, hashData.toString());

            // Debug: In hash để kiểm tra
            System.out.println("Hash nhận từ VNPay: " + params.get("vnp_SecureHash")[0]);
            System.out.println("Hash tự tính toán: " + generatedHash);

            // So sánh hash nhận từ VNPay với hash tự tính
            return generatedHash.equals(params.get("vnp_SecureHash")[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
