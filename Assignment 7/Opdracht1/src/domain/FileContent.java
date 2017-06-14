package domain;

/**
 *
 * @author Merik Westerveld en Robin Laugs - Klas S44
 */
public class FileContent {

    private final byte[] signature;
    private final String name;
    private final String content;

    public FileContent(byte[] signature, String name, String content) {
        this.signature = signature;
        this.name = name;
        this.content = content;
    }

    public byte[] getSignature() {
        return signature;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

}
