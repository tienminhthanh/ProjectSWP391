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
import java.io.EOFException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.CartItem;
import model.Product;

/**
 *
 * @author ADMIN
 */
@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private CartItemDAO cartItemDAO;
    private Product product;

    @Override
    public void init() throws ServletException {
        cartItemDAO = new CartItemDAO();
        product = new Product();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String customerId = request.getParameter("customerID");
            int customerID = customerId != null ? Integer.parseInt(customerId) : 0;
            List<CartItem> cartItems = cartItemDAO.getCartItemsByCustomer(customerID);
            request.setAttribute("cartItems", cartItems);
            RequestDispatcher dispatcher = request.getRequestDispatcher("cart.jsp");
            dispatcher.forward(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(CartServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        int customerID = Integer.parseInt(request.getParameter("customerID"));
        int productID = Integer.parseInt(request.getParameter("productID"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        BigDecimal priceWithQuantity = new BigDecimal(request.getParameter("priceWithQuantity"));

        try {
            if ("add".equals(action)) {
                CartItem cartItem = new CartItem(customerID, productID, quantity, priceWithQuantity);
                addToCart(cartItem);
            } else if ("update".equals(action)) {
                int itemID = Integer.parseInt(request.getParameter("itemID"));
                CartItem cartItem = new CartItem(itemID, customerID, productID, quantity, priceWithQuantity);
                updateCartItem(cartItem);
            } else if ("delete".equals(action)) {
                int itemID = Integer.parseInt(request.getParameter("itemID"));
                deleteCartItem(itemID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("cart?customerID=" + customerID);
    }

    private void addToCart(CartItem cartItem) throws SQLException, Exception {
//        if (cartItem.getQuantity() > product.getStockCount()) {
//            throw new Exception("Not enough stock for this product!");
//        }
        cartItemDAO.addCartItem(cartItem);
    }

    private void updateCartItem(CartItem cartItem) throws SQLException, Exception {
//        if (cartItem.getQuantity() > product.getStockCount()) {
//            throw new Exception("Not enough stock for this product!");
//        }
        cartItemDAO.updateCartItem(cartItem);
    }

    private void deleteCartItem(int itemID) throws SQLException {
        cartItemDAO.deleteCartItem(itemID);
    }
}
