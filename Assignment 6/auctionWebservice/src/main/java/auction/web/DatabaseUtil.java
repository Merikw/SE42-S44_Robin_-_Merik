package auction.web;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import util.DatabaseCleaner;

/**
 * 
 * @author Merik Westerveld & Robin Laugs - Klas S44
 */
@WebService
public class DatabaseUtil {
    
    private static final Logger logger = Logger.getLogger(DatabaseUtil.class.getSimpleName());
    
    private final DatabaseCleaner databaseCleaner;

    public DatabaseUtil(EntityManager entityManager) {
        databaseCleaner = new DatabaseCleaner(entityManager);
    }
    
    public void cleanDatabase() {
        try {
            databaseCleaner.clean();
        } catch (SQLException e) {
            logger.log(Level.WARNING, "An error occurred while cleaning the database.", e);
        }
    }
    
}
