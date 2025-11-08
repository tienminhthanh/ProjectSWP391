/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.cart;

import dao.CartItemDAO;
import dao.ProductDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.CartItem;
import model.product_related.Product;

/**
 *
 * @author ADMIN
 */
@WebServlet("/viewCart")
public class ViewCartController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final CartItemDAO cartItemDAO = CartItemDAO.getInstance();
    private final ProductDAO productDAO = ProductDAO.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String customerId = request.getParameter("customerID");
        int customerID = customerId != null ? Integer.parseInt(customerId) : 0;
        String currentURL = request.getParameter("currentURL");

        try {
            List<CartItem> cartItems = cartItemDAO.getCartItemsByCustomer(customerID);
            for (CartItem item : cartItems) {
                Product product = productDAO.getProductById(item.getProductID());
                item.setProduct(product);
                item.updateCartItemPrice(); 
                cartItemDAO.updateCartItem(item);
            }
            session.setAttribute("cartItems", cartItems);
            RequestDispatcher dispatcher = request.getRequestDispatcher("cart.jsp");
            dispatcher.forward(request, response);
        } catch (Exception ex) {
            Logger.getLogger(ViewCartController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (currentURL == null || currentURL.trim().isEmpty()) {
                currentURL = "viewCart?customerID=" + request.getParameter("customerID");
            }
    }
}