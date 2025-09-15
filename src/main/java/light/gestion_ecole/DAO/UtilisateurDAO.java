package light.gestion_ecole.DAO;

import light.gestion_ecole.Model.Utilisateur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO {
    public static Utilisateur Connecter(String mdp, String nom) throws SQLException {
        String sql = "SELECT emailutil FROM utilisateur WHERE nomutilisateur = ? AND mdp = ?";
        try(Connection connect = Database.connect();
            PreparedStatement stmt = connect.prepareStatement(sql);){
            stmt.setString(1,nom);
            stmt.setString(2,mdp);
            try(ResultSet rs = stmt.executeQuery()){
                if (rs.next()){
                    String mail = rs.getString("emailutil");
                    Utilisateur u = new Utilisateur(mail,nom);
                    return u;
                }else {
                    return null;
                }
            }
        }
    }
    public static List<String> getEmailUtilisateur() throws SQLException {
        List<String> emailUtilisateur = new ArrayList<>();
        String sql = "SELECT emailutil FROM utilisateur ";
        try(Connection connect = Database.connect();
            Statement stmt = connect.createStatement();){
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
            emailUtilisateur.add(rs.getString("emailutil"));}
        }
        return   emailUtilisateur;

    }
}
