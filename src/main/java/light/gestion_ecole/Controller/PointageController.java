package light.gestion_ecole.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;


import java.io.IOException;
import java.util.List;

public class PointageController {
    public List<HBox> tousLesMenuPointage;
    @FXML HBox menuRetard;
    @FXML HBox menuAbs;
    @FXML StackPane contentArea;
    @FXML void  initialize(){
        tousLesMenuPointage = List.of(menuRetard,menuAbs);
        styliserMenuStat(menuRetard);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/light/gestion_ecole/View/Retards-View.fxml"));
        try {
            Node absence = loader.load();
            afficherMenuPointage(absence);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        menuAbs.setOnMouseClicked(event -> {
            FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/light/gestion_ecole/View/Absence-View.fxml"));
            try {
                Node absence = loader1.load();
                afficherMenuPointage(absence);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            styliserMenuStat(menuAbs);
        });
        menuRetard.setOnMouseClicked(event -> {
            FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/light/gestion_ecole/View/Retards-View.fxml"));
            try {
                Node absence = loader1.load();
                afficherMenuPointage(absence);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            styliserMenuStat(menuRetard);
        });

    }
    private void styliserMenuStat(HBox menuActif) {
        String style = "-fx-background-color: #e1f3fc;";

        for (HBox hbox : tousLesMenuPointage) {
            if (hbox == menuActif) {
                hbox.setStyle(style);
            } else {
                hbox.setStyle("");
            }
        }
    }
    public void afficherMenuPointage(Node root){
        contentArea.getChildren().clear();
        contentArea.getChildren().add(root);
    }
}

