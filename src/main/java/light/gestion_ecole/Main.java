package light.gestion_ecole;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/light/gestion_ecole/View/Login-View.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Connexion");
        stage.setScene(scene);
        stage.setMinWidth(650);
        stage.setMinHeight(550);
        stage.setResizable(false);
        stage.setMaximized(false);
        setStageIcon(stage);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
    public static void setStageIcon(Stage stage) {
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("/light/gestion_ecole/Photo/500px-Lutherrose.svg.png")));
    }

}