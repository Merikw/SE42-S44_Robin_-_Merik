package application1;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Merik Westerveld en Robin Laugs - Klas S44
 */
public class Application1 {
    
    private static final Logger logger = Logger.getLogger(Application1.class.getSimpleName());

    private static final String FILE_NAME_PUBLIC_KEY = "public.txt";
    private static final String FILE_NAME_PRIVATE_KEY = "private.txt";

    public static void main(String[] args) throws Exception {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");

        generator.initialize(1024, random);
        KeyPair pair = generator.generateKeyPair();
                
        persistPublicKey(pair.getPublic());
        persistPrivateKey(pair.getPrivate());
    }

    private static void persistPublicKey(PublicKey publicKey) {
        try (OutputStream fos = new FileOutputStream(FILE_NAME_PUBLIC_KEY)) {
            fos.write(publicKey.getEncoded());
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "An error occurred while writing persisting the public key.", e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "An error occurred while writing persisting the public key.", e);
        }
    }

    private static void persistPrivateKey(PrivateKey privateKey) {
        try (OutputStream fos = new FileOutputStream(FILE_NAME_PRIVATE_KEY)) {
            fos.write(privateKey.getEncoded());
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "An error occurred while writing persisting the private key.", e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "An error occurred while writing persisting the private key.", e);
        }
    }

}
