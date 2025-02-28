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
import java.util.ArrayList;
import java.util.List;
import model.Account;
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
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");

        if (cartItems == null || cartItems.isEmpty()) {
            response.sendRedirect("home");
            return;
        }
        double subtotal = 0;
        for (CartItem item : cartItems) {
            subtotal += item.getPriceWithQuantity().doubleValue();
        }
        Account account = (Account) session.getAttribute("account");
        if (account != null) {
            // Set thông tin từ account vào request để gửi đến JSP
            request.setAttribute("fullName", account.getUsername());
//            request.setAttribute("address", account.());
            request.setAttribute("phone", account.getPhoneNumber());
            request.setAttribute("email", account.getEmail());
        }
        request.setAttribute("cartItems", cartItems);
        request.setAttribute("priceWithQuantity", subtotal);

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
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Retrieve the cart items from the session
        List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");

        if (cartItems == null || cartItems.isEmpty()) {
            response.sendRedirect("home"); // Redirect to home if cart is empty
            return;
        }

        Account account = (Account) session.getAttribute("account");

        if (account == null) {
            response.sendRedirect("login");
            return;
        }

        try {
            OrderInfo orderInfo = new OrderInfo();

            orderInfo.setCustomerID(account.getAccountID());
            orderInfo.setDeliveryAddress(request.getParameter("addr")); // Assuming form has a field for delivery address
            String test = (request.getParameter("shippingOption"));
            orderInfo.setDeliveryOptionID(Integer.parseInt(request.getParameter("shippingOption"))); // Delivery option selected by user
            orderInfo.setPaymentMethod(request.getParameter("paymentMethod"));

            // Add products to the OrderInfo object
            List<OrderProduct> orderProductList = new ArrayList<>();
            for (CartItem item : cartItems) {
                OrderProduct orderProduct = new OrderProduct(item.getProductID(), item.getQuantity(), item.getPriceWithQuantity().intValue());
                orderProductList.add(orderProduct);
            }
            orderInfo.setOrderProductList(orderProductList);

            // Insert the order info into the database
            OrderDAO orderDAO = new OrderDAO();
            orderDAO.insertOrderInfo(orderInfo);

            // Clear the cart after the order is successfully placed
            session.removeAttribute("cartItems");

            // Redirect the user to the order confirmation page or show a success message
//            request.setAttribute("totalAmount", orderInfo.getPreVoucherAmount());
            response.sendRedirect("OrderListController");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            response.sendRedirect("error.jsp"); // Redirect to an error page if something goes wrong
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
