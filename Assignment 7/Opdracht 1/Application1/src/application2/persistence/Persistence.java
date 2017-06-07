package application2.persistence;

import domain.FileContent;

/**
 *
 * @author Merik Westerveld en Robin Laugs - Klas S44
 */
public interface Persistence {

    byte[] readPrivateKey();

    String readFile();

    void persistSignedFile(FileContent fileContent);

}
