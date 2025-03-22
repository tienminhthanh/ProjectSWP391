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
                Product product = null;
                ProductDAO productDAO = new ProductDAO();

                BigDecimal priceWithQuantity = item.getCartItemPrice().multiply(BigDecimal.valueOf(item.getCartItemQuantity()));
                subtotal += priceWithQuantity.doubleValue();
                try {
                    product = productDAO.getProductById(item.getProductID());
                    double a = product.getDiscountPercentage() > 0 ? product.getPrice() * (100 - product.getDiscountPercentage()) / 100 : product.getPrice();
                    item.getProduct().setPrice(a);
                } catch (SQLException ex) {
                    Logger.getLogger(OrderController.class.getName()).log(Level.SEVERE, null, ex);
                }

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
            if (product.getDiscountPercentage() > 0) {
                Double priceWithDiscount = 0.0;
                priceWithDiscount = product.getPrice() * (100 - product.getDiscountPercentage()) / 100;
                product.setPrice(priceWithDiscount);
            }

            Double sum = product.getDiscountPercentage() > 0 ? (quantity * product.getPrice() * (100 - product.getDiscountPercentage()) / 100) : (product.getPrice() * quantity);
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
        OrderInfo orderInfo = new OrderInfo();
        List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");
        String orderTotalStr = request.getParameter("orderTotal");
        Double orderTotal = Double.parseDouble(orderTotalStr);
        Account account = (Account) session.getAttribute("account");
        String voucherIDParam = request.getParameter("voucherID");

        OrderDAO orderDAO = new OrderDAO();
        VoucherDAO voucherDAO = new VoucherDAO();
        if (account == null) {
            response.sendRedirect("login");
            return;
        }
        Integer voucherID = null; // Mặc định là null nếu không chọn voucher
        if (!voucherIDParam.trim().isEmpty()) {
            int tempVoucherID = Integer.parseInt(voucherIDParam);
            Voucher voucher = voucherDAO.getVoucherByID(tempVoucherID);
            voucherID = voucher.getVoucherID();
        }
        try {
            orderInfo.setCustomerID(account.getAccountID());
            orderInfo.setDeliveryAddress(request.getParameter("addr"));
            orderInfo.setDeliveryOptionID(Integer.parseInt(request.getParameter("shippingOption")));
            orderInfo.setPaymentMethod(request.getParameter("paymentMethod"));
            orderInfo.setPreVoucherAmount(orderTotal);
            orderInfo.setVoucherID(voucherID);
            orderDAO.updatePreVoucherAmount(orderInfo.getOrderID(), orderTotal);
            if (voucherID != null) {
                orderDAO.updateQuantityVoucher(voucherID);
            }
            List< OrderProduct> orderProductList = new ArrayList<>();
            for (CartItem item : cartItems) {
                OrderProduct orderProduct = new OrderProduct(item.getProductID(), item.getCartItemQuantity(), item.getCartItemPrice().intValue());
                orderProductList.add(orderProduct);
            }
            orderInfo.setOrderProductList(orderProductList);
            orderDAO.insertOrderInfo(orderInfo);
            session.removeAttribute("cartItems");
            response.sendRedirect("OrderListController");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            request.setAttribute("errorMessage1", "Something went wrong. Please try again later!");
//            response.sendRedirect("error.jsp");
            request.getRequestDispatcher("OrderSummaryView.jsp").forward(request, response);
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
