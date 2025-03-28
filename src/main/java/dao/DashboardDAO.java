package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import model.*;

public class DashboardDAO {

    private utils.DBContext context;

    public DashboardDAO() {
        context = new utils.DBContext();
    }

    public Map<String, List<Product>> getTopProductsByCategory(String year, String quarter, String month, boolean isFilterApplied, Map<Integer, Integer> quantitySoldMap, Map<Integer, Double> revenueMap) {
        Map<String, List<Product>> categoryTopProducts = new HashMap<>();

        // Bước 1: Lấy danh sách tất cả các categoryName từ bảng Category
        String categorySql = "SELECT DISTINCT c.categoryName "
                + "FROM [WIBOOKS].[dbo].[Category] c;";

        try {
            ResultSet categoryRs = context.exeQuery(categorySql, null);
            if (!categoryRs.isBeforeFirst()) {
                System.out.println("No categories found in Category table.");
                return categoryTopProducts; // Trả về Map rỗng nếu không có thể loại
            }

            while (categoryRs.next()) {
                String categoryName = categoryRs.getString("categoryName");
                if (categoryName == null || categoryName.trim().isEmpty()) {
                    System.out.println("Found a null or empty category name, skipping...");
                    continue;
                }

                // Bước 2: Lấy top 5 sản phẩm bán chạy nhất cho từng categoryName
                String sql = "SELECT TOP 5 "
                        + "    op.productID, "
                        + "    p.productName, "
                        + "    SUM(op.orderProductQuantity) AS totalQuantitySold, "
                        + "    SUM(op.orderProductPrice * op.orderProductQuantity) AS totalRevenue "
                        + "FROM [WIBOOKS].[dbo].[OrderInfo] oi "
                        + "JOIN [WIBOOKS].[dbo].[Order_Product] op ON oi.orderID = op.orderID "
                        + "JOIN [WIBOOKS].[dbo].[Product] p ON op.productID = p.productID "
                        + "JOIN [WIBOOKS].[dbo].[Category] c ON p.categoryID = c.categoryID "
                        + "WHERE oi.orderStatus IN ('delivered', 'completed') "
                        + "    AND p.productIsActive = 1 "
                        + "    AND c.categoryName = ? ";

                // Áp dụng bộ lọc thời gian
                String filterCondition = buildFilterCondition(year, quarter, month, isFilterApplied);
                sql += filterCondition;

                // Nhóm theo sản phẩm và sắp xếp theo tổng số lượng bán ra
                sql += " GROUP BY op.productID, p.productName "
                        + "ORDER BY SUM(op.orderProductQuantity) DESC;";

                // In truy vấn để debug
                System.out.println("Executing query for category: " + categoryName);
                System.out.println("SQL: " + sql);

                // Sử dụng PreparedStatement để truyền tham số categoryName
                Object[] params = {categoryName};
                ResultSet rs = context.exeQuery(sql, params);

                List<Product> topProducts = new ArrayList<>();
                if (!rs.isBeforeFirst()) {
                    System.out.println("No products found for category: " + categoryName);
                } else {
                    while (rs.next()) {
                        int productID = rs.getInt("productID");
                        Product product = new Product();
                        product.setProductID(productID);
                        product.setProductName(rs.getString("productName"));

                        // Lưu totalQuantitySold và totalRevenue vào Map
                        quantitySoldMap.put(productID, rs.getInt("totalQuantitySold"));
                        revenueMap.put(productID, rs.getDouble("totalRevenue"));

                        topProducts.add(product);
                    }
                }

                // Thêm danh mục vào Map, ngay cả khi không có sản phẩm
                categoryTopProducts.put(categoryName, topProducts);
                System.out.println("Added " + topProducts.size() + " products for category: " + categoryName);
            }
        } catch (Exception e) {
            System.out.println("Error in getTopProductsByCategory: " + e.getMessage());
            e.printStackTrace();
        }

        if (categoryTopProducts.isEmpty()) {
            System.out.println("No categories found.");
        } else {
            System.out.println("Total categories: " + categoryTopProducts.size());
        }

        return categoryTopProducts;
    }

    public List<Customer> getTopBuyers() {
        List<Customer> topBuyers = new ArrayList<>();
        String sql = "SELECT TOP 5 "
                + "    a.accountID, "
                + "    a.username, "
                + "    a.firstName, "
                + "    a.lastName, "
                + "    c.totalPurchasePoints, "
                + "    a.email "
                + "FROM [WIBOOKS].[dbo].[Account] a "
                + "JOIN [WIBOOKS].[dbo].[Customer] c ON a.accountID = c.customerID "
                + "WHERE a.role = 'customer' "
                + "    AND a.accountIsActive = 1 "
                + "    AND c.totalPurchasePoints > 0 "
                + // Chỉ lấy khách hàng có điểm > 0
                "ORDER BY c.totalPurchasePoints DESC;";

        try {
            ResultSet rs = context.exeQuery(sql, null);
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setAccountID(rs.getInt("accountID"));
                customer.setUsername(rs.getString("username"));
                customer.setFirstName(rs.getString("firstName"));
                customer.setLastName(rs.getString("lastName"));
                customer.setTotalPurchasePoints(rs.getDouble("totalPurchasePoints"));
                customer.setEmail(rs.getString("email"));
                topBuyers.add(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return topBuyers;
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
                + "WHERE 1=1 "; // Bỏ điều kiện chỉ lọc đơn hàng thành công ở đây
        sql += buildFilterCondition(year, quarter, month, isFilterApplied);
        System.out.println(sql);

        try {
            ResultSet rs = context.exeQuery(sql, null);
            if (rs.next()) {
                return rs.getDouble("orderConversionRate");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getTotalOrders(String year, String quarter, String month, boolean isFilterApplied) {
        String sql = "SELECT COUNT(*) AS totalOrders "
                + "FROM OrderInfo "
                + "WHERE 1=1 "; // Đếm tất cả đơn hàng
        sql += buildFilterCondition(year, quarter, month, isFilterApplied);
        try {
            ResultSet rs = context.exeQuery(sql, null);
            if (rs.next()) {
                return rs.getInt("totalOrders");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getSuccessfulOrders(String year, String quarter, String month, boolean isFilterApplied) {
        String sql = "SELECT SUM(CASE WHEN orderStatus IN ('delivered', 'completed') THEN 1 ELSE 0 END) AS successfulOrders "
                + "FROM OrderInfo "
                + "WHERE 1=1 "; // Chỉ đếm đơn hàng thành công, nhưng điều kiện lọc thời gian vẫn áp dụng
        sql += buildFilterCondition(year, quarter, month, isFilterApplied);

        try {
            ResultSet rs = context.exeQuery(sql, null);
            if (rs.next()) {
                return rs.getInt("successfulOrders");
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
                + "AND role = 'customer' "
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
        DashboardDAO dao = new DashboardDAO();

        Map<String, Double> trend = dao.getRevenueTrend("year", "2025", null, null, true);
        double total = dao.getTotalRevenue("2025", null, null, true);
        double trendSum = trend.values().stream().mapToDouble(Double::doubleValue).sum();
        System.out.println("Trend sum: " + trendSum);
        System.out.println("Total: " + total);
    }
}
