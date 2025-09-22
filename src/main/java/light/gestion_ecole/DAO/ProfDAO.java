package light.gestion_ecole.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import light.gestion_ecole.Model.Professeur;
import light.gestion_ecole.Model.QueryLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfDAO {

    public List<Professeur> getProfesseurs() throws SQLException {
        List<Professeur> profs = new ArrayList<>();
        String sql = "SELECT p.idprof, p.nomprof, c.Designation, p.contactprof, p.adresseprof, p.emailprof " +
                "FROM Professeur p " +
                "LEFT JOIN classe c on p.nomprof = c.\"Titulaire\" " +
                "ORDER BY p.nomprof;";

        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

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
        }
        return profs;
    }

    public void ajoutProf(Professeur p, String titulaire) throws SQLException {
        String sql = "INSERT INTO professeur (nomprof, contactprof, adresseprof, emailprof) VALUES (?,?,?,?)";
        String upsql = "UPDATE classe SET \"Titulaire\" = ? WHERE designation = ? ";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getNom());
            stmt.setString(2, p.getContact());
            stmt.setString(3, p.getAdresse());
            stmt.setString(4, p.getEmail());
            stmt.executeUpdate();

            QueryLogger.append("INSERT INTO professeur (nomprof, contactprof, adresseprof, emailprof) VALUES (" +
                    toSQLString(p.getNom()) + ", " +
                    toSQLString(p.getContact()) + ", " +
                    toSQLString(p.getAdresse()) + ", " +
                    toSQLString(p.getEmail()) + ")");

            if (titulaire != null && !titulaire.isEmpty()) {
                try (PreparedStatement upstmt = conn.prepareStatement(upsql)) {
                    upstmt.setString(1, p.getNom());
                    upstmt.setString(2, titulaire);
                    upstmt.executeUpdate();

                    QueryLogger.append("UPDATE classe SET \"Titulaire\" = " + toSQLString(p.getNom()) +
                            " WHERE designation = " + toSQLString(titulaire));
                }
            }
        }
    }

    public void modifieProf(Professeur p, String titulaire, String oldtitulaire) throws SQLException {
        String sql = "UPDATE professeur SET nomprof = ?, contactprof = ?, adresseprof = ?, emailprof = ? WHERE idprof = ?";
        String sql2 = "UPDATE classe SET \"Titulaire\" = ? WHERE designation = ?";
        String sql3 = "UPDATE classe SET \"Titulaire\" = '' WHERE designation = ?";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getNom());
            stmt.setString(2, p.getContact());
            stmt.setString(3, p.getAdresse());
            stmt.setString(4, p.getEmail());
            stmt.setInt(5, p.getIdprof());
            stmt.executeUpdate();

            QueryLogger.append("UPDATE professeur SET nomprof = " + toSQLString(p.getNom()) +
                    ", contactprof = " + toSQLString(p.getContact()) +
                    ", adresseprof = " + toSQLString(p.getAdresse()) +
                    ", emailprof = " + toSQLString(p.getEmail()) +
                    " WHERE idprof = " + p.getIdprof());

            if (!titulaire.equals(oldtitulaire)) {
                try (PreparedStatement upstmt = conn.prepareStatement(sql2);
                     PreparedStatement upstmt2 = conn.prepareStatement(sql3)) {

                    if (titulaire != null && !titulaire.isEmpty()) {
                        upstmt.setString(1, p.getNom());
                        upstmt.setString(2, titulaire);
                        upstmt.executeUpdate();
                        QueryLogger.append("UPDATE classe SET \"Titulaire\" = " + toSQLString(p.getNom()) +
                                " WHERE designation = " + toSQLString(titulaire));
                    }

                    if (oldtitulaire != null && !oldtitulaire.isEmpty()) {
                        upstmt2.setString(1, oldtitulaire);
                        upstmt2.executeUpdate();
                        QueryLogger.append("UPDATE classe SET \"Titulaire\" = '' WHERE designation = " +
                                toSQLString(oldtitulaire));
                    }
                }
            }
        }
    }

    public void supprimerProf(int idprof) throws SQLException {
        String sql = "DELETE FROM professeur WHERE idprof = ?";
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idprof);
            stmt.executeUpdate();

            QueryLogger.append("DELETE FROM professeur WHERE idprof = " + idprof);
        }
    }

    public ObservableList<String> getprenom() throws SQLException {
        ObservableList<String> prenoms = FXCollections.observableArrayList();
        String sql = "SELECT p.nomprof FROM professeur p " +
                "LEFT JOIN classe c on p.nomprof = c.\"Titulaire\" " +
                "WHERE c.\"Titulaire\" is null";

        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                prenoms.add(rs.getString("nomprof"));
            }
        }
        return prenoms;
    }

    public List<String> getNomProf() throws SQLException {
        List<String> noms = new ArrayList<>();
        String sql = "SELECT nomprof FROM PROFESSEUR";
        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                noms.add(rs.getString("nomprof"));
            }
        }
        return noms;
    }

    public int getIdprof(String nom) {
        String sql = "SELECT idprof FROM PROFESSEUR WHERE nomprof = ?";
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nom);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("idprof");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private String toSQLString(String value) {
        if (value == null || value.isEmpty()) return "NULL";
        return "'" + value.replace("'", "''") + "'";
    }
}
