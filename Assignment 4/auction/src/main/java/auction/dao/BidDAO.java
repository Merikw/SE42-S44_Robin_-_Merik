package auction.dao;

import auction.domain.Bid;

/**
 *
 * @author Merik Westerveld & Robin Laugs - Klas S44
 */
public interface BidDAO {

    int count();

    void create(Bid bid);

    void edit(Bid bid);

    Bid find(Long id);

    void remove(Bid bid);

}
