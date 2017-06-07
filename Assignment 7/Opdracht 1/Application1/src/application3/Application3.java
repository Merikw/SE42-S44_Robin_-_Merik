package application3;

import application2.Application2;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Merik Westerveld en Robin Laugs - Klas S44
 */
public class Application3 {

    private static final Logger logger = Logger.getLogger(Application3.class.getSimpleName());

    private static final String FILE_NAME_PUBLIC_KEY = "public.txt";

    public static void main(String[] args) {
        PublicKey publicKey = readPublicKey();

        System.out.println("Write your name:");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();

        boolean fileIsVerified = fileIsVerified(publicKey, name);
        System.out.println(fileIsVerified);
    }

    private static PublicKey readPublicKey() {
        byte[] bytes = null;

        try (InputStream fis = new FileInputStream(FILE_NAME_PUBLIC_KEY)) {
            bytes = new byte[fis.available()];
            fis.read(bytes);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "An error occurred while reading the public key.", e);
        }

        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            logger.log(Level.SEVERE, "An error occurred while creating the key factory.", e);
        }

        PublicKey key = null;
        try {
            key = keyFactory.generatePublic(new X509EncodedKeySpec(bytes));
        } catch (InvalidKeySpecException e) {
            logger.log(Level.SEVERE, "An error occurred while creating the public key.", e);
        }

        return key;
    }

    private static boolean fileIsVerified(PublicKey publicKey, String name) {
        byte[] signature = null;
        String content = "";

        try (FileInputStream fis = new FileInputStream(String.format("input(signedby%s).txt", name));
                ObjectInputStream ois = new ObjectInputStream(fis)) {
            int length = (Integer) ois.readObject();
            signature = (byte[])ois.readObject();
            content = (String) ois.readObject();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Application2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Application2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Application3.class.getName()).log(Level.SEVERE, null, ex);
        }

        Signature signer = null;
        try {
            signer = Signature.getInstance("SHA1withRSA");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Application3.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            signer.initVerify(publicKey);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(Application3.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            signer.update(content.getBytes());
        } catch (SignatureException ex) {
            Logger.getLogger(Application3.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            return signer.verify(signature);
        } catch (SignatureException ex) {
            Logger.getLogger(Application3.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

}
