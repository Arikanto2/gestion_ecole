package light.gestion_ecole.Model;

public class ParentT {
    private int idparent;
    private String nompere;
    private String professionpere;
    private String nommere;
    private String professionmere;
    private String tuteur;
    private String professiontuteur;
    private String contact;
    private String emailparent;
    private String classeEleve; // Nouvelle propriété pour la classe de l'élève

    public ParentT(int idparent, String nompere, String professionpere, String nommere, String professionmere,
                   String tuteur, String professiontuteur, String contact, String emailparent, String classeEleve)
    {
        this.idparent = idparent;
        this.nompere = nompere;
        this.professionpere = professionpere;
        this.nommere = nommere;
        this.professionmere = professionmere;
        this.tuteur = tuteur;
        this.professiontuteur = professiontuteur;
        this.contact = contact;
        this.emailparent = emailparent;
        this.classeEleve = classeEleve;
    }

    public ParentT(int idparent, String nompere, String professionpere, String nommere, String professionmere,
                   String tuteur, String professiontuteur, String contact, String emailparent)
    {
        this.idparent = idparent;
        this.nompere = nompere;
        this.professionpere = professionpere;
        this.nommere = nommere;
        this.professionmere = professionmere;
        this.tuteur = tuteur;
        this.professiontuteur = professiontuteur;
        this.contact = contact;
        this.emailparent = emailparent;
        this.classeEleve = ""; // Par défaut vide
    }
    
    public ParentT(String nompere, String professionpere, String nommere, String professionmere,
                   String tuteur, String professiontuteur, String contact, String emailparent)
    {
        this.nompere = nompere;
        this.professionpere = professionpere;
        this.nommere = nommere;
        this.professionmere = professionmere;
        this.tuteur = tuteur;
        this.professiontuteur = professiontuteur;
        this.contact = contact;
        this.emailparent = emailparent;
        this.classeEleve = ""; // Par défaut vide
    }
    public int getIdparent() {
        return idparent;
    }
    public void setIdparent(int idparent) {
        this.idparent = idparent;
    }
    public String getNompere() {
        return nompere;
    }
    public void setNompere(String nompere) {
        this.nompere = nompere;
    }
    public String getProfessionpere() {
        return professionpere;
    }
    public void setProfessionpere(String professionpere) {
        this.professionpere = professionpere;
    }
    public String getNommere() {
        return nommere;
    }
    public void setNommere(String nommere) {
        this.nommere = nommere;
    }
    public String getProfessionmere() {
        return professionmere;
    }
    public void setProfessionmere(String professionmere) {
        this.professionmere = professionmere;
    }
    public String getTuteur() {
        if (tuteur == null) {
            return " ";
        }
        return tuteur;
    }
    public void setTuteur(String tuteur) {
        this.tuteur = tuteur;
    }
    public String getProfessiontuteur() {
        if (tuteur == null) {
            return " ";
        }
        return professiontuteur;
    }
    public void setProfessiontuteur(String professiontuteur) {
        this.professiontuteur = professiontuteur;
    }
    public String getContact() {
        return contact;
    }
    public void setContact(String contact) {
        this.contact = contact;
    }
    public String getEmailparent() {
        return emailparent;
    }
    public void setEmailparent(String emailparent) {
        this.emailparent = emailparent;
    }

    public String getClasseEleve() {
        return classeEleve != null ? classeEleve : "";
    }

    public void setClasseEleve(String classeEleve) {
        this.classeEleve = classeEleve;
    }
}
