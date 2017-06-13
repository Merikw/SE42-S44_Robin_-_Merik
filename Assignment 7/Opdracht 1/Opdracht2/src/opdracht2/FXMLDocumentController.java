package opdracht2;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import opdracht2.controller.Encryption;
import opdracht2.controller.EncryptionController;

/**
 *
 * @author Merik Westerveld en Robin Laugs - Klas S44
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField textField;

    @FXML
    private Button button;

    private Encryption controller;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        controller = new EncryptionController();

        button.setOnAction(e -> handleButtonClick());
    }

    private void handleButtonClick() {
        char[] password = passwordField.getText().toCharArray();
        String message = textField.getText();

        if (!message.trim().isEmpty()) {
            controller.encrypt(message, password);
            passwordField.clear();
            textField.clear();
        } else {
            textField.setText(controller.decrypt(password));
            passwordField.clear();
        }

        password = null;
    }

}
