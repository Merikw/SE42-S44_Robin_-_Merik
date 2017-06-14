package application3.persistence;

import domain.FileContent;

/**
 *
 * @author Merik Westerveld en Robin Laugs - Klas S44
 */
public interface Persistence {

    byte[] readPublicKey();

    FileContent readFile(String name);

}
