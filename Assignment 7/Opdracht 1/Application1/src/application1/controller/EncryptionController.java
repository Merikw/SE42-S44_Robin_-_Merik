package application1.controller;

import application1.persistence.Persistence;
import application1.persistence.PersistenceController;
import constant.EncryptionConstants;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Merik Westerveld en Robin Laugs - Klas S44
 */
public class EncryptionController implements Encryption {

    private static final Logger logger = Logger.getLogger(EncryptionController.class.getName());

    private final Persistence persistence;

    public EncryptionController() {
        persistence = new PersistenceController();
    }

    @Override
    public void generateKeyPair() {
        try {
            SecureRandom random = SecureRandom.getInstance(EncryptionConstants.SECURE_RANDOM_ALGORITHM);
            KeyPairGenerator generator = KeyPairGenerator.getInstance(EncryptionConstants.KEY_PAIR_GENERATOR_ALGORITHM);
            generator.initialize(EncryptionConstants.KEY_SIZE, random);
            KeyPair pair = generator.generateKeyPair();

            persistence.persistPublicKey(pair.getPublic().getEncoded());
            persistence.persistPrivateKey(pair.getPrivate().getEncoded());
        } catch (NoSuchAlgorithmException e) {
            logger.log(Level.SEVERE, "An error occurred while generating the key pair.", e);
        }
    }

}
