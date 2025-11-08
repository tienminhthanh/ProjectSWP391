/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.cart;

import dao.CartItemDAO;
import dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import model.CartItem;
import model.product_related.Product;

/**
 *
 * @author ADMIN
 */
@WebServlet("/updateCart")
public class UpdateCartController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final CartItemDAO cartItemDAO = CartItemDAO.getInstance();
    private final ProductDAO productDAO = ProductDAO.getInstance();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        try {
            // Check required parameters
            String customerIdStr = request.getParameter("customerID");
            String productIdStr = request.getParameter("productID");
            String quantityStr = request.getParameter("quantity");
            String priceStr = request.getParameter("priceWithQuantity");
            String currentURL = request.getParameter("currentURL");

            int customerID = Integer.parseInt(customerIdStr);
            int productID = Integer.parseInt(productIdStr);
            int quantity = Integer.parseInt(quantityStr);
            BigDecimal priceWithQuantity = new BigDecimal(priceStr);
            int itemID = Integer.parseInt(request.getParameter("itemID"));

            // Validate session
            if (session.getAttribute("account") == null) {
                response.sendRedirect("login");
                return;
            }

            if (customerIdStr == null || productIdStr == null || quantityStr == null || priceStr == null) {
                throw new Exception("Missing required parameters!");
            }
            Product product = productDAO.getProductById(productID);
            if (product == null) {
                throw new Exception("Product not found!");
            }
            CartItem cartItem = new CartItem(itemID, customerID, productID, quantity, priceWithQuantity);
            updateCartItem(cartItem, product);

            if (currentURL == null || currentURL.trim().isEmpty()) {
                currentURL = "viewCart?customerID=" + customerID;
            }
            response.sendRedirect(currentURL);
            return;

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void updateCartItem(CartItem cartItem, Product product) throws SQLException, Exception {
        // Check if updated quantity exceeds stock
        if (cartItem.getQuantity() > product.getStockCount()) {
            throw new Exception("Updated quantity exceeds available stock! Available: " + product.getStockCount());
        }
        cartItemDAO.updateCartItem(cartItem);
    }
}