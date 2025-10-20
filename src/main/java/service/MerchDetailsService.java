/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.CreatorDAO;
import dao.MerchDAO;
import dao.OrderProductDAO;
import dao.interfaces.IProductExtraAttributesDAO;
import dao.interfaces.ISpecificProductDAO;
import java.sql.SQLException;
import model.product_related.Product;
import service.interfaces.*;
import service.registration.ProductDetailsServiceRegistration;

/**
 *
 * @author anhkc
 */
public class MerchDetailsService implements IProductDetailsService {

    private static final MerchDetailsService instance = new MerchDetailsService();
    private final IProductExtraAttributesDAO creatorDAO;
    private final IProductExtraAttributesDAO orderProductDAO;
    private final ISpecificProductDAO merchDAO;
    
    
    static{
        ProductDetailsServiceRegistration.register("merch", MerchDetailsService.getInstance());
    }

    private MerchDetailsService() {
        creatorDAO = CreatorDAO.getInstance();
        orderProductDAO = OrderProductDAO.getInstance();
        merchDAO = MerchDAO.getInstance();
    }
    

    public static MerchDetailsService getInstance() {
        return instance;
    }

    @Override
    public void loadExtraAttributes(Product product, int id) throws SQLException {
        product.setCreatorList(creatorDAO.getExtraAttributesByProductID(id))
                .setOrderProductList(orderProductDAO.getExtraAttributesByProductID(id));
    }

    @Override
    public Product findProduct(int id, boolean isManagement) throws SQLException {
        Product product = merchDAO.getProductById(id, isManagement);
        loadExtraAttributes(product, id);
        return product;
    }
}