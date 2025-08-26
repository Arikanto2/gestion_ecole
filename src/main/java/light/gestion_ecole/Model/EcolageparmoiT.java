package light.gestion_ecole.Model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EcolageparmoiT {
    private String ideleve;
    private String nummat;
    private int idecolage;
    private boolean statut;
    private Date ecolagemoi;
    public EcolageparmoiT(String ideleve, String nummat, int idecolage, boolean statut, Date ecolagemoi) {
        this.ideleve = ideleve;
        this.nummat = nummat;
        this.idecolage = idecolage;
        this.statut = statut;
        this.ecolagemoi = ecolagemoi;
    }
    public int getIdecolage() {
        return idecolage;
    }
    public void setIdecolage(int idecolage) {
        this.idecolage = idecolage;
    }
    public String getStatut() {
        if (statut){
            return "Payé";
        }
        else{
            return "Non Payé";
        }
    }
    public void setStatut(boolean statut) {
        this.statut = statut;
    }
    public String getEcolagemoi() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(ecolagemoi);
    }
    public void setEcolagemoi(Date ecolagemoi) {
        this.ecolagemoi = ecolagemoi;
    }
}
