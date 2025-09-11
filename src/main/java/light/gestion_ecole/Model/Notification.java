package light.gestion_ecole.Model;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class Notification {
    public static void showSuccess(String message) {
        Label titleLabel = new Label("Succès!");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");
        Label msgLabel = new Label(message);
        msgLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

        VBox notifContent = new VBox(5, titleLabel, msgLabel);
        notifContent.setStyle(
                "-fx-background-color: rgb(105,185,19);"  +
                        "-fx-padding: 15;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-radius: 12;" +
                        "-fx-border-color: rgba(255,255,255,0.3);" +
                        "-fx-border-width: 1;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 10,0,0,2);"
        );

        Notifications notif = Notifications.create()
                .graphic(notifContent)
                .hideAfter(Duration.seconds(4))
                .position(Pos.BOTTOM_RIGHT);

        notif.show();

    }

    public static void showWarning(String message) {
        Label titleLabel = new Label("Attention!");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #000000;");
        Label msgLabel = new Label(message);
        msgLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #000000;");

        VBox notifContent = new VBox(5, titleLabel, msgLabel);
        notifContent.setStyle(
                "-fx-background-color: rgb(244,255,3);"  +
                        "-fx-padding: 15;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-radius: 12;" +
                        "-fx-border-color: rgba(255,255,255,0.3);" +
                        "-fx-border-width: 1;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 10,0,0,2);"
        );

        Notifications notif = Notifications.create()
                .graphic(notifContent)
                .hideAfter(Duration.seconds(4))
                .position(Pos.BOTTOM_RIGHT);


        notif.show();

    }

    public  static void showError(String message) {
        Label titleLabel = new Label("Erreur!");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #000000;");
        Label msgLabel = new Label(message);
        msgLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #000000;");

        VBox notifContent = new VBox(5, titleLabel, msgLabel);
        notifContent.setStyle(
                "-fx-background-color: rgb(227,30,41);"  +
                        "-fx-padding: 15;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-radius: 12;" +
                        "-fx-border-color: rgba(255,255,255,0.3);" +
                        "-fx-border-width: 1;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 10,0,0,2);"
        );

        Notifications notif = Notifications.create()
                .graphic(notifContent)
                .hideAfter(Duration.seconds(4))
                .position(Pos.BOTTOM_RIGHT);

        notif.show();
    }
}
