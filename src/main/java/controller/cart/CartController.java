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
            String customerIdStr = request.getParameter("customerID");
            String productIdStr = request.getParameter("productID");
            String quantityStr = request.getParameter("quantity");
            String priceStr = request.getParameter("priceWithQuantity");
            String currentURL = request.getParameter("currentURL");

            int customerID = Integer.parseInt(customerIdStr);

            if (session.getAttribute("account") == null) {
                throw new Exception("Please login to add items to cart!");
            }

            if ("add".equals(action)) {
                int productID = Integer.parseInt(productIdStr);
                int cartItemQuantity = Integer.parseInt(quantityStr);
                BigDecimal cartItemPrice = new BigDecimal(priceStr);
                Product product = productDAO.getProductById(productID);
                if (product == null) {
                    throw new Exception("Product not found!");
                }
                addToCart(request, customerID, productID, cartItemQuantity, cartItemPrice, product);

                if (currentURL == null || currentURL.trim().isEmpty()) {
                    currentURL = "home";
                }
                response.sendRedirect(currentURL);
                return;

            } else if ("update".equals(action)) {
                int itemID = Integer.parseInt(request.getParameter("itemID"));
                int productID = Integer.parseInt(productIdStr);
                int cartItemQuantity = Integer.parseInt(quantityStr);
                BigDecimal cartItemPrice = new BigDecimal(priceStr);
                if (customerIdStr == null || productIdStr == null || quantityStr == null || priceStr == null) {
                    throw new Exception("Missing required parameters!");
                }
                Product product = productDAO.getProductById(productID);
                if (product == null) {
                    throw new Exception("Product not found!");
                }
                CartItem cartItem = new CartItem(itemID, customerID, productID, cartItemQuantity, cartItemPrice);
                updateCartItem(cartItem, product);

            } else if ("delete".equals(action)) {
                int itemID = Integer.parseInt(request.getParameter("itemID"));
                deleteCartItem(customerID, itemID);
                List<CartItem> cartItems = cartItemDAO.getCartItemsByCustomer(customerID);
                session.setAttribute("cartItems", cartItems);
            }
            if (currentURL == null || currentURL.trim().isEmpty()) {
                currentURL = "cart?customerID=" + request.getParameter("customerID");
            }
            response.sendRedirect(currentURL);
            return;

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void addToCart(HttpServletRequest request, int customerID, int productID, int cartItemQuantity,
            BigDecimal cartItemPrice, Product product) throws SQLException, Exception {
        HttpSession session = request.getSession();

        if (product == null) {
            throw new Exception("Product not found!");
        }

        if (cartItemQuantity > product.getStockCount()) {
            throw new Exception("Not enough stock for this product! Available: " + product.getStockCount());
        }

        List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");
        if (cartItems == null) {
            cartItems = cartItemDAO.getCartItemsByCustomer(customerID);
            session.setAttribute("cartItems", cartItems);
        }

        CartItem existingCartItem = cartItemDAO.getCartItemByCustomerAndProduct(customerID, productID);
        if (existingCartItem != null) {
            int newQuantity = existingCartItem.getCartItemQuantity() + cartItemQuantity;
            if (newQuantity > product.getStockCount()) {
                throw new Exception("Total quantity exceeds available stock! Available: " + product.getStockCount());
            }
            CartItem updatedCartItem = new CartItem(existingCartItem.getItemID(),
                    customerID,
                    productID,
                    newQuantity,
                    cartItemPrice
            );
            cartItemDAO.updateCartItem(updatedCartItem);

            for (int i = 0; i < cartItems.size(); i++) {
                if (cartItems.get(i).getItemID() == existingCartItem.getItemID()) {
                    cartItems.set(i, updatedCartItem);
                    break;
                }
            }
        } else {
            CartItem cartItem = new CartItem(customerID, productID, cartItemQuantity, cartItemPrice);
            cartItemDAO.addCartItem(cartItem);
            cartItems.add(cartItem);
        }

        session.setAttribute("cartItems", cartItems);
    }

    private void updateCartItem(CartItem cartItem, Product product) throws SQLException, Exception {
        if (cartItem.getCartItemQuantity() > product.getStockCount()) {
            throw new Exception("Updated quantity exceeds available stock! Available: " + product.getStockCount());
        }
        cartItemDAO.updateCartItem(cartItem);
    }

    private void deleteCartItem(int customerID, int itemID) throws SQLException {
        cartItemDAO.deleteCartItem(customerID, itemID);
    }
}