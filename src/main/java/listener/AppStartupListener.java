package listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import utils.LoggingConfig;
import java.util.logging.Logger;

@WebListener
public class AppStartupListener implements ServletContextListener {
    private static final Logger LOGGER = LoggingConfig.getLogger(AppStartupListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("Application started. Logging initialized.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("Application is shutting down.");
    }
}
