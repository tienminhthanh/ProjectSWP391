/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.product;

import dao.ProductDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Account;
import model.Book;
import model.Brand;
import model.Category;
import model.Creator;
import model.Genre;
import model.Merchandise;
import model.OGCharacter;
import model.Product;
import model.Publisher;
import model.Series;
import dao.provider.product.ProductFactory;
import utils.LoggingConfig;

/**
 *
 * @author anhkc
 */
@WebServlet(name = "UpdateProductController", urlPatterns = {"/updateProduct"})
public class UpdateProductController extends HttpServlet {

    private static final Logger LOGGER = LoggingConfig.getLogger(UpdateProductController.class);
    private static final boolean IS_MANAGEMENT = true;
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
        //Retrieve product info from database based on id and generalCategory (product type)
        String productID = request.getParameter("id");
        String type = request.getParameter("type");
        try {
            int id = Integer.parseInt(productID);
            Product requestedProduct = ProductFactory.getProduct(type, id, IS_MANAGEMENT);
            if (requestedProduct == null) {
                request.setAttribute("message", "Cannot retrieve information of productID=" + id);
                request.getRequestDispatcher("manageProductList").forward(request, response);
            } else {
                //Format linebreak for html display
                requestedProduct.setDescription(requestedProduct.getDescription().replaceAll("\r\n|\r|\n", "<br>"));

                //Get and creators
                Collections.sort(requestedProduct.getCreatorList(), Comparator.comparing(c -> c.getCreatorName()));

                request.setAttribute("product", requestedProduct);
                request.setAttribute("type", type);
                //Set the formAction to ensure the page show the correct form
                request.setAttribute("formAction", "update");
                request.getRequestDispatcher("productInventoryManagement.jsp").forward(request, response);
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An error occurred while fetching the product: {0}", e.toString());
            request.setAttribute("errorMessage", "An error occurred while fetching the product: " + e.getMessage());
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
        String action = request.getParameter("action");

        Map<String, String[]> paramMap = request.getParameterMap() != null ? request.getParameterMap() : new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<Object> dataList = new ArrayList<>();
        List<Object> tempObjects = new ArrayList<>();
        int associatedCounter = 0;

        Account currentAccount = (Account) (request.getSession().getAttribute("account"));

        try {
            action = action != null ? action : "";
            //Instantiate updatedProduct
            Product updatedProduct = action.equals("updateBook") ? new Book()
                    : action.equals("updateMerch") ? new Merchandise()
                    : new Product();

            updatedProduct.setProductID(Integer.parseInt(paramMap.get("productID")[0]))
                    .setProductName(paramMap.get("productName")[0])
                    .setPrice(1000 * Double.parseDouble(paramMap.get("price")[0])) //Multiply prices by 1000
                    .setStockCount(Integer.parseInt(paramMap.get("stockCount")[0]))
                    .setSpecificCategory(new Category().setCategoryID(Integer.parseInt(paramMap.get("category")[0])))
                    .setSpecialFilter(paramMap.get("specialFilter")[0])
                    .setDescription(paramMap.get("description")[0])
                    .setReleaseDate(LocalDate.parse(paramMap.get("releaseDate")[0], formatter))
                    .setIsActive(Boolean.parseBoolean(paramMap.get("isActive")[0]))
                    .setKeywords(paramMap.get("keywords")[0])
                    .setImageURL(paramMap.get("imageURL")[0])
                    .setAdminID(currentAccount.getAccountID())
                    .setGeneralCategory(action.equals("updateBook") ? "book"
                            : action.equals("updateMerch") ? "merch" : "unset");

            //Handling creators
            String[] associatedCreatorIDs = paramMap.get("associatedCreatorID");
            Set<String> creIDSet = associatedCreatorIDs != null
                    ? new HashSet<>(Arrays.asList(associatedCreatorIDs))
                    : new HashSet<>();
            String[] creatorNames = paramMap.get("creatorName");
            String[] creatorRoles = paramMap.get("creatorRole");

            for (int i = 0; i < creatorNames.length; i++) {
                //Skip if name is empty
                if (creatorNames[i].trim().isEmpty()) {
                    continue;
                }

                Creator creator = new Creator();
                int creatorID = productDAO.getCreatorIDByNameAndRole(creatorNames[i], creatorRoles[i]);

                if (creatorID == 0) {
                    //New creator -> add new + associate
                    creator.setCreatorName(creatorNames[i]).setCreatorRole(creatorRoles[i]);
                } else if (creIDSet.contains(String.valueOf(creatorID))) {
                    //Existing creator that already associated -> mark them
                    associatedCounter++;
                    creator.setCreatorID(creatorID).setCreatorName(creatorNames[i] + "(associated)").setCreatorRole(creatorRoles[i]);
                } else {
                    //Existing creator that not associated yet-> associate
                    creator.setCreatorID(creatorID).setCreatorName(creatorNames[i]).setCreatorRole(creatorRoles[i]);
                }

                tempObjects.add(creator);

            }
            //Only add if there are changes
            if (associatedCounter < creIDSet.size() || tempObjects.size() != creIDSet.size()) {
                dataList.addAll(tempObjects);
            }

            //Reset after done with an entity
            tempObjects.clear();
            associatedCounter = 0;

            //Type-specific atributes
            if (updatedProduct instanceof Book) {
                Book updatedBook = (Book) updatedProduct;
                updatedBook.setDuration(paramMap.get("duration")[0]);

                String[] associatedGenreIDs = paramMap.get("associatedGenreID");
                Set<String> genIDSet = associatedGenreIDs != null
                        ? new HashSet<>(Arrays.asList(associatedGenreIDs))
                        : new HashSet<>();
                String[] genres = paramMap.get("genre");
                genres = genres != null ? genres : new String[0];
                if (genres.length > 0) {
                    for (String genIdStr : genres) {
                        int genreID = Integer.parseInt(genIdStr);
                        Genre genre = new Genre().setGenreID(genreID).setGenreName("");
                        if (genIDSet.contains(String.valueOf(genreID))) {
                            //Mark if associated
                            associatedCounter++;
                            genre.setGenreName("(associated)");
                        }
                        tempObjects.add(genre);
                    }

                    //Only add if there are changes
                    if (associatedCounter < genIDSet.size() || tempObjects.size() != genIDSet.size()) {
                        dataList.addAll(tempObjects);
                    }
                } else {
                    //Delete all
                    dataList.add(new Genre().setGenreID(Integer.parseInt(associatedGenreIDs[0])).setGenreName("(deleteAll)"));
                }

                String associatedPublisherID = paramMap.get("associatedPublisherID")[0];
                associatedPublisherID = associatedPublisherID != null ? associatedPublisherID : "";
                if (!paramMap.get("publisherName")[0].trim().isEmpty()) {

                    Publisher publisher = new Publisher().setPublisherName(paramMap.get("publisherName")[0]);
                    int publisherID = productDAO.getPublisherIDByName(paramMap.get("publisherName")[0]);

                    if (publisherID > 0) {
                        //Set if exist
                        publisher.setPublisherID(publisherID).setPublisherName(paramMap.get("publisherName")[0]);
                    }

                    dataList.add(publisher);
                }

                if (productDAO.updateProducts(updatedBook, dataList.toArray())) {
                    LOGGER.log(Level.INFO, "The Book has been updated successfully!");
                    request.setAttribute("message", "The Book has been updated successfully!");
                } else {
                    LOGGER.log(Level.SEVERE, "Failed to update the product!");
                    request.setAttribute("errorMessage", "Failed to update the product!");
                }

            } else if (updatedProduct instanceof Merchandise) {
                Merchandise updatedMerch = (Merchandise) updatedProduct;
                //set String attributes
                updatedMerch.setScaleLevel(paramMap.get("scaleLevel")[0])
                        .setMaterial(paramMap.get("material")[0])
                        .setSize(paramMap.get("size")[0]);

                //Handle series,character,brand
                String associatedMerchAttrID = paramMap.get("associatedSeriesID")[0];
                associatedMerchAttrID = associatedMerchAttrID != null ? associatedMerchAttrID : "";
                if (!paramMap.get("seriesName")[0].trim().isEmpty()) {
                    Series series = new Series().setSeriesName(paramMap.get("seriesName")[0]);
                    int id = productDAO.getSeriesIDByName(paramMap.get("seriesName")[0]);

                    if (id > 0) {
                        series.setSeriesID(id);
                    }

                    dataList.add(series);
                }

                associatedMerchAttrID = paramMap.get("associatedCharacterID")[0];
                associatedMerchAttrID = associatedMerchAttrID != null ? associatedMerchAttrID : "";
                if (!paramMap.get("characterName")[0].trim().isEmpty()) {
                    OGCharacter character = new OGCharacter().setCharacterName(paramMap.get("characterName")[0]);
                    int id = productDAO.getCharacterIDByName(paramMap.get("characterName")[0]);

                    if (id > 0) {
                        character.setCharacterID(id);
                    }

                    dataList.add(character);
                }

                associatedMerchAttrID = paramMap.get("associatedBrandID")[0];
                associatedMerchAttrID = associatedMerchAttrID != null ? associatedMerchAttrID : "";
                if (!paramMap.get("brandName")[0].trim().isEmpty()) {
                    Brand brand = new Brand().setBrandName(paramMap.get("brandName")[0]);
                    int id = productDAO.getBrandIDByName(paramMap.get("brandName")[0]);

                    if (id > 0) {
                        brand.setBrandID(id);
                    }
                    dataList.add(brand);
                }

                if (productDAO.updateProducts(updatedMerch, dataList.toArray())) {
                    LOGGER.log(Level.INFO, "The Merch has been updated successfully!");
                    request.setAttribute("message", "The Merch has been updated successfully!");
                } else {
                    LOGGER.log(Level.SEVERE, "Failed to update the product!");
                    request.setAttribute("errorMessage", "Failed to update the product!");
                }
            }

            //Ensure application scope attributes are up to date
//            getServletContext().setAttribute("creators", productDAO.getAllCreators());
//            getServletContext().setAttribute("publishers", productDAO.getAllPublishers());
//            getServletContext().setAttribute("brands", productDAO.getAllBrands());
//            getServletContext().setAttribute("series", productDAO.getAllSeries());
//            getServletContext().setAttribute("characters", productDAO.getAllCharacters());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);

            // Retrieve and log suppressed exceptions
            Throwable[] suppressed = e.getSuppressed();
            for (Throwable sup : suppressed) {
                LOGGER.log(Level.SEVERE, "Suppressed Exception: " + sup.toString(), sup);
            }

            request.setAttribute("errorMessage", "Failed to update the product due to:<br>" + e.toString());
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
