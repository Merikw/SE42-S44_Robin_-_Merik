package auctionclient;

import auction.web.User;

/**
 * 
 * @author Merik Westerveld & Robin Laugs - Klas S44
 */
public class AuctionClient {

    public static void main(String[] args) {
        registerUser("maatwerk@outlook.com");
    }

    private static User registerUser(java.lang.String arg0) {
        auction.web.RegistrationService service = new auction.web.RegistrationService();
        auction.web.Registration port = service.getRegistrationPort();
        return port.registerUser(arg0);
    }
    
}
