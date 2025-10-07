package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.CartItem;
import model.product_related.Product;
import utils.DBContext;

public class CartItemDAO {
    private DBContext context;

    public CartItemDAO() {
        context = new DBContext();
    }

    public boolean addCartItem(CartItem cartItem) throws SQLException {
      
        String sql = "INSERT INTO CartItem (customerID, productID, cartItemQuantity, cartItemPrice) VALUES (?, ?, ?, ?)";
        Object[] params = {cartItem.getCustomerID(), cartItem.getProductID(), cartItem.getQuantity(), cartItem.getPriceWithQuantity()};
        int rowsAffected = context.exeNonQuery(sql, params);
        return rowsAffected > 0;
    }

    public boolean updateCartItem(CartItem cartItem) throws SQLException {
        String sql = "UPDATE CartItem SET cartItemQuantity = ?, cartItemPrice = ? WHERE itemID = ? ";
        Object[] params = {cartItem.getQuantity(), cartItem.getPriceWithQuantity(), cartItem.getItemID()};
        int rowsAffected = context.exeNonQuery(sql, params);
        return rowsAffected > 0;
    }

    public boolean deleteCartItem(int customerID, int itemID) throws SQLException {
        String sql = "DELETE FROM CartItem WHERE customerID=? AND itemID = ?";
        Object[] params = {customerID, itemID};
        int rowsAffected = context.exeNonQuery(sql, params);
        return rowsAffected > 0;
    }

    public CartItem getCartItemById(int itemID) throws SQLException {
        String sql = "SELECT * FROM CartItem WHERE itemID = ?";
        Object[] params = {itemID};
        ResultSet rs = context.exeQuery(sql, params);
        if (rs.next()) {
            return mapResultSetToCartItem(rs);
        }
        return null;
    }

    public List<CartItem> getCartItemsByCustomer(int customerID) throws SQLException {
        List<CartItem> cartItems = new ArrayList<>();
        String sql = "SELECT * FROM CartItem join Product on CartItem.productID=Product.productID WHERE customerID = ?";
        Object[] params = {customerID};
        ResultSet rs = context.exeQuery(sql, params);
        while (rs.next()) {
            cartItems.add(mapResultSetToCartItem(rs));
        }
        return cartItems;
    }

    public CartItem getCartItemByCustomerAndProduct(int customerID, int productID) throws SQLException {
        String sql = "SELECT * FROM CartItem JOIN Product ON CartItem.productID = Product.productID WHERE customerID = ? AND CartItem.productID = ?";
        Object[] params = {customerID, productID};
        try (ResultSet rs = context.exeQuery(sql, params)) {
            if (rs.next()) {
                return mapResultSetToCartItem(rs);
            }
            return null;
        }
    }

    private CartItem mapResultSetToCartItem(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setProductID(rs.getInt("productID"));
        product.setProductName(rs.getString("productName"));
        product.setPrice(rs.getDouble("price"));
        product.setStockCount(rs.getInt("stockCount"));
        product.setImageURL(rs.getString("imageURL"));
        product.setGeneralCategory(rs.getString("generalCategory"));

        CartItem cartItem = new CartItem(
                rs.getInt("itemID"),
                rs.getInt("customerID"),
                rs.getInt("productID"),
                rs.getInt("cartItemQuantity"),
                rs.getBigDecimal("cartItemPrice")
        );
        cartItem.setProduct(product);
        return cartItem;
    }
}
