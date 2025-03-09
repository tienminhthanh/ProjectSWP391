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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Account;
import model.OrderInfo;

/**
 *
 * @author Macbook
 */
@WebServlet(name = "OrderListForShipperController", urlPatterns = {"/OrderListForShipperController"})
public class OrderListForShipperController extends HttpServlet {

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
            out.println("<title>Servlet OrderListForShipperController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet OrderListForShipperController at " + request.getContextPath() + "</h1>");
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
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");

        try {
            List<OrderInfo> orderList = orderDAO.getOrdersByShipperID(account.getAccountID());
            List<Account> accountList = new ArrayList<>();
        // Duyệt qua từng đơn hàng để lấy thông tin khách hàng
            for (OrderInfo order : orderList) {
                Account acc = orderDAO.getAccountByShipperIDAndOrderID(order.getOrderID(), account.getAccountID());
                if (acc != null) {
                    accountList.add(acc); // Chỉ thêm nếu không null
                }
            }
            request.setAttribute("list", orderList); // Đặt dữ liệu vào requestScope
            session.setAttribute("order", orderList); // Đặt dữ liệu vào requestScope
             // Đặt dữ liệu vào requestScope
            request.setAttribute("accountList", accountList);
            request.getRequestDispatcher("OrderListForShipperView.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching order list.");
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
