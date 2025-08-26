package light.gestion_ecole.Controller;

import javafx.animation.FadeTransition;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import light.gestion_ecole.DAO.*;
import light.gestion_ecole.Model.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class EleveController {
    @FXML private TableView<Eleve> eleves;
    @FXML private TableColumn<Eleve, Boolean> selectColumn;
    @FXML private TableColumn<Eleve, String> idEleve;
    @FXML private TableColumn<Eleve, String> nomEleve;
    @FXML private TableColumn<Eleve, String> prenomEleve;
    @FXML private TableColumn<Eleve, String> adresseEleve;
    @FXML private TableColumn<Eleve, String> datenaiss;
    @FXML private TableColumn<Eleve, ImageView> genreEleve;
    //info General
    @FXML private Label lblNum;
    @FXML private Label lblNom;
    @FXML private Label lblPrenom;
    @FXML private Label lblAdresse;
    @FXML private Label lblDateNaiss;
    @FXML private Label lblSexe;
    @FXML private Label lblClasse;
    @FXML private Label lblAnneScolaire;
    @FXML private Label lblIspassant;
    @FXML private Label lblExamenNational;
    @FXML private Label lblHandicap;
    //tuteur
    @FXML private Label lblNomPere;
    @FXML private Label lblProfessionPere;
    @FXML private Label lblNomMere;
    @FXML private Label lblProfessionMere;
    @FXML private Label lblTuteurNom;
    @FXML private Label lblProfessionTuteur;
    @FXML private Label lblTuteurTel;
    @FXML private Label lblTuteurEmail;
    //attitude
    @FXML private TableView<AttitudeT>attitudes;
    @FXML private TableColumn<AttitudeT, String>dateattitudeColumn;
    @FXML private TableColumn<AttitudeT, String>participationColumn;
    @FXML private TableColumn<AttitudeT, String>comportementColumn;
    @FXML private TableColumn<AttitudeT, String>retardColumn;
    //note
    @FXML private TableView<NoteT>notes;
    @FXML private TableColumn<NoteT, String>matiereColumn;
    @FXML private TableColumn<NoteT, Integer>noteColumn;
    @FXML private TableColumn<NoteT, Double>coefficientColumn;
    @FXML private TableColumn<NoteT, String>commentaireColumn;
    //ecolage
    @FXML private TableView<EcolageparmoiT>ecolages;
    @FXML private TableColumn<EcolageparmoiT, String>dateEcolageColumn;
    @FXML private TableColumn<EcolageparmoiT, String>statutPayerColumn;

    @FXML private TextField txtSearch;
    @FXML private ComboBox comboAnnee;
    @FXML private ComboBox comboClasse;

    @FXML private Button btnAjouter;
    @FXML private Button btnModifier;

    @FXML private AnchorPane formOverlay;
    @FXML private ComboBox comboClasse2;
    @FXML private ComboBox comboSexe;
    @FXML private ComboBox comboHandicap;
    @FXML private TextField txtNom;
    @FXML private TextField txtPrenom;
    @FXML private TextField txtAnneeScolaire;
    //@FXML private TextField txtnummat;
    @FXML private DatePicker txtdateNaissance;
    @FXML private TextField txtAdresse;
    @FXML private TextField txtNomPere;
    @FXML private TextField txtProfessionPere;
    @FXML private TextField txtNomMere;
    @FXML private TextField txtProfessionMere;
    @FXML private TextField txtTuteur;
    @FXML private TextField  txtTuteurProfession;
    @FXML private TextField txtContact;
    @FXML private TextField txtEmail;

    @FXML private RadioButton passantOui;
    @FXML private RadioButton passantNon;
    @FXML private ToggleGroup passantGroup;
    @FXML private RadioButton nationalOui;
    @FXML private RadioButton nationalNon;
    @FXML private ToggleGroup nationalGroup;


    private List<Button> buttonList;

    private ObservableList<Eleve> allEleves;
    private EleveDAO eleveDAO = new EleveDAO();
    private ParentDAOT parentDAOT = new ParentDAOT();
    private AttitudeDAOT attitudeDAOT = new AttitudeDAOT();
    private NoteDAOT noteDAOT = new NoteDAOT();
    private EcolageDAOT ecolageDAOT = new  EcolageDAOT();

    @FXML
    public void initialize() throws SQLException {
        buttonList = List.of(btnAjouter,btnModifier);

        selectColumn.setCellValueFactory(param -> new SimpleBooleanProperty(false));
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));

        idEleve.setCellValueFactory(new PropertyValueFactory<>("nummat"));
        nomEleve.setCellValueFactory(new PropertyValueFactory<>("nomeleve"));
        prenomEleve.setCellValueFactory(new  PropertyValueFactory<>("prenomeleve"));
        adresseEleve.setCellValueFactory(new PropertyValueFactory<>("adresseeleve"));
        datenaiss.setCellValueFactory(new PropertyValueFactory<>("datenaissance"));
        genreEleve.setCellValueFactory(data ->
                new SimpleObjectProperty<>(data.getValue().getGenreeleveIcon()));

        dateattitudeColumn.setCellValueFactory(new PropertyValueFactory<>("dateattitude"));
        participationColumn.setCellValueFactory(new PropertyValueFactory<>("participation"));
        comportementColumn.setCellValueFactory(new PropertyValueFactory<>("comportement"));
        retardColumn.setCellValueFactory(new PropertyValueFactory<>("retard"));

        matiereColumn.setCellValueFactory(new PropertyValueFactory<>("matiere"));
        coefficientColumn.setCellValueFactory(new PropertyValueFactory<>("coefficient"));
        noteColumn.setCellValueFactory(new PropertyValueFactory<>("note"));
        commentaireColumn.setCellValueFactory(new PropertyValueFactory<>("commentaire"));

        dateEcolageColumn.setCellValueFactory(new PropertyValueFactory<>("ecolagemoi"));
        statutPayerColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));

        eleves.getColumns().forEach(col -> {
            col.setReorderable(false); // empêche de déplacer
            col.setResizable(false);   // empêche de redimensionner
        });
        attitudes.getColumns().forEach(col -> {
            col.setReorderable(false);
            col.setResizable(false);
        });
        notes.getColumns().forEach(col -> {
            col.setReorderable(false);
            col.setResizable(false);
        });
        eleves.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                try {
                    lblNum.setText(newSelection.getNummat());
                    lblNom.setText(newSelection.getNomeleve());
                    lblPrenom.setText(newSelection.getPrenomeleve());
                    lblAdresse.setText(newSelection.getAdresseeleve());
                    lblDateNaiss.setText(newSelection.getDatenaissance());
                    lblSexe.setText(newSelection.getGenreeleve());
                    lblClasse.setText(newSelection.getClasse());
                    lblAnneScolaire.setText(newSelection.getAnneescolaire());
                    lblIspassant.setText(newSelection.getIspassant());
                    lblExamenNational.setText(newSelection.getExamennational());
                    lblHandicap.setText(newSelection.getHandicap());

                    loadParent(newSelection.getIdparent());
                    loadAttitude(newSelection.getNummat());
                    loadNote(newSelection.getIdeleve());
                    loadEcolage(newSelection.getIdeleve());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });


        buttonList.forEach(button -> {
           button.setCursor(Cursor.HAND);
        });
        loadComboData();
        loadEleves();

        txtSearch.textProperty().addListener((obs, oldVal, newVal) -> filterTable());
        comboAnnee.valueProperty().addListener((obs, oldVal, newVal) -> filterTable());
        comboClasse.valueProperty().addListener((obs, oldVal, newVal) -> filterTable());
        passantGroup = new ToggleGroup();
        passantOui.setToggleGroup(passantGroup);
        passantNon.setToggleGroup(passantGroup);
        nationalGroup = new ToggleGroup();
        nationalOui.setToggleGroup(nationalGroup);
        nationalNon.setToggleGroup(nationalGroup);
    }
    private void loadParent(int idparent) throws SQLException {
        try{
            ParentT parentT = parentDAOT.getParents(idparent);
            if(parentT != null){
                lblNomPere.setText(parentT.getNompere());
                lblProfessionPere.setText(parentT.getProfessionpere());
                lblNomMere.setText(parentT.getNommere());
                lblProfessionMere.setText(parentT.getProfessionmere());
                lblTuteurNom.setText(parentT.getTuteur());
                lblProfessionTuteur.setText(parentT.getProfessiontuteur());
                lblTuteurTel.setText(parentT.getContact());
                lblTuteurEmail.setText(parentT.getEmailparent());

            }
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }
    private void loadAttitude(String nummat) throws SQLException {
        try {
            ObservableList<AttitudeT> attitudesList = FXCollections.observableArrayList(attitudeDAOT.getAttitudes(nummat));
            attitudes.setItems(attitudesList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void loadNote(String id) throws SQLException {
        try {
            ObservableList<NoteT> notesList = FXCollections.observableArrayList(noteDAOT.getNotes(id));
            notes.setItems(notesList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void loadEcolage(String id) throws SQLException {
        try {
            ObservableList<EcolageparmoiT> ecolageList = FXCollections.observableArrayList(ecolageDAOT.getEcolages(id));
            ecolages.setItems(ecolageList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void loadComboData() throws SQLException {
        comboAnnee.setItems(FXCollections.observableArrayList(eleveDAO.getDistinctAnnees()));
        comboClasse.setItems(FXCollections.observableArrayList(eleveDAO.getDistinctClasses()));
        comboClasse2.setItems(FXCollections.observableArrayList(eleveDAO.getDistinctClasses()));
    }
    private void loadEleves() throws SQLException {
        allEleves = FXCollections.observableArrayList(eleveDAO.getEleves());
        eleves.setItems(allEleves);
    }

    private void filterTable() {
        String searchText = txtSearch.getText() == null ? "" : txtSearch.getText().toLowerCase();
        String selectedAnnee = comboAnnee.getValue() == null ? null : comboAnnee.getValue().toString();
        String selectedClasse = comboClasse.getValue() == null ? null : comboClasse.getValue().toString();

        eleves.setItems(allEleves.filtered(eleve ->
                {
                    try {
                        return (searchText.isEmpty()
                                || eleve.getNomeleve().toLowerCase().contains(searchText)
                                || eleve.getPrenomeleve().toLowerCase().contains(searchText)
                                || eleve.getNummat().toLowerCase().contains(searchText))
                                && (selectedAnnee == null || eleve.getAnneescolaire().equals(selectedAnnee))
                                && (selectedClasse == null || eleve.getClasse().equals(selectedClasse));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
        ));
    }

    private boolean getBooleanFromRadio(ToggleGroup group, RadioButton radioButton) {
        if (group.getSelectedToggle() == null) {
            return false;
        }
        return group.getSelectedToggle() == radioButton;
    }

    @FXML
    private void openEleveView() {
        formOverlay.setVisible(true);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), formOverlay);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    // Bouton "Annuler"
    @FXML
    private void handleCancel() throws SQLException {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), formOverlay);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        fadeOut.setOnFinished(e -> formOverlay.setVisible(false));
        fadeOut.play();
        loadEleves();
    }

    // Bouton "Enregistrer"
    @FXML
    private void handleSave() throws SQLException {

        ParentT parent = new ParentT(txtNomPere.getText(),txtProfessionPere.getText(),txtNomMere.getText(),
                txtProfessionMere.getText(),txtTuteur.getText(),txtTuteurProfession.getText(),txtContact.getText(),txtEmail.getText());
        parentDAOT.insertParents(parent);

        int idClass = eleveDAO.getIdClass((String) comboClasse2.getValue());
        int idPrt = parentDAOT.getIdParents(txtContact.getText());
        boolean estPassant = getBooleanFromRadio(passantGroup, passantOui);
        boolean estNational = getBooleanFromRadio(nationalGroup, nationalOui);
        int nb = eleveDAO.nbrEleves();
        String matricule = "00"+ nb+1 + getGenreeleve2();
        String id = matricule+'-'+ txtAnneeScolaire.getText();
        Eleve eleve = new Eleve(id,matricule,idClass,idPrt,txtNom.getText(),txtPrenom.getText(),
                txtAdresse.getText(),java.sql.Date.valueOf(txtdateNaissance.getValue()),(String) comboSexe.getValue(),txtAnneeScolaire.getText(),
                estPassant,estNational,(String) comboHandicap.getValue());
        eleveDAO.insertEleve(eleve);
        loadEleves();
        handleCancel();
    }
    public String getGenreeleve2() {
        if ((String) comboSexe.getValue() == "Garçon")
            return "G";
        return "F";
    }
}
