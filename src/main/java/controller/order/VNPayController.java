package controller.order;

import dao.OrderDAO;
import dao.VoucherDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import model.Account;
import model.CartItem;
import model.Voucher;
import utils.VNPayService;

@WebServlet(name = "VNPayController", urlPatterns = {"/VNPayController"})
public class VNPayController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        OrderDAO orderDAO = new OrderDAO();
        VoucherDAO voucherDAO = new VoucherDAO();

        List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");
        String orderTotalStr = request.getParameter("orderTotal");
        Double orderTotal = Double.parseDouble(orderTotalStr);
        Account account = (Account) session.getAttribute("account");
        String voucherIDParam = request.getParameter("voucherID");

        if (account == null) {
            response.sendRedirect("login");
            return;
        }

        Integer voucherID = null;
        if (voucherIDParam != null && !voucherIDParam.trim().isEmpty()) {
            int tempVoucherID = Integer.parseInt(voucherIDParam);
            Voucher voucher = voucherDAO.getVoucherByID(tempVoucherID);
            if (voucher != null) {
                voucherID = voucher.getVoucherID();
            }
        }
        try {
            // Tạo mã giao dịch tạm thời thay vì orderID
            String txnRef = String.valueOf(System.currentTimeMillis()); // Chỉ dùng số

            // Lưu thông tin tạm vào session để xử lý sau khi thanh toán
            session.setAttribute("txnRef", txnRef);
            session.setAttribute("cartItems", cartItems);
            session.setAttribute("orderTotal", orderTotal);
            session.setAttribute("account", account);
            session.setAttribute("addr", request.getParameter("addr"));
            session.setAttribute("shippingOption", request.getParameter("shippingOption"));
            session.setAttribute("voucherID", voucherID);

            // Gọi VNPayService để tạo link thanh toán
            String paymentUrl = VNPayService.generatePaymentUrl(txnRef, orderTotal, request);
            System.out.println(paymentUrl);
            // Chuyển hướng đến VNPay để thanh toán
            response.sendRedirect(paymentUrl);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Có lỗi xảy ra khi xử lý thanh toán VNPay. Vui lòng thử lại!");
            request.getRequestDispatcher("OrderSummaryView.jsp").forward(request, response);
        }
    }
}
