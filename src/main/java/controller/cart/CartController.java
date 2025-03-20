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
import model.Product;

/**
 *
 * @author ADMIN
 */
@WebServlet("/cart")
public class CartController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private CartItemDAO cartItemDAO;
    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        cartItemDAO = new CartItemDAO();
        productDAO = new ProductDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String customerId = request.getParameter("customerID");
        int customerID = customerId != null ? Integer.parseInt(customerId) : 0;

        try {
            List<CartItem> cartItems = cartItemDAO.getCartItemsByCustomer(customerID);
            session.setAttribute("cartItems", cartItems);
            RequestDispatcher dispatcher = request.getRequestDispatcher("cart.jsp");
            dispatcher.forward(request, response);
        } catch (Exception ex) {
            Logger.getLogger(CartController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String action = request.getParameter("action");

        try {
            // Check required parameters
            String customerIdStr = request.getParameter("customerID");
            String productIdStr = request.getParameter("productID");
            String quantityStr = request.getParameter("quantity");
            String priceStr = request.getParameter("priceWithQuantity");
            String currentURL = request.getParameter("currentURL");

            int customerID = Integer.parseInt(customerIdStr);

            // Validate session
            if (session.getAttribute("account") == null) {
                throw new Exception("Please login to add items to cart!");
            }

            if ("add".equals(action)) {
                int productID = Integer.parseInt(productIdStr);
                int quantity = Integer.parseInt(quantityStr);
                BigDecimal priceWithQuantity = new BigDecimal(priceStr);
                Product product = productDAO.getProductById(productID);
                if (product == null) {
                    throw new Exception("Product not found!");
                }
                addToCart(request, customerID, productID, quantity, priceWithQuantity, product);

                if (currentURL == null || currentURL.trim().isEmpty()) {
                    currentURL = "home";
                }
                response.sendRedirect(currentURL);
                return;

            } else if ("update".equals(action)) {
                int itemID = Integer.parseInt(request.getParameter("itemID"));
                int productID = Integer.parseInt(productIdStr);
                int quantity = Integer.parseInt(quantityStr);
                BigDecimal priceWithQuantity = new BigDecimal(priceStr);
                if (customerIdStr == null || productIdStr == null || quantityStr == null || priceStr == null) {
                    throw new Exception("Missing required parameters!");
                }
                Product product = productDAO.getProductById(productID);
                if (product == null) {
                    throw new Exception("Product not found!");
                }
                CartItem cartItem = new CartItem(itemID, customerID, productID, quantity, priceWithQuantity);
                updateCartItem(cartItem, product);

            } else if ("delete".equals(action)) {
                int itemID = Integer.parseInt(request.getParameter("itemID"));
                deleteCartItem(customerID, itemID);
                List<CartItem> cartItems = cartItemDAO.getCartItemsByCustomer(customerID); // Lấy lại danh sách mới
                session.setAttribute("cartItems", cartItems); // Cập nhật session
            }
            if (currentURL == null || currentURL.trim().isEmpty()) {
                currentURL = "cart?customerID=" + request.getParameter("customerID");
            }
            response.sendRedirect(currentURL);
            return;
//            response.sendRedirect("cart?customerID=" + request.getParameter("customerID"));

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

    private void updateCartItem(CartItem cartItem, Product product) throws SQLException, Exception {
//         Check if updated quantity exceeds stock
        if (cartItem.getQuantity() > product.getStockCount()) {
            throw new Exception("Updated quantity exceeds available stock! Available: " + product.getStockCount());
        }
        cartItemDAO.updateCartItem(cartItem);
    }

    private void deleteCartItem(int customerID, int itemID) throws SQLException {
        cartItemDAO.deleteCartItem(customerID, itemID);

    }
}