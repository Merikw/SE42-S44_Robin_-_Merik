package opdracht2.persistence;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import opdracht2.domain.FileContent;

/**
 *
 * @author Merik Westerveld en Robin Laugs - Klas S44
 */
public class PersistenceController implements Persistence {

    private static final Logger logger = Logger.getLogger(PersistenceController.class.getName());

    private static final String FILE_NAME = "message";

    @Override
    public void persistData(FileContent fileContent) {
        try (FileOutputStream fis = new FileOutputStream(String.format(FILE_NAME));
                ObjectOutputStream ois = new ObjectOutputStream(fis)) {
            ois.writeObject(fileContent);
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "An error occurred while reading the file.", e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "An error occurred while reading the file.", e);
        }
    }

    @Override
    public FileContent readData() {
        FileContent fileContent = null;

        try (FileInputStream fis = new FileInputStream(String.format(FILE_NAME));
                ObjectInputStream ois = new ObjectInputStream(fis)) {
            fileContent = (FileContent) ois.readObject();
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "An error occurred while reading the file.", e);
        } catch (IOException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "An error occurred while reading the file.", e);
        }

        return fileContent;
    }

}
