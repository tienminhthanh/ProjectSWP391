/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.provider.product;

import dao.CreatorDAO;
import dao.MerchDAO;
import dao.OrderProductDAO;
import dao.interfaces.IProductExtraAttributesDAO;
import dao.interfaces.ISpecificProductDAO;
import java.sql.SQLException;
import model.Product;
import dao.provider.product.*;
import dao.interfaces.*;

/**
 *
 * @author anhkc
 */
public class MerchProvider implements IProductProvider {

    private static final MerchProvider instance = new MerchProvider();
    private final IProductExtraAttributesDAO creatorDAO;
    private final IProductExtraAttributesDAO orderProductDAO;
    private final ISpecificProductDAO merchDAO;
    
    
    static{
        ProductProviderRegistration.register("merch", MerchProvider.getInstance());
    }

    private MerchProvider() {
        creatorDAO = CreatorDAO.getInstance();
        orderProductDAO = OrderProductDAO.getInstance();
        merchDAO = MerchDAO.getInstance();
    }
    

    public static MerchProvider getInstance() {
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