package light.gestion_ecole.Controller;

import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.UnitValue;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import light.gestion_ecole.DAO.EleveDAO;
import light.gestion_ecole.Main;
import light.gestion_ecole.Model.Classe;
import light.gestion_ecole.Model.Eleve;
import light.gestion_ecole.Model.Notification;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
    @FXML private Button afficherStatClasse;
    private String anneescolaire;

    private Classe classeselected;
    private EleveDAO eleveDAO = new EleveDAO();

    public void setClasse (Classe classe, String st){
        this.classeselected = classe;
        lblClasse.setText(classe.getDesignation());
        lblprof.setText(classe.getTitulaire());
        this.anneescolaire = st;
        try{
            chargerEleves(classe,anneescolaire);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void initialize() {
        nom.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNom()));
        prenom.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPrenom()));
        numero.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getNumero()).asObject());

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        numero.prefWidthProperty().bind(tableView.widthProperty().multiply(0.33));
        nom.prefWidthProperty().bind(tableView.widthProperty().multiply(0.33));
        prenom.prefWidthProperty().bind(tableView.widthProperty().multiply(0.33));

        fermer.setOnAction(e -> ((Stage) fermer.getScene().getWindow()).close());
        btnpdf.setOnAction(e -> exporterPDF());
        afficherStatClasse.setOnMouseClicked(e -> {
            StatistiqueParClasseController.classe = classeselected;
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/light/gestion_ecole/View/StatistiqueParClasse-View.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 320, 240);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            Stage stage = new Stage();
            Main.setStageIcon(stage);
            stage.setTitle("Statistique (" + classeselected.getDesignation() + ")");
            stage.setScene(scene);
            stage.setMinWidth(750);
            stage.setMinHeight(460);
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.show();
        });
    }

    @FXML public void chargerEleves(Classe classe, String anneescolaire) throws SQLException {
        List<Eleve> eleves = eleveDAO.getElevesFiltre(classe,anneescolaire);

        ObservableList<Eleve> data = FXCollections.observableList(eleves);
        tableView.setItems(data);
        lbleleves.setText(data.size() + " éleves");
    }

    @FXML
    public void exporterPDF() {

        if (classeselected == null || tableView.getItems().isEmpty()) {
            Notification.showWarning("Aucune donnée à exporter");
        }
        else {
            try {

                String userDesktop = System.getProperty("user.home") + "/Desktop/Listes_eleves";
                File dossier = new File(userDesktop);

                if (!dossier.exists()) {
                    dossier.mkdirs();
                }

                String filePath = userDesktop + "/Classe_" + classeselected.getDesignation().trim() + ".pdf";



                PdfWriter writer = new PdfWriter(filePath);
                PdfDocument pdfDoc = new PdfDocument(writer);
                Document document = new Document(pdfDoc);
                document.setMargins(20, 20, 20, 20);

                try {
                    String logoPath = getClass().getResource("/light/gestion_ecole/Photo/logo.png").toExternalForm();
                    ImageData imageData = ImageDataFactory.create(logoPath);
                    Image logo = new Image(imageData);
                    logo.setHorizontalAlignment(HorizontalAlignment.CENTER);
                    document.add(logo);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                document.add(new Paragraph("Classe : " + classeselected.getDesignation()).setBold().setFontSize(14));
                document.add(new Paragraph("Titulaire : " + classeselected.getTitulaire()));
                document.add(new Paragraph("Effectifs : " + tableView.getItems().size()));
                document.add(new Paragraph("\n"));

                Table table = new Table(UnitValue.createPercentArray(new float[]{1, 4, 4})).useAllAvailableWidth();
                table.addHeaderCell("Numéro");
                table.addHeaderCell("Nom");
                table.addHeaderCell("Prénom");

                for (int i = 0; i < tableView.getItems().size(); i++) {
                    Eleve eleve = tableView.getItems().get(i);
                    String nomEleve = eleve.getNom() != null ? eleve.getNom() : "-";
                    String prenomEleve = eleve.getPrenom() != null ? eleve.getPrenom() : "-";
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(i + 1))));
                    table.addCell(new Cell().add(new Paragraph(nomEleve)));
                    table.addCell(new Cell().add(new Paragraph(prenomEleve)));
                }

                document.add(table);
                document.close();

                Notification.showSuccess("PDF généré avec succès sur le Bureau : " + filePath);

            } catch (Exception e) {
                e.printStackTrace();
                Notification.showError("Impossible de générer le PDF");
            }
        }
    }
}
