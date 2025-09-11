package light.gestion_ecole.Model;

public class ExamenNa {

    private String numeroMatricule;
    private String nomEleve;
    private String admission;

    public ExamenNa() {
    }

    public ExamenNa(String numeroMatricule, String nomEleve, String admission) {
        this.numeroMatricule = numeroMatricule;
        this.nomEleve = nomEleve;
        this.admission = admission;
    }

    public String getNumeroMatricule() {
        return numeroMatricule;
    }

    public void setNumeroMatricule(String numeroMatricule) {
        this.numeroMatricule = numeroMatricule;
    }

    public String getNomEleve() {
        return nomEleve;
    }

    public void setNomEleve(String nomEleve) {
        this.nomEleve = nomEleve;
    }

    public String getAdmission() {
        return admission;
    }

    public void setAdmission(String admission) {
        this.admission = admission;
    }


}
