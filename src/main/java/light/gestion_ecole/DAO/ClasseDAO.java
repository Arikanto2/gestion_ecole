package light.gestion_ecole.DAO;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import light.gestion_ecole.Model.Classe;
import light.gestion_ecole.Model.QueryLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClasseDAO {
    public static List<Classe> getAllClasses(String anne) throws SQLException {
        List<Classe> classes = new ArrayList<>();
        String sql = "SELECT c.idclass, c.designation, " +
                "COUNT(e.idclass) AS nbr_eleves, " +
                "c.\"Titulaire\", c.prixecolage " +
                "FROM classe c " +
                "LEFT JOIN eleve e ON c.idclass = e.idclass " +
                "AND e.avertissement IS DISTINCT FROM 'renvoyé' " +
                "AND e.anneescolaire = ? " +
                "GROUP BY c.idclass, c.designation, c.\"Titulaire\", c.prixecolage " +
                "ORDER BY c.designation";
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, anne);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    classes.add(new Classe(
                            rs.getInt("idclass"),
                            rs.getString("designation"),
                            rs.getInt("nbr_eleves"),
                            rs.getString("Titulaire"),
                            rs.getDouble("prixecolage")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classes;
    }




    public void ajouterClasse(Classe c) throws SQLException {
        String sql = "INSERT INTO classe (designation, \"Titulaire\", prixecolage) VALUES (?, ?, ?)";
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getDesignation());
            if (c.getProf() != null) ps.setString(2, c.getProf());
            else ps.setNull(2, Types.VARCHAR);
            ps.setDouble(3, c.getPrixEcolage());
            ps.executeUpdate();

            String profValue = (c.getProf() != null) ? ("'" + c.getProf() + "'") : "NULL";

            QueryLogger.append("INSERT INTO classe (designation, \"Titulaire\", prixecolage) VALUES ('"
                    + c.getDesignation() + "', "
                    + profValue + ", "
                    + c.getPrixEcolage() + ")");
        }
    }

    public void modifierClasse(Classe c) throws SQLException {
        String sql = "UPDATE classe SET designation = ?, \"Titulaire\" = ?, prixecolage = ? WHERE idclass = ?";
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getDesignation());
            if (c.getProf() != null) ps.setString(2, c.getProf());
            else ps.setNull(2, Types.VARCHAR);
            ps.setDouble(3, c.getPrixEcolage());
            ps.setInt(4, c.getIdClasse());
            ps.executeUpdate();

            String profValue = (c.getProf() != null) ? ("'" + c.getProf() + "'") : "NULL";

            QueryLogger.append("UPDATE classe SET designation = '" + c.getDesignation()
                    + "', \"Titulaire\" = " + profValue
                    + ", prixecolage = " + c.getPrixEcolage()
                    + " WHERE idclass = " + c.getIdClasse());
        }
    }


    public void supprimerClasse(int idClasse) throws SQLException {
        String sql = "DELETE FROM classe WHERE idclass = ?";
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idClasse);
            ps.executeUpdate();

            QueryLogger.append("DELETE FROM classe WHERE idclass = " + idClasse);
        }
    }

    // recupere les classe(combobox)
    public static ObservableList<String> getdesignationclasse() throws SQLException {
        ObservableList<String> classes = FXCollections.observableArrayList();
        String sql = "SELECT designation FROM classe " +
                "where \"Titulaire\" is null or \"Titulaire\" = '' ";
        try (Connection conn = Database.connect()){
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                classes.add(rs.getString("designation"));
            }
        }
        return classes;
    }

    public int getPrixEcolage(int id) throws SQLException {
        int prix = 0;
        String sql = "SELECT prixecolage FROM CLASSE WHERE idclass = ?";
        try (Connection conn = Database.connect(); PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                prix = rs.getInt("prixecolage");
            }
        }
        return prix;
    }

}
