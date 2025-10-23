/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import dao.ProductDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Account;
import model.ImportItem;
import model.Product;
import model.Supplier;
import utils.LoggingConfig;

/**
 *
 * @author anhkc
 */
@WebServlet(name = "ImportProductController", urlPatterns = {"/importProduct"})
public class ImportProductController extends HttpServlet {
    private static final Logger LOGGER = LoggingConfig.getLogger(ImportProductController.class);
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
        //Retrieve import info from database based on id 
        String productID = request.getParameter("id");
        try {
            int id = Integer.parseInt(productID);

            Map<Supplier, List<ImportItem>> importMap = productDAO.getPendingImportMapByProductID(id);
            if (importMap.isEmpty()) {
                request.setAttribute("errorMessage", "No pending import found for this product (ID:" + id + ").");
            } else {
                String json = new Gson().toJson(importMap);
                request.setAttribute("importMap", importMap);
                request.setAttribute("jsonMap", json);
                //Set the formAction to ensure the page show the correct form
                request.setAttribute("formAction", "import");
            }
            request.getRequestDispatcher("productInventoryManagement.jsp").forward(request, response);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An error occurred while fetching import info of the product: {0}", e.toString());
            request.setAttribute("errorMessage", "An error occurred while fetching import info of the product: " + e.getMessage());
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

            // Get the encoded JSON string from request parameters
            String[] encodedJsons = request.getParameterValues("importItem");
            int productID = Integer.parseInt(request.getParameter("productID"));
            if (encodedJsons == null) {
                throw new Exception("Cannot retrieve import data from form submission!");
            }

            List<ImportItem> items = new ArrayList<>();
            for (String encodedJson : encodedJsons) {
                // Decode the URL-encoded JSON
                String decodedJson = URLDecoder.decode(encodedJson, StandardCharsets.UTF_8);
                // Parse JSON into Java object
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                ImportItem importItem = objectMapper.readValue(decodedJson, ImportItem.class);
                if (importItem != null) {
                    items.add(importItem);
                }
            }

            LocalDate rlsDate = items.get(0).getProduct().getReleaseDate();
            rlsDate = rlsDate != null ? rlsDate : LocalDate.parse("1970-01-01");
            if (rlsDate.toString().equals("1970-01-01")) {
                Product product = productDAO.getProductById(productID);
                rlsDate = product != null ? product.getReleaseDate() : rlsDate;
            }

            for (ImportItem item : items) {
                item.getProduct().setReleaseDate(rlsDate);
            }

            if (productDAO.importProducts(items)) {
                LOGGER.log(Level.INFO, "Products has been imported to inventory successfully! (ID:{0})", productID);
                request.setAttribute("message", "The product has been imported to inventory successfully! (ID:" + productID + ")");
            } else {
                LOGGER.log(Level.SEVERE, "Failed to import products!");
                request.setAttribute("errorMessage", "Failed to import the product!");
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            // Retrieve and log suppressed exceptions
            Throwable[] suppressed = e.getSuppressed();
            for (Throwable sup : suppressed) {
                LOGGER.log(Level.SEVERE, "Suppressed Exception: " + sup.toString(), sup);
            }
            request.setAttribute("errorMessage", "Failed to import the product due to:<br>" + e.toString());

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
