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

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "VoucherDeleteServlet", urlPatterns = {"/voucherDelete"})
public class VoucherDeleteController extends HttpServlet {

    private final String VOUCHER_LIST_PAGE = "voucherList";

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
        String url = VOUCHER_LIST_PAGE;
        HttpSession session = request.getSession();

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            VoucherDAO vDao = new VoucherDAO();

//            if (vDao.deleteVoucher(id)) {
//                session.setAttribute("message", "Voucher deleted successfully!");
//                session.setAttribute("messageType", "success");
//            } else {
//                session.setAttribute("message", "Failed to delete voucher.");
//                session.setAttribute("messageType", "error");
//            }
            String action = request.getParameter("action");
            boolean success = vDao.deleteVoucher(id);
            if ("delete".equals(action)) {
                if (success) {
                    session.setAttribute("message", "Voucher deleted successfully!");
                    session.setAttribute("messageType", "delete");
                } else {
                    session.setAttribute("message", "Failed to delete voucher.");
                    session.setAttribute("messageType", "error");
                }
            } else if ("unlock".equals(action)) {
                if (success) {
                    session.setAttribute("message", "Voucher unlocked successfully!");
                    session.setAttribute("messageType", "unlock");
                } else {
                    session.setAttribute("message", "Failed to unlock voucher.");
                    session.setAttribute("messageType", "error");
                }
            }

        } catch (Exception e) {
            session.setAttribute("message", "Error: " + e.getMessage());
            session.setAttribute("messageType", "error");
        }
        response.sendRedirect(url);
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
