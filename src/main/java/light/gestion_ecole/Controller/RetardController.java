package light.gestion_ecole.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import light.gestion_ecole.DAO.RetardDAO;
import light.gestion_ecole.Model.Retard;

import java.sql.SQLException;
import java.util.List;

public class RetardController {

    @FXML private TableView<Retard> ret;
    @FXML private TextField searchField1;
    @FXML private TableColumn<Retard, String> idEleve;
    @FXML private TableColumn<Retard, String> nomEleve;
    @FXML private TableColumn<Retard, String> nbRetard;
    @FXML private TableColumn<Retard, HBox> Action;
    @FXML
    void initialize() throws SQLException {
        List<Retard> retards = RetardDAO.getAbsences();
        idEleve.setCellValueFactory(new PropertyValueFactory<>("numero"));
        nomEleve.setCellValueFactory(new PropertyValueFactory<>("nom_Prenom"));
        nbRetard.setCellValueFactory(new PropertyValueFactory<>("nbRetards"));
        Action.setCellValueFactory(new PropertyValueFactory<>("hBox"));

        ObservableList<Retard> data = FXCollections.observableArrayList(retards);
        FilteredList<Retard> filteredData = new FilteredList<>(data, p -> true);
        ret.setItems(filteredData);

        ret.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        ret.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double tableWidth = newWidth.doubleValue();
            idEleve.setPrefWidth(tableWidth * 0.20);
            nbRetard.setPrefWidth(tableWidth * 0.15);
            Action.setPrefWidth(tableWidth * 0.20);
            nomEleve.setPrefWidth(tableWidth - (idEleve.getWidth() + nbRetard.getWidth() + Action.getWidth()));
        });

        ret.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Retard retard, boolean empty) {
                super.updateItem(retard, empty);
                if (retard != null) {
                    setStyle(retard.getNbRetards() >= 6 ? "-fx-background-color: #fa6d6d;" : "");
                    retard.getAjout().setOnAction(e -> {
                        retard.setNbRetards(retard.getNbRetards() + 1);
                        try { new RetardDAO().ajout(retard.getNumero(), 1); } catch (SQLException ex) { ex.printStackTrace(); }
                        ret.refresh();
                    });
                    retard.getDiminuer().setOnAction(e -> {
                        if (retard.getNbRetards() > 0) {
                            retard.setNbRetards(retard.getNbRetards() - 1);
                            try { new RetardDAO().ajout(retard.getNumero(), -1); } catch (SQLException ex) { ex.printStackTrace(); }
                            ret.refresh();
                        }
                    });
                }
            }
        });

        searchField1.textProperty().addListener((obs, oldText, newText) -> {
            filteredData.setPredicate(retard -> {
                if (newText == null || newText.isEmpty()) return true;
                String lower = newText.toLowerCase();
                return retard.getNom_Prenom().toLowerCase().contains(lower) ||
                        retard.getNumero().toLowerCase().contains(lower);
            });
        });
    }


}
