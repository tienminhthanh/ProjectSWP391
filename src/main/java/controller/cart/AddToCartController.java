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
import java.util.List;
import model.CartItem;
import model.Product;


/**
 *
 * @author ADMIN
 */
@WebServlet("/addToCart")
public class AddToCartController extends HttpServlet {

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
            double price = Double.parseDouble(priceStr);

            // Validate session
            if (session.getAttribute("account") == null) {
                response.sendRedirect("login");
                return;
            }

            Product product = productDAO.getProductById(productID);
            
            if (product == null) {
                throw new Exception("Product not found!");
            }
            product.setPrice(price);
            addToCart(request, customerID, productID, quantity, priceWithQuantity, product);

            if (currentURL == null || currentURL.trim().isEmpty()) {
                currentURL = "home";
            }
            response.sendRedirect(currentURL);
            return;

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void addToCart(HttpServletRequest request, int customerID, int productID, int quantity,
            BigDecimal priceWithQuantity, Product product) throws SQLException, Exception {
        HttpSession session = request.getSession();

        // Check product availability
        if (product == null) {
            throw new Exception("Product not found!");
        }

        // Check stock
        if (quantity > product.getStockCount()) {
            throw new Exception("Not enough stock for this product! Available: " + product.getStockCount());
        }

        // Get cart from session or database
        List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");
        if (cartItems == null) {
            cartItems = cartItemDAO.getCartItemsByCustomer(customerID);
            session.setAttribute("cartItems", cartItems);
        }
        // Check if product already exists in cart
        CartItem existingCartItem = cartItemDAO.getCartItemByCustomerAndProduct(customerID, productID);
        if (existingCartItem != null) {
            // Update quantity if product exists
            int newQuantity = existingCartItem.getQuantity() + quantity;
            if (newQuantity > product.getStockCount()) {
                throw new Exception("Total quantity exceeds available stock! Available: " + product.getStockCount());
            }
            // Update existing item using the constructor with itemID
            CartItem updatedCartItem = new CartItem(existingCartItem.getItemID(),
                    customerID,
                    productID,
                    newQuantity,
                    priceWithQuantity
            );
            cartItemDAO.updateCartItem(updatedCartItem);

            // Update the item in the session list
            for (int i = 0; i < cartItems.size(); i++) {
                if (cartItems.get(i).getItemID() == existingCartItem.getItemID()) {
                    cartItems.set(i, updatedCartItem);
                    break;
                }
            }
        } else {
            // Add new item to cart
            CartItem cartItem = new CartItem(customerID, productID, quantity, priceWithQuantity);
            cartItemDAO.addCartItem(cartItem);
            cartItems.add(cartItem);
        }

        session.setAttribute("cartItems", cartItems);
    }
}