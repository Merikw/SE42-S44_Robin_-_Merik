package auction.service;

import auction.dao.ItemDAOJPAImpl;
import auction.dao.UserDAOJPAImpl;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import util.DatabaseCleaner;
import auction.domain.*;
import java.util.Iterator;
import nl.fontys.util.Money;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FurnitureAndPaintingTest {

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
    public void newFurniture() {
        String omsch = "omsch1";
        String iemand1 = "iemand1@def";
        User u1 = registrationMgr.registerUser(iemand1);
        User u2 = registrationMgr.registerUser("iemand2@def");
        Category cat = new Category("cat2");

        Item furniture1 = sellerMgr.offerFurniture(u1, cat, "broodkast", "ijzer");
        assertEquals("seller of item correct", furniture1.getSeller(), u1);

        User foundUser = registrationMgr.getUser(iemand1);
        Iterator<Item> it = foundUser.getOfferedItems();
        Item firstItem = it.next();
   //        int xxx = 22;
        assertEquals("item added in offeredItems", furniture1, firstItem);
        Item item2 = sellerMgr.offerPainting(u1, cat, omsch, "Nachtwacht", "Rembrandt");
        it = registrationMgr.getUser(iemand1).getOfferedItems();
        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
        it.next();
        assertFalse(it.hasNext());

        //de volgende code verwijderen als Item abstract is
        Item item3 = sellerMgr.offerItem(u1, new Category("boek"), "The science of Discworld");
        it = registrationMgr.getUser(iemand1).getOfferedItems();
        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
        it.next();
        assertFalse(it.hasNext());

        assertNull(furniture1.getHighestBid());
        Bid bid = auctionMgr.newBid(furniture1, u2, new Money(150000, Money.EURO));
        assertNotNull(furniture1.getHighestBid());

        Item foundFurniture = auctionMgr.getItem(furniture1.getId());
        int i = 3;
        assertEquals(foundFurniture.getHighestBid(), bid);
        assertTrue(foundFurniture.getClass() == Furniture.class);
    }
}
