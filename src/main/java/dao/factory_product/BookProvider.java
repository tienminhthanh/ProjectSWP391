/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.factory_product;

import dao.BookDAO;
import dao.CreatorDAO;
import dao.GenreDAO;
import dao.OrderProductDAO;
import dao.interfaces.*;
import java.sql.SQLException;
import java.util.List;
import model.Book;
import model.Genre;
import model.Product;

/**
 *
 * @author anhkc
 */
public class BookProvider implements IProductProvider {

    private static final BookProvider instance = new BookProvider();
    private final IProductExtraAttributesDAO genreDAO;
    private final IProductExtraAttributesDAO creatorDAO;
    private final IProductExtraAttributesDAO orderProductDAO;
    private final ISpecificProductDAO bookDAO;

    static {
        ProductProviderRegistration.register("book", BookProvider.getInstance());
    }

    //Normal run
    private BookProvider() {
        genreDAO = GenreDAO.getInstance();
        creatorDAO = CreatorDAO.getInstance();
        orderProductDAO = OrderProductDAO.getInstance();
        bookDAO = BookDAO.getInstance();
    }

    public static BookProvider getInstance() {
        return instance;
    }

    @Override
    public void loadExtraAttributes(Product product, int id, boolean isManagement) throws SQLException {
        product.setCreatorList(creatorDAO.getExtraAttributesByProductID(id));
        product.setOrderProductList(orderProductDAO.getExtraAttributesByProductID(id));
        // Only run if the product is actually a Book (optional safety check)
        if (product instanceof Book) {
            List<Genre> genreList = genreDAO.getExtraAttributesByProductID(id);
            // You'll need a way to set these, see step D.
            ((Book) product).setGenreList(genreList);
        }
    }

    @Override
    public Product findProduct(int id, boolean isManagement) throws SQLException {
        Product product = bookDAO.getProductById(id, isManagement);
        loadExtraAttributes(product, id, isManagement);
        return product;
    }

}
