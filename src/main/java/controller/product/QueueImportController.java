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
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Account;
import model.product_related.ImportItem;
import model.product_related.Product;
import model.product_related.Supplier;
import utils.LoggingConfig;

/**
 *
 * @author anhkc
 */
@WebServlet(name = "QueueImportController", urlPatterns = {"/queueImport"})
public class QueueImportController extends HttpServlet {

    private static final Logger LOGGER = LoggingConfig.getLogger(QueueImportController.class);
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
        try {
            List<Product> productList = productDAO.getAllProductsForQueueing();
            List<Supplier> supplierList = productDAO.getAllSuppliers();

            if (productList.isEmpty() || supplierList.isEmpty()) {
                request.setAttribute("errorMessage", "Failed to retrieve sufficient information for queueing imports!");
            } else {
                request.setAttribute("productList", productList);
                request.setAttribute("supplierList", supplierList);
                request.setAttribute("formAction", "queue");

            }
            request.getRequestDispatcher("productInventoryManagement.jsp").forward(request, response);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An error occurred while fetching products and suppliers for queueing imports", e);
            request.setAttribute("errorMessage", "An error occurred while fetching products and suppliers for queueing imports: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int productID = Integer.parseInt(request.getParameter("product"));
            int supplierID = Integer.parseInt(request.getParameter("supplier"));
            double price = Double.parseDouble(request.getParameter("price"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            ImportItem queuedItem = new ImportItem()
                    .setProduct(new Product().setProductID(productID))
                    .setSupplier(new Supplier().setSupplierID(supplierID))
                    .setImportDate(LocalDate.now())
                    .setImportPrice(price * 1000)
                    .setImportQuantity(quantity)
                    .setIsImported(false);

            if (productDAO.queueImport(queuedItem)) {
                LOGGER.log(Level.INFO, "A new import has been queued successfully! (productID:{0} - supplierID:{1})",
                        new Object[]{productID, supplierID});
                request.setAttribute("message", "A new import has been queued successfully! (productID:" + productID + " - supplierID:" + supplierID + ")");
            } else {
                LOGGER.log(Level.SEVERE, "Failed to queue the import!");
                request.setAttribute("errorMessage", "Failed to queue the import!");
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            // Retrieve and log suppressed exceptions
            Throwable[] suppressed = e.getSuppressed();
            for (Throwable sup : suppressed) {
                LOGGER.log(Level.SEVERE, "Suppressed Exception: " + sup.toString(), sup);
            }
            request.setAttribute("errorMessage", "Failed to queue the import due to:<br>" + e.toString());

        }
        request.getRequestDispatcher("productInventoryManagement.jsp").forward(request, response);

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
