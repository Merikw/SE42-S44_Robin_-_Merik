package opdracht2.controller;

import opdracht2.exception.InvalidPasswordException;

/**
 *
 * @author Merik Westerveld en Robin Laugs - Klas S44
 */
public interface Encryption {

    void encrypt(String message, char[] password);

    String decrypt(char[] password) throws InvalidPasswordException;

}
