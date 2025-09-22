package light.gestion_ecole.Model;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;


public class Notification {
    public static Stage mainStage;

    private static void showNotification(String title, String message, String backgroundColor, String textColor) {
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + textColor + ";");

        Label msgLabel = new Label(message);
        msgLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: " + textColor + ";");

        VBox notifContent = new VBox(5, titleLabel, msgLabel);
        notifContent.setStyle(
                "-fx-background-color: " + backgroundColor + ";" +
                        "-fx-padding: 15;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-radius: 12;" +
                        "-fx-border-color: rgba(255,255,255,0.3);" +
                        "-fx-border-width: 1;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 10,0,0,2);"
        );


        Platform.runLater(() -> {
            Notifications.create()
                    .owner(mainStage)
                    .graphic(notifContent)
                    .hideAfter(Duration.seconds(4))
                    .position(Pos.BOTTOM_RIGHT)
                    .show();
        });
    }

    public static void showSuccess(String message) {
        showNotification("Succès !", message, "rgb(105,185,19)", "white");
    }

    public static void showWarning(String message) {
        showNotification("Attention !", message, "rgb(244,255,3)", "#000000");
    }

    public static void showError(String message) {
        showNotification("Erreur !", message, "rgb(227,30,41)", "#000000");
    }
}
