package light.gestion_ecole.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import light.gestion_ecole.DAO.StatDAO;
import light.gestion_ecole.DAO.UtilisateurDAO;
import light.gestion_ecole.Model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AccueilController {
    @FXML private Label titreAnne;
    @FXML private Label menuPointage;
    @FXML private Label menuExamen;
    @FXML private BorderPane accueilview;
    @FXML private Button btnImport;
    @FXML private Button btnExport;


    String annescolaire;
    @FXML void initialize(){
        List<String> annees = StatDAO.getAnnescolaire();
        if (!annees.isEmpty()) {
            annescolaire = annees.get(0);
            titreAnne.setText("ANNEES SCOLAIRE " + annescolaire);
        } else {
            annescolaire = "";
            titreAnne.setText("ANNEES SCOLAIRE : Aucune donnée");
            System.out.println("Attention : aucune année scolaire trouvée dans la base de données !");
        }

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

        btnExport.setOnAction(e -> {
            try {
                List<String> emailutilisateur = UtilisateurDAO.getEmailUtilisateur();
                String email1 = LoginController.util.getEmail();
                for(String email : emailutilisateur){
                    if(!email.equals(email1)){
                        ExportService.envoyerParEmail(email);
                    }
                }
                QueryLogger.clear();
                Notification.showSuccess("Fichier de mise à jour envoyer aux autres utilisateurs.");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        btnImport.setOnAction(e -> {
            try {
                ImportWatcher.importerFile();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
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
