package auction.service;

import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import auction.web.DatabaseUtil;
import auction.web.DatabaseUtilService;
import auction.web.Registration;
import auction.web.RegistrationService;
import auction.web.User;

/**
 *
 * @author Merik Westerveld & Robin Laugs - Klas S44
 */
public class RegistrationMgrTest {

    private Registration registration;
    private DatabaseUtil datebaseUtil;

    @Before
    public void setUp() {
        registration = new RegistrationService().getRegistrationPort();
        datebaseUtil = new DatabaseUtilService().getDatabaseUtilPort();
        datebaseUtil.cleanDatabase();
    }

    @Test
    public void registerUser() {
        User user1 = registration.registerUser("xxx1@yyy");
        assertTrue(user1.getEmail().equals("xxx1@yyy"));
        User user2 = registration.registerUser("xxx2@yyy2");
        assertTrue(user2.getEmail().equals("xxx2@yyy2"));
        User user2bis = registration.registerUser("xxx2@yyy2");
        assertEquals(user2bis.getEmail(), user2.getEmail());
        //geen @ in het adres
        assertNull(registration.registerUser("abc"));
    }

    @Test
    public void getUser() {
        User user1 = registration.registerUser("xxx5@yyy5");
        User userGet = registration.getUser("xxx5@yyy5");
        assertEquals(userGet.getEmail(), user1.getEmail());
        assertNull(registration.getUser("aaa4@bb5"));
        registration.registerUser("abc");
        assertNull(registration.getUser("abc"));
    }

    @Test
    public void getUsers() {
        List<User> users = registration.getUsers();
        assertEquals(0, users.size());

        User user1 = registration.registerUser("xxx8@yyy");
        users = registration.getUsers();
        assertEquals(1, users.size());
        assertEquals(users.get(0).getEmail(), user1.getEmail());

        User user2 = registration.registerUser("xxx9@yyy");
        users = registration.getUsers();
        assertEquals(2, users.size());

        registration.registerUser("abc");
        //geen nieuwe user toegevoegd, dus gedrag hetzelfde als hiervoor
        users = registration.getUsers();
        assertEquals(2, users.size());
    }
}
