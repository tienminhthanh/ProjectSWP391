/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.cart;

import dao.CartItemDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.CartItem;

/**
 *
 * @author ADMIN
 */
@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private CartItemDAO cartItemDAO;

    @Override
    public void init() throws ServletException {
        cartItemDAO = new CartItemDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy danh sách Cart theo customerID (truyền vào dưới dạng tham số)
            String customerIdParam = request.getParameter("customerID");
            int customerID = customerIdParam != null ? Integer.parseInt(customerIdParam) : 0;
//            int customerID = 2;
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

        CartItem cartItem = new CartItem(customerID, productID, quantity, priceWithQuantity);

        try {
            if ("add".equals(action)) {
                addToCart(cartItem);
            } else if ("update".equals(action)) {
                int itemID = Integer.parseInt(request.getParameter("itemID"));
                cartItem.setItemID(itemID);
                updateCartItem(cartItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("cart.jsp");
    }

//    private void addCartItem(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException, SQLException {
//        int customerID = Integer.parseInt(request.getParameter("customerID"));
//        int productID = Integer.parseInt(request.getParameter("productID"));
//        int quantity = Integer.parseInt(request.getParameter("quantity"));
//        BigDecimal priceWithQuantity = new BigDecimal(request.getParameter("priceWithQuantity"));
//
//        CartItem cartItem = new CartItem(customerID, productID, quantity, priceWithQuantity);
//        cartItemDAO.addCartItem(cartItem);
//        response.sendRedirect("cart?customerID=" + customerID);
//    }
//
//    private void updateCartItem(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException, SQLException {
//        int itemID = Integer.parseInt(request.getParameter("itemID"));
//        int customerID = Integer.parseInt(request.getParameter("customerID"));
//        int productID = Integer.parseInt(request.getParameter("productID"));
//        int quantity = Integer.parseInt(request.getParameter("quantity"));
//        BigDecimal priceWithQuantity = new BigDecimal(request.getParameter("priceWithQuantity"));
//        CartItem cartitem = (CartItem) request.getSession().getAttribute("cart");
//
//        cartitem.setQuantity(quantity);
//        cartitem = new CartItem(customerID, productID, quantity, priceWithQuantity, itemID);
//        cartItemDAO.updateCartItem(cartitem);
//        response.sendRedirect("cart?customerID=" + customerID);
//    }
    private void addToCart(CartItem cartItem) throws Exception {
        cartItemDAO.addCartItem(cartItem);
    }

    private void updateCartItem(CartItem cartItem) throws Exception {
        cartItemDAO.updateCartItem(cartItem);
    }

    private void deleteCartItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        int itemID = Integer.parseInt(request.getParameter("itemID"));
        int customerID = Integer.parseInt(request.getParameter("customerID"));
        cartItemDAO.deleteCartItem(itemID);
        response.sendRedirect("cart?customerID=" + customerID);
    }
}
