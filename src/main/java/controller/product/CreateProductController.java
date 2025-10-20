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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Account;
import model.product_related.Book;
import model.product_related.Brand;
import model.product_related.Category;
import model.product_related.Creator;
import model.product_related.Genre;
import model.product_related.Merchandise;
import model.product_related.OGCharacter;
import model.product_related.Product;
import model.product_related.Publisher;
import model.product_related.Series;
import utils.LoggingConfig;

/**
 *
 * @author anhkc
 */
@WebServlet(name = "AddProductController", urlPatterns = {"/addProduct"})
public class CreateProductController extends HttpServlet {

    private static final Logger LOGGER = LoggingConfig.getLogger(CreateProductController.class);
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
        //Ensure only forms for adding products are shown
        request.setAttribute("formAction", "add");
        //Forward to view
        request.getRequestDispatcher("productInventoryManagement.jsp").forward(request, response);
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

        //After form submission
        Map<String, String[]> paramMap = request.getParameterMap() != null ? request.getParameterMap() : new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<Object> dataList = new ArrayList<>();
        
        Account currentAccount = (Account) (request.getSession().getAttribute("account"));

        try {
            //Instantiate newProduct
            Product newProduct = action.equals("addBook") ? new Book()
                    : action.equals("addMerch") ? new Merchandise()
                    : new Product();

            newProduct.setProductName(paramMap.get("productName")[0])
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

                //Instantitate creator without id
                Creator creator = new Creator().setCreatorName(creatorNames[i]).setCreatorRole(creatorRoles[i]);

                //Check if exist
                int id = productDAO.getCreatorIDByNameAndRole(creatorNames[i], creatorRoles[i]);

                //If exist => assign the found id to creator
                if (id > 0) {
                    creator.setCreatorID(id);
                }

                //Add to dataList
                dataList.add(creator);

            }

            //Type-specific atributes
            if (newProduct instanceof Book) {
                Book newBook = (Book) newProduct;

                //Set duration
                newBook.setDuration(paramMap.get("duration")[0]);

                //Handle genres
                String[] genres = paramMap.get("genre");
                if (genres != null) {
                    for (String genIdStr : genres) {
                        //Parse to ensure genIdStr is an integer
                        int genreID = Integer.parseInt(genIdStr);
                        //Assign id if valid
                        Genre genre = new Genre().setGenreID(genreID);
                        //Add to dataList
                        dataList.add(genre);
                    }
                }

                //Hanlde publisher
                if (!paramMap.get("publisherName")[0].trim().isEmpty()) {
                    //Instantiate publisher without id
                    Publisher publisher = new Publisher().setPublisherName(paramMap.get("publisherName")[0]);

                    //Check if exist
                    int id = productDAO.getPublisherIDByName(paramMap.get("publisherName")[0]);

                    //If exist => assign id to publisher
                    if (id > 0) {
                        publisher.setPublisherID(id);
                    }

                    //Add to dataList
                    dataList.add(publisher);
                }

                //Add new book and its related data
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
                    //Instantite series without id
                    Series series = new Series().setSeriesName(paramMap.get("seriesName")[0]);
                    //Check if exist
                    int id = productDAO.getSeriesIDByName(paramMap.get("seriesName")[0]);
                    //If exist => assign id to series
                    if (id > 0) {
                        series.setSeriesID(id);
                    }
                    //Add to dataList
                    dataList.add(series);
                }
                if (!paramMap.get("characterName")[0].trim().isEmpty()) {
                    //Instantiate character without id
                    OGCharacter character = new OGCharacter().setCharacterName(paramMap.get("characterName")[0]);
                    //Check if exist
                    int id = productDAO.getCharacterIDByName(paramMap.get("characterName")[0]);
                    //If exist => assign id to character
                    if (id > 0) {
                        character.setCharacterID(id);
                    }
                    //Add to dataList
                    dataList.add(character);
                }
                if (!paramMap.get("brandName")[0].trim().isEmpty()) {
                    //Instantiate brand without id
                    Brand brand = new Brand().setBrandName(paramMap.get("brandName")[0]);
                    //Check if exist
                    int id = productDAO.getBrandIDByName(paramMap.get("brandName")[0]);
                    //If exist = > assign id to brand
                    if (id > 0) {
                        brand.setBrandID(id);
                    }
                    //Add to dataList
                    dataList.add(brand);
                }

                //Add new merch and its related data
                if (productDAO.addNewProducts(newMerch, dataList.toArray())) {
                    LOGGER.log(Level.INFO, "A new Merchandise has been added successfully!");
                    request.setAttribute("message", "A new Merchandise has been added successfully!");
                } else {
                    LOGGER.log(Level.SEVERE, "Failed to add new product!");
                    request.setAttribute("errorMessage", "Failed to add new product!");
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

            request.setAttribute("errorMessage", "Failed to add new product due to:<br>" + e.toString());
        }

        //Forward to view
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
