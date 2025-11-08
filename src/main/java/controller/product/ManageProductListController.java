/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.product;

import dao.ProductDAO;
import dao.interfaces.IGeneralProductDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.LoggingConfig;
import model.Account;
import model.Product;

/**
 *
 * @author anhkc
 */
@WebServlet(name = "ManageProductListController", urlPatterns = {"/manageProductList"})
public class ManageProductListController extends HttpServlet {
    private final IGeneralProductDAO productDAO = ProductDAO.getInstance();
    private static final Logger LOGGER = LoggingConfig.getLogger(ManageProductListController.class);

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.service(request, response); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        response.setContentType("text/html;charset=UTF-8");
        String action = request.getParameter("action");
        HttpSession session = request.getSession();

        // Nếu action=clearMessage, xóa session và trả về response
        if ("clearMessage".equals(action)) {
            session.removeAttribute("message");
            session.removeAttribute("messageType");
        }

        //Prevent unauthorized access - STAFF, AD
        Account currentAccount = (Account) (session.getAttribute("account"));
        boolean isManagement = currentAccount != null
                ? currentAccount.getRole().equals("admin") || currentAccount.getRole().equals("staff")
                : false;
        if (!isManagement) {
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
        //Five items per page
        int pageSize = 5;
        String pageStr = request.getParameter("page");
        String query = request.getParameter("query");
        String type = request.getParameter("type");
        //Default page is 1
        int page = pageStr != null ? Integer.parseInt(pageStr) : 1;

        try {
            // Get all products
            List<Product> productList = productDAO.getAllProducts(query, type, query != null && !query.trim().isEmpty() ? "relevance" : "", page, pageSize);

            // Calculate total pages
            //Default value is 1
            int totalProducts = productDAO.countProductListForManagement(query, type);
            int totalPages = totalProducts > 0 ? (int) Math.ceil((double) totalProducts / pageSize) : 1;

            LOGGER.log(Level.INFO, "Retrieved {0} - {1} of total {2} products!",
                    new Object[]{
                        (page - 1) * pageSize + 1,
                        (page * pageSize) > totalProducts ? totalProducts : page * pageSize,
                        totalProducts
                    });

            request.setAttribute("productList", productList);
            request.setAttribute("type", type);
            request.setAttribute("query", query);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("totalProducts", totalProducts);

            request.getRequestDispatcher("productCatalogManagement.jsp").forward(request, response);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error: " + e.getMessage(), e);
            request.setAttribute("errorMessage", "An error occurred while fetching products: " + e.getMessage());
            request.getRequestDispatcher("productCatalogManagement.jsp").forward(request, response);
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
