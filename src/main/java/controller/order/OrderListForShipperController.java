/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.order;

import dao.NotificationDAO;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import model.Account;
import model.DeliveryOption;
import model.Notification;
import model.OrderInfo;
import model.Shipper;

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
        String status = request.getParameter("status");
        Account account = (Account) session.getAttribute("account");
        DeliveryOption delivery = new DeliveryOption();
        if (status == null || status.isEmpty()) {
            status = "shipped";
        }
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

            final String finalStatus = status;
            orderList = orderList.stream()                 
                    .filter(order -> finalStatus.equals(order.getDeliveryStatus()))
                    .collect(Collectors.toList());

            for (OrderInfo orderInfo : orderList) {
                int deliveryTimeInDays;
                delivery = orderDAO.getDeliveryOption(orderInfo.getDeliveryOptionID());
                deliveryTimeInDays = delivery.getEstimatedTime();
                Calendar calendar = Calendar.getInstance();
                Date orderDate = orderInfo.getOrderDate();
                calendar.setTime(orderDate); // Nếu orderDate là null, tránh lỗi ở đây
                calendar.add(Calendar.DAY_OF_MONTH, deliveryTimeInDays);
                Date expectedDeliveryDate = new Date(calendar.getTimeInMillis());
                orderInfo.setExpectedDeliveryDate(expectedDeliveryDate);
                System.out.println(orderInfo.getExpectedDeliveryDate());
            }

            NotificationDAO notiDAO = new NotificationDAO();
            List<Notification> listNoti = session.getAttribute("notifications") != null ? (List<Notification>) session.getAttribute("notifications") : notiDAO.getNotificationsByReceiverDESC(account.getAccountID());
            session.setAttribute("notifications", listNoti);

            request.setAttribute("list", orderList); // Đặt dữ liệu vào requestScope

            // Đặt dữ liệu vào requestScope
            request.setAttribute("accountList", accountList);
            request.setAttribute("currentStatus", status);
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
        int orderID = Integer.parseInt(request.getParameter("orderID"));
        String actionType = request.getParameter("actionType");
        Account account = new Account();
        OrderDAO orderDao = new OrderDAO();
        Shipper accShipper = null;
        try {
            account = orderDao.getShipperByOrderID(orderID); // Lấy thông tin từ DAO
            if (account instanceof Shipper) { // Kiểm tra xem có đúng là Shipper không
                accShipper = (Shipper) account;
            } else {
                System.out.println("Error: Retrieved account is not a Shipper.");
            }

        } catch (SQLException ex) {
            Logger.getLogger(OrderListForShipperController.class.getName()).log(Level.SEVERE, null, ex);
        }
        int totalDeliveries;

        try {
            if ("updateStatus".equals(actionType)) {
                String status = "delivered";
                orderDao.updateDeliverystatus(orderID, status);
                totalDeliveries = accShipper.getTotalDeliveries();
                totalDeliveries = totalDeliveries + 1;
                orderDao.updateTotalDeliveries(account.getAccountID(), totalDeliveries);
                request.setAttribute("message", "Cập nhật trạng thái đơn hàng thành công!");
            } else if ("cancelOrder".equals(actionType)) {
                String status = "canceled";
                orderDao.updateOrderstatus(orderID, status);
                String statusDeli = "delivered";
                orderDao.updateDeliverystatus(orderID, statusDeli);
                totalDeliveries = accShipper.getTotalDeliveries();
                totalDeliveries = totalDeliveries + 1;
                request.setAttribute("message", "Đơn hàng đã bị hủy!");
            }

//            accshipper = orderDao.getShipperByOrderID(orderID);
        } catch (SQLException ex) {
            Logger.getLogger(OrderListForShipperController.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.sendRedirect(request.getRequestURI());

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
