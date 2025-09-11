package light.gestion_ecole.Model;

public class Absence {
    private String numero;
    private String nom;
    private String prenom;
    private String dateAbsence;
    private String dateRetour;
    private String motif;
    private int idAbsence;

    public Absence() {}

    public String getNomComplet() {
        return nom + " " + prenom;
    }

    public String getNumero() {
        return numero;
    }
    public void setNumero(String numero) {
        this.numero = numero;
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

    public String getDateAbsence() {
        return dateAbsence;
    }
    public void setDateAbsence(String dateAbsence) {
        this.dateAbsence = dateAbsence;
    }

    public String getDateRetour() {
        return dateRetour;
    }
    public void setDateRetour(String dateRetour) {
        this.dateRetour = dateRetour;
    }

    public String getMotif() {
        return motif;
    }
    public void setMotif(String motif) {
        this.motif = motif;
    }

    public int getIdAbsence() {
        return idAbsence;
    }
    public void setIdAbsence(int idAbsence) {
        this.idAbsence = idAbsence;
    }
}
