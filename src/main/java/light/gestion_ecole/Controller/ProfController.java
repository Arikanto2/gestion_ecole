package light.gestion_ecole.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import light.gestion_ecole.DAO.ClasseDAO;
import light.gestion_ecole.DAO.ProfDAO;
import light.gestion_ecole.Model.Notification;
import light.gestion_ecole.Model.Professeur;

import java.sql.SQLException;

public class ProfController {
    @FXML private TableView<Professeur> tableView;
    @FXML private TableColumn<Professeur, String> Nom;
    @FXML private TableColumn<Professeur, String> Titulaire;
    @FXML private TableColumn<Professeur, String> Contact;
    @FXML private TableColumn<Professeur, String> Adresse;
    @FXML private TableColumn<Professeur, String> Email;

    @FXML private TextField searchField;

    @FXML private Button btnAjouter;
    @FXML private Button btnModifier;
    @FXML private Button btnSupprimer;

    // === Champs overlay (formulaire Prof) ===
    @FXML private AnchorPane formOverlayProf;
    @FXML private Label lblTitreProf;
    @FXML private TextField txtIdProf, txtNomProf, txtContactProf, txtAdresseProf, txtEmailProf;
    @FXML private ComboBox<String> comboTitulaire;
    @FXML private Button btnEnregistrerProf, btnAnnulerProf;

    private ProfDAO profDAO = new ProfDAO();
    private String oldTitulaire;

    @FXML
    public void initialize() throws SQLException {
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        Nom.setCellValueFactory(cell -> cell.getValue().nomProperty());
        Titulaire.setCellValueFactory(cell -> cell.getValue().titulaireProperty());
        Contact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        Adresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        Email.setCellValueFactory(new PropertyValueFactory<>("email"));

        loadprofs();

        btnAjouter.setOnAction(e -> ouvrirform(null));
        btnModifier.setOnAction(e -> {
            Professeur selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) ouvrirform(selected);
            else Notification.showWarning("Sélectionnez un professeur à modifier !");
        });

        btnSupprimer.setOnAction(e -> {
            Professeur selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("Supprimer ce professeur ?");
                alert.initOwner(btnSupprimer.getScene().getWindow());

                ButtonType yesButton = new ButtonType("Oui", ButtonBar.ButtonData.OK_DONE);
                ButtonType noButton = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(yesButton, noButton);

                alert.showAndWait().ifPresent(response -> {
                    if (response == yesButton) {
                        try {
                            profDAO.supprimerProf(selected.getIdprof());
                            loadprofs();
                            Notification.showSuccess("Professeur supprimé avec succès !");
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            Notification.showError("Erreur lors de la suppression : " + ex.getMessage());
                        }
                    }
                });
            } else {
                Notification.showWarning("Sélectionnez un professeur à supprimer !");
            }
        });

        // Bouton Annuler dans l’overlay
        btnAnnulerProf.setOnAction(e -> fermerOverlay());
    }

    private void loadprofs() throws SQLException {
        ObservableList<Professeur> list = FXCollections.observableArrayList(profDAO.getProfesseurs());

        FilteredList<Professeur> filteredProfs = new FilteredList<>(list, p -> true);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredProfs.setPredicate(professeur -> {
                if (newVal == null || newVal.isEmpty()) {
                    return true;
                }
                String lowerCase = newVal.toLowerCase();

                String nom = professeur.getNom() == null ? "" : professeur.getNom().toLowerCase();
                String titulaire = professeur.getTitulaire() == null ? "" : professeur.getTitulaire().toLowerCase();
                String email = professeur.getEmail() == null ? "" : professeur.getEmail().toLowerCase();

                return nom.contains(lowerCase) || titulaire.contains(lowerCase) || email.contains(lowerCase);
            });
        });

        SortedList<Professeur> sortedList = new SortedList<>(filteredProfs);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedList);
    }

    private void ouvrirform(Professeur professeurmodifier) {
        try {
            // Charger les classes dans la combo
            ClasseDAO classeDAO = new ClasseDAO();
            ObservableList<String> classes = classeDAO.getdesignationclasse();
            classes.add(0, "");
            comboTitulaire.setPromptText("Sélectionner une classe...");
            comboTitulaire.setItems(classes);

            if (professeurmodifier == null) {
                lblTitreProf.setText("Ajouter un professeur");
                txtIdProf.clear();
                txtNomProf.clear();
                txtContactProf.clear();
                txtAdresseProf.clear();
                txtEmailProf.clear();
                comboTitulaire.setValue("");
                txtIdProf.setDisable(true);
                oldTitulaire = null;
            } else {
                lblTitreProf.setText("Modifier un professeur");
                txtIdProf.setText(String.valueOf(professeurmodifier.getIdprof()));
                txtNomProf.setText(professeurmodifier.getNom());
                txtContactProf.setText(professeurmodifier.getContact());
                txtAdresseProf.setText(professeurmodifier.getAdresse());
                txtEmailProf.setText(professeurmodifier.getEmail());
                comboTitulaire.setValue(professeurmodifier.getTitulaire());
                txtIdProf.setDisable(true);
                oldTitulaire = professeurmodifier.getTitulaire();
            }

            // Afficher l’overlay
            formOverlayProf.setVisible(true);
            formOverlayProf.setOpacity(1);

            btnEnregistrerProf.setOnAction(e -> {
                try {
                    String nom = txtNomProf.getText();
                    String contact = txtContactProf.getText();
                    String adresse = txtAdresseProf.getText();
                    String email = txtEmailProf.getText();
                    String titulaire = comboTitulaire.getValue();

                    if (nom.isEmpty() || contact.isEmpty() || adresse.isEmpty() || email.isEmpty()) {
                        Notification.showWarning("Veuillez remplir tous les champs.");
                        return;
                    }

                    if (professeurmodifier == null) {
                        Professeur newprof = new Professeur(0, nom, contact, adresse, email);
                        profDAO.ajoutProf(newprof, titulaire);
                    } else {
                        int idprofs = Integer.parseInt(txtIdProf.getText());
                        professeurmodifier.setIdprof(idprofs);
                        professeurmodifier.setNom(nom);
                        professeurmodifier.setContact(contact);
                        professeurmodifier.setAdresse(adresse);
                        professeurmodifier.setEmail(email);
                        profDAO.modifieProf(professeurmodifier, titulaire, oldTitulaire);
                    }
                    loadprofs();
                    fermerOverlay();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    Notification.showError("Erreur : " + ex.getMessage());
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void fermerOverlay() {
        formOverlayProf.setVisible(false);
        formOverlayProf.setOpacity(0);
    }
}
