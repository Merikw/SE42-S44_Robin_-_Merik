package application3;

import application3.controller.EncryptionController;
import java.util.Scanner;
import application3.controller.Encryption;

/**
 *
 * @author Merik Westerveld en Robin Laugs - Klas S44
 */
public class Application3 {

    public static void main(String[] args) {
        System.out.println("Write your name:");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();

        Encryption controller = new EncryptionController();
        boolean verified = controller.fileIsVerified(name);

        System.out.println("File verified: " + verified);
    }

}
