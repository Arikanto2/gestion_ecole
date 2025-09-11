package light.gestion_ecole.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import light.gestion_ecole.DAO.AbsenceDAO;
import light.gestion_ecole.DAO.ClasseDAO;
import light.gestion_ecole.DAO.StatDAO;
import light.gestion_ecole.Model.Absence;
import light.gestion_ecole.Model.Classe;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AbsenceController {
    @FXML private Button btnNewAb;
    @FXML public ComboBox comboClasse;
    @FXML private TableView<Absence> abs;
    @FXML private TextField searchField;
    @FXML private TableColumn<Absence, Integer> idAbsence;
    @FXML private TableColumn<Absence, String> idEleve;
    @FXML private TableColumn<Absence, String> nomEleve;
    @FXML private TableColumn<Absence, String> dateAbs;
    @FXML private TableColumn<Absence, String> dateRet;
    @FXML private TableColumn<Absence, String> motif;
    @FXML private Label nbAbs;

    String anne = StatDAO.getAnnescolaire().get(0);
    public List<Classe> classe = ClasseDAO.getAllClasses(anne);

    private ObservableList<Absence> masterData = FXCollections.observableArrayList();
    private FilteredList<Absence> filteredData;

    public AbsenceController() throws SQLException {}

    @FXML
    void initialize() throws SQLException {
        idAbsence.setCellValueFactory(new PropertyValueFactory<>("idAbsence"));
        idEleve.setCellValueFactory(new PropertyValueFactory<>("numero"));
        nomEleve.setCellValueFactory(new PropertyValueFactory<>("nomComplet"));
        dateAbs.setCellValueFactory(new PropertyValueFactory<>("dateAbsence"));
        dateRet.setCellValueFactory(new PropertyValueFactory<>("dateRetour"));
        motif.setCellValueFactory(new PropertyValueFactory<>("motif"));

        idAbsence.setVisible(false);

        abs.setRowFactory(tv -> {
            TableRow<Absence> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Absence selectedAbs = row.getItem();
                    ouvrirFenetreAbsence(selectedAbs);
                }
            });
            return row;
        });

        abs.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        abs.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double tableWidth = newWidth.doubleValue();
            idEleve.setPrefWidth(tableWidth * 0.10);
            motif.setPrefWidth(tableWidth * 0.20);
            dateAbs.setPrefWidth(tableWidth * 0.20);
            dateRet.setPrefWidth(tableWidth * 0.20);
            nomEleve.setPrefWidth(tableWidth - (idEleve.getWidth() + motif.getWidth()
                    + dateAbs.getWidth()
                    + dateRet.getWidth()));
        });

        idEleve.setResizable(false);
        nomEleve.setResizable(false);
        dateAbs.setResizable(false);
        dateRet.setResizable(false);
        motif.setResizable(false);

        comboClasse.setPrefWidth(90);
        comboClasse.setStyle("-fx-font-size: 13;");

        for (Classe c : classe) {
            comboClasse.getItems().add(c.getDesignation());
        }
        comboClasse.getSelectionModel().select(0);
        afficheAbs(comboClasse.getSelectionModel().getSelectedItem().toString());

        comboClasse.setOnAction(e -> {
            String c = comboClasse.getSelectionModel().getSelectedItem().toString();
            try {
                afficheAbs(c);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        btnNewAb.setOnAction(e -> ouvrirFenetreAbsence(null));

        filteredData = new FilteredList<>(masterData, p -> true);
        searchField.textProperty().addListener((obs, oldValue, newValue) -> {
            filteredData.setPredicate(absence -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lower = newValue.toLowerCase();
                return String.valueOf(absence.getIdAbsence()).toLowerCase().contains(lower)
                        || absence.getNumero().toLowerCase().contains(lower)
                        || absence.getNomComplet().toLowerCase().contains(lower)
                        || absence.getDateAbsence().toLowerCase().contains(lower)
                        || (absence.getDateRetour() != null && absence.getDateRetour().toLowerCase().contains(lower));
            });
        });
        SortedList<Absence> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(abs.comparatorProperty());
        abs.setItems(sortedData);
    }

    public void afficheAbs(String str) throws SQLException {
        Classe resultat = classe.stream()
                .filter(cl -> cl.getDesignation().equals(str))
                .findFirst()
                .orElse(null);

        AbsenceDAO absence = new AbsenceDAO();
        List<Absence> absences = absence.getAbsence(resultat);

        masterData.setAll(absences);

        if (resultat != null) {
            int nb = StatDAO.getScoreAbsenceParClasse(resultat.getIdClasse());
            nbAbs.setText(String.valueOf(nb));
        }
    }

    private void ouvrirFenetreAbsence(Absence absence) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/light/gestion_ecole/View/AjoutAbsence-View.fxml"));
            Parent root = loader.load();

            AjoutAbsenceController ajoutController = loader.getController();
            ajoutController.setAbsenceController(this);
            if (absence != null) {
                ajoutController.setAbsence(absence);
            }

            Stage stage = new Stage();
            stage.setTitle(absence == null ? "Nouvelle absence" : "Modifier absence");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setMaxWidth(400);
            stage.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
