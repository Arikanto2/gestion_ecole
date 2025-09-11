package light.gestion_ecole.Controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import light.gestion_ecole.DAO.EleveDAO;
import light.gestion_ecole.DAO.StatDAO;
import light.gestion_ecole.Model.Eleve;

import java.sql.SQLException;
import java.util.List;

public class StatUnElveController {
    @FXML Label premierLettre;
    @FXML Label nummat;
    @FXML Label nompre;
    @FXML Label classeelve;
    @FXML Label anneescolaire;
    @FXML Label MG;
    @FXML Label nbretard;
    @FXML Label absence;
    @FXML StackPane MGEl;
    @FXML StackPane Assi;
     static Eleve eleve;
    @FXML void initialize() throws SQLException {
        afficheStatEleve(eleve);
    }
    void afficheStatEleve(Eleve eleve) throws SQLException {
        double mg = StatDAO.getMGElve(eleve);
        String nom = eleve.getNomeleve();
        String prenom = eleve.getPrenomeleve();
        premierLettre.setText(nom.charAt(0) + " " + prenom.charAt(0));
        nummat.setText(eleve.getNummat());
        nompre.setText(nom);

        classeelve.setText(eleve.getClasse());
        anneescolaire.setText(eleve.getAnneescolaire());
        MG.setText(String.valueOf(Math.round(mg * 100.0) / 100.0));
        nbretard.setText(String.valueOf(StatDAO.getRetards(eleve.getIdeleve())));
        absence.setText(String.valueOf(StatDAO.getScoreAbsence(eleve.getIdeleve())));
        affichMG(eleve);
        afficheAssui(eleve);
    }
    public void affichMG(Eleve eleve){
        double[] moyenne = StatDAO.getMGParTrimestreTriEl(eleve);
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Trimestre");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Moyenne Générale");
        LineChart<String ,Number> graph = new LineChart<>(xAxis,yAxis);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Moyenne Générale par période");
        if (moyenne[0] != 0) series.getData().add(new XYChart.Data<>("Interro 1", moyenne[0]));
        if (moyenne[1] != 0) series.getData().add(new XYChart.Data<>("Examen I", moyenne[1]));
        if (moyenne[2] != 0) series.getData().add(new XYChart.Data<>("Interro 2", moyenne[2]));
        if (moyenne[3] != 0) series.getData().add(new XYChart.Data<>("Examen II", moyenne[3]));
        if (moyenne[4] != 0) series.getData().add(new XYChart.Data<>("Interro 3", moyenne[4]));
        if (moyenne[5] != 0) series.getData().add(new XYChart.Data<>("Examen III", moyenne[5]));
        for (XYChart.Data<String, Number> data : series.getData()) {
            data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    Tooltip tooltip = new Tooltip(data.getXValue() + " : " + data.getYValue());
                    tooltip.setStyle(
                            "-fx-background-color: #333333; " +
                                    "-fx-text-fill: rgba(65, 3, 13, 0.91); " +
                                    "-fx-font-size: 14px; " +
                                    "-fx-padding: 10px; " +
                                    "-fx-background-radius: 5px;" +
                                    "-fx-background-color: white;"
                    );
                    Tooltip.install(newNode, tooltip);
                }
            });
        }

        graph.getData().addAll(series);
        MGEl.getChildren().add(graph);

    }
    public void afficheAssui(Eleve eleve){
        int[] comportement = StatDAO.getComportement(eleve);
        int[] participation = StatDAO.getParticipation(eleve);

        int nbExcellenteComp = comportement[0];
        int nbCorrect = comportement[1];
        int nbMauvais = comportement[2];

        int nbExcellentPa = participation[0];
        int nbMoyenne = participation[1];
        int nbJamais = participation[2];

        String participationText;
        if (nbExcellentPa >= nbMoyenne && nbExcellentPa >= nbJamais) {
            participationText = " * L’élève participe régulièrement aux cours.";
        } else if (nbMoyenne >= nbExcellentPa && nbMoyenne >= nbJamais) {
            participationText = " * L’élève participe de manière modérée.";
        } else {
            participationText = " * L’élève participe rarement en classe.";
        }

        String comportementText;
        if (nbExcellenteComp >= nbCorrect && nbExcellenteComp >= nbMauvais) {
            comportementText = " * L’élève a un comportement exemplaire.";
        } else if (nbCorrect >= nbExcellenteComp && nbCorrect >= nbMauvais) {
            comportementText = " * L’élève a un comportement correct.";
        } else {
            comportementText = " * L’élève a souvent un comportement perturbateur.";
        }

        Label TitreASS = new Label("Appréciation générale :");
        TitreASS.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: rgba(6,56,86,0.91); -fx-font-family: Goudy Old Style;");

        Label participationLabel = new Label(participationText);
        participationLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #063856; -fx-font-family: Segoe UI;");

        Label comportementLabel = new Label(comportementText);
        comportementLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #063856; -fx-font-family: Segoe UI;");


        VBox root = new VBox(8, participationLabel, comportementLabel);
        root.setMargin(participationLabel, new Insets(0,0,0,20) );
        root.setMargin(comportementLabel, new Insets(0,0,0,20) );
        root.setAlignment(Pos.TOP_LEFT);
        root.setAlignment(Pos.CENTER_LEFT);
        VBox root1 = new VBox(10, TitreASS, root);
        root1.setAlignment(Pos.TOP_CENTER);
        root1.setMargin(root, new Insets(20, 0, 0, 0));
        root1.setStyle("-fx-background-color: #edf4f5; -fx-background-radius: 8px;");
        root1.setPadding(new Insets(50,0,0,0));

        Assi.getChildren().add(root1);
        Assi.setMargin(root1, new Insets(30, 30, 30, 30));



    }
}
