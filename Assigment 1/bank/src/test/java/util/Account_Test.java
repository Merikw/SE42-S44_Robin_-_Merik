package util;

import bank.dao.AccountDAOJPAImpl;
import bank.domain.Account;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Merik Westerveld en Robin Laugs - Klas S44
 */
public class Account_Test {

    private static final String PERSISTENCE_UNIT_NAME = "bankPU";

    private EntityManagerFactory factory;
    private EntityManager em;
    private DatabaseCleaner dbCleaner;
    private AccountDAOJPAImpl accountDataAccess;

    @Before
    public void setUp() {
        try {
            factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            em = factory.createEntityManager();
            dbCleaner = new DatabaseCleaner(em);
            dbCleaner.clean();
            em = factory.createEntityManager();
            accountDataAccess = new AccountDAOJPAImpl(em);
        } catch (SQLException ex) {
            Logger.getLogger(Account_Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 1. Wat is de waarde van asserties en printstatements? 
     * account.getId() == null 
     * account.getId() > 0L == 6L > 0L == true 
     * 
     * AccountId: 6
     *
     * 2. Welke SQL statements worden gegenereerd? 
     * em.persist --> INSERT INTO ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (?, ?, ?) bind => [111, 0, 0] 
     * em.getTransaction --> SELECT LAST_INSERT_ID()
     *
     * 3. Wat is het eindresultaat in de database? Er is een nieuwe tabel
     * aangemaakt genaamd 'ACCOUNT' met een nieuw record bestaande uit de velden
     * van de Account klasse. Het record heeft een automatisch gegenereerde ID
     * en de waarde van het kolom ACCOUNTNR is 111.
     */
    @Test
    public void test_vraag1() {
        Account account = new Account(111L);
        
        em.getTransaction().begin();
        em.persist(account);

        assertNull(account.getId());
        
        em.getTransaction().commit();
        
        System.out.println("AccountId: " + account.getId());
        assertTrue(account.getId() > 0L);
    }

    /**
     * 1. Wat is de waarde van asserties en printstatements? 
     * account.getId() == null
     * 0 == accountDataAccess.count() == 0 == 0
     * 
     * 2. Welke SQL statements worden gegenereerd? 
     * SELECT COUNT(ID) FROM ACCOUNT
     * Omdat er niet wordt gecommit wordt het persist statement niet uitgevoerd op de database.
     * TODO bevestiging vragen aan docent
     * 
     * 3. Wat is het eindresultaat in de database? Er is een nieuwe tabel
     * De 'ACCOUNT' tabel is leeg.
     */
    @Test
    public void test_vraag2() {
        Account account = new Account(111L);
        
        em.getTransaction().begin();
        em.persist(account);
        
        assertNull(account.getId());
        
        em.getTransaction().rollback();
        
        assertEquals(0, accountDataAccess.count());
    }

}
