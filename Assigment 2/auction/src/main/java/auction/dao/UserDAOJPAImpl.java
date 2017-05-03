package auction.dao;

import auction.domain.User;
import java.util.List;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class UserDAOJPAImpl implements UserDAO {

    private final EntityManager entityManager;

    public UserDAOJPAImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public int count() {
        Query query = entityManager.createNamedQuery("User.count", User.class);

        return ((Long) query.getSingleResult()).intValue();
    }

    @Override
    public void create(User user) {
        entityManager.getTransaction().begin();

        if (findByEmail(user.getEmail()) == null) {
            entityManager.persist(user);
            entityManager.getTransaction().commit();
        }
    }

    @Override
    public void edit(User user) {
        if (findByEmail(user.getEmail()) == null) {
            throw new IllegalArgumentException();
        }

        entityManager.merge(user);
    }

    @Override
    public List<User> findAll() {
        Query query = entityManager.createNamedQuery("User.getAll", User.class);

        return query.getResultList();
    }

    @Override
    public User findByEmail(String email) {
        Query query = entityManager.createNamedQuery("User.findByEmail", User.class);
        query.setParameter("email", email);
        List<User> users = query.getResultList();
        if (users.size() != 1) {
            return null;
        }

        return users.get(0);
    }

    @Override
    public void remove(User user) {
        entityManager.remove(user);
    }

}
