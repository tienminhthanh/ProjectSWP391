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
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import model.Account;
import model.DeliveryOption;
import model.OrderInfo;

/**
 *
 * @author Macbook
 */
@WebServlet(name = "OrderListController", urlPatterns = {"/OrderListController"})
public class OrderListController extends HttpServlet {

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
            out.println("<title>Servlet OrderListController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet OrderListController at " + request.getContextPath() + "</h1>");
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
        String status = request.getParameter("status");
        DeliveryOption delivery = new DeliveryOption();
        String vnp_TxnRef = request.getParameter("vnp_TxnRef");
        String vnp_TransactionNo = request.getParameter("vnp_TransactionNo");
        String vnp_TransactionDate = request.getParameter("vnp_PayDate");
        String vnp_TransactionStatus = request.getParameter("vnp_TransactionStatus");
        Object orderId = session.getAttribute("orderIdNew");
        int orderID =0;
        if (orderId != null) {
            orderID = (int) orderId; // Ép kiểu nếu biết chắc kiểu dữ liệu
       
        }
        if (vnp_TxnRef != null && vnp_TransactionNo != null) {
            try {
                orderDAO.updateTransactionNoByTxnRef(vnp_TransactionNo, vnp_TxnRef);
                orderDAO.updateTransactionDateByOrderID(vnp_TransactionDate, vnp_TransactionStatus, vnp_TxnRef);
           
                orderDAO.updatepaymentStatusByOrderID(orderID);
            } catch (SQLException ex) {
                Logger.getLogger(OrderListController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            List<OrderInfo> orderList = orderDAO.getOrdersByCustomerID(account.getAccountID());
            if (status == null || status.isEmpty()) {
                status = "pending";
            }
            if (!status.isEmpty()) {
                final String finalStatus = status;
                orderList = orderList.stream()
                        .filter(order -> finalStatus.equals(order.getOrderStatus().toLowerCase()))
                        .collect(Collectors.toList());
            }
            request.setAttribute("list", orderList); // Đặt dữ liệu vào requestScope
            for (OrderInfo orderInfo : orderList) {
                int deliveryTimeInDays;
                OrderInfo info = orderDAO.getOrderByID(orderInfo.getOrderID(), orderInfo.getCustomerID());

                delivery = orderDAO.getDeliveryOption(orderInfo.getDeliveryOptionID());
                deliveryTimeInDays = delivery.getEstimatedTime();
                Calendar calendar = Calendar.getInstance();
                Date orderDate = orderInfo.getOrderDate();
                calendar.setTime(orderDate); // Nếu orderDate là null, tránh lỗi ở đây
                calendar.add(Calendar.DAY_OF_MONTH, deliveryTimeInDays);
                Date expectedDeliveryDate = new Date(calendar.getTimeInMillis());
                orderInfo.setExpectedDeliveryDate(expectedDeliveryDate);
                System.out.println(orderInfo.getExpectedDeliveryDate());
                orderInfo.setOrderProductList(info.getOrderProductList());
            }

            request.setAttribute("currentStatus", status);
            // Chuyển hướng đến OrderListView.jsp

            request.getRequestDispatcher("OrderListView.jsp").forward(request, response);
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
