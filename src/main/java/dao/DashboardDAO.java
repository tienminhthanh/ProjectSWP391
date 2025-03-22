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

    public Map<String, Double> getRevenueTrend(String timeFrame) {
        String timePeriodColumn = "";

        switch (timeFrame) {
            case "month":
                timePeriodColumn = "FORMAT(orderDate, 'yyyy-MM')";
                break;
            case "quarter":
                timePeriodColumn = "CONCAT(YEAR(orderDate), '-Q', DATEPART(QUARTER, orderDate))";
                break;
            case "halfyear":
                timePeriodColumn = "CONCAT(YEAR(orderDate), '-H', (CASE WHEN MONTH(orderDate) <= 6 THEN 1 ELSE 2 END))";
                break;
            case "year":
                timePeriodColumn = "FORMAT(orderDate, 'yyyy')";
                break;
            default:
                throw new IllegalArgumentException("Invalid timeFrame: " + timeFrame);
        }

        String sql = "SELECT " + timePeriodColumn + " AS timePeriod, "
                + "SUM(finalAmount) AS totalRevenue "
                + "FROM OrderInfo "
                + "WHERE orderStatus = 'delivered' "
                + "GROUP BY " + timePeriodColumn + " "
                + "ORDER BY timePeriod;";

        Map<String, Double> revenueTrend = new LinkedHashMap<>();
        try {
            ResultSet rs = context.exeQuery(sql, null); // Không cần truyền params
            while (rs.next()) {
                revenueTrend.put(rs.getString(1), rs.getDouble(2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return revenueTrend;
    }

    public double getGrossProfit() {
        String sql = "SELECT "
                + "    SUM(op.orderProductQuantity * op.orderProductPrice) - "
                + "    SUM(op.orderProductQuantity * ii.importPrice) AS profit "
                + "FROM Order_Product op "
                + "JOIN OrderInfo oi ON op.orderID = oi.orderID "
                + "JOIN ImportItem ii ON op.productID = ii.productID "
                + "WHERE oi.orderStatus = 'delivered'";
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

    public double getProfitMargin() {
        String sql = "SELECT "
                + "    (SUM(op.orderProductQuantity * op.orderProductPrice) - "
                + "    SUM(op.orderProductQuantity * ii.importPrice)) / "
                + "    SUM(op.orderProductQuantity * op.orderProductPrice) * 100 AS profitMargin "
                + "FROM Order_Product op "
                + "JOIN OrderInfo oi ON op.orderID = oi.orderID "
                + "JOIN ImportItem ii ON op.productID = ii.productID "
                + "WHERE oi.orderStatus = 'delivered'";
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

    public double getOrderConversionRate() {
        String sql = "SELECT "
                + "    (COUNT(DISTINCT CASE WHEN oi.orderStatus = 'delivered' THEN oi.orderID END) * 1.0 / "
                + "    COUNT(DISTINCT oi.orderID)) * 100 AS conversionRate "
                + "FROM OrderInfo oi";
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

    public int getTotalQuantitySold() {
        String sql = "SELECT SUM(op.orderProductQuantity) AS totalQuantity "
                + "FROM Order_Product op "
                + "JOIN OrderInfo oi ON op.orderID = oi.orderID "
                + "WHERE oi.orderStatus = 'delivered'";
        try {
            ResultSet rs = context.exeQuery(sql, null);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 7;
    }

    public double getTotalRevenue() {
        String sql = "SELECT SUM(op.orderProductQuantity * op.orderProductPrice) AS totalRevenue "
                + "FROM Order_Product op "
                + "JOIN OrderInfo oi ON op.orderID = oi.orderID "
                + "WHERE oi.orderStatus = 'delivered'";
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
        System.out.println(dDao.getRevenueTrend("month"));
        System.out.println(dDao.getGrossProfit());
        System.out.println(dDao.getOrderConversionRate());
        System.out.println(dDao.getProfitMargin());
        System.out.println(dDao.getTotalRevenue());
        System.out.println(dDao.getTotalQuantitySold());
    }
}
