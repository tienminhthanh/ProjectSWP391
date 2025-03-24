/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package test_dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import dao.*;
import model.*;
import org.mockito.invocation.InvocationOnMock;
import utils.*;

/**
 *
 * @author anhkc
 */
public class ProductDAOTest {

    private ProductDAO dao;
    private DBContext context; // Mocked database connection
    private Utility tool;
    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;
    private Product newBook;
    private Product newMerch;

    @Before
    public void setUp() throws SQLException {
        context = mock(DBContext.class); // Mock DBContext
        tool = new Utility();
        dao = new ProductDAO(context, tool);
        
        newBook = new Book()
        .setDuration("200 pages")
        .setProductName("New Book")
        .setPrice(99000)
        .setStockCount(5)
        .setSpecificCategory(new Category(1, "Light Novel", "book"))
        .setDescription("A test book")
        .setReleaseDate(LocalDate.now())
        .setSpecialFilter("new")
        .setAdminID(1)
        .setKeywords("book keywords")
        .setGeneralCategory("book")
        .setIsActive(true)
        .setImageURL("image.jpg");
        
        newMerch = new Merchandise()
        .setSize("H144mm")
        .setScaleLevel("1/7")
        .setMaterial("Plastic")
        .setProductName("New Merch")
        .setPrice(699000)
        .setStockCount(3)
        .setSpecificCategory(new Category(3, "Figure", "merch"))
        .setDescription("A test merch")
        .setReleaseDate(LocalDate.now())
        .setSpecialFilter("new")
        .setAdminID(1)
        .setKeywords("merch keywords")
        .setGeneralCategory("merch")
        .setIsActive(true)
        .setImageURL("image.jpg");
        
        
        mockConnection = mock(Connection.class);
        mockStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        // Mock the connection setup
        when(context.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(context.exeQuery(anyString(), any(Object[].class))).thenReturn(mockResultSet);
        when(context.exeQuery(eq(mockStatement), any(Object[].class))).thenReturn(mockResultSet);
    }

    @After
    public void tearDown() {
        dao = null;
    }

    @Test
    public void testChangeProductStatus_Success() throws SQLException {
        int productID = 1;
        boolean newStatus = true;

        // Simulate successful update (rows affected > 0)
        when(context.exeNonQuery(anyString(), any())).thenReturn(1);

        boolean result = dao.changeProductStatus(productID, newStatus);

        assertTrue(result);
        verify(context, times(1)).exeNonQuery(anyString(), any());
    }

    @Test
    public void testChangeProductStatus_Failure() throws SQLException {
        int productID = 2;
        boolean newStatus = false;

        // Simulate failed update (no rows affected)
        when(context.exeNonQuery(anyString(), any())).thenReturn(0);

        boolean result = dao.changeProductStatus(productID, newStatus);

        assertFalse(result);
        verify(context, times(1)).exeNonQuery(anyString(), any());
    }

    @Test
    public void testChangeProductStatus_SQLException() throws SQLException {
        int productID = 3;
        boolean newStatus = false;

        // Simulate SQL exception
        when(context.exeNonQuery(anyString(), any())).thenThrow(new SQLException("DB Error"));

        try {
            dao.changeProductStatus(productID, newStatus);
            fail("Expected SQLException to be thrown");
        } catch (SQLException e) {
            assertEquals("DB Error", e.getMessage());
        }
    }

    @Test
    public void testGetProductById_Success() throws SQLException {
        int productID = 1;

        // Mock ResultSet behavior
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt("productID")).thenReturn(productID);
        when(mockResultSet.getString("productName")).thenReturn("Test Product");
        when(mockResultSet.getDouble("price")).thenReturn(19.99);
        when(mockResultSet.getInt("stockCount")).thenReturn(10);
        when(mockResultSet.getInt("categoryID")).thenReturn(1);
        when(mockResultSet.getString("categoryName")).thenReturn("Books");
        when(mockResultSet.getBoolean("productIsActive")).thenReturn(true);

        Product result = dao.getProductById(productID);

        assertNotNull(result);
        assertEquals(productID, result.getProductID());
        assertEquals("Test Product", result.getProductName());
        assertEquals(19.99, result.getPrice(), 0.01);
        verify(context, times(1)).exeQuery(anyString(), any());
    }

    @Test
    public void testGetProductById_NotFound() throws SQLException {
        int productID = 999;

        // Simulate no results
        when(mockResultSet.next()).thenReturn(false);

        Product result = dao.getProductById(productID);

        assertNull(result);
        verify(context, times(1)).exeQuery(anyString(), any());
    }

    @Test
    public void testGetSearchResult_Success() throws SQLException {
        String query = "test";
        String type = "book";
        String sortCriteria = "relevance";
        Map<String, String> filterMap = Collections.emptyMap();
        int page = 1;
        int pageSize = 10;

        // Mock ResultSet behavior
        when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false); // 2 results
        when(mockResultSet.getInt("productID")).thenReturn(1).thenReturn(2);
        when(mockResultSet.getString("productName")).thenReturn("Book 1").thenReturn("Book 2");
        when(mockResultSet.getDouble("price")).thenReturn(15.99).thenReturn(25.99);
        when(mockResultSet.getInt("categoryID")).thenReturn(1);
        when(mockResultSet.getString("categoryName")).thenReturn("Books");

        List<Product> result = dao.getSearchResult(query, type, sortCriteria, filterMap, page, pageSize);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Book 1", result.get(0).getProductName());
        assertEquals("Book 2", result.get(1).getProductName());
        verify(mockConnection, times(1)).prepareStatement(anyString());
    }

    @Test
    public void testGetSearchResult_NoResults() throws SQLException {
        String query = "nonexistent";
        String type = "merch";
        String sortCriteria = "priceLowToHigh";
        Map<String, String> filterMap = Collections.emptyMap();
        int page = 1;
        int pageSize = 10;

        // Simulate no results
        when(mockResultSet.next()).thenReturn(false);

        List<Product> result = dao.getSearchResult(query, type, sortCriteria, filterMap, page, pageSize);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mockConnection, times(1)).prepareStatement(anyString());
    }

@Test
public void testAddNewProducts_Book_Success() throws SQLException {
    Product newProduct = newBook;
    
    Object[] dataArray = { 
        new Creator().setCreatorName("Author Name").setCreatorRole("author"),
        new Creator().setCreatorID(2),
        new Genre().setGenreID(1),
        new Genre().setGenreID(3),
        new Publisher().setPublisherName("Publisher Name")
    };

    // Mock successful inserts with all matchers
    when(context.exeNonQuery(eq(mockConnection), anyString(), any(Object[].class), eq(true))).thenReturn(1); // Product,creator,publisher insert
    when(context.exeNonQuery(eq(mockConnection), anyString(), any(Object[].class), eq(false))).thenReturn(1); // creator,genre association and final update

    boolean result = dao.addNewProducts(newProduct, dataArray);

    assertTrue(result);
    verify(mockConnection, times(1)).setAutoCommit(false);
    verify(mockConnection, times(1)).commit();
    verify(mockConnection, times(1)).setAutoCommit(true);
    verify(context, times(8)).exeNonQuery(eq(mockConnection), anyString(), any(Object[].class), anyBoolean()); // Product, Creator, Book update
}

@Test
public void testAddNewProducts_Merch_Success() throws SQLException {
    Product newProduct = newMerch;
    Object[] dataArray =  { 
        new Creator().setCreatorName("Sculptor Name").setCreatorRole("sculptor"),
        new Creator().setCreatorID(3),
        new Series(0, "Test Series"),
        new OGCharacter(0, "Test Character"), 
        new Brand(0, "Test Brand") 
    };

    // Mock successful inserts with all matchers
    when(context.exeNonQuery(eq(mockConnection), anyString(), any(Object[].class), eq(true))).thenReturn(1); // Product, Creator, Brand, Series, Character insert
    when(context.exeNonQuery(eq(mockConnection), anyString(), any(Object[].class), eq(false))).thenReturn(1); // Association and Final update

    boolean result = dao.addNewProducts(newProduct, dataArray);

    assertTrue(result);
    verify(mockConnection, times(1)).setAutoCommit(false);
    verify(mockConnection, times(1)).commit();
    verify(mockConnection, times(1)).setAutoCommit(true);
    verify(context, times(8)).exeNonQuery(eq(mockConnection), anyString(), any(Object[].class), anyBoolean()); 
}

@Test
public void testAddNewProducts_Book_Failure() throws SQLException {
     Product newProduct = newBook;
    
    Object[] dataArray = { 
        new Creator().setCreatorName("Author Name").setCreatorRole("author"),
        new Creator().setCreatorID(2),
        new Genre().setGenreID(1),
        new Genre().setGenreID(3),
        new Publisher().setPublisherName("Publisher Name")
    };
    
    // Mock final update failure
    when(context.exeNonQuery(eq(mockConnection), anyString(), any(Object[].class), anyBoolean()))
            .thenAnswer((InvocationOnMock answer) -> {
                String sql = answer.getArgument(1, String.class);
                boolean returnGeneratedKeys = answer.getArgument(3, Boolean.class);
                if(sql.toUpperCase().startsWith("UPDATE") && !returnGeneratedKeys){
                    return 0;
                }
                return 1;
            });

    boolean result = dao.addNewProducts(newProduct, dataArray);

    assertFalse(result);
    verify(mockConnection, times(1)).setAutoCommit(false);
    verify(mockConnection, times(1)).rollback();
    verify(mockConnection, times(1)).setAutoCommit(true);
    verify(context, times(8)).exeNonQuery(eq(mockConnection), anyString(), any(Object[].class), anyBoolean()); 
}

@Test
public void testAddNewProducts_Merch_Failure() throws SQLException {
     Product newProduct = newMerch;
    Object[] dataArray =  { 
        new Creator().setCreatorName("Sculptor Name").setCreatorRole("sculptor"),
        new Creator().setCreatorID(3),
        new Series(0, "Test Series"),
        new OGCharacter(0, "Test Character"), 
        new Brand(0, "Test Brand") 
    };

    // Mock final update failure
    when(context.exeNonQuery(eq(mockConnection), anyString(), any(Object[].class), anyBoolean()))
            .thenAnswer((InvocationOnMock answer) -> {
                String sql = answer.getArgument(1, String.class);
                boolean returnGeneratedKeys = answer.getArgument(3, Boolean.class);
                if(sql.toUpperCase().startsWith("UPDATE") && !returnGeneratedKeys){
                    return 0;
                }
                return 1;
            });

    boolean result = dao.addNewProducts(newProduct, dataArray);

    assertFalse(result);
    verify(mockConnection, times(1)).setAutoCommit(false);
    verify(mockConnection, times(1)).rollback();
    verify(mockConnection, times(1)).setAutoCommit(true);
    verify(context, times(8)).exeNonQuery(eq(mockConnection), anyString(), any(Object[].class), anyBoolean()); 
}

@Test
public void testAddNewProducts_Product_SQLException() throws SQLException {
   Product newProduct = newBook;
    
    Object[] dataArray = { 
        new Creator().setCreatorName("Author Name").setCreatorRole("author"),
        new Creator().setCreatorID(2),
        new Genre().setGenreID(1),
        new Genre().setGenreID(3),
        new Publisher().setPublisherName("Publisher Name")
    };

    // Simulate SQLException on product insert
    when(context.exeNonQuery(eq(mockConnection), anyString(), any(Object[].class), anyBoolean())).thenReturn(0);

    try {
        dao.addNewProducts(newProduct, dataArray);
        fail("Expected SQLException to be thrown");
    } catch (SQLException e) {
        assertTrue(e.getMessage().contains("Failed to add this product!"));
    }
    verify(mockConnection, times(1)).setAutoCommit(false);
    verify(mockConnection, times(1)).rollback();
    verify(mockConnection, times(1)).setAutoCommit(true);
    verify(context, times(1)).exeNonQuery(eq(mockConnection), anyString(), any(Object[].class), eq(true));
}


@Test
public void testAddNewProducts_Creator_SQLException() throws SQLException {
   Product newProduct = newBook;
    
    Object[] dataArray = { 
        new Creator().setCreatorName("Author Name").setCreatorRole("author"),
        new Creator().setCreatorID(2),
        new Genre().setGenreID(1),
        new Genre().setGenreID(3),
        new Publisher().setPublisherName("Publisher Name")
    };

    // Simulate SQLException on creator insert
    when(context.exeNonQuery(eq(mockConnection), anyString(), any(Object[].class), anyBoolean()))
            .thenAnswer(answer->{
                String sql = answer.getArgument(1,String.class);
                boolean returnGeneratedKeys = answer.getArgument(3,Boolean.class);
                if(sql.startsWith("INSERT INTO [dbo].[Creator]") && returnGeneratedKeys){
                    return 0;
                }
                return 1;
            });

    try {
        dao.addNewProducts(newProduct, dataArray);
        fail("Expected SQLException to be thrown");
    } catch (SQLException e) {
        assertTrue(e.getMessage().contains("Error adding creator: "));
    }
    verify(mockConnection, times(1)).setAutoCommit(false);
    verify(mockConnection, times(1)).rollback();
    verify(mockConnection, times(1)).setAutoCommit(true);
    verify(context, times(2)).exeNonQuery(eq(mockConnection), anyString(), any(Object[].class), eq(true));
    
    
    // Simulate SQLException on creator association
    when(context.exeNonQuery(eq(mockConnection), anyString(), any(Object[].class), anyBoolean()))
            .thenAnswer(answer->{
                if(answer.getArgument(3,Boolean.class)){
                    return 1;
                }
                return 0;
            });

    try {
        dao.addNewProducts(newProduct, dataArray);
        fail("Expected SQLException to be thrown");
    } catch (SQLException e) {
        assertTrue(e.getMessage().contains("Error assigning creatorID "));
    }
    verify(mockConnection, times(2)).setAutoCommit(false);
    verify(mockConnection, times(2)).rollback();
    verify(mockConnection, times(2)).setAutoCommit(true);
    verify(context, times(5)).exeNonQuery(eq(mockConnection), anyString(), any(Object[].class), anyBoolean());
}

@Test
public void testAddNewProducts_Genre_SQLException() throws SQLException {
   Product newProduct = newBook;
    
    Object[] dataArray = { 
        new Genre().setGenreID(1),
        new Genre().setGenreID(3),
        new Creator().setCreatorName("Author Name").setCreatorRole("author"),
        new Creator().setCreatorID(2),
        new Publisher().setPublisherName("Publisher Name")
    };

    // Simulate SQLException on product insert
    when(context.exeNonQuery(eq(mockConnection), anyString(), any(Object[].class), anyBoolean()))
            .thenAnswer(answer->{
                if(answer.getArgument(3,Boolean.class)){
                    return 1;
                }
                return 0;
            });

    try {
        dao.addNewProducts(newProduct, dataArray);
        fail("Expected SQLException to be thrown");
    } catch (SQLException e) {
        assertTrue(e.getMessage().contains("Error assigning genreID "));
    }
    verify(mockConnection, times(1)).setAutoCommit(false);
    verify(mockConnection, times(1)).rollback();
    verify(mockConnection, times(1)).setAutoCommit(true);
    verify(context, times(2)).exeNonQuery(eq(mockConnection), anyString(), any(Object[].class),anyBoolean());
}

@Test
public void testAddNewProducts_Publisher_SQLException() throws SQLException {
   Product newProduct = newBook;
    
    Object[] dataArray = { 
        new Publisher().setPublisherName("Publisher Name"),
        new Genre().setGenreID(3),
        new Creator().setCreatorName("Author Name").setCreatorRole("author"),
        new Creator().setCreatorID(2),
        new Genre().setGenreID(1)
    };

     // Simulate SQLException on series insert
    when(context.exeNonQuery(eq(mockConnection), anyString(), any(Object[].class), eq(true)))
            .thenAnswer(ans -> {
                if(ans.getArgument(1,String.class).startsWith("INSERT INTO [dbo].[Publisher]")){
                    return 0;
                }
                return 1;
            });
        

    try {
        dao.addNewProducts(newProduct, dataArray);
        fail("Expected SQLException to be thrown");
    } catch (SQLException e) {
        assertTrue(e.getMessage().contains("Error adding publisher: "));
    }
    
    verify(mockConnection, times(1)).setAutoCommit(false);
    verify(mockConnection, times(1)).rollback();
    verify(mockConnection, times(1)).setAutoCommit(true);
    verify(context, times(2)).exeNonQuery(eq(mockConnection), anyString(), any(Object[].class), eq(true));
}

@Test
public void testAddNewProducts_Series_SQLException() throws SQLException {
   Product newProduct = newMerch;
    Object[] dataArray =  { 
        new Series(0, "Test Series"),
        new Creator().setCreatorName("Sculptor Name").setCreatorRole("sculptor"),
        new Creator().setCreatorID(3),
        new OGCharacter(0, "Test Character"), 
        new Brand(0, "Test Brand") 
    };

    // Simulate SQLException on series insert
    when(context.exeNonQuery(eq(mockConnection), anyString(), any(Object[].class), eq(true)))
            .thenAnswer(ans -> {
                if(ans.getArgument(1,String.class).startsWith("INSERT INTO [dbo].[Series]")){
                    return 0;
                }
                return 1;
            });
        

    try {
        dao.addNewProducts(newProduct, dataArray);
        fail("Expected SQLException to be thrown");
    } catch (SQLException e) {
        assertTrue(e.getMessage().contains("Error adding merch series: "));
    }
    
    verify(mockConnection, times(1)).setAutoCommit(false);
    verify(mockConnection, times(1)).rollback();
    verify(mockConnection, times(1)).setAutoCommit(true);
    verify(context, times(2)).exeNonQuery(eq(mockConnection), anyString(), any(Object[].class), eq(true));
}
@Test
public void testAddNewProducts_Character_SQLException() throws SQLException {
   Product newProduct = newMerch;
    Object[] dataArray =  { 
        new OGCharacter(0, "Test Character"), 
        new Series(0, "Test Series"),
        new Creator().setCreatorName("Sculptor Name").setCreatorRole("sculptor"),
        new Creator().setCreatorID(3),
        new Brand(0, "Test Brand") 
    };

    // Simulate SQLException on series insert
    when(context.exeNonQuery(eq(mockConnection), anyString(), any(Object[].class), eq(true)))
            .thenAnswer(ans -> {
                if(ans.getArgument(1,String.class).startsWith("INSERT INTO [dbo].[Character]")){
                    return 0;
                }
                return 1;
            });
        

    try {
        dao.addNewProducts(newProduct, dataArray);
        fail("Expected SQLException to be thrown");
    } catch (SQLException e) {
        assertTrue(e.getMessage().contains("Error adding merch character: "));
    }
    
    verify(mockConnection, times(1)).setAutoCommit(false);
    verify(mockConnection, times(1)).rollback();
    verify(mockConnection, times(1)).setAutoCommit(true);
    verify(context, times(2)).exeNonQuery(eq(mockConnection), anyString(), any(Object[].class), eq(true));
}
@Test
public void testAddNewProducts_Brand_SQLException() throws SQLException {
   Product newProduct = newMerch;
    Object[] dataArray =  { 
        new Brand(0, "Test Brand"),
        new Series(0, "Test Series"),
        new Creator().setCreatorName("Sculptor Name").setCreatorRole("sculptor"),
        new Creator().setCreatorID(3),
        new OGCharacter(0, "Test Character")
    };

    // Simulate SQLException on series insert
    when(context.exeNonQuery(eq(mockConnection), anyString(), any(Object[].class), eq(true)))
            .thenAnswer(ans -> {
                if(ans.getArgument(1,String.class).startsWith("INSERT INTO [dbo].[Brand]")){
                    return 0;
                }
                return 1;
            });
        

    try {
        dao.addNewProducts(newProduct, dataArray);
        fail("Expected SQLException to be thrown");
    } catch (SQLException e) {
        assertTrue(e.getMessage().contains("Error adding merch brand: "));
    }
    
    verify(mockConnection, times(1)).setAutoCommit(false);
    verify(mockConnection, times(1)).rollback();
    verify(mockConnection, times(1)).setAutoCommit(true);
    verify(context, times(2)).exeNonQuery(eq(mockConnection), anyString(), any(Object[].class), eq(true));
}
}