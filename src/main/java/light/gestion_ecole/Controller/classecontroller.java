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
import javafx.geometry.Pos;
import javafx.util.Duration;
import light.gestion_ecole.DAO.ClasseDAO;
import light.gestion_ecole.DAO.ProfDAO;
import light.gestion_ecole.DAO.StatDAO;
import light.gestion_ecole.Model.Classe;
import org.controlsfx.control.Notifications;
import java.lang.*;

import java.io.Console;
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
        loadclasse();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        Designation.setCellValueFactory(cell -> cell.getValue().designationProperty());
        nbr_eleves.setCellValueFactory(cell -> cell.getValue().nbrElevesProperty().asObject());
        Ecolage.setCellValueFactory(new PropertyValueFactory<>("prixEcolage"));
        Titulaire.setCellValueFactory(cell -> cell.getValue().titulaireProperty());

        anneeScolaire.setOnAction(event -> {
            try {
                loadclasse();
            } catch (SQLException e) {
                e.printStackTrace();
                showError("Erreur lors du chargement des classes : " + e.getMessage());
            }
        });


        btnAjouter.setOnAction(e -> ouvrirFormulaire(null));

        btnModifier.setOnAction(e -> {
            Classe selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) ouvrirFormulaire(selected);
            else showWarning("Sélectionnez une classe à modifier !");
        });

        btnSupprimer.setOnAction(e -> {
            Classe selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                // Création d'un Dialog de confirmation
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setTitle("Confirmation");
                dialog.setHeaderText("Supprimer cette classe ?");
                dialog.initOwner(btnSupprimer.getScene().getWindow());

                // Boutons Oui / Non
                ButtonType yesButton = new ButtonType("Oui", ButtonBar.ButtonData.OK_DONE);
                ButtonType noButton = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);
                dialog.getDialogPane().getButtonTypes().addAll(yesButton, noButton);

                // Centrer le dialog
                dialog.getDialogPane().setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px;");

                dialog.showAndWait().ifPresent(response -> {
                    if (response == yesButton) {
                        try {
                            System.out.println(selected.getIdClasse());
                            classeDAO.supprimerClasse(selected.getIdClasse());
                            loadclasse();
                            showSuccess("Classe supprimée avec succès !");
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            showError("Erreur lors de la suppression : " + ex.getMessage());
                        }
                    }
                });
            } else {
                showWarning("Sélectionnez une classe à supprimer !");
            }
    });

        tableView.setRowFactory(tv -> {
            TableRow<Classe> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Classe selected = row.getItem();

                    Dialog<ButtonType> dialog = new Dialog<>();
                    dialog.setTitle("Confirmation");
                    dialog.setHeaderText("Qu'est ce que vous voulez voir?");
                    dialog.initOwner(tableView.getScene().getWindow());

                    ButtonType rang = new ButtonType("Rang", ButtonBar.ButtonData.OK_DONE);
                    ButtonType liste = new ButtonType("Liste", ButtonBar.ButtonData.OK_DONE);

                    dialog.getDialogPane().getButtonTypes().addAll(rang, liste);
                    dialog.getDialogPane().setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px;");

                    dialog.showAndWait().ifPresent(response -> {
                        if (response == rang) {
                            ouvrirrangeleves(selected);
                        }
                        else {
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
            ComboBox<String> comboDesignation = (ComboBox<String>) root.lookup("#comboDesignation");
            TextField txtPrix = (TextField) root.lookup("#txtPrix");
            ComboBox<String> comboprof = (ComboBox<String>) root.lookup("#comboprof");

            ProfDAO profDAO = new ProfDAO();
            ObservableList<String> profs = profDAO.getprenom();
            profs.add(0, "");
            comboprof.setPromptText("Sélectionner un prof...");
            comboprof.setItems(profs);

            Button btnEnregistrer = (Button) root.lookup("#btnEnregistrer");
            Button btnAnnuler = (Button) root.lookup("#btnAnnuler");

            comboDesignation.getItems().addAll("12eme","11eme", "10eme", "CE",
                    "CM1", "CM2", "6eme",
                    "5eme", "4eme", "3eme",
                    "Seconde", "Première", "TA", "TC", "TD");

            if (classeAModifier == null){
                txtID.setDisable(true);
            }
            if (classeAModifier != null) {
                txtID.setText(String.valueOf(classeAModifier.getIdClasse()));
                comboDesignation.setValue(classeAModifier.getDesignation());
                txtPrix.setText(String.valueOf(classeAModifier.getPrixEcolage()));
                comboprof.setValue(classeAModifier.getTitulaire());
                txtID.setEditable(false);
            }

            Stage stage = new Stage();
            stage.setTitle(classeAModifier == null ? "Ajouter une Classe" : "Modifier Classe");
            Scene scene = new Scene(root);
            stage.setScene(scene);

            btnAnnuler.setOnAction(e -> stage.close());

            btnEnregistrer.setOnAction(e -> {
                try {
                    String designation = comboDesignation.getValue();
                    String prixStr = txtPrix.getText();
                    String prof = comboprof.getValue();

                    if (designation == null || prixStr.isEmpty()) {
                        showWarning("Veuillez remplir tous les champs !");
                        return;
                    }

                    double prix = Double.parseDouble(prixStr);

                    if (classeAModifier == null) {
                        Classe nouvelleClasse = new Classe(0, designation, prof, prix);
                        classeDAO.ajouterClasse(nouvelleClasse);
                        showSuccess("Classe ajoutée avec succès !");
                    } else {
                        int idclass = Integer.parseInt(txtID.getText());
                        classeAModifier.setIdClasse(idclass);
                        classeAModifier.setDesignation(designation);
                        classeAModifier.setPrixEcolage(prix);
                        classeAModifier.setProf(prof);
                        classeDAO.modifierClasse(classeAModifier);
                        showSuccess("Classe modifiée avec succès !");
                    }

                    loadclasse();
                    stage.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showError("Erreur : " + ex.getMessage());
                }
            });

            stage.showAndWait();

        } catch (Exception ex) {
            ex.printStackTrace();
            showError("Erreur lors de l'ouverture du formulaire : " + ex.getMessage());
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
            stage1.setResizable(false);
            stage1.setMaximized(false);
            stage1.setMaxWidth(395);
            stage1.setMaxHeight(500);

            stage1.showAndWait();

        } catch (Exception ex) {
            ex.printStackTrace();
            showError("Erreur lors de l'ouverture de la liste des élèves : " + ex.getMessage());
        }
    }
    /// ////////////////////////// par rang /////////////////////////////////////////
    public  void ouvrirrangeleves(Classe c_pdp) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/light/gestion_ecole/View/Eleves_Rang.fxml"));
            Parent root = loader.load();

            //Rang_classeController rang = loader.getController();
            //rang.setRang(c_pdp, annee);

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


    private void showSuccess(String message) {
        Notifications.create()
                .title("Succès")
                .text(message)
                .position(Pos.CENTER)
                .hideAfter(Duration.seconds(2.5))
                .owner(tableView.getScene().getWindow())
                .showInformation();
    }

    private void showWarning(String message) {
        Notifications.create()
                .title("Attention")
                .text(message)
                .position(Pos.CENTER)
                .hideAfter(Duration.seconds(2.5))
                .owner(tableView.getScene().getWindow())
                .showWarning();
    }

    private void showError(String message) {
        Notifications.create()
                .title("Erreur")
                .text(message)
                .position(Pos.CENTER)
                .hideAfter(Duration.seconds(2.5))
                .owner(tableView.getScene().getWindow())
                .showError();
    }
}
