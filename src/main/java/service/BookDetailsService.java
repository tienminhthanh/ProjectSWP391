/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.BookDAO;
import dao.CreatorDAO;
import dao.GenreDAO;
import dao.OrderProductDAO;
import dao.interfaces.*;
import java.sql.SQLException;
import java.util.List;
import model.product_related.Book;
import model.product_related.Genre;
import model.product_related.Product;
import service.interfaces.*;
import service.registration.ProductDetailsServiceRegistration;

/**
 *
 * @author anhkc
 */
public class BookDetailsService implements IProductDetailsService {

    private static final BookDetailsService instance = new BookDetailsService();
    private final IProductExtraAttributesDAO genreDAO;
    private final IProductExtraAttributesDAO creatorDAO;
    private final IProductExtraAttributesDAO orderProductDAO;
    private final ISpecificProductDAO bookDAO;
    
    static{
        ProductDetailsServiceRegistration.register("book", BookDetailsService.getInstance());
    }
    
    //Normal run
    private BookDetailsService() {
        genreDAO = GenreDAO.getInstance();
        creatorDAO = CreatorDAO.getInstance();
        orderProductDAO = OrderProductDAO.getInstance();
        bookDAO = BookDAO.getInstance();
    }

    public static BookDetailsService getInstance() {
        return instance;
    }

    @Override
    public void loadExtraAttributes(Product product, int id) throws SQLException {
        product.setCreatorList(creatorDAO.getExtraAttributesByProductID(id))
                .setOrderProductList(orderProductDAO.getExtraAttributesByProductID(id));
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
        loadExtraAttributes(product, id);
        return product;
    }
}
