/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.product;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import dao.ImportItemDAO;
import dao.ProductDAO;
import dao.interfaces.IGeneralProductDAO;
import dao.interfaces.IImportItemDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Account;
import model.ImportItem;
import model.Product;
import model.ProductSupplier;
import model.Supplier;
import utils.LoggingConfig;

/**
 *
 * @author anhkc
 */
@WebServlet(name = "QueueImportController", urlPatterns = {"/queueImport"})
public class QueueImportController extends HttpServlet {

    private static final Logger LOGGER = LoggingConfig.getLogger(QueueImportController.class);
    private final IImportItemDAO importItemDAO = ImportItemDAO.getInstance();

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
            List<ProductSupplier> productSupplyList = importItemDAO.getAllProductSupplies();

            if (productSupplyList.isEmpty()) {
                request.setAttribute("errorMessage", "Failed to retrieve sufficient information for queueing imports!");
            } else {
                String json = new Gson().toJson(productSupplyList);
                request.setAttribute("productSupplyList", productSupplyList);
                request.setAttribute("productSupplyListJSON", json);
                request.setAttribute("formAction", "queue");

            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An error occurred while fetching products and suppliers for queueing imports", e);
            request.setAttribute("errorMessage", "An error occurred while fetching products and suppliers for queueing imports: " + e.getMessage());
        }
        request.getRequestDispatcher("productInventoryManagement.jsp").forward(request, response);
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
        String supplyData = request.getParameter("importData");
        try {
            if (supplyData == null) {
                throw new Exception("Cannot retrieve supply data from form submission!");
            }
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            List<ImportItem> items = new ArrayList<>();
            Map<String, ImportItem> importMap = objectMapper.readValue(supplyData,
                    new TypeReference<Map<String, ImportItem>>() {
            });
            items.addAll(importMap.values());
            if (importItemDAO.queueImports(items)) {
                LOGGER.log(Level.INFO, "New imports has been queued successfully!");
                request.setAttribute("message", "New import has been queued successfully!");
            } else {
                LOGGER.log(Level.SEVERE, "Failed to queue imports!");
                request.setAttribute("errorMessage", "Failed to queue imports!");
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            // Retrieve and log suppressed exceptions
            Throwable[] suppressed = e.getSuppressed();
            for (Throwable sup : suppressed) {
                LOGGER.log(Level.SEVERE, "Suppressed Exception: " + sup.toString(), sup);
            }
            request.setAttribute("errorMessage", "Failed to queue imports due to:<br>" + e.toString());

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
