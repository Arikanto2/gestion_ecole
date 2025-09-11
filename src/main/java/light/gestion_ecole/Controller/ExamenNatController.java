package light.gestion_ecole.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import light.gestion_ecole.DAO.ExamnatDAO;
import light.gestion_ecole.DAO.StatDAO;
import light.gestion_ecole.Model.ExamenNa;
import org.controlsfx.control.Notifications;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExamenNatController {

    @FXML private ComboBox<String> comboClasse;
    @FXML private ComboBox<String> comboAnnescolaire;
    @FXML private TableView<ExamenNa> abs;
    @FXML private TableColumn<ExamenNa, Boolean> Action;
    @FXML private TableColumn<ExamenNa, String> idEleve;
    @FXML private TableColumn<ExamenNa, String> nomEleve;
    @FXML private TableColumn<ExamenNa, String> admission;
    @FXML private Button btnEnregistrer;
    @FXML private Label Taux;
    @FXML private VBox aj;
    @FXML private Button btnNew;
    @FXML private Button btnAnnuler;
    @FXML private ComboBox<String> comboClasseExamen;
    @FXML private ComboBox<String> comboClassNormal;
    @FXML private Button bt;

    private ObservableList<ExamenNa> examenList = FXCollections.observableArrayList();
    private final List<String> ideleve = new ArrayList<>();

    @FXML
    public void initialize() {
    remplirCombo();

        comboClasseExamen.getItems().addAll(ExamnatDAO.getNonClasseEXAMEN());
        comboClassNormal.getItems().addAll(ExamnatDAO.getClasseEXAMEN());

        aj.setVisible(false);
        aj.setManaged(false);

        btnNew.setOnMouseClicked(event -> {
            aj.setVisible(true);
            aj.setManaged(true);
            ColorAdjust effect = new ColorAdjust();
            effect.setBrightness(-0.5);
            abs.setEffect(effect);
            abs.setDisable(true);
        });

        btnAnnuler.setOnMouseClicked(event -> {
            aj.setVisible(false);
            aj.setManaged(false);
            abs.setEffect(null);
            abs.setDisable(false);
        });

        bt.setOnAction(event -> {
            String classeAjouter = comboClasseExamen.getSelectionModel().getSelectedItem() != null ?
                    comboClasseExamen.getSelectionModel().getSelectedItem() : "";
            String classeSupprimer = comboClassNormal.getSelectionModel().getSelectedItem() != null ?
                    comboClassNormal.getSelectionModel().getSelectedItem() : "";

            try {
                if (!classeAjouter.isEmpty()) ExamnatDAO.AjouterExamen(classeAjouter);
                if (!classeSupprimer.isEmpty()) ExamnatDAO.supprimerClasseExamen(classeSupprimer);

                showNotif("Succès", "L'ajout et la suppression de la classe ont été pris en compte", false);

                comboClasseExamen.getItems().setAll(ExamnatDAO.getNonClasseEXAMEN());
                comboClassNormal.getItems().setAll(ExamnatDAO.getClasseEXAMEN());
                remplirCombo();


            } catch (SQLException e) {
                showNotif("Erreur", "Problème lors de la mise à jour", true);
                e.printStackTrace();
            }
        });

        idEleve.setCellValueFactory(new PropertyValueFactory<>("numeroMatricule"));
        nomEleve.setCellValueFactory(new PropertyValueFactory<>("nomEleve"));
        admission.setCellValueFactory(new PropertyValueFactory<>("admission"));

        abs.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double tableWidth = newWidth.doubleValue();
            idEleve.setPrefWidth(tableWidth * 0.20);
            Action.setPrefWidth(tableWidth * 0.10);
            admission.setPrefWidth(tableWidth * 0.20);
            nomEleve.setPrefWidth(tableWidth - (idEleve.getWidth() + Action.getWidth() + admission.getWidth()));
        });

        Action.setCellFactory(col -> new TableCell<>() {
            private final CheckBox checkBox = new CheckBox();

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    ExamenNa e = getTableRow().getItem();
                    if (e != null) {
                        checkBox.setOnAction(ev -> {
                            Object selectedAnnee = comboAnnescolaire.getSelectionModel().getSelectedItem();
                            if (selectedAnnee != null) {
                                String id = e.getNumeroMatricule() + "-" + selectedAnnee.toString();
                                if (checkBox.isSelected()) {
                                    if (!ideleve.contains(id)) ideleve.add(id);
                                } else {
                                    ideleve.remove(id);
                                }
                            }
                        });
                    }
                    setGraphic(checkBox);
                }
            }
        });

        btnEnregistrer.setOnMouseClicked(event -> {
            String cl = comboClasse.getSelectionModel().getSelectedItem();
            if (cl != null && !cl.isEmpty()) {
                try {
                    ExamnatDAO.UpdateExamen(ideleve, cl);
                    showNotif("Succès", "Mise à jour effectuée", false);
                    ideleve.clear();
                    loadExamen();
                } catch (SQLException e) {
                    showNotif("Erreur", "Problème lors de la mise à jour", true);
                    e.printStackTrace();
                }
            } else {
                showNotif("Attention", "Veuillez sélectionner une classe", true);
            }
        });

        comboClasse.setOnAction(e -> loadExamen());
        comboAnnescolaire.setOnAction(e -> loadExamen());
    }

    private void loadExamen() {
        String classe = comboClasse.getValue();
        String annee = comboAnnescolaire.getValue();
        if (classe != null && annee != null) {
            Taux.setText(StatDAO.getExamenNatParClasse(annee, classe));
            examenList.clear();
            try {
                List<ExamenNa> list = ExamnatDAO.getExamenNa(annee, classe);
                examenList.addAll(list);
                abs.setItems(examenList);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void showNotif(String titre, String msg, boolean warning) {
        Label titleLabel = new Label(titre);
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");
        Label msgLabel = new Label(msg);
        msgLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

        VBox notifContent = new VBox(5, titleLabel, msgLabel);
        notifContent.setStyle(
                "-fx-background-color: " + (warning ? "#e74c3c" : "#2a9df4") + ";" +
                        "-fx-padding: 15;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-radius: 12;" +
                        "-fx-border-color: rgba(255,255,255,0.3);" +
                        "-fx-border-width: 1;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 10,0,0,2);"
        );

        Notifications notif = Notifications.create()
                .graphic(notifContent)
                .hideAfter(Duration.seconds(3))
                .position(Pos.BOTTOM_RIGHT);

        notif.show();
    }
    public void remplirCombo(){
        List<String> classe = ExamnatDAO.getClasseEXAMEN();
        List<String> anne = StatDAO.getAnnescolaire();
        comboClasse.getItems().clear();
        comboAnnescolaire.getItems().clear();
        comboClasse.getItems().addAll(classe);
        comboAnnescolaire.getItems().addAll(anne);
    }
}
