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
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

/**
 *
 * @author anhkc
 */
@WebServlet({"/manageProductList", "/manageProductDetails", "/updateProduct", "/addProduct", "/changeProductStatus"})
public class ProductManagementController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ProductManagementController.class.getName());
    private ProductDAO productDAO;
    private OrderDAO orderDAO;
    private EventDAO eDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        productDAO = new ProductDAO();
        orderDAO = new OrderDAO();
        eDAO = new EventDAO();
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

            Product requestedProduct = productDAO.callGetProductByTypeAndId(type, id);
            if (requestedProduct == null) {
                request.setAttribute("message", "Cannot retrieve information of productID=" + id);
                request.getRequestDispatcher("manageProductList").forward(request, response);
            } else {
                //Format linebreak for html display
                requestedProduct.setDescription(requestedProduct.getDescription().replaceAll("\r\n|\r|\n", "<br>"));

                //Get and creators
                HashMap<String, Creator> creatorMap = productDAO.getCreatorsOfThisProduct(id);
                request.setAttribute("creatorMap", creatorMap);

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
        request.setAttribute("formAction", "update");
        request.getRequestDispatcher("productInventoryManagement.jsp").forward(request, response);

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
            StringBuilder message = new StringBuilder();

            try {
                //Instantiate newProduct
                Product newProduct = action.equals("addBook") ? new Book()
                        : action.equals("addMerch") ? new Merchandise()
                        : new Product();

                newProduct.setProductName(paramMap.get("productName")[0])
                        .setPrice(Integer.parseInt(paramMap.get("price")[0]))
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

                //Insert new product to database
                if (!productDAO.addNewProducts(newProduct)) {
                    throw new RuntimeException("Failed to add this product!");
                }
                message.append("New product has been added!\n");

                //Get ID of new product
                int productID = productDAO.getLatestProductID();
                if (productID > 0) {
                    newProduct.setProductID(productID);
                }

                //Handling creators
                String[] creatorNames = paramMap.get("creatorName");
                String[] creatorRoles = paramMap.get("creatorRole");
                for(int i = 0; i < creatorNames.length; i++) {
                    //Skip if name is empty
                    if (creatorNames[i].trim().isEmpty()) {
                        continue;
                    }

                    //Check if exist (creatorID > 0)
                    int creatorID = productDAO.getCreatorIDByNameAndRole(creatorNames[i], creatorRoles[i]);
                    //Insert to database if not exist
                    boolean isAssignable = creatorID == 0 ? productDAO.addNewCreators(new Creator()
                            .setCreatorName(creatorNames[i])
                            .setCreatorRole(creatorRoles[i]))
                            : true;

                    //Throw exception if adding creators failed
                    if (!isAssignable) {
                        throw new SQLException("Error adding creator: " + creatorNames[i] + " - " + creatorRoles[i]);
                    }

                    //Ensure IDs is valid
                    creatorID = creatorID > 0 ? creatorID : productDAO.getCreatorIDByNameAndRole(creatorNames[i], creatorRoles[i]);
                    if (creatorID > 0 && productID > 0) {
                        //Insert to junction table
                        if (!productDAO.assignCreatorsToProduct(productID, creatorID,"insert")) {
                            throw new SQLException("Error assigning creatorID " + creatorID + " to productID " + productID);
                        }
                    }
                    

                }
                
                //Ensure creatorMap in application scope is up to date
                getServletContext().setAttribute("creators", productDAO.getAllCreators());

                //Type-specific atributes
                if (newProduct instanceof Book) {
                    //Handle book-specific attributes
                    addBook(request, response, paramMap, (Book) newProduct);
                } else if (newProduct instanceof Merchandise) {
                    //Handle merch-specific attributes
                    addMerch(request, response, paramMap, (Merchandise) newProduct);
                }

            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
                request.setAttribute("errorMessage",
                        message.append("...But cannot be categorized due to:\n")
                                .append(e.toString())
                                .toString()
                                .replaceAll("\r\n|\r|\n", "<br>"));
            }
        }
        
        //Forward to view
        request.getRequestDispatcher("productInventoryManagement.jsp").forward(request, response);
    }

    private void addBook(HttpServletRequest request, HttpServletResponse response, Map<String, String[]> paramMap, Book newBook)
            throws Exception {
        //Handle genres
        String[] genres = paramMap.get("genre");
        for (String genre : genres) {
            int genreID = Integer.parseInt(genre);
            //Insert to junction table
            if (!productDAO.assignGenresToBook(newBook.getProductID(), genreID,"insert")) {
                throw new SQLException("Error assigning genreID " + genreID + " to productID " + newBook.getProductID());
            }
        }
        
        //Handle publisher
        String publisherName = paramMap.get("publisherName")[0];
        if (!publisherName.trim().isEmpty()) {
            Publisher publisher = new Publisher();
            //Check if exist (publisherID > 0)
            int publisherID = productDAO.getPublisherIDByName(publisherName);
            //Insert to database if not exist
            boolean isAssignable = publisherID == 0 ? productDAO.addNewPublishers(publisher
                    .setPublisherName(publisherName))
                    : true;

            //If insert failed
            if (!isAssignable) {
                throw new SQLException("Error adding publisher: " + publisherName);
            }

            //Ensure ID is valid
            publisherID = publisherID > 0 ? publisherID : productDAO.getPublisherIDByName(publisherName);
            if (publisherID > 0) {
                newBook.setPublisher(publisher.setPublisherID(publisherID));
            }
        
            //Ensure publisherMap in application scope is up to date
            getServletContext().setAttribute("publishers", productDAO.getAllPublishers());
        }
        
        //setDuration
        newBook.setDuration(paramMap.get("duration")[0]);

        //Update Book
        if (productDAO.updateBooks(newBook)) {
            request.setAttribute("message", "A new Book has been added successfully!");
        } else {
            request.setAttribute("errorMessage", "New product has been added, but cannot be categorized at the moment!");
        }

    }

    private void addMerch(HttpServletRequest request, HttpServletResponse response, Map<String, String[]> paramMap, Merchandise newMerch)
            throws Exception {

        //set String attributes
        newMerch.setScaleLevel(paramMap.get("scaleLevel")[0])
                .setMaterial(paramMap.get("material")[0])
                .setSize(paramMap.get("size")[0]);

        //Handle series,character,brand
        String seriesName = paramMap.get("seriesName")[0];
        String characterName = paramMap.get("characterName")[0];
        String brandName = paramMap.get("brandName")[0];

        if (!seriesName.trim().isEmpty()) {
            //Check if exist (seriesID > 0)
            int id = productDAO.getSeriesIDByName(seriesName);
            //Insert to database if not exist
            boolean isAssignable = id == 0 ? productDAO.addNewMerchSeries(new Series()
                    .setSeriesName(seriesName))
                    : true;

            //If insert failed
            if (!isAssignable) {
                throw new SQLException("Error adding merch series: " + seriesName);
            }

            //Ensure ID is valid
            id = id > 0 ? id : productDAO.getSeriesIDByName(seriesName);
            if (id > 0) {
                newMerch.setSeries(new Series(id, seriesName));
            }
        
            //Ensure seriesMap in application scope is up to date
            getServletContext().setAttribute("series", productDAO.getAllSeries());
        }

        if (!characterName.trim().isEmpty()) {
            //Check if exist (characterID > 0)
            int id = productDAO.getCharacterIDByName(characterName);
            //Insert to database if not exist
            boolean isAssignable = id == 0 ? productDAO.addNewMerchCharacter(new OGCharacter()
                    .setCharacterName(characterName))
                    : true;

            //If insert failed
            if (!isAssignable) {
                throw new SQLException("Error adding merch character: " + characterName);
            }

            //Ensure ID is valid
            id = id > 0 ? id : productDAO.getCharacterIDByName(characterName);
            if (id > 0) {
                newMerch.setCharacter(new OGCharacter(id, characterName));
            }
            
            //Ensure characterMap in application scope is up to date
            getServletContext().setAttribute("characters", productDAO.getAllCharacters());
        }

        if (!brandName.trim().isEmpty()) {
            //Check if exist (brandID > 0)
            int id = productDAO.getBrandIDByName(brandName);
            //Insert to database if not exist
            boolean isAssignable = id == 0 ? productDAO.addNewMerchBrand(new Brand()
                    .setBrandName(brandName))
                    : true;

            //If insert failed
            if (!isAssignable) {
                throw new SQLException("Error adding merch brand: " + brandName);
            }

            //Ensure ID is valid
            id = id > 0 ? id : productDAO.getBrandIDByName(brandName);
            if (id > 0) {
                newMerch.setBrand(new Brand(id, brandName));
            }
            
            //Ensure brandMap in application scope is up to date
            getServletContext().setAttribute("brands", productDAO.getAllBrands());
        }

        //Update Merch
        if (productDAO.updateMerch(newMerch)) {
            request.setAttribute("message", "A new Merch has been added successfully!");
        } else {
            request.setAttribute("errorMessage", "New product has been added, but cannot be categorized at the moment!");
        }
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
