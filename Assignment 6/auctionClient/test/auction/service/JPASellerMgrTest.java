package auction.service;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import auction.web.Auction;
import auction.web.AuctionService;
import auction.web.Category;
import auction.web.DatabaseUtil;
import auction.web.DatabaseUtilService;
import auction.web.Item;
import auction.web.Money;
import auction.web.Registration;
import auction.web.RegistrationService;
import auction.web.User;

/**
 *
 * @author Merik Westerveld & Robin Laugs - Klas S44
 */
public class JPASellerMgrTest {

    private Registration registration;
    private Auction auction;
    private DatabaseUtil datebaseUtil;

    @Before
    public void setUp() {
        registration = new RegistrationService().getRegistrationPort();
        auction = new AuctionService().getAuctionPort();
        datebaseUtil = new DatabaseUtilService().getDatabaseUtilPort();
        datebaseUtil.cleanDatabase();
    }

    /**
     * Test of offerItem method, of class SellerMgr.
     */
    @Test
    public void testOfferItem() {
        String omsch = "omsch";

        User user1 = registration.registerUser("xx@nl");
        Category cat = new Category();
        cat.setDescription("cat1");
        Item item1 = auction.offerItem(user1, cat, omsch);
        assertEquals(omsch, item1.getDescription());
        assertNotNull(item1.getId());
    }

    /**
     * Test of revokeItem method, of class SellerMgr.
     */
    @Test
    public void testRevokeItem() {
        String omsch = "omsch";
        String omsch2 = "omsch2";

        User seller = registration.registerUser("sel@nl");
        User buyer = registration.registerUser("buy@nl");
        Category cat = new Category();
        cat.setDescription("cat1");

        // revoke before bidding
        Item item1 = auction.offerItem(seller, cat, omsch);
        boolean res = auction.revokeItem(item1);
        assertTrue(res);
        int count = auction.findItemByDescription(omsch).size();
        assertEquals(0, count);

        // revoke after bid has been made
        Item item2 = auction.offerItem(seller, cat, omsch2);
        Money money = new Money();
        money.setCents(100);
        money.setCurrency("Euro");
        auction.newBid(item2, buyer, money);
        item2 = auction.findItemById(item2.getId()); // de state van item moet worden bijgewerkt omdat op het highest bid op de server inmiddels is bijgewerkt
        boolean res2 = auction.revokeItem(item2);
        assertFalse(res2);
        int count2 = auction.findItemByDescription(omsch2).size();
        assertEquals(1, count2);
    }

}
