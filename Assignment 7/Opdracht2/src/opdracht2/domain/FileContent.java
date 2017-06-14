package opdracht2.domain;

import java.io.Serializable;

/**
 *
 * @author Merik Westerveld en Robin Laugs - Klas S44
 */
public class FileContent implements Serializable {

    private final byte[] encryptedMessage;
    private final byte[] salt;
    private final byte[] iv;

    public FileContent(byte[] encryptedMessage, byte[] salt, byte[] iv) {
        this.encryptedMessage = encryptedMessage;
        this.salt = salt;
        this.iv = iv;
    }

    public byte[] getEncryptedMessage() {
        return encryptedMessage;
    }

    public byte[] getSalt() {
        return salt;
    }

    public byte[] getIv() {
        return iv;
    }
    
}
