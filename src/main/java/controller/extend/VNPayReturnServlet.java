//package controller.extend;
//
//import dao.OrderDAO;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//@WebServlet(name = "VnpayReturn", urlPatterns = {"/VnpayReturn"})
//
//public class VnpayReturn extends HttpServlet {
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        try {
//            String vnp_TxnRef = req.getParameter("vnp_TxnRef");  // Mã đơn hàng
//            String vnp_ResponseCode = req.getParameter("vnp_ResponseCode"); // Mã phản hồi
//            String vnp_TransactionStatus = req.getParameter("vnp_TransactionStatus"); // Trạng thái giao dịch
//            String vnp_Amount = req.getParameter("vnp_Amount"); // Số tiền thanh toán
//            String vnp_TransactionNo = req.getParameter("vnp_TransactionNo"); // Mã giao dịch VNPay
//            String vnp_PayMethod = req.getParameter("vnp_PayMethod"); // Phương thức thanh toán
//
//            OrderDAO orderDAO = new OrderDAO();
//            int orderId = Integer.parseInt(vnp_TxnRef);
//            boolean isSuccess = "00".equals(vnp_TransactionStatus); // "00" = Thanh toán thành công
//
//            // Cập nhật trạng thái đơn hàng trong database
////            orderDAO.updateOrderStatus(orderId, isSuccess ? "Completed" : "Failed");
//
//            // Lưu thông tin giao dịch VNPay
////            orderDAO.updateTransactionInfo(orderId, vnp_TransactionNo, vnp_PayMethod, Double.parseDouble(vnp_Amount) / 100);
//
//            // Điều hướng về trang phù hợp
//            if (isSuccess) {
//                 resp.sendRedirect("OrderListController");
//            } else {
//                resp.sendRedirect("order-failed.jsp?orderId=" + orderId);
//            }
//
//        } catch (Exception ex) {
//            Logger.getLogger(VnpayReturn.class.getName()).log(Level.SEVERE, null, ex);
//            resp.sendRedirect("order-failed.jsp?error=exception");
//        }
//    }
//}
