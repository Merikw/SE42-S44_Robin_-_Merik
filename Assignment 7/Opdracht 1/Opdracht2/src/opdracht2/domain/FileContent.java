package opdracht2.domain;

import java.io.Serializable;

/**
 *
 * @author Merik Westerveld en Robin Laugs - Klas S44
 */
public class FileContent implements Serializable {

    private final byte[] encryptedMessage;
    private final byte[] salt;
    private final byte[] ivBytes;

    public FileContent(byte[] encryptedMessage, byte[] salt, byte[] ivBytes) {
        this.encryptedMessage = encryptedMessage;
        this.salt = salt;
        this.ivBytes = ivBytes;
    }

    public byte[] getEncryptedMessage() {
        return encryptedMessage;
    }

    public byte[] getSalt() {
        return salt;
    }

    public byte[] getIvBytes() {
        return ivBytes;
    }
    
}
