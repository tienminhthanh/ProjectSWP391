/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.product;

import dao.ProductDAO;
import dao.interfaces.IGeneralProductDAO;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map;
import model.Account;
import model.product_related.NonEntityClassification;
import model.product_related.Product;
import utils.Utility;
import model.interfaces.IProductClassification;

/**
 *
 * @author anhkc
 */
@WebServlet({
    "/category", "/genre", "/publisher", "/creator",
    "/series", "/brand", "/character", "/new", "/sale"
})
public class ViewProductListController extends HttpServlet {
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
        String path = request.getServletPath();
        String pageStr = request.getParameter("page");
        String classificationId = request.getParameter("id");
        String clsfType = request.getParameter("type");
        String sortCriteria = request.getParameter("sortCriteria");
        Map<String, String[]> paramMap = request.getParameterMap();
        
        //For redirect back to original page after logging in or adding items to cart
        String currentURL = request.getRequestURL().toString() + (request.getQueryString() != null ? "?" + request.getQueryString() : "");

        try {
            //Default page is 1
            int page = pageStr != null ? Integer.parseInt(pageStr) : 1;

            //Parse id string to integer if path is neither /new nor /sale
            int id = classificationId != null ? Integer.parseInt(classificationId) : 0;

            //Extract pathname as clsfCode
            String clsfCode = path.substring(1);

            //Get the correct classification instance
            IProductClassification classification = getClassficationAttributes(getServletContext(), clsfCode, clsfType, id);

            //Get type if null
            if (clsfType == null) {
                clsfType = classification.getType();
            }
            
            //Get breadCrumb and pageTitle
            String breadCrumb = buildBreadCrumb(classification, clsfCode);
            String pageTitle = buildPageTitle(classification);

            //Handling filters
            SimpleEntry<StringBuilder, Map<String, String>> filterMapEntry = Utility.getFilterMapAndMessage(paramMap, clsfType);
            StringBuilder message = filterMapEntry.getKey();
            Map<String, String> filterMap = filterMapEntry.getValue();

            // Get initial sort order on first page load
            if (sortCriteria == null) {
                sortCriteria = Utility.getDefaultSortCriteria(null);
            }

            //Get product list
            List<Product> productList = productDAO.getClassifiedProductList(classification, sortCriteria, filterMap, page, PAGE_SIZE, false);

            // Calculate total pages
            //Default value is 1
            int totalProducts = 0;
            int totalPages = 1;

            if (productList.isEmpty()) {
                //No result found
                message.setLength(0);
                message.append("No result found! Please deselect some filter if any.");
            } else {
                totalProducts = productDAO.countClassifiedProductList(classification, filterMap);
                totalPages = totalProducts > 0 ? (int) Math.ceil((double) totalProducts / PAGE_SIZE) : 1;
                request.setAttribute("productList", productList);
                //For displaying current sort criteria
                request.setAttribute("sortCriteria", sortCriteria);
            }

            //Set up remaining attributes and forward the request
            if (message.length() > 0) {
                request.setAttribute("message", message);
            }
            request.setAttribute("type", clsfType);
            request.setAttribute("currentURL", currentURL);
            request.setAttribute("formattedURL", Utility.formatURL(currentURL));
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("pageSize", PAGE_SIZE);
            request.setAttribute("totalProducts", totalProducts);
            request.setAttribute("pageTitle", pageTitle);
            request.setAttribute("breadCrumb", breadCrumb);

            request.getRequestDispatcher("productCatalog.jsp").forward(request, response);

        } catch (Exception e) {
            System.out.println(e.toString());
            request.setAttribute("errorMessage", e.toString());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
    
    
    private IProductClassification getClassficationAttributes(ServletContext context, String clsfCode, String clsfType, int clsfID) {

        //For Non-EntityClassification like Sale, New
        if (clsfID == 0 && clsfType != null) {
            return getNonEntityClassification(clsfCode, clsfType);
        }

        //Resolve attribute name in servlet context
        String attrName = Utility.resolveServletContextAtributeName(clsfCode);

        //Get the attribute from servlet context and cast to its original form
        Map<IProductClassification, Integer> entityMap = (Map<IProductClassification, Integer>) context.getAttribute(attrName);
        if (entityMap == null) {
            throw new IllegalStateException("No map found in context for " + attrName);
        }

        // Get the correct classification based on id
        for (IProductClassification entity : entityMap.keySet()) {
            if (entity.getId() == clsfID) {
                return entity;
            }
        }
        return null;
    }

    private NonEntityClassification getNonEntityClassification(String clsfCode, String clsfType) {
        String name;
        switch ((clsfCode != null ? clsfCode : "")) {
            case "new":
                name = "New Release";
                break;
            case "sale":
                name = "On Sale";
                break;
            default:
                name = "";
        }
        return new NonEntityClassification(name, clsfType,clsfCode);

    }


    private String buildBreadCrumb(IProductClassification clsf, String clsfCode) {
        int clsfId = clsf.getId();
        String clsfName = clsf.getName();
        String clsfType = clsf.getType();
        Map<String, Object> extraAttrs = clsf.getExtraAttributes();

        StringBuilder breadCrumb = new StringBuilder("<a href='home'>Home</a>");
        if (extraAttrs.containsKey("isNonEntity")) {
            breadCrumb.append(String.format(" > <a href='search?type=%s'>%s</a>", clsfType, Utility.getDisplayTextBasedOnType(clsfType)));
            breadCrumb.append(String.format(" > <a href='%s?type=%s'>%s</a>", clsfCode, clsfType, clsfName));
        } else {
            StringBuilder displayText = new StringBuilder(clsfName);
            displayText = extraAttrs.containsKey("creatorRole")
                    ? displayText.append(" - ").append(extraAttrs.get("creatorRole"))
                    : displayText;
            breadCrumb.append(String.format(" > <a href='%s?id=%s'>%s</a>", clsfCode, clsfId, displayText));
        }
        return breadCrumb.toString();
    }

    private String buildPageTitle(IProductClassification clsf) {
        String clsfName = clsf.getName();
        String clsfType = clsf.getType();
        Map<String, Object> extraAttrs = clsf.getExtraAttributes();

        StringBuilder pageTitle = new StringBuilder(clsfName);
        if (extraAttrs.containsKey("isNonEntity")) {
            pageTitle.append(" - ").append(Utility.getDisplayTextBasedOnType(clsfType));
        } else {
            pageTitle = extraAttrs.containsKey("creatorRole")
                    ? pageTitle.append(" - ").append(extraAttrs.get("creatorRole"))
                    : pageTitle;
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
