package auction.service;

import auction.dao.ItemDAOJPAImpl;
import auction.dao.UserDAOJPAImpl;
import javax.persistence.*;
import util.DatabaseCleaner;
import auction.domain.Category;
import auction.domain.Item;
import auction.domain.User;
import java.util.Iterator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 * @author Merik Westerveld & Robin Laugs - Klas S44
 */
public class ItemsFromSellerTest {

    private static final String PERSISTENCE_UNIT_NAME = "auction";
    
    private EntityManager entityManager;
    
    private RegistrationMgr registrationMgr;
    private AuctionMgr auctionMgr;
    private SellerMgr sellerMgr;

    @Before
    public void setUp() throws Exception {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        entityManager = entityManagerFactory.createEntityManager();

        DatabaseCleaner databaseCleaner = new DatabaseCleaner(entityManager);
        databaseCleaner.clean();
                
        registrationMgr = new RegistrationMgr(new UserDAOJPAImpl(entityManager));
        auctionMgr = new AuctionMgr(new ItemDAOJPAImpl(entityManager));
        sellerMgr = new SellerMgr(new ItemDAOJPAImpl(entityManager));
    }

    @Test
    public void numberOfOfferedItems() {
        String email = "ifu1@nl";
        String omsch1 = "omsch_ifu1";
        String omsch2 = "omsch_ifu2";

        User user1 = registrationMgr.registerUser(email);
        assertEquals(0, user1.numberOfOfferedItems());

        Category cat = new Category("cat2");
        Item item1 = sellerMgr.offerItem(user1, cat, omsch1);

        // test number of items belonging to user1
        assertEquals(1, user1.numberOfOfferedItems());

        /*
         *  expected: which one of te above two assertions do you expect to be true?
         *  QUESTION:
         *    Explain the result in terms of entity manager and persistance context.
         * 
         *  De user wordt meegegeven aan het item waardoor het item automatisch wordt toegevoegd aan de user, vandaag kan het aantal offered items nooit 0 zijn.
         */
        assertEquals(1, item1.getSeller().numberOfOfferedItems());

        User user2 = registrationMgr.getUser(email);
        assertEquals(1, user2.numberOfOfferedItems());
        Item item2 = sellerMgr.offerItem(user2, cat, omsch2);
        assertEquals(2, user2.numberOfOfferedItems());

        User user3 = registrationMgr.getUser(email);
        assertEquals(2, user3.numberOfOfferedItems());

        User userWithItem = item2.getSeller();
        assertEquals(2, userWithItem.numberOfOfferedItems());
        /*
         *  expected: which one of te above two assertions do you expect to be true?
         *  QUESTION:
         *    Explain the result in terms of entity manager and persistance context.
         *
         *  // Er wordt nergens een derde item aan de seller toegevoegd.       
         */

        assertSame(user3, userWithItem); // Users zijn gelijk aan elkaar omdat ze beiden uit de persistence context worden opgehaald waarin dezelfde user al gecached is.
        assertEquals(user3, userWithItem);
    }

    @Test
    public void getItemsFromSeller() {
        String email = "ifu1@nl";
        String omsch1 = "omsch_ifu1";
        String omsch2 = "omsch_ifu2";

        Category cat = new Category("cat2");

        User user10 = registrationMgr.registerUser(email);
        Item item10 = sellerMgr.offerItem(user10, cat, omsch1);
        Iterator<Item> it = user10.getOfferedItems();
        // testing number of items of java object
        assertTrue(it.hasNext());

        // now testing number of items for same user fetched from db.
        User user11 = registrationMgr.getUser(email);
        Iterator<Item> it11 = user11.getOfferedItems();
        assertTrue(it11.hasNext());
        it11.next();
        assertFalse(it11.hasNext());

        // Explain difference in above two tests for the iterator of 'same' user:
        // Na het vragen van de .hasNext() wordt deze uit de iterator gehaald waardoor hij na de .hasNext() false aangeeft als er weer een .hasNext() wordt aangroepen.
        User user20 = registrationMgr.getUser(email);
        Item item20 = sellerMgr.offerItem(user20, cat, omsch2);
        Iterator<Item> it20 = user20.getOfferedItems();
        assertTrue(it20.hasNext());
        it20.next();
        assertTrue(it20.hasNext());

        User user30 = item20.getSeller();
        Iterator<Item> it30 = user30.getOfferedItems();
        assertTrue(it30.hasNext());
        it30.next();
        assertTrue(it30.hasNext());

    }
}
