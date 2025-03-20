/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.product;

import dao.EventDAO;
import dao.OrderDAO;
import dao.ProductDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Account;
import model.Book;
import model.Brand;
import model.Category;
import model.Creator;
import model.Event;
import model.Genre;
import model.Merchandise;
import model.OGCharacter;
import model.Product;
import model.Publisher;
import model.Series;
import utils.LoggingConfig;

/**
 *
 * @author anhkc
 */
@WebServlet({"/manageProductList", "/manageProductDetails", "/updateProduct", "/addProduct", "/changeProductStatus"})
public class ProductManagementController extends HttpServlet {

    private static final Logger LOGGER = LoggingConfig.getLogger(ProductManagementController.class);
    private ProductDAO productDAO;
    private OrderDAO orderDAO;
    private EventDAO eDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        productDAO = new ProductDAO();
        orderDAO = new OrderDAO();
        eDAO = new EventDAO();
        LOGGER.info("ProductManagementController logger initialized");
    }

    /**
     * Processes requests for both HTTP GET and POST methods.
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
            case "/manageProductList":
                manageList(request, response);
                break;
            case "/manageProductDetails":
                manageDetails(request, response);
                break;
            case "/updateProduct":
                manageUpdate(request, response);
                break;
            case "/addProduct":
                manageAdd(request, response);
                break;
            case "/changeProductStatus":
                manageStatus(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid product management url: " + path);
                break;
        }
    }

    private void manageList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //Three items per page
        int pageSize = 5;
        String pageStr = request.getParameter("page");
        String query = request.getParameter("query");
        String type = request.getParameter("type");
        //Default page is 1
        int page = pageStr != null ? Integer.parseInt(pageStr) : 1;

        //Prevent unauthorized access
        HttpSession session = request.getSession();
        Account currentAccount = (Account) (session.getAttribute("account"));
        if (currentAccount == null || (!currentAccount.getRole().equals("admin") && !currentAccount.getRole().equals("staff"))) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            // Get all products
            List<Product> productList = productDAO.getAllProducts(query, type, query != null && !query.trim().isEmpty() ? "relevance" : "", page, pageSize);

            // Calculate total pages
            //Default value is 1
            int totalProducts = productDAO.getProductsCount(query, type);
            int totalPages = totalProducts > 0 ? (int) Math.ceil((double) totalProducts / pageSize) : 1;

            LOGGER.log(Level.INFO, "Retrieved {0} - {1} of total {2} products!",
                    new Object[]{
                        (page - 1) * pageSize + 1,
                         (page * pageSize) > totalProducts ? totalProducts : page * pageSize,
                         totalProducts
                    });

            request.setAttribute("productList", productList);
            request.setAttribute("type", type);
            request.setAttribute("query", query);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("totalProducts", totalProducts);

            request.getRequestDispatcher("productCatalogManagement.jsp").forward(request, response);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error: " + e.getMessage(), e);
            request.setAttribute("errorMessage", "An error occurred while fetching products: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void manageDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String productID = request.getParameter("id");
        String currentURL = request.getRequestURL().toString() + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
        String type = request.getParameter("type");

        //Prevent unauthorized access
        HttpSession session = request.getSession();
        Account currentAccount = (Account) (session.getAttribute("account"));
        if (currentAccount == null || (!currentAccount.getRole().equals("admin") && !currentAccount.getRole().equals("staff"))) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            int id = Integer.parseInt(productID);

            Product requestedProduct = productDAO.callGetProductByTypeAndId(type, id,true);
            if (requestedProduct == null) {
                request.setAttribute("message", "Cannot retrieve information of productID=" + id);
                request.getRequestDispatcher("manageProductList").forward(request, response);
            } else {
                //Format linebreak for html display
                requestedProduct.setDescription(requestedProduct.getDescription().replaceAll("\r\n|\r|\n", "<br>"));

                //Get and creators
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

                //Get event list
                List<Event> eventList = eDAO.getListActiveEvents();
                request.setAttribute("eventList", eventList);

                //Check if product is currently featured in an event
                request.setAttribute("productEventStatus", requestedProduct.getEventEndDate() == null || LocalDate.now().isAfter(requestedProduct.getEventEndDate()) ? "notInEvent" : "inEvent");
                request.setAttribute("product", requestedProduct);
                request.setAttribute("type", type);
                request.setAttribute("currentURL", currentURL);

                request.getRequestDispatcher("productDetailsManagement.jsp").forward(request, response);
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error: " + e.getMessage(), e);
            request.setAttribute("errorMessage", "An error occurred while fetching the product: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void manageUpdate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        //Prevent unauthorized access
        HttpSession session = request.getSession();
        Account currentAccount = (Account) (session.getAttribute("account"));
        if (currentAccount == null || !currentAccount.getRole().equals("admin")) {
            response.sendRedirect("login.jsp");
            return;
        }

        if (action == null) {
            retrieveProduct(request, response);
            return;
        } else {

            //Update general info
            //
            //For related entities, keep the input type=text, then use either one of these 2 approaches:
            //Easy (maybe) but require more database connection:
            //Use where contains(name,formatQueryTight('inputText')) to dynammically fetch full-text search from database
            //After submission, check if exist ( double check )
            //If not, add new
            //If exist -> re-associate if the association is new
            //NOT includes update existing entries of related entities
            //Difficult, less database connection:
            //Use the entityMap in applicationScope and similarity check with Levenshtein and Jaro-Winkler instead of database search
            //After submission, check if exist with full-text search ( double check )
            //If not, add new
            //If exist -> re-associate if the association is new
            //NOT includes update existing entries of related entities
            switch (action) {
                case "updateBook":
//                    updateBook(request, response, paramMap, newBook);
                    break;
                case "updateMerch":
//                    updateMerch(request, response, paramMap, newBook);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid product management action: " + action);
            }
        }
    }

    private void retrieveProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //Retrieve product info from database based on id and generalCategory (product type)
        //Set the formAction to ensure the page show the correct form

        String productID = request.getParameter("id");
        String type = request.getParameter("type");
        try {
            int id = Integer.parseInt(productID);

            Product requestedProduct = productDAO.callGetProductByTypeAndId(type, id,true);
            if (requestedProduct == null) {
                request.setAttribute("message", "Cannot retrieve information of productID=" + id);
                request.getRequestDispatcher("manageProductList").forward(request, response);
            } else {
                //Format linebreak for html display
                requestedProduct.setDescription(requestedProduct.getDescription().replaceAll("\r\n|\r|\n", "<br>"));

                //Get and creators
                List<Creator> creatorList = productDAO.getCreatorsOfThisProduct(id);
                Collections.sort(creatorList, Comparator.comparing(c -> c.getCreatorName()));
                request.setAttribute("creatorList", creatorList);

                //Get genres if product is a book
                if (requestedProduct.getGeneralCategory().equals("book")) {
                    List<Genre> genreList = productDAO.getGenresOfThisBook(id);
                    request.setAttribute("genreList", genreList);
                }

                //Check if product is currently featured in an event
                request.setAttribute("product", requestedProduct);
                request.setAttribute("type", type);

                request.setAttribute("formAction", "update");
                request.getRequestDispatcher("productInventoryManagement.jsp").forward(request, response);
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An error occurred while fetching the product: {0}", e.toString());
            request.setAttribute("errorMessage", "An error occurred while fetching the product: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }

    }

    private void updateBook(HttpServletRequest request, HttpServletResponse response, Map<String, String[]> paramMap, Book newBook)
            throws Exception {
//       Handle book specific attributes and related entities

    }

    private void updateMerch(HttpServletRequest request, HttpServletResponse response, Map<String, String[]> paramMap, Book newBook)
            throws Exception {

//       Handle merch specific attributes and related entities
    }

    private void manageAdd(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        //Prevent unauthorized access
        HttpSession session = request.getSession();
        Account currentAccount = (Account) (session.getAttribute("account"));
        if (currentAccount == null || !currentAccount.getRole().equals("admin")) {
            response.sendRedirect("login.jsp");
            return;
        }

        if (action == null) {
            //Ensure only forms for adding products are shown
            request.setAttribute("formAction", "add");
        } else {
            //After form submission
            Map<String, String[]> paramMap = request.getParameterMap() != null ? request.getParameterMap() : new HashMap<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            List<Object> dataList = new ArrayList<>();

            try {
                //Instantiate newProduct
                Product newProduct = action.equals("addBook") ? new Book()
                        : action.equals("addMerch") ? new Merchandise()
                        : new Product();

                newProduct.setProductName(paramMap.get("productName")[0])
                        .setPrice(1000*Integer.parseInt(paramMap.get("price")[0]))  //Multiply prices by 1000
                        .setStockCount(Integer.parseInt(paramMap.get("stockCount")[0]))
                        .setSpecificCategory(new Category().setCategoryID(Integer.parseInt(paramMap.get("category")[0])))
                        .setSpecialFilter(paramMap.get("specialFilter")[0])
                        .setDescription(paramMap.get("description")[0])
                        .setReleaseDate(LocalDate.parse(paramMap.get("releaseDate")[0], formatter))
                        .setIsActive(Boolean.parseBoolean(paramMap.get("isActive")[0]))
                        .setKeywords(paramMap.get("keywords")[0])
                        .setImageURL(paramMap.get("imageURL")[0])
                        .setAdminID(currentAccount.getAccountID())
                        .setGeneralCategory(action.equals("addBook") ? "book"
                                : action.equals("addMerch") ? "merch" : "unset");

                //Handling creators
                String[] creatorNames = paramMap.get("creatorName");
                String[] creatorRoles = paramMap.get("creatorRole");
                for (int i = 0; i < creatorNames.length; i++) {
                    //Skip if name is empty
                    if (creatorNames[i].trim().isEmpty()) {
                        continue;
                    }

                    dataList.add(new Creator().setCreatorName(creatorNames[i]).setCreatorRole(creatorRoles[i]));
                }

                //Type-specific atributes
                if (newProduct instanceof Book) {
                    Book newBook = (Book) newProduct;
                    newBook.setDuration(paramMap.get("duration")[0]);

                    String[] genres = paramMap.get("genre") != null ? paramMap.get("genre") : new String[0];
                    for (String genre : genres) {
                        int genreID = Integer.parseInt(genre);
                        dataList.add(new Genre().setGenreID(genreID));
                    }

                    if (!paramMap.get("publisherName")[0].trim().isEmpty()) {
                        dataList.add(new Publisher().setPublisherName(paramMap.get("publisherName")[0]));
                    }

                    if (productDAO.addNewProducts(newBook, dataList.toArray())) {
                        LOGGER.log(Level.INFO, "A new Book has been added successfully!");
                        request.setAttribute("message", "A new Book has been added successfully!");
                    } else {
                        LOGGER.log(Level.SEVERE, "Failed to add new product!");
                        request.setAttribute("errorMessage", "Failed to add new product!");
                    }

                } else if (newProduct instanceof Merchandise) {
                    Merchandise newMerch = (Merchandise) newProduct;
                    //set String attributes
                    newMerch.setScaleLevel(paramMap.get("scaleLevel")[0])
                            .setMaterial(paramMap.get("material")[0])
                            .setSize(paramMap.get("size")[0]);

                    //Handle series,character,brand
                    if (!paramMap.get("seriesName")[0].trim().isEmpty()) {
                        dataList.add(new Series().setSeriesName(paramMap.get("seriesName")[0]));
                    }
                    if (!paramMap.get("characterName")[0].trim().isEmpty()) {
                        dataList.add(new OGCharacter().setCharacterName(paramMap.get("characterName")[0]));
                    }
                    if (!paramMap.get("brandName")[0].trim().isEmpty()) {
                        dataList.add(new Brand().setBrandName(paramMap.get("brandName")[0]));
                    }

                    if (productDAO.addNewProducts(newMerch, dataList.toArray())) {
                        LOGGER.log(Level.INFO, "A new Merchandise has been added successfully!");
                        request.setAttribute("message", "A new Merchandise has been added successfully!");
                    } else {
                        LOGGER.log(Level.SEVERE, "Failed to add new product!");
                        request.setAttribute("errorMessage", "Failed to add new product!");
                    }
                }

                //Ensure application scope attributes are up to date
                getServletContext().setAttribute("creators", productDAO.getAllCreators());
                getServletContext().setAttribute("publishers", productDAO.getAllPublishers());
                getServletContext().setAttribute("brands", productDAO.getAllBrands());
                getServletContext().setAttribute("series", productDAO.getAllSeries());
                getServletContext().setAttribute("characters", productDAO.getAllCharacters());

            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
                request.setAttribute("errorMessage", "Failed to add new product due to:<br>" + e.toString());
            }
        }

        //Forward to view
        request.getRequestDispatcher("productInventoryManagement.jsp").forward(request, response);
    }

    private void manageStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //Prevent unauthorized access
        HttpSession session = request.getSession();
        Account currentAccount = (Account) (session.getAttribute("account"));
        if (currentAccount == null || !currentAccount.getRole().equals("admin")) {
            response.sendRedirect("login.jsp");
            return;
        }

        String productID = request.getParameter("id");
        String action = request.getParameter("action");

        try {
            int id = Integer.parseInt(productID);
            //Handle null
            if (action == null) {
                throw new Exception("Cannot identify action!");
            }

            //Resolve target status by action
            //true = active, false = inactive
            String statusString = action.equalsIgnoreCase("activate") ? "true" : action.equalsIgnoreCase("deactivate") ? "false" : "";

            //Execute activation or deactivation
            boolean transactionState = productDAO.changeProductStatus(id, Boolean.parseBoolean(statusString));

            //Send meaningful messages to view based on transactionState
            // true = success, false = failed
            String message = transactionState ? "The product has been " + action + "d" + " successfully"
                    : "Failed to " + action + " the product!";
            request.setAttribute(transactionState ? "successfulMessage" : "failedMessage", message);

            request.getRequestDispatcher("manageProductList").forward(request, response);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error: " + e.getMessage(), e);
            request.setAttribute("errorMessage", "An error occurred while changing product status: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Manages product-related operations";
    }// </editor-fold>
}
