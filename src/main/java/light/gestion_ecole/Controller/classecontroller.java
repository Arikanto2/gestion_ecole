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
import light.gestion_ecole.Model.Classe;

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


    private ClasseDAO classeDAO = new ClasseDAO();

    @FXML
    public void initialize() throws SQLException {
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        Designation.setCellValueFactory(cell -> cell.getValue().designationProperty());
        nbr_eleves.setCellValueFactory(cell -> cell.getValue().nbrElevesProperty().asObject());
        Ecolage.setCellValueFactory(new PropertyValueFactory<>("prixEcolage"));
        Titulaire.setCellValueFactory(cell -> cell.getValue().titulaireProperty());

        loadclasse();

        btnAjouter.setOnAction(e -> ouvrirFormulaire(null));

        btnModifier.setOnAction(e -> {
            Classe selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) ouvrirFormulaire(selected);
            else new Alert(Alert.AlertType.WARNING, "Sélectionnez une classe à modifier !").showAndWait();
        });

        btnSupprimer.setOnAction(e -> {
            Classe selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Alert conf = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer cette classe ?", ButtonType.YES, ButtonType.NO);
                conf.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.YES) {
                        try {
                            classeDAO.supprimerClasse(selected.getIdClasse());
                            loadclasse();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            } else {
                new Alert(Alert.AlertType.WARNING, "Sélectionnez une classe à supprimer !").showAndWait();
            }
        });
    }

    private void loadclasse() throws SQLException {
        ObservableList<Classe> liste = FXCollections.observableArrayList(classeDAO.getAllClasses());

        FilteredList<Classe> filteredClasse = new FilteredList<>(liste, e -> true);

        //ecoute de searchfield

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredClasse.setPredicate(classe -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (classe.getDesignation() != null && classe.getDesignation().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (classe.getTitulaire() != null && classe.getTitulaire().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(classe.getNbrEleves()).contains(lowerCaseFilter)) {
                    return true;
                } else if (classe.getPrixEcolage() != null && String.valueOf(classe.getPrixEcolage()).contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
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

            comboDesignation.getItems().addAll("11eme", "10eme", "CE",
                    "CM1", "CM2", "6eme",
                    "5eme", "4eme", "3eme",
                    "Seconde", "Première", "Terminale");

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
                        new Alert(Alert.AlertType.WARNING, "Veuillez remplir tous les champs !").showAndWait();
                        return;
                    }


                    double prix = Double.parseDouble(prixStr);

                    if (classeAModifier == null) {
                        int idclass = 0;
                        Classe nouvelleClasse = new Classe(idclass,designation, prof,prix);
                        classeDAO.ajouterClasse(nouvelleClasse);
                    } else {
                        String idclasse = txtID.getText();
                        int idclass = Integer.parseInt(idclasse);
                        classeAModifier.setIdClasse(idclass);
                        classeAModifier.setDesignation(designation);
                        classeAModifier.setPrixEcolage(prix);
                        classeAModifier.setProf(prof);
                        classeDAO.modifierClasse(classeAModifier);
                    }

                    loadclasse();
                    stage.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Erreur : " + ex.getMessage()).showAndWait();
                }
            });

            stage.showAndWait();

        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
