package light.gestion_ecole.Model;

import javafx.beans.property.SimpleBooleanProperty;

import java.util.Date;

public class Eleve {
    private SimpleBooleanProperty selected;
    private String ideleve;
    private String nummat;
    private int idattitude;
    private int idclass;
    private int idparent;
    private String nomeleve;
    private String prenomeleve;
    private String adresseeleve;
    private Date datenaissance;
    private String genreeleve;
    private String anneescolaire;
    private Boolean ispassant;
    private Boolean examennational;

    public Eleve(/*String idEleve,*/ String matricule,int idattitude, int idclass, int idparent,
                 String nom, String prenom, String adresse, Date datenaiss, String sex,
                 String annee_scolaire, Boolean ispassant, Boolean examennational) {
        //this.ideleve = idEleve;
        this.nummat = matricule;
        this.idattitude = idattitude;
        this.idclass = idclass;
        this.idparent = idparent;
        this.nomeleve = nom;
        this.prenomeleve = prenom;
        this.adresseeleve = adresse;
        this.datenaissance = datenaiss;
        this.genreeleve = sex;
        this.anneescolaire = annee_scolaire;
        this.ispassant = ispassant;
        this.examennational = examennational;
    }

    public String getIdeleve() {
        return ideleve;
    }
    public void setIdeleve(String ideleve) {
        this.ideleve = ideleve;
    }
    public String getNummat() {
        return nummat;
    }
    public void setNummat(String nummat) {
        this.nummat = nummat;
    }
    public int getIdattitude() {
        return idattitude;
    }
    public void setIdattitude(int idattitude) {
        this.idattitude = idattitude;
    }
    public int getIdclass() {
        return idclass;
    }
    public void setIdclass(int idclass) {
        this.idclass = idclass;
    }
    public int getIdparent() {
        return idparent;
    }
    public void setIdparent(int idparent) {
        this.idparent = idparent;
    }
    public String getNomeleve() {
        return nomeleve;
    }
    public void setNomeleve(String nomeleve) {
        this.nomeleve = nomeleve;
    }
    public String getPrenomeleve() {
        return prenomeleve;
    }
    public void setPrenomeleve(String prenomeleve) {
        this.prenomeleve = prenomeleve;
    }
    public String getAdresseeleve() {
        return adresseeleve;
    }
    public void setAdresseeleve(String adresseeleve) {
        this.adresseeleve = adresseeleve;
    }
    public Date getDatenaissance() {
        return datenaissance;
    }
    public void setDatenaissance(Date datenaissance) {
        this.datenaissance = datenaissance;
    }
    public String getGenreeleve() {
        return genreeleve;
    }
    public void setGenreeleve(String genreeleve) {
        this.genreeleve = genreeleve;
    }
    public String getAnneescolaire() {
        return anneescolaire;
    }
    public void setAnneescolaire(String anneescolaire) {
        this.anneescolaire = anneescolaire;
    }
    public Boolean getIspassant() {
        return ispassant;
    }
    public void setIspassant(Boolean ispassant) {
        this.ispassant = ispassant;
    }
    public Boolean getExamennational() {
        return examennational;
    }
    public void setExamennational(Boolean examennational) {
        this.examennational = examennational;
    }
}
