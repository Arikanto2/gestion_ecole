package light.gestion_ecole.Controller;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import light.gestion_ecole.DAO.*;
import light.gestion_ecole.Model.Classe;
import light.gestion_ecole.Model.Eleve;
import light.gestion_ecole.Model.NoteT;
import light.gestion_ecole.Model.Notification;

import java.sql.SQLException;

public class classecontroller {


    @FXML private TableView<Classe> tableView;
    @FXML private TableColumn<Classe, String> Designation;
    @FXML private TableColumn<Classe, Integer> nbr_eleves;
    @FXML private TableColumn<Classe, Double> Ecolage;
    @FXML private TableColumn<Classe, String> Titulaire;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> anneeScolaire;
    @FXML private Button btnAjouter, btnModifier, btnSupprimer;
    @FXML private AnchorPane formOverlayClasse;
    @FXML private Label lblTitreClasse;
    @FXML private TextField txtID, textFielDesignation, txtPrix;
    @FXML private ComboBox<String> comboprof;
    @FXML private Button btnEnregistrer, btnAnnuler;
    @FXML private Label txtFID;


    @FXML public AnchorPane overlayListeEleves;
    @FXML private TableView<Eleve> tableListeEleves;
    @FXML private Button btnFermerListeEleves;


    @FXML private AnchorPane overlayRangEleves;
    @FXML private TableView<NoteT> tableRangEleves;
    @FXML private Button btnFermerRangEleves;

    private String annee;
    private ClasseDAO classeDAO = new ClasseDAO();
    private Classe classeSelectionnee = null;

    @FXML
    public void initialize() throws SQLException {
        txtFID.setVisible(false);
        txtFID.setManaged(false);
        txtID.setManaged(false);
        txtID.setVisible(false);
        anneeScolaire.getItems().addAll(StatDAO.getAnnescolaire());
        anneeScolaire.getSelectionModel().select(0);
        if (anneeScolaire.getValue() != null) {
            StatistiqueParClasseController.anneescolaire = anneeScolaire.getItems().get(0).toString();
            loadclasse();
        }
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        Designation.setCellValueFactory(cell -> cell.getValue().designationProperty());
        nbr_eleves.setCellValueFactory(cell -> cell.getValue().nbrElevesProperty().asObject());
        Ecolage.setCellValueFactory(new PropertyValueFactory<>("prixEcolage"));
        Titulaire.setCellValueFactory(cell -> cell.getValue().titulaireProperty());

        anneeScolaire.setOnAction(event -> {
            try {
                loadclasse();
                StatistiqueParClasseController.anneescolaire = anneeScolaire.getSelectionModel().getSelectedItem().toString();
            } catch (SQLException e) {
                e.printStackTrace();
                Notification.showError("Erreur lors du chargement des classes : " + e.getMessage());
            }
        });


        btnAjouter.setOnAction(e -> ouvrirFormulaire(null));

        btnModifier.setOnAction(e -> {
            Classe selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) ouvrirFormulaire(selected);
            else Notification.showWarning("Sélectionnez une classe à modifier !");
        });

        btnSupprimer.setOnAction(e -> {
            Classe selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("Supprimer cette classe ?");
                alert.initOwner(btnSupprimer.getScene().getWindow());

                ButtonType yesButton = new ButtonType("Oui", ButtonBar.ButtonData.OK_DONE);
                ButtonType noButton = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);

                alert.getButtonTypes().setAll(yesButton, noButton);

                alert.showAndWait().ifPresent(response -> {
                    if (response == yesButton) {
                        try {
                            classeDAO.supprimerClasse(selected.getIdClasse());
                            loadclasse();
                            Notification.showSuccess("Classe supprimée avec succès !");
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            Notification.showError("Erreur lors de la suppression : " + ex.getMessage());
                        }
                    }
                });
            } else {
                Notification.showWarning("Sélectionnez une classe à supprimer !");
            }
        });


        tableView.setRowFactory(tv -> {
            TableRow<Classe> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Classe selected = row.getItem();

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText("Qu'est-ce que vous voulez voir ?");
                    alert.initOwner(tableView.getScene().getWindow());

                    ButtonType rang = new ButtonType("Rang");
                    ButtonType liste = new ButtonType("Liste");
                    ButtonType annuler = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);

                    alert.getButtonTypes().setAll(rang, liste, annuler);

                    alert.showAndWait().ifPresent(response -> {
                        if (response == rang) {
                            ouvrirrangeleves(selected);   // Ancien comportement conservé
                        } else if (response == liste) {
                            ouvrirlisteEleves(selected);  // Ancien comportement conservé
                        }
                    });
                }
            });
            return row;
        });
        tableView.getColumns().forEach(col -> {
            col.setReorderable(false);
            col.setResizable(false);
        });
        // Charger années scolaires
        anneeScolaire.getItems().addAll(StatDAO.getAnnescolaire());
        anneeScolaire.getSelectionModel().select(0);
        annee = anneeScolaire.getSelectionModel().getSelectedItem();

        // Colonnes TableView
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        Designation.setCellValueFactory(cell -> cell.getValue().designationProperty());
        nbr_eleves.setCellValueFactory(cell -> cell.getValue().nbrElevesProperty().asObject());
        Ecolage.setCellValueFactory(new PropertyValueFactory<>("prixEcolage"));
        Titulaire.setCellValueFactory(cell -> cell.getValue().titulaireProperty());

        // Charger classes
        loadclasse();

        // Changement année scolaire
        anneeScolaire.setOnAction(event -> {
            try {
                annee = anneeScolaire.getSelectionModel().getSelectedItem();
                loadclasse();
            } catch (SQLException e) {
                e.printStackTrace();
                Notification.showError("Erreur lors du chargement des classes : " + e.getMessage());
            }
        });

        btnAjouter.setOnAction(e -> ouvrirFormulaire(null));
        btnModifier.setOnAction(e -> {
            Classe selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) ouvrirFormulaire(selected);
            else Notification.showWarning("Sélectionnez une classe à modifier !");
        });
        btnSupprimer.setOnAction(e -> supprimerClasse());
        btnAnnuler.setOnAction(e -> fermerOverlay(formOverlayClasse));
        btnFermerListeEleves.setOnAction(e -> fermerOverlay(overlayListeEleves));
        btnFermerRangEleves.setOnAction(e -> fermerOverlay(overlayRangEleves));
    }

    private void loadclasse() throws SQLException {
        ObservableList<Classe> liste = FXCollections.observableArrayList(classeDAO.getAllClasses(annee));
        FilteredList<Classe> filteredClasse = new FilteredList<>(liste, e -> true);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredClasse.setPredicate(classe -> {
                if (newVal == null || newVal.isEmpty()) return true;
                String lower = newVal.toLowerCase();
                return (classe.getDesignation() != null && classe.getDesignation().toLowerCase().contains(lower))
                        || (classe.getTitulaire() != null && classe.getTitulaire().toLowerCase().contains(lower))
                        || String.valueOf(classe.getNbrEleves()).contains(lower)
                        || (classe.getPrixEcolage() != null && String.valueOf(classe.getPrixEcolage()).contains(lower));
            });
        });

        SortedList<Classe> sortedClasse = new SortedList<>(filteredClasse);
        sortedClasse.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedClasse);
    }

    private void ouvrirFormulaire(Classe classeAModifier) {
        try {
            formOverlayClasse.setVisible(true);
            formOverlayClasse.setOpacity(1);

            // Charger profs
            ProfDAO profDAO = new ProfDAO();
            ObservableList<String> profs = profDAO.getprenom();
            profs.add(0, "");
            comboprof.setItems(profs);

            // Remplir champs
            if (classeAModifier == null) {
                lblTitreClasse.setText("Ajouter une classe");
                txtID.clear();
                txtID.setDisable(true);
                textFielDesignation.clear();
                txtPrix.clear();
                comboprof.setValue(null);
            } else {
                lblTitreClasse.setText("Modifier une classe");
                txtID.setText(String.valueOf(classeAModifier.getIdClasse()));
                txtID.setDisable(true);
                textFielDesignation.setText(classeAModifier.getDesignation());
                txtPrix.setText(String.valueOf(classeAModifier.getPrixEcolage()));
                comboprof.setValue(classeAModifier.getTitulaire());
            }

            classeSelectionnee = classeAModifier;

            btnEnregistrer.setOnAction(e -> enregistrerClasse());
        } catch (Exception ex) {
            ex.printStackTrace();
            Notification.showError("Erreur : " + ex.getMessage());
        }
    }

    private void enregistrerClasse() {
        try {
            String designation = textFielDesignation.getText();
            String prixStr = txtPrix.getText();
            String prof = comboprof.getValue();

            if (designation.isEmpty() || prixStr.isEmpty()) {
                Notification.showWarning("Veuillez remplir tous les champs !");
                return;
            }

            double prix = Double.parseDouble(prixStr);

            if (classeSelectionnee == null) {
                Classe nouvelleClasse = new Classe(0, designation, prof, prix);
                classeDAO.ajouterClasse(nouvelleClasse);
                Notification.showSuccess("Classe ajoutée avec succès !");
            } else {
                classeSelectionnee.setDesignation(designation);
                classeSelectionnee.setPrixEcolage(prix);
                classeSelectionnee.setProf(prof);
                classeDAO.modifierClasse(classeSelectionnee);
                Notification.showSuccess("Classe modifiée avec succès !");
            }

            loadclasse();
            fermerOverlay(formOverlayClasse);
        } catch (Exception ex) {
            ex.printStackTrace();
            Notification.showError("Erreur : " + ex.getMessage());
        }
    }

    private void supprimerClasse() {
        Classe selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Notification.showWarning("Sélectionnez une classe à supprimer !");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer cette classe ?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    classeDAO.supprimerClasse(selected.getIdClasse());
                    loadclasse();
                    Notification.showSuccess("Classe supprimée !");
                } catch (SQLException e) {
                    e.printStackTrace();
                    Notification.showError("Erreur : " + e.getMessage());
                }
            }
        });
    }

    public static void fermerOverlay(AnchorPane overlay) {
        if (overlay != null){
            overlay.setVisible(false);
            overlay.setOpacity(0);
        }
    }

    private void ouvrirlisteEleves(Classe c_pdp) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/light/gestion_ecole/View/Eleves_Pdf.fxml"));
            VBox root = loader.load();

            pdfClasse_Elves pdfController = loader.getController();
            pdfController.setClasse(c_pdp, annee);
            pdfController.setOverlayParent(overlayListeEleves);

            overlayListeEleves.getChildren().clear();
            overlayListeEleves.getChildren().add(root);
            AnchorPane.setTopAnchor(root, 50.0);
            AnchorPane.setBottomAnchor(root, 50.0);
            AnchorPane.setLeftAnchor(root, 150.0);
            AnchorPane.setRightAnchor(root, 150.0);

            overlayListeEleves.setVisible(true);
            overlayListeEleves.setOpacity(1);

        } catch (Exception ex) {
            ex.printStackTrace();
            Notification.showError("Erreur lors de l'ouverture de la liste des élèves : " + ex.getMessage());
        }
    }


    private void ouvrirrangeleves(Classe c_pdp) {
    try {
        // Charger le FXML du rang des élèves
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/light/gestion_ecole/View/Eleves_Rang.fxml"));
        VBox root = loader.load();

        // Récupérer le controller et initialiser avec la classe et l'année
        Rang_classeController rangController = loader.getController();
        rangController.setRang(c_pdp, annee);
        rangController.setOverlayParent(overlayRangEleves);

        // Insérer le contenu dans l'AnchorPane overlayRangEleve
        overlayRangEleves.getChildren().clear();
        overlayRangEleves.getChildren().add(root);
        AnchorPane.setTopAnchor(root, 50.0);
        AnchorPane.setBottomAnchor(root, 50.0);
        AnchorPane.setLeftAnchor(root, 50.0);
        AnchorPane.setRightAnchor(root, 50.0);

        overlayRangEleves.setVisible(true);
        overlayRangEleves.setOpacity(1);

    } catch (Exception ex) {
        ex.printStackTrace();
        Notification.showError("Erreur lors de l'ouverture du rang des élèves : " + ex.getMessage());
    }
}

}
