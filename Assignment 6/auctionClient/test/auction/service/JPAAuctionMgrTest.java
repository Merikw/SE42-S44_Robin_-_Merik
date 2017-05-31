package auction.service;

import auction.web.Auction;
import auction.web.AuctionService;
import auction.web.Bid;
import auction.web.Category;
import auction.web.DatabaseUtil;
import auction.web.DatabaseUtilService;
import auction.web.Item;
import auction.web.Money;
import auction.web.Registration;
import auction.web.RegistrationService;
import auction.web.User;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Merik Westerveld & Robin Laugs - Klas S44
 */
public class JPAAuctionMgrTest {

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

    @Test
    public void getItem() {
        String email = "xx2@nl";
        String omsch = "omsch";

        User seller1 = registration.registerUser(email);
        Category cat = new Category();
        cat.setDescription("cat2");
        Item item1 = auction.offerItem(seller1, cat, omsch);
        Item item2 = auction.findItemById(item1.getId());
        assertEquals(omsch, item2.getDescription());
        assertEquals(email, item2.getSeller().getEmail());
    }

    @Test
    public void findItemByDescription() {
        String email3 = "xx3@nl";
        String omsch = "omsch";
        String email4 = "xx4@nl";
        String omsch2 = "omsch2";

        User seller3 = registration.registerUser(email3);
        User seller4 = registration.registerUser(email4);
        Category cat = new Category();
        cat.setDescription("cat3");
        Item item1 = auction.offerItem(seller3, cat, omsch);
        Item item2 = auction.offerItem(seller4, cat, omsch);

        List<Item> res = auction.findItemByDescription(omsch2); // Cast naar ArrayList weggehaald omdat een een JPQL-query een lijst teruggeeft van het List-implementatietype Vector.
        assertEquals(0, res.size());

        res = auction.findItemByDescription(omsch);
        assertEquals(2, res.size());
    }

    @Test
    public void newBid() {
        String email = "ss2@nl";
        String emailb = "bb@nl";
        String emailb2 = "bb2@nl";
        String omsch = "omsch_bb";

        User seller = registration.registerUser(email);
        User buyer = registration.registerUser(emailb);
        User buyer2 = registration.registerUser(emailb2);
        // eerste bod
        Category cat = new Category();
        cat.setDescription("cat9");
        Item item1 = auction.offerItem(seller, cat, omsch);
        Money money = new Money();
        money.setCents(10);
        money.setCurrency("eur");
        Bid new1 = auction.newBid(item1, buyer, money);
        assertEquals(emailb, new1.getBuyer().getEmail());

        // lager bod
        money = new Money();
        money.setCents(9);
        money.setCurrency("eur");
        Bid new2 = auction.newBid(item1, buyer2, money);
        assertNull(new2);

        // hoger bod
        money = new Money();
        money.setCents(11);
        money.setCurrency("eur");
        Bid new3 = auction.newBid(item1, buyer2, money);
        assertEquals(emailb2, new3.getBuyer().getEmail());
    }

}
