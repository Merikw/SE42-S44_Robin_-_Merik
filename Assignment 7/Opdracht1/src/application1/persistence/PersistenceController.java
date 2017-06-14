package application1.persistence;

import constant.PersistenceConstants;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Merik Westerveld en Robin Laugs - Klas S44
 */
public class PersistenceController implements Persistence {

    private static final Logger logger = Logger.getLogger(PersistenceController.class.getName());

    @Override
    public void persistPublicKey(byte[] publicKeyBytes) {
        try (OutputStream fos = new FileOutputStream(PersistenceConstants.FILE_NAME_PUBLIC_KEY)) {
            fos.write(publicKeyBytes);
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "An error occurred while writing persisting the public key.", e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "An error occurred while writing persisting the public key.", e);
        }
    }

    @Override
    public void persistPrivateKey(byte[] privateKeyBytes) {
        try (OutputStream fos = new FileOutputStream(PersistenceConstants.FILE_NAME_PRIVATE_KEY)) {
            fos.write(privateKeyBytes);
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "An error occurred while writing persisting the private key.", e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "An error occurred while writing persisting the private key.", e);
        }
    }

}
