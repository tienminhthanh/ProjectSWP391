package controller.order;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import utils.VNPayService;

@WebServlet(name = "VNPayReturnController", urlPatterns = {"/VNPayReturn"})
public class VNPayReturnController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Map<String, String[]> parameterMap = request.getParameterMap();

        // Log toàn bộ request parameters
        System.out.println("=== Debug VNPay Response ===");
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            System.out.println(entry.getKey() + " = " + String.join(", ", entry.getValue()));
        }

        // Kiểm tra chữ ký (SecureHash)
        boolean isValid = VNPayService.validateResponse(parameterMap);

        if (!isValid) {
            System.out.println("Giao dịch không hợp lệ! Chữ ký không khớp.");
            request.setAttribute("message", "Giao dịch không hợp lệ! Chữ ký không khớp.");
            request.getRequestDispatcher("OrderSummaryView.jsp").forward(request, response);
            return;
        }

        String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");
        String txnRef = request.getParameter("vnp_TxnRef");

        System.out.println("vnp_ResponseCode: " + vnp_ResponseCode);
        System.out.println("vnp_TxnRef: " + txnRef);

        if ("00".equals(vnp_ResponseCode)) {
            System.out.println("Thanh toán thành công! Mã giao dịch: " + txnRef);
            request.setAttribute("message", "Thanh toán thành công! Mã giao dịch: " + txnRef);
            // Xử lý cập nhật đơn hàng vào database (chưa có trong code này)
        } else {
            System.out.println("Thanh toán thất bại! Mã lỗi: " + vnp_ResponseCode);
            request.setAttribute("message", "Thanh toán thất bại! Mã lỗi: " + vnp_ResponseCode);
        }

        // Chuyển hướng về trang kết quả
        request.getRequestDispatcher("OrderSummaryView.jsp").forward(request, response);
    }
}