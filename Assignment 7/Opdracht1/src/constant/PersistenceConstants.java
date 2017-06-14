package constant;

/**
 *
 * @author Merik Westerveld en Robin Laugs - Klas S44
 */
public class PersistenceConstants {

    public static final String FILE_NAME_PUBLIC_KEY = "public.txt";
    public static final String FILE_NAME_PRIVATE_KEY = "private.txt";
    public static final String FILE_NAME_CONTENT = "content.txt";
    public static final String FILE_NAME_CONTENT_SIGNED = "content(signed by %s).txt";

    private PersistenceConstants() {
        // Constructor is marked private because this class should never be instantiated.
    }

}
