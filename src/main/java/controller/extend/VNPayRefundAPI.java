package controller.extend;

import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class VNPayRefundAPI {

    public static boolean refundTransaction(String vnpTxnRef, String vnpTransactionNo, double amount, String TransactionDate) {
        try {
            // URL API hoàn tiền VNPay
            String apiUrl = "https://sandbox.vnpayment.vn/merchant_webapi/api/transaction";

            // Chuẩn bị tham số request
            Map<String, String> params = new HashMap<>();

            params.put("vnp_RequestId", UUID.randomUUID().toString().replace("-", ""));
            params.put("vnp_Version", "2.1.0");
            params.put("vnp_Command", "refund");
            params.put("vnp_TmnCode", Config.vnp_TmnCode);
            params.put("vnp_TransactionType", "02"); // 02: Hoàn toàn, 03: Một phần
            params.put("vnp_TxnRef", vnpTxnRef); // Mã đơn hàng
            params.put("vnp_TransactionNo", vnpTransactionNo); // Mã giao dịch từ VNPay
            params.put("vnp_Amount", String.valueOf((int) (amount * 100))); // Đơn vị VND x100
            params.put("vnp_OrderInfo", "Refund for order: " + vnpTxnRef);
            params.put("vnp_CreateBy", "admin");
            params.put("vnp_IpAddr", "127.0.0.1");

            // 🔴 THÊM vnp_TransactionDate (phải lấy từ DB)
            params.put("vnp_TransactionDate", TransactionDate); // Lấy từ DB, không được để null!

            // Tạo thời gian phát sinh yêu cầu hoàn tiền
            String vnpCreateDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            params.put("vnp_CreateDate", vnpCreateDate);

            // Sắp xếp key theo thứ tự (Bắt buộc theo quy định của VNPay)
            List<String> fieldNames = new ArrayList<>(params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();

            for (String fieldName : fieldNames) {
                String fieldValue = params.get(fieldName);
                if (fieldValue != null && !fieldValue.isEmpty()) {
                    hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, "UTF-8"))
                            .append('&');
                    query.append(URLEncoder.encode(fieldName, "UTF-8")).append('=')
                            .append(URLEncoder.encode(fieldValue, "UTF-8")).append('&');
                }
            }

            if (hashData.length() > 0) {
                hashData.setLength(hashData.length() - 1);
                query.setLength(query.length() - 1);
            }

            // Tạo SecureHash
            String secureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
            params.put("vnp_SecureHash", secureHash);
            System.out.println("Params: " + params);

            // Gửi request API
            String response = sendPostRequest(apiUrl, params);
            System.out.println("VNPay Refund Response: " + response);

            // Kiểm tra phản hồi
            return response.contains("\"vnp_ResponseCode\":\"00\"");

        } catch (Exception e) {
            System.out.println("Lỗi khi gửi yêu cầu hoàn tiền: " + e.getMessage());
            e.printStackTrace();
            return false;
        }

    }

    private static String sendPostRequest(String url, Map<String, String> params) throws Exception {
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (postData.length() != 0) {
                postData.append("&");
            }
            postData.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            postData.append("=");
            postData.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        URL requestUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) requestUrl.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);

        // Kiểm tra response code
        int responseCode = conn.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        InputStream inputStream = (responseCode >= 200 && responseCode < 300) ? conn.getInputStream() : conn.getErrorStream();

        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        
        
        System.out.println("VNPay Response: " + response.toString());
        return response.toString();
    }

}
