package application3.controller;

/**
 *
 * @author Merik Westerveld en Robin Laugs - Klas S44
 */
public interface Encryption {

    boolean fileIsVerified(String name);
    
    String getMessage(String name);

}
