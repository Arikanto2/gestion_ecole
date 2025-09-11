package light.gestion_ecole.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import light.gestion_ecole.Main;

import java.io.IOException;
import java.util.List;

public class MainController {
    @FXML StackPane contentArea;
    @FXML HBox menuAccueil;
    @FXML HBox menuStat;
    @FXML HBox menuEleve;
    @FXML HBox menuProf;
    @FXML HBox menuClasse;

    @FXML Label nomUtilisateur;
    @FXML Label premierLet;
    @FXML HBox poweroff;
    @FXML HBox menuParent;


    private List<HBox> tousLesMenus;
    @FXML
    public void initialize() {
        poweroff.setOnMouseClicked(event -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Déconnexion");
            alert.setHeaderText(null);
            alert.setContentText("Voulez-vous vraiment vous déconnecter ?");
            ImageView imageView = new ImageView(
                    getClass().getResource("/light/gestion_ecole/Photo/icons8-power-off-80.png").toExternalForm()
            );
            imageView.setFitHeight(48);
            imageView.setFitWidth(48);
            alert.setGraphic(imageView);

            ButtonType buttonOui = new ButtonType("Oui");
            ButtonType buttonNon = new ButtonType("Non");

            alert.getButtonTypes().setAll(buttonOui, buttonNon);

            alert.showAndWait().ifPresent(reponse -> {
                if (reponse == buttonOui) {
                    Stage satgeMain = (Stage) poweroff.getScene().getWindow();
                    satgeMain.close();
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/light/gestion_ecole/View/Login-View.fxml"));
                    Scene scene = null;
                    try {
                        scene = new Scene(fxmlLoader.load(), 320, 240);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Stage stage = new Stage();
                    stage.setTitle("Connexion");
                    stage.setScene(scene);
                    stage.setMinWidth(650);
                    stage.setMinHeight(550);
                    stage.setResizable(false);
                    stage.setMaximized(false);
                    Main.setStageIcon(stage);
                    stage.show();
                }
            });


        });
        nomUtilisateur.setText("@"+ LoginController.nom);
        premierLet.setText(String.valueOf(LoginController.nom.charAt(0)));

        tousLesMenus = List.of(menuAccueil,menuStat,menuEleve,menuProf,menuClasse,menuParent);
        tousLesMenus.forEach(menu -> {
            menu.setCursor(Cursor.HAND);
        });
        try {
            FXMLLoader vue = new FXMLLoader(getClass().getResource("/light/gestion_ecole/View/Accueil-View.fxml"));
            Node vueNode = vue.load();
            afficheView(vueNode);
            styliserMenu(menuAccueil);

            menuAccueil.setOnMouseClicked(e -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/light/gestion_ecole/View/Accueil-View.fxml"));
                    Node node = loader.load();
                    afficheView(node);
                    styliserMenu(menuAccueil);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        menuStat.setOnMouseClicked(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/light/gestion_ecole/View/Statistique-View.fxml"));
                Node node = loader.load();
                afficheView(node);
                styliserMenu(menuStat);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        menuEleve.setOnMouseClicked(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/light/gestion_ecole/View/Eleve-View.fxml"));
                Node node = loader.load();
                afficheView(node);
                styliserMenu(menuEleve);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        menuParent.setOnMouseClicked(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/light/gestion_ecole/View/Parent-View.fxml"));
                Node node = loader.load();
                afficheView(node);
                styliserMenu(menuParent);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        menuProf.setOnMouseClicked(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/light/gestion_ecole/View/Professeur-View.fxml"));
                Node node = loader.load();
                afficheView(node);
                styliserMenu(menuProf);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        menuClasse.setOnMouseClicked(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/light/gestion_ecole/View/Classes_View.fxml"));
                Node node = loader.load();
                afficheView(node);
                styliserMenu(menuClasse);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }



    public void afficheView( Node view){
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);

    }
    private void styliserMenu(HBox menuActif) {
        String style = "-fx-background-color: rgb(255,255,255);" +
                "-fx-border-width: 1px;" +
                "-fx-border-radius: 15px;" +
                "-fx-background-radius: 15px;" +
                "-fx-padding: 5;" +
                "-fx-pref-height:15px;" +
                "-fx-pref-width: 110px;";

        for (HBox hbox : tousLesMenus) {
            if (hbox == menuActif) {
                hbox.setStyle(style);
            } else {
                hbox.setStyle("");
            }
        }
    }

}