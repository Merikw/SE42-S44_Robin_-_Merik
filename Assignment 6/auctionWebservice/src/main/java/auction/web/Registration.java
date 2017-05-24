package auction.web;

import auction.dao.UserDAO;
import auction.domain.User;
import auction.service.RegistrationMgr;
import javax.jws.WebService;

/**
 * 
 * @author Merik Westerveld & Robin Laugs - Klas S44
 */
@WebService
public class Registration {
    
    private final RegistrationMgr registrationMgr;

    public Registration(UserDAO context) {
        registrationMgr = new RegistrationMgr(context);
    }
    
    public User registerUser(String email) {
        return registrationMgr.registerUser(email);
    }
    
    public User getUser(String email) {
        return  registrationMgr.getUser(email);
    }
    
}
