/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.product;

import dao.*;
import model.*;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author anhkc
 */
@WebServlet(name = "ProductDetailsController", urlPatterns = {"/productDetails"})
public class ProductDetailsController extends HttpServlet {

    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAO();
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
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ProductDetailsController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ProductDetailsController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
        String generalCategory = request.getParameter("type");
        if (generalCategory.equals("book")) {
            getTheBook(request, response);
        } else if (generalCategory.equals("merch")) {
            getTheMerch(request, response);
        }

    }

    private void getTheBook(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String productID = request.getParameter("id");
        String currentURL = request.getRequestURL().toString() + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
        
        try {
            int id = Integer.parseInt(productID);
            
            Book requestedBook = productDAO.getBookById(id);
            HashMap<String, Creator> creatorMap = productDAO.getCreatorsOfThisProduct(id);
            List<Genre> genreList = productDAO.getGenresOfThisBook(id);
            
            String breadCrumb = String.format("<a href='home'>Home</a> > <a href='search?type=book'>Books</a> > <a href='catalog?category=%s'>%s</a> > <a href='productDetails?id=%s&type=%s'>%s</a>",
                    requestedBook.getSpecificCategory().getCategoryID(), requestedBook.getSpecificCategory().getCategoryName(), id, requestedBook.getGeneralCategory(), requestedBook.getProductName());
            
            request.setAttribute("type", requestedBook.getGeneralCategory());
            request.setAttribute("currentURL", currentURL);
            request.setAttribute("breadCrumb", breadCrumb);
            request.setAttribute("product", requestedBook);
            request.setAttribute("creatorMap", creatorMap);
            request.setAttribute("genreList", genreList);
            
            request.getRequestDispatcher("productDetails.jsp").forward(request, response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void getTheMerch(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        WORKING ON PROGRESS
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

}
