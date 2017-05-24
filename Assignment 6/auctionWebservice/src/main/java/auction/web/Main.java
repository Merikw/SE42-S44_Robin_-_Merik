package auction.web;

import auction.dao.ItemDAO;
import auction.dao.ItemDAOJPAImpl;
import auction.dao.UserDAO;
import auction.dao.UserDAOJPAImpl;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.xml.ws.Endpoint;

/**
 *
 * @author Merik Westerveld & Robin Laugs - Klas S44
 */
public class Main {

    private static final String PERSISTENCE_UNIT_NAME = "auction";
    private static final String ADDRESS = "http://localhost:8080/";

    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        UserDAO userDAOContext = new UserDAOJPAImpl(entityManager);
        ItemDAO itemDAOContext = new ItemDAOJPAImpl(entityManager);

        Endpoint.publish(ADDRESS + Registration.class.getSimpleName(), new Registration(userDAOContext));
        Endpoint.publish(ADDRESS + Auction.class.getSimpleName(), new Auction(itemDAOContext));
    }

}
