//package test_controller;
//
//import controller.cart.CartController;
//import dao.CartItemDAO;
//import dao.ProductDAO;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpSession;
//import model.CartItem;
//import model.Product;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.math.BigDecimal;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.Assert.*;
//import static org.mockito.Mockito.*;
//
//@RunWith(MockitoJUnitRunner.class)
//public class CartControllerTest {

//    @Mock
//    private HttpServletRequest request;
//
//    @Mock
//    private HttpSession session;
//
//    @Mock
//    private CartItemDAO cartItemDAO;
//
//    @Mock
//    private ProductDAO productDAO;
//
//    @InjectMocks
//    private CartController cartController;
//
//    private Product product;
//    private List<CartItem> cartItems;
//
//    @Before
//    public void setUp() throws SQLException {
//        // Initialize a sample product
//        product = new Product();
//        product.setProductID(1);
//        product.setStockCount(10);
//        product.setPrice(100.0);
//        product.setProductName("Test Product");
//
//        // Initialize cartItems list
//        cartItems = new ArrayList<>();
//    }
//
//    @Test
//    public void testAddToCart_NewProduct_Success() throws SQLException, Exception {
//        // Arrange
//        int customerID = 1;
//        int productID = 1;
//        int quantity = 2;
//        BigDecimal priceWithQuantity = new BigDecimal("200.00");
//
//        when(request.getSession()).thenReturn(session);
//        when(session.getAttribute("cartItems")).thenReturn(cartItems);
//        when(cartItemDAO.getCartItemByCustomerAndProduct(customerID, productID)).thenReturn(null);
//
//        // Act
//        cartController.addToCart(request, customerID, productID, quantity, priceWithQuantity, product);
//
//        // Assert
//        verify(cartItemDAO, times(1)).addCartItem(any(CartItem.class));
//        verify(session, times(1)).setAttribute(eq("cartItems"), anyList());
//        assertEquals(1, cartItems.size());
//        assertEquals(quantity, cartItems.get(0).getQuantity());
//        assertEquals(productID, cartItems.get(0).getProductID());
//    }
//
//    @Test
//    public void testAddToCart_ExistingProduct_Success() throws SQLException, Exception {
//        // Arrange
//        int customerID = 1;
//        int productID = 1;
//        int quantity = 3;
//        BigDecimal priceWithQuantity = new BigDecimal("300.00");
//
//        CartItem existingCartItem = new CartItem(1, customerID, productID, 2, new BigDecimal("200.00"));
//        cartItems.add(existingCartItem);
//
//        when(request.getSession()).thenReturn(session);
//       
//
// when(session.getAttribute("cartItems")).thenReturn(cartItems);
//        when(cartItemDAO.getCartItemByCustomerAndProduct(customerID, productID)).thenReturn(existingCartItem);
//
//        // Act
//        cartController.addToCart(request, customerID, productID, quantity, priceWithQuantity, product);
//
//        // Assert
//        verify(cartItemDAO, times(1)).updateCartItem(any(CartItem.class));
//        verify(session, times(1)).setAttribute(eq("cartItems"), anyList());
//        assertEquals(1, cartItems.size());
//        assertEquals(5, cartItems.get(0).getQuantity());
//    }
//
//    @Test(expected = Exception.class)
//    public void testAddToCart_ProductNotFound_Failure() throws SQLException, Exception {
//        // Arrange
//        int customerID = 1;
//        int productID = 1;
//        int quantity = 2;
//        BigDecimal priceWithQuantity = new BigDecimal("200.00");
//
//        // Act
//        cartController.addToCart(request, customerID, productID, quantity, priceWithQuantity, null);
//
//        // Assert (handled by @Test(expected = Exception.class))
//    }
//
//    @Test(expected = Exception.class)
//    public void testAddToCart_InsufficientStock_Failure() throws SQLException, Exception {
//        // Arrange
//        int customerID = 1;
//        int productID = 1;
//        int quantity = 15; // Exceeds stock (10)
//        BigDecimal priceWithQuantity = new BigDecimal("1500.00");
//
//        // Act
//        cartController.addToCart(request, customerID, productID, quantity, priceWithQuantity, product);
//
//        // Assert (handled by @Test(expected = Exception.class))
//    }
//
//    @Test(expected = Exception.class)
//    public void testAddToCart_ExistingProduct_QuantityExceedsStock_Failure() throws SQLException, Exception {
//        // Arrange
//        int customerID = 1;
//        int productID = 1;
//        int quantity = 9; // Existing (2) + New (9) = 11, exceeds stock (10)
//        BigDecimal priceWithQuantity = new BigDecimal("900.00");
//
//        CartItem existingCartItem = new CartItem(1, customerID, productID, 2, new BigDecimal("200.00"));
//        cartItems.add(existingCartItem);
//
//        when(request.getSession()).thenReturn(session);
//        when(session.getAttribute("cartItems")).thenReturn(cartItems);
//        when(cartItemDAO.getCartItemByCustomerAndProduct(customerID, productID)).thenReturn(existingCartItem);
//
//        // Act
//        cartController.addToCart(request, customerID, productID, quantity, priceWithQuantity, product);
//
//        // Assert (handled by @Test(expected = Exception.class))
//    }
//
//    @Test
//    public void testAddToCart_QuantityAtStockBoundary_Success() throws SQLException, Exception {
//        // Arrange
//        int customerID = 1;
//        int productID = 1;
//        int quantity = 10; // Equal to stock (10)
//        BigDecimal priceWithQuantity = new BigDecimal("1000.00");
//
//        when(request.getSession()).thenReturn(session);
//        when(session.getAttribute("cartItems")).thenReturn(cartItems);
//        when(cartItemDAO.getCartItemByCustomerAndProduct(customerID, productID)).thenReturn(null);
//
//        // Act
//        cartController.addToCart(request, customerID, productID, quantity, priceWithQuantity, product);
//
//        // Assert
//        verify(cartItemDAO, times(1)).addCartItem(any(CartItem.class));
//        verify(session, times(1)).setAttribute(eq("cartItems"), anyList());
//        assertEquals(1, cartItems.size());
//        assertEquals(quantity, cartItems.get(0).getQuantity());
//    }
//
//   
//    @Test
//    public void testUpdateCartItem_Success() throws SQLException, Exception {
//        // Arrange
//        int customerID = 1;
//        int productID = 1;
//        int itemID = 1;
//        int newQuantity = 6;
//        BigDecimal priceWithQuantity = new BigDecimal("500.00");
//
//        CartItem cartItem = new CartItem(itemID, customerID, productID, newQuantity, priceWithQuantity);
//
//        when(cartItemDAO.updateCartItem(cartItem)).thenReturn(true);
//
//        // Act
//        cartController.updateCartItem(cartItem, product);
//
//        // Assert
//        verify(cartItemDAO, times(1)).updateCartItem(cartItem);
//    }
//
//    @Test(expected = Exception.class)
//    public void testUpdateCartItem_QuantityExceedsStock_Failure() throws SQLException, Exception {
//        // Arrange
//        int customerID = 1;
//        int productID = 1;
//        int itemID = 1;
//        int newQuantity = 11; // Exceeds stock (10) by 1
//        BigDecimal priceWithQuantity = new BigDecimal("1100.00");
//
//        CartItem cartItem = new CartItem(itemID, customerID, productID, newQuantity, priceWithQuantity);
//
//        // Act
//        cartController.updateCartItem(cartItem, product);
//
//        // Assert (handled by @Test(expected = Exception.class))
//    }
//
//    @Test
//    public void testDeleteCartItem_Success() throws SQLException {
//        // Arrange
//        int customerID = 1;
//        int itemID = 1;
//
//        when(cartItemDAO.deleteCartItem(customerID, itemID)).thenReturn(true);
//
//        // Act
//        cartController.deleteCartItem(customerID, itemID);
//
//        // Assert
//        verify(cartItemDAO, times(1)).deleteCartItem(customerID, itemID);
//    }
//
//    @Test
//    public void testDeleteCartItem_Failure() throws SQLException {
//        // Arrange
//        int customerID = 1;
//        int itemID = 1;
//
//        when(cartItemDAO.deleteCartItem(customerID, itemID)).thenReturn(false);
//
//        // Act
//        cartController.deleteCartItem(customerID, itemID);
//
//        // Assert
//        verify(cartItemDAO, times(1)).deleteCartItem(customerID, itemID);
//    }
//}