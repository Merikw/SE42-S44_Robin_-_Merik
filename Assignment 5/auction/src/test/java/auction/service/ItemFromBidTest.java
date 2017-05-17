package auction.service;

import auction.dao.BidDAO;
import auction.dao.BidDAOJPAImpl;
import auction.dao.ItemDAO;
import auction.dao.ItemDAOJPAImpl;
import auction.dao.UserDAOJPAImpl;
import auction.domain.Bid;
import auction.domain.Category;
import auction.domain.Item;
import auction.domain.User;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import nl.fontys.util.Money;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import util.DatabaseCleaner;

/**
 *
 * @author Merik Westerveld & Robin Laugs - Klas S44
 */
public class ItemFromBidTest {

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
    public void testRelationBetweenBidAndItem() {
        User seller = registrationMgr.registerUser("seller@email.com");
        User buyer = registrationMgr.registerUser("buyer@email.com");

        Money money = new Money(100, "EUR");
        Category category = new Category("cat_description");
        Item item = sellerMgr.offerItem(seller, category, "description");

        Bid bid = auctionMgr.newBid(item, buyer, money);

        assertEquals(bid, item.getHighestBid());
        assertEquals(item, bid.getItem());

        ItemDAO itemDAO = new ItemDAOJPAImpl(entityManager);
        BidDAO bidDAO = new BidDAOJPAImpl(entityManager);
        
        entityManager.clear();
        
        item = itemDAO.find(item.getId());
        bid = bidDAO.find(bid.getId());

        assertEquals(bid, item.getHighestBid());
        assertEquals(item, bid.getItem());
    }

}
