package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.logging.*;

public class LoggingConfig {
    private static final Logger LOGGER = Logger.getLogger("WIBOOKS");
    
    static {
        try {
            // Define log directory
            String logDir = System.getProperty("user.home") != null 
                    ? System.getProperty("user.home") + "/WIBOOKS/logs"
                    :"WIBOOKS/logs";
            Files.createDirectories(Paths.get(logDir));

            // Define log file pattern (rotating logs)
            FileHandler fileHandler = new FileHandler(logDir + "/" + LocalDate.now() + ".log", true);
            fileHandler.setFormatter(new SimpleFormatter());

            Logger rootLogger = Logger.getLogger("");

            // Add file handler to root logger
            rootLogger.addHandler(fileHandler);
            rootLogger.setLevel(Level.INFO); // Set logging level (e.g., INFO, WARNING)

        } catch (IOException e) {
            System.err.println("Failed to initialize logger: " + e.getMessage());
        }
    }

    public static Logger getLogger(Class<?> clazz) {
        return Logger.getLogger(clazz.getName());
    }
}
