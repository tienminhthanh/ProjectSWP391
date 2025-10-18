/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/ServletListener.java to edit this template
 */
package listener;

import dao.BrandDAO;
import dao.CategoryDAO;
import dao.CharacterDAO;
import dao.CreatorDAO;
import dao.GenreDAO;
import dao.PublisherDAO;
import dao.SeriesDAO;
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
        CategoryDAO ctgrDAO = CategoryDAO.getInstance();
        CreatorDAO creatorDAO = CreatorDAO.getInstance();
        GenreDAO genreDAO = GenreDAO.getInstance();
        PublisherDAO publisherDAO = PublisherDAO.getInstance();
        BrandDAO brandDAO = BrandDAO.getInstance();
        SeriesDAO seriesDAO = SeriesDAO.getInstance();
        CharacterDAO characterDAO = CharacterDAO.getInstance();

        try {
            // Load data and store in application scope
            context.setAttribute("categories", ctgrDAO.getAllClassficationEntitiesWithCount());
            context.setAttribute("creators", creatorDAO.getAllClassficationEntitiesWithCount());
            context.setAttribute("genres", genreDAO.getAllClassficationEntitiesWithCount());
            context.setAttribute("publishers", publisherDAO.getAllClassficationEntitiesWithCount());
            context.setAttribute("brands", brandDAO.getAllClassficationEntitiesWithCount());
            context.setAttribute("series", seriesDAO.getAllClassficationEntitiesWithCount());
            context.setAttribute("characters", characterDAO.getAllClassficationEntitiesWithCount());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        System.out.println("Context destroyed successfully!");
    }
}
