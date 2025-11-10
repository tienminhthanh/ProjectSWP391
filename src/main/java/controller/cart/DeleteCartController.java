/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.cart;

import dao.CartItemDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import model.CartItem;

/**
 *
 * @author ADMIN
 */
@WebServlet("/deleteCart")
public class DeleteCartController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final CartItemDAO cartItemDAO = CartItemDAO.getInstance();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        try {
            // Check required parameters
            String customerIdStr = request.getParameter("customerID");
            int customerID = Integer.parseInt(customerIdStr);
            int itemID = Integer.parseInt(request.getParameter("itemID"));
            String currentURL = request.getParameter("currentURL");

            // Validate session
            if (session.getAttribute("account") == null) {
                response.sendRedirect("login");
                return;
            }

            deleteCartItem(customerID, itemID);
            List<CartItem> cartItems = cartItemDAO.getCartItemsByCustomer(customerID); // Lấy lại danh sách mới
            session.setAttribute("cartItems", cartItems); // Cập nhật session

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

    private void deleteCartItem(int customerID, int itemID) throws SQLException {
        cartItemDAO.deleteCartItem(customerID, itemID);
    }
}