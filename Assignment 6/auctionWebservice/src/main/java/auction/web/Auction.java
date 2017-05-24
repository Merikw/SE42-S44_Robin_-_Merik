package auction.web;

import auction.dao.ItemDAO;
import auction.domain.Bid;
import auction.domain.Category;
import auction.domain.Item;
import auction.domain.User;
import auction.service.AuctionMgr;
import auction.service.SellerMgr;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import nl.fontys.util.Money;

/**
 *
 * @author Merik Westerveld & Robin Laugs - Klas S44
 */
@WebService
public class Auction {

    private final AuctionMgr auctionMgr;
    private final SellerMgr sellerMgr;

    public Auction(ItemDAO context) {
        auctionMgr = new AuctionMgr(context);
        sellerMgr = new SellerMgr(context);
    }

    @WebMethod(operationName = "FindItemById")
    public Item getItem(Long id) {
        return auctionMgr.getItem(id);
    }

    public List<Item> findItemByDescription(String description) {
        return auctionMgr.findItemByDescription(description);
    }

    public Bid newBid(Item item, User buyer, Money amount) {
        return auctionMgr.newBid(item, buyer, amount);
    }

    public Item offerItem(User seller, Category cat, String description) {
        return sellerMgr.offerItem(seller, cat, description);
    }

    @WebResult(name = "IsRevoked")
    public boolean revokeItem(Item item) {
        return sellerMgr.revokeItem(item);
    }

}
