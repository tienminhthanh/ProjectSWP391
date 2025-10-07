/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.product;

import model.product_related.Creator;
import model.product_related.Product;
import model.product_related.Genre;
import dao.*;
import jakarta.servlet.ServletContext;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import model.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import model.interfaces.ProductClassification;
import model.product_related.NonEntityClassification;

/**
 *
 * @author anhkc
 */
@WebServlet({
    "/search", "/category", "/genre", "/publisher", "/creator",
    "/series", "/brand", "/character", "/new", "/sale",
    "/home", "/ranking", "/productDetails"
})

public class ProductCatalogController extends HttpServlet {

    private static final Set<String> MERCH_FILTERS = Set.of("ftSrs", "ftBrn", "ftChr");
    private static final Set<String> BOOK_FITLERS = Set.of("ftGnr", "ftPbl");
    private static final Set<String> SINGLE_FILTERS = Set.of("ftCtg", "ftPbl", "ftSrs", "ftBrn", "ftChr");
    private static final int PAGE_SIZE = 12;

    private ProductDAO productDAO;
    private VoucherDAO vDao;
//    private EventDAO eDao;
    private OrderDAO orderDAO;

    @Override
    public void init() throws ServletException {
        vDao = new VoucherDAO();
        productDAO = new ProductDAO();
        orderDAO = new OrderDAO();
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
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

        switch (path) {
            case "/search":
                handleSearch(request, response);
                break;
            case "/category":
            case "/creator":
            case "/genre":
            case "/publisher":
            case "/series":
            case "/brand":
            case "/character":
            case "/new":
            case "/sale":
                handleList(request, response,path);
                break;
            case "/productDetails":
                handleDetails(request, response);
                break;
            case "/ranking":
                handleRanking(request, response);
                break;
            case "/home":
            default:
                handleHomepage(request, response);
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
        processRequest(request, response);
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
        processRequest(request, response);

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

    private void handleSearch(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String type = request.getParameter("type");
        String query = request.getParameter("query");
        String pageStr = request.getParameter("page");

        String sortCriteria = request.getParameter("sortCriteria");
        Map<String, String[]> paramMap = request.getParameterMap();
        //For redirect back to original page after logging in or adding items to cart
        String currentURL = request.getRequestURL().toString() + (request.getQueryString() != null ? "?" + request.getQueryString() : "");

        StringBuilder message = new StringBuilder();
        //Handling filters
        Map<String, String> filterMap = new HashMap<>();
        if (paramMap != null) {
            for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
                String name = entry.getKey();
                String[] values = entry.getValue();

                //Skip non-filter params
                if (!name.startsWith("ft") || filterMap.containsKey(name)) {
                    continue;
                }

                //Prevent SINGLE_FILTERS from being selected multiple times
                if (SINGLE_FILTERS.contains(name) && values[0].split(",").length > 1) {
                    message.append("Only genres and creators can be selected multiple times!\n");
                    continue;
                }

                //Special case for price range filter
                if (name.equals("ftPrc")) {
                    filterMap.put(name, values[0] + "-" + values[1]);
                } else {
                    //Normal case
                    filterMap.put(name, values[0]);
                }
            }
        }

        // Get initial sort order on first page load
        if (sortCriteria == null) {
            sortCriteria = getDefaultSortCriteria(query);
        }

        try {
            //Parse page number
            int page = pageStr != null ? Integer.parseInt(pageStr) : 1;

            // Set up breadCrumb and page Title
            // Get product list
            String breadCrumb = "<a href='home'>Home</a>";
            StringBuilder pageTitle = new StringBuilder();
            List<Product> productList;
            if (query == null || query.trim().isEmpty()) {
                //No keywords entered
                breadCrumb += String.format(" > <a href='search?type=%s'>%s</a>", type, getDisplayTextBasedOnType(type));
                pageTitle.append(getDisplayTextBasedOnType(type));
                productList = productDAO.getActiveProducts(type, sortCriteria, filterMap, page, PAGE_SIZE);

            } else {
                breadCrumb += String.format(" > <a href='search?type=%s&query=%s'>Search Result: %s</a>", type, query, query);
                pageTitle.append("Search Result: ").append(query);
                productList = productDAO.getSearchResult(query, type, sortCriteria, filterMap, page, PAGE_SIZE);
                // Set attribute if there are keywords entered
                request.setAttribute("query", query);
            }

            // Calculate total pages
            //Default value is 1
            int totalProducts = productDAO.getProductsCount(query, type, filterMap);
            int totalPages = totalProducts > 0 ? (int) Math.ceil((double) totalProducts / PAGE_SIZE) : 1;

            // Set up the product list
            if (productList.isEmpty()) {
                // No result found
                message.setLength(0);
                message.append(getNoResultMessage(type));

                //If filter are selected
                if (!filterMap.isEmpty()) {
                    message.append("Or deselect some filter if any!\n");
                }
            } else {
                request.setAttribute("productList", productList);
                // For displaying current sort criteria
                request.setAttribute("sortCriteria", sortCriteria);

            }

            //Set up remaining attributes and forward the request
            if (message.length() > 0) {
                request.setAttribute("message", message);
            }
            request.setAttribute("currentURL", currentURL);
            request.setAttribute("formattedURL", formatURL(currentURL));
            request.setAttribute("type", type);
            request.setAttribute("breadCrumb", breadCrumb);
            request.setAttribute("pageTitle", pageTitle.toString());

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

    private String getDisplayTextBasedOnType(String type) {
        switch (type) {
            case "book":
                return "Books";
            case "merch":
                return "Merchandise";
            default:
                return "";
        }
    }

    private String getDefaultSortCriteria(String query) {
        if (query == null || query.trim().isEmpty()) {
            return "releaseDate";
        } else {
            return "relevance";
        }

    }

    private String getNoResultMessage(String type) {
        String message = "";
        if (type.equals("book")) {
            message = "No result found! Try entering series title, name of author/artist, categories or genre.\n";
        } else if (type.equals("merch")) {
            message = "No result found! Try entering series title, name of sculptor/artist/character/brand or category.\n";
        }
        return message;
    }

    private void handleList(HttpServletRequest request, HttpServletResponse response, String path)
            throws ServletException, IOException {
        String pageStr = request.getParameter("page");
        String classificationId = request.getParameter("id");
        String clsfType = request.getParameter("type");
        String sortCriteria = request.getParameter("sortCriteria");
        Map<String, String[]> paramMap = request.getParameterMap();
        //For redirect back to original page after logging in or adding items to cart
        String currentURL = request.getRequestURL().toString() + (request.getQueryString() != null ? "?" + request.getQueryString() : "");

        StringBuilder message = new StringBuilder();

        //Handling filters
        Map<String, String> filterMap = new HashMap<>();
        if (paramMap != null) {
            for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
                String name = entry.getKey();
                String[] values = entry.getValue();

                //Skip non-filter params
                if (!name.startsWith("ft") || filterMap.containsKey(name)) {
                    continue;
                }

                //Prevent SINGLE_FILTERS from being selected multiple times
                if (SINGLE_FILTERS.contains(name) && values[0].split(",").length > 1) {
                    message.append("Only genres and creators can be selected multiple times!\n");
                    continue;
                }

                //Special case for price range filter
                if (name.equals("ftPrc")) {
                    filterMap.put(name, values[0] + "-" + values[1]);
                } else {
                    //Normal case
                    filterMap.put(name, values[0]);
                }

            }
        }

        // Get initial sort order on first page load
        if (sortCriteria == null) {
            sortCriteria = getDefaultSortCriteria(null);
        }

        try {
            //Default page is 1
            int page = pageStr != null ? Integer.parseInt(pageStr) : 1;

            //Parse id string to integer if path is neither /new nor /sale
            int id = classificationId != null ? Integer.parseInt(classificationId) : 0;
            
            //Extract pathname as clsfCode
            String clsfCode = path.substring(1);
            
            //Get the correct classification instance
            ProductClassification classification = getClassficationAttributes(getServletContext(), clsfCode, clsfType, id);
            
            //Get type if null
            if(clsfType == null){
                clsfType =  classification.getType();
            }
            
            //Get breadCrumb and pageTitle
            String breadCrumb = buildBreadCrumb(classification,clsfCode);
            String pageTitle = buildPageTitle(classification);

            //Get product list
            List<Product> productList = productDAO.getProductsByCondition(id, sortCriteria, filterMap, clsfCode, clsfType, "", page, PAGE_SIZE);

            // Calculate total pages
            //Default value is 1
            int totalProducts = 0;
            int totalPages = 1;

            if (productList.isEmpty()) {
                //No result found
                message.setLength(0);
                message.append("No result found! Please deselect some filter if any.");
            } else {
                totalProducts = productDAO.getProductsCount(id, filterMap, clsfCode, clsfType);
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
            request.setAttribute("formattedURL", formatURL(currentURL));
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

    private ProductClassification getClassficationAttributes(ServletContext context, String clsfCode, String clsfType, int clsfID) {
        
        //For Non-EntityClassification like Sale, New
        if (clsfID == 0 && clsfType != null){
            return getNonEntityClassification(clsfCode, clsfType);
        }
        
        //Resolve attribute name in servlet context
        String attrName = resolveServletContextAtributeName(clsfCode);
        
        //Get the attribute from servlet context and cast to its original form
        Map<ProductClassification, Integer> entityMap = (Map<ProductClassification, Integer>) context.getAttribute(attrName);
        if (entityMap == null) {
            throw new IllegalStateException("No map found in context for " + attrName);
        }
        
        // Get the correct classification based on id
        for (ProductClassification entity : entityMap.keySet()) {
                if (entity.getId() == clsfID) return entity;
        }
        return null;
    }
    
    private NonEntityClassification getNonEntityClassification(String clsfCode, String clsfType){
        String name;
        switch((clsfCode != null ? clsfCode : "")){
            case "new": name = "New Release"; break;
            case "sale": name = "On Sale"; break;
            default: name = "";
        }
        return new NonEntityClassification(name, clsfType);
        
    }
    
    private String resolveServletContextAtributeName(String clsfCode){
        String attrName;
        switch(clsfCode != null ? clsfCode : ""){
            case "category": attrName = "categories"; break;
            case "series": attrName = clsfCode; break;
            case "creator":
            case "brand":
            case "character":
            case "publisher":
            case "genre": attrName = clsfCode + "s"; break;
            default: attrName = "";
        }
        return attrName;
    }
    
    private String buildBreadCrumb(ProductClassification clsf, String clsfCode){
        int clsfId = clsf.getId();
        String clsfName = clsf.getName();
        String clsfType = clsf.getType();
        Map<String,Object> extraAttrs = clsf.getExtraAttributes();
        
        
        StringBuilder breadCrumb = new StringBuilder("<a href='home'>Home</a>");
        if(extraAttrs.containsKey("isNonEntity")){
            breadCrumb.append(String.format(" > <a href='search?type=%s'>%s</a>", clsfType, getDisplayTextBasedOnType(clsfType)));
            breadCrumb.append(String.format(" > <a href='%s?type=%s'>%s</a>", clsfCode,clsfType,clsfName));
        }
        else{
            StringBuilder displayText = new StringBuilder(clsfName);
            displayText = extraAttrs.containsKey("creatorRole")
                    ? displayText.append(" - ").append(extraAttrs.get("creatorRole"))
                    : displayText;
            breadCrumb.append( String.format(" > <a href='%s?id=%s'>%s</a>", clsfCode,clsfId, displayText));
        }
        return breadCrumb.toString();
    }
    
    private String buildPageTitle(ProductClassification clsf){
        String clsfName = clsf.getName();
        String clsfType = clsf.getType();
        Map<String,Object> extraAttrs = clsf.getExtraAttributes();
        
        
        StringBuilder pageTitle = new StringBuilder(clsfName);
        if(extraAttrs.containsKey("isNonEntity")){
            pageTitle.append(" - ").append(getDisplayTextBasedOnType(clsfType));
        }
        else{
            pageTitle = extraAttrs.containsKey("creatorRole")
                    ? pageTitle.append(" - ").append(extraAttrs.get("creatorRole"))
                    : pageTitle;
        }
        
        return pageTitle.toString();
    }
    





    private void handleDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String productID = request.getParameter("id");
        String currentURL = request.getRequestURL().toString() + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
        String type = request.getParameter("type");

        try {
            int id = Integer.parseInt(productID);

            Product requestedProduct = productDAO.callGetProductByTypeAndId(type, id, false);
            if (requestedProduct == null) {
                request.setAttribute("message", "The product is not available right now!");
//                request.getRequestDispatcher("home").forward(request, response);
            } else {
                //Handle linebreak for html display
                requestedProduct.setDescription(requestedProduct.getDescription().replaceAll("\r\n|\r|\n", "<br>"));

                //Get creators
                List<Creator> creatorList = productDAO.getCreatorsOfThisProduct(id);
                Collections.sort(creatorList, Comparator.comparing(c -> c.getCreatorName()));
                request.setAttribute("creatorList", creatorList);

                //Get comments & ratings
                Map<String, String[]> reviewMap = orderDAO.getRatingsAndCommentsByProduct(id);
                if (!reviewMap.isEmpty()) {
                    request.setAttribute("reviewMap", reviewMap);
                }

                //Get genres if product is a book
                if (requestedProduct.getGeneralCategory().equals("book")) {
                    List<Genre> genreList = productDAO.getGenresOfThisBook(id);
                    request.setAttribute("genreList", genreList);
                }

                //Construct breadCrumb
                String breadCrumb = String.format("<a href='home'>Home</a> > <a href='search?type=%s'>%s</a> > <a href='category?id=%s'>%s</a> > <a href='productDetails?id=%s&type=%s'>%s</a>",
                        requestedProduct.getGeneralCategory(), getDisplayTextBasedOnType(requestedProduct.getGeneralCategory()), requestedProduct.getSpecificCategory().getCategoryID(), requestedProduct.getSpecificCategory().getCategoryName(), id, requestedProduct.getGeneralCategory(), requestedProduct.getProductName());
                request.setAttribute("breadCrumb", breadCrumb);
                request.setAttribute("product", requestedProduct);
                request.setAttribute("type", type);
                request.setAttribute("currentURL", currentURL);
                request.setAttribute("formattedURL", formatURL(currentURL));

            }
            request.getRequestDispatcher("productDetails.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", e.toString());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void handleHomepage(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        try {
            if (account != null && account.getRole().equals("customer")) {
                CartItemDAO cartDAO = new CartItemDAO();
                List<CartItem> listCart = session.getAttribute("cartItems") != null ? (List<CartItem>) session.getAttribute("cartItems") : cartDAO.getCartItemsByCustomer(account.getAccountID());
                session.setAttribute("cartItems", listCart);

                NotificationDAO notiDAO = new NotificationDAO();
                List<Notification> listNoti = session.getAttribute("notifications") != null ? (List<Notification>) session.getAttribute("notifications") : notiDAO.getNotificationsByReceiverDESC(account.getAccountID());
                session.setAttribute("notifications", listNoti);
            }
            showProductsInHomepage(request);
            showVouchersInHomepage(request);
            showVouchersAvalaibleInHomepage(request);
            request.getRequestDispatcher("home.jsp").forward(request, response);
        } catch (Exception e) {
            System.out.println(e.toString());
            request.setAttribute("errorMessage", e.toString());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }

    }

    private void showProductsInHomepage(HttpServletRequest request) throws Exception {

        List<Product> newBookHome = productDAO.getProductsByCondition(0, "releaseDate", null, "new", "book", "home", 0, 0);
        List<Product> newMerchHome = productDAO.getProductsByCondition(0, "releaseDate", null, "new", "merch", "home", 0, 0);
        List<Product> saleBookHome = productDAO.getProductsByCondition(0, "hotDeal", null, "sale", "book", "home", 0, 0);
        List<Product> saleMerchHome = productDAO.getProductsByCondition(0, "hotDeal", null, "sale", "merch", "home", 0, 0);
        List<Product> animeBookHome = productDAO.getProductsByCondition(18, "releaseDate", null, "genre", "book", "home", 0, 0);
        List<Product> holoMerchHome = productDAO.getProductsByCondition(1, "releaseDate", null, "series", "merch", "home", 0, 0);

        request.setAttribute("newBookHome", newBookHome);
        request.setAttribute("newMerchHome", newMerchHome);
        request.setAttribute("saleBookHome", saleBookHome);
        request.setAttribute("saleMerchHome", saleMerchHome);
        request.setAttribute("animeBookHome", animeBookHome);
        request.setAttribute("holoMerchHome", holoMerchHome);

    }

//    private void showBannerInHomepage(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        List<String> bannerList = eDao.getBannerEvent();
//
//        if (bannerList.isEmpty()) {
//            throw new Exception("Found no products in the catalog!");
//        }
//        request.setAttribute("bannerList", bannerList);
//    }
    private void showVouchersInHomepage(HttpServletRequest request) throws Exception {

        List<Voucher> listVoucher = vDao.getListVoucherAvailableNow();
        request.setAttribute("listVoucher", listVoucher);

    }

    private void showVouchersAvalaibleInHomepage(HttpServletRequest request) throws Exception {

        List<Voucher> listVoucher = vDao.getListVoucherComeSoon();
        request.setAttribute("listVoucherComeSoon", listVoucher);

    }

    private void handleRanking(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String type = request.getParameter("type");

        //For redirect back to original page after logging in or adding items to cart
        String currentURL = request.getRequestURL().toString() + (request.getQueryString() != null ? "?" + request.getQueryString() : "");

        StringBuilder message = new StringBuilder();

        try {

            // Set up breadCrumb and page Title
            // Get product list
            StringBuilder breadCrumb = new StringBuilder("<a href='home'>Home</a>");
            breadCrumb.append(String.format(" > <a href='search?type=%s'>%s</a>", type, getDisplayTextBasedOnType(type)));
            breadCrumb.append(String.format(" > <a href='ranking?type=%s'>Ranking</a>", type));
            List<Product> productList = productDAO.getRankedProducts(type);

            // Set up the product list
            if (productList.isEmpty()) {
                // No result
                message.setLength(0);
                message.append("Leaderboard is not available right now\n");
            } else {
                request.setAttribute("productList", productList);
            }

            //Set up remaining attributes and forward the request
            if (message.length() > 0) {
                request.setAttribute("message", message);
            }

            request.setAttribute("currentURL", currentURL);
            request.setAttribute("formattedURL", formatURL(currentURL));
            request.setAttribute("type", type);
            request.setAttribute("breadCrumb", breadCrumb);
            request.setAttribute("pageTitle", "Leaderboard - " + getDisplayTextBasedOnType(type));
            request.getRequestDispatcher("productCatalog.jsp").forward(request, response);

        } catch (Exception e) {
            System.out.println(e.toString());
            request.setAttribute("errorMessage", e.toString());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private String formatURL(String encodedURL) throws UnsupportedEncodingException, MalformedURLException {

        Map<String, String> decodedURLParts = new HashMap<>();

        // Decode the URL
        String decodedUrl = URLDecoder.decode(encodedURL, "UTF-8");

        // Parse the URL
        URL url = new URL(decodedUrl);

        // Extract components
        String protocol = url.getProtocol();    //http
        String host = url.getHost();        //locohost
        int port = url.getPort();           //8080
        String path = url.getPath();      //Servlet path  
        String query = url.getQuery();       //parameters

        // Extract parameters from query string
        String[] params = query.split("&");
        for (String param : params) {
            String[] keyValue = param.split("=");           //Split param name and value
            String key = keyValue[0];

            if (key.equalsIgnoreCase("page")) {
                continue;
            }

            //Ensure value never null
            String value = keyValue.length > 1 ? keyValue[1] : "";

            //Put only valid param
            decodedURLParts.put(key, value);
        }

        StringBuilder formattedParams = new StringBuilder();
        for (Map.Entry<String, String> entry : decodedURLParts.entrySet()) {
            Object key = entry.getKey();
            Object val = entry.getValue();
            formattedParams.append(key.toString()).append("=").append(val.toString());
            formattedParams.append("&");
        }
        formattedParams.deleteCharAt(formattedParams.lastIndexOf("&"));

        return protocol + "://" + host + (port != -1 ? ":" + port : "") + path + (formattedParams.length() > 0 ? "?" + formattedParams.toString() : "");

    }

    public static void main(String[] args) {
        //TESTING
    }
}
