/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.home;

import controller.chat.ChatController;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.*;
import dao.*;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author anhkc
 */
@WebServlet(name = "HomeProductListController", urlPatterns = {"/home"})
public class HomepageController extends HttpServlet {

    private static final int ADMIN_ID = 1; // Admin's accountID
    private ChatDAO chatDAO;
    private static final Logger LOGGER = Logger.getLogger(ChatController.class.getName());

    @Override
    public void init() throws ServletException {
        try {
            chatDAO = new ChatDAO();
            LOGGER.info("ChatDAO initialized successfully");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize ChatDAO", e);
            throw new ServletException("Failed to initialize ChatController", e);
        }
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
            out.println("<title>Servlet HomeProductListController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet HomeProductListController at " + request.getContextPath() + "</h1>");
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
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("account") != null) {
            try {
                Account account = (Account) session.getAttribute("account");
                switch (account.getRole()) {
                    case "staff":
                        response.sendRedirect("dashboard.jsp");
                        break;
                    case "shipper":
                        response.sendRedirect("shipperDashboard.jsp");
                        break;
                    case "admin":
                        response.sendRedirect("listAccount"); // Điều hướng đến danh sách tài khoản
                        break;
                    case "customer":
                        getChatList(request, response);
                        break;
                }
                if (!account.getRole().equals("customer")) {
                    return;
                }
            } catch (SQLException ex) {
                Logger.getLogger(HomepageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try {
            ProductDAO productDAO = new ProductDAO();
            List<Product> productList = productDAO.get10RandomActiveProducts("book");

            if (productList.isEmpty()) {
                throw new Exception("Found no products in the catalog!");
            }

            VoucherDAO vDao = new VoucherDAO();
            List<Voucher> listVoucher = vDao.getListVoucher();
            request.setAttribute("listVoucher", listVoucher);

            request.setAttribute("productList", productList);
            request.getRequestDispatcher("home.jsp").forward(request, response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }

    }

    private void getChatList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        Account account = (Account) request.getSession().getAttribute("account");
//        if (account == null) {
//            LOGGER.warning("User not logged in, redirecting to login");
//            response.sendRedirect("login"); // Giả định có trang login
//            return;
//        }
        int userID = account.getAccountID();

        if (userID == ADMIN_ID) {
            // Admin: Hiển thị danh sách khách hàng và cuộc chat đã chọn
            Set<Integer> customerIDs = chatDAO.getAdminChatCustomers();
            request.setAttribute("customerIDs", customerIDs.isEmpty() ? null : customerIDs);

            String customerIDParam = request.getParameter("customerID");
            if (customerIDParam != null && !customerIDParam.isEmpty()) {
                int customerID = Integer.parseInt(customerIDParam);
                List<Chat> chats = chatDAO.getChatsBetweenUsers(ADMIN_ID, customerID);
                request.setAttribute("chats", chats.isEmpty() ? null : chats);
                request.setAttribute("selectedCustomerID", customerID);
            }
        } else {
            // Customer: Chỉ hiển thị chat với admin
            List<Chat> chats = chatDAO.getChatsBetweenUsers(userID, ADMIN_ID);
            request.setAttribute("chats", chats.isEmpty() ? null : chats);
        }
//            LOGGER.log(Level.INFO, "Forwarding to chat.jsp for accountID: {0}", userID);
//            request.getRequestDispatcher("/home").forward(request, response);

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
