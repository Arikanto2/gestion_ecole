package light.gestion_ecole.Model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Classe {
    private int idClasse;
    private SimpleStringProperty designation;
    private SimpleIntegerProperty nbrEleves;
    private SimpleStringProperty titulaire;
    private String prof;
    private Double prixEcolage;

    public  Classe(int idClasse, String designation, int nbrEleves, String titulaire, Double prixEcolage) {
        this.idClasse = idClasse;
        this.designation = new SimpleStringProperty(designation);
        this.nbrEleves = new SimpleIntegerProperty(nbrEleves);
        this.titulaire = new SimpleStringProperty(titulaire);
        this.prixEcolage = prixEcolage;
    }

    // ajout et modif
    public  Classe(int idClasse, String designation, String prof, Double prixEcolage) {
        this.idClasse = idClasse;
        this.designation = new SimpleStringProperty(designation);
        this.prof = prof;
        this.prixEcolage = prixEcolage;
    }


    public String getDesignation() {
        return designation.get();
    }

    public SimpleStringProperty designationProperty() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation.set(designation);
    }

    public int getNbrEleves() {
        return nbrEleves.get();
    }

    public SimpleIntegerProperty nbrElevesProperty() {
        return nbrEleves;
    }

    public void setNbrEleves(int nbrEleves) {
        this.nbrEleves.set(nbrEleves);
    }

    public String getTitulaire() {
        return titulaire.get();
    }

    public SimpleStringProperty titulaireProperty() {
        return titulaire;
    }

    public void setTitulaire(String titulaire) {
        this.titulaire.set(titulaire);
    }

    public int getIdClasse() {
        return idClasse;
    }

    public void setIdClasse(int idClasse) {
        this.idClasse = idClasse;
    }


    public Double getPrixEcolage() {
        return prixEcolage;
    }

    public void setPrixEcolage(Double prixEcolage) {
        this.prixEcolage = prixEcolage;
    }


    public String getProf() {
        return prof;
    }

    public void setProf(String prof) {
        this.prof = prof;
    }
}
