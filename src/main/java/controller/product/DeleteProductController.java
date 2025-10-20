/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.product;

import dao.ProductDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Account;
import utils.LoggingConfig;

/**
 *
 * @author anhkc
 */
@WebServlet(name = "DeleteProductController", urlPatterns = {"/deleteProduct"})
public class DeleteProductController extends HttpServlet {

    private static final Logger LOGGER = LoggingConfig.getLogger(DeleteProductController.class);
    private final ProductDAO productDAO = ProductDAO.getInstance();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.service(request, response);
        response.setContentType("text/html;charset=UTF-8");
        String action = request.getParameter("action");

        HttpSession session = request.getSession();
        // Nếu action=clearMessage, xóa session và trả về response
        if ("clearMessage".equals(action)) {
            session.removeAttribute("message");
            session.removeAttribute("messageType");
        }
        //Prevent unauthorized access  - ADMIN
        Account currentAccount = (Account) (session.getAttribute("account"));
        if (currentAccount == null || !currentAccount.getRole().equals("admin")) {
            response.sendRedirect("login.jsp");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String productID = request.getParameter("id");
        String action = request.getParameter("action");

        try {
            int id = Integer.parseInt(productID);
            //Handle null
            if (action == null) {
                throw new Exception("Cannot identify action!");
            }

            //Resolve target status by action
            //true = active, false = inactive
            String statusString = action.equalsIgnoreCase("activate") ? "true" : action.equalsIgnoreCase("deactivate") ? "false" : "";

            //Execute activation or deactivation
            boolean transactionState = productDAO.changeProductStatus(id, Boolean.parseBoolean(statusString));

            //Send meaningful messages to view based on transactionState
            // true = success, false = failed
            String message = transactionState ? "The product has been " + action + "d" + " successfully"
                    : "Failed to " + action + " the product!";
            request.setAttribute(transactionState ? "successfulMessage" : "failedMessage", message);

            request.getRequestDispatcher("manageProductList").forward(request, response);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error: " + e.getMessage(), e);
            request.setAttribute("errorMessage", "An error occurred while changing product status: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
