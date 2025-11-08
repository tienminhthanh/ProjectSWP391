/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.product;

import dao.BookDAO;
import dao.MerchDAO;
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
import dao.factory_product.ProductFactory;
import dao.interfaces.ISpecificProductDAO;
import java.util.AbstractList;
import utils.LoggingConfig;

/**
 *
 * @author anhkc
 */
@WebServlet(name = "UpdateProductController", urlPatterns = {"/updateProduct"})
public class UpdateProductController extends HttpServlet {

    private static final Logger LOGGER = LoggingConfig.getLogger(UpdateProductController.class);
    private static final boolean IS_MANAGEMENT = true;
//    private final ProductDAO productDAO = ProductDAO.getInstance();
    private ISpecificProductDAO productDAO;

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

            String[] creators = paramMap.get("creator");
            creators = creators != null ? creators : new String[0];
            List<Creator> creatorList = new ArrayList<>();
            if (creators.length > 0) {
                for (String crtIDStr : creators) {
                    int creatorID = Integer.parseInt(crtIDStr);
                    Creator creator = new Creator().setCreatorID(creatorID).setCreatorName("");
                    if (creIDSet.contains(String.valueOf(creatorID))) {
                        //Mark if associated
                        associatedCounter++;
                        creator.setCreatorName("associated");
                    }

                    creatorList.add(creator);
                }

                associatedCounter = 0;

            } else {
                //Delete all

                creatorList.add(new Creator().setCreatorID(0).setCreatorName("deleteAll"));
            }

            updatedProduct.setCreatorList(creatorList);

            //Type-specific atributes
            if (updatedProduct instanceof Book) {
                productDAO = BookDAO.getInstance();
                Book updatedBook = (Book) updatedProduct;

                updatedBook.setDuration(paramMap.get("duration")[0]);

                String[] associatedGenreIDs = paramMap.get("associatedGenreID");
                Set<String> genIDSet = associatedGenreIDs != null
                        ? new HashSet<>(Arrays.asList(associatedGenreIDs))
                        : new HashSet<>();
                String[] genres = paramMap.get("genre");
                List<Genre> genreList = new ArrayList<>();
                genres = genres != null ? genres : new String[0];
                if (genres.length > 0) {
                    for (String genIdStr : genres) {
                        int genreID = Integer.parseInt(genIdStr);
                        Genre genre = new Genre().setGenreID(genreID).setGenreName("");
                        if (genIDSet.contains(String.valueOf(genreID))) {
                            //Mark if associated
                            associatedCounter++;
                            genre.setGenreName("associated");
                        }

                        genreList.add(genre);
                    }

                    associatedCounter = 0;

                } else {
                    //Delete all
                    genreList.add(new Genre().setGenreID(0).setGenreName("deleteAll"));
                }
                updatedBook.setGenreList(genreList);

                updatedBook.setPublisher(new Publisher().setPublisherID(Integer.parseInt(paramMap.get("publisher")[0])));

                if (productDAO.updateProduct(updatedBook)) {
                    LOGGER.log(Level.INFO, "The Book has been updated successfully!");
                    request.setAttribute("message", "The Book has been updated successfully!");
                } else {
                    LOGGER.log(Level.SEVERE, "Failed to update the product!");
                    request.setAttribute("errorMessage", "Failed to update the product!");
                }

            } else if (updatedProduct instanceof Merchandise) {
                productDAO = MerchDAO.getInstance();
                Merchandise updatedMerch = (Merchandise) updatedProduct;
                //set String attributes
                updatedMerch.setScaleLevel(paramMap.get("scaleLevel")[0])
                        .setMaterial(paramMap.get("material")[0])
                        .setSize(paramMap.get("size")[0]);

                //Handle series,character,brand
                updatedMerch.setSeries(new Series().setSeriesID(Integer.parseInt(paramMap.get("series")[0])));
                updatedMerch.setCharacter(new OGCharacter().setCharacterID(Integer.parseInt(paramMap.get("character")[0])));
                updatedMerch.setBrand(new Brand().setBrandID(Integer.parseInt(paramMap.get("brand")[0])));

                if (productDAO.updateProduct(updatedMerch)) {
                    LOGGER.log(Level.INFO, "The Merch has been updated successfully!");
                    request.setAttribute("message", "The Merch has been updated successfully!");
                } else {
                    LOGGER.log(Level.SEVERE, "Failed to update the product!");
                    request.setAttribute("errorMessage", "Failed to update the product!");
                }
            }

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
