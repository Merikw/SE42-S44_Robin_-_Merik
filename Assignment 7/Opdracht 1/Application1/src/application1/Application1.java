package application1;

import application1.controller.EncryptionController;
import application1.controller.Encryption;

/**
 *
 * @author Merik Westerveld en Robin Laugs - Klas S44
 */
public class Application1 {

    public static void main(String[] args) throws Exception {
        Encryption controller = new EncryptionController();
        controller.generateKeyPair();
    }

}
