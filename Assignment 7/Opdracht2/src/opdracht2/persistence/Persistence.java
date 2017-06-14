package opdracht2.persistence;

import opdracht2.domain.FileContent;

/**
 *
 * @author Merik Westerveld en Robin Laugs - Klas S44
 */
public interface Persistence {
    
    void persistData(FileContent fileContent);
    
    FileContent readData();
    
}
