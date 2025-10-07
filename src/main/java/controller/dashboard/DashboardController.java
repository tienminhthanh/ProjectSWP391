package controller.dashboard;

import model.product_related.Product;
import dao.*;
import model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate; // Thêm import để lấy năm hiện tại

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "DashboardController", urlPatterns = {"/dashboard"})
public class DashboardController extends HttpServlet {

    private final DashboardDAO dashboardDAO = new DashboardDAO();

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

        // Lấy năm hiện tại làm năm mặc định
        String defaultYear = String.valueOf(LocalDate.now().getYear()); // Ví dụ: 2025 nếu hôm nay là 27/03/2025

        // Nếu year không được chọn, sử dụng năm hiện tại
        if (year == null || year.isEmpty()) {
            year = defaultYear;
        }

        // Nếu revenueTrendYear không được chọn (mới vào dashboard), sử dụng năm hiện tại
        if (revenueTrendYear == null || revenueTrendYear.isEmpty()) {
            revenueTrendYear = defaultYear;
        }

        DashboardDAO dao = new DashboardDAO();
        double totalRevenue = dao.getTotalRevenue(year, quarter, month, isFilterApplied);
        int totalQuantitySold = dao.getTotalQuantitySold(year, quarter, month, isFilterApplied);
        int totalQuantitySoldFromOrder = dao.getTotalQuantitySoldFromOrder(year, quarter, month, isFilterApplied);
        double grossProfit = dao.getGrossProfit(year, quarter, month, isFilterApplied);
        double profitMargin = dao.getProfitMargin(year, quarter, month, isFilterApplied);
        double orderConversionRate = dao.getOrderConversionRate(year, quarter, month, isFilterApplied);
        int totalOrders = dao.getTotalOrders(year, quarter, month, isFilterApplied);
        int successfulOrders = dao.getSuccessfulOrders(year, quarter, month, isFilterApplied);
        List<Customer> topBuyers = dao.getTopBuyers(); // Không áp dụng bộ lọc thời gian

        // Tạo Map để lưu totalQuantitySold và totalRevenue
        Map<Integer, Integer> quantitySoldMap = new HashMap<>();
        Map<Integer, Double> revenueMap = new HashMap<>();

        // Lấy top 5 sản phẩm theo thể loại
        Map<String, List<Product>> topProductsByCategory = dao.getTopProductsByCategory(year, quarter, month, isFilterApplied, quantitySoldMap, revenueMap);

        // Luôn lấy Revenue Trend theo tháng cho năm được chọn
        Map<String, Double> revenueTrend = dao.getRevenueTrend("month", revenueTrendYear, null, null, true);

        // Đưa dữ liệu vào request
        request.setAttribute("totalRevenue", totalRevenue);
        request.setAttribute("totalQuantitySold", totalQuantitySold);
        request.setAttribute("totalQuantitySoldFromOrder", totalQuantitySoldFromOrder);
        request.setAttribute("grossProfit", grossProfit);
        request.setAttribute("profitMargin", profitMargin);
        request.setAttribute("orderConversionRate", orderConversionRate);
        request.setAttribute("totalOrders", totalOrders);
        request.setAttribute("successfulOrders", successfulOrders);
        request.setAttribute("revenueTrend", revenueTrend);
        request.setAttribute("selectedRevenueTrendYear", revenueTrendYear);
        request.setAttribute("topBuyers", topBuyers);
        request.setAttribute("topProductsByCategory", topProductsByCategory);
        request.setAttribute("quantitySoldMap", quantitySoldMap);
        request.setAttribute("revenueMap", revenueMap);

        Map<String, Integer> ageStats = dao.getAgeStatistics();
        request.setAttribute("ageStatistics", ageStats);

        // Truyền các tham số lọc để JSP hiển thị giá trị đã chọn
        request.setAttribute("selectedYear", year);
        request.setAttribute("selectedQuarter", quarter);
        request.setAttribute("selectedMonth", month);

        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
