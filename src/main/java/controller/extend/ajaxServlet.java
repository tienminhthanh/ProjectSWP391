package controller.extend;

import dao.OrderDAO;
import dao.VoucherDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Account;
import model.OrderInfo;
import model.Voucher;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.CartItem;
import model.OrderProduct;

@WebServlet(name = "ajaxServlet", urlPatterns = {"/ajaxServlet"})

public class ajaxServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Account account = (Account) session.getAttribute("account");
        List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");
        String orderTotalStr = req.getParameter("orderTotal");
        String voucherIDParam = req.getParameter("voucherID");
        String selectedAddress = req.getParameter("selectedAddress");
        String defaultDeliveryAddress = req.getParameter("defaultDeliveryAddress");

        if (account == null) {
            resp.sendRedirect("login");
            return;
        }
        if (orderTotalStr == null || cartItems == null || cartItems.isEmpty()) {
            resp.sendRedirect("cart");
            return;
        }

        double orderTotal = Double.parseDouble(orderTotalStr);
        OrderDAO orderDAO = new OrderDAO();
        VoucherDAO voucherDAO = new VoucherDAO();

        Integer voucherID = null;
        if (voucherIDParam != null && !voucherIDParam.trim().isEmpty()) {
            int tempVoucherID = Integer.parseInt(voucherIDParam);
            Voucher voucher = voucherDAO.getVoucherByID(tempVoucherID);
            if (voucher != null) {
                voucherID = voucher.getVoucherID();
            }
        }
        int id = 0;
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCustomerID(account.getAccountID());
        try {
            orderInfo.setDeliveryOptionID(Integer.parseInt(req.getParameter("shippingOption")));
            if (selectedAddress != "") {
                orderInfo.setDeliveryAddress(orderDAO.getAddressDetailByAddressID(Integer.parseInt(selectedAddress)));
            } else {
                orderInfo.setDeliveryAddress(defaultDeliveryAddress);
            }
            orderInfo.setDeliveryOptionID(Integer.parseInt(req.getParameter("shippingOption")));
            orderInfo.setPreVoucherAmount(orderTotal);
            orderInfo.setVoucherID(voucherID);
            orderInfo.setPaymentMethod(req.getParameter("paymentMethod"));
            List< OrderProduct> orderProductList = new ArrayList<>();
            for (CartItem item : cartItems) {
                OrderProduct orderProduct = new OrderProduct(item.getProductID(), item.getQuantity(), item.getPriceWithQuantity().intValue());
                orderProductList.add(orderProduct);
            }
            orderInfo.setOrderProductList(orderProductList);
            orderDAO.insertOrderInfo(orderInfo);
            id = orderDAO.getLastInsertedOrderID(account.getAccountID());
            session.setAttribute("orderIdNew", id);
            
//            orderDAO.updatepaymentStatusByOrderID(id); bỏ bên đây
            if (voucherID != null) {
                orderDAO.updateQuantityVoucher(voucherID);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ajaxServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        session.removeAttribute("cartItems");
        String newTxnRef = Config.getRandomNumber(8); // Sinh mã mới, tránh trùng lặp
        try {
            orderDAO.updateTxnRefByOrderID(id, newTxnRef);
        } catch (SQLException ex) {
            Logger.getLogger(ajaxServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        long amount = (long) (orderTotal * 100);
        String vnp_TxnRef = newTxnRef;
        String vnp_IpAddr = Config.getIpAddress(req);
        String vnp_TmnCode = Config.vnp_TmnCode;
        System.out.println("ma key:"+vnp_TmnCode);
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_ReturnUrl", Config.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        String locate = req.getParameter("language");
        if (locate != null && !locate.isEmpty()) {
            vnp_Params.put("vnp_Locale", locate);
        } else {
            vnp_Params.put("vnp_Locale", "vn");
        }
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII)).append('&');
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII)).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII)).append('&');
            }
        }

        if (hashData.length() > 0) {
            hashData.setLength(hashData.length() - 1);
            query.setLength(query.length() - 1);
        }

        String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
        System.out.println("ma key:"+Config.secretKey);
         System.out.println("ma key:"+vnp_SecureHash);
        query.append("&vnp_SecureHash=").append(vnp_SecureHash);
        String paymentUrl = Config.vnp_PayUrl + "?" + query;
        resp.sendRedirect(paymentUrl);
        System.out.println(paymentUrl);
    }
}
