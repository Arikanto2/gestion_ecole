package light.gestion_ecole.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Text;
import light.gestion_ecole.DAO.StatDAO;
import java.sql.SQLException;
import java.util.*;



public class StatistiqueController {
    @FXML ComboBox anneeScolStat;
    @FXML Label titreStat;
    @FXML Label titreAnnee;
    @FXML Label nbEleve;
    @FXML Label MG;
    @FXML Label EXAM;
    @FXML Label EXAM1;
    @FXML VBox legendEffec;
    @FXML Label renv;
    @FXML Label audition;
    @FXML Label vision;
    @FXML Label intel;
    @FXML Label psycho;
    @FXML HBox PaneChart;
    @FXML HBox effecEL;
    @FXML HBox moyG;
    @FXML HBox AssI;
    @FXML HBox EXA;
    @FXML Label tit;
    private String statQUI;

    private List<String> listeAnneScol = StatDAO.getAnnescolaire();
    public List<HBox> tousLesMenuStat;

    @FXML
    public void initialize() throws SQLException {
        tousLesMenuStat = List.of(effecEL, moyG, AssI, EXA);
        legendEffec.setManaged(false);
        legendEffec.setVisible(false);
        anneeScolStat.getItems().add("Tous");
        anneeScolStat.getItems().addAll(listeAnneScol);
        anneeScolStat.getSelectionModel().select(1);
        statQUI = "effecEL";
        affocheEffecEl();
        styliserMenuStat(effecEL);
        afficherStat();
        anneeScolStat.setOnAction(event -> {
            if(anneeScolStat.getSelectionModel().getSelectedItem().equals("Tous")){

                EXAM1.setText("(Examen Nationaux)");
            }
            try {
                afficherStat();
                switch (statQUI) {
                    case "effecEL" -> affocheEffecEl();
                    case "moyG" -> {
                        if (anneeScolStat.getSelectionModel().getSelectedItem().equals("Tous")) {
                            PaneChart.getChildren().clear();
                            PaneChart.getChildren().add(afficheChartG("moyG"));
                        } else {
                            afficherMGparTRimestre();
                        }
                    }
                    case "EXA" -> {
                        if (anneeScolStat.getSelectionModel().getSelectedItem().equals("Tous")) {
                            PaneChart.getChildren().clear();
                            PaneChart.getChildren().add(afficheChartG("EXA"));
                        } else {
                            PaneChart.getChildren().clear();
                            afficheEXAMAnne();
                        }
                    }
                    case "AssI" ->{
                        if (!anneeScolStat.getSelectionModel().getSelectedItem().equals("Tous")) {
                           afficheAssui();
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        effecEL.setOnMouseClicked(event -> {
            statQUI = "effecEL";
            affocheEffecEl();
            styliserMenuStat(effecEL);
            tit.setText("Graph représentant  l'effectif des élèves");
        });
        moyG.setOnMouseClicked(event -> {
            statQUI = "moyG";
            PaneChart.getChildren().clear();
            try {
                if (anneeScolStat.getSelectionModel().getSelectedItem().equals("Tous")){
                    PaneChart.getChildren().add(afficheChartG("moyG"));
                }else{
                    afficherMGparTRimestre();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            styliserMenuStat(moyG);
            tit.setText("Graph représentant la moyenne générale de l'école");
        });

        AssI.setOnMouseClicked(event -> {
            statQUI = "AssI";
            PaneChart.getChildren().clear();
            if(!anneeScolStat.getSelectionModel().getSelectedItem().equals("Tous")){
                afficheAssui();
            }else{

            }
            styliserMenuStat(AssI);
        });
        EXA.setOnMouseClicked(event -> {
            PaneChart.getChildren().clear();
            statQUI = "EXA";
            try {
                if (anneeScolStat.getSelectionModel().getSelectedItem().equals("Tous")){
                    PaneChart.getChildren().add(afficheChartG("EXA"));
                }else{
                    afficheEXAMAnne();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            tit.setText("Graph représentant le taux de réussite au examen nationaux");
            styliserMenuStat(EXA);
        });

    }
    public LineChart<String, Number> afficheChartG(String st) throws SQLException {
        legendEffec.setManaged(false);
        legendEffec.setVisible(false);
        int[] listNBEleve = new int[listeAnneScol.size()];
        double[] listMG = new double[listeAnneScol.size()];
        String[] listEX = new String[listeAnneScol.size()];
        int i = 0;
        List<String> listeAnneScolReverse = listeAnneScol.reversed();
        for(String anne :  listeAnneScolReverse){
            listNBEleve[i] = StatDAO.getNBEleve(anne);
            listMG[i] = StatDAO.getMG(anne);
            if(StatDAO.getExamenNat(anne).equals("En cours")){
                listEX[i] = "vide";
            }else{
                String[] val = StatDAO.getExamenNat(anne).split("%");
                listEX[i] = val[0];
            }
            i++;
        }
        i = 0;
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Année scolaire");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Valeur");
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setAnimated(true);
        /*Series*/
            XYChart.Series<String, Number> Effectif = new XYChart.Series<>();
            XYChart.Series<String, Number> MG = new XYChart.Series<>();
            XYChart.Series<String, Number> Taux = new XYChart.Series<>();
            XYChart.Series<String, Number> Ass = new XYChart.Series<>();
            XYChart.Series<String, Number> Examens = new XYChart.Series<>();
        /*Légende*/
            Effectif.setName("Effectif des élèves");
            MG.setName("Moyenne Générale");
            Taux.setName("Taux de Passage");
            Examens.setName("Taux de réussite aux examens Nationaux");
        /*Données*/
            for (String anne : listeAnneScolReverse){
                Effectif.getData().add(new XYChart.Data<>(anne, listNBEleve[i]));
                MG.getData().add(new XYChart.Data<>(anne, listMG[i]));
                if (!listEX[i].equals("vide")){
                    double valueEX = Double.parseDouble(listEX[i].replace(",","."));
                    Examens.getData().add(new XYChart.Data<>(anne, valueEX));
                }
                i++;
            }


        /*Ajout des Séries*/

        switch (st) {
            case "NBEleve" -> lineChart.getData().add(Effectif);
            case "moyG" -> lineChart.getData().add(MG);
            case "Taux" -> lineChart.getData().add(Taux);
            case "EXA" -> lineChart.getData().add(Examens);
        }


        // Couleur personnalisée + tooltip
        for (XYChart.Series<String, Number> series : lineChart.getData()) {
            // couleur selon le nom
            if (series.getName().contains("Effectif")) {
                series.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: #eca080;");
            } else if (series.getName().contains("Moyenne")) {
                series.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: #8bc2e5;");
            } else if (series.getName().contains("Taux de Passage")) {
                series.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: #aad9aa;");
            }  else if (series.getName().contains("examens")) {
                series.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: #e8b4e8;");
            }

            // tooltip pour chaque point
            for (XYChart.Data<String, Number> data : series.getData()) {
                Tooltip tooltip = new Tooltip(data.getXValue() + "  :  " + data.getYValue());
                tooltip.setStyle(
                        "-fx-background-color: #333333; " +
                                "-fx-text-fill: rgba(65, 3, 13, 0.91); " +
                                "-fx-font-size: 14px; " +
                                "-fx-padding: 10px; " +
                                "-fx-background-radius: 5px;" +
                                "-fx-background-color: white;"
                );
                Tooltip.install(data.getNode(), tooltip);
            }

        }

        return lineChart;
    }
    public PieChart afficheEffecAnne(){
        int[] valeur = StatDAO.getNBEleveAnnee(anneeScolStat.getSelectionModel().getSelectedItem().toString());

        ObservableList<PieChart.Data> pieData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Fille", valeur[0]),
                        new PieChart.Data("Garçon", valeur[1])
                );

        PieChart pieChart = new PieChart(pieData);
        pieChart.setLegendVisible(true);
        pieChart.setLabelsVisible(true);
        renv.setText(String.valueOf(valeur[2]));
        audition.setText(String.valueOf(valeur[3]));
        vision.setText(String.valueOf(valeur[4]));
        intel.setText(String.valueOf(valeur[5]));
        psycho.setText(String.valueOf(valeur[6]));
        for (PieChart.Data data : pieChart.getData()) {
            if (data.getName().equals("Fille")) {
                data.getNode().setStyle("-fx-pie-color: rgba(215,5,5,0.65);");
            } else if (data.getName().equals("Garçon")) {
                data.getNode().setStyle("-fx-pie-color: rgba(0,62,255,0.6);");
            }
        }
        pieChart.setLegendVisible(false);

        for (PieChart.Data data : pieChart.getData()) {
            Tooltip tooltip = new Tooltip(data.getName() + " : " + (int)data.getPieValue());
            Tooltip.install(data.getNode(), tooltip);
            tooltip.setStyle(
                    "-fx-background-color: #333333; " +
                            "-fx-text-fill: rgba(65, 3, 13, 0.91); " +
                            "-fx-font-size: 14px; " +
                            "-fx-padding: 10px; " +
                            "-fx-background-radius: 5px;" +
                            "-fx-background-color: white;"
            );
        }
        legendEffec.setManaged(true);
        legendEffec.setVisible(true);
        return pieChart;

    }
    public void afficherStat() throws SQLException {
        int index = anneeScolStat.getSelectionModel().getSelectedIndex();
        String titreAnnescolaire = anneeScolStat.getItems().get(index).toString();
        if(titreAnnescolaire.equals("Tous")){
            nbEleve.setText("");
            MG.setText("");
            EXAM.setText("");
        }else{
            titreAnnee.setText(titreAnnescolaire);
            nbEleve.setText(String.valueOf(StatDAO.getNBEleve(titreAnnescolaire)));
            try {
                MG.setText(String.valueOf(StatDAO.getMG(titreAnnescolaire)) + "/20");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            String[] exa1 = StatDAO.getExamenNat(titreAnnescolaire).split("-");
            if(exa1.length == 2){
                EXAM.setText(exa1[0]);
                EXAM1.setText("(Examens \n nationaux " + exa1[1] + " )");
            }else {
                EXAM.setText(exa1[0]);
                EXAM1.setText("(Examens  nationaux )");
            }

        }
    }
    private void styliserMenuStat(HBox menuActif) {
        String style = "-fx-background-color: linear-gradient(to right, #e6f7ff, rgb(255, 255, 255))";

        for (HBox hbox : tousLesMenuStat) {
            if (hbox == menuActif) {
                hbox.setStyle(style);
            } else {
                hbox.setStyle("");
            }
        }
    }
    public void affocheEffecEl(){
        try {
            if (anneeScolStat.getSelectionModel().getSelectedItem().equals("Tous")) {
                PaneChart.getChildren().clear();
                PaneChart.getChildren().add(afficheChartG("NBEleve"));
                legendEffec.setManaged(false);
                legendEffec.setVisible(false);
            }else{
                PaneChart.getChildren().clear();
                PaneChart.getChildren().add(afficheEffecAnne());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void afficherMGparTRimestre() {
        double[] moyenne = StatDAO.getMGParTrimestre(anneeScolStat.getSelectionModel().getSelectedItem().toString());
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
        PaneChart.getChildren().clear();
        PaneChart.getChildren().add(graph);
        legendEffec.setManaged(false);
        legendEffec.setVisible(false);
    }
    public void afficheEXAMAnne() {
        String ex = StatDAO.getExamenNat(anneeScolStat.getSelectionModel().getSelectedItem().toString());
        if (!ex.equals("En cours")) {
            List<List<String>> lis = StatDAO.getEXAMNATPardes(
                    anneeScolStat.getSelectionModel().getSelectedItem().toString());

            Map<String, Integer> dicafaka = new HashMap<>();
            Map<String, Integer> dictsafaka = new HashMap<>();

            for (List<String> l : lis) {
                String classe = l.get(0);
                String[] at = l.get(1).split("-");
                if (at[0].equals("A")) {
                    dicafaka.put(classe, Integer.parseInt(at[1]));
                } else {
                    dictsafaka.put(classe, Integer.parseInt(at[1]));
                }
            }
            for (Map.Entry<String, Integer> entry : dicafaka.entrySet()) {
                if (!dictsafaka.containsKey(entry.getKey())) {
                    dictsafaka.put(entry.getKey(), 0);
                }
            }
            for (Map.Entry<String, Integer> entry : dictsafaka.entrySet()) {
                if (!dicafaka.containsKey(entry.getKey())) {
                    dicafaka.put(entry.getKey(), 0);
                }
            }

            CategoryAxis xAxis = new CategoryAxis();
            xAxis.setLabel("Examen Nationnal");
            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel("Taux de réussite");

            BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
            List<XYChart.Series<String, Number>> series = new ArrayList<>();
            for(Map.Entry<String, Integer> entry : dicafaka.entrySet()) {
                Double pourcentage;
                pourcentage = (double) ((entry.getValue()/(entry.getValue() + dictsafaka.get(entry.getKey())))*100);
                XYChart.Series<String, Number> series1 = new XYChart.Series<>();
                series1.setName(entry.getKey());
                String exa;
                if (entry.getKey().equals("CM2")) {
                    exa = "CEPE";
                }else if (entry.getKey().equals("3eme")) {
                    exa = "BEPC";
                }else{
                    exa = "BACC (" + entry.getKey() + ")";
                }
                series1.getData().add(new XYChart.Data<>(exa, pourcentage));
                series.add(series1);
            }
            for(XYChart.Series<String, Number> series2 : series) {
                barChart.getData().addAll(series2);
            }
            for(int i = 0 ; i < series.size() ; i++) {
                for (XYChart.Data<String, Number> data : series.get(i).getData()) {
                    Tooltip tooltip = new Tooltip(data.getXValue() + "  :  " + data.getYValue()+ "%");
                    tooltip.setStyle(
                            "-fx-background-color: #333333; " +
                                    "-fx-text-fill: rgba(65, 3, 13, 0.91); " +
                                    "-fx-font-size: 14px; " +
                                    "-fx-padding: 10px; " +
                                    "-fx-background-radius: 5px;" +
                                    "-fx-background-color: white;"
                    );
                    Tooltip.install(data.getNode(), tooltip);

                }
            }
            VBox root = new VBox();
            root.setAlignment(Pos.CENTER_LEFT);
            for (Map.Entry<String, Integer> entry : dicafaka.entrySet()) {
                int nbafaka = entry.getValue();
                int nb = nbafaka + dictsafaka.get(entry.getKey());
                String exa;
                if (entry.getKey().equals("CM2")) {
                    exa = "CEPE";
                }else if (entry.getKey().equals("3eme")) {
                    exa = "BEPC";
                }else{
                    exa = "BACC (" + entry.getKey() + ")";
                }
                Text txt = new Text(exa + ": " + nbafaka + "/" + nb);
                txt.setFill(Color.web("#213572"));
                root.getChildren().add(txt);
            }
            HBox h = new HBox(barChart,root);
            PaneChart.getChildren().addAll(h);
        }else {
            Text encours = new Text("En cours...");
            encours.setFill(Color.web("#213572"));
            encours.setStyle("-fx-font-family: \"Book Antiqua\";\n" +
                    "    -fx-font-size: 30;\n" +
                    "    -fx-text-fill: rgba(6, 56, 86, 0.91);");

            PaneChart.getChildren().add(encours);
        }
        legendEffec.setManaged(false);
        legendEffec.setVisible(false);

    }
    public void afficheAssui() {
        double nbElve = StatDAO.getNBEleve(anneeScolStat.getSelectionModel().getSelectedItem().toString());
        int nbAbsence = StatDAO.getScoreAbsenceGlobal(anneeScolStat.getSelectionModel().getSelectedItem().toString());
        int nbExcellenteComp = 0;
        int nbExcellentPa = 0;
        int nbCorrect = 0;
        int nbMoyenne = 0;
        int nbJamais = 0;
        int nbMauvais = 0;
        int nbretard = StatDAO.getRetardsGlobal(anneeScolStat.getSelectionModel().getSelectedItem().toString());
        int[] comportement = StatDAO.getComportementGlobal(anneeScolStat.getSelectionModel().getSelectedItem().toString());
        int[] participation =  StatDAO.getParticipationGlobal(anneeScolStat.getSelectionModel().getSelectedItem().toString());
        nbExcellenteComp = comportement[0];
        nbCorrect = comportement[1];
        nbMauvais = comportement[2];
        nbExcellentPa = participation[0];
        nbMoyenne =  participation[1];
        nbJamais = participation[2];

        tit.setText("Assiduité des élèves");

        Label TitreASS = new Label("En moyenne, un élève :");
        TitreASS.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: rgba(6,56,86,0.91); -fx-font-family: Goudy Old Style;");

        Label retard = new Label("• a " + String.format("%.2f", nbretard/nbElve) + " retards en moyenne ("+ nbretard+"/"+(int)nbElve + ")");
        retard.setStyle("-fx-font-size: 14px; -fx-text-fill: rgba(6,56,86,0.91);");

        Label abss = new Label("• a " + String.format("%.2f", nbAbsence/nbElve) + " absences en moyenne ("+ nbAbsence+"/"+(int)nbElve + ")");
        abss.setStyle("-fx-font-size: 14px; -fx-text-fill: rgba(6,56,86,0.91);");

        Label ExcellentPar = new Label("• participe activement environ " + String.format("%.2f", nbExcellentPa/nbElve) + " fois");
        ExcellentPar.setStyle("-fx-font-size: 14px; -fx-text-fill: rgba(6,56,86,0.91);");

        Label MoyennePar = new Label("• participe moyennement environ " + String.format("%.2f", nbMoyenne/nbElve) + " fois");
        MoyennePar.setStyle("-fx-font-size: 14px; -fx-text-fill: rgba(6,56,86,0.91);");

        Label JamaisPar = new Label("• ne participe jamais dans environ " + String.format("%.2f", nbJamais/nbElve) + " cas");
        JamaisPar.setStyle("-fx-font-size: 14px; -fx-text-fill: rgba(6,56,86,0.91);");

        Label ExcellenComp = new Label("• a un comportement excellent dans " + String.format("%.2f", nbExcellenteComp/nbElve) + " cas");
        ExcellenComp.setStyle("-fx-font-size: 14px; -fx-text-fill: rgba(6,56,86,0.91);");

        Label Correcte = new Label("• a un comportement correct dans " + String.format("%.2f", nbCorrect/nbElve) + " cas");
        Correcte.setStyle("-fx-font-size: 14px; -fx-text-fill: rgba(6,56,86,0.91);");

        Label Mauvais =  new Label("• a un mauvais comportement dans " + String.format("%.2f", nbMauvais/nbElve) + " cas");
        Mauvais.setStyle("-fx-font-size: 14px; -fx-text-fill: rgba(6,56,86,0.91);");

        VBox root = new VBox(10, retard, abss, ExcellentPar, MoyennePar, JamaisPar, ExcellenComp, Correcte, Mauvais);
        root.setAlignment(Pos.CENTER_LEFT);

        VBox root1 = new VBox(15, TitreASS, root);
        root1.setAlignment(Pos.CENTER_LEFT);
        root1.setStyle("-fx-padding: 14px; -fx-background-color: #edf4f5; -fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10;");

        PaneChart.getChildren().clear();
        PaneChart.getChildren().add(root1);
        PaneChart.setMargin(root1, new Insets(20, 0, 20, 0));

        legendEffec.setManaged(false);
        legendEffec.setVisible(false);
    }







}
