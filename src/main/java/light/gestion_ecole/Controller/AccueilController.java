package light.gestion_ecole.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import light.gestion_ecole.DAO.StatDAO;
import light.gestion_ecole.Model.ExportService;
import light.gestion_ecole.Model.ImportWatcher;
import light.gestion_ecole.Model.QueryLogger;

import java.io.IOException;

public class AccueilController {
    @FXML private Label titreAnne;
    @FXML private Label menuPointage;
    @FXML private Label menuExamen;
    @FXML private BorderPane accueilview;
    @FXML private Button btnImport;
    @FXML private Button btnExport;
    @FXML private Label ajoutEleve;

    String annescolaire;
    @FXML void initialize(){
        annescolaire = StatDAO.getAnnescolaire().get(0);

        titreAnne.setText("ANNEES SCOLAIRE " + annescolaire);
        menuPointage.setOnMouseClicked(e->{
            try {
                afficherMenu(menuPointage);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        menuExamen.setOnMouseClicked(e->{
            try {
                afficherMenu(menuExamen);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        ajoutEleve.setOnMouseClicked(e->{
           try {
               FXMLLoader loader = new FXMLLoader(getClass().getResource("/light/gestion_ecole/View/Eleve-View.fxml"));
               Node node = loader.load();
           } catch (IOException ex) {
               throw new RuntimeException(ex);
           }
        });
        btnExport.setOnAction(e -> {
            try {

                ExportService.exporterEtVider();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        btnImport.setOnAction(e -> {

            Thread t = new Thread(() -> {
                try {
                    ImportWatcher.startWatching();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            t.setDaemon(true);
            t.start();
        });
    }
    public void afficherMenu(Label menu) throws IOException {
        String view;
        if(menu == menuPointage){
            view = "Pointage-View";
        }else if(menu == menuExamen){
            view = "ExamenNational-View";
        }else {
            view = "";
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/light/gestion_ecole/View/"+ view +".fxml"));
        Node node = loader.load();
        accueilview.getChildren().clear();
        accueilview.setCenter(node);
    }

}
