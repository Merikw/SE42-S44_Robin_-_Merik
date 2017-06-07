package application2;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Merik Westerveld en Robin Laugs - Klas S44
 */
public class Application2 {

    private static final Logger logger = Logger.getLogger(Application2.class.getSimpleName());

    private static final String FILE_NAME_PRIVATE_KEY = "private.txt";
    private static final String FILE_NAME_INPUT = "input.txt";

    public static void main(String[] args) {
        PrivateKey key = readPrivateKey();
        String content = readFile();

        System.out.println("Write your name:");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();

        byte[] signature = generateSignature(key, name);
        
        persistSignature(name, content, signature);
    }

    private static PrivateKey readPrivateKey() {
        byte[] bytes = null;

        try (FileInputStream fis = new FileInputStream(FILE_NAME_PRIVATE_KEY)) {
            bytes = new byte[(int)fis.getChannel().size()];
            fis.read(bytes);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "An error occurred while reading the private key.", e);
        }

        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            logger.log(Level.SEVERE, "An error occurred while creating the key factory.", e);
        }

        PrivateKey key = null;
        try {
            key = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(bytes));
        } catch (InvalidKeySpecException e) {
            logger.log(Level.SEVERE, "An error occurred while creating the private key.", e);
        }

        return key;
    }

    private static String readFile() {
        String content = "";

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME_INPUT))) {
            String line;
            while ((line = br.readLine()) != null) {
                content += line;
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "An error occurred while reading the input.", e);
        }

        return content;
    }

    private static byte[] generateSignature(PrivateKey key, String name) {
        byte[] bytes = null;

        try {
            Signature signature = Signature.getInstance("SHA1withRSA");
            signature.initSign(key);
            signature.update(name.getBytes());
            bytes = signature.sign();
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            logger.log(Level.SEVERE, "An error occurred while creating the signature.", e);
        }

        return bytes;
    }

    private static void persistSignature(String name, String content, byte[] signature) {
        try (FileOutputStream fos = new FileOutputStream(String.format("input(signedby%s).txt", name));
                ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(signature.length);
            oos.writeObject(signature);
            oos.writeObject(content);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Application2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Application2.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}
