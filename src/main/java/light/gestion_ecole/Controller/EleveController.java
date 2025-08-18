package light.gestion_ecole.Controller;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import light.gestion_ecole.DAO.EleveDAO;
import light.gestion_ecole.Model.Eleve;

import java.sql.SQLException;
import java.util.Date;

public class EleveController {
    @FXML private TableView<Eleve> eleves;
    @FXML private TableColumn<Eleve, Boolean> selectColumn;
    @FXML private TableColumn<Eleve, String> idEleve;
    @FXML private TableColumn<Eleve, String> nomEleve;
    @FXML private TableColumn<Eleve, String> prenomEleve;
    @FXML private TableColumn<Eleve, String> adresseEleve;
    @FXML private TableColumn<Eleve, Date> datenaiss;
    @FXML private TableColumn<Eleve, String> genreEleve;

    @FXML private Label lblNum;
    @FXML private Label lblNom;
    @FXML private Label lblPrenom;
    @FXML private Label lblAdresse;
    @FXML private Label lblDateNaiss;
    @FXML private Label lblSexe;

    @FXML private TextField txtSearch;
    @FXML private ComboBox comboAnnee;
    @FXML private ComboBox comboClasse;


    private EleveDAO eleveDAO = new EleveDAO();

    @FXML
    public void initialize() throws SQLException {
        selectColumn.setCellValueFactory(param -> new SimpleBooleanProperty(false));
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));

        idEleve.setCellValueFactory(new PropertyValueFactory<>("nummat"));
        nomEleve.setCellValueFactory(new PropertyValueFactory<>("nomeleve"));
        prenomEleve.setCellValueFactory(new  PropertyValueFactory<>("prenomeleve"));
        adresseEleve.setCellValueFactory(new PropertyValueFactory<>("adresseeleve"));
        datenaiss.setCellValueFactory(new PropertyValueFactory<>("datenaissance"));
        genreEleve.setCellValueFactory(new PropertyValueFactory<>("genreeleve"));

        eleves.getColumns().forEach(col -> {
            col.setReorderable(false); // empêche de déplacer
            col.setResizable(false);   // empêche de redimensionner
        });
        eleves.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                lblNum.setText(newSelection.getNummat());
                lblNom.setText(newSelection.getNomeleve());
                lblPrenom.setText(newSelection.getPrenomeleve());
                lblAdresse.setText(newSelection.getAdresseeleve());
                lblDateNaiss.setText(newSelection.getDatenaissance().toString());
                lblSexe.setText(newSelection.getGenreeleve());
            }
        });

        txtSearch.textProperty().addListener((obs, oldVal, newVal) -> filterTable());
        comboAnnee.valueProperty().addListener((obs, oldVal, newVal) -> filterTable());
        comboClasse.valueProperty().addListener((obs, oldVal, newVal) -> filterTable());


        loadEleves();
    }

    private void loadEleves() throws SQLException {
        ObservableList<Eleve> data = FXCollections.observableArrayList(eleveDAO.getEleves());
        eleves.setItems(data);
    }

    private void filterTable() {
        /*String searchText = txtSearch.getText().toLowerCase();
        String selectedAnnee = comboAnnee.getValue();
        String selectedClasse = comboClasse.getValue();

        eleves.setItems(getEleves.filtered(eleve ->
                (searchText.isEmpty() || eleve.getNom().toLowerCase().contains(searchText)
                        || eleve.getId().toLowerCase().contains(searchText))
                        && (selectedAnnee == null || eleve.getAnnee().equals(selectedAnnee))
                        && (selectedClasse == null || eleve.getClasse().equals(selectedClasse))
        ));*/
    }

}
