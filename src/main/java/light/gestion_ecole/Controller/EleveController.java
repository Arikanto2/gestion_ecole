package light.gestion_ecole.Controller;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import javafx.animation.FadeTransition;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import light.gestion_ecole.DAO.*;
import light.gestion_ecole.Main;
import light.gestion_ecole.Model.*;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import com.itextpdf.layout.element.Cell;


public class EleveController {
    @FXML private TableView<Eleve> eleves;
    @FXML private TableColumn<Eleve, String> idEleve;
    @FXML private TableColumn<Eleve, String> nomEleve;
    @FXML private TableColumn<Eleve, String> datenaiss;
    @FXML private TableColumn<Eleve, ImageView> genreEleve;
    @FXML private Button btNStat;
    //info General
    @FXML private Label lblNum;
    @FXML private Label lblNom;
    @FXML private Label lblAdresse;
    @FXML private Label lblDateNaiss;
    @FXML private Label lblSexe;
    @FXML private Label lblClasse;
    @FXML private Label lblAnneScolaire;
    @FXML private Label lblHandicap;
    @FXML private Label lblAvertissement;
    //tuteur
    @FXML private Label lblNomPere;
//    @FXML private Label lblProfessionPere;
    @FXML private Label lblNomMere;
//    @FXML private Label lblProfessionMere;
    @FXML private Label lblTuteurNom;
//    @FXML private Label lblProfessionTuteur;
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
    @FXML private Label lblTotalCoef;
    @FXML private Label lblTotalNote;
    @FXML private Label lblMoyenne;
    @FXML private Label lblMention;
    @FXML private Label lblRang;
    //ecolage
    @FXML private TableView<EcolageparmoiT>ecolages;
    @FXML private TableColumn<EcolageparmoiT, String>moiEcolageColumn;
    @FXML private TableColumn<EcolageparmoiT, String>dateEcolageColumn;
    @FXML private TableColumn<EcolageparmoiT, String>statutPayerColumn;

    @FXML private TextField txtSearch;
    @FXML private ComboBox comboAnnee;
    @FXML private ComboBox comboClasse;

    @FXML private Button btnAjouterT;
    @FXML private Button btnModifierT;

    @FXML private AnchorPane formOverlay;
    @FXML private ComboBox comboClasse2;
    @FXML private ComboBox comboSexe;
    @FXML private TextField txtNom;
    @FXML private TextField IM;
    @FXML private TextField txtPrenom;
    @FXML private TextField txtAnneeScolaire;
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

    @FXML private CheckBox check1;
    @FXML private CheckBox check2;
    @FXML private CheckBox check3;
    @FXML private CheckBox check4;

    @FXML private Button btnNewEleve;
    @FXML private Button btnEnregistrerT;
    @FXML private Button btnAnnulerT;

    @FXML private AnchorPane formOverlayUpdate;
    @FXML private ComboBox comboClasseModif;
    @FXML private ComboBox comboSexeModif;
    @FXML private CheckBox checkModif1;
    @FXML private CheckBox checkModif2;
    @FXML private CheckBox checkModif3;
    @FXML private CheckBox checkModif4;
    @FXML private TextField txtNomModif;
    @FXML private TextField txtPrenomModif;
    @FXML private TextField txtAnneeScolaireModif;
    @FXML private DatePicker txtdateNaissanceModif;
    @FXML private TextField txtAdresseModif;
    @FXML private TextField txtNomPereModif;
    @FXML private TextField txtProfessionPereModif;
    @FXML private TextField txtNomMereModif;
    @FXML private TextField txtProfessionMereModif;
    @FXML private TextField txtTuteurModif;
    @FXML private TextField  txtTuteurProfessionModif;
    @FXML private TextField txtContactModif;
    @FXML private TextField txtEmailModif;
    @FXML private Button btnEnregistrerModifT;
    @FXML private Button btnAnnulerModifT;

    @FXML private AnchorPane formOverlayAddChoise;
    @FXML private AnchorPane formOverlayAddAttitude;
    @FXML private AnchorPane formOverlayAddNote;
    @FXML private AnchorPane formOverlayAddEcolage;
    @FXML private Label lblNumAttitude;
    @FXML private Label lblNomAttitude;
    @FXML private DatePicker txtDate;
    @FXML private ComboBox comboParticipation;
    @FXML private ComboBox comboComportement;
    @FXML private TextField txtRetard;

    @FXML private ComboBox comboNote;
    @FXML private ComboBox comboAnneeNote;
    @FXML private ComboBox comboClasseNote;
    @FXML private ComboBox comboProf;
    @FXML private ComboBox comboMatiere;
    @FXML private ComboBox comboEvaluation;
    @FXML private TextField txtSiMatiere;
    @FXML private ComboBox<Double> comboCoef;
    @FXML private TextField txtSiCoef;
    @FXML private TableView<Eleve>attribuerNotes;
    @FXML private TableColumn<Eleve, String> columnMat;
    @FXML private TableColumn<Eleve, String> columnNom;
    @FXML private TableColumn<Eleve, Double> columnNote;
    @FXML private TableColumn<Eleve, String> columnComment;

    @FXML private  Label lblNumEcolage;
    @FXML private Label lblNomEcolage;
    @FXML private Label comboClasseEcolage;
    @FXML private DatePicker txtDateEcolage;
    @FXML private TableView<EcolageparmoiT> attribuerEcolages;
    @FXML private TableColumn<EcolageparmoiT, String> moiColumn;
    @FXML private TableColumn<EcolageparmoiT, Boolean> columnPayer;
    @FXML private Label lblPrixEcolage;
    @FXML private Label lblTotalEcolage;

    @FXML private AnchorPane formOverlayListEcolage;
    @FXML private TextField txtSearchListe;
    @FXML private ComboBox comboClasseListe;
    @FXML private ComboBox comboAnneeListe;
    @FXML private ComboBox comboMoisListe;
    @FXML private TableView<Eleve> listages;
    @FXML private TableColumn<Eleve, String> idListe;
    @FXML private TableColumn<Eleve, String> nomListe;
    @FXML private TableColumn<Eleve, String> ecoListe;

    @FXML private AnchorPane formOverlayAddAvertissement;
    @FXML private Label lblNumAvertissement;
    @FXML private Label lblNomAvertissement;
    @FXML private Label lblClasseAvertissement;
    @FXML private ComboBox comboAvertissement;

    @FXML private AnchorPane formOvelayAddReinscrit;
    @FXML private ComboBox comboClasseR;
    @FXML private TextField txtAnneeScolaireR;
    @FXML private TextField txtNomR;
    @FXML private TextField txtPrenomR;
    @FXML private DatePicker txtdateNaissanceR;
    @FXML private ComboBox comboSexeR;
    @FXML private TextField txtAdresseR;
    @FXML private CheckBox check1R;
    @FXML private CheckBox check2R;
    @FXML private CheckBox check3R;
    @FXML private CheckBox check4R;
    @FXML private TextField txtNomPereR;
    @FXML private TextField txtProfessionPereR;
    @FXML private TextField txtNomMereR;
    @FXML private TextField txtProfessionMereR;
    @FXML private TextField txtTuteurR;
    @FXML private TextField txtTuteurProfessionR;
    @FXML private TextField txtContactR;
    @FXML private TextField txtEmailR;

    private List<Button> buttonList;

    private ObservableList<Eleve> allElevesNonPayer;
    private ObservableList<Eleve> allEleves;
    private EleveDAO eleveDAO = new EleveDAO();
    private ParentDAOT parentDAOT = new ParentDAOT();
    private AttitudeDAOT attitudeDAOT = new AttitudeDAOT();
    private NoteDAOT noteDAOT = new NoteDAOT();
    private EcolageDAOT ecolageDAOT = new  EcolageDAOT();
    private ProfDAO profDAO = new ProfDAO();
    private ClasseDAO classeDAO = new ClasseDAO();
    private AttitudeT attitudeT;
    private EcolageparmoiT ecolageparmoiT;
    private Eleve selectedEleve;
    private  int som = 0;
    private final Map<String, NoteT> notesBuffer = new HashMap<>();
    private String MatiereNote;
    private double coeff;
    private List<String> moisPayes;
    private  int prix = 0;
    private String annee;

    @FXML
    public void initialize() throws SQLException {
        buttonList = List.of(btnAjouterT,btnModifierT,btnNewEleve,btnEnregistrerT,btnAnnulerT,btnEnregistrerModifT,btnAnnulerModifT);

        idEleve.setCellValueFactory(new PropertyValueFactory<>("nummat"));
        nomEleve.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNomeleve() + " " + cellData.getValue().getPrenomeleve()));
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

        moiEcolageColumn.setCellValueFactory(new PropertyValueFactory<>("moiseco"));
        dateEcolageColumn.setCellValueFactory(new PropertyValueFactory<>("ecolagemoi"));
        statutPayerColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
        idListe.setCellValueFactory(new PropertyValueFactory<>("nummat"));
        nomListe.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNomeleve()));
        ecoListe.setCellValueFactory(new PropertyValueFactory<>("listMoi"));

        btNStat.setOnMouseClicked(event -> {
            if(selectedEleve != null){
                StatUnElveController.eleve = selectedEleve;
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/light/gestion_ecole/View/StatUnEleve-View.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load(), 320, 240);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                Stage stage = new Stage();
                Main.setStageIcon(stage);
                stage.setTitle("Statistique (" + selectedEleve.getNummat() + ")");
                stage.setScene(scene);
                stage.setMinWidth(750);
                stage.setMinHeight(460);
                stage.setResizable(false);
                stage.setMaximized(false);
                stage.show();

            } else {
                Notification.showWarning("Veuillez selectionné un élève.");
            }
        });

        columnMat.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNummat()));
        columnNom.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNomeleve() + " " + cellData.getValue().getPrenomeleve()));

        columnNote.setCellFactory(col -> new TableCell<Eleve, Double>() {
            private final TextField textField = new TextField();

            {
                textField.setOnKeyReleased(event -> {
                    Eleve e = getTableRow().getItem();
                    if (e != null) {
                        if (comboMatiere.getValue() != null){
                            MatiereNote = (String) comboMatiere.getValue();
                        } else {
                            MatiereNote = txtSiMatiere.getText();
                        }
                        NoteT note = notesBuffer.computeIfAbsent(
                                e.getNummat(),
                                k -> noteDAOT.getOrCreateNoteForEleve(e, (String) comboEvaluation.getValue(), MatiereNote)
                        );

                        try {
                            double val = Double.parseDouble(textField.getText());
                            note.setNote(val);
                        } catch (NumberFormatException ignored) {
                            note.setNote(null);
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Eleve e = getTableRow().getItem();
                    if (e != null) {
                        if (comboMatiere.getValue() != null){
                            MatiereNote = (String) comboMatiere.getValue();
                        } else {
                            MatiereNote = txtSiMatiere.getText();
                        }
                        NoteT note = notesBuffer.computeIfAbsent(
                                e.getNummat(),
                                k -> noteDAOT.getOrCreateNoteForEleve(e, (String) comboEvaluation.getValue(), MatiereNote)
                        );

                        textField.setText(note.getNote() != null ? note.getNote().toString() : "");
                        setGraphic(textField);
                    }
                }
            }
        });

        columnComment.setCellFactory(col -> new TableCell<Eleve, String>() {
            private final TextField textField = new TextField();

            {
                textField.setOnKeyReleased(event -> {
                    Eleve e = getTableRow().getItem();
                    if (e != null) {
                        if (comboMatiere.getValue() != null){
                            MatiereNote = (String) comboMatiere.getValue();
                        } else {
                            MatiereNote = txtSiMatiere.getText();
                        }
                        NoteT note = notesBuffer.computeIfAbsent(
                                e.getNummat(),
                                k -> noteDAOT.getOrCreateNoteForEleve(e, (String) comboEvaluation.getValue(), MatiereNote)
                        );

                        note.setCommentaire(textField.getText());
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Eleve e = getTableRow().getItem();
                    if (e != null) {
                        if (comboMatiere.getValue() != null){
                            MatiereNote = (String) comboMatiere.getValue();
                        } else {
                            MatiereNote = txtSiMatiere.getText();
                        }
                        NoteT note = notesBuffer.computeIfAbsent(
                                e.getNummat(),
                                k -> noteDAOT.getOrCreateNoteForEleve(e, (String) comboEvaluation.getValue(), MatiereNote)
                        );

                        textField.setText(note.getCommentaire());
                        setGraphic(textField);
                    }
                }
            }
        });

        attribuerNotes.setEditable(true);
        moiColumn.setCellValueFactory(cellData ->new SimpleStringProperty(cellData.getValue().getMoiseco()));
        columnPayer.setCellValueFactory(cellData -> {
            SimpleBooleanProperty property = new SimpleBooleanProperty(cellData.getValue().isStatut());
            property.addListener((obs, oldVal, newVal) ->{
                cellData.getValue().setStatut(newVal);
                calculerTotal();
            });
            return property;
        });
        columnPayer.setCellFactory(col -> {
            CheckBoxTableCell<EcolageparmoiT, Boolean> cell = new CheckBoxTableCell<>() {
                @Override
                public void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                        EcolageparmoiT rowData = getTableView().getItems().get(getIndex());
                        setDisable(rowData.isDisabled());
                    }
                }
            };
            return cell;
        });
        attribuerEcolages.setEditable(true);

        eleves.getColumns().forEach(col -> {
            col.setReorderable(false);
            col.setResizable(false);
        });
        attitudes.getColumns().forEach(col -> {
            col.setReorderable(false);
            col.setResizable(false);
        });
        notes.getColumns().forEach(col -> {
            col.setReorderable(false);
            col.setResizable(false);
        });
        attribuerNotes.getColumns().forEach(col -> {
            col.setReorderable(false);
            col.setResizable(false);
        });
        ecolages.getColumns().forEach(col -> {
            col.setReorderable(false);
            col.setResizable(false);
        });
        listages.getColumns().forEach(col -> {
            col.setReorderable(false);
            col.setResizable(false);
        });

        eleves.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedEleve = newSelection;
                try {
                    lblNum.setText(newSelection.getNummat());
                    lblNom.setText(newSelection.getNomeleve() + " " + newSelection.getPrenomeleve());
                    lblAdresse.setText(newSelection.getAdresseeleve());
                    lblDateNaiss.setText(newSelection.getDatenaissance());
                    lblSexe.setText(newSelection.getGenreeleve());
                    lblClasse.setText(newSelection.getClasse());
                    lblAnneScolaire.setText(newSelection.getAnneescolaire());
                    lblHandicap.setText(newSelection.getHandicap());
                    lblAvertissement.setText(newSelection.getAvertissement());

                    loadParent(newSelection.getIdparent());
                    loadAttitude(newSelection.getIdeleve());
                    loadNote(newSelection.getIdeleve(),(String) comboNote.getValue());
                    loadEcolage(newSelection.getIdeleve());
                    loadResultat();
                    loadEcolagesForEleve(newSelection.getIdeleve());
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
        loadAttribuerNotes();
        loadEcolageNonPayer();

        txtSearch.textProperty().addListener((obs, oldVal, newVal) -> filterTable());
        comboAnnee.valueProperty().addListener((obs, oldVal, newVal) -> filterTable());
        comboClasse.valueProperty().addListener((obs, oldVal, newVal) -> filterTable());
        comboNote.valueProperty().addListener((obs, oldVal, newVal) -> filterNote());
        comboClasseListe.valueProperty().addListener((obs, oldVal, newVal) -> filterTableListe());
        comboAnneeListe.valueProperty().addListener((obs, oldVal, newVal) -> filterTableListe());
        comboMoisListe.valueProperty().addListener((obs, oldVal, newVal) -> filterTableListe());
        txtSearchListe.textProperty().addListener((obs, oldVal, newVal) -> filterTableListe());
        comboClasseNote.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (comboNote.getValue() != null && comboAnneeNote.getValue() != null) {
                try {
                    filterAttribuerNote();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        comboAnneeNote.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (comboNote.getValue() != null && comboAnneeNote.getValue() != null) {
                try {
                    filterAttribuerNote();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        comboMatiere.setOnAction(event -> {
            notesBuffer.clear();
            attribuerNotes.refresh();
        });
        comboCoef.getItems().addAll(1.0,2.0,3.0,4.0,5.0,6.0);

        eleves.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        autoResizeColumn2(nomEleve);
        autoResizeColumn2(datenaiss);

        attitudes.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        autoResizeColumn2(participationColumn);
        autoResizeColumn2(comportementColumn);

        notes.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        autoResizeColumn2(matiereColumn);
        autoResizeColumn2(commentaireColumn);

        attribuerNotes.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        autoResizeColumn2(columnNom);
        autoResizeColumn2(columnNote);
        autoResizeColumn2(columnComment);

        ecolages.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        autoResizeColumn2(moiEcolageColumn);
        autoResizeColumn2(dateEcolageColumn);

        listages.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        autoResizeColumn2(idListe);
        autoResizeColumn2(nomListe);
        autoResizeColumn2(ecoListe);
    }

    @FXML
    private void onSaveBTN() throws SQLException {
        if ((comboCoef.getValue() != null || !txtSiCoef.getText().isEmpty()) && (comboMatiere.getValue() != null || !txtSiMatiere.getText().isEmpty()) && comboProf.getValue() != null ) {
            for (Eleve e : attribuerNotes.getItems()) {
                try {
                    NoteT note = notesBuffer.get(e.getNummat());
                    if (note == null) continue;
                    int idprof = profDAO.getIdprof((String) comboProf.getValue());
                    if (comboCoef.getValue() != null){
                        coeff = comboCoef.getValue();
                    } else {
                        coeff =Double.parseDouble(txtSiCoef.getText());
                    }
                    String id = eleveDAO.getIdEleve(e.getNummat(),(String) comboAnneeNote.getValue());
                    if (comboMatiere.getValue() != null){
                        MatiereNote = (String) comboMatiere.getValue();
                    } else {
                        MatiereNote = txtSiMatiere.getText();
                    }
                    noteDAOT.saveOrUpdate(note, (String) comboEvaluation.getValue(),MatiereNote,idprof,coeff,id);
                    loadEleves();
                } catch (Exception ex){
                    throw new RuntimeException(ex);
                }
            }
            Notification.showSuccess("Note de "+MatiereNote+ " ajouté avec succé!");
            comboAnneeNote.setValue(comboAnnee.getValue());
            comboClasseNote.setPromptText("Classe");
            comboProf.setPromptText("Professeur");
            comboMatiere.setPromptText("Matiere");
            loadEleves();
            onNote();
        } else {
            Notification.showWarning("Veuillez-remplir les champs manquant!");
        }
        comboAnneeNote.setValue(comboAnnee.getValue());
        comboClasseNote.setPromptText("Classe");
        comboProf.setPromptText("Professeur");
        comboMatiere.setPromptText("Matiere");
        loadEleves();
    }
    @FXML
    private void onCancelBTN() throws SQLException {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), formOverlayAddNote);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        fadeOut.setOnFinished(e -> formOverlayAddNote.setVisible(false));
        fadeOut.play();
        loadEleves();
        btnAjouterT();
        comboAnneeNote.setValue(comboAnnee.getValue());
        comboClasseNote.setPromptText("Classe");
        comboProf.setPromptText("Professeur");
        comboMatiere.setPromptText("Matiere");
    }
    @FXML
    private void onAnnulerListe() throws SQLException {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), formOverlayListEcolage);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        fadeOut.setOnFinished(e -> formOverlayListEcolage.setVisible(false));
        fadeOut.play();
        loadEleves();
    }
    @FXML
    private void onEnregistrerEcolage() throws SQLException {
        ObservableList<EcolageparmoiT> data = attribuerEcolages.getItems();
        if (selectedEleve != null && txtDateEcolage.getValue() != null){
            for (EcolageparmoiT e : data) {
                if (e.isStatut() && !e.isDisabled()) {
                    ecolageparmoiT = new EcolageparmoiT(selectedEleve.getIdeleve(), selectedEleve.getNummat(),
                            e.getIdecolage(),true,java.sql.Date.valueOf(txtDateEcolage.getValue()),e.getMoiseco());
                    ecolageDAOT.insertEcolageparmoiT(ecolageparmoiT);
                }
            }
            Notification.showSuccess("Ecolage de "+ selectedEleve.getNomeleve2()+ " ajouté");
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), formOverlayAddEcolage);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);

            fadeOut.setOnFinished(event -> formOverlayAddEcolage.setVisible(false));
            fadeOut.play();
            loadEleves();
            loadEcolage(selectedEleve.getIdeleve());
        } else {
            if (txtDateEcolage.getValue() == null) {
                Notification.showWarning("Remplir la date d'ajout de l'ecolage!");
            } else {
                Notification.showWarning("Aucun élève selectionné!");
            }
        }
    }
    @FXML
    private void onListageEco() throws SQLException {
        formOverlayListEcolage.setVisible(true);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), formOverlayListEcolage);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
        loadEcolageNonPayer();
        comboAnneeListe.setValue(comboAnnee.getValue());
    }
    private void calculerTotal() {
        int nbSelection = 0;
        for (EcolageparmoiT e : attribuerEcolages.getItems()) {
            if (e.isStatut() && !e.isDisabled()) {
                nbSelection++;
            }
        }
        int total = prix * nbSelection;
        lblTotalEcolage.setText(total + "Ar");
    }
    @FXML
    private void onAnnulerEcolage() throws SQLException {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), formOverlayAddEcolage);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        fadeOut.setOnFinished(e -> formOverlayAddEcolage.setVisible(false));
        fadeOut.play();
        loadEleves();
        btnAjouterT();
    }
    @FXML
    private void btnModifierT() throws SQLException {
        Eleve selected = eleves.getSelectionModel().getSelectedItem();
        if (selected != null) {
            formOverlayUpdate.setVisible(true);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), formOverlayUpdate);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
            txtNomModif.setText(selected.getNomeleve());
            txtPrenomModif.setText(selected.getPrenomeleve());
            txtAnneeScolaireModif.setText(selected.getAnneescolaire());
            txtAdresseModif.setText(selected.getAdresseeleve());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.FRENCH);
            txtdateNaissanceModif.setValue(LocalDate.parse(selected.getDatenaissance(), formatter));
            comboSexeModif.setValue(selected.getGenreeleve());
            comboClasseModif.setValue(selected.getClasse());
            String res = selected.getHandicap();
            String[] selec = res.split("-");
            List<CheckBox> checkBoxes = List.of(checkModif1,checkModif2,checkModif3,checkModif4);

            for (CheckBox checkBox : checkBoxes) {
                checkBox.setSelected(false);
                for (String val : selec) {
                    if (checkBox.getText().equals(val)) {
                        checkBox.setSelected(true);
                    }
                }
            }
            int IdParent = selected.getIdparent();
            ParentT parentT = parentDAOT.getParents(IdParent);
            txtNomPereModif.setText(parentT.getNompere());
            txtProfessionPereModif.setText(parentT.getProfessionpere());
            txtNomMereModif.setText(parentT.getNommere());
            txtProfessionMereModif.setText(parentT.getProfessionmere());
            txtTuteurModif.setText(parentT.getTuteur());
            txtTuteurProfessionModif.setText(parentT.getProfessiontuteur());
            txtContactModif.setText(parentT.getContact());
            txtEmailModif.setText(parentT.getEmailparent());
        } else {
            Notification.showWarning("Aucun élève selectionné!");
        }
    }

    @FXML
    private void onAvertissement() throws SQLException {
        selectedEleve = eleves.getSelectionModel().getSelectedItem();
        if (selectedEleve != null) {
            onCancelButton();
            formOverlayAddAvertissement.setVisible(true);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), formOverlayAddAvertissement);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
            lblNumAvertissement.setText(selectedEleve.getNummat());
            lblNomAvertissement.setText(selectedEleve.getNomeleve());
            lblClasseAvertissement.setText(selectedEleve.getClasse());
        } else {
            Notification.showWarning("Aucun élève selectionné!");
        }
    }
    private void autoResizeColumn(TableColumn<?, ?> column) {
        Text t = new Text(column.getText());
        double max = t.getLayoutBounds().getWidth();

        for (int i = 0; i < eleves.getItems().size(); i++) {
            if (column.getCellData(i) != null){
                t = new Text(column.getCellData(i).toString());
                double calcWidth = t.getLayoutBounds().getWidth();
                if (calcWidth > max) {
                    max = calcWidth;
                }
            }
        }
        column.setPrefWidth(max + 25);
    }
    private void autoResizeColumn2(TableColumn<?, ?> column) {
        Text t = new Text(column.getText());
        double max = t.getLayoutBounds().getWidth();

        for (int i = 0; i < eleves.getItems().size(); i++) {
            if (column.getCellData(i) != null){
                t = new Text(column.getCellData(i).toString());
                double calcWidth = t.getLayoutBounds().getWidth();
                if (calcWidth > max) {
                    max = calcWidth;
                }
            }
        }
        column.setPrefWidth(max + 70);
    }
    private void loadEcolagesForEleve(String ideleve) {
        moisPayes = ecolageDAOT.getMoisPayes(ideleve);
        ObservableList<EcolageparmoiT> data = FXCollections.observableArrayList();
        String[] mois = {"Janvier", "Fevrier", "Mars", "Avril", "Mai", "Juin",
                "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Decembre"};
        for (String m : mois){
            EcolageparmoiT e = new EcolageparmoiT(m, false);
            e.setIdeleve(ideleve);
            if (moisPayes.contains(m)){
                e.setStatut(true);
                e.setDisabled(true);
            }
            data.add(e);
        }
        attribuerEcolages.setItems(data);
    }
    private void loadParent(int idparent) throws SQLException {
        try{
            ParentT parentT = parentDAOT.getParents(idparent);
            if(parentT != null){
                lblNomPere.setText(parentT.getNompere());
                //lblProfessionPere.setText(parentT.getProfessionpere());
                lblNomMere.setText(parentT.getNommere());
                //lblProfessionMere.setText(parentT.getProfessionmere());
                lblTuteurNom.setText(parentT.getTuteur());
                //lblProfessionTuteur.setText(parentT.getProfessiontuteur());
                lblTuteurTel.setText(parentT.getContact());
                lblTuteurEmail.setText(parentT.getEmailparent());
            }
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }
    private void loadAttitude(String idEleve) throws SQLException {
        try {
            ObservableList<AttitudeT> attitudesList = FXCollections.observableArrayList(attitudeDAOT.getAttitudes(idEleve));
            attitudes.setItems(attitudesList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void loadNote(String id,String evaluation) throws SQLException {
        try {
            ObservableList<NoteT> notesList = FXCollections.observableArrayList(noteDAOT.getNotes(id,evaluation));
            notes.setItems(notesList);
            loadResultat();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void filterNote(){
        String selectedComboNote = comboNote.getValue().toString();
        if (selectedEleve != null) {
            List<NoteT> list = noteDAOT.getNotes(selectedEleve.getIdeleve(),selectedComboNote);
            ObservableList<NoteT> notesList = FXCollections.observableArrayList(list);
            notes.setItems(notesList);
            loadResultat();
        }
    }
    private void filterAttribuerNote() throws SQLException {
        if (comboAnneeNote.getValue() != null && comboClasseNote.getValue() != null) {
            List<Eleve> list = eleveDAO.getNumNom(comboClasseNote.getValue().toString(),comboAnneeNote.getValue().toString());
            ObservableList<Eleve> eleves = FXCollections.observableArrayList(list);
            attribuerNotes.setItems(eleves);
        } else {
            attribuerNotes.setItems(null);
        }
    }
    private void loadResultat(){
        String selectedComboNote = comboNote.getValue().toString();
        if (selectedEleve != null) {
            lblTotalCoef.setText(String.valueOf(noteDAOT.getTotalCoef(selectedEleve.getIdeleve(),selectedComboNote)));
            lblTotalNote.setText(String.valueOf(noteDAOT.getTotalNote(selectedEleve.getIdeleve(),selectedComboNote)));
            double moyenne = noteDAOT.getMoyenne(selectedEleve.getIdeleve(),selectedComboNote);
            if (moyenne > 0){
                lblMoyenne.setText(String.valueOf(moyenne+"/20"));
                if (moyenne >= 18) {
                    lblMention.setText("Honorable");
                } else if (moyenne >= 16) {
                    lblMention.setText("Trés Bien");
                } else if (moyenne >= 14) {
                    lblMention.setText("Bien");
                } else if (moyenne >= 12) {
                    lblMention.setText("Assez-Bien");
                } else if (moyenne >= 10) {
                    lblMention.setText("Passable");
                } else {
                    lblMention.setText("Faible");
                }
            } else {
                lblMoyenne.setText("0/20");
                lblMention.setText("Aucun");
            }
            if (comboAnnee.getValue() != null) {
                String annee = comboAnnee.getValue().toString();
                lblRang.setText(noteDAOT.getRang(selectedEleve.getIdeleve(), selectedEleve.getIdclass(),selectedComboNote,annee)+"°/"+noteDAOT.getNbrEleve(selectedEleve.getIdclass(),selectedEleve.getAnneescolaire()));
            }
        } else {
            Notification.showWarning("Aucun élève selectionné!");
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
        comboAnnee.getSelectionModel().select(0);
        comboAnnee.setOnAction(event -> {
            try {
                loadEleves();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        comboClasse.setOnAction(event -> {
            try {
                loadEleves();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        comboClasse.setItems(FXCollections.observableArrayList(eleveDAO.getDistinctClasses()));
        comboClasse2.setItems(FXCollections.observableArrayList(eleveDAO.getDistinctClasses()));
        comboClasseModif.setItems(FXCollections.observableArrayList(eleveDAO.getDistinctClasses()));
        comboAnneeNote.setItems(FXCollections.observableArrayList(eleveDAO.getDistinctAnnees()));
        comboClasseNote.setItems(FXCollections.observableArrayList(eleveDAO.getDistinctClasses()));
        comboProf.setItems(FXCollections.observableArrayList(profDAO.getNomProf()));
        comboAnneeListe.setItems(FXCollections.observableArrayList(eleveDAO.getDistinctAnnees()));
        comboClasseListe.setItems(FXCollections.observableArrayList(eleveDAO.getDistinctClasses()));
        comboClasseR.setItems(FXCollections.observableArrayList(eleveDAO.getDistinctClasses()));
    }
    private void loadEleves() throws SQLException {
        if (comboAnnee.getSelectionModel().getSelectedItem() != null) {
            annee = comboAnnee.getSelectionModel().getSelectedItem().toString();
            allEleves = FXCollections.observableArrayList(eleveDAO.getEleves(annee));
            eleves.setItems(allEleves);
            if (selectedEleve != null){
                loadAttitude(selectedEleve.getIdeleve());
            }
            txtSearch.textProperty().addListener((obs, oldVal, newVal) -> filterTable());
            comboAnnee.valueProperty().addListener((obs, oldVal, newVal) -> {
                try {
                    loadEleves(); // recharge la nouvelle année
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            comboClasse.valueProperty().addListener((obs, oldVal, newVal) -> filterTable());
        }
    }

    private void loadAttribuerNotes() throws SQLException {
        if (comboAnneeNote.getValue() != null && comboClasseNote.getValue() != null){
            List<Eleve> list = eleveDAO.getNumNom(comboClasseNote.getValue().toString(),(String) comboAnneeNote.getValue());
            ObservableList<Eleve> eleves = FXCollections.observableArrayList(list);
            attribuerNotes.setItems(eleves);
        }
    }
    private void loadEcolageNonPayer() throws SQLException {
        if (comboAnneeListe.getValue() == null && comboClasseListe.getValue() == null && comboMoisListe.getValue() == null){
            allElevesNonPayer = FXCollections.observableArrayList(ecolageDAOT.getListNonPayer());
            listages.setItems(allElevesNonPayer);
        }

    }

    private void filterTable() {
        if (allEleves == null) {
            return;
        }
        String searchText = txtSearch.getText() == null ? "" : txtSearch.getText().toLowerCase();
        String selectedAnnee = comboAnnee.getValue() == null ? null : comboAnnee.getValue().toString();
        String selectedClasse = comboClasse.getValue() == null ? null : comboClasse.getValue().toString();

        eleves.setItems(allEleves.filtered(eleve -> {
            try {
                String nom = eleve.getNomeleve() == null ? "" : eleve.getNomeleve().toLowerCase();
                String prenom = eleve.getPrenomeleve() == null ? "" : eleve.getPrenomeleve().toLowerCase();
                String nummat = eleve.getNummat() == null ? "" : eleve.getNummat().toLowerCase();

                boolean matchesSearch = searchText.isEmpty()
                        || nom.contains(searchText)
                        || prenom.contains(searchText)
                        || nummat.contains(searchText);

                boolean matchesAnnee = selectedAnnee == null || eleve.getAnneescolaire().equals(selectedAnnee);
                boolean matchesClasse = selectedClasse == null || eleve.getClasse().equals(selectedClasse);

                return matchesSearch && matchesAnnee && matchesClasse;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }));
    }


    private void filterTableListe() {
        String searchText = txtSearchListe.getText() == null ? "" : txtSearchListe.getText().toLowerCase();
        String selectedAnnee = comboAnneeListe.getValue() == null ? null : comboAnneeListe.getValue().toString();
        String selectedMoi = comboMoisListe.getValue() == null ? null : comboMoisListe.getValue().toString();
        String selectedClasse = comboClasseListe.getValue() == null ? null : comboClasseListe.getValue().toString();

        listages.setItems(allElevesNonPayer.filtered(eleve ->
        {
            try {
                return (searchText.isEmpty()
                || eleve.getNomeleve().toLowerCase().contains(searchText)
                || eleve.getNummat().toLowerCase().contains(searchText))
                && (selectedAnnee == null || eleve.getAnneescolaire().equals(selectedAnnee))
                && (selectedClasse == null || eleve.getClasse().equals(selectedClasse))
//                && (selectedClasse == null || eleve.getIdclass() == eleveDAO.getIdClass(selectedClasse))
                && (selectedMoi == null || eleve.getListMoi().contains(selectedMoi));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        ));
    }

    @FXML
    private void openEleveView() {
        formOverlay.setVisible(true);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), formOverlay);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
        if (comboAnnee.getValue() != null) {
            txtAnneeScolaire.setText(comboAnnee.getValue().toString());
        }
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

    @FXML
    private void onCancelReinscrit() throws SQLException {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), formOvelayAddReinscrit);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        fadeOut.setOnFinished(e -> formOvelayAddReinscrit.setVisible(false));
        fadeOut.play();
        loadEleves();
        btnAjouterT();
    }

    // Bouton "Enregistrer"
    @FXML
    private void handleSave() throws SQLException {
        if (txtTuteur.getText() != null) {
            ParentT parent = new ParentT(txtNomPere.getText(),txtProfessionPere.getText(),txtNomMere.getText(),
                    txtProfessionMere.getText(),txtTuteur.getText(),txtTuteurProfession.getText(),txtContact.getText(),txtEmail.getText());
            if (comboClasse2.getValue() != null && txtAnneeScolaire.getText() != null && txtNom.getText() != null && txtAdresse.getText() != null
                    && txtdateNaissance.getValue() != null && comboSexe.getValue() != null) {
                parentDAOT.insertParents(parent);
                String im = IM.getText().trim();
                int idClass = eleveDAO.getIdClass((String) comboClasse2.getValue());
                int idPrt = parentDAOT.getIdParents(txtContact.getText());
                List<CheckBox> checkBoxes = List.of(check1,check2,check3,check4);
                List<String> checkString = new ArrayList<>();
                for (CheckBox checkBox : checkBoxes) {
                    if (checkBox.isSelected()) {
                        checkString.add(checkBox.getText());
                    }
                }
                String result = String.join("-",checkString);
                String nummat;

                if (im == null || im.isBlank()) {
                    nummat = String.valueOf(eleveDAO.getLastMatricule() + 1);
                } else {
                    nummat = im;
                }

                String id = nummat+'-'+ txtAnneeScolaire.getText();
                Eleve eleve = new Eleve(id,nummat,idClass,idPrt,txtNom.getText(),txtPrenom.getText(),
                        txtAdresse.getText(),java.sql.Date.valueOf(txtdateNaissance.getValue()),(String) comboSexe.getValue(),txtAnneeScolaire.getText(),
                        result);
                eleveDAO.insertEleve(eleve);
                Notification.showSuccess("Elève ajouter!");
                handleCancel();
            } else {
                Notification.showWarning("Veuillez-remplir bien les champs de l'élève et les parents!");
            }
        } else {
            Notification.showWarning("Veuillez-remplir les formulaires du parent!");
        }
        loadEleves();
        loadComboData();
    }
    @FXML
    private void onSaveReinscrit() throws SQLException {
        if (selectedEleve != null && comboClasseR.getValue() != null && !txtAnneeScolaireR.getText().isEmpty()) {
            ParentT parent = new ParentT(selectedEleve.getIdparent(),txtNomPereR.getText(),txtProfessionPereR.getText(),
                    txtNomMereR.getText(),txtProfessionMereR.getText(),txtTuteurR.getText(),txtTuteurProfessionR.getText(),
                    txtContactR.getText(),txtEmailR.getText());
            parentDAOT.updateParents(parent);

            int idClass = eleveDAO.getIdClass((String) comboClasseR.getValue());
            List<CheckBox> checkBoxes = List.of(check1R,check2R,check3R,check4R);
            List<String> checkString = new ArrayList<>();
            for (CheckBox checkBox : checkBoxes) {
                if (checkBox.isSelected()) {
                    checkString.add(checkBox.getText());
                }
            }
            if (!Objects.equals(txtAnneeScolaireR.getText(), selectedEleve.getAnneescolaire())){
                String result = String.join("-",checkString);
                String mat = selectedEleve.getNummat();
                String id = mat+'-'+ txtAnneeScolaireR.getText();
                Eleve eleve = new Eleve(id,mat,idClass,selectedEleve.getIdparent(),txtNomR.getText(),txtPrenomR.getText(),txtAdresseR.getText(),
                        java.sql.Date.valueOf(txtdateNaissanceR.getValue()),(String) comboSexeR.getValue(),txtAnneeScolaireR.getText(),result);
                eleveDAO.insertEleve(eleve);
                Notification.showSuccess("Ajout élève reussi!");
                loadEleves();
                FadeTransition fadeOut = new FadeTransition(Duration.millis(300), formOvelayAddReinscrit);
                fadeOut.setFromValue(1);
                fadeOut.setToValue(0);

                fadeOut.setOnFinished(e -> formOvelayAddReinscrit.setVisible(false));
                fadeOut.play();
            } else {
                Notification.showError("Verifie bien l'année scolaire que vous ajoute!");
            }
            comboAnnee.valueProperty().addListener((obs, oldVal, newVal) -> filterTable());
            comboClasse.valueProperty().addListener((obs, oldVal, newVal) -> filterTable());
        } else {
            if (comboClasseR.getValue() == null) {
                Notification.showWarning("Selectionner la classe de l'élève!");
            } else if (txtAnneeScolaireR.getText().isEmpty()) {
                Notification.showWarning("L'année scolaire est requit!");
            } else {
                Notification.showWarning("Aucun élève selectionner!");
            }
        }
    }
    @FXML
    private void handleSaveUpdate() throws SQLException {
        selectedEleve = eleves.getSelectionModel().getSelectedItem();
        if (txtNomModif.getText() != null && txtPrenomModif.getText() != null && txtPrenomModif.getText() != null && txtAdresseModif != null
        && txtdateNaissanceModif.getValue() != null && comboSexeModif.getValue() != null && txtAnneeScolaireModif.getText() != null && txtContactModif.getText() != null) {
            int idClass = eleveDAO.getIdClass((String) comboClasseModif.getValue());
            List<CheckBox> checkBoxes = List.of(checkModif1,checkModif2,checkModif3,checkModif4);
            List<String> checkString = new ArrayList<>();
            for (CheckBox checkBox : checkBoxes) {
                if (checkBox.isSelected()) {
                    checkString.add(checkBox.getText());
                }
            }
            String result = String.join("-",checkString);
            Eleve eleve1 = new Eleve(selectedEleve.getIdeleve(),selectedEleve.getNummat(),idClass,selectedEleve.getIdparent(),txtNomModif.getText(),
                    txtPrenomModif.getText(),txtAdresseModif.getText(),java.sql.Date.valueOf(txtdateNaissanceModif.getValue()),
                    (String) comboSexeModif.getValue(),txtAnneeScolaireModif.getText(),result);
            eleveDAO.updateEleve(eleve1);
            ParentT parentT = new ParentT(selectedEleve.getIdparent(),txtNomPereModif.getText(),txtProfessionPereModif.getText(),txtNomMereModif.getText(),
                    txtProfessionMereModif.getText(),txtTuteurModif.getText(),txtTuteurProfessionModif.getText(),txtContactModif.getText(),txtEmailModif.getText());
            parentDAOT.updateParents(parentT);
            Notification.showSuccess("Modification faites!");
        } else {
            Notification.showWarning("Veuillez-remplir bien les champs à modifié!");
        }
        handleCancelUpdate();
        loadEleves();
    }
    @FXML
    private void handleCancelUpdate() throws SQLException {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), formOverlayUpdate);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        fadeOut.setOnFinished(e -> formOverlayUpdate.setVisible(false));
        fadeOut.play();
        loadEleves();
    }
    public String getGenreeleve2() {
        if (Objects.equals((String) comboSexe.getValue(), "Garçon"))
            return "G";
        else
            return "F";
    }
    @FXML
    private void btnAjouterT(){
        formOverlayAddChoise.setVisible(true);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), formOverlayAddChoise);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }
    @FXML
    private void onCancelButton() throws SQLException{
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), formOverlayAddChoise);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        fadeOut.setOnFinished(e -> formOverlayAddChoise.setVisible(false));
        fadeOut.play();
        loadEleves();
    }
    @FXML
    private void onAttitude() throws SQLException{
        selectedEleve = eleves.getSelectionModel().getSelectedItem();
        if (selectedEleve != null) {
            onCancelButton();
            formOverlayAddAttitude.setVisible(true);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), formOverlayAddAttitude);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
            lblNumAttitude.setText(selectedEleve.getNummat());
            lblNomAttitude.setText(selectedEleve.getNomeleve());
        }
        else {
            Notification.showWarning("Aucun élève selectionné!");
        }
    }
    @FXML
    private void onNote() throws SQLException{
        selectedEleve = eleves.getSelectionModel().getSelectedItem();
        onCancelButton();
        formOverlayAddNote.setVisible(true);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), formOverlayAddNote);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
        comboAnneeNote.setValue(comboAnnee.getValue());
    }
    @FXML
    private void onReinscrit() throws SQLException{
        selectedEleve = eleves.getSelectionModel().getSelectedItem();
        if (selectedEleve != null) {
            onCancelButton();
            formOvelayAddReinscrit.setVisible(true);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), formOvelayAddReinscrit);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
            txtNomR.setText(selectedEleve.getNomeleve2());
            txtPrenomR.setText(selectedEleve.getPrenomeleve());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.FRENCH);
            txtdateNaissanceR.setValue(LocalDate.parse(selectedEleve.getDatenaissance(), formatter));
            comboSexeR.setValue(selectedEleve.getGenreeleve());
            txtAdresseR.setText(selectedEleve.getAdresseeleve());
            String handicap = selectedEleve.getHandicap();
            String[] res = handicap.split("-");
            List<CheckBox> checkBoxes = List.of(check1R,check2R,check3R,check4R);
            for (CheckBox checkBox : checkBoxes) {
                checkBox.setSelected(false);
                for (String val : res) {
                    if (checkBox.getText().equals(val)) {
                        checkBox.setSelected(true);
                    }
                }
            }
            ParentT parent = parentDAOT.getParents(selectedEleve.getIdparent());
            txtNomPereR.setText(parent.getNompere());
            txtProfessionPereR.setText(parent.getProfessionpere());
            txtNomMereR.setText(parent.getNommere());
            txtProfessionMereR.setText(parent.getProfessionmere());
            txtTuteurR.setText(parent.getTuteur());
            txtTuteurProfessionR.setText(parent.getProfessiontuteur());
            txtContactR.setText(parent.getContact());
            txtEmailR.setText(parent.getEmailparent());

        } else {
            Notification.showWarning("Aucun élève selectionné!");
        }
    }
    @FXML
    private void onEcolage() throws SQLException{
        selectedEleve = eleves.getSelectionModel().getSelectedItem();
        if (selectedEleve != null) {
            onCancelButton();
            formOverlayAddEcolage.setVisible(true);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), formOverlayAddEcolage);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
            lblNumEcolage.setText(selectedEleve.getNummat());
            lblNomEcolage.setText(selectedEleve.getNomeleve());
            comboClasseEcolage.setText(selectedEleve.getClasse());
            txtDateEcolage.setValue(LocalDate.now());
            int id = eleveDAO.getIdClass(comboClasseEcolage.getText());
            prix = classeDAO.getPrixEcolage(id);
            lblPrixEcolage.setText(prix+" Ar");
        } else {
            Notification.showWarning("Aucun élève selectionné!");
        }
    }
    @FXML
    private void btnAnnulerAttitude() throws SQLException{
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), formOverlayAddAttitude);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        fadeOut.setOnFinished(e -> formOverlayAddAttitude.setVisible(false));
        fadeOut.play();
        loadEleves();
        btnAjouterT();
    }
    @FXML
    private void btnAnnulerAvertissement() throws SQLException{
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), formOverlayAddAvertissement);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        fadeOut.setOnFinished(e -> formOverlayAddAvertissement.setVisible(false));
        fadeOut.play();
        loadEleves();
        btnAjouterT();
    }
    @FXML
    private void onPlus(){
        som +=1;
        txtRetard.setText(String.valueOf(som));
    }
    @FXML
    private void onMoins(){
        if (som > 0){
            som -=1;
        }
        txtRetard.setText(String.valueOf(som));
    }
    @FXML
    private void onEnregistrerAttitude() throws SQLException{
        if (selectedEleve != null && txtDate.getValue() != null && comboParticipation.getValue() != null && comboComportement.getValue() != null) {
            attitudeT = new AttitudeT(selectedEleve.getIdeleve(), selectedEleve.getNummat(),(String) comboParticipation.getValue(),
                    (String) comboComportement.getValue(),java.sql.Date.valueOf(txtDate.getValue()),Integer.parseInt(txtRetard.getText()));
            attitudeDAOT.InsertAttitude(attitudeT);
            btnAnnulerAttitude();
            onCancelButton();
            loadEleves();
        } else {
            Notification.showError("Veuillez-remplir les champs!");
        }
    }
    @FXML
    private void onSaveAvertissement() throws SQLException{
        if (selectedEleve != null && comboAvertissement.getValue() != null){
            try {
                eleveDAO.giveAvertissement(selectedEleve.getIdeleve(),(String) comboAvertissement.getValue());
                Notification.showSuccess(comboAvertissement.getValue()+" pour"+ selectedEleve.getNomeleve()+" matricule :"+ selectedEleve.getNummat());
                btnAnnulerAvertissement();
                loadEleves();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            if (comboAvertissement.getValue() == null){
                Notification.showWarning("Veuillez-selectionner l'avertissement a donné!");
            } else {
                Notification.showWarning("Aucun élève selectionné!");
            }
        }
    }

    @FXML
    private void onPdf() throws SQLException {
        if (selectedEleve != null && noteDAOT.getTotalCoef(selectedEleve.getIdeleve(),comboNote.getValue().toString()) != 0.0) {
            try {
                String userDesktop = System.getProperty("user.home") + "/Desktop/Listes_eleves/Bulletin";
                File dossier = new File(userDesktop);
                if (!dossier.exists()) {
                    dossier.mkdirs();
                }

                String filePath = userDesktop + "/Eleve_" + selectedEleve.getIdeleve().trim() + ".pdf";
                PdfWriter writer = new PdfWriter(filePath);
                PdfDocument pdfDoc = new PdfDocument(writer);

                // Marges larges et équilibrées
                Document document = new Document(pdfDoc, PageSize.A4, false);
                document.setMargins(50, 50, 50, 50);

                // Polices
                PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
                PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
                document.setFont(font);

                // --- Logo centré en haut ---
                try {
                    String logoPath = getClass().getResource("/light/gestion_ecole/Photo/logo.png").toExternalForm();
                    ImageData imageData = ImageDataFactory.create(logoPath);
                    Image logo = new Image(imageData);
                    logo.setHorizontalAlignment(HorizontalAlignment.CENTER);
                    logo.setHeight(70);
                    logo.setMarginBottom(20);
                    document.add(logo);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                // --- Titre ---
                String eval = comboEvaluation.getValue().toString().toUpperCase();
                Paragraph titre = new Paragraph("RELEVÉ DE NOTES D’" + eval)
                        .setFont(bold)
                        .setFontSize(18)
                        .setFontColor(ColorConstants.BLUE)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setUnderline()
                        .setMarginBottom(25);
                document.add(titre);

                // --- Infos élève ---
                Table infos = new Table(new float[]{3, 7});
                infos.setWidth(UnitValue.createPercentValue(100));
                infos.setMarginBottom(20);

                infos.addCell(new Cell().add(new Paragraph("Nom et Prénoms:")).setBold().setBackgroundColor(ColorConstants.LIGHT_GRAY));
                infos.addCell(new Cell().add(new Paragraph(selectedEleve.getNomeleve())));
                infos.addCell(new Cell().add(new Paragraph("Année scolaire:")).setBold().setBackgroundColor(ColorConstants.LIGHT_GRAY));
                infos.addCell(new Cell().add(new Paragraph(selectedEleve.getAnneescolaire())));
                infos.addCell(new Cell().add(new Paragraph("Classe:")).setBold().setBackgroundColor(ColorConstants.LIGHT_GRAY));
                infos.addCell(new Cell().add(new Paragraph(selectedEleve.getClasse())));
                document.add(infos);

                // --- Tableau des notes ---
                Table table = new Table(new float[]{4, 2, 2, 4});
                table.setWidth(UnitValue.createPercentValue(100));

                // En-têtes
                String[] headers = {"Matières", "Coefficient", "Note", "Commentaire"};
                for (String h : headers) {
                    table.addHeaderCell(new Cell()
                            .add(new Paragraph(h).setBold().setFontColor(ColorConstants.WHITE))
                            .setBackgroundColor(ColorConstants.BLUE)
                            .setTextAlignment(TextAlignment.CENTER));
                }

                // Lignes alternées
                boolean alternate = false;
                for (NoteT note : notes.getItems()) {
                    Color bg = alternate ? ColorConstants.LIGHT_GRAY : ColorConstants.WHITE;
                    table.addCell(new Cell().add(new Paragraph(note.getMatiere())).setBackgroundColor(bg));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(note.getCoefficient()))).setBackgroundColor(bg).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(note.getNote()))).setBackgroundColor(bg).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph(note.getCommentaire())).setBackgroundColor(bg).setTextAlignment(TextAlignment.CENTER));
                    alternate = !alternate;
                }
                document.add(table);

                // --- Résumé ---
                document.add(new Paragraph("\nRésumé")
                        .setFont(bold).setFontSize(14).setFontColor(ColorConstants.BLUE)
                        .setMarginTop(20));
                document.add(new Paragraph("Total coefficient : " + lblTotalCoef.getText()));
                document.add(new Paragraph("Total : " + lblTotalNote.getText()));
                document.add(new Paragraph("Moyenne : " + lblMoyenne.getText()));
                document.add(new Paragraph("Rang : " + lblRang.getText()));

                // --- Signature ---
                document.add(new Paragraph("\n\nFait le " + LocalDate.now().format(DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.FRENCH)))
                        .setTextAlignment(TextAlignment.RIGHT));
                document.add(new Paragraph("Le Directeur").setBold().setTextAlignment(TextAlignment.RIGHT));

                // --- Contact ---
                Link mail = new Link("lyceeloteranaivory@.com", PdfAction.createURI("mailto:lyceeloteranaivory@.com"));
                document.add(new Paragraph().add("Contact : ").add(mail).setMarginTop(15));

                document.close();
                Notification.showSuccess("PDF GÉNÉRÉ SUR LE BUREAU : " + filePath);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            if (selectedEleve == null) {
                Notification.showWarning("Aucun élève selectionné!");
            } else {
                Notification.showError("Aucun notes trouvé!");
            }
        }
    }

}