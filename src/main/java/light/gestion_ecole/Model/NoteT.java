package light.gestion_ecole.Model;

import java.util.Date;

public class NoteT {
    private String ideleve;
    private String numnat;
    private int idprof;
    private String matiere;
    private Double note;
    private double coefficient;
    private String commentaire = "";
    private String typeevaluation;

    public NoteT(String ideleve, String num, int idprof, String matiere, Double note, double coefficient, String commentaire, String typeevaluation) {
        this.ideleve = ideleve;
        this.numnat = num;
        this.idprof = idprof;
        this.matiere = matiere;
        this.note = note;
        this.coefficient = coefficient;
        this.commentaire = commentaire;
        this.typeevaluation = typeevaluation;
    }

    public NoteT(Eleve e) {
        this.ideleve = e.getIdeleve();
        this.numnat = e.getNummat();
    }

    ///////////pour rang et moyenne ///////////
    private int rang;
    private String nom;
    private String prenom;
    private Double moyenne;

    public NoteT(int rang, String nom, String prenom, Double moyenne) {
        this.rang = rang;
        this.nom = nom;
        this.prenom = prenom;
        this.moyenne = moyenne;
    }
    public int getRang() {
        return rang;
    }

    public void setRang(int rang) {
        this.rang = rang;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Double getMoyenne() {
        return moyenne;
    }

    public void setMoyenne(Double moyenne) {
        this.moyenne = moyenne;
    }

    /// //////////////////////////////////////////////////////

    public String getIdeleve() {
        return ideleve;
    }
    public void setIdeleve(String ideleve) {
        this.ideleve = ideleve;
    }
    public String getNumnat() {
        return numnat;
    }
    public void setNumnat(String numnat) {
        this.numnat = numnat;
    }
    public int getIdprof() {
        return idprof;
    }
    public void setIdprof(int idprof) {
        this.idprof = idprof;
    }
    public String getMatiere() {
        return matiere;
    }
    public void setMatiere(String matiere) {
        this.matiere = matiere;
    }
    public Double getNote() {
        return note;
    }
    public void setNote(Double note) {
        this.note = note;
    }
    public double getCoefficient() {
        return coefficient;
    }
    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }
    public String getCommentaire() { return commentaire;}
    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
    public String getTypeevaluation() {
        return typeevaluation;
    }
    public void setTypeevaluation(String typeevaluation) {
        this.typeevaluation = typeevaluation;
    }


}
