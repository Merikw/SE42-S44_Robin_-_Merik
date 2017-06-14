package application2;

import application2.controller.EncryptionController;
import java.util.Scanner;
import application2.controller.Encryption;

/**
 *
 * @author Merik Westerveld en Robin Laugs - Klas S44
 */
public class Application2 {

    public static void main(String[] args) {
        System.out.println("Write your name:");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        
        Encryption controller = new EncryptionController();
        controller.generateSignature(name);
    }

}
