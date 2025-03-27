package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class DashboardDAO {

    private utils.DBContext context;

    public DashboardDAO() {
        context = new utils.DBContext();
    }

    public String buildFilterCondition(String year, String quarter, String month, boolean isFilterApplied) {
        if (!isFilterApplied) {
            return ""; // Không áp dụng bộ lọc nếu isFilterApplied = false
        }

        StringBuilder condition = new StringBuilder(" AND 1=1 ");
        if (year != null && !year.isEmpty()) {
            condition.append(" AND YEAR(orderDate) = ").append(year);
        }
        if (quarter != null && !quarter.isEmpty()) {
            condition.append(" AND DATEPART(QUARTER, orderDate) = ").append(quarter);
        }
        if (month != null && !month.isEmpty()) {
            condition.append(" AND MONTH(orderDate) = ").append(month);
        }

        return condition.toString();
    }

    public Map<String, Double> getRevenueTrend(String timeFrame, String year, String quarter, String month, boolean isFilterApplied) {
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
                + "FROM OrderInfo WHERE orderStatus IN ('delivered', 'completed') ";
        sql += buildFilterCondition(year, quarter, month, isFilterApplied);
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

    public double getGrossProfit(String year, String quarter, String month, boolean isFilterApplied) {
        String sql = "WITH WeightedImportPrice AS ("
                + "    SELECT productID, "
                + "           SUM(importPrice * importQuantity) / NULLIF(SUM(importQuantity), 0) AS weightedImportPrice "
                + "    FROM ImportItem "
                + "    WHERE isImported = 1 "
                + "    GROUP BY productID "
                + ")"
                + "SELECT SUM(oi.finalAmount) - "
                + "       SUM(op.orderProductQuantity * wip.weightedImportPrice) AS profit "
                + "FROM Order_Product op "
                + "JOIN OrderInfo oi ON op.orderID = oi.orderID "
                + "JOIN WeightedImportPrice wip ON op.productID = wip.productID "
                + "WHERE oi.orderStatus IN ('delivered', 'completed') ";
        sql += buildFilterCondition(year, quarter, month, isFilterApplied);

        try {
            ResultSet rs = context.exeQuery(sql, null);
            if (rs.next()) {
                return rs.getDouble(1);
            } else {
                System.out.println("No data found for the given time period.");
                return 0;
            }
        } catch (Exception e) {
            System.err.println("Error calculating gross profit: " + e.getMessage());
            throw new RuntimeException("Failed to calculate gross profit", e);
        }
    }

    public double getProfitMargin(String year, String quarter, String month, boolean isFilterApplied) {
        String sql = "WITH WeightedImportPrice AS ("
                + "    SELECT productID, "
                + "           SUM(importPrice * importQuantity) / NULLIF(SUM(importQuantity), 0) AS weightedImportPrice "
                + "    FROM ImportItem "
                + "    WHERE isImported = 1 "
                + "    GROUP BY productID "
                + ")"
                + "SELECT "
                + "    (SUM(oi.finalAmount) - "
                + "     SUM(op.orderProductQuantity * wip.weightedImportPrice)) / "
                + "    NULLIF(SUM(oi.finalAmount), 0) * 100 AS profitMargin "
                + "FROM Order_Product op "
                + "JOIN OrderInfo oi ON op.orderID = oi.orderID "
                + "JOIN WeightedImportPrice wip ON op.productID = wip.productID "
                + "WHERE oi.orderStatus IN ('delivered', 'completed') ";
        sql += buildFilterCondition(year, quarter, month, isFilterApplied);

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

    public double getOrderConversionRate(String year, String quarter, String month, boolean isFilterApplied) {
        String sql = "SELECT "
                + "    (SUM(CASE WHEN orderStatus IN ('delivered', 'completed') THEN 1 ELSE 0 END) * 100.0) / "
                + "    NULLIF(COUNT(*), 0) AS orderConversionRate "
                + "FROM OrderInfo "
                + "WHERE orderStatus IN ('delivered', 'completed') "; // Chỉ tính đơn hàng thành công
        sql += buildFilterCondition(year, quarter, month, isFilterApplied);

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

    public int getTotalOrders(String year, String quarter, String month, boolean isFilterApplied) {
        String sql = "SELECT COUNT(*) AS totalOrders "
                + "FROM OrderInfo "
                + "WHERE orderStatus IN ('delivered', 'completed') "; // Chỉ đếm đơn hàng thành công
        sql += buildFilterCondition(year, quarter, month, isFilterApplied);

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

    public int getSuccessfulOrders(String year, String quarter, String month, boolean isFilterApplied) {
        String sql = "SELECT SUM(CASE WHEN orderStatus IN ('delivered', 'completed') THEN 1 ELSE 0 END) AS successfulOrders "
                + "FROM OrderInfo "
                + "WHERE orderStatus IN ('delivered', 'completed') "; // Chỉ đếm đơn hàng thành công
        sql += buildFilterCondition(year, quarter, month, isFilterApplied);

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

    public int getTotalQuantitySold(String year, String quarter, String month, boolean isFilterApplied) {
        String sql = "SELECT SUM(soldQuantity) AS totalQuantity "
                + "FROM SaleHistory "
                + "WHERE 1=1 ";

        // Thêm bộ lọc thời gian, thay orderDate bằng saleDate
        if (isFilterApplied) {
            StringBuilder condition = new StringBuilder();
            if (year != null && !year.isEmpty()) {
                condition.append(" AND YEAR(saleDate) = ").append(year);
            }
            if (quarter != null && !quarter.isEmpty()) {
                condition.append(" AND DATEPART(QUARTER, saleDate) = ").append(quarter);
            }
            if (month != null && !month.isEmpty()) {
                condition.append(" AND MONTH(saleDate) = ").append(month);
            }
            sql += condition.toString();
        }

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

    // Thêm hàm mới để lấy Total Quantity Sold từ Order_Product (để so sánh)
    public int getTotalQuantitySoldFromOrder(String year, String quarter, String month, boolean isFilterApplied) {
        String sql = "SELECT SUM(op.orderProductQuantity) AS totalQuantitySold "
                + "FROM OrderInfo oi "
                + "JOIN Order_Product op ON oi.orderID = op.orderID "
                + "WHERE oi.orderStatus IN ('delivered', 'completed') ";
        sql += buildFilterCondition(year, quarter, month, isFilterApplied);

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

    public double getTotalRevenue(String year, String quarter, String month, boolean isFilterApplied) {
        String sql = "SELECT SUM(oi.finalAmount) AS totalRevenue "
                + "FROM OrderInfo oi "
                + "WHERE oi.orderStatus IN ('delivered', 'completed') ";
        sql += buildFilterCondition(year, quarter, month, isFilterApplied);

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
