/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.product;

import dao.ProductDAO;
import dao.VoucherDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import model.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Utility;

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

    private ProductDAO productDAO;
    private VoucherDAO vDao;

    @Override
    public void init() throws ServletException {
        vDao = new VoucherDAO();
        productDAO = (ProductDAO) getServletContext().getAttribute("productDAO");

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

        switch (path) {
            case "/search":
                handleSearch(request, response);
                break;
            case "/category":
                handleCategory(request, response);
                break;
            case "/genre":
                handleGenre(request, response);
                break;
            case "/publisher":
                handlePublisher(request, response);
                break;
            case "/creator":
                handleCreator(request, response);
                break;
            case "/series":
                handleSeries(request, response);
                break;
            case "/brand":
                handleBrand(request, response);
                break;
            case "/character":
                handleCharacter(request, response);
                break;
            case "/new":
                handleNewRelease(request, response);
                break;
            case "/sale":
                handleOnSale(request, response);
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

        String sortCriteria = request.getParameter("sortCriteria");
        Map<String, String[]> paramMap = request.getParameterMap();
        //For redirect back to original page after logging in or adding items to cart
        String currentURL = request.getRequestURL().toString() + (request.getQueryString() != null ? "?" + request.getQueryString() : "");

        //Handling filters
        Map<String, String> filterMap = new HashMap<>();
        if (paramMap != null) {
            for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
                String name = entry.getKey();
                String[] values = entry.getValue();

                if (!name.startsWith("ft") || filterMap.containsKey(name)) {
                    continue;
                }

                if (name.equals("ftPrc")) {
                    filterMap.put(name, values[0] + "-" + values[1]);
                } else {
                    filterMap.put(name, values[0]);
                }
            }
        }

        // Get initial sort order on first page load
        if (sortCriteria == null) {
            sortCriteria = getDefaultSortCriteria(query);
        }

        try {

            // Set up breadCrumb and page Title
            // Get product list
            String breadCrumb = "<a href='home'>Home</a>";
            StringBuilder pageTitle = new StringBuilder();
            List<Product> productList;
            if (query == null || query.trim().isEmpty()) {
                //No keywords entered
                breadCrumb += String.format(" > <a href='search?type=%s'>%s</a>", type, getDisplayTextBasedOnType(type));
                pageTitle.append(getDisplayTextBasedOnType(type));
                productList = productDAO.getAllActiveProducts(type, sortCriteria, filterMap);

            } else {
                breadCrumb += String.format(" > <a href='search?type=%s&query=%s'>Search Result: %s</a>", type, query, query);
                pageTitle.append("Search Result: ").append(query);
                productList = productDAO.getSearchResult(query, type, sortCriteria, filterMap);
                // Set attribute if there are keywords entered
                request.setAttribute("query", query);
            }

            request.setAttribute("breadCrumb", breadCrumb);
            request.setAttribute("pageTitle", pageTitle.toString());

            // Set up the product list
            if (productList.isEmpty()) {
                // No result found
                request.setAttribute("message", getNoResultMessage(type));
            } else {
                request.setAttribute("productList", productList);
                // For displaying current sort criteria
                request.setAttribute("sortCriteria", sortCriteria);

            }

            //Set up remaining attributes and forward the request
            request.setAttribute("currentURL", currentURL);
            request.setAttribute("type", type);
            request.getRequestDispatcher("productCatalog.jsp").forward(request, response);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
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
            message = "No result found! Try entering series title, name of author/artist, categories or genre!";
        } else if (type.equals("merch")) {
            message = "No result found! Try entering series title, name of sculptor/artist/character/brand or category!";
        }
        return message;
    }

    private void handleCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String categoryID = request.getParameter("id");
        String sortCriteria = request.getParameter("sortCriteria");
        Map<String, String[]> paramMap = request.getParameterMap();
        //For redirect back to original page after logging in or adding items to cart
        String currentURL = request.getRequestURL().toString() + (request.getQueryString() != null ? "?" + request.getQueryString() : "");

        //Handling filters
        Map<String, String> filterMap = new HashMap<>();
        if (paramMap != null) {
            for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
                String name = entry.getKey();
                String[] values = entry.getValue();

                if (!name.startsWith("ft") || filterMap.containsKey(name)) {
                    continue;
                }

                if (name.equals("ftPrc")) {
                    filterMap.put(name, values[0] + "-" + values[1]);
                } else {
                    filterMap.put(name, values[0]);
                }

            }
        }

        // Get initial sort order on first page load
        if (sortCriteria == null) {
            sortCriteria = getDefaultSortCriteria(null);
        }

        try {
            //Parse id string to integer
            int id = Integer.parseInt(categoryID);

            //            Set up breadCrumb and page title
            String breadCrumb = "<a href='home'>Home</a>";
            Category selectedCategory = productDAO.getCategoryById(id);
            String categoryName = selectedCategory.getCategoryName();
            breadCrumb += String.format(" > <a href='category?id=%s'>%s</a>", id, categoryName);
            request.setAttribute("pageTitle", categoryName);
            request.setAttribute("breadCrumb", breadCrumb);

            //Get product list
            List<Product> productList = productDAO.getProductsByCondition(id, sortCriteria, filterMap, "ctg", selectedCategory.getGeneralCategory());
            if (productList.isEmpty()) {
                //No result found
                request.setAttribute("message", "No result found! Please deselect some filter if any.");
            } else {
                request.setAttribute("productList", productList);
                //For displaying current sort criteria
                request.setAttribute("sortCriteria", sortCriteria);
            }

            //Set up remaining attributes and forward the request
            request.setAttribute("type", selectedCategory.getGeneralCategory());
            request.setAttribute("currentURL", currentURL);
            request.getRequestDispatcher("productCatalog.jsp").forward(request, response);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }

    }

    private void handleGenre(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String genreID = request.getParameter("id");
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

                if (!name.startsWith("ft") || filterMap.containsKey(name)) {
                    continue;
                }

                if (name.equals("ftSrs") || name.equals("ftChr") || name.equals("ftBrn")) {
                    message.append("Cannot apply this filter to Books!");
                    continue;
                }

                if (name.equals("ftPrc")) {
                    filterMap.put(name, values[0] + "-" + values[1]);
                } else {
                    filterMap.put(name, values[0]);
                }

            }
        }

        // Get initial sort order on first page load
        if (sortCriteria == null) {
            sortCriteria = getDefaultSortCriteria(null);
        }

        try {
            
            //Parse id string to integer
            int id = Integer.parseInt(genreID);

            //            Set up breadCrumb and page title
            String breadCrumb = "<a href='home'>Home</a>";
            Genre selectedGenre = productDAO.getGenreById(id);
            String genreName = selectedGenre.getGenreName();
            breadCrumb += String.format(" > <a href='genre?id=%s'>%s</a>", id, genreName);
            request.setAttribute("pageTitle", genreName);
            request.setAttribute("breadCrumb", breadCrumb);

            //Get product list
            List<Product> productList = productDAO.getProductsByCondition(id, sortCriteria, filterMap, "gnr", "book");
            if (productList.isEmpty()) {
                //No result found
                message.setLength(0);
                message.append("No result found! Please deselect some filter if any.");
            } else {
                request.setAttribute("productList", productList);
                //For displaying current sort criteria
                request.setAttribute("sortCriteria", sortCriteria);
            }

            //Set up remaining attributes and forward the request
            if (message.length() > 0) {
                request.setAttribute("message", message);
            }
            request.setAttribute("type", "book");
            request.setAttribute("currentURL", currentURL);
            request.getRequestDispatcher("productCatalog.jsp").forward(request, response);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void handlePublisher(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Implementation for publisher functionality
    }

    private void handleCreator(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String creatorID = request.getParameter("id");
        String sortCriteria = request.getParameter("sortCriteria");
        Map<String, String[]> paramMap = request.getParameterMap();
        //For redirect back to original page after logging in or adding items to cart
        String currentURL = request.getRequestURL().toString() + (request.getQueryString() != null ? "?" + request.getQueryString() : "");

        //Handling filters
        Map<String, String> filterMap = new HashMap<>();
        if (paramMap != null) {
            for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
                String name = entry.getKey();
                String[] values = entry.getValue();

                if (!name.startsWith("ft") || filterMap.containsKey(name)) {
                    continue;
                }

                if (name.equals("ftPrc")) {
                    filterMap.put(name, values[0] + "-" + values[1]);
                } else {
                    filterMap.put(name, values[0]);
                }

            }
        }

        // Get initial sort order on first page load
        if (sortCriteria == null) {
            sortCriteria = getDefaultSortCriteria(null);
        }

        try {
            //Parse id string to integer
            int id = Integer.parseInt(creatorID);

            //            Set up breadCrumb and page title
            String breadCrumb = "<a href='home'>Home</a>";
            Creator selectedCreator = productDAO.getCreatorById(id);
            String creatorName = selectedCreator.getCreatorName();
            breadCrumb += String.format(" > <a href='creator?id=%s'>%s</a>", id, creatorName);
            request.setAttribute("pageTitle", creatorName);
            request.setAttribute("breadCrumb", breadCrumb);

            //Get product list
            List<Product> productList = productDAO.getProductsByCondition(id, sortCriteria, filterMap, "crt", selectedCreator.getGeneralCategory());
            if (productList.isEmpty()) {
                //No result found
                request.setAttribute("message", "No result found! Please deselect some filter if any.");
            } else {
                request.setAttribute("productList", productList);
                //For displaying current sort criteria
                request.setAttribute("sortCriteria", sortCriteria);
            }

            //Set up remaining attributes and forward the request
            request.setAttribute("type", selectedCreator.getGeneralCategory());
            request.setAttribute("currentURL", currentURL);
            request.getRequestDispatcher("productCatalog.jsp").forward(request, response);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void handleSeries(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Implementation for series functionality
    }

    private void handleBrand(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Implementation for brand functionality
    }

    private void handleCharacter(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Implementation for character functionality
    }

    private void handleNewRelease(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Implementation for new content functionality
    }

    private void handleOnSale(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Implementation for sale functionality
    }

    private void handleDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String productID = request.getParameter("id");
        String currentURL = request.getRequestURL().toString() + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
        String type = request.getParameter("type");

        try {
            int id = Integer.parseInt(productID);

            Product requestedProduct = productDAO.callGetProductByTypeAndId(type, id);
            if (requestedProduct == null) {
                throw new Exception("The product is not available right now!");
            }

            //Get creators
            HashMap<String, Creator> creatorMap = productDAO.getCreatorsOfThisProduct(id);

            //Get genres if product is a book
            if (requestedProduct.getGeneralCategory().equals("book")) {
                List<Genre> genreList = productDAO.getGenresOfThisBook(id);
                request.setAttribute("genreList", genreList);
            }

            //Construct breadCrumb
            String breadCrumb = String.format("<a href='home'>Home</a> > <a href='search?type=%s'>%s</a> > <a href='catalog?category=%s'>%s</a> > <a href='productDetails?id=%s&type=%s'>%s</a>",
                    requestedProduct.getGeneralCategory(), getDisplayTextBasedOnType(requestedProduct.getGeneralCategory()), requestedProduct.getSpecificCategory().getCategoryID(), requestedProduct.getSpecificCategory().getCategoryName(), id, requestedProduct.getGeneralCategory(), requestedProduct.getProductName());

            request.setAttribute("type", requestedProduct.getGeneralCategory());
            request.setAttribute("currentURL", currentURL);
            request.setAttribute("breadCrumb", breadCrumb);
            request.setAttribute("product", requestedProduct);
            request.setAttribute("creatorMap", creatorMap);

            request.getRequestDispatcher("productDetails.jsp").forward(request, response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void handleHomepage(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("account") != null) {
            Account account = (Account) session.getAttribute("account");
            switch (account.getRole()) {
                case "staff":
                    response.sendRedirect("dashboard.jsp");
                    break;
                case "shipper":
                    response.sendRedirect("shipperDashboard.jsp");
                    break;
                case "admin":
                    response.sendRedirect("listAccount"); // Điều hướng đến danh sách tài khoản
                    break;
            }
            if (!account.getRole().equals("customer")) {
                return;
            }
        }

        try {
            showProductsInHomepage(request, response);
            showVouchersInHomepage(request, response);

            request.getRequestDispatcher("home.jsp").forward(request, response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }

    }

    private void showProductsInHomepage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Product> productList = productDAO.get10RandomActiveProducts("book");

        if (productList.isEmpty()) {
            throw new Exception("Found no products in the catalog!");
        }
        request.setAttribute("productList", productList);
    }

    private void showVouchersInHomepage(HttpServletRequest request, HttpServletResponse response) throws Exception {

        List<Voucher> listVoucher = vDao.getListVoucher();
        request.setAttribute("listVoucher", listVoucher);

    }

    private void handleRanking(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Implementation for ranking functionality
    }
    
    private String formatURL(String encodedURL, Set<String> invalidParams) throws UnsupportedEncodingException, MalformedURLException {

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
            
            //Skip if invalid params found
            if (invalidParams.contains(key)) {
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
