package light.gestion_ecole.Controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.geometry.Pos;
import light.gestion_ecole.DAO.EleveDAO;
import light.gestion_ecole.Model.Classe;
import light.gestion_ecole.Model.Eleve;

// alert
import org.controlsfx.control.Notifications;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.sql.SQLException;
import java.util.List;

public class pdfClasse_Elves {
    @FXML private Label lblClasse;
    @FXML private Label lbleleves;
    @FXML private Label lblprof;
    @FXML private TableView<Eleve> tableView;
    @FXML private TableColumn<Eleve, Integer> numero;
    @FXML private TableColumn<Eleve, String> nom;
    @FXML private TableColumn<Eleve, String> prenom;
    @FXML private Button btnpdf;
    @FXML private Button fermer;

    private Classe classeselected;
    private EleveDAO eleveDAO = new EleveDAO();

    public void setClasse (Classe classe){
        this.classeselected = classe;
        lblClasse.setText(classe.getDesignation());
        lblprof.setText(classe.getTitulaire());
        try{
            chargerEleves(classe.getDesignation());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void initialize() {
        numero.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getNumero()).asObject());
        nom.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNom()));
        prenom.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPrenom()));

        fermer.setOnAction(e -> ((Stage) fermer.getScene().getWindow()).close());
        btnpdf.setOnAction(e->exporterPDF());

    }

    @FXML public void chargerEleves(String designation) throws SQLException {
        List<Eleve> eleves = eleveDAO.getalleleveinclasse(designation);

        ObservableList<Eleve> data = FXCollections.observableList(eleves);
        tableView.setItems(data);
        lbleleves.setText(data.size() + " eleves");
    }

    @FXML
    public void exporterPDF() {

        if (classeselected == null || tableView.getItems().isEmpty()) {
            Notifications.create()
                    .title("Information")
                    .text("Aucune donnée à exporter")
                    .position(Pos.CENTER)
                    .hideAfter(Duration.seconds(3))
                    .darkStyle()
                    .showInformation();
        }
        else {
            try {
                String userDesktop = System.getProperty("user.home") + "/Desktop";
                String filePath = userDesktop + "/Classe_" + classeselected.getDesignation().trim() + ".pdf";

                PdfWriter writer = new PdfWriter(filePath);
                PdfDocument pdfDoc = new PdfDocument(writer);
                Document document = new Document(pdfDoc);

                document.add(new Paragraph("Classe : " + classeselected.getDesignation())
                        .setBold().setFontSize(16));
                document.add(new Paragraph("Titulaire : " + classeselected.getTitulaire()));
                document.add(new Paragraph("Nombre d'élèves : " + tableView.getItems().size()));
                document.add(new Paragraph("\n"));

                Table table = new Table(3);
                table.addHeaderCell("Numéro");
                table.addHeaderCell("Nom");
                table.addHeaderCell("Prénom");

                for (Eleve eleve : tableView.getItems()) {
                    table.addCell(String.valueOf(eleve.getNumero()));
                    table.addCell(eleve.getNom());
                    table.addCell(eleve.getPrenom());
                }

                document.add(table);
                document.close();

                Notifications.create()
                        .title("Succès")
                        .text("PDF généré avec succès sur le Bureau : " + filePath)
                        .position(Pos.CENTER)
                        .hideAfter(Duration.seconds(3))
                        .owner(btnpdf.getScene().getWindow())
                        .showInformation();

            } catch (Exception e) {
                e.printStackTrace();
                Notifications.create()
                        .title("Erreur")
                        .text("Impossible de générer le PDF.")
                        .position(Pos.CENTER)
                        .hideAfter(Duration.seconds(3))
                        .owner(btnpdf.getScene().getWindow())
                        .showError();
            }
        }
    }
}
