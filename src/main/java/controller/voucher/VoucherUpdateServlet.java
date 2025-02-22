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
import java.util.List;
import model.Voucher;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "VoucherUpdateServlet", urlPatterns = {"/voucherUpdate"})
public class VoucherUpdateServlet extends HttpServlet {

    private final String VOUCHER_UPDATE_PAGE = "voucherUpdate.jsp";

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
        String url = VOUCHER_UPDATE_PAGE;
        try {
            int id = Integer.parseInt(request.getParameter("voucherID"));
            VoucherDAO vDao = new VoucherDAO();
            Voucher voucherDetails = vDao.getVoucherByID(id);
            request.setAttribute("VOUCHER_DETAILS", voucherDetails);
        } catch (Exception ex) {
            log("VoucherDetailsServlet error:" + ex.getMessage());
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
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
        VoucherDAO vDao = new VoucherDAO();
        int id = Integer.parseInt(request.getParameter("voucherID"));
        String name = request.getParameter("voucherName");
        double value = Double.parseDouble(request.getParameter("voucherValue"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        int minimum = Integer.parseInt(request.getParameter("minimumPurchaseAmount"));
        String dateCreated = request.getParameter("dateCreated");
//        LocalDate today = LocalDate.now();
        int duration = Integer.parseInt(request.getParameter("duration"));
        int adminID = Integer.parseInt(request.getParameter("adminID"));
        Voucher voucher = new Voucher(id, name, value, quantity, minimum, dateCreated, duration, adminID, vDao.getVoucherByID(id).isIsActive());
        if (vDao.updateVoucher(voucher)) {
            response.sendRedirect("voucherList");
        }
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
