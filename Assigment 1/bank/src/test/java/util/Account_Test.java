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
    
    /**
     * 1. Wat is de waarde van asserties en printstatements?
     * expected == account.getId() == -100L == -100L == true. 
     * expected == account.getId() == -100L == 16L == true (door de NotEquals).
     * 
     * Doordat er pas later geflusht wordt, wordt er een autoincrement op de database gedaan waardoor het ID veranderd wordt. 
     * Hierdoor is deze later anders.
     * 
     * 2. Welke SQL statements worden gegenereerd? 
     * Na de .flush() wordt INSERT INTO ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (?, ?, ?) bind => [111, 0, 0] uitgevoerd.
     * Na de .getId() wordt SELECT LAST_INSERT_ID() uitgevoerd.
     * 
     * 3. Wat is het eindresultaat in de database? 
     * Er wordt een nieuw account in de database gezet met een ID van 16 en niet -100.
     */
    @Test
    public void test_vraag3(){
        Long expected = -100L;
        Account account = new Account(111L);
        account.setId(expected);
        em.getTransaction().begin();
        em.persist(account);

        assertEquals(expected, account.getId());
        em.flush();

        assertNotEquals(expected, account.getId());
        em.getTransaction().commit();
    }
    
    /**
     * 1. Wat is de waarde van asserties en printstatements?
     * ExpectedBalance == account.getBalance() == 400L == 400L == true.
     * De entity Account wordt gepersisteerd en daarna nog veranderd. Pas na de commit wordt het ook
     * daadwerkelijk in de database gezet. 
     * 
     * expectedBalance == found.getBalance() == 400L == 400L == true.
     * Het gevonden account heeft in de database een balance van 400 en is daarom true.
     * 
     * 2. Welke SQL statements worden gegenereerd? 
     * Na de commit() wordt INSERT INTO ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (?, ?, ?) bind => [114, 400, 0] uitgevoerd.
     * Bij find() wordt SELECT ID, ACCOUNTNR, BALANCE, THRESHOLD FROM ACCOUNT WHERE (ID = ?) bind => [29] uitgevoerd.
     * 
     * 3. Wat is het eindresultaat in de database? 
     * In de database zit een nieuw record van een klant met ID 29 en balans 400 en accountnr 114. 
     */
    @Test
    public void test_vraag4(){
        Long expectedBalance = 400L;
        Account account = new Account(114L);
        em.getTransaction().begin();
        em.persist(account);
        account.setBalance(expectedBalance);
        System.out.println("2");
        em.getTransaction().commit();
        System.out.println("3");
        assertEquals(expectedBalance, account.getBalance());

        Long cid = account.getId();
        account = null;
        EntityManager em2 = factory.createEntityManager();
        em2.getTransaction().begin();
        Account found = em2.find(Account.class,  cid);

        assertEquals(expectedBalance, found.getBalance());
    }

}
