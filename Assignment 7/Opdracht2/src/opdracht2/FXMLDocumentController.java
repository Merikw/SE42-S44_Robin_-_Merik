package opdracht2;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import opdracht2.controller.Encryption;
import opdracht2.controller.EncryptionController;
import opdracht2.exception.InvalidPasswordException;

/**
 *
 * @author Merik Westerveld en Robin Laugs - Klas S44
 */
public class FXMLDocumentController implements Initializable {

    private static final String BUTTON_ENCRYPT_TEXT = "Encrypt";
    private static final String BUTTON_DECRYPT_TEXT = "Decrypt";
    private static final String INVALID_PASSWORD_ALERT_HEADER = "Invalid password";
    private static final String INVALID_PASSWORD_ALERT_CONTENT = "The password you entered is incorrect.";

    @FXML
    private TextField textFieldMessage;

    @FXML
    private PasswordField passwordFieldPassword;

    @FXML
    private Button buttonEncryptDecrypt;

    private Encryption controller;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        controller = new EncryptionController();

        buttonEncryptDecrypt.setOnAction(e -> handleButtonClick());
        buttonEncryptDecrypt.disableProperty().bind(
                passwordFieldPassword.textProperty().isEmpty());
        buttonEncryptDecrypt.textProperty().bind(
                Bindings.when(textFieldMessage.textProperty().isEmpty())
                        .then(BUTTON_DECRYPT_TEXT)
                        .otherwise(BUTTON_ENCRYPT_TEXT));
    }

    private void handleButtonClick() {
        String message = textFieldMessage.getText();
        char[] password = passwordFieldPassword.getText().toCharArray();

        boolean userEnteredMessage = !message.trim().isEmpty();

        if (userEnteredMessage) {
            controller.encrypt(message, password);
            textFieldMessage.clear();
        } else {
            try {
                textFieldMessage.setText(controller.decrypt(password));
            } catch (InvalidPasswordException e) {
                showInvalidPasswordAlert();
            }
        }

        passwordFieldPassword.clear();
        password = null; // Explicitly clear the password variable.
    }

    private void showInvalidPasswordAlert() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setHeaderText(INVALID_PASSWORD_ALERT_HEADER);
        alert.setContentText(INVALID_PASSWORD_ALERT_CONTENT);
        alert.show();
    }

}
