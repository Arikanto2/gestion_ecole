package light.gestion_ecole.Model;

public class Utilisateur {
    public String email;
    public String MDP;
    public int idUtil;
    public String nom;

    public Utilisateur(String email, String MDP) {
        this.email = email;
        this.MDP = MDP;
        this.idUtil = 0;
        this.nom = "";
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getMDP() {
        return MDP;
    }
    public void setMDP(String MDP) {
        this.MDP = MDP;
    }
    public int getIdUtil() {
        return idUtil;
    }
    public void setIdUtil(int idUtil) {
        this.idUtil = idUtil;
    }
}
