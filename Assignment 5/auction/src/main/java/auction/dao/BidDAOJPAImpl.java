package auction.dao;

import auction.domain.Bid;
import auction.domain.Item;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Merik Westerveld & Robin Laugs - Klas S44
 */
public class BidDAOJPAImpl implements BidDAO {

    private final EntityManager entityManager;

    public BidDAOJPAImpl(EntityManager em) {
        this.entityManager = em;
    }

    @Override
    public int count() {
        Query query = entityManager.createNamedQuery("Bid.count", Bid.class);
        return ((Long) query.getSingleResult()).intValue();
    }

    @Override
    public void create(Bid bid) {
        entityManager.getTransaction().begin();

        if (find(bid.getId()) == null) {
            entityManager.persist(bid);
            entityManager.getTransaction().commit();
        }
    }

    @Override
    public void edit(Bid bid) {
        if (find(bid.getId()) == null) {
            throw new IllegalArgumentException();
        }

        entityManager.merge(bid);
        entityManager.getTransaction().commit();
    }

    @Override
    public Bid find(Long id) {
        Query query = entityManager.createNamedQuery("Bid.findById", Item.class);
        query.setParameter("id", id);
        List<Bid> bids = query.getResultList();
        if (bids.size() != 1) {
            return null;
        }

        return bids.get(0);
    }

    @Override
    public void remove(Bid bid) {
        entityManager.remove(bid);
        entityManager.getTransaction().begin();
        entityManager.getTransaction().commit();
    }

}
