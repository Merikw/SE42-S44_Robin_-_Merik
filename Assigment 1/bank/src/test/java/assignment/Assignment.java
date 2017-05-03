package assignment;

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
import util.DatabaseCleaner;
import static org.junit.Assert.*;

/**
 * SE42 - Assignment persistence 1
 *
 * @authors Merik Westerveld en Robin Laugs - Klas S44
 */
public class Assignment {

    private static final String PERSISTENCE_UNIT_NAME = "bankPU";

    private static final Logger logger = Logger.getLogger(Assignment.class.getSimpleName());

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private AccountDAOJPAImpl accountDataAccessObject;
    private DatabaseCleaner cleaner;

    @Before
    public void setUp() {
        entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        entityManager = entityManagerFactory.createEntityManager();

        accountDataAccessObject = new AccountDAOJPAImpl(entityManager);

        try {
            cleaner = new DatabaseCleaner(entityManager);
            cleaner.clean();
        } catch (SQLException e) {
            logger.log(Level.WARNING, "An error occurred while cleaning the database", e);
        }
    }

    @Test
    public void vraag1() {
        Account account = new Account(111L);

        entityManager.getTransaction().begin();
        entityManager.persist(account);

        assertNull(account.getId()); // null

        entityManager.getTransaction().commit();
        // INSERT INTO ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (?, ?, ?) bind => [111, 0, 0] 
        // SELECT LAST_INSERT_ID() 

        assertNotNull(account.getId()); // 6 (not null)

        System.out.println("AccountId: " + account.getId()); // AccountId : 6

        assertTrue(account.getId() > 0L); // 6L > 0L == true

        // Eindresultaat database:
        // Er is een nieuwe tabel aangemaakt genaamd 'ACCOUNT' met een nieuw 
        // record bestaande uit de velden van de Account klasse. Het record 
        // heeft een automatisch gegenereerd 'ID' en de waarde van de kolom 
        // 'ACCOUNTNR' is 111.
    }

    @Test
    public void vraag2() {
        Account account = new Account(111L);

        entityManager.getTransaction().begin();
        entityManager.persist(account);

        assertNull(account.getId()); // null

        entityManager.getTransaction().rollback();

        assertEquals(0, accountDataAccessObject.count()); // 0 == 0 (equal)
        // SELECT COUNT(ID) FROM ACCOUNT

        // Eindresultaat database:
        // Omdat er niet wordt gecommit wordt de persist actie niet uitgevoerd
        // op de database. De tabel genaamd 'ACCOUNT' is daarom leeg.
    }

    @Test
    public void vraag3() {
        Long expected = -100L;

        Account account = new Account(111L);
        account.setId(expected);

        entityManager.getTransaction().begin();
        entityManager.persist(account);

        assertEquals(expected, account.getId()); // -100L == -100L (equal)

        entityManager.flush();
        // Met de flush operatie worden de SQL-instructies onmiddelijk uitgevoerd (zonder commit).
        // Het opgegeven id van -100L wordt overschreven met een automatisch gegenereerd id.
        // INSERT INTO ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (?, ?, ?) bind => [111, 0, 0]
        // SELECT LAST_INSERT_ID()

        assertNotEquals(expected, account.getId()); // -100L != 16L (not equal)

        entityManager.getTransaction().commit();

        // Eindresultaat database:
        // Er is een nieuw record toegevoegd in de 'ACCOUNT' tabel met een 
        // automatisch gegenereerd 'ID' (geen -100L).
    }

    @Test
    public void vraag4() {
        Long expectedBalance = 400L;

        Account account = new Account(114L);

        entityManager.getTransaction().begin();
        entityManager.persist(account);

        account.setBalance(expectedBalance);

        entityManager.getTransaction().commit();
        // INSERT INTO ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (?, ?, ?) bind => [114, 400, 0]
        // SELECT LAST_INSERT_ID()

        assertEquals(expectedBalance, account.getBalance()); // 400L == 400L (equal)
        // de Account entiteit wordt gepersisteerd en daarna nog veranderd. Pas 
        // na de commit wordt de entiteit ook daadwerkelijk weggeschreven in de
        // database.

        Long id = account.getId();

        EntityManager entityManager2 = entityManagerFactory.createEntityManager();
        entityManager2.getTransaction().begin();

        Account account2 = entityManager2.find(Account.class, id);
        // SELECT ID, ACCOUNTNR, BALANCE, THRESHOLD FROM ACCOUNT WHERE (ID = ?) bind => [29]

        assertEquals(expectedBalance, account2.getBalance()); // 400L == 400L (equal)
        // Het nieuwe Account object wordt opgehaald op basis van het id van het oude Account object.

        // Eindresultaat database:
        // Er is een nieuw record toegevoegd aan de 'ACCOUNT' tabel met een 
        // 'BALANCE' van 400 en het 'ACCOUNTNR' 114.
    }

    @Test
    public void vraag5() {
        Long expectedBalance = 400L;

        Account account = new Account(114L);

        entityManager.getTransaction().begin();
        entityManager.persist(account);

        account.setBalance(expectedBalance);

        entityManager.getTransaction().commit();
        // INSERT INTO ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (?, ?, ?) bind => [114, 400, 0]
        // SELECT LAST_INSERT_ID()

        assertEquals(expectedBalance, account.getBalance()); // 400L == 400L (equal)

        Long id = account.getId();

        EntityManager entityManager2 = entityManagerFactory.createEntityManager();
        entityManager2.getTransaction().begin();

        Account account2 = entityManager2.find(Account.class, id);
        // SELECT ID, ACCOUNTNR, BALANCE, THRESHOLD FROM ACCOUNT WHERE (ID = ?) bind => [366]
        account2.setBalance(10L);

        entityManager2.persist(account2);
        entityManager2.getTransaction().commit();
        // UPDATE ACCOUNT SET BALANCE = ? WHERE (ID = ?) bind => [10, 366]

        Long id2 = account2.getId();

        EntityManager entityManager3 = entityManagerFactory.createEntityManager();
        entityManager3.getTransaction().begin();

        Account account3 = entityManager3.find(Account.class, id2);
        // SELECT ID, ACCOUNTNR, BALANCE, THRESHOLD FROM ACCOUNT WHERE (ID = ?) bind => [366]

        assertEquals(account2.getBalance(), account3.getBalance()); // 10L == 10L (equal)
        assertNotEquals(account.getBalance(), account3.getBalance()); //400L != 10L (not equal)

        // Eindresultaat database:
        // De 'BALANCE' van het record in de 'ACCOUNT' tabel is gewijzigd van 400 naar 10.
    }

    @Test
    public void vraag6() {
        Account acc = new Account(1L);
        Account acc2;
        Account acc9;

        // Scenario 1
        Long balance1 = 100L;

        entityManager.getTransaction().begin();
        entityManager.persist(acc);

        acc.setBalance(balance1);

        entityManager.getTransaction().commit();
        // INSERT INTO ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (?, ?, ?) bind => [1, 100, 0]
        // SELECT LAST_INSERT_ID()

        assertEquals(balance1, acc.getBalance()); // 100L == 100L (equal)

        entityManager.getTransaction().begin();

        Account found = entityManager.find(Account.class, acc.getId());

        assertEquals(acc.getBalance(), found.getBalance()); // 100L == 100L (equal)

        // Scenario 2
        Long balance2a = 211L;
        Long balance2b = balance2a + balance2a;

        acc = new Account(2L);

        acc9 = entityManager.merge(acc);

        acc.setBalance(balance2a);
        acc9.setBalance(balance2b);

        entityManager.getTransaction().commit();
        // INSERT INTO ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (?, ?, ?) bind => [2, 422, 0]
        // SELECT LAST_INSERT_ID()

        assertEquals(balance2a, acc.getBalance()); // 211L == 211L (equal)
        assertEquals(balance2b, acc9.getBalance()); // 422L == 422L (equal)

        found = accountDataAccessObject.findByAccountNr(2L);
        // SELECT ID, ACCOUNTNR, BALANCE, THRESHOLD FROM ACCOUNT WHERE (ACCOUNTNR = ?) bind => [2]

        assertEquals(balance2b, found.getBalance()); // 422L == 422L (equal)

        // Scenario 3
        Long balance3a = 322L;
        Long balance3b = 333L;

        acc = new Account(3L);

        entityManager.getTransaction().begin();

        acc2 = entityManager.merge(acc);

        assertFalse(entityManager.contains(acc)); // Object niet in managed state (niet tracked).
        assertTrue(entityManager.contains(acc2)); // Object in managed state (tracked).
        assertNotEquals(acc, acc2);  // Objecten zijn niet 'equal' aan elkaar omdat merge een nieuwe managed instantie teruggeeft.

        acc2.setBalance(balance3a);
        acc.setBalance(balance3b);

        entityManager.getTransaction().commit();
        // INSERT INTO ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (?, ?, ?) bind => [3, 322, 0]
        // SELECT LAST_INSERT_ID()

        assertEquals(balance3a, acc2.getBalance()); // 322L == 322L (equal)
        assertEquals(balance3b, acc.getBalance()); // 333L == 333L (equal)

        found = accountDataAccessObject.findByAccountNr(3L);
        // SELECT ID, ACCOUNTNR, BALANCE, THRESHOLD FROM ACCOUNT WHERE (ACCOUNTNR = ?) bind => [3]

        assertEquals(balance3a, found.getBalance()); // 322L == 322L (equal)

        // Scenario 4
        Account account = new Account(114L);
        account.setBalance(450L);

        entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(account);
        entityManager.getTransaction().commit();
        // INSERT INTO ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (?, ?, ?)	bind => [114, 450, 0]
        // SELECT LAST_INSERT_ID()

        Account account2 = new Account(114L);
        Account tweedeAccountObject = account2;
        tweedeAccountObject.setBalance(650L);

        assertEquals((Long) 650L, account2.getBalance());  // 650L == 650L (equal)
        // Omdat tweedeAccountObject een kopie is van account2 wordt de balans voor beide objecten gezet.

        account2.setId(account.getId());

        entityManager.getTransaction().begin();

        account2 = entityManager.merge(account2);

        assertSame(account, account2);  // Gelijk aan elkaar omdat account en account2 hetzelfde id hebben, na de merge actie is account2 geupdate met de eigenschappen van account.
        assertTrue(entityManager.contains(account2));  // Object in managed state (tracked).
        assertFalse(entityManager.contains(tweedeAccountObject));  // Object niet in managed state (niet tracked).

        tweedeAccountObject.setBalance(850l);

        assertEquals((Long) 650L, account.getBalance());  // Omdat account en account2 hetzelfde id hebben zijn ze beide bijgewerkt bij het setten van de balans.
        assertEquals((Long) 650L, account2.getBalance());  // Na de merge is account een 'nieuw' object, tweedeAccountObject is niet meer verwant aan account2.

        entityManager.getTransaction().commit();
        // UPDATE ACCOUNT SET BALANCE = ? WHERE (ID = ?) bind => [650, 382]

        // Eindresultaat database:
        // Er zijn vier records toegevoegd in de 'ACCOUNT' tabel.
        // Record 1: 'ACCOUNTNR' = 1, 'BALANCE' = 100
        // Record 2: 'ACCOUNTNR' = 2, 'BALANCE' = 422
        // Record 3: 'ACCOUNTNR' = 3, 'BALANCE' = 322
        // Record 4: 'ACCOUNTNR' = 114, 'BALANCE' = 650
    }

    @Test
    public void vraag7() {
        Account account = new Account(77L);

        entityManager.getTransaction().begin();
        entityManager.persist(account);
        entityManager.getTransaction().commit();
        // INSERT INTO ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (?, ?, ?) bind => [77, 0, 0]
        // SELECT LAST_INSERT_ID()

        // Scenario 1        
        Account account2;
        Account account3;

        account2 = entityManager.find(Account.class, account.getId());
        account3 = entityManager.find(Account.class, account.getId());

        assertSame(account2, account3); // Gelijk aan elkaar.

        // Scenario 2        
        account2 = entityManager.find(Account.class, account.getId());

        entityManager.clear();

        account3 = entityManager.find(Account.class, account.getId());
        // SELECT ID, ACCOUNTNR, BALANCE, THRESHOLD FROM ACCOUNT WHERE (ID = ?) bind => [388]

        assertNotSame(account2, account3); // Niet gelijk aan elkaar omdat de persistence context leeg is gemaakt voor het ophalen van account3.
        // Door het leeg maken van de entity manager kan het gecache account 2 niet meer worden toegewezen aan account 2.

        // Eindresultaat database:
        // Er is een nieuw record toegevoegd aan de 'ACCOUNT' tabel met het 
        // 'ACCOUNTNR' 77.
    }

    @Test
    public void vraag8() {
        Account account = new Account(88L);

        entityManager.getTransaction().begin();
        entityManager.persist(account);
        entityManager.getTransaction().commit();
        // INSERT INTO ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (?, ?, ?) bind => [88, 0, 0]
        // SELECT LAST_INSERT_ID()

        Long id = account.getId();

        entityManager.remove(account);

        assertEquals(id, account.getId()); // 397l == 387L (equal)

        Account account2 = entityManager.find(Account.class, id);
        assertNull(account2); // Omdat het Account object uit de persistence context wordt verwijderd kan deze vervolgens niet worden opgevraagd met de find methode.

        // Eindresultaat database:
        // Er is een nieuw record toegevoegd aan de 'ACCOUNT' tabel met het 
        // 'ACCOUNTNR' 88.
    }

    @Test
    public void vraag9() {
        // Met GenerationType.SEQUENCE kan een sequentie worden gedefinieerd op basis waarvan het unieke id van de eniteit moet worden bepaald.
        // Met GenerationType.TABLE wordt een onderliggende tabel aangemaakt die vergelijkbaar is met GenerationType.SEQUENCE (voor providers die GenerationType.SEQUENCE niet ondersteunen).
    }

}
