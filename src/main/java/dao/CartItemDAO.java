package dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.CartItem;
import utils.DBContext;

public class CartItemDAO {

    private DBContext context;

    public CartItemDAO() {
        context = new DBContext();
    }

    public boolean addCartItem(CartItem cartItem) throws SQLException {
        String sql = "INSERT INTO CartItem (customerID, productID, quantity, priceWithQuantity) VALUES (?, ?, ?, ?)";
        Object[] params = {cartItem.getCustomerID(), cartItem.getProductID(), cartItem.getQuantity(), cartItem.getPriceWithQuantity()};
        int rowsAffected = context.exeNonQuery(sql, params);
        return rowsAffected > 0;
    }

    public boolean updateCartItem(CartItem cartItem) throws SQLException {
        String sql = "UPDATE CartItem SET quantity = ?, priceWithQuantity = ? WHERE itemID = ?";
        Object[] params = {cartItem.getQuantity(), cartItem.getPriceWithQuantity(), cartItem.getItemID()};
        int rowsAffected = context.exeNonQuery(sql, params);
        return rowsAffected > 0;
    }

    public boolean deleteCartItem(int itemID) throws SQLException {
        String sql = "DELETE FROM CartItem WHERE itemID = ?";
        Object[] params = {itemID};
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
        String sql = "SELECT * FROM CartItem WHERE customerID = ?";
        Object[] params = {customerID};
        ResultSet rs = context.exeQuery(sql, params);
        while (rs.next()) {
            cartItems.add(mapResultSetToCartItem(rs));
        }
        return cartItems;
    }

    private CartItem mapResultSetToCartItem(ResultSet rs) throws SQLException {
        return new CartItem(
                rs.getInt("itemID"),
                rs.getInt("customerID"),
                rs.getInt("productID"),
                rs.getInt("quantity"),
                rs.getBigDecimal("priceWithQuantity")
        );
    }
}
