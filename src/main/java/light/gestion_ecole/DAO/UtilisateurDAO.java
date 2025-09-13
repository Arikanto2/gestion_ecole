package light.gestion_ecole.DAO;

import light.gestion_ecole.Model.Utilisateur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtilisateurDAO {
    public static String Connecter(Utilisateur utilisateur) throws SQLException {
        String sql = "SELECT * FROM utilisateur WHERE nomutilisateur = ? AND mdp = ?";
        try(Connection connect = Database.connect();
            PreparedStatement stmt = connect.prepareStatement(sql);){
            stmt.setString(1,utilisateur.getEmail());
            stmt.setString(2,utilisateur.getMDP());
            try(ResultSet rs = stmt.executeQuery()){
                if (rs.next()){
                    String nom = rs.getString("nomutilisateur");
                    return nom;
                }else {
                    return "";
                }
            }
        }
    }
}
