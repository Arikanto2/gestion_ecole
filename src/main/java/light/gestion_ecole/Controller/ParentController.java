package light.gestion_ecole.Controller;

import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import light.gestion_ecole.DAO.*;
import light.gestion_ecole.Model.*;

import javax.sound.sampled.Mixer;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class ParentController {

    @FXML private TableView<ParentT>tableParent;
    @FXML private TableColumn<Parent,String> nomPere;
    @FXML private TableColumn<Parent,String> professionPere;
    @FXML private TableColumn<Parent,String> nomMere;
    @FXML private TableColumn<Parent,String> professionMere;
    @FXML private TableColumn<Parent,String> tuteur;
    @FXML private TableColumn<Parent,String> professionTuteur;
    @FXML private TableColumn<Parent,String> contact;
    @FXML private TableColumn<Parent,String> emailParent;
    @FXML private ObservableList<ParentT> observableParent;
    @FXML private AnchorPane formOverlayParent;

    @FXML private TextField txtNomPere;
    @FXML private TextField txtProfessionPere;
    @FXML private TextField txtNomMere;
    @FXML private TextField txtProfessionMere;
    @FXML private TextField txtTuteur;
    @FXML private TextField txtProfessionTuteur;
    @FXML private TextField txtContact;
    @FXML private TextField txtEmail;
    @FXML private TextField rechercheParent;

    @FXML private TextField txtNomPere2;
    @FXML private TextField txtProfessionPere2;
    @FXML private TextField txtNomMere2;
    @FXML private TextField txtProfessionMere2;
    @FXML private TextField txtTuteur2;
    @FXML private TextField  txtTuteurProfession2;
    @FXML private TextField txtContact2;
    @FXML private TextField txtEmail2;
    @FXML private AnchorPane formOverlay;
    @FXML private ComboBox comboClasse2;
    @FXML private ComboBox comboSexe;
    @FXML private ComboBox comboHandicap;
    @FXML private TextField txtNom;
    @FXML private TextField txtPrenom;
    @FXML private TextField txtAnneeScolaire;
    @FXML private DatePicker txtdateNaissance;
    @FXML private TextField txtAdresse;

    @FXML private RadioButton passantOui;
    @FXML private RadioButton passantNon;
    @FXML private ToggleGroup passantGroup;
    @FXML private RadioButton nationalOui;
    @FXML private RadioButton nationalNon;
    @FXML private ToggleGroup nationalGroup;
    @FXML private Button enregistrerBtn;
    @FXML private Button annulerBtn;
    @FXML private Button btnAjouterT;
    @FXML private Button btnModifierT;
    @FXML private Button btnSupprimerT;
    @FXML private Button btnEnregistrerT;
    @FXML private Button btnAnnulerT;

    ParentDAOT parentDAOT = new ParentDAOT();
    EleveDAO eleveDAO = new EleveDAO();
    private List<Button> buttonList;


    @FXML
    public void initialize() throws SQLException {
        buttonList = List.of(enregistrerBtn, annulerBtn,btnAjouterT, btnModifierT, btnSupprimerT,btnEnregistrerT,btnAnnulerT);
        nomPere.setCellValueFactory(new PropertyValueFactory<>("nompere"));
        professionPere.setCellValueFactory(new PropertyValueFactory<>("professionpere"));
        nomMere.setCellValueFactory(new PropertyValueFactory<>("nommere"));
        professionMere.setCellValueFactory(new PropertyValueFactory<>("professionmere"));
        tuteur.setCellValueFactory(new PropertyValueFactory<>("tuteur"));
        professionTuteur.setCellValueFactory(new PropertyValueFactory<>("professiontuteur"));
        contact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        emailParent.setCellValueFactory(new PropertyValueFactory<>("emailparent"));
        rechercheParent.textProperty().addListener((observable, oldValue, newValue) -> filterTable());
        passantGroup = new ToggleGroup();
        passantOui.setToggleGroup(passantGroup);
        passantNon.setToggleGroup(passantGroup);
        nationalGroup = new ToggleGroup();
        nationalOui.setToggleGroup(nationalGroup);
        nationalNon.setToggleGroup(nationalGroup);
        buttonList.forEach(button -> {
            button.setCursor(Cursor.HAND);
        });
        loadComboData();
        loadParent();
        tableParent.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableParent.getColumns().forEach(column -> {
            column.setReorderable(false);
            column.setResizable(false);
        });
    }
    private void loadParent() throws SQLException {
        observableParent = FXCollections.observableArrayList(parentDAOT.getAllParents());
        tableParent.setItems(observableParent);
    }
    private void loadComboData() throws SQLException {
        comboClasse2.setItems(FXCollections.observableArrayList(eleveDAO.getDistinctClasses()));
    }

    @FXML
    private void OnDelete() throws SQLException {
        ParentT selected =  tableParent.getSelectionModel().getSelectedItem();
        if(selected!=null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Voulez-vous vraiment supprimer ceci?", ButtonType.YES,ButtonType.NO);
            alert.showAndWait().ifPresent(response -> {
                if(response == ButtonType.YES){
                    try {
                        parentDAOT.deleteParents(selected.getIdparent());
                        loadParent();
                    } catch (SQLException e){
                        e.printStackTrace();
                    }
                }
            });
        } else {
            Alert.AlertType alertType = Alert.AlertType.WARNING;
            Alert alert = new Alert(alertType);
            alert.setTitle("Attention");
            alert.setContentText("Aucun element selectionner");
            alert.showAndWait();
        }
    }
    @FXML
    private void OnUpdate() throws SQLException {
        ParentT selected =  tableParent.getSelectionModel().getSelectedItem();
        if(selected!=null){
            formOverlayParent.setVisible(true);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), formOverlayParent);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
            txtNomPere.setText(selected.getNompere());
            txtProfessionPere.setText(selected.getProfessionpere());
            txtNomMere.setText(selected.getNommere());
            txtProfessionMere.setText(selected.getProfessionmere());
            txtTuteur.setText(selected.getTuteur());
            txtProfessionTuteur.setText(selected.getProfessiontuteur());
            txtContact.setText(selected.getContact());
            txtEmail.setText(selected.getEmailparent());
        } else {
            Alert.AlertType alertType = Alert.AlertType.WARNING;
            Alert alert = new Alert(alertType);
            alert.setTitle("Attention");
            alert.setContentText("Aucun element selectionner");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleCancel() throws SQLException {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), formOverlayParent);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        fadeOut.setOnFinished(e -> formOverlayParent.setVisible(false));
        fadeOut.setOnFinished(e->formOverlay.setVisible(false));
        fadeOut.play();
        loadParent();
    }

    @FXML
    private void handleSubmit() throws SQLException {
        ParentT selected =  tableParent.getSelectionModel().getSelectedItem();
        ParentT parent = new ParentT(selected.getIdparent(),txtNomPere.getText(),txtProfessionPere.getText(),txtNomMere.getText(),
                txtProfessionMere.getText(),txtTuteur.getText(),txtProfessionTuteur.getText(),txtContact.getText(),txtEmail.getText());
        try {
            parentDAOT.updateParents(parent);
            handleCancel();
        } catch (SQLException ex) {
            throw new SQLException(ex);
        }
    }

    @FXML
    private void OnAddEleve() throws SQLException {
        ParentT selected =  tableParent.getSelectionModel().getSelectedItem();
        if(selected!=null){
            formOverlay.setVisible(true);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), formOverlay);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
            txtNomPere2.setText(selected.getNompere());
            txtProfessionPere2.setText(selected.getProfessionpere());
            txtNomMere2.setText(selected.getNommere());
            txtProfessionMere2.setText(selected.getProfessionmere());
            txtTuteur2.setText(selected.getTuteur());
            txtTuteurProfession2.setText(selected.getProfessiontuteur());
            txtContact2.setText(selected.getContact());
            txtEmail2.setText(selected.getEmailparent());
        } else {
            Alert.AlertType alertType = Alert.AlertType.INFORMATION;
            Alert alert = new Alert(alertType);
            alert.setTitle("Attention");
            alert.setContentText("Aucun element selectionner");
            alert.showAndWait();
        }
    }
    @FXML
    private void handleSave() throws SQLException {
        int idClass = eleveDAO.getIdClass((String) comboClasse2.getValue());
        ParentT selected =tableParent.getSelectionModel().getSelectedItem();
        int idPrt = selected.getIdparent();
        boolean estPassant = getBooleanFromRadio(passantGroup, passantOui);
        boolean estNational = getBooleanFromRadio(nationalGroup, nationalOui);
        int nb = eleveDAO.nbrEleves()+1;
        String matricule = "00"+ nb + getGenreeleve2();
        String id = matricule+'-'+ txtAnneeScolaire.getText();
        Eleve eleve = new Eleve(id,matricule,idClass,idPrt,txtNom.getText(),txtPrenom.getText(),
                txtAdresse.getText(),java.sql.Date.valueOf(txtdateNaissance.getValue()),(String) comboSexe.getValue(),txtAnneeScolaire.getText(),
                estPassant,estNational,(String) comboHandicap.getValue());
        eleveDAO.insertEleve(eleve);
        handleCancel();
    }
    public String getGenreeleve2() {
        if (Objects.equals((String) comboSexe.getValue(), "Garçon"))
            return "G";
        else
            return "F";
    }
    private boolean getBooleanFromRadio(ToggleGroup group, RadioButton radioButton) {
        if (group.getSelectedToggle() == null) {
            return false;
        }
        return group.getSelectedToggle() == radioButton;
    }

    private void filterTable() {
        String txtRecherche = rechercheParent.getText() == null ? "" : rechercheParent.getText().toLowerCase();
        tableParent.setItems(observableParent.filtered(parentT ->
        {
            try {
                return (txtRecherche.isEmpty()
                || parentT.getNompere().toLowerCase().contains(txtRecherche)
                || parentT.getProfessionpere().toLowerCase().contains(txtRecherche)
                || parentT.getNommere().toLowerCase().contains(txtRecherche)
                || parentT.getProfessionmere().toLowerCase().contains(txtRecherche)
                || parentT.getTuteur().toLowerCase().contains(txtRecherche)
                || parentT.getProfessiontuteur().toLowerCase().contains(txtRecherche)
                || parentT.getContact().toLowerCase().contains(txtRecherche)
                || parentT.getEmailparent().toLowerCase().contains(txtRecherche));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        ));
    }
}
