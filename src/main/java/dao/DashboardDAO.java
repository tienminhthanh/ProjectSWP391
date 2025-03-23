/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author ADMIN
 */
public class DashboardDAO {

    private utils.DBContext context;

    public DashboardDAO() {
        context = new utils.DBContext();
    }

    public String buildFilterCondition(String year, String month, String week, String day, boolean isFilterApplied) {
        if (!isFilterApplied) {
            return ""; // Không áp dụng bộ lọc nếu isFilterApplied = false
        }

        StringBuilder condition = new StringBuilder(" AND 1=1 ");
        if (year != null && !year.isEmpty()) {
            condition.append(" AND YEAR(orderDate) = ").append(year);
        }
        if (month != null && !month.isEmpty()) {
            condition.append(" AND MONTH(orderDate) = ").append(month);
        }
        if (week != null && !week.isEmpty()) {
            condition.append(" AND DATEPART(WEEK, orderDate) = ").append(week);
        }
        if (day != null && !day.isEmpty()) {
            condition.append(" AND DAY(orderDate) = ").append(day);
        }

        return condition.toString();
    }

    public Map<String, Double> getRevenueTrend(String timeFrame, String year, String month, String week, String day, boolean isFilterApplied) {
        String timePeriodColumn = "";

        switch (timeFrame) {
            case "month":
                timePeriodColumn = "FORMAT(orderDate, 'yyyy-MM')";
                break;
            case "quarter":
                timePeriodColumn = "CONCAT(YEAR(orderDate), '-Q', DATEPART(QUARTER, orderDate))";
                break;
            case "year":
                timePeriodColumn = "FORMAT(orderDate, 'yyyy')";
                break;
            default:
                throw new IllegalArgumentException("Invalid timeFrame: " + timeFrame);
        }

        String sql = "SELECT " + timePeriodColumn + " AS timePeriod, SUM(finalAmount) AS totalRevenue "
                + "FROM OrderInfo WHERE orderStatus = 'delivered' ";
        sql += buildFilterCondition(year, month, week, day, isFilterApplied);
        sql += " GROUP BY " + timePeriodColumn + " ORDER BY timePeriod;";

        Map<String, Double> revenueTrend = new LinkedHashMap<>();
        try {
            ResultSet rs = context.exeQuery(sql, null);
            while (rs.next()) {
                revenueTrend.put(rs.getString(1), rs.getDouble(2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return revenueTrend;
    }

    public double getGrossProfit(String year, String month, String week, String day, boolean isFilterApplied) {
        String sql = "SELECT SUM(op.orderProductQuantity * op.orderProductPrice) - "
                + "SUM(op.orderProductQuantity * ii.importPrice) AS profit "
                + "FROM Order_Product op "
                + "JOIN OrderInfo oi ON op.orderID = oi.orderID "
                + "JOIN ImportItem ii ON op.productID = ii.productID "
                + "WHERE oi.orderStatus = 'delivered' ";
        sql += buildFilterCondition(year, month, week, day, isFilterApplied);

        try {
            ResultSet rs = context.exeQuery(sql, null);
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double getProfitMargin(String year, String month, String week, String day, boolean isFilterApplied) {
        String sql = "SELECT "
                + "    (SUM(op.orderProductQuantity * op.orderProductPrice) - "
                + "    SUM(op.orderProductQuantity * ii.importPrice)) / "
                + "    NULLIF(SUM(op.orderProductQuantity * op.orderProductPrice), 0) * 100 AS profitMargin "
                + "FROM Order_Product op "
                + "JOIN OrderInfo oi ON op.orderID = oi.orderID "
                + "JOIN ImportItem ii ON op.productID = ii.productID "
                + "WHERE oi.orderStatus = 'delivered' ";
        sql += buildFilterCondition(year, month, week, day, isFilterApplied);

        try {
            ResultSet rs = context.exeQuery(sql, null);
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double getOrderConversionRate(String year, String month, String week, String day, boolean isFilterApplied) {
        String sql = "SELECT "
                + "(COUNT(DISTINCT CASE WHEN oi.orderStatus = 'delivered' THEN oi.orderID END) * 1.0 / "
                + "COUNT(DISTINCT oi.orderID)) * 100 AS conversionRate "
                + "FROM OrderInfo oi WHERE 1=1 ";
        sql += buildFilterCondition(year, month, week, day, isFilterApplied);

        try {
            ResultSet rs = context.exeQuery(sql, null);
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getTotalQuantitySold(String year, String month, String week, String day, boolean isFilterApplied) {
        String sql = "SELECT SUM(op.orderProductQuantity) AS totalQuantity "
                + "FROM Order_Product op "
                + "JOIN OrderInfo oi ON op.orderID = oi.orderID "
                + "WHERE oi.orderStatus = 'delivered' ";
        sql += buildFilterCondition(year, month, week, day, isFilterApplied);

        try {
            ResultSet rs = context.exeQuery(sql, null);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double getTotalRevenue(String year, String month, String week, String day, boolean isFilterApplied) {
        String sql = "SELECT SUM(op.orderProductQuantity * op.orderProductPrice) AS totalRevenue "
                + "FROM Order_Product op "
                + "JOIN OrderInfo oi ON op.orderID = oi.orderID "
                + "WHERE oi.orderStatus = 'delivered' ";
        sql += buildFilterCondition(year, month, week, day, isFilterApplied);

        try {
            ResultSet rs = context.exeQuery(sql, null);
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Map<String, Integer> getAgeStatistics() {
        Map<String, Integer> ageStats = new LinkedHashMap<>();
        String sql = "SELECT "
                + "    CASE "
                + "        WHEN DATEDIFF(YEAR, birthDate, GETDATE()) < 18 THEN '<18' "
                + "        WHEN DATEDIFF(YEAR, birthDate, GETDATE()) BETWEEN 18 AND 24 THEN '18-24' "
                + "        WHEN DATEDIFF(YEAR, birthDate, GETDATE()) BETWEEN 25 AND 34 THEN '25-34' "
                + "        WHEN DATEDIFF(YEAR, birthDate, GETDATE()) BETWEEN 35 AND 44 THEN '35-44' "
                + "        WHEN DATEDIFF(YEAR, birthDate, GETDATE()) BETWEEN 45 AND 54 THEN '45-54' "
                + "        ELSE '55+' "
                + "    END AS ageGroup, COUNT(*) AS userCount "
                + "FROM Account "
                + "WHERE accountIsActive = 1 "
                + "GROUP BY "
                + "    CASE "
                + "        WHEN DATEDIFF(YEAR, birthDate, GETDATE()) < 18 THEN '<18' "
                + "        WHEN DATEDIFF(YEAR, birthDate, GETDATE()) BETWEEN 18 AND 24 THEN '18-24' "
                + "        WHEN DATEDIFF(YEAR, birthDate, GETDATE()) BETWEEN 25 AND 34 THEN '25-34' "
                + "        WHEN DATEDIFF(YEAR, birthDate, GETDATE()) BETWEEN 35 AND 44 THEN '35-44' "
                + "        WHEN DATEDIFF(YEAR, birthDate, GETDATE()) BETWEEN 45 AND 54 THEN '45-54' "
                + "        ELSE '55+' "
                + "    END "
                + "ORDER BY ageGroup";

        try {
            ResultSet rs = context.exeQuery(sql, null);
            while (rs.next()) {
                ageStats.put(rs.getString("ageGroup"), rs.getInt("userCount"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ageStats;
    }

    public static void main(String[] args) {
        DashboardDAO dDao = new DashboardDAO();

    }
}
