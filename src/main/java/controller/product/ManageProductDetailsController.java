/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.product;

import dao.EventDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Account;
import model.Customer;
import model.Event;
import model.OrderProduct;
import model.Product;
import dao.factory_product.ProductFactory;
import utils.LoggingConfig;
import utils.Utility;

/**
 *
 * @author anhkc
 */
@WebServlet(name = "ManageProductDetailsController", urlPatterns = {"/manageProductDetails"})
public class ManageProductDetailsController extends HttpServlet {

    private static final boolean IS_MANGEMENT = true;
    private static final String HASH_SALT = "Sugar";
    private static final Logger LOGGER = LoggingConfig.getLogger(ManageProductDetailsController.class);
    private final EventDAO eDAO = EventDAO.getInstance();

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
        String productID = request.getParameter("id");
        String currentURL = request.getRequestURL().toString() + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
        String type = request.getParameter("type");

        try {
            int id = Integer.parseInt(productID);

            Product requestedProduct = ProductFactory.getProduct(type, id, IS_MANGEMENT);
            if (requestedProduct == null) {
                request.setAttribute("message", "The product is not available right now!");
            } else {
                //Handle linebreak for html display
                requestedProduct.setDescription(requestedProduct.getDescription().replaceAll("\r\n|\r|\n", "<br>"));

                //Sort creators
                Collections.sort(requestedProduct.getCreatorList(), Comparator.comparing(c -> c.getCreatorName()));

                //Get comments & ratings
                Map<String, String[]> reviewMap = getReviewMap(requestedProduct.getOrderProductList(), id);

                if (!reviewMap.isEmpty()) {
                    request.setAttribute("reviewMap", reviewMap);
                }

                //Get event list
                List<Event> eventList = eDAO.getListActiveEvents();
                request.setAttribute("eventList", eventList);

                Event event = eDAO.getEventByProductID(id);
                request.setAttribute("event", event);

                boolean isSoldOrPreOrder = "upcoming".equals(requestedProduct.getSpecialFilter()) || requestedProduct.getStockCount() == 0;
                request.setAttribute("isSoldOrPreOrder", isSoldOrPreOrder);

                //Check if product is currently featured in an event
                request.setAttribute("productEventStatus", requestedProduct.getEventEndDate() == null || LocalDate.now().isAfter(requestedProduct.getEventEndDate())
                        ? "notInEvent"
                        : "inEvent");
                request.setAttribute("product", requestedProduct);
                request.setAttribute("type", type);
                request.setAttribute("currentURL", currentURL);

                request.getRequestDispatcher("productDetailsManagement.jsp").forward(request, response);
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error: " + e.getMessage(), e);
            request.setAttribute("message", "An error occurred while fetching the product: " + e.getMessage());
            request.getRequestDispatcher("productCatalogManagement.jsp").forward(request, response);
        }
    }
    
     private Map<String, String[]> getReviewMap(List<OrderProduct> orderProductList, int productID) {
        Map<String, String[]> reviewMap = new HashMap<>();
        for (OrderProduct item : orderProductList) {
            Customer customer = item.getOrderInfo().getCustomer();

            // --- 1. Create the Hashed Key ---
            String dataToHash = item.getOrderID() + productID + HASH_SALT;

            // 🔑 Use the new function here:
            String key = Utility.generateSHA256Hash(dataToHash);

            // Example Value Construction:
            String displayCustomerName = Utility.maskName(customer.getLastName() + " " + customer.getFirstName());

            reviewMap.put(key,
                    new String[]{
                        String.valueOf(item.getRating()),
                        item.getComment(),
                        displayCustomerName
                    });

        }
        return reviewMap;
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
