package light.gestion_ecole.Controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import light.gestion_ecole.DAO.AbsenceDAO;
import light.gestion_ecole.Model.Absence;
import light.gestion_ecole.Model.Notification;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

import static light.gestion_ecole.Model.Notification.*;

public class AjoutAbsenceController {
    @FXML private TextField nummat;
    @FXML private DatePicker DateAb;
    @FXML private DatePicker DateRe;
    @FXML private TextField motifAbs;
    @FXML private Button btnEnregistrer;
    @FXML private Button btnAnnuler;
    @FXML private HBox btnSupprimer;

    private Absence absence;
    private AbsenceController absenceController;

    public void setAbsenceController(AbsenceController controller) {
        this.absenceController = controller;
    }

    public void setAbsence(Absence absence) {
        this.absence = absence;
        if (absence != null) {
            btnSupprimer.setVisible(true);
            btnSupprimer.setManaged(true);
            nummat.setText(absence.getNumero());
            nummat.setEditable(false);
            motifAbs.setText(absence.getMotif());
            DateAb.setValue(LocalDate.parse(absence.getDateAbsence()));
            if (absence.getDateRetour() != null) {
                if(!absence.getDateRetour().trim().equals("Encore absent(e)")){
                    DateRe.setValue(LocalDate.parse(absence.getDateRetour()));
                }
            }
            btnEnregistrer.setText("Enregistrer");
        }
    }

    @FXML
    void initialize() {
        btnSupprimer.setVisible(false);
        btnSupprimer.setManaged(false);
        btnEnregistrer.setOnAction(e -> {
            try {
                if (absence != null) {
                    modifierAbsence();
                } else {
                    ajouterAbsence();
                }
            } catch (SQLException | IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        btnAnnuler.setOnAction(e -> {
            closeWindow();
        });
        btnSupprimer.setOnMouseClicked(e -> {
            if (absence != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Suppression !");
                alert.setHeaderText(null);
                alert.setContentText("Êtes-vous sûr de vouloir supprimer cette absence ?");


                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        try {
                            AbsenceDAO DAO = new AbsenceDAO();
                            DAO.supprimerAbsence(absence.getIdAbsence());
                            Notification.showSuccess("Suppression effectué!");
                            closeWindow();
                            if (absenceController != null) {
                                String classe = absenceController.comboClasse.getSelectionModel()
                                        .getSelectedItem().toString();
                                absenceController.afficheAbs(classe);
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }

                });
            }

        });

    }

    private void ajouterAbsence() throws SQLException, IOException {
        String numero = nummat.getText().trim();
        LocalDate dateAbsDate = DateAb.getValue();
        LocalDate dateRetDate = DateRe.getValue();
        String motifAbsent = motifAbs.getText();

        if (numero.isEmpty()) {
            Notification.showWarning("Le numéro matricule est obligatoire !");
            return;
        }
        if (dateAbsDate == null) {
            Notification.showWarning("La date d'absence est obligatoire !");
            return;
        }
        if (!AbsenceDAO.isMatExiste(numero)) {
            Notification.showWarning("Le numéro matricule de l'élève n'existe pas ou L'élève est encore absent(e)!");
            return;
        }
        if (dateRetDate != null && dateRetDate.isBefore(dateAbsDate)) {
            Notification.showWarning("La date de retour doit être postérieure à la date d'absence !");
            return;
        }

        Absence ab = new Absence();
        ab.setNumero(numero);
        ab.setDateAbsence(dateAbsDate.toString());
        ab.setDateRetour(dateRetDate != null ? dateRetDate.toString() : null);
        ab.setMotif(motifAbsent == null ? "" : motifAbsent);

        AbsenceDAO DAO = new AbsenceDAO();
        DAO.ajouterAbsence(ab);
        if (absenceController != null) {
            String classe = absenceController.comboClasse.getSelectionModel().getSelectedItem().toString();
            absenceController.afficheAbs(classe);
        }
        Notification.showSuccess("L'absence de l'élève a été ajoutée avec succès.");

    }

    private void modifierAbsence() throws SQLException, IOException {
        String numero = nummat.getText().trim();
        LocalDate dateAbsDate = DateAb.getValue();
        LocalDate dateRetDate = DateRe.getValue();
        String motifAbsent = motifAbs.getText();

        if (numero.isEmpty()) {
            Notification.showWarning( "Le numéro matricule est obligatoire !");
            return;
        }
        if (dateAbsDate == null) {
            Notification.showWarning("La date d'absence est obligatoire.");
            return;
        }

        if (dateRetDate != null && dateRetDate.isBefore(dateAbsDate)) {
            Notification.showWarning("La date de retour doit être postérieure à la date d'absence.");
            return;
        }

        Absence ab = new Absence();
        ab.setIdAbsence(absence.getIdAbsence());
        ab.setNumero(numero);
        ab.setDateAbsence(dateAbsDate.toString());
        ab.setDateRetour(dateRetDate != null ? dateRetDate.toString() : null);
        ab.setMotif(motifAbsent == null ? "" : motifAbsent);

        AbsenceDAO DAO = new AbsenceDAO();
        DAO.modifierAbsence(ab);
        if (absenceController != null) {
            String classe = absenceController.comboClasse.getSelectionModel().getSelectedItem().toString();
            absenceController.afficheAbs(classe);
        }
        Notification.showSuccess("L'absence de l'élève a été modifiée avec succès.");

    }





    private void closeWindow() {
        Stage stage = (Stage) btnEnregistrer.getScene().getWindow();
        stage.close();
    }
}
