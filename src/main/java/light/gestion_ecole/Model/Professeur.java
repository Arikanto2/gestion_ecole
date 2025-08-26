package light.gestion_ecole.Model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class Professeur {
    private int idprof;
    private SimpleStringProperty Nom;
    private SimpleStringProperty Titulaire;
    private String Contact;
    private String Adresse;
    private SimpleStringProperty Email;

    public Professeur(int idprof ,String Nom, String Titulaire, String Contact, String Adresse, String Email) {
        this.idprof = idprof;
        this.Nom = new SimpleStringProperty(Nom);
        this.Titulaire = new SimpleStringProperty(Titulaire);
        this.Contact = Contact;
        this.Adresse = Adresse;
        this.Email = new  SimpleStringProperty(Email);
    }

    // ajout et modif
    public Professeur(int idprof ,String Nom, String Contact, String Adresse, String Email) {
        this.idprof = idprof;
        this.Nom = new SimpleStringProperty(Nom);
        this.Contact = Contact;
        this.Adresse = Adresse;
        this.Email = new  SimpleStringProperty(Email);
    }


    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getAdresse() {
        return Adresse;
    }

    public void setAdresse(String adresse) {
        Adresse = adresse;
    }


    public String getNom() {
        return Nom.get();
    }

    public SimpleStringProperty nomProperty() {
        return Nom;
    }

    public void setNom(String nom) {
        this.Nom.set(nom);
    }

    public String getTitulaire() {
        return Titulaire.get();
    }

    public SimpleStringProperty titulaireProperty() {
        return Titulaire;
    }

    public void setTitulaire(String titulaire) {
        this.Titulaire.set(titulaire);
    }

    public String getEmail() {
        return Email.get();
    }

    public SimpleStringProperty emailProperty() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email.set(email);
    }

    public int getIdprof() {
        return idprof;
    }

    public void setIdprof(int idprof) {
        this.idprof = idprof;
    }
}
