package light.gestion_ecole.Model;

public class Utilisateur {
    public String email;
    public String nom;
    public String MDP;

    public Utilisateur(String email, String nom) {
        this.email = email;
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getNom() {
        return this.nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getMDP() {
        return this.MDP;
    }
    public  void setMDP(String MDP) {
        this.MDP = MDP;
    }
}