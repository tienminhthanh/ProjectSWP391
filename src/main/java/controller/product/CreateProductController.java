/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.product;

import dao.BookDAO;
import dao.MerchDAO;
import dao.ProductDAO;
import dao.interfaces.ISpecificProductDAO;
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
import utils.LoggingConfig;

/**
 *
 * @author anhkc
 */
@WebServlet({"/addProduct"})
public class CreateProductController extends HttpServlet {

    private static final Logger LOGGER = LoggingConfig.getLogger(CreateProductController.class);
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
            String[] creators = paramMap.get("creator");
            List<Creator> creatorList = new ArrayList<>();
            if (creators != null) {
                for (String creatorIdStr : creators) {
                    //Parse to ensure genIdStr is an integer
                    int creatorID = Integer.parseInt(creatorIdStr);
                    //Assign id if valid
                    Creator creator = new Creator().setCreatorID(creatorID);
                    creatorList.add(creator);
                }
            }
            newProduct.setCreatorList(creatorList);

            //Type-specific atributes
            if (newProduct instanceof Book) {
                productDAO = BookDAO.getInstance();
                Book newBook = (Book) newProduct;

                //Set duration
                newBook.setDuration(paramMap.get("duration")[0]);

                //Handle genres
                String[] genres = paramMap.get("genre");
                List<Genre> genreList = new ArrayList<>();
                if (genres != null) {
                    for (String genIdStr : genres) {
                        //Parse to ensure genIdStr is an integer
                        int genreID = Integer.parseInt(genIdStr);
                        //Assign id if valid
                        Genre genre = new Genre().setGenreID(genreID);
                        genreList.add(genre);
                    }
                }
                newBook.setGenreList(genreList);

                //Hanlde publisher
                newBook.setPublisher(new Publisher().setPublisherID(Integer.parseInt(paramMap.get("publisher")[0])));

                //Add new book and its related data
                if (productDAO.addNewProduct(newBook)) {
                    LOGGER.log(Level.INFO, "A new Book has been added successfully!");
                    request.setAttribute("message", "A new Book has been added successfully!");
                } else {
                    LOGGER.log(Level.SEVERE, "Failed to add new product!");
                    request.setAttribute("errorMessage", "Failed to add new product!");
                }

            } else if (newProduct instanceof Merchandise) {
                productDAO = MerchDAO.getInstance();
                Merchandise newMerch = (Merchandise) newProduct;
                //set String attributes
                newMerch.setScaleLevel(paramMap.get("scaleLevel")[0])
                        .setMaterial(paramMap.get("material")[0])
                        .setSize(paramMap.get("size")[0]);

                //Handle series,character,brand
                newMerch.setSeries(new Series().setSeriesID(Integer.parseInt(paramMap.get("series")[0])));
                newMerch.setCharacter(new OGCharacter().setCharacterID(Integer.parseInt(paramMap.get("character")[0])));
                newMerch.setBrand(new Brand().setBrandID(Integer.parseInt(paramMap.get("brand")[0])));

                //Add new merch and its related data
                if (productDAO.addNewProduct(newMerch)) {
                    LOGGER.log(Level.INFO, "A new Merchandise has been added successfully!");
                    request.setAttribute("message", "A new Merchandise has been added successfully!");
                } else {
                    LOGGER.log(Level.SEVERE, "Failed to add new product!");
                    request.setAttribute("errorMessage", "Failed to add new product!");
                }
            }


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
