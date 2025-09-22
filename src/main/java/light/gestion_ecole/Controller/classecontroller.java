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
import javafx.stage.Stage;
import light.gestion_ecole.DAO.ClasseDAO;
import light.gestion_ecole.DAO.ProfDAO;
import light.gestion_ecole.DAO.StatDAO;
import light.gestion_ecole.Main;
import light.gestion_ecole.Model.Classe;
import light.gestion_ecole.Model.Notification;

import java.lang.*;

import java.sql.SQLException;

public class classecontroller {
    @FXML
    private TableView<Classe> tableView;
    @FXML
    private TableColumn<Classe, String> Designation;
    @FXML
    private TableColumn<Classe, Integer> nbr_eleves;
    @FXML
    private TableColumn<Classe, Double> Ecolage;
    @FXML
    private TableColumn<Classe, String> Titulaire;
    @FXML
    private TextField searchField;
    @FXML
    private Button btnAjouter;
    @FXML
    private Button btnModifier;
    @FXML
    private Button btnSupprimer;
    @FXML ComboBox anneeScolaire;

    private String annee;
    private ClasseDAO classeDAO = new ClasseDAO();

    @FXML
    public void initialize() throws SQLException {
        anneeScolaire.getItems().addAll(StatDAO.getAnnescolaire());
        anneeScolaire.getSelectionModel().select(0);
        StatistiqueParClasseController.anneescolaire = anneeScolaire.getItems().get(0).toString();
        loadclasse();
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
                            ouvrirrangeleves(selected);
                        } else if (response == liste) {
                            ouvrirlisteEleves(selected);
                        }
                    });
                }

            });
            return row;
        });
        annee = anneeScolaire.valueProperty().get().toString();
    }

    private void loadclasse() throws SQLException {
        String annee = (String) anneeScolaire.getSelectionModel().getSelectedItem();
        if (annee == null) return;

        ObservableList<Classe> liste = FXCollections.observableArrayList(
                classeDAO.getAllClasses(annee)
        );

        FilteredList<Classe> filteredClasse = new FilteredList<>(liste, e -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredClasse.setPredicate(classe -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lowerCaseFilter = newValue.toLowerCase();

                if (classe.getDesignation() != null && classe.getDesignation().toLowerCase().contains(lowerCaseFilter))
                    return true;
                else if (classe.getTitulaire() != null && classe.getTitulaire().toLowerCase().contains(lowerCaseFilter))
                    return true;
                else if (String.valueOf(classe.getNbrEleves()).contains(lowerCaseFilter))
                    return true;
                else return classe.getPrixEcolage() != null &&
                            String.valueOf(classe.getPrixEcolage()).contains(lowerCaseFilter);
            });
        });

        SortedList<Classe> sortedClasse = new SortedList<>(filteredClasse);
        sortedClasse.comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setItems(sortedClasse);
    }


    private void ouvrirFormulaire(Classe classeAModifier) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/light/gestion_ecole/View/ajout_modif_classe.fxml"));
            Parent root = loader.load();

            TextField txtID = (TextField) root.lookup("#txtID");
            TextField designation =  (TextField) root.lookup("#textFielDesignation");

            TextField txtPrix = (TextField) root.lookup("#txtPrix");
            ComboBox<String> comboprof = (ComboBox<String>) root.lookup("#comboprof");

            ProfDAO profDAO = new ProfDAO();
            ObservableList<String> profs = profDAO.getprenom();
            profs.add(0, "");
            comboprof.setPromptText("Sélectionner un prof...");
            comboprof.setItems(profs);

            Button btnEnregistrer = (Button) root.lookup("#btnEnregistrer");
            Button btnAnnuler = (Button) root.lookup("#btnAnnuler");


            if (classeAModifier == null){
                txtID.setDisable(true);
            }
            if (classeAModifier != null) {
                txtID.setText(String.valueOf(classeAModifier.getIdClasse()));
                txtPrix.setText(String.valueOf(classeAModifier.getPrixEcolage()));
                comboprof.setValue(classeAModifier.getTitulaire());
                txtID.setEditable(false);
                designation.setText(classeAModifier.getDesignation().trim());
            }

            Stage stage = new Stage();
            Main.setStageIcon(stage);
            stage.setTitle(classeAModifier == null ? "Ajouter une Classe" : "Modifier Classe");
            Scene scene = new Scene(root);
            stage.setScene(scene);

            btnAnnuler.setOnAction(e -> stage.close());

            btnEnregistrer.setOnAction(e -> {
                try {

                    String prixStr = txtPrix.getText();
                    String prof = comboprof.getValue();
                    String txtdesignation = designation.getText();


                    if (designation == null || prixStr.isEmpty()) {

                        Notification.showWarning("Veuillez remplir tous les champs !");
                        return;
                    }

                    double prix = Double.parseDouble(prixStr);

                    if (classeAModifier == null) {
                        Classe nouvelleClasse = new Classe(0, txtdesignation, prof, prix);
                        classeDAO.ajouterClasse(nouvelleClasse);
                        Notification.showSuccess("Classe ajoutée avec succès !");
                    } else {
                        int idclass = Integer.parseInt(txtID.getText());
                        classeAModifier.setIdClasse(idclass);
                        classeAModifier.setDesignation(txtdesignation);
                        classeAModifier.setPrixEcolage(prix);
                        classeAModifier.setProf(prof);
                        classeDAO.modifierClasse(classeAModifier);
                        Notification.showSuccess("Classe modifiée avec succès !");
                    }

                    loadclasse();
                    stage.close();
                } catch (Exception ex) {
                    ex.printStackTrace();

                    Notification.showError("Veuillez entrer des valeur valide.");

                }
            });

            stage.showAndWait();

        } catch (Exception ex) {
            ex.printStackTrace();
            Notification.showError("Erreur lors de l'ouverture du formulaire : " + ex.getMessage());
        }
    }
    ////////////////////// listage des eleves par classe ///////////////////////////////
    private void ouvrirlisteEleves(Classe c_pdp) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/light/gestion_ecole/View/Eleves_Pdf.fxml"));
            Parent root = loader.load();

            pdfClasse_Elves pdf = loader.getController();
            pdf.setClasse(c_pdp, anneeScolaire.getSelectionModel().getSelectedItem().toString());

            Stage stage1 = new Stage();
            stage1.setTitle("Eleves de " + c_pdp.getDesignation());

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/light/gestion_ecole/Style/pdf.css").toExternalForm());
            stage1.setScene(scene);
            Main.setStageIcon(stage1);
            stage1.setResizable(false);
            stage1.setMaximized(false);
            stage1.setMaxWidth(395);
            stage1.setMaxHeight(500);

            stage1.showAndWait();

        } catch (Exception ex) {
            ex.printStackTrace();
            Notification.showError("Erreur lors de l'ouverture de la liste des élèves : " + ex.getMessage());
        }
    }
    /// ////////////////////////// par rang /////////////////////////////////////////
    public  void ouvrirrangeleves(Classe c_pdp) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/light/gestion_ecole/View/Eleves_Rang.fxml"));
            Parent root = loader.load();

            Rang_classeController rang = loader.getController();
            rang.setRang(c_pdp, annee);

            Stage stage1 = new Stage();
            stage1.setTitle("Eleves de " + c_pdp.getDesignation());

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/light/gestion_ecole/Style/pdf.css").toExternalForm());
            stage1.setScene(scene);
            stage1.setResizable(false);
            stage1.setMaximized(false);
            stage1.setMaxWidth(395);
            stage1.setMaxHeight(500);

            stage1.showAndWait();

        }catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
