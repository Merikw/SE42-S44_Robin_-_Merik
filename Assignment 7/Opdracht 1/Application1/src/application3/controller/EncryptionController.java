package application3.controller;

import application3.persistence.Persistence;
import application3.persistence.PersistenceController;
import constant.EncryptionConstants;
import domain.FileContent;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Merik Westerveld en Robin Laugs - Klas S44
 */
public class EncryptionController implements Encryption {

    private static final Logger logger = Logger.getLogger(PersistenceController.class.getName());

    private final Persistence persistence;

    public EncryptionController() {
        persistence = new PersistenceController();
    }

    @Override
    public boolean fileIsVerified(String name) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(EncryptionConstants.KEY_FACTORY_ALGORITHM);
            byte[] publicKeyBytes = persistence.readPublicKey();
            PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));

            Signature signature = Signature.getInstance(EncryptionConstants.SIGNATURE_ALGORITHM);
            signature.initVerify(publicKey);
            FileContent fileContent = persistence.readFile(name);
            signature.update(fileContent.getContent().getBytes());
            boolean verified = signature.verify(fileContent.getSignature());

            return verified;
        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidKeySpecException | SignatureException e) {
            logger.log(Level.SEVERE, "An error occurred while verifying the public file.", e);
        }

        return false;
    }

    @Override
    public String getMessage(String name) {
        FileContent fileContent = persistence.readFile(name);
        return fileContent.getContent();
    }

}
