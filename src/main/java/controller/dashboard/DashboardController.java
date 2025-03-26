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
        // Lấy tham số lọc mới
        String year = request.getParameter("year");
        String quarter = request.getParameter("quarter");
        String month = request.getParameter("month");
        String revenueTrendYear = request.getParameter("revenueTrendYear");

        // Nếu không có bộ lọc nào, lấy toàn bộ dữ liệu từ trước đến nay
        boolean isFilterApplied = (year != null && !year.isEmpty())
                || (quarter != null && !quarter.isEmpty())
                || (month != null && !month.isEmpty());

        // Nếu year không được chọn, mặc định là năm hiện tại
        if (year == null || year.isEmpty()) {
            year = String.valueOf(LocalDate.now().getYear()); // 2025
        }

        // Nếu revenueTrendYear không được chọn (mới vào dashboard), mặc định là năm hiện tại
        if (revenueTrendYear == null || revenueTrendYear.isEmpty()) {
            revenueTrendYear = String.valueOf(LocalDate.now().getYear()); // 2025
        }

        DashboardDAO dao = new DashboardDAO();
        double totalRevenue = dao.getTotalRevenue(year, quarter, month, isFilterApplied);
        int totalQuantitySold = dao.getTotalQuantitySold(year, quarter, month, isFilterApplied);
        double grossProfit = dao.getGrossProfit(year, quarter, month, isFilterApplied);
        double profitMargin = dao.getProfitMargin(year, quarter, month, isFilterApplied);
        double orderConversionRate = dao.getOrderConversionRate(year, quarter, month, isFilterApplied);

        // Luôn lấy Revenue Trend theo tháng cho năm được chọn
        Map<String, Double> revenueTrend = dao.getRevenueTrend("month", revenueTrendYear, null, null, true);

        // Đưa dữ liệu vào request
        request.setAttribute("totalRevenue", totalRevenue);
        request.setAttribute("totalQuantitySold", totalQuantitySold);
        request.setAttribute("grossProfit", grossProfit);
        request.setAttribute("profitMargin", profitMargin);
        request.setAttribute("orderConversionRate", orderConversionRate);
        request.setAttribute("revenueTrend", revenueTrend);
        request.setAttribute("selectedRevenueTrendYear", revenueTrendYear); // Truyền năm đã chọn để JSP hiển thị
        Map<String, Integer> ageStats = dao.getAgeStatistics();
        request.setAttribute("ageStatistics", ageStats);

        // Truyền các tham số lọc để JSP hiển thị giá trị đã chọn
        request.setAttribute("selectedYear", year);
        request.setAttribute("selectedQuarter", quarter);
        request.setAttribute("selectedMonth", month);

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
    }
}
