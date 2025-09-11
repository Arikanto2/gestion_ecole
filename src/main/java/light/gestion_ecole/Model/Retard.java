package light.gestion_ecole.Model;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class Retard {
    private String Numero;
    private String Nom_Prenom;
    private int nbRetards;
    private HBox hBox;
    private Button Ajout;
    private Button Diminuer;

    public Retard(String Numero, String Nom_Prenom, int nbRetards) {
        this.Numero = Numero;
        this.Nom_Prenom = Nom_Prenom;
        this.nbRetards = nbRetards;
        this.hBox = new HBox();
        this.Ajout = new Button("+");
        this.Diminuer = new Button("-");

        this.Ajout.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-background-color: linear-gradient(to right,#0088cc,#1f6391);" +
                        "-fx-text-fill: #ffffff;" +
                        "-fx-border-radius: 50px;"

        );

        this.Diminuer.setStyle(
                "-fx-background-color: linear-gradient(to left, #ff9999, #ff6666);" +
                        "-fx-border-radius: 120px;" +
                        "-fx-text-fill: White;" +
                        "-fx-font-size: 12px;" +
                        "-fx-pref-width: 27;" +
                        "-fx-pref-height: 35;"
        );


        this.hBox.getChildren().addAll(this.Ajout, this.Diminuer);
        this.hBox.setAlignment(Pos.CENTER);
        this.hBox.setSpacing(5);
    }

    public String getNumero() {
        return Numero;
    }

    public void setNumero(String numero) {
        this.Numero = numero;
    }

    public String getNom_Prenom() {
        return Nom_Prenom;
    }

    public void setNom_Prenom(String nom_Prenom) {
        this.Nom_Prenom = nom_Prenom;
    }

    public int getNbRetards() {
        return nbRetards;
    }

    public void setNbRetards(int nbRetards) {
        this.nbRetards = nbRetards;
    }

    public HBox getHBox() {
        return hBox;
    }

    public void setHBox(HBox hBox) {
        this.hBox = hBox;
    }

    public Button getAjout() {
        return Ajout;
    }

    public void setAjout(Button ajout) {
        this.Ajout = ajout;
    }

    public Button getDiminuer() {
        return Diminuer;
    }

    public void setDiminuer(Button diminuer) {
        this.Diminuer = diminuer;
    }
}
