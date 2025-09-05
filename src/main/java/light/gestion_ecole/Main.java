package light.gestion_ecole;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/light/gestion_ecole/View/Main-View.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Connexion");
        stage.setScene(scene);
        /*stage.setMinWidth(650);
        stage.setMinHeight(550);
        stage.setResizable(false);
        stage.setMaximized(false);*/
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}