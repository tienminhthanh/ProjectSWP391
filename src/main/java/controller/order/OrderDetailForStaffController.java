/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.order;

import dao.NotificationDAO;
import dao.OrderDAO;
import dao.ProductDAO;
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
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Account;
import model.DeliveryOption;
import model.Notification;
import model.OrderInfo;
import model.OrderProduct;
import model.Product;
import model.Shipper;

/**
 *
 * @author Macbook
 */
@WebServlet(name = "OrderDetailForStaffController", urlPatterns = {"/OrderDetailForStaffController"})
public class OrderDetailForStaffController extends HttpServlet {

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
            out.println("<title>Servlet OrderDetailForStaff</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet OrderDetailForStaff at " + request.getContextPath() + "</h1>");
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
        Account accShipper = new Account();
        if (account == null) {
            response.sendRedirect("login");
            return;
        }
        OrderDAO orderDAO = new OrderDAO();
        DeliveryOption delivery = new DeliveryOption();
        OrderInfo orderInfo = null; // Khai báo biến orderInfo trước khi dùng
        Account customer = null;
        Account handler = null;
        String orderID = request.getParameter("id");
        int valueVoucher = 0;
        try {
            if (orderID != null && !orderID.isEmpty()) {  // Kiểm tra orderID hợp lệ
                int id = Integer.parseInt(orderID);
                customer = orderDAO.getCustomerByOrderID(id);
                handler = orderDAO.getOrderHandlerByOrderID(id);
                accShipper = orderDAO.getShipperByOrderID(id);
                if (customer != null) {
                    int idcus = customer.getAccountID();
                    orderInfo = orderDAO.getOrderByID(id, idcus);
                    valueVoucher = orderDAO.getVoucherValueByOrderID(id);
                }
            }
        } catch (SQLException | NumberFormatException ex) {
            Logger.getLogger(OrderListForStaffController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //set ngày giao
        int deliveryTimeInDays;
        try {
            delivery = orderDAO.getDeliveryOption(orderInfo.getDeliveryOptionID());
        } catch (SQLException ex) {
            Logger.getLogger(OrderDetailForStaffController.class.getName()).log(Level.SEVERE, null, ex);
        }
        deliveryTimeInDays = delivery.getEstimatedTime();
        Calendar calendar = Calendar.getInstance();
        Date orderDate = orderInfo.getOrderDate();
        calendar.setTime(orderDate); // Nếu orderDate là null, tránh lỗi ở đây
        calendar.add(Calendar.DAY_OF_MONTH, deliveryTimeInDays);
        Date expectedDeliveryDate = new Date(calendar.getTimeInMillis());
        orderInfo.setExpectedDeliveryDate(expectedDeliveryDate);
        request.setAttribute("account", account);
        request.setAttribute("handler", handler);
        request.setAttribute("accShipper", accShipper);
        request.setAttribute("orderInfo", orderInfo);
        request.setAttribute("customer", customer);
        request.setAttribute("valueVoucher", valueVoucher);
        request.getRequestDispatcher("OrderDetailForStaffView.jsp").forward(request, response);
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
        if (account == null) {
            response.sendRedirect("login");
            return;
        }
        int orderID = Integer.parseInt(request.getParameter("orderID"));
        OrderDAO orderDao = new OrderDAO();
        String status = "canceled";
        NotificationDAO notificationDAO = new NotificationDAO();
        String cusID = request.getParameter("customerID");

        try {
            int customerID = Integer.parseInt(cusID);
            orderDao.updateOrderstatus(orderID, status);
            orderDao.updateAdminIdForOrderInfo(account.getAccountID(), orderID);
            // Send notification to shipper
            Notification notification = new Notification();
            notification.setSenderID(account.getAccountID()); // Staff who assigned the order
            notification.setReceiverID(customerID); // Shipper's account ID
            notification.setNotificationDetails("Your order has been canceled by the system! Order ID: " + orderID);
            notification.setDateCreated(new Date(System.currentTimeMillis()));
            notification.setDeleted(false);
            notification.setNotificationTitle("Order Cancellation");
            notification.setRead(false);

            notificationDAO.insertNotification(notification);

        } catch (SQLException ex) {
            Logger.getLogger(OrderDetailForStaffController.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.sendRedirect("OrderDetailForStaffController?id=" + orderID);

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
