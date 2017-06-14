package application2.controller;

import application2.persistence.Persistence;
import application2.persistence.PersistenceController;
import constant.EncryptionConstants;
import domain.FileContent;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
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
    public void generateSignature(String name) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(EncryptionConstants.KEY_FACTORY_ALGORITHM);
            byte[] privateKeyBytes = persistence.readPrivateKey();
            PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));

            Signature signature = Signature.getInstance(EncryptionConstants.SIGNATURE_ALGORITHM);
            signature.initSign(privateKey);
            String content = persistence.readFile();
            signature.update(content.getBytes());
            byte[] bytes = signature.sign();

            FileContent fileContent = new FileContent(bytes, name, content);
            persistence.persistSignedFile(fileContent);
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException | InvalidKeySpecException e) {
            logger.log(Level.SEVERE, "An error occurred while creating the signature.", e);
        }
    }

}
