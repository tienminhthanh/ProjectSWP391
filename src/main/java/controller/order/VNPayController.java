package controller.order;

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
import model.OrderInfo;
import utils.VNPayService; // Import service xử lý VNPay

@WebServlet(name="VNPayController", urlPatterns={"/VNPayController"})
public class VNPayController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         HttpSession session = request.getSession();
        OrderInfo orderInfo = new OrderInfo();
        List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");
        String orderTotalStr = request.getParameter("orderTotal");
        Double orderTotal = Double.parseDouble(orderTotalStr);
        Account account = (Account) session.getAttribute("account");
        String voucherIDParam = request.getParameter("voucherID");
//        String paymentUrl = VNPayService.createPaymentUrl(orderInfo.getOrderID(), orderTotal, request);

//        response.sendRedirect(paymentUrl);
    }
}
