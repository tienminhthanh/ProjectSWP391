/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.order;

import dao.NotificationDAO;
import dao.OrderDAO;
import dao.VoucherDAO;
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
import model.Account;
import model.DeliveryOption;
import model.Notification;
import model.OrderInfo;
import model.Voucher;

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
        Voucher voucher = new Voucher();
        if (account == null) {
            response.sendRedirect("login");
            return;
        }
        OrderDAO orderDAO = new OrderDAO();
        VoucherDAO voucherDao = new VoucherDAO();

        DeliveryOption delivery = new DeliveryOption();
        OrderInfo orderInfo = null; // Khai báo biến orderInfo trước khi dùng
        Account customer = null;
        List<Account> handlerList = new ArrayList<>();
        String orderID = request.getParameter("id");
        double valueOfVoucher = 0;
        try {
            if (orderID != null && !orderID.isEmpty()) {  // Kiểm tra orderID hợp lệ
                int id = Integer.parseInt(orderID);
                customer = orderDAO.getCustomerByOrderID(id);
                handlerList = orderDAO.getOrderHandlerByOrderID(id);
                accShipper = orderDAO.getShipperByOrderID(id);
                if (customer != null) {
                    int idcus = customer.getAccountID();
                    orderInfo = orderDAO.getOrderByID(id, idcus);
                    voucher = voucherDao.getVoucherByID(orderInfo.getVoucherID());
                    if (voucher.getVoucherType().equals("FIXED_AMOUNT")) {
                        valueOfVoucher = voucher.getVoucherValue();
                    } else {
                        valueOfVoucher = (orderInfo.getPreVoucherAmount() * voucher.getVoucherValue()) / 100;
                        if (valueOfVoucher >= voucher.getMaxDiscountAmount()) {
                            valueOfVoucher = voucher.getMaxDiscountAmount();
                        }
                    }
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
        request.setAttribute("handlerList", handlerList);
        request.setAttribute("accShipper", accShipper);
        request.setAttribute("orderInfo", orderInfo);
        request.setAttribute("customer", customer);
        request.setAttribute("valueVoucher", valueOfVoucher);
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
            orderDao.restoreProductStockByOrderID(orderID);
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
