/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.product;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author anhkc
 */
@WebServlet({"/manage-product/list", "/manage-product/details", "/manage-product/update", "/manage-product/add", "/manage-product/delete"})
public class ManageProductController extends HttpServlet {

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
            case "/manage-product/list":
                manageList(request, response);
                break;
            case "/manage-product/details":
                manageDetails(request, response);
                break;
            case "/manage-product/update":
                manageUpdate(request, response);
                break;
            case "/manage-product/add":
                manageAdd(request, response);
                break;
            case "/manage-product/delete":
                manageDelete(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid product management url: " + path);
                break;
        }
    }

    private void manageList(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head><title>Product List</title></head>");
            out.println("<body>");
            out.println("<h1>Product List</h1>");
            out.println("<p>Displaying all products here...</p>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private void manageDetails(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head><title>Product Details</title></head>");
            out.println("<body>");
            out.println("<h1>Product Details</h1>");
            out.println("<p>Showing details for a specific product...</p>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private void manageUpdate(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head><title>Update Product</title></head>");
            out.println("<body>");
            out.println("<h1>Update Product</h1>");
            out.println("<p>Updating a product...</p>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private void manageAdd(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head><title>Add Product</title></head>");
            out.println("<body>");
            out.println("<h1>Add Product</h1>");
            out.println("<p>Adding a new product...</p>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private void manageDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head><title>Delete Product</title></head>");
            out.println("<body>");
            out.println("<h1>Delete Product</h1>");
            out.println("<p>Deleting a product...</p>");
            out.println("</body>");
            out.println("</html>");
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