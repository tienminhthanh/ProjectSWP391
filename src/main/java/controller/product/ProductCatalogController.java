/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.product;

import dao.*;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
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

    private ProductDAO productDAO;
    private VoucherDAO vDao;
    private EventDAO eDao;
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

            // Set up breadCrumb and page Title
            // Get product list
            String breadCrumb = "<a href='home'>Home</a>";
            StringBuilder pageTitle = new StringBuilder();
            List<Product> productList;
            if (query == null || query.trim().isEmpty()) {
                //No keywords entered
                breadCrumb += String.format(" > <a href='search?type=%s'>%s</a>", type, getDisplayTextBasedOnType(type));
                pageTitle.append(getDisplayTextBasedOnType(type));
                productList = productDAO.getActiveProducts(type, sortCriteria, filterMap);

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
            request.setAttribute("type", type);
            request.getRequestDispatcher("productCatalog.jsp").forward(request, response);

        } catch (Exception e) {
            System.out.println(e.toString());
            request.setAttribute("errorMessage", e.toString());
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
            message = "No result found! Try entering series title, name of author/artist, categories or genre.\n";
        } else if (type.equals("merch")) {
            message = "No result found! Try entering series title, name of sculptor/artist/character/brand or category.\n";
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
            List<Product> productList = productDAO.getProductsByCondition(id, sortCriteria, filterMap, "ctg", selectedCategory.getGeneralCategory(), "");
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
            request.setAttribute("type", selectedCategory.getGeneralCategory());
            request.setAttribute("currentURL", currentURL);
            request.getRequestDispatcher("productCatalog.jsp").forward(request, response);

        } catch (Exception e) {
            System.out.println(e.toString());
            request.setAttribute("errorMessage", e.toString());
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

                //Skip non-filter params
                if (!name.startsWith("ft") || filterMap.containsKey(name)) {
                    continue;
                }

                //Prevent MERCH_FITLERS from being applied to Books
                if (MERCH_FILTERS.contains(name)) {
                    message.append("Cannot apply this filter to Books!");
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

            //Parse id string to integer
            int id = Integer.parseInt(genreID);

            //            Set up breadCrumb and page title
            Genre selectedGenre = productDAO.getGenreById(id);
            String genreName = selectedGenre.getGenreName();
            StringBuilder breadCrumb = new StringBuilder("<a href='home'>Home</a>");
            breadCrumb.append(" > <a href='search?type=book'>Books</a>");
            breadCrumb.append(String.format(" > <a href='genre?id=%s'>%s</a>", id, genreName));
            request.setAttribute("pageTitle", genreName);
            request.setAttribute("breadCrumb", breadCrumb);

            //Get product list
            List<Product> productList = productDAO.getProductsByCondition(id, sortCriteria, filterMap, "gnr", "book", "");
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
            System.out.println(e.toString());
            request.setAttribute("errorMessage", e.toString());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void handlePublisher(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String publisherID = request.getParameter("id");
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

                //Prevent MERCH_FITLERS from being applied to Books
                if (MERCH_FILTERS.contains(name)) {
                    message.append("Cannot apply this filter to Books!");
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

            //Parse id string to integer
            int id = Integer.parseInt(publisherID);

            //            Set up breadCrumb and page title
            Publisher selectedPublisher = productDAO.getPublisherById(id);
            String publisherName = selectedPublisher.getPublisherName();
            StringBuilder breadCrumb = new StringBuilder("<a href='home'>Home</a>");
            breadCrumb.append(" > <a href='search?type=book'>Books</a>");
            breadCrumb.append(String.format(" > <a href='publisher?id=%s'>%s</a>", id, publisherName));
            request.setAttribute("pageTitle", publisherName);
            request.setAttribute("breadCrumb", breadCrumb);

            //Get product list
            List<Product> productList = productDAO.getProductsByCondition(id, sortCriteria, filterMap, "pbl", "book", "");
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
            System.out.println(e.toString());
            request.setAttribute("errorMessage", e.toString());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void handleCreator(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String creatorID = request.getParameter("id");
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
            //Parse id string to integer
            int id = Integer.parseInt(creatorID);

            //            Set up breadCrumb and page title
            String breadCrumb = "<a href='home'>Home</a>";
            Creator selectedCreator = productDAO.getCreatorById(id);
            String creatorName = selectedCreator.getCreatorName();
            String creatorRole = selectedCreator.getCreatorRole();
            breadCrumb += String.format(" > <a href='creator?id=%s'>%s - %s</a>", id, creatorName, creatorRole);
            request.setAttribute("pageTitle", creatorName + " - " + creatorRole);
            request.setAttribute("breadCrumb", breadCrumb);

            //Get product list
            List<Product> productList = productDAO.getProductsByCondition(id, sortCriteria, filterMap, "crt", selectedCreator.getGeneralCategory(), "");
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
            request.setAttribute("type", selectedCreator.getGeneralCategory());
            request.setAttribute("currentURL", currentURL);
            request.getRequestDispatcher("productCatalog.jsp").forward(request, response);

        } catch (Exception e) {
            System.out.println(e.toString());
            request.setAttribute("errorMessage", e.toString());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void handleSeries(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String seriesID = request.getParameter("id");
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

                //Prevent MERCH_FITLERS from being applied to Books
                if (BOOK_FITLERS.contains(name)) {
                    message.append("Cannot apply this filter to Merch!");
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

            //Parse id string to integer
            int id = Integer.parseInt(seriesID);

            //            Set up breadCrumb and page title
            Series selectedSeries = productDAO.getSeriesById(id);
            String seriesName = selectedSeries != null ? selectedSeries.getSeriesName() : "Coming Soon";
            StringBuilder breadCrumb = new StringBuilder("<a href='home'>Home</a>");
            breadCrumb.append(" > <a href='search?type=merch'>Merchandise</a>");
            breadCrumb.append(String.format(" > <a href='series?id=%s'>%s</a>", id, seriesName));
            request.setAttribute("pageTitle", seriesName);
            request.setAttribute("breadCrumb", breadCrumb);

            //Get product list
            List<Product> productList = productDAO.getProductsByCondition(id, sortCriteria, filterMap, "srs", "merch", "");
            if (productList.isEmpty()) {
                //No result found
                message.setLength(0);
                message.append(selectedSeries != null ? "No result found! Please deselect some filter if any." : "This category is unavailable for now. Feel free to browse our other products!");
            } else {
                request.setAttribute("productList", productList);
                //For displaying current sort criteria
                request.setAttribute("sortCriteria", sortCriteria);
            }

            //Set up remaining attributes and forward the request
            if (message.length() > 0) {
                request.setAttribute("message", message);
            }
            request.setAttribute("type", "merch");
            request.setAttribute("currentURL", currentURL);
            request.getRequestDispatcher("productCatalog.jsp").forward(request, response);

        } catch (Exception e) {
            System.out.println(e.toString());
            request.setAttribute("errorMessage", e.toString());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void handleBrand(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String brandID = request.getParameter("id");
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

                //Prevent MERCH_FITLERS from being applied to Books
                if (BOOK_FITLERS.contains(name)) {
                    message.append("Cannot apply this filter to Merch!");
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

            //Parse id string to integer
            int id = Integer.parseInt(brandID);

            //            Set up breadCrumb and page title
            Brand selectedBrand = productDAO.getBrandById(id);
            String brandName = selectedBrand != null ? selectedBrand.getBrandName() : "Coming Soon";
            StringBuilder breadCrumb = new StringBuilder("<a href='home'>Home</a>");
            breadCrumb.append(" > <a href='search?type=merch'>Merchandise</a>");
            breadCrumb.append(String.format(" > <a href='brand?id=%s'>%s</a>", id, brandName));
            request.setAttribute("pageTitle", brandName);
            request.setAttribute("breadCrumb", breadCrumb);

            //Get product list
            List<Product> productList = productDAO.getProductsByCondition(id, sortCriteria, filterMap, "brn", "merch", "");
            if (productList.isEmpty()) {
                //No result found
                message.setLength(0);
                message.append(selectedBrand != null ? "No result found! Please deselect some filter if any." : "This category is unavailable for now. Feel free to browse our other products!");
            } else {
                request.setAttribute("productList", productList);
                //For displaying current sort criteria
                request.setAttribute("sortCriteria", sortCriteria);
            }

            //Set up remaining attributes and forward the request
            if (message.length() > 0) {
                request.setAttribute("message", message);
            }
            request.setAttribute("type", "merch");
            request.setAttribute("currentURL", currentURL);
            request.getRequestDispatcher("productCatalog.jsp").forward(request, response);

        } catch (Exception e) {
            System.out.println(e.toString());
            request.setAttribute("errorMessage", e.toString());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void handleCharacter(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String characterID = request.getParameter("id");
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

                //Prevent MERCH_FITLERS from being applied to Books
                if (BOOK_FITLERS.contains(name)) {
                    message.append("Cannot apply this filter to Merch!");
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

            //Parse id string to integer
            int id = Integer.parseInt(characterID);

            //            Set up breadCrumb and page title
            OGCharacter selectedCharacter = productDAO.getCharacterById(id);
            String characterName = selectedCharacter != null ? selectedCharacter.getCharacterName() : "Coming Soon";
            StringBuilder breadCrumb = new StringBuilder("<a href='home'>Home</a>");
            breadCrumb.append(" > <a href='search?type=merch'>Merchandise</a>");
            breadCrumb.append(String.format(" > <a href='character?id=%s'>%s</a>", id, characterName));
            request.setAttribute("pageTitle", characterName);
            request.setAttribute("breadCrumb", breadCrumb);

            //Get product list
            List<Product> productList = productDAO.getProductsByCondition(id, sortCriteria, filterMap, "chr", "merch", "");
            if (productList.isEmpty()) {
                //No result found
                message.setLength(0);
                message.append(selectedCharacter != null ? "No result found! Please deselect some filter if any." : "This category is unavailable for now. Feel free to browse our other products!");
            } else {
                request.setAttribute("productList", productList);
                //For displaying current sort criteria
                request.setAttribute("sortCriteria", sortCriteria);
            }

            //Set up remaining attributes and forward the request
            if (message.length() > 0) {
                request.setAttribute("message", message);
            }
            request.setAttribute("type", "merch");
            request.setAttribute("currentURL", currentURL);
            request.getRequestDispatcher("productCatalog.jsp").forward(request, response);

        } catch (Exception e) {
            System.out.println(e.toString());
            request.setAttribute("errorMessage", e.toString());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void handleNewRelease(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String type = request.getParameter("type");

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

            // Set up breadCrumb and page Title
            // Get product list
            StringBuilder breadCrumb = new StringBuilder("<a href='home'>Home</a>");
            breadCrumb.append(String.format(" > <a href='search?type=%s'>%s</a>", type, getDisplayTextBasedOnType(type)));
            breadCrumb.append(String.format(" > <a href='new?type=%s'>New Release</a>", type));
            List<Product> productList = productDAO.getProductsByCondition(0, sortCriteria, filterMap, "new", type, "");

            // Set up the product list
            if (productList.isEmpty()) {
                // No result
                message.setLength(0);

                //If filter too tight
                if (!filterMap.isEmpty()) {
                    message.append("No result found! Please deselect some filter if any!\n");
                } else {
                    message.append("No new releases available right now. More interesting products coming soon!\n");
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
            request.setAttribute("type", type);
            request.setAttribute("breadCrumb", breadCrumb);
            request.setAttribute("pageTitle", "New Release - " + getDisplayTextBasedOnType(type));
            request.getRequestDispatcher("productCatalog.jsp").forward(request, response);

        } catch (Exception e) {
            System.out.println(e.toString());
            request.setAttribute("errorMessage", e.toString());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void handleOnSale(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String type = request.getParameter("type");

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

            // Set up breadCrumb and page Title
            // Get product list
            StringBuilder breadCrumb = new StringBuilder("<a href='home'>Home</a>");
            breadCrumb.append(String.format(" > <a href='search?type=%s'>%s</a>", type, getDisplayTextBasedOnType(type)));
            breadCrumb.append(String.format(" > <a href='sale?type=%s'>On Sale</a>", type));
            List<Product> productList = productDAO.getProductsByCondition(0, sortCriteria, filterMap, "sale", type, "");

            // Set up the product list
            if (productList.isEmpty()) {
                // No result
                message.setLength(0);

                //If filter too tight
                if (!filterMap.isEmpty()) {
                    message.append("No result found! Please deselect some filter if any!\n");
                } else {
                    message.append("All discounted items are sold out! Stay tuned for the next sale.\n");
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
            request.setAttribute("type", type);
            request.setAttribute("breadCrumb", breadCrumb);
            request.setAttribute("pageTitle", "On Sale - " + getDisplayTextBasedOnType(type));
            request.getRequestDispatcher("productCatalog.jsp").forward(request, response);

        } catch (Exception e) {
            System.out.println(e.toString());
            request.setAttribute("errorMessage", e.toString());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void handleDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String productID = request.getParameter("id");
        String currentURL = request.getRequestURL().toString() + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
        String type = request.getParameter("type");

        try {
            int id = Integer.parseInt(productID);

            Product requestedProduct = productDAO.callGetProductByTypeAndId(type, id,false);
            if (requestedProduct == null) {
                request.setAttribute("message", "The product is not available right now!");
                request.getRequestDispatcher("home").forward(request, response);
            } else {
                //Handle linebreak for html display
                requestedProduct.setDescription(requestedProduct.getDescription().replaceAll("\r\n|\r|\n", "<br>"));
                
                //Get and creators
                List<Creator> creatorList = productDAO.getCreatorsOfThisProduct(id);
                Collections.sort(creatorList,Comparator.comparing(c -> c.getCreatorName()));
                request.setAttribute("creatorList",creatorList);

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

                request.getRequestDispatcher("productDetails.jsp").forward(request, response);
            }

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
            if (account != null && (account.getRole().equals("customer"))) {
                CartItemDAO cartDAO = new CartItemDAO();
                List<CartItem> listCart = session.getAttribute("cartItems") != null ? (List<CartItem>) session.getAttribute("cartItems") : cartDAO.getCartItemsByCustomer(account.getAccountID());
                session.setAttribute("cartItems", listCart);

                NotificationDAO notiDAO = new NotificationDAO();
                List<Notification> listNoti = session.getAttribute("notifications") != null ? (List<Notification>) session.getAttribute("notifications") : notiDAO.getNotificationsByReceiverDESC(account.getAccountID());
                session.setAttribute("notifications", listNoti);
            }
            showProductsInHomepage(session);
            showVouchersInHomepage(request);
            request.getRequestDispatcher("home.jsp").forward(request, response);
        } catch (Exception e) {
            System.out.println(e.toString());
            request.setAttribute("errorMessage", e.toString());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }

    }

    private void showProductsInHomepage(HttpSession session) throws Exception {

        List<Product> newBookHome = session.getAttribute("newBookHome") != null ? (List<Product>) session.getAttribute("newBookHome") : productDAO.getProductsByCondition(0, "releaseDate", null, "new", "book", "home");
        List<Product> newMerchHome = session.getAttribute("newMerchHome") != null ? (List<Product>) session.getAttribute("newMerchHome") : productDAO.getProductsByCondition(0, "releaseDate", null, "new", "merch", "home");
        List<Product> saleBookHome = session.getAttribute("saleBookHome") != null ? (List<Product>) session.getAttribute("saleBookHome") : productDAO.getProductsByCondition(0, "hotDeal", null, "sale", "book", "home");
        List<Product> saleMerchHome = session.getAttribute("saleMerchHome") != null ? (List<Product>) session.getAttribute("saleMerchHome") : productDAO.getProductsByCondition(0, "hotDeal", null, "sale", "merch", "home");
        List<Product> animeBookHome = session.getAttribute("animeBookHome") != null ? (List<Product>) session.getAttribute("animeBookHome") : productDAO.getProductsByCondition(18, "releaseDate", null, "gnr", "book", "home");
        List<Product> holoMerchHome = session.getAttribute("holoMerchHome") != null ? (List<Product>) session.getAttribute("holoMerchHome") : productDAO.getProductsByCondition(1, "releaseDate", null, "srs", "merch", "home");

        session.setAttribute("newBookHome", newBookHome);
        session.setAttribute("newMerchHome", newMerchHome);
        session.setAttribute("saleBookHome", saleBookHome);
        session.setAttribute("saleMerchHome", saleMerchHome);
        session.setAttribute("animeBookHome", animeBookHome);
        session.setAttribute("holoMerchHome", holoMerchHome);

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

        List<Voucher> listVoucher = vDao.getListVoucher();
        request.setAttribute("listVoucher", listVoucher);

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
