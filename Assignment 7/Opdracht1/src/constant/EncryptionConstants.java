package constant;

/**
 *
 * @author Merik Westerveld en Robin Laugs - Klas S44
 */
public class EncryptionConstants {

    public static final String SECURE_RANDOM_ALGORITHM = "SHA1PRNG";
    public static final String KEY_PAIR_GENERATOR_ALGORITHM = "RSA";
    public static final String KEY_FACTORY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "SHA1withRSA";
    public static final int KEY_SIZE = 1024;

    private EncryptionConstants() {
        // Constructor is marked private because this class should never be instantiated.
    }

}
