package application2.persistence;

import application2.Application2;
import constant.PersistenceConstants;
import domain.FileContent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Merik Westerveld en Robin Laugs - Klas S44
 */
public class PersistenceController implements Persistence {

    private static final Logger logger = Logger.getLogger(Application2.class.getName());

    @Override
    public byte[] readPrivateKey() {
        byte[] bytes = null;

        try (FileInputStream fis = new FileInputStream(PersistenceConstants.FILE_NAME_PRIVATE_KEY)) {
            bytes = new byte[(int) fis.getChannel().size()];
            fis.read(bytes);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "An error occurred while reading the private key.", e);
        }

        return bytes;
    }

    @Override
    public String readFile() {
        StringBuilder builder = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(PersistenceConstants.FILE_NAME_CONTENT))) {
            String line;
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "An error occurred while reading the content file.", e);
        }

        return builder.toString();
    }

    @Override
    public void persistSignedFile(FileContent fileContent) {
        try (FileOutputStream fos = new FileOutputStream(String.format(PersistenceConstants.FILE_NAME_CONTENT_SIGNED, fileContent.getName()));
                ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(fileContent.getSignature());
            oos.writeObject(fileContent.getContent());
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "An error occurred while persisting the signed content file.", e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "An error occurred while persisting the signed content file.", e);
        }
    }

}
