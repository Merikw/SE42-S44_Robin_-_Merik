package auction.dao;

import auction.domain.Item;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Merik Westerveld & Robin Laugs - Klas S44
 */
public class ItemDAOJPAImpl implements ItemDAO {

    private final EntityManager entityManager;

    public ItemDAOJPAImpl(EntityManager em) {
        this.entityManager = em;
    }

    @Override
    public int count() {
        Query query = entityManager.createNamedQuery("Item.count", Item.class);
        return ((Long) query.getSingleResult()).intValue();
    }

    @Override
    public void create(Item item) {
        entityManager.getTransaction().begin();

        if (find(item.getId()) == null) {
            entityManager.persist(item);
            entityManager.getTransaction().commit();
        }
    }

    @Override
    public void edit(Item item) {
        if (find(item.getId()) == null) {
            throw new IllegalArgumentException();
        }

        entityManager.merge(item);
    }

    @Override
    public Item find(Long id) {
        Query query = entityManager.createNamedQuery("Item.findById", Item.class);
        query.setParameter("id", id);
        List<Item> items = query.getResultList();
        if (items.size() != 1) {
            return null;
        }

        return items.get(0);
    }

    @Override
    public List<Item> findAll() {
        Query query = entityManager.createNamedQuery("Item.getAll", Item.class);

        return query.getResultList();
    }

    @Override
    public List<Item> findByDescription(String description) {
        Query query = entityManager.createNamedQuery("Item.findByDescription", Item.class);
        query.setParameter("description", description);
        List<Item> items = query.getResultList();
        return items;
    }

    @Override
    public void remove(Item item) {
        entityManager.remove(item);
        entityManager.getTransaction().begin();
        entityManager.getTransaction().commit();
    }

}
