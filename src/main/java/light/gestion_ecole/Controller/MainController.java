package light.gestion_ecole.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.List;

public class MainController {
    @FXML StackPane contentArea;
    @FXML HBox menuAccueil;
    @FXML HBox menuStat;
    @FXML HBox menuEleve;

    private List<HBox> tousLesMenus;
    @FXML
    public void initialize() {
        tousLesMenus = List.of(menuAccueil,menuStat,menuEleve);

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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/light/gestion_ecole/View/Statistique-View.fxml"));
                Node node = loader.load();
                afficheView(node);
                styliserMenu(menuEleve);
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