/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.product;

import dao.ProductDAO;
import dao.interfaces.IGeneralProductDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Account;
import model.product_related.Product;
import utils.Utility;

/**
 *
 * @author anhkc
 */
@WebServlet({"/ranking"})
public class ViewProductRankingController extends HttpServlet {

    private final IGeneralProductDAO productDAO = ProductDAO.getInstance();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.service(request, response); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(false);
        Account account = session != null ? (Account) session.getAttribute("account") : null;
        String role = account != null ? account.getRole() : "guest";
        switch (role.toLowerCase()) {
            case "admin":
                response.sendRedirect("listAccount");
                return;
            case "staff":
                response.sendRedirect("OrderListForStaffController");
                return;
            case "shipper":
                response.sendRedirect("OrderListForShipperController");
                return;
            case "customer":
            case "guest":
            default:
                break;
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
        String type = request.getParameter("type");

        //For redirect back to original page after logging in or adding items to cart
        String currentURL = request.getRequestURL().toString() + (request.getQueryString() != null ? "?" + request.getQueryString() : "");

        StringBuilder message = new StringBuilder();

        try {

            // Set up breadCrumb and page Title
            // Get product list
            StringBuilder breadCrumb = new StringBuilder("<a href='home'>Home</a>");
            breadCrumb.append(String.format(" > <a href='search?type=%s'>%s</a>", type, Utility.getDisplayTextBasedOnType(type)));
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
            request.setAttribute("formattedURL", Utility.formatURL(currentURL));
            request.setAttribute("type", type);
            request.setAttribute("breadCrumb", breadCrumb);
            request.setAttribute("pageTitle", "Leaderboard - " + Utility.getDisplayTextBasedOnType(type));
            request.getRequestDispatcher("productCatalog.jsp").forward(request, response);

        } catch (Exception e) {
            System.out.println(e.toString());
            request.setAttribute("errorMessage", e.toString());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
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
