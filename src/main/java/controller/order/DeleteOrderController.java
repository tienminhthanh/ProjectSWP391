/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.order;

import controller.extend.VNPayRefundAPI;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import model.OrderInfo;

/**
 *
 * @author Macbook
 */
@WebServlet(name = "DeleteOrderContronller", urlPatterns = {"/DeleteOrderController"})
public class DeleteOrderController extends HttpServlet {

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
            out.println("<title>Servlet DeleteOrderContronller</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DeleteOrderContronller at " + request.getContextPath() + "</h1>");
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
        processRequest(request, response);
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
        OrderDAO orderDAO = new OrderDAO();
        String orderID = request.getParameter("id");
        OrderInfo orderInfo = new OrderInfo();
        int id = Integer.parseInt(orderID);
        try {

            System.out.println(id);
            String status = "canceled";
            orderDAO.restoreProductStockByOrderID(id);
            orderDAO.updateOrderstatus(id, status);
            orderInfo = orderDAO.getTransactionInfoByOrderID(id);
            if (orderInfo.getPaymentMethod().equals("online")) {
                // Nếu thanh toán online, lưu thông báo vào session
                HttpSession session = request.getSession();
                session.setAttribute("refundMessage",
                        "A refund request has been sent to the bank for the order.<br>"
                        + "Order ID: " + orderInfo.getOrderID() + ".<br>"
                        + "Transaction Reference: " + orderInfo.getVnp_TxnRef() + ".<br>"
                        + "Transaction Number: " + orderInfo.getVnp_TransactionNo() + ".<br>"
                        + "Transaction Date: " + orderInfo.getVnp_TransactionDate() + ".<br>"
                        + "Total Amount: " + orderInfo.getPreVoucherAmount() + " đ.<br>"
                        + "Please wait 2-3 days for processing.<br>"
                        + "<strong>We sincerely apologize for any inconvenience caused.</strong>");
                // Điều hướng về danh sách đơn hàng
                response.sendRedirect("OrderListController");
                return;
            }

            request.setAttribute("Transaction", orderInfo);

        } catch (SQLException ex) {
            Logger.getLogger(DeleteOrderController.class.getName()).log(Level.SEVERE, null, ex);
            request.setAttribute("errorMessage1", "Something went wrong. Please try again later!");
//            response.sendRedirect("error.jsp");
            request.getRequestDispatcher("OrderDetailController?id=" + id).forward(request, response);
        }
        response.sendRedirect("OrderListController");

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
