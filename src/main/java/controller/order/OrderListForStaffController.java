/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.order;

import dao.OrderDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Account;
import model.OrderInfo;
import model.Shipper;

/**
 *
 * @author Macbook
 */
@WebServlet(name = "OrderListForStaffController", urlPatterns = {"/OrderListForStaffController"})
public class OrderListForStaffController extends HttpServlet {

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
            out.println("<title>Servlet OrderListForStaffController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet OrderListForStaffController at " + request.getContextPath() + "</h1>");
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
        OrderDAO orderDAO = new OrderDAO();
        List<OrderInfo> orderList = null;
        Map<Integer, Account> customerMap = new HashMap<>(); // Lưu orderID -> Account
        List<Shipper> shipperList = new ArrayList<>();
        String status = request.getParameter("status");
        try {
            orderList = orderDAO.getAllOrders();
            for (OrderInfo order : orderList) {
                Account customer = orderDAO.getInfoCustomerByOrderID(order.getOrderID());
                if (customer != null) {
                    customerMap.put(order.getOrderID(), customer); // Lưu vào Map
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderListForStaffController.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (status != null && !status.isEmpty()) {
            try {
                orderList = orderDAO.getOrdersByStatus(status);
            } catch (SQLException ex) {
                Logger.getLogger(OrderListForStaffController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        shipperList = orderDAO.getAllShippers();
        request.setAttribute("status", status);
        request.setAttribute("shipperList", shipperList);
        request.setAttribute("orderList", orderList);
        request.setAttribute("customerMap", customerMap); // Gửi Map sang JSP
        request.getRequestDispatcher("OrderListForStaffView.jsp").forward(request, response);
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
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        int shipperID = Integer.parseInt(request.getParameter("shipperID"));
        int orderID = Integer.parseInt(request.getParameter("orderID"));

        OrderDAO orderDAO = new OrderDAO();
        if (account == null) {
            response.sendRedirect("login");
            return;
        }
        try {
            orderDAO.updateStaffAndShipperForOrder(orderID, account.getAccountID(), shipperID);
            orderDAO.updateDeliverystatus(orderID);
            orderDAO.updateOrderstatus(orderID);
        } catch (SQLException ex) {
            Logger.getLogger(OrderListForStaffController.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.sendRedirect("OrderListForStaffController");
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
