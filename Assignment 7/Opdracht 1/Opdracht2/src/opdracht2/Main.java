package opdracht2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Merik Westerveld en Robin Laugs - Klas S44
 */
public class Main extends Application {
    
    private static final String FXML_DOCUMENT = "FXMLDocument.fxml";
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(FXML_DOCUMENT));        
        Scene scene = new Scene(root);        
        stage.setScene(scene);
        stage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
