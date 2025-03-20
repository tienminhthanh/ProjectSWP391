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
@WebServlet(name = "VoucherListServlet", urlPatterns = {"/voucherList"})
public class VoucherListController extends HttpServlet {

    private final String VOUCHER_LIST_PAGE = "voucherList.jsp";

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
        int page = 1;
        int pageSize = 5;
        String pageStr = request.getParameter("page");
        if (pageStr != null) {
            try {
                page = Integer.parseInt(pageStr);
            } catch (Exception e) {
                page = 1;
            }
        }
        // Lấy tham số tìm kiếm
        String searchKeyword = request.getParameter("search");
        String voucherType = request.getParameter("voucherType");
        String isActiveParam = request.getParameter("isActive");

        try {
            VoucherDAO vDao = new VoucherDAO();
            List<Voucher> listVoucher = vDao.getVoucherByPage(searchKeyword, voucherType, isActiveParam, page, pageSize);
            int totalVouchers = vDao.getTotalVoucher(searchKeyword, voucherType, isActiveParam);
            int totalPages = (int) Math.ceil((double) totalVouchers / pageSize);

            request.setAttribute("LIST_VOUCHER", listVoucher);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPage", totalPages);
            request.setAttribute("searchName", searchKeyword);
            request.setAttribute("voucherType", voucherType);
            request.setAttribute("isActiveParam", isActiveParam);
        } catch (Exception ex) {
            log("VoucherListServlet error:" + ex.getMessage());
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
