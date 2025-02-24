package controller.order;

import dao.OrderDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import model.CartItem;
import model.OrderInfo;
import model.OrderProduct;

@WebServlet(name = "OrderController", urlPatterns = {"/OrderController"})
public class OrderController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
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
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Retrieve the cart items from the session
        List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");

        if (cartItems == null || cartItems.isEmpty()) {
            response.sendRedirect("home"); // Redirect to home if cart is empty
            return;
        }

        // Calculate the order subtotal
        double subtotal = 0;
        for (CartItem item : cartItems) {
            subtotal += item.getPriceWithQuantity().doubleValue();
        }

        // Set attributes for the cart and subtotal
        request.setAttribute("cartItems", cartItems);
        request.setAttribute("priceWithQuantity", subtotal);

        // Forward to the order summary view
        RequestDispatcher dispatcher = request.getRequestDispatcher("OrderSummaryView.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Retrieve the cart items from the session
        List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");

        if (cartItems == null || cartItems.isEmpty()) {
            response.sendRedirect("home"); // Redirect to home if cart is empty
            return;
        }

        // Calculate the order subtotal
        double subtotal = 0;
        for (CartItem item : cartItems) {
            subtotal += item.getPriceWithQuantity().doubleValue();
        }

        // Retrieve shipping option and discount code from request
        String shippingOption = request.getParameter("shippingOption"); // "DEL001" or "DEL002"
        String discountCode = request.getParameter("discountCode");

        // Calculate shipping fee based on the selected option
        double shippingFee = 0;
        if ("DEL001".equals(shippingOption)) {
            shippingFee = 30000; // Express shipping
        } else if ("DEL002".equals(shippingOption)) {
            shippingFee = 15000; // Economy shipping
        }

        // Apply discount if valid
        double discount = 0;
        if ("DISCOUNT10".equals(discountCode)) {
            discount = 10000; // Discount for valid code , ex
        }

        // Calculate the total order amount
        double orderTotalAmount = subtotal + shippingFee - discount;

        // Set attributes for cart, shipping fee, discount, and total amount
        request.setAttribute("cartItems", cartItems);
        request.setAttribute("priceWithQuantity", subtotal);
        request.setAttribute("optionCost", shippingFee);
        request.setAttribute("voucherValue", discount);
        request.setAttribute("orderTotalAmount", orderTotalAmount);

        // Retrieve other parameters from the request (delivery address, payment method, etc.)
        String deliveryAddress = request.getParameter("deliveryAddress");
        String paymentMethod = request.getParameter("paymentMethod");

        // Create a new OrderInfo object
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setDeliveryAddress(deliveryAddress);
        orderInfo.setDeliveryOptionID(Integer.parseInt(shippingOption)); // Update with correct option ID
        orderInfo.setOrderStatus("Pending");
        orderInfo.setDeliveryStatus("Pending");
        orderInfo.setPaymentMethod(paymentMethod);
        orderInfo.setPaymentStatus("Pending");

        // Set other properties like customer, voucher
        orderInfo.setCustomerID(Integer.parseInt(request.getParameter("customerID")));
        orderInfo.setPreVoucherAmount(Integer.parseInt(request.getParameter("preVoucherAmount")));
        orderInfo.setVoucherID(Integer.parseInt(request.getParameter("voucherID")));
        orderInfo.setStaffID(Integer.parseInt(request.getParameter("staffID")));
        orderInfo.setShipperID(Integer.parseInt(request.getParameter("shipperID")));

        // Add the cart items to the order
        List<OrderProduct> orderProducts = (List<OrderProduct>) request.getSession().getAttribute("cart");
        orderInfo.setOrderProductList(orderProducts);

        // Call DAO to insert the order into the database
        OrderDAO orderDAO = new OrderDAO();
        try {
            orderDAO.insertOrderInfo(orderInfo);
            //for de update stock cua product
            for (OrderProduct orderProduct : orderProducts) {
                // Call the updateProductStock method for each product in the order
                boolean isUpdated = orderDAO.updateProductStock(orderProduct.getProductID(), orderProduct.getQuantity());
                if (isUpdated) {
                    System.out.println("Stock updated for product ID: " + orderProduct.getProductID());
                } else {
                    System.out.println("Failed to update stock for product ID: " + orderProduct.getProductID());
                }
            }
            response.sendRedirect("cart");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("home");
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    public String getServletInfo() {
        return "Order Controller Servlet";
    }
}
