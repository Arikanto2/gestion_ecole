package light.gestion_ecole.Model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import light.gestion_ecole.DAO.EleveDAO;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

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
    private String handicap;

    public Eleve(String idEleve, String matricule,int idclass, int idparent,
                 String nom, String prenom, String adresse, Date datenaiss, String sex,
                 String annee_scolaire, Boolean ispassant, Boolean examennational,String handicap) {
        this.ideleve = idEleve;
        this.nummat = matricule;
        //this.idattitude = idattitude;
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
        this.handicap = handicap;
    }
    public Eleve(String nummat,String nomeleve){
        this.nummat = nummat;
        this.nomeleve = nomeleve;
    }

    /////////////// constructeur pour pdf pour chaque classe //////////////
    private int numero;
    private String nom;
    private String prenom;

    public Eleve(int numero, String nom, String prenom){
        this.numero = numero;
        this.nom = nom;
        this.prenom = prenom;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
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

        //////////////////////// fin ////////////////

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
    public String getDatenaissance() {
        SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy", Locale.FRENCH);
        return sdf.format(datenaissance);
    }
    public Date getDatenaissance2() {
        return datenaissance;
    }
    public void setDatenaissance(Date datenaissance) {
        this.datenaissance = datenaissance;
    }
    public ImageView getGenreeleveIcon() {
        String path;
        if(genreeleve.trim().equalsIgnoreCase("Garçon")) {
            path = "/light/gestion_ecole/Photo/male2.png";
        }else{
            path = "/light/gestion_ecole/Photo/female2.png";
        }
        var is = getClass().getResourceAsStream(path);

        if(is == null) {
            System.err.println("Image genre eleve introuvable");
            return new ImageView();
        }
        Image image = new Image(is);
        ImageView genreeleve = new ImageView(image);
        genreeleve.setFitHeight(30);
        genreeleve.setFitWidth(25);
        genreeleve.setPreserveRatio(true);
        return genreeleve;
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
    public String getIspassant() {
        if (ispassant)
            return "Passant";
        else
            return "Redoublant";
    }
    public Boolean getIspassant2() {
            return ispassant;
    }
    public void setIspassant(Boolean ispassant) {
        this.ispassant = ispassant;
    }
    public String getExamennational() {
        if (examennational)
            return "reussi";
        else
            return "Pas encore";
    }
    public Boolean getNational(){
        return examennational;
    }
    public void setExamennational(Boolean examennational) {
        this.examennational = examennational;
    }
    public String getClasse() throws SQLException {
        String classe = EleveDAO.getDistinctClasse(idclass);
        return String.valueOf(classe);
    }
    public String getHandicap(){
        if(handicap==null){
            return "Non handicap";
        } else {
            return handicap;
        }
    }


}
