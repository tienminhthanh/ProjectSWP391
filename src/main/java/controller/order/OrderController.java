package controller.order;

import dao.OrderDAO;
import dao.ProductDAO;
import dao.VoucherDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Account;
import model.CartItem;
import model.DeliveryOption;
import model.OrderInfo;
import model.OrderProduct;
import model.Product;
import model.Voucher;

/**
 *
 * @author Macbook
 */
@WebServlet(name = "OrderController", urlPatterns = {"/OrderController"})
public class OrderController extends HttpServlet {

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet OrderController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet OrderController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String action = request.getParameter("action");
        Map<Integer, Double> computedValues = new HashMap<>();
        List<Double> computedValuesList = new ArrayList<>();
        int bestVoucherID = 0;
        List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");

        if ("checkOut".equals(action)) {

            if (cartItems == null || cartItems.isEmpty()) {
                response.sendRedirect("home");
                return;
            }
            double subtotal = 0;

            List<DeliveryOption> deliveryOptions = new ArrayList<>();

            try {
                OrderDAO OrderDAO = new OrderDAO();
                deliveryOptions = OrderDAO.getAllDeliveryOptions();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            for (CartItem item : cartItems) {
                BigDecimal priceWithQuantity = item.getPriceWithQuantity().multiply(BigDecimal.valueOf(item.getQuantity()));
                subtotal += priceWithQuantity.doubleValue();
            }

            VoucherDAO vDao = new VoucherDAO();
            List<Voucher> listVoucher = vDao.getListVoucher();
            List<Voucher> validVouchers = new ArrayList<>();
            double valueOfVoucher = 0;
            for (Voucher voucher : listVoucher) {
                if (subtotal >= voucher.getMinimumPurchaseAmount() && voucher.isIsActive()) {
                    validVouchers.add(voucher);
                    if (voucher.getVoucherType().equals("FIXED_AMOUNT")) {
                        valueOfVoucher = voucher.getVoucherValue();
                    } else {
                        valueOfVoucher = (subtotal * voucher.getVoucherValue()) / 100;
                        if (valueOfVoucher >= voucher.getMaxDiscountAmount()) {
                            valueOfVoucher = voucher.getMaxDiscountAmount();
                        }
                    }
                    computedValues.put(voucher.getVoucherID(), valueOfVoucher); // 
                    computedValuesList.add(valueOfVoucher);
                }
            }
            double bestValueVoucher = 0;

            for (int i = 0; i < computedValuesList.size(); i++) {
                double value = computedValuesList.get(i);
                if (bestValueVoucher <= value) {
                    bestValueVoucher = value;
                }
            }

            for (Map.Entry<Integer, Double> entry : computedValues.entrySet()) {
                if (entry.getValue().equals(bestValueVoucher)) {
                    bestVoucherID = entry.getKey();
                    break;
                }
            }

            request.setAttribute("bestVoucherID", bestVoucherID);
            request.setAttribute("listVoucher", validVouchers);
            Account account = (Account) session.getAttribute("account");
            if (account != null) {
                request.setAttribute("fullName", account.getUsername());
                request.setAttribute("phone", account.getPhoneNumber());
                request.setAttribute("email", account.getEmail());
            }
            request.setAttribute("cartItems", cartItems);
            request.setAttribute("deliveryOptions", deliveryOptions);
            request.setAttribute("computedValues", computedValues);
            request.setAttribute("priceWithQuantity", subtotal);
        } else if ("buyNow".equals(action)) {

            cartItems = new ArrayList<>();

            Account account = (Account) session.getAttribute("account");
            if (account == null) {
                response.sendRedirect("home");
                return;
            }

            int productID = Integer.parseInt(request.getParameter("productID"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            if (account != null) {
                request.setAttribute("fullName", account.getUsername());
                request.setAttribute("phone", account.getPhoneNumber());
                request.setAttribute("email", account.getEmail());
            }
            ProductDAO productDAO = new ProductDAO();
            Product product = null;
            try {
                product = productDAO.getProductById(productID);
            } catch (SQLException ex) {
                Logger.getLogger(OrderController.class.getName()).log(Level.SEVERE, null, ex);
            }
            List<DeliveryOption> deliveryOptions = new ArrayList<>();

            try {
                OrderDAO OrderDAO = new OrderDAO();
                deliveryOptions = OrderDAO.getAllDeliveryOptions();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Double sum = product.getDiscountPercentage() > 0 ? (quantity * product.getPrice() * (100-product.getDiscountPercentage())/100) : (product.getPrice() * quantity);
            BigDecimal subtotal = BigDecimal.valueOf(sum);
            VoucherDAO vDao = new VoucherDAO();
            List<Voucher> listVoucher = vDao.getListVoucher();
            List<Voucher> validVouchers = new ArrayList<>();
            double valueOfVoucher = 0;
            for (Voucher voucher : listVoucher) {
                if (sum >= voucher.getMinimumPurchaseAmount() && voucher.isIsActive()) {
                    validVouchers.add(voucher);
                    if (voucher.getVoucherType().equals("FIXED_AMOUNT")) {
                        valueOfVoucher = voucher.getVoucherValue();
                    } else {
                        valueOfVoucher = (sum * voucher.getVoucherValue()) / 100;
                        if (valueOfVoucher >= voucher.getMaxDiscountAmount()) {
                            valueOfVoucher = voucher.getMaxDiscountAmount();
                        }
                    }
                    computedValues.put(voucher.getVoucherID(), valueOfVoucher); // 
                    computedValuesList.add(valueOfVoucher);
                }
            }
            double bestValueVoucher = 0;

            for (int i = 0; i < computedValuesList.size(); i++) {
                double value = computedValuesList.get(i);
                if (bestValueVoucher <= value) {
                    bestValueVoucher = value;
                }
            }

            for (Map.Entry<Integer, Double> entry : computedValues.entrySet()) {
                if (entry.getValue().equals(bestValueVoucher)) {
                    bestVoucherID = entry.getKey();
                    break;
                }
            }
            request.setAttribute("bestVoucherID", bestVoucherID);
            request.setAttribute("computedValues", computedValues);
            request.setAttribute("listVoucher", validVouchers);
            cartItems.add(new CartItem(account.getAccountID(), product, quantity, subtotal));
            request.setAttribute("cartItems", cartItems);
            session.setAttribute("cartItems", cartItems);
            request.setAttribute("priceWithQuantity", subtotal);
            request.setAttribute("deliveryOptions", deliveryOptions);

        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("OrderSummaryView.jsp");
        dispatcher.forward(request, response);
    }

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");
        DeliveryOption delivery = new DeliveryOption();
        int estimatedTime = 0;
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }

        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            response.sendRedirect("login");
            return;
        }

        Double subtotal = 0.0;
        Double preOrderPrice = 0.0;
        for (CartItem item : cartItems) {
            subtotal += item.getPriceWithQuantity().doubleValue();
            preOrderPrice = subtotal;

        }
        OrderDAO orderDAO = new OrderDAO();
        String deliveryOptionID = request.getParameter("shippingOption");

        if (deliveryOptionID != null) {
            try {
                int ID = Integer.parseInt(deliveryOptionID);
                delivery = orderDAO.getDeliveryOption(ID);

                if (delivery != null && delivery.getDeliveryOptionID() == ID) {
                    subtotal += delivery.getOptionCost(); // Cộng phí giao hàng vào tổng tiền

                } else {
                    System.out.println("Không tìm thấy phương thức giao hàng với ID: " + ID);
                }
            } catch (SQLException | NumberFormatException e) {
                e.printStackTrace();
            }
        }

        String voucherIDParam = request.getParameter("voucherID");
        VoucherDAO voucherDAO = new VoucherDAO();
        int tempVoucherID = Integer.parseInt(voucherIDParam);

        Integer voucherID = null; // Mặc định là null nếu không chọn voucher

        if (tempVoucherID > 0 && !voucherIDParam.trim().isEmpty()) {
            try {
                double valueOfVoucher = 0;
                Voucher voucher = voucherDAO.getVoucherByID(tempVoucherID);
                voucherID = voucher.getVoucherID();
                if (voucherID != null && voucher.isIsActive() && preOrderPrice >= voucher.getMinimumPurchaseAmount()) {
                    if (voucher.getVoucherType().equals("FIXED_AMOUNT")) {
                        valueOfVoucher = voucher.getVoucherValue();
                    } else {
                        valueOfVoucher = (subtotal * voucher.getVoucherValue()) / 100;
                        if (valueOfVoucher >= voucher.getMaxDiscountAmount()) {
                            valueOfVoucher = voucher.getMaxDiscountAmount();
                        }
                    }
                    subtotal -= valueOfVoucher;
                    voucherID = tempVoucherID; // Chỉ gán khi voucher hợp lệ
                    voucher.setQuantity(voucher.getQuantity() - 1);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid voucher ID format: " + voucherIDParam);
                voucherID = null;
            }
        }

        try {
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setCustomerID(account.getAccountID());
            orderInfo.setDeliveryAddress(request.getParameter("addr"));
            orderInfo.setDeliveryOptionID(Integer.parseInt(request.getParameter("shippingOption")));
            orderInfo.setPaymentMethod(request.getParameter("paymentMethod"));
            orderInfo.setPreVoucherAmount(subtotal);
            orderInfo.setVoucherID(voucherID);

            List< OrderProduct> orderProductList = new ArrayList<>();
            for (CartItem item : cartItems) {
                OrderProduct orderProduct = new OrderProduct(item.getProductID(), item.getQuantity(), item.getPriceWithQuantity().intValue());
                orderProductList.add(orderProduct);
            }
            orderInfo.setOrderProductList(orderProductList);

            orderDAO.insertOrderInfo(orderInfo);
            session.removeAttribute("cartItems");
            response.sendRedirect("OrderListController");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            response.sendRedirect("error.jsp");
        }
    }

    /**
     *
     * @return
     */
    @Override
    public String getServletInfo() {
        return "Order Controller Servlet";
    }
}
