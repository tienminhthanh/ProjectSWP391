/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.product;

import service.factory.ProductDetailsFactory;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Account;
import model.Customer;
import model.OrderProduct;
import model.product_related.Product;
import utils.Utility;

/**
 *
 * @author anhkc
 */
@WebServlet({
    "/productDetails"
})
public class ViewProductDetailsController extends HttpServlet {

    private static final boolean IS_MANGEMENT = false;
    private static final String HASH_SALT = "Sugar";

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.service(request, response); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(false);
        Account account = session != null ? (Account) session.getAttribute("account") : null;
        String role = account != null ? account.getRole() : "guest";
        switch (role.toLowerCase()) {
            case "admin":
                response.sendRedirect("listAccount");
                return;
            case "staff":
                response.sendRedirect("OrderListForStaffController");
                return;
            case "shipper":
                response.sendRedirect("OrderListForShipperController");
                return;
            case "customer":
            case "guest":
            default:
                break;
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

            Product requestedProduct = ProductDetailsFactory.getProduct(type, id, IS_MANGEMENT);
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

                //Construct breadCrumb
                String breadCrumb
                        = String.format("<a href='home'>Home</a> > <a href='search?type=%s'>%s</a> > <a href='category?id=%s'>%s</a> > <a href='productDetails?id=%s&type=%s'>%s</a>",
                                requestedProduct.getGeneralCategory(),
                                Utility.getDisplayTextBasedOnType(requestedProduct.getGeneralCategory()),
                                requestedProduct.getSpecificCategory().getCategoryID(),
                                requestedProduct.getSpecificCategory().getCategoryName(),
                                id,
                                requestedProduct.getGeneralCategory(),
                                requestedProduct.getProductName());
                request.setAttribute("breadCrumb", breadCrumb);
                request.setAttribute("product", requestedProduct);
                request.setAttribute("type", type);
                request.setAttribute("currentURL", currentURL);
                request.setAttribute("formattedURL", Utility.formatURL(currentURL));

            }
            request.getRequestDispatcher("productDetails.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", e.toString());
            request.getRequestDispatcher("error.jsp").forward(request, response);
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
