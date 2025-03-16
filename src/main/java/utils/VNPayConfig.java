package utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class VNPayConfig {
    private static String vnp_TmnCode;
    private static String vnp_HashSecret;
    private static String vnp_Url;

    static {
        loadConfig();
    }

    private static void loadConfig() {
        try {
            InputStream inputStream = VNPayConfig.class.getClassLoader().getResourceAsStream("config.json");

            if (inputStream == null) {
                System.err.println("❌ Không tìm thấy file config.json! Kiểm tra lại thư mục resources.");
                return;
            }

            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            Gson gson = new Gson();
            JsonObject json = gson.fromJson(reader, JsonObject.class);

            vnp_TmnCode = json.get("vnp_TmnCode").getAsString();
            vnp_HashSecret = json.get("vnp_HashSecret").getAsString();
            vnp_Url = json.get("vnp_Url").getAsString();

            System.out.println("✅ VNPay Config Loaded Successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getVnpTmnCode() {
        return vnp_TmnCode;
    }

    public static String getVnpHashSecret() {
        return vnp_HashSecret;
    }

    public static String getVnpUrl() {
        return vnp_Url;
    }

    // 🛠 HÀM MAIN TEST
    public static void main(String[] args) {
        System.out.println("🔍 Đang kiểm tra VNPay Config...");
        
        System.out.println("👉 vnp_TmnCode: " + getVnpTmnCode());
        System.out.println("👉 vnp_HashSecret: " + getVnpHashSecret());
        System.out.println("👉 vnp_Url: " + getVnpUrl());
    }
}
