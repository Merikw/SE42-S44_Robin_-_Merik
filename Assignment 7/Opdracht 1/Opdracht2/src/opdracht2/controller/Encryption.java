package opdracht2.controller;

/**
 *
 * @author Merik Westerveld en Robin Laugs - Klas S44
 */
public interface Encryption {

    void encrypt(String message, char[] password);

    String decrypt(char[] password);

}
