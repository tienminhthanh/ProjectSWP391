/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.provider.product;

import java.sql.SQLException;
import java.util.List;
import model.OrderProduct;
import model.Book;
import model.Product;
import dao.interfaces.IProductProvider;

/**
 *
 * @author anhkc
 */
public final class ProductFactory {
    

    public static Product getProduct(String type, int id, boolean isManagement) throws SQLException {
        IProductProvider service = ProductProviderRegistration.getProductProvider(type.toLowerCase());
        if (service == null) {
            throw new IllegalArgumentException("Unknown product type: " + type);
        }
        return service.findProduct(id, isManagement);
    }

    public static void main(String[] args) {
        try {
            Class.forName(BookProvider.class.getName());
            Class.forName(MerchProvider.class.getName());
            Product p = getProduct("merch", 165, false);
            System.out.println(p);
            
            if(p == null) return;
            
            p.getCreatorList().forEach(creator -> System.out.println(creator.toString()));
            
            List<OrderProduct> items = p.getOrderProductList();
            if (items.isEmpty()) {
                System.out.println("NO ORDER ITEMS");
            } else {
                items.forEach(item -> System.out.println(item.toString()));
            }

            if (p instanceof Book) {
                Book b = (Book) p;
                b.getGenreList().forEach(genre -> System.out.println(genre.toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
