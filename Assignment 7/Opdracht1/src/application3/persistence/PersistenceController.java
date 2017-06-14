package application3.persistence;

import constant.PersistenceConstants;
import domain.FileContent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Merik Westerveld en Robin Laugs - Klas S44
 */
public class PersistenceController implements Persistence {

    private final Logger logger = Logger.getLogger(PersistenceController.class.getName());

    @Override
    public byte[] readPublicKey() {
        byte[] bytes = null;

        try (InputStream fis = new FileInputStream(PersistenceConstants.FILE_NAME_PUBLIC_KEY)) {
            bytes = new byte[fis.available()];
            fis.read(bytes);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "An error occurred while reading the public key.", e);
        }

        return bytes;
    }

    @Override
    public FileContent readFile(String name) {
        FileContent fileContent = null;

        try (FileInputStream fis = new FileInputStream(String.format(PersistenceConstants.FILE_NAME_CONTENT_SIGNED, name));
                ObjectInputStream ois = new ObjectInputStream(fis)) {
            fileContent = new FileContent((byte[]) ois.readObject(), name, (String) ois.readObject());
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "An error occurred while reading the file.", e);
        } catch (IOException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "An error occurred while reading the file.", e);
        }

        return fileContent;
    }

}
