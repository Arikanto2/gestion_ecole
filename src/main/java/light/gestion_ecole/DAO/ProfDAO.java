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
                "LEFT JOIN classe c on p.nomprof = c.\"Titulaire\" " ;

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

    public void ajoutProf(Professeur p, String titulaire) throws SQLException{
        String sql = "INSERT INTO professeur (nomprof, contactprof, adresseprof, emailprof) VALUES (?,?,?,?)";
        String upsql = "UPDATE classe SET \"Titulaire\" = ? WHERE designation = ? ";
        try(Connection conn = Database.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, p.getNom());
            stmt.setString(2, p.getContact());
            stmt.setString(3, p.getAdresse());
            stmt.setString(4, p.getEmail());
            stmt.executeUpdate();


            if (titulaire != null ) {
                try (PreparedStatement upstmt = conn.prepareStatement(upsql)) {
                    upstmt.setString(1, p.getNom());
                    upstmt.setString(2, titulaire);
                    upstmt.executeUpdate();
                }
            }
        }

    }

    public void modifieProf(Professeur p, String titulaire, String oldtitulaire) throws  SQLException{
        String sql = "UPDATE professeur SET nomprof = ?, contactprof = ?,  adresseprof = ?, emailprof = ? WHERE idprof = ?";
        String sql2 = "UPDATE classe SET \"Titulaire\" = ? WHERE designation = ?";
        String sql3 = "UPDATE classe SET \"Titulaire\" = '' WHERE designation = ?";
        try(Connection conn = Database.connect();
        PreparedStatement stmt = conn.prepareStatement(sql)){
          stmt.setString(1, p.getNom());
          stmt.setString(2, p.getContact());
          stmt.setString(3, p.getAdresse());
          stmt.setString(4, p.getEmail());
          stmt.setInt(5, p.getIdprof());
          stmt.executeUpdate();

          if (titulaire != oldtitulaire ) {
              try (PreparedStatement upstmt = conn.prepareStatement(sql2);
                   PreparedStatement upstmt2 = conn.prepareStatement(sql3)) {
                  upstmt.setString(1, p.getNom());
                  upstmt.setString(2, titulaire);
                  upstmt2.setString(1, oldtitulaire);
                  upstmt.executeUpdate();
                  upstmt2.executeUpdate();
              }
          }
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
        String sql = "SELECT p.nomprof FROM professeur p "+
                "LEFT JOIN classe c on p.nomprof = c.\"Titulaire\" " +
                "WHERE c.\"Titulaire\" is null";
        try (Connection conn = Database.connect()){
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                prenoms.add(rs.getString("nomprof"));
            }
        }
        return prenoms;
    }
    public List<String> getNomProf() throws SQLException {
        List<String> noms = new ArrayList<>();
        String sql = "SELECT nomprof FROM PROFESSEUR";
        try (Connection conn = Database.connect()){
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                noms.add(rs.getString("nomprof"));
            }
        }
        return noms;
    }
    public int getIdprof(String nom) {
        String sql = "SELECT idprof FROM PROFESSEUR WHERE nomprof = ?";
        try (Connection conn = Database.connect();
        PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, nom);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("idprof");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }
}
