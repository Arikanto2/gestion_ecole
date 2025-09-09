package light.gestion_ecole.Model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EcolageparmoiT {
    private String ideleve;
    private String nummat;
    private int idecolage;
    private boolean statut;
    private Date ecolagemoi;
    private String moiseco;
    private Boolean disabled;
    public EcolageparmoiT(String ideleve, String nummat, int idecolage, boolean statut, Date ecolagemoi, String moiseco) {
        this.ideleve = ideleve;
        this.nummat = nummat;
        this.idecolage = idecolage;
        this.statut = statut;
        this.ecolagemoi = ecolagemoi;
        this.moiseco = moiseco;
        this.disabled = false;
    }
    public EcolageparmoiT(String moiseco, boolean statut) {
        this.moiseco = moiseco;
        this.statut = statut;
        this.disabled = false;
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
    public boolean isStatut() {
        return statut;
    }
    public void setStatut(boolean statut) {
        this.statut = statut;
    }
    public String getEcolagemoi() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(ecolagemoi);
    }
    public Date getecolagemoiAsDate() {
        return  ecolagemoi;
    }
    public void setEcolagemoi(Date ecolagemoi) {
        this.ecolagemoi = ecolagemoi;
    }
    public String getMoiseco() {
        return moiseco;
    }
    public void setMoiseco(String moiseco) {
        this.moiseco = moiseco;
    }
    public Boolean isDisabled() {
        return disabled;
    }
    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }
}
