package listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.util.logging.Level;
import utils.LoggingConfig;
import java.util.logging.Logger;
import utils.ProductProviderInitializer;
import dao.factory_product.ProductProviderRegistration;

@WebListener
public class AppStartupListener implements ServletContextListener {

    private static final Logger LOGGER = LoggingConfig.getLogger(AppStartupListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("Application started. Logging initialized.");
        try {
            ProductProviderInitializer.init();
            
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "An error occured during product type initilization: {0}", e.toString());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("Application is shutting down.");
    }
}
