/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import bank.domain.Account;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Merik
 */
public class Account_Test {

    private static final String PERSISTENCE_UNIT_NAME = "dbi338166";
    private EntityManagerFactory factory;
    private EntityManager em;
    private DatabaseCleaner dbCleaner;

    public Account_Test() {
    }

    @Before
    public void setUp() {
        try {
            factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            em = factory.createEntityManager();
            dbCleaner = new DatabaseCleaner(em);
            dbCleaner.clean();
        } catch (SQLException ex) {
            Logger.getLogger(Account_Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void test_vraag1() {
        Account account = new Account(111L);
        em.getTransaction().begin();
        em.persist(account);
        //TODO: verklaar en pas eventueel aan
        assertNull(account.getId());
        em.getTransaction().commit();
        System.out.println(
                "AccountId: " + account.getId()
        );
        //TODO: verklaar en pas eventueel aan
        assertTrue(account.getId() > 0L);
    }
}
