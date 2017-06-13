package opdracht2.controller;

import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import opdracht2.domain.FileContent;
import opdracht2.persistence.Persistence;
import opdracht2.persistence.PersistenceController;

/**
 *
 * @author Merik Westerveld en Robin Laugs - Klas S44
 */
public class EncryptionController implements Encryption {

    private static final Logger logger = Logger.getLogger(EncryptionController.class.getName());

    private static final String SECRET_KEY_ALGORITHM = "AES";
    private static final String CIPHER_ALGORITM = "AES/CBC/PKCS5Padding";
    private static final String KEY_FACTORY_ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final String SECURE_RANDOM_ALGORITHM = "SHA1PRNG";
    private static final String CHAR_SET = "UTF-8";
    private static final int KEY_SPEC_SIZE = 128;
    private static final int ITERATION_COUNT = 1000;
    private static final int SALT_SIZE = 16;

    private final Persistence persistence;

    public EncryptionController() {
        persistence = new PersistenceController();
    }

    @Override
    public void encrypt(String message, char[] password) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_FACTORY_ALGORITHM);
            byte[] salt = generateSalt();
            KeySpec spec = new PBEKeySpec(password, salt, ITERATION_COUNT, KEY_SPEC_SIZE);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), SECRET_KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITM);
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            AlgorithmParameters params = cipher.getParameters();
            byte[] ivBytes = params.getParameterSpec(IvParameterSpec.class).getIV();
            byte[] encryptedMessage = cipher.doFinal(message.getBytes(CHAR_SET));
            persistence.persistData(new FileContent(encryptedMessage, salt, ivBytes));
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException
                | InvalidKeySpecException | InvalidParameterSpecException | NoSuchPaddingException | UnsupportedEncodingException e) {
            logger.log(Level.SEVERE, "An error occurred while encrypting the message.", e);
        }
    }

    @Override
    public String decrypt(char[] password) {
        String decryptedMessage = "";

        try {
            FileContent content = persistence.readData();
            SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_FACTORY_ALGORITHM);
            KeySpec spec = new PBEKeySpec(password, content.getSalt(), ITERATION_COUNT, KEY_SPEC_SIZE);
            SecretKey tmp = factory.generateSecret(spec);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITM);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), SECRET_KEY_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(content.getIvBytes()));
            byte[] decrypted = cipher.doFinal(content.getEncryptedMessage());
            decryptedMessage = new String(decrypted, CHAR_SET);
        } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | IllegalBlockSizeException
                | BadPaddingException | InvalidAlgorithmParameterException | NoSuchPaddingException | UnsupportedEncodingException e) {
            logger.log(Level.SEVERE, "An error occurred while decrypting the message.", e);
        }

        return decryptedMessage;
    }

    private byte[] generateSalt() {
        byte[] salt = new byte[SALT_SIZE];

        try {
            SecureRandom secureRandom = SecureRandom.getInstance(SECURE_RANDOM_ALGORITHM);
            secureRandom.nextBytes(salt);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("An error occurred while generating a salt.", e);
        }

        return salt;
    }

}
