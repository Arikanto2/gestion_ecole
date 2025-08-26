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
import light.gestion_ecole.DAO.ClasseDAO;
import light.gestion_ecole.DAO.ProfDAO;
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

    private ProfDAO profDAO = new ProfDAO();

    @FXML public void initialize() throws SQLException {
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        Nom.setCellValueFactory(cell -> cell.getValue().nomProperty());
        Titulaire.setCellValueFactory(cell -> cell.getValue().titulaireProperty());
        Contact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        Adresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        Email.setCellValueFactory(new PropertyValueFactory<>("email"));
        
        loadprofs();

        btnAjouter.setOnAction(e -> {ouvrirform(null);});

        btnModifier.setOnAction(e->{
            Professeur selected = tableView.getSelectionModel().getSelectedItem();
            if (selected!=null) ouvrirform(selected);
            else new Alert(Alert.AlertType.WARNING, "Sélectionnez un prof à modifier !").showAndWait();
        });

        btnSupprimer.setOnAction(e->{
            Professeur selected = tableView.getSelectionModel().getSelectedItem();
            if (selected!=null) {
                Alert conf = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer cette classe ?", ButtonType.YES, ButtonType.NO);
                conf.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.YES) {
                        try{
                            profDAO.supprimerProf(selected.getIdprof());
                            loadprofs();
                        }catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }
        });
    }
    
    private void loadprofs() throws SQLException {
        ObservableList<Professeur> list = FXCollections.observableArrayList(profDAO.getProfesseurs());
    
        FilteredList<Professeur> filteredProfs = new FilteredList<>(list, p -> true);
        
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
           filteredProfs.setPredicate(professeur -> {
               if(newVal == null || newVal.isEmpty() ){
                   return true;
               }
               String lowerCase = newVal.toLowerCase();
               
               if(professeur.getNom().toLowerCase().contains(lowerCase)){
                   return true;
               } else if (professeur.getTitulaire().toLowerCase().contains(lowerCase)) {
                   return true;
               } else if (professeur.getEmail().toLowerCase().contains(lowerCase)) {
                   return true;
               }
               return false;
           }); 
        });

        SortedList<Professeur> sortedList = new SortedList<>(filteredProfs);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedList);
    }

    private void ouvrirform(Professeur professeurmodifier) {
       try {
           FXMLLoader loader = new FXMLLoader(getClass().getResource("/light/gestion_ecole/View/ajout_modif_profs.fxml"));
           Parent root = loader.load();

           TextField txtID = (TextField) root.lookup("#txtIdProf");
           TextField Nom = (TextField) root.lookup("#txtNomProf");
           TextField Contact = (TextField) root.lookup("#txtContactProf");
           TextField Adresse = (TextField) root.lookup("#txtAdresseProf");
           TextField Email = (TextField) root.lookup("#txtEmailProf");
           ComboBox<String> comboTitulaire = (ComboBox<String>) root.lookup("#comboTitulaire");
           ClasseDAO classeDAO = new ClasseDAO();
           comboTitulaire.setItems(classeDAO.getdesignationclasse());

           Button btnEnregistrer = (Button) root.lookup("#btnEnregistrerProf");
           Button btnAnnuler = (Button) root.lookup("#btnAnnulerProf");

           if (professeurmodifier == null){
               txtID.setDisable(true);
           }

           if (professeurmodifier != null){
               txtID.setText(String.valueOf(professeurmodifier.getIdprof()));
               Nom.setText(String.valueOf(professeurmodifier.getNom()));
               Contact.setText(String.valueOf(professeurmodifier.getContact()));
               Adresse.setText(String.valueOf(professeurmodifier.getAdresse()));
               Email.setText(String.valueOf(professeurmodifier.getEmail()));
               comboTitulaire.setValue(professeurmodifier.getTitulaire());

               txtID.setEditable(false);
           }

           Stage stage = new Stage();
           stage.setTitle(professeurmodifier == null ? "Ajouter un professeur" : "Modifier un professeur" );
           Scene scene = new Scene(root);
           stage.setScene(scene);

           btnAnnuler.setOnAction(e -> stage.close());

           btnEnregistrer.setOnAction(e -> {
               try{
                    String nom = Nom.getText();
                    String contact = Contact.getText();
                    String adresse = Adresse.getText();
                    String email = Email.getText();

                    if (nom == null || contact == null || adresse == null || email == null ) {
                        new Alert(Alert.AlertType.WARNING, "Veuillez remplir tous les champs !").showAndWait();
                        return;
                    }


                    if (professeurmodifier == null){
                        int idprofs = 0;
                        Professeur newprof = new Professeur(idprofs,nom,contact,adresse,email);
                        profDAO.ajoutProf(newprof);
                    }else{
                        String idprof = txtID.getText();
                        int idprofs = Integer.parseInt(idprof);
                        professeurmodifier.setIdprof(idprofs);
                        professeurmodifier.setNom(nom);
                        professeurmodifier.setContact(contact);
                        professeurmodifier.setAdresse(adresse);
                        professeurmodifier.setEmail(email);
                        profDAO.modifieProf(professeurmodifier);
                    }
                    loadprofs();
                    stage.close();
               }
               catch (Exception ex) {
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
