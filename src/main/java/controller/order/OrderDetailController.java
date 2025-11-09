/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.order;

import dao.AccountDAO;
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
import model.DeliveryAddress;
import model.DeliveryOption;
import model.OrderInfo;
import model.Voucher;

/**
 *
 * @author Macbook
 */
@WebServlet(name = "OrderDetailController", urlPatterns = {"/OrderDetailController"})
public class OrderDetailController extends HttpServlet {

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
            out.println("<title>Servlet OrderDetailController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet OrderDetailController at " + request.getContextPath() + "</h1>");
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
        AccountDAO accountDAO = new AccountDAO();
        Account account = (Account) session.getAttribute("account");
        VoucherDAO voucherDao = new VoucherDAO();
        List<DeliveryAddress> listAddress = new ArrayList<>();

        Voucher voucher = new Voucher();
        try {
            String orderID = request.getParameter("id");
            OrderInfo orderInfo = orderDAO.getOrderByID(Integer.parseInt(orderID), account.getAccountID());
            //set ngày giao
            int deliveryTimeInDays;
            DeliveryOption delivery = orderDAO.getDeliveryOption(orderInfo.getDeliveryOptionID());
            deliveryTimeInDays = delivery.getEstimatedTime();
            Calendar calendar = Calendar.getInstance();
            Date orderDate = orderInfo.getOrderDate();
            calendar.setTime(orderDate); // Nếu orderDate là null, tránh lỗi ở đây
            calendar.add(Calendar.DAY_OF_MONTH, deliveryTimeInDays);
            Date expectedDeliveryDate = new Date(calendar.getTimeInMillis());
            orderInfo.setExpectedDeliveryDate(expectedDeliveryDate);
            voucher = voucherDao.getVoucherByID(orderInfo.getVoucherID());
            double valueOfVoucher = 0;
            if (voucher != null) {
                if (voucher.getVoucherType().equals("FIXED_AMOUNT")) {
                    valueOfVoucher = voucher.getVoucherValue();
                } else {
                    valueOfVoucher = (orderInfo.getPreVoucherAmount() * voucher.getVoucherValue()) / 100;
                    if (valueOfVoucher >= voucher.getMaxDiscountAmount()) {
                        valueOfVoucher = voucher.getMaxDiscountAmount();
                    }
                }
            }
            account = accountDAO.getAdditionalInfo(account);
            session.setAttribute("account", account);
            listAddress = accountDAO.getAllAddressByCustomerID(account.getAccountID());

            delivery = (DeliveryOption) orderDAO.getDeliveryOption(orderInfo.getDeliveryOptionID());
            request.setAttribute("orderInfo", orderInfo); // Đặt dữ liệu vào requestScope
            request.setAttribute("delivery", delivery);
            request.setAttribute("voucher", valueOfVoucher);
            request.setAttribute("addressList", listAddress);

            // Chuyển hướng đến OrderListView.jsp
            request.getRequestDispatcher("OrderDetailView.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage1", "Something went wrong. Please try again later!");
//            response.sendRedirect("error.jsp");
            request.getRequestDispatcher("OrderListView.jsp").forward(request, response);
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
        String status = "completed";
        String action = request.getParameter("action");
        OrderDAO orderDao = new OrderDAO();

        try {
            if (null != action) {
                switch (action) {
                    case "confirm":
                        orderDao.updateOrderstatus(orderID, status);
                        break;
                    case "rateAndReview": {
                        int productID = Integer.parseInt(request.getParameter("productID"));
                        int rate = Integer.parseInt(request.getParameter("rating"));
                        orderDao.updateRatingForProduct(orderID, productID, rate);
                        String reviewContent = request.getParameter("reviewContent");
                        orderDao.updateCommentForProduct(orderID, productID, reviewContent);
                        break;
                      
                    }
                    case "review": {
                        
                    }
                    default:
                        break;
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(OrderDetailController.class.getName()).log(Level.SEVERE, null, ex);
        }

        response.sendRedirect("OrderDetailController?id=" + orderID);
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