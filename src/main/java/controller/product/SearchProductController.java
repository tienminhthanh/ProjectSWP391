/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.product;

import dao.ProductDAO;
import dao.interfaces.IGeneralProductDAO;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.AbstractMap.SimpleEntry;
import model.Account;
import model.product_related.Product;
import utils.Utility;

/**
 *
 * @author anhkc
 */
@WebServlet({"/search"})
public class SearchProductController extends HttpServlet {

    private static final int PAGE_SIZE = 12;
    private final IGeneralProductDAO productDAO = ProductDAO.getInstance();

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
        String type = request.getParameter("type");
        String query = request.getParameter("query");
        String pageStr = request.getParameter("page");

        String sortCriteria = request.getParameter("sortCriteria");
        Map<String, String[]> paramMap = request.getParameterMap();
        //For redirect back to original page after logging in or adding items to cart
        String currentURL = request.getRequestURL().toString() + (request.getQueryString() != null ? "?" + request.getQueryString() : "");

        try {
            //Parse page number
            int page = pageStr != null ? Integer.parseInt(pageStr) : 1;

            //Handling filters
            SimpleEntry<StringBuilder, Map<String, String>> filterMapEntry = Utility.getFilterMapAndMessage(paramMap, type);
            StringBuilder message = filterMapEntry.getKey();
            Map<String, String> filterMap = filterMapEntry.getValue();

            // Get initial sort order on first page load
            if (sortCriteria == null) {
                sortCriteria = Utility.getDefaultSortCriteria(query);
            }

            // Set up breadCrumb and page Title
            String breadCrumb = buildBreadCrumb(query, type);
            String pageTitle = buildPageTitle(query, type);
            
            // Get product list
            List<Product> productList = productDAO.getSearchResult(query, type, sortCriteria, filterMap, page, PAGE_SIZE);

            // Calculate total pages
            //Default value is 1
            int totalProducts = 0;
            int totalPages = 1;

            // Set up the product list
            if (productList.isEmpty()) {
                // No result found
                message.setLength(0);
                message.append(Utility.getNoResultMessage(type));

                //If filter are selected
                if (!filterMap.isEmpty()) {
                    message.append("Or deselect some filter if any!\n");
                }
            } else {
                totalProducts = productDAO.countSearchResult(query, type, filterMap);
                totalPages = totalProducts > 0 ? (int) Math.ceil((double) totalProducts / PAGE_SIZE) : 1;
                request.setAttribute("productList", productList);
                // For displaying current sort criteria
                request.setAttribute("sortCriteria", sortCriteria);
            }
            
            //Set query as request attribute if any
            if (query != null && !query.trim().isEmpty()) {
                request.setAttribute("query", query);
            }

            //Set up remaining attributes and forward the request
            if (message.length() > 0) {
                request.setAttribute("message", message);
            }
            request.setAttribute("currentURL", currentURL);
            request.setAttribute("formattedURL", Utility.formatURL(currentURL));
            request.setAttribute("type", type);
            request.setAttribute("breadCrumb", breadCrumb);
            request.setAttribute("pageTitle", pageTitle);

            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("pageSize", PAGE_SIZE);
            request.setAttribute("totalProducts", totalProducts);

            request.getRequestDispatcher("productCatalog.jsp").forward(request, response);

        } catch (Exception e) {
            System.out.println(e.toString());
            request.setAttribute("message", "Search is not available at the moment!");
            request.getRequestDispatcher("home").forward(request, response);
        }
    }
    
    private String buildBreadCrumb(String query, String type) {
        StringBuilder breadCrumb = new StringBuilder("<a href='home'>Home</a>");
        StringBuilder displayText = new StringBuilder();
        if (query == null || query.trim().isEmpty()) {
            displayText.append(Utility.getDisplayTextBasedOnType(type));
            breadCrumb.append(String.format(" > <a href='search?type=%s'>%s</a>", type, displayText));

        } else {
            displayText.append("Search Result: ").append(query);
            breadCrumb.append(String.format(" > <a href='search?type=%s&query=%s'>%s</a>", type, query, displayText));
        }
        return breadCrumb.toString();
    }

    private String buildPageTitle(String query, String type) {
        StringBuilder pageTitle = new StringBuilder();
        if (query == null || query.trim().isEmpty()) {
            pageTitle.append(Utility.getDisplayTextBasedOnType(type));

        } else {
            pageTitle.append("Search Result: ").append(query);

        }
        return pageTitle.toString();
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
