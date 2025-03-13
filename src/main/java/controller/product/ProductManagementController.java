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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Account;
import model.Product;

/**
 *
 * @author anhkc
 */
@WebServlet({"/manageProductList", "/manageProductDetails", "/updateProduct", "/addProduct", "/deleteProduct"})
public class ProductManagementController extends HttpServlet {
    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        super.init(); 
        productDAO = new ProductDAO();
    }
    

    /**
     * Processes requests for both HTTP GET and POST methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String path = request.getServletPath();

        switch (path) {
            case "/manageProductList":
                manageList(request, response);
                break;
            case "/manageProductDetails":
                manageDetails(request, response);
                break;
            case "/updateProduct":
                manageUpdate(request, response);
                break;
            case "/addProduct":
                manageAdd(request, response);
                break;
            case "/deleteProduct":
                manageDelete(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid product management url: " + path);
                break;
        }
    }

    private void manageList(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        //Three items per page
        int pageSize = 5;
        String pageStr = request.getParameter("page");
        String query = request.getParameter("query");
        String type = request.getParameter("type");
        //Default page is 1
        int page = pageStr != null ? Integer.parseInt(pageStr) : 1;
        
        //Prevent unauthorized access
        HttpSession session = request.getSession();
        Account currentAccount = (Account)(session.getAttribute("account"));
        if (currentAccount == null || (!currentAccount.getRole().equals("admin") && !currentAccount.getRole().equals("staff"))) {
            response.sendRedirect(request.getContextPath() + "login.jsp");
            return;
        }

        try {
            // Get all products
            List<Product> productList = productDAO.getAllProducts(query,type, query != null && !query.trim().isEmpty() ? "relevance" : "", page, pageSize);

            // Calculate total pages
            //Default value is 1
            int totalProducts = productDAO.getProductsCount(query,type);
            int totalPages = totalProducts > 0 ? (int) Math.ceil((double) totalProducts / pageSize) : 1;

            
            request.setAttribute("productList", productList);
            request.setAttribute("type", type);
            request.setAttribute("query", query);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages); 
            request.setAttribute("pageSize", pageSize); 
            request.setAttribute("totalProducts", totalProducts);

            
        } catch (Exception ex) {
            Logger.getLogger(ProductManagementController.class.getName()).log(Level.SEVERE, "Error: " + ex.getMessage(), ex);
            request.setAttribute("errorMessage", "An error occurred while fetching products: " + ex.getMessage());
        }
        request.getRequestDispatcher("productCatalogManagement.jsp").forward(request, response);
    }

    private void manageDetails(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head><title>Product Details</title></head>");
            out.println("<body>");
            out.println("<h1>Product Details</h1>");
            out.println("<p>Showing details for a specific product...</p>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private void manageUpdate(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head><title>Update Product</title></head>");
            out.println("<body>");
            out.println("<h1>Update Product</h1>");
            out.println("<p>Updating a product...</p>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private void manageAdd(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head><title>Add Product</title></head>");
            out.println("<body>");
            out.println("<h1>Add Product</h1>");
            out.println("<p>Adding a new product...</p>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private void manageDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head><title>Delete Product</title></head>");
            out.println("<body>");
            out.println("<h1>Delete Product</h1>");
            out.println("<p>Deleting a product...</p>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Manages product-related operations";
    }// </editor-fold>
}