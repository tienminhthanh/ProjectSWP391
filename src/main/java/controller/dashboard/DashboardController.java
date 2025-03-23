/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.dashboard;

import dao.*;
import model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "DashboardController", urlPatterns = {"/dashboard"})
public class DashboardController extends HttpServlet {

    private final DashboardDAO dashboardDAO = new DashboardDAO();

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
            out.println("<title>Servlet DashboardController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DashboardController at " + request.getContextPath() + "</h1>");
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        String week = request.getParameter("week");
        String day = request.getParameter("day");
        String timeFrame = request.getParameter("timeFrame");

        // Nếu không có bộ lọc nào, lấy toàn bộ dữ liệu từ trước đến nay
        boolean isFilterApplied = (year != null && !year.isEmpty())
                || (month != null && !month.isEmpty())
                || (week != null && !week.isEmpty())
                || (day != null && !day.isEmpty());

        DashboardDAO dao = new DashboardDAO();
        double totalRevenue = dao.getTotalRevenue(year, month, week, day, isFilterApplied);
        int totalQuantitySold = dao.getTotalQuantitySold(year, month, week, day, isFilterApplied);
        double grossProfit = dao.getGrossProfit(year, month, week, day, isFilterApplied);
        double profitMargin = dao.getProfitMargin(year, month, week, day, isFilterApplied);
        double orderConversionRate = dao.getOrderConversionRate(year, month, week, day, isFilterApplied);

        // Nếu không có bộ lọc, lấy toàn bộ xu hướng theo năm
        if (timeFrame == null || timeFrame.isEmpty()) {
            timeFrame = "year";
        }
        Map<String, Double> revenueTrend = dao.getRevenueTrend(timeFrame, year, month, week, day, isFilterApplied);

        // Đưa dữ liệu vào request
        request.setAttribute("totalRevenue", totalRevenue);
        request.setAttribute("totalQuantitySold", totalQuantitySold);
        request.setAttribute("grossProfit", grossProfit);
        request.setAttribute("profitMargin", profitMargin);
        request.setAttribute("orderConversionRate", orderConversionRate);
        request.setAttribute("revenueTrend", revenueTrend);
        Map<String, Integer> ageStats = dao.getAgeStatistics();
        request.setAttribute("ageStatistics", ageStats);

        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
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
