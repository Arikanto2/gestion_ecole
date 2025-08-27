package light.gestion_ecole.Model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AttitudeT {
    private int idattitude;
    private String participation;
    private String comportement;
    private Date dateattitude;
    private String ideleve;
    private String nummat;
    private int retard;

    public AttitudeT(String ideleve, String nummat, String participation, String comportement, Date dateattitude, int retard){
        this.ideleve=ideleve;
        this.nummat=nummat;
        this.participation=participation;
        this.comportement=comportement;
        this.dateattitude=dateattitude;
        this.retard = retard;
    }
    public int getIdattitude() {
        return idattitude;
    }
    public void setIdattitude(int idattitude) {
        this.idattitude = idattitude;
    }
    public String getParticipation() {
        return participation;
    }
    public void setParticipation(String participation) {
        this.participation = participation;
    }
    public String getComportement() {
        return comportement;
    }
    public void setComportement(String comportement) {
        this.comportement = comportement;
    }
    public String getDateattitude() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(dateattitude);
    }
    public void setDateattitude(Date dateattitude) {
        this.dateattitude = dateattitude;
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
    public int getRetard(){
        return retard;
    }
    public void setRetard(int retard){
        this.retard = retard;
    }
}
