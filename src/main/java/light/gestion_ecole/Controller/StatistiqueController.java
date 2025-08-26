package light.gestion_ecole.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import light.gestion_ecole.DAO.StatDAO;
import java.sql.SQLException;
import java.util.List;


public class StatistiqueController {
    @FXML ComboBox anneeScolStat;
    @FXML Label titreStat;
    @FXML Label titreAnnee;
    @FXML ComboBox statMnu;
    @FXML Label nbEleve;
    @FXML Label MG;
    @FXML Label TX;
    @FXML Label TX1;
    @FXML Label CM;
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
    @FXML HBox TXREU;
    @FXML HBox AssI;
    @FXML HBox EXA;
    private String statQUI;

    private List<String> listeAnneScol = StatDAO.getAnnescolaire();
    public List<HBox> tousLesMenuStat;

    @FXML
    public void initialize() throws SQLException {
        tousLesMenuStat = List.of(effecEL, moyG, TXREU, AssI, EXA);
        legendEffec.setManaged(false);
        legendEffec.setVisible(false);
        anneeScolStat.getItems().add("Tous");
        anneeScolStat.getItems().addAll(listeAnneScol);
        anneeScolStat.getSelectionModel().select(1);
        statQUI = "effecEL";
        affocheEffecEl();
        styliserMenuStat(effecEL);
        afficherStat();
        statMnu.getSelectionModel().select(0);
        anneeScolStat.setOnAction(event -> {
            try {
                afficherStat();
                if (statQUI.equals("effecEL")){
                    affocheEffecEl();
                }else if (statQUI.equals("moyG")){
                    if (anneeScolStat.getSelectionModel().getSelectedItem().equals("Tous")){
                        PaneChart.getChildren().clear();
                        PaneChart.getChildren().add(afficheChartG("moyG"));
                    }else{
                        afficherMGparTRimestre();
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        statMnu.setOnAction(e -> {
            afficherTitrStat();
        });
        effecEL.setOnMouseClicked(event -> {
            statQUI = "effecEL";
            affocheEffecEl();
            styliserMenuStat(effecEL);
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
        });
        TXREU.setOnMouseClicked(event -> {
            PaneChart.getChildren().clear();
            try {
                PaneChart.getChildren().add(afficheChartG("Taux"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            styliserMenuStat(TXREU);
        });
        AssI.setOnMouseClicked(event -> {
            PaneChart.getChildren().clear();
            try {
                PaneChart.getChildren().add(afficheChartG("Assi"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            styliserMenuStat(AssI);
        });
        EXA.setOnMouseClicked(event -> {
            PaneChart.getChildren().clear();
            try {
                PaneChart.getChildren().add(afficheChartG("EXA"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            styliserMenuStat(EXA);
        });

    }
    public LineChart<String, Number> afficheChartG(String st) throws SQLException {
        legendEffec.setManaged(false);
        legendEffec.setVisible(false);
        int[] listNBEleve = new int[listeAnneScol.size()];
        double[] listMG = new double[listeAnneScol.size()];
        String[] listTX = new String[listeAnneScol.size()];
        String[] listEX = new String[listeAnneScol.size()];
        double[] listAss =  new double[listeAnneScol.size()];
        int i = 0;
        List<String> listeAnneScolReverse = listeAnneScol.reversed();
        for(String anne :  listeAnneScolReverse){
            listNBEleve[i] = StatDAO.getNBEleve(anne);
            listMG[i] = StatDAO.getMG(anne);
            listAss[i] = StatDAO.getScoreAssiduiteMoyen(anne);
            if(StatDAO.getTauxReussite(anne).equals("En cours")){
                listTX[i] = "vide";
            }else{
                String[] val = StatDAO.getTauxReussite(anne).split("%");
                listTX[i] = val[0];
            }
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
            Ass.setName("Note d' assiduité");
            Examens.setName("Taux de réussite aux examens Nationaux");
        /*Données*/
            for (String anne : listeAnneScolReverse){
                Effectif.getData().add(new XYChart.Data<>(anne, listNBEleve[i]));
                MG.getData().add(new XYChart.Data<>(anne, listMG[i]));
                Ass.getData().add(new XYChart.Data<>(anne,listAss[i]));
                if (!listTX[i].equals("vide")){
                    double valueTX = Double.parseDouble(listTX[i].replace(",","."));
                    Taux.getData().add(new XYChart.Data<>(anne, valueTX));
                }
                if (!listEX[i].equals("vide")){
                    double valueEX = Double.parseDouble(listEX[i].replace(",","."));
                    Examens.getData().add(new XYChart.Data<>(anne, valueEX));
                }
                i++;
            }


        /*Ajout des Séries*/

            if (st.equals("NBEleve")){
                lineChart.getData().add(Effectif);
            } else if (st.equals("moyG")) {
                lineChart.getData().add(MG);
            }else if(st.equals("Taux")){
                lineChart.getData().add(Taux);
            }else if (st.equals("Assi")){
                lineChart.getData().add(Ass);
            }else if(st.equals("EXA")){
                lineChart.getData().add(Examens);
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
            } else if (series.getName().contains("assiduité")) {
                series.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: #f3d176;");
            } else if (series.getName().contains("examens")) {
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
            TX.setText("");
            CM.setText("");
            EXAM.setText("");
        }else{
            titreAnnee.setText(titreAnnescolaire);
            nbEleve.setText(String.valueOf(StatDAO.getNBEleve(titreAnnescolaire)));
            try {
                MG.setText(String.valueOf(StatDAO.getMG(titreAnnescolaire)) + "/20");
                CM.setText(String.valueOf(StatDAO.getScoreAssiduiteMoyen(titreAnnescolaire)) + "/10");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            String[] tx = StatDAO.getTauxReussite(titreAnnescolaire).split("-");
            if (tx.length == 2) {
                TX.setText(tx[0]);
                TX1.setText(String.format("(Taux de reussite %s)",tx[1]));
            }else{
                TX.setText(tx[0]);
                TX1.setText("Taux de reussite");
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
    public void afficherTitrStat(){
        int index1 = statMnu.getSelectionModel().getSelectedIndex();
        String menuSelectedStat = statMnu.getItems().get(index1).toString();
        if (menuSelectedStat.equals("ECOLE")) {
            titreStat.setText("Statistiques Globale de l'Ecole: ");
        } else if (menuSelectedStat.equals("ELEVE")) {
            titreStat.setText("Statistiques de chaque Elève: ");
        } else if (menuSelectedStat.equals("PROFESSEUR")) {
            titreStat.setText("Statistiques des professeurs: ");
        } else if (menuSelectedStat.equals("CLASSE")) {
            titreStat.setText("Statistiques de Chaque Classe: ");
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
        if (moyenne[0] != 0) series.getData().add(new XYChart.Data<>("Interro1", moyenne[0]));
        if (moyenne[1] != 0) series.getData().add(new XYChart.Data<>("Examen I", moyenne[1]));
        if (moyenne[2] != 0) series.getData().add(new XYChart.Data<>("Interro2", moyenne[2]));
        if (moyenne[3] != 0) series.getData().add(new XYChart.Data<>("Examen II", moyenne[3]));
        if (moyenne[4] != 0) series.getData().add(new XYChart.Data<>("Interro3", moyenne[4]));
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


}
