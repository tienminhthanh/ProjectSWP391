/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.order;

import dao.NotificationDAO;
import dao.OrderDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            response.sendRedirect("login");
            return;
        }
        OrderDAO orderDAO = new OrderDAO();

        List<OrderInfo> orderList = null;
        OrderInfo orderInfo = new OrderInfo();
        Map<Integer, Account> customerMap = new HashMap<>(); // Lưu orderID -> Account
        List<Shipper> shipperList = new ArrayList<>();
        DeliveryOption delivery = new DeliveryOption();

        String status = request.getParameter("status");
        if (status == null || status.isEmpty()) {
            status = "pending";
        }
        try {
            orderList = orderDAO.getAllOrders();
            final String finalStatus = status;
            orderList = orderList.stream()
                    .filter(order -> finalStatus.equals(order.getOrderStatus()))
                    .collect(Collectors.toList());
            for (OrderInfo order : orderList) {
                Account customer = orderDAO.getInfoCustomerByOrderID(order.getOrderID());
                if (customer != null) {
                    customerMap.put(order.getOrderID(), customer); // Lưu vào Map
                }
                orderInfo = orderDAO.getOrderByID(order.getOrderID(), customer.getAccountID());
                int deliveryTimeInDays;

                delivery = orderDAO.getDeliveryOption(order.getDeliveryOptionID());
                deliveryTimeInDays = delivery.getEstimatedTime();
                Calendar calendar = Calendar.getInstance();
                Date orderDate = order.getOrderDate();
                calendar.setTime(orderDate); // Nếu orderDate là null, tránh lỗi ở đây
                calendar.add(Calendar.DAY_OF_MONTH, deliveryTimeInDays);
                Date expectedDeliveryDate = new Date(calendar.getTimeInMillis());
                order.setExpectedDeliveryDate(expectedDeliveryDate);

            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderListForStaffController.class.getName()).log(Level.SEVERE, null, ex);
        }
        shipperList = orderDAO.getAllShippers();
        request.setAttribute("currentStatus", status);
        request.setAttribute("shipperList", shipperList);
        request.setAttribute("orderList", orderList);
        request.setAttribute("orderInfo", orderList);
        request.setAttribute("account", account);
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
        NotificationDAO notificationDAO = new NotificationDAO();
        
        OrderDAO orderDAO = new OrderDAO();
        if (account == null) {
            response.sendRedirect("login");
            return;
        }
        try {
            String status = "Shipped";
            orderDAO.updateStaffAndShipperForOrder(orderID, account.getAccountID(), shipperID);
            orderDAO.updateDeliverystatus(orderID, status);
            orderDAO.updateOrderstatus(orderID, status);
            session.setAttribute("orderID", orderID);
            System.out.println();
            // Send notification to shipper
//            if (request.getParameter("shipperID") != null) {
            Notification notification = new Notification();
            notification.setSenderID(1); // Staff who assigned the order
            notification.setReceiverID(7); // Shipper's account ID
            notification.setNotificationDetails("You have a new order to deliver! Order ID: " + orderID);
            notification.setDateCreated(new Date(System.currentTimeMillis()));
            notification.setDeleted(false);
            notification.setNotificationTitle("New Delivery Assignment");
            notification.setRead(false);

            notificationDAO.insertNotification(notification);

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
