/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.voucher;

import dao.VoucherDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import model.Account;
import model.Voucher;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "VoucherAddNewServlet", urlPatterns = {"/voucherAddNew"})
public class VoucherAddNewController extends HttpServlet {

    private final String VOUCHER_ADDNEW_PAGE = "voucherAddNew.jsp";
    private final String VOUCHER_LIST_PAGE = "voucherList";
    private final VoucherDAO vDao = VoucherDAO.getInstance(); 

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
            out.println("<title>Servlet VoucherAddNewServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet VoucherAddNewServlet at " + request.getContextPath() + "</h1>");
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
        request.getRequestDispatcher(VOUCHER_ADDNEW_PAGE).forward(request, response);
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
        try {
            String name = request.getParameter("voucherName");
            String type = request.getParameter("voucherType");
            double value = Double.parseDouble(request.getParameter("voucherValue"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            int minimum = Integer.parseInt(request.getParameter("minimumPurchaseAmount"));
            int duration = Integer.parseInt(request.getParameter("duration"));
            Double maxDiscountAmount = null;
            if ("PERCENTAGE".equals(type)) {
                String maxDiscountStr = request.getParameter("maxDiscountAmount");
                if (maxDiscountStr != null && !maxDiscountStr.isEmpty()) {
                    maxDiscountAmount = Double.parseDouble(maxDiscountStr);
                }
            }

            LocalDate dateCreated = LocalDate.now();
            String dateStarted = request.getParameter("dateStarted");

            LocalDate today = LocalDate.now();
            LocalDate expiryDate = LocalDate.parse(dateStarted).plusDays(duration);

            boolean isActive = false;
            //ngày hết hạn >= hôm nay               ngày bắt đầu <= hôm nay
            if (!expiryDate.isBefore(today) && (LocalDate.parse(dateStarted).isBefore(today) || LocalDate.parse(dateStarted).isEqual(today))) {
                isActive = true;
            }
            Account account = (Account) session.getAttribute("account");
            if (account == null) {
                response.sendRedirect("login.jsp");
                return;
            }
            int adminID = account.getAccountID();

            Voucher voucher = new Voucher(name, value, quantity, minimum, dateCreated.toString(), duration, adminID, isActive, true, type, maxDiscountAmount, dateStarted);
            boolean add = vDao.addVoucher(voucher);
            if (add) {
                session.setAttribute("message", "Voucher created successfully!");
                session.setAttribute("messageType", "success");
            } else {
                session.setAttribute("message", "Failed to create voucher.");
                session.setAttribute("messageType", "error");
            }
        } catch (Exception e) {
            session.setAttribute("message", "Error: " + e.getMessage());
            session.setAttribute("messageType", "error");
        }
        response.sendRedirect(VOUCHER_LIST_PAGE);

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
