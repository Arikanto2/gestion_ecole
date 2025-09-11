package light.gestion_ecole.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import light.gestion_ecole.DAO.StatDAO;

public class AccueilController {
    @FXML Label titreAnne;
    String annescolaire;
    @FXML void initialize(){
        annescolaire = StatDAO.getAnnescolaire().get(0);
        titreAnne.setText("ANNEES SCOLAIRE " + annescolaire);
    }

}
