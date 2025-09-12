package light.gestion_ecole.Controller;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import light.gestion_ecole.DAO.NoteDAOT;
import light.gestion_ecole.Model.Classe;
import light.gestion_ecole.Model.Eleve;
import light.gestion_ecole.Model.NoteT;

import java.sql.SQLException;
import java.util.List;

public class Rang_classeController {
    @FXML
    private Label txtClasse;
    @FXML private Label txtprof;
    @FXML private Label txteleves;
    @FXML private TableView<NoteT> tableView;
    @FXML private TableColumn<NoteT, Integer> rang;
    @FXML private TableColumn<NoteT, String> rnom;
    @FXML private TableColumn<NoteT, String> rprenom;
    @FXML private TableColumn<NoteT, Double> moyenne;
    @FXML
    ComboBox evaluation;
    private NoteDAOT noteDAO = new NoteDAOT() ;

    private Classe classeselected;

    public void setRang(Classe classe, String annee) {
        txtClasse.setText(classe.getDesignation());
        txtprof.setText(classe.getTitulaire());
        this.classeselected = classe;
        try{
            evaluation.getItems().addAll(noteDAO.getevaluation());
            evaluation.getSelectionModel().select(0);
            String eval = evaluation.valueProperty().get().toString();
            rangeleves(eval,classe.getIdClasse(),annee);

            evaluation.valueProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    try {
                        rangeleves(newVal.toString(), classe.getIdClasse(), annee);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }

    }
    public void initialize() {
        rang.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getRang()).asObject());
        rnom.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNom()));
        rprenom.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPrenom()));
        moyenne.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().getMoyenne()).asObject());
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        rang.prefWidthProperty().bind(tableView.widthProperty().multiply(0.33));
        rnom.prefWidthProperty().bind(tableView.widthProperty().multiply(0.33));
        rprenom.prefWidthProperty().bind(tableView.widthProperty().multiply(0.33));
    }

    @FXML public void rangeleves(String evalu,int idclass, String annee) throws SQLException {
        List<NoteT> eleves = noteDAO.rang_classe(evalu, idclass, annee);

        ObservableList<NoteT> data =  FXCollections.observableList(eleves);
        tableView.setItems(data);
        txteleves.setText(data.size() + " éleves");
    }


}
