package auction.service;

import auction.dao.ItemDAO;
import auction.domain.Category;
import auction.domain.Furniture;
import auction.domain.Item;
import auction.domain.Painting;
import auction.domain.User;

public class SellerMgr {

    private final ItemDAO itemDAO;

    public SellerMgr(ItemDAO itemDAO) {
        this.itemDAO = itemDAO;
    }

    /**
     * @param seller
     * @param cat
     * @param description
     * @return het item aangeboden door seller, behorende tot de categorie cat
     * en met de beschrijving description
     */
    
    public Item offerItem(User seller, Category cat, String description) {
        Item item = new Item(seller, cat, description);
        itemDAO.create(item);
        return item;
    }

    /**
     * @param item
     * @return true als er nog niet geboden is op het item. Het item word
     * verwijderd. false als er al geboden was op het item.
     */
    public boolean revokeItem(Item item) {
        if (item.getHighestBid() == null) {
            itemDAO.remove(item);
            return true;
        } else {
            return false;
        }
    }

    public Item offerFurniture(User user, Category cat, String description, String material) {
        Item item = new Furniture(material, user, cat, description);
        itemDAO.create(item);
        return item;
    }

    public Item offerPainting(User user, Category cat, String description, String titel, String painter) {
        Item item = new Painting(titel, painter, user, cat, description);
        itemDAO.create(item);
        return item;
    }
}
