 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/ServletListener.java to edit this template
 */
package listener;

import dao.ProductDAO;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

/**
 * Web application lifecycle listener.
 *
 * @author anhkc
 */
public class DataCacheListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        ProductDAO productDAO = new ProductDAO();
        
        try {
            
        // Load data and store in application scope
        context.setAttribute("categories", productDAO.getAllCategories());
        context.setAttribute("creators", productDAO.getAllCreators());
        context.setAttribute("genres", productDAO.getAllGenres());
        context.setAttribute("publishers", productDAO.getAllPublishers());
        context.setAttribute("brands", productDAO.getAllBrands());
        context.setAttribute("series", productDAO.getAllSeries());
        context.setAttribute("characters", productDAO.getAllCharacters());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        System.out.println("Context destroyed successfully!");
    }
}
