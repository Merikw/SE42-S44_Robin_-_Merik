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
     * 1. Wat is de waarde van asserties en printstatements? account.getId() ==
     * null account.getId() > 0L == 6L > 0L == true
     *
     * AccountId: 6
     *
     * 2. Welke SQL statements worden gegenereerd? em.persist --> INSERT INTO
     * ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (?, ?, ?) bind => [111, 0,
     * 0] em.getTransaction --> SELECT LAST_INSERT_ID()
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
     * 1. Wat is de waarde van asserties en printstatements? account.getId() ==
     * null 0 == accountDataAccess.count() == 0 == 0
     *
     * 2. Welke SQL statements worden gegenereerd? SELECT COUNT(ID) FROM ACCOUNT
     * Omdat er niet wordt gecommit wordt het persist statement niet uitgevoerd
     * op de database. TODO bevestiging vragen aan docent
     *
     * 3. Wat is het eindresultaat in de database? Er is een nieuwe tabel De
     * 'ACCOUNT' tabel is leeg.
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
     * 1. Wat is de waarde van asserties en printstatements? expected ==
     * account.getId() == -100L == -100L == true. expected == account.getId() ==
     * -100L == 16L == true (door de NotEquals).
     *
     * Doordat er pas later geflusht wordt, wordt er een autoincrement op de
     * database gedaan waardoor het ID veranderd wordt. Hierdoor is deze later
     * anders.
     *
     * 2. Welke SQL statements worden gegenereerd? Na de .flush() wordt INSERT
     * INTO ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (?, ?, ?) bind =>
     * [111, 0, 0] uitgevoerd. Na de .getId() wordt SELECT LAST_INSERT_ID()
     * uitgevoerd.
     *
     * 3. Wat is het eindresultaat in de database? Er wordt een nieuw account in
     * de database gezet met een ID van 16 en niet -100.
     */
    @Test
    public void test_vraag3() {
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
     * 1. Wat is de waarde van asserties en printstatements? ExpectedBalance ==
     * account.getBalance() == 400L == 400L == true. De entity Account wordt
     * gepersisteerd en daarna nog veranderd. Pas na de commit wordt het ook
     * daadwerkelijk in de database gezet.
     *
     * expectedBalance == found.getBalance() == 400L == 400L == true. Het
     * gevonden account heeft in de database een balance van 400 en is daarom
     * true.
     *
     * 2. Welke SQL statements worden gegenereerd? Na de commit() wordt INSERT
     * INTO ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (?, ?, ?) bind =>
     * [114, 400, 0] uitgevoerd. Bij find() wordt SELECT ID, ACCOUNTNR, BALANCE,
     * THRESHOLD FROM ACCOUNT WHERE (ID = ?) bind => [29] uitgevoerd.
     *
     * 3. Wat is het eindresultaat in de database? In de database zit een nieuw
     * record van een klant met ID 29 en balans 400 en accountnr 114.
     */
    @Test
    public void test_vraag4() {
        Long expectedBalance = 400L;
        Account account = new Account(114L);
        em.getTransaction().begin();
        em.persist(account);
        account.setBalance(expectedBalance);
        em.getTransaction().commit();
        assertEquals(expectedBalance, account.getBalance());

        Long cid = account.getId();
        account = null;
        EntityManager em2 = factory.createEntityManager();
        em2.getTransaction().begin();
        Account found = em2.find(Account.class, cid);

        assertEquals(expectedBalance, found.getBalance());
    }

    /**
     * 1. Wat is de waarde van asserties en printstatements? expectedBalace ==
     * account.getBalance == 400L == 400L found.getBalance() ==
     * found2.getBalance == 10L == 10L account.getBalance() != found2.getBalance
     * == 400L != 10L
     *
     * 2. Welke SQL statements worden gegenereerd? INSERT INTO ACCOUNT
     * (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (?, ?, ?) bind => [114, 400, 0]
     * SELECT LAST_INSERT_ID() SELECT ID, ACCOUNTNR, BALANCE, THRESHOLD FROM
     * ACCOUNT WHERE (ID = ?) bind => [52] UPDATE ACCOUNT SET BALANCE = ? WHERE
     * (ID = ?) bind => [10, 52] SELECT ID, ACCOUNTNR, BALANCE, THRESHOLD FROM
     * ACCOUNT WHERE (ID = ?) bind => [52]
     *
     * 3. Wat is het eindresultaat in de database? De balans van het record in
     * de 'ACCOUNT' tabel is gewijzigd van 400 naar 10.
     *
     */
    @Test
    public void test_vraag5() {
        Long expectedBalance = 400L;
        Account account = new Account(114L);

        em.getTransaction().begin();
        em.persist(account);

        account.setBalance(expectedBalance);

        em.getTransaction().commit();

        assertEquals(expectedBalance, account.getBalance());

        Long cid = account.getId();

        EntityManager em2 = factory.createEntityManager();
        em2.getTransaction().begin();

        Account found = em2.find(Account.class, cid);
        found.setBalance(10L);

        em2.persist(found);
        em2.getTransaction().commit();

        Long id = found.getId();

        EntityManager em3 = factory.createEntityManager();
        em3.getTransaction().begin();

        Account found2 = em3.find(Account.class, id);

        assertEquals(found.getBalance(), found2.getBalance());
        assertNotEquals(account.getBalance(), found2.getBalance());
    }

    @Test
    public void test_vraag6() {
        Account acc = new Account(1L);
        Account acc2;
        Account acc9;

        // scenario 1
        Long balance1 = 100L;
        em.getTransaction().begin();
        em.persist(acc);
        acc.setBalance(balance1);
        em.getTransaction().commit();

        assertEquals(balance1, acc.getBalance());

        em.getTransaction().begin();
        Account found = em.find(Account.class, acc.getId());

        assertEquals(acc.getBalance(), found.getBalance());

        // scenario 2
        Long balance2a = 211L;
        acc = new Account(2L);

        acc9 = em.merge(acc);
        acc.setBalance(balance2a);
        acc9.setBalance(balance2a + balance2a);
        em.getTransaction().commit();

        assertEquals(balance2a, acc.getBalance());
        Long balance3 = balance2a + balance2a;

        assertEquals(balance3, acc9.getBalance());

        found = accountDataAccess.findByAccountNr(2L);
        assertEquals(balance3, found.getBalance());

        // scenario 3
        Long balance3b = 322L;
        Long balance3c = 333L;
        acc = new Account(3L);
        em.getTransaction().begin();
        acc2 = em.merge(acc);
        assertFalse(em.contains(acc)); // object is niet in managed state (niet tracked) 
        assertTrue(em.contains(acc2)); // object is in managed state (tracked)
        assertNotEquals(acc, acc2);  // niet precies hetzelfde object
        acc2.setBalance(balance3b);
        acc.setBalance(balance3c);
        em.getTransaction().commit();

        assertEquals(balance3b, acc2.getBalance());
        assertEquals(balance3c, acc.getBalance());

        found = accountDataAccess.findByAccountNr(3L);
        assertEquals(balance3b, found.getBalance());

        // scenario 4
        Account account = new Account(114L);
        account.setBalance(450L);
        em = factory.createEntityManager();
        em.getTransaction().begin();
        em.persist(account);
        em.getTransaction().commit();

        Account account2 = new Account(114L);
        Account tweedeAccountObject = account2;
        tweedeAccountObject.setBalance(650L);
        assertEquals((Long) 650L, account2.getBalance());  // omdat tweedeAccountObject een kopie is van account2 wordt de balans voor beide objecten geset
        account2.setId(account.getId());
        em.getTransaction().begin();
        account2 = em.merge(account2);
        assertSame(account, account2);  // het zijn dezelfde objecten, maar de eigenschappen van beide objecten zijn wel gelijk
        assertTrue(em.contains(account2));  // object is in managed state (tracked)
        assertFalse(em.contains(tweedeAccountObject));  // object is niet in managed state (niet tracked)
        tweedeAccountObject.setBalance(850l);
        assertEquals((Long) 650L, account.getBalance());  // omdat account hetzelfde id heeft als account2 worden ze beide bewerkt
        assertEquals((Long) 650L, account2.getBalance());  // na de merge is account een 'nieuw' object, tweedeAccountObject is niet meer verwant aan account2
        em.getTransaction().commit();
    }

    /**
     *
     */
    @Test
    public void test_vraag7() {
        Account acc1 = new Account(77L);
        em.getTransaction().begin();
        em.persist(acc1);
        em.getTransaction().commit();
        //Database bevat nu een account.

        // scenario 1        
        Account accF1;
        Account accF2;
        accF1 = em.find(Account.class, acc1.getId());
        accF2 = em.find(Account.class, acc1.getId());
        assertSame(accF1, accF2);

        // scenario 2        
        accF1 = em.find(Account.class, acc1.getId());
        em.clear();
        accF2 = em.find(Account.class, acc1.getId());
        assertNotSame(accF1, accF2);
        // TODO vragen aan docent over invulling properties accF2
                
        // omdat de EntityManager cache wordt gecleared kan het account met accountNr 77 niet worden gevonden
    }

    /**
     * 
     */
    @Test
    public void test_vraag8() {
        Account acc1 = new Account(88L);
        em.getTransaction().begin();
        em.persist(acc1);
        em.getTransaction().commit();
        Long id = acc1.getId();
        // Database bevat nu een account.

        em.remove(acc1);
        assertEquals(id, acc1.getId());
        Account accFound = em.find(Account.class, id);
        assertNull(accFound);
        
        // omdat het Account object uit EntityManager cache wordt verwijderd kan deze vervolgens niet worden opgevraagd met de find() methode, hierdoor is accFound null
    }
    
    @Test
    public void vraag9() {
        // Met GenerationType.SEQUENCE kan een sequentie worden gedefinieerd
        // Met GenerationType.TABLE wordt een onderliggende tabel aangemaakt die vergelijkbaar is met GenerationType.SEQUENCE
        // (voor providers die GenerationType.SEQUENCE niet ondersteunen)
    }
    
}
