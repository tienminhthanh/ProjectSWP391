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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Account;
import model.CartItem;
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

        if ("checkOut".equals(action)) {
            List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");
            if (cartItems == null || cartItems.isEmpty()) {
                response.sendRedirect("home");
                return;
            }

            VoucherDAO vDao = new VoucherDAO();
            List<Voucher> listVoucher = vDao.getListVoucher();
            request.setAttribute("listVoucher", listVoucher);

            double subtotal = 0;
            for (CartItem item : cartItems) {
                subtotal += item.getPriceWithQuantity().doubleValue();
            }

            Account account = (Account) session.getAttribute("account");
            if (account != null) {
                request.setAttribute("fullName", account.getUsername());
                request.setAttribute("phone", account.getPhoneNumber());
                request.setAttribute("email", account.getEmail());
            }

            request.setAttribute("cartItems", cartItems);
            request.setAttribute("priceWithQuantity", subtotal);
        } else if ("buyNow".equals(action)) {
            List<CartItem> cartItems = new ArrayList<>();

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
            VoucherDAO vDao = new VoucherDAO();
            List<Voucher> listVoucher = vDao.getListVoucher();
            request.setAttribute("listVoucher", listVoucher);

            int sum = (int) (product.getPrice() * quantity);
            BigDecimal subtotal = BigDecimal.valueOf(sum);
            cartItems.add(new CartItem(account.getAccountID(), product, quantity, subtotal));
            request.setAttribute("cartItems", cartItems);
            request.setAttribute("priceWithQuantity", subtotal);
            session.setAttribute("cartItems", cartItems);
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
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }

        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            response.sendRedirect("login");
            return;
        }

        int subtotal = 0;
        for (CartItem item : cartItems) {
            subtotal += item.getPriceWithQuantity().doubleValue();
        }

        if ("1".equals(request.getParameter("shippingOption"))) {
            subtotal += 50000;
        } else {
            subtotal += 30000;
        }

        String voucherIDParam = request.getParameter("voucherID");
        VoucherDAO voucherDAO = new VoucherDAO();

        Integer voucherID = null; // Mặc định là null nếu không chọn voucher

        if (voucherIDParam != null && !voucherIDParam.trim().isEmpty()) {
            try {
                int tempVoucherID = Integer.parseInt(voucherIDParam);
                Voucher voucher = voucherDAO.getVoucherByID(tempVoucherID);
                if (voucher != null && voucher.isIsActive() && subtotal >= voucher.getMinimumPurchaseAmount()) {
                    subtotal -= voucher.getVoucherValue();
                    voucherID = tempVoucherID; // Chỉ gán khi voucher hợp lệ
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

            List<OrderProduct> orderProductList = new ArrayList<>();
            for (CartItem item : cartItems) {
                OrderProduct orderProduct = new OrderProduct(item.getProductID(), item.getQuantity(), item.getPriceWithQuantity().intValue());
                orderProductList.add(orderProduct);
            }
            orderInfo.setOrderProductList(orderProductList);

            OrderDAO orderDAO = new OrderDAO();
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
