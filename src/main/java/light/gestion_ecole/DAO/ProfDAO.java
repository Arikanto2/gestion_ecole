package light.gestion_ecole.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import light.gestion_ecole.Model.Professeur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfDAO {
    public List<Professeur> getProfesseurs() throws SQLException {
        List<Professeur> profs = new ArrayList<>();
        String sql = "SELECT p.idprof, p.nomprof,c.Designation, p.contactprof, p.adresseprof, p.emailprof " +
                "From Professeur p " +
                "LEFT JOIN classe c on substring(p.nomprof FROM position(' ' in nomprof)+1 ) = c.\"Titulaire\" " ;

        try (Connection conn = Database.connect()){
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                profs.add(new Professeur(
                        rs.getInt("idprof"),
                        rs.getString("nomprof"),
                        rs.getString("designation"),
                        rs.getString("contactprof"),
                        rs.getString("adresseprof"),
                        rs.getString("emailprof")

                ));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return profs;
    }

    public void ajoutProf(Professeur p) throws SQLException{
        String sql = "INSERT INTO professeur (nomprof, contactprof, adresseprof, emailprof) VALUES (?,?,?,?)";
        try(Connection conn = Database.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, p.getNom());
            stmt.setString(2, p.getContact());
            stmt.setString(3, p.getAdresse());
            stmt.setString(4, p.getEmail());
            stmt.executeUpdate();
        }
    }

    public void modifieProf(Professeur p) throws  SQLException{
        String sql = "UPDATE professeur SET nomprof = ?, contactprof = ?,  adresseprof = ?, emailprof = ? WHERE idprof = ?";
        try(Connection conn = Database.connect();
        PreparedStatement stmt = conn.prepareStatement(sql)){
          stmt.setString(1, p.getNom());
          stmt.setString(2, p.getContact());
          stmt.setString(3, p.getAdresse());
          stmt.setString(4, p.getEmail());
          stmt.setInt(5, p.getIdprof());
          stmt.executeUpdate();
        }
    }

    public void supprimerProf(int idprof) throws SQLException{
        String sql = "DELETE FROM professeur WHERE idprof = ?";
        try(Connection conn = Database.connect();
        PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, idprof);
            stmt.executeUpdate();
        }
    }

    //pour profs dans classe (combobox)
    public ObservableList<String> getprenom() throws SQLException {
        ObservableList<String> prenoms = FXCollections.observableArrayList();
        String sql = "SELECT substring(nomprof FROM position(' ' in nomprof)+1 ) AS prenom from professeur ";
        try (Connection conn = Database.connect()){
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                prenoms.add(rs.getString("prenom"));
            }
        }
        return prenoms;
    }
}
