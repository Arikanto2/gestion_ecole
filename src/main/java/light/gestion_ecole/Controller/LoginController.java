package light.gestion_ecole.Controller;




import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import light.gestion_ecole.DAO.UtilisateurDAO;
import light.gestion_ecole.Main;
import light.gestion_ecole.Model.Notification;
import light.gestion_ecole.Model.Utilisateur;

import java.io.IOException;
import java.sql.SQLException;


public class LoginController {

    @FXML PasswordField passwordField;
    @FXML TextField textField;
    @FXML TextField mail;
    @FXML private ImageView toggleBtn;
    @FXML BorderPane log;
    @FXML Button valider;
    public  static String nom;


    private boolean isPasswordVisible = false;

    @FXML
    public void initialize() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, e -> log.setStyle("-fx-background-color: rgba(230,247,255,1);")),
                new KeyFrame(Duration.seconds(0.5), e -> log.setStyle("-fx-background-color: rgba(230,247,255,0.9);")),
                new KeyFrame(Duration.seconds(1), e -> log.setStyle("-fx-background-color: rgba(230,247,255,0.8);")),
                new KeyFrame(Duration.seconds(1.5), e -> log.setStyle("-fx-background-color: rgba(230,247,255,0.7);")),
                new KeyFrame(Duration.seconds(2), e -> log.setStyle("-fx-background-color: rgba(230,247,255,0.6);")),
                new KeyFrame(Duration.seconds(2.5), e -> log.setStyle("-fx-background-color: rgba(230,247,255,0.5);")),
                new KeyFrame(Duration.seconds(3), e -> log.setStyle("-fx-background-color: rgba(230,247,255,0.4);")),
                new KeyFrame(Duration.seconds(3.5), e -> log.setStyle("-fx-background-color: rgba(230,247,255,0.3);")),
                new KeyFrame(Duration.seconds(4), e -> log.setStyle("-fx-background-color: rgba(230,247,255,0.2);")),
                new KeyFrame(Duration.seconds(4.5), e -> log.setStyle("-fx-background-color: rgba(230,247,255,0.1);")),
                new KeyFrame(Duration.seconds(5), e -> log.setStyle("-fx-background-color: rgba(230,247,255,0);"))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        timeline.play();
        textField.managedProperty().bind(textField.visibleProperty());
        passwordField.managedProperty().bind(passwordField.visibleProperty());
        mail.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER -> {
                    passwordField.requestFocus();
                    textField.requestFocus();
                }

            }
        });

        passwordField.setOnKeyPressed(event -> {
           switch (event.getCode()) {
               case ENTER -> {
                   connecterVous();
               }
           }
        });

        textField.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER -> {
                    connecterVous();
                }
            }
        });


        textField.textProperty().addListener((obs, oldText, newText) -> passwordField.setText(newText));
        passwordField.textProperty().addListener((obs, oldText, newText) -> textField.setText(newText));
        toggleBtn.setOnMouseClicked(this::togglePasswordVisibility);
        valider.setOnMouseClicked(e -> {
            connecterVous();
        });


    }

    private void togglePasswordVisibility(MouseEvent event) {
        if (isPasswordVisible) {
            textField.setVisible(false);
            passwordField.setVisible(true);
            isPasswordVisible = false;
            toggleBtn.setImage(new Image(getClass().getResourceAsStream("/light/gestion_ecole/Photo/icons8-closed-eye-96.png")));
        } else {
            textField.setVisible(true);
            passwordField.setVisible(false);
            isPasswordVisible = true;
            toggleBtn.setImage(new Image(getClass().getResourceAsStream("/light/gestion_ecole/Photo/icons8-eye-90.png")));
        }
    }

    public void connecterVous() {
        String mail1 = mail.getText();
        String password = passwordField.getText();
        Utilisateur util = new Utilisateur(mail1,password);
        try {
            String isUtilexist = UtilisateurDAO.Connecter(util);
            if (!isUtilexist.equals("")) {

                nom = isUtilexist;
                Stage stage = (Stage) valider.getScene().getWindow();
                stage.close();
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/light/gestion_ecole/View/Main-View.fxml"));
                Scene scene = new Scene(fxmlLoader.load());

                Stage mainStage = new Stage();
                mainStage.setTitle("LLRA");
                mainStage.setScene(scene);
                mainStage.setMinWidth(1000);
                mainStage.setMinHeight(700);
                mainStage.setMaximized(true);
                mainStage.show();
                Main.setStageIcon(mainStage);
                Notification.showSuccess("Connexion réussi!");


                Stage loginStage = (Stage) valider.getScene().getWindow();
                loginStage.close();




            } else {
                Notification.showWarning("Nom d'utilisateur ou mot de passe incorrect.");
            }
        } catch (SQLException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
