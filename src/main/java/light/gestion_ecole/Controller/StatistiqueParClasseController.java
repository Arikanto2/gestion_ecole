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
import light.gestion_ecole.DAO.StatDAO;
import light.gestion_ecole.Model.Classe;
import java.sql.SQLException;

public class StatistiqueParClasseController {
    @FXML Label designClasse;
    @FXML Label nbEleve;
    @FXML Label nbrenvoyé;
    @FXML Label nbhandicapé;
    @FXML Label MGclasse;
    @FXML Label nbretard;
    @FXML Label absence;
    @FXML Label Mauvais;
    @FXML StackPane MGClasse;
    @FXML StackPane Assi;
    static Classe classe;
    static String anneescolaire;
    @FXML  public void initialize() throws SQLException {
        afficheStatClasse(classe);
    }
    public  void afficheStatClasse(Classe classe) throws SQLException {
        int[] nb = StatDAO.getNBrenvoyéHandic(classe.getIdClasse(),anneescolaire);
        int nbmv = StatDAO.getNBmauvais(classe.getIdClasse(),anneescolaire);
        designClasse.setText(classe.getDesignation());
        designClasse.setStyle("-fx-text-fill: #730505;");
        nbEleve.setText(classe.getNbrEleves() + " élèves");
        if (nb[0] != 0 ) {
            nbrenvoyé.setText(String.valueOf(nb[0]));
        }
        if (nb[1] != 0 ) {
            nbhandicapé.setText(String.valueOf(nb[1]));
        }
        MGclasse.setText(StatDAO.getMGclasse(classe.getIdClasse(), anneescolaire) + "/20");
        nbretard.setText(String.valueOf(StatDAO.getRetardsParClasse(classe.getIdClasse(),anneescolaire)));
        absence.setText(String.valueOf(StatDAO.getScoreAbsenceParClasse(classe.getIdClasse(),anneescolaire)));

        if(nbmv != 0){
            Mauvais.setText(String.valueOf(nbmv));
        }
        affichMG(classe.getIdClasse());
        afficheAssui(classe);
    }
    public void affichMG(int idclasse){
        double[] moyenne = StatDAO.getMGParTrimestreTri(idclasse, anneescolaire);
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
        MGClasse.getChildren().add(graph);
    }
    public void afficheAssui(Classe classe){
        double nbElve = classe.getNbrEleves();
        int nbAbsence = StatDAO.getScoreAbsenceParClasse(classe.getIdClasse(),anneescolaire);
        int nbExcellenteComp = 0;
        int nbExcellentPa = 0;
        int nbCorrect = 0;
        int nbMoyenne = 0;
        int nbJamais = 0;
        int nbMauvais = 0;
        int nbretard = StatDAO.getRetardsParClasse(classe.getIdClasse(),anneescolaire);
        int[] comportement = StatDAO.getComportementGlobalClasse(classe.getIdClasse(),anneescolaire);
        int[] participation =  StatDAO.getParticipationGlobalClasse(classe.getIdClasse(),anneescolaire);
        nbExcellenteComp = comportement[0];
        nbCorrect = comportement[1];
        nbMauvais = comportement[2];
        nbExcellentPa = participation[0];
        nbMoyenne =  participation[1];
        nbJamais = participation[2];

        Label TitreASS = new Label("En moyenne, un élève :");
        TitreASS.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: rgba(6,56,86,0.91); -fx-font-family: Goudy Old Style;");

        Label retard = new Label("• a " + String.format("%.2f", nbretard/nbElve) + " retards en moyenne ("+ nbretard+"/"+(int)nbElve + ")");
        retard.setStyle("-fx-font-size: 12px; -fx-text-fill: rgba(6,56,86,0.91);");

        Label abss = new Label("• a " + String.format("%.2f", nbAbsence/nbElve) + " absences en moyenne ("+ nbAbsence+"/"+(int)nbElve + ")");
        abss.setStyle("-fx-font-size: 12px; -fx-text-fill: rgba(6,56,86,0.91);");

        Label ExcellentPar = new Label("• participe activement environ " + String.format("%.2f", nbExcellentPa/nbElve) + " fois");
        ExcellentPar.setStyle("-fx-font-size: 12px; -fx-text-fill: rgba(6,56,86,0.91);");

        Label MoyennePar = new Label("• participe moyennement environ " + String.format("%.2f", nbMoyenne/nbElve) + " fois");
        MoyennePar.setStyle("-fx-font-size: 12px; -fx-text-fill: rgba(6,56,86,0.91);");

        Label JamaisPar = new Label("• ne participe jamais dans environ " + String.format("%.2f", nbJamais/nbElve) + " cas");
        JamaisPar.setStyle("-fx-font-size: 12px; -fx-text-fill: rgba(6,56,86,0.91);");

        Label ExcellenComp = new Label("• a un comportement excellent dans " + String.format("%.2f", nbExcellenteComp/nbElve) + " cas");
        ExcellenComp.setStyle("-fx-font-size: 12px; -fx-text-fill: rgba(6,56,86,0.91);");

        Label Correcte = new Label("• a un comportement correct dans " + String.format("%.2f", nbCorrect/nbElve) + " cas");
        Correcte.setStyle("-fx-font-size: 12px; -fx-text-fill: rgba(6,56,86,0.91);");

        Label Mauvais =  new Label("• a un mauvais comportement dans " + String.format("%.2f", nbMauvais/nbElve) + " cas");
        Mauvais.setStyle("-fx-font-size: 12px; -fx-text-fill: rgba(6,56,86,0.91);");

        VBox root = new VBox(10, retard, abss, ExcellentPar, MoyennePar, JamaisPar, ExcellenComp, Correcte, Mauvais);
        root.setAlignment(Pos.CENTER_LEFT);

        VBox root1 = new VBox(15, TitreASS, root);
        root1.setAlignment(Pos.CENTER_LEFT);
        root1.setStyle("-fx-padding: 15px; -fx-background-color: #edf4f5; ");
        Assi.getChildren().add(root1);
        Assi.setMargin(root1, new Insets(10, 10, 10, 10));
    }

}
