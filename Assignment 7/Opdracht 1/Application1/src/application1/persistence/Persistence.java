package application1.persistence;

/**
 *
 * @author Merik Westerveld en Robin Laugs - Klas S44
 */
public interface Persistence {

    void persistPublicKey(byte[] publicKeyBytes);

    void persistPrivateKey(byte[] privateKeyBytes);

}
