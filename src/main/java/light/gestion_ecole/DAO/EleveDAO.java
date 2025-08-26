package light.gestion_ecole.DAO;


import light.gestion_ecole.Model.Eleve;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EleveDAO {
    public List<Eleve> getEleves() throws SQLException {
        List<Eleve> eleves = new ArrayList<>();
        String sql = "SELECT * FROM eleve";

        try (Connection conn  = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                eleves.add(new Eleve(rs.getString("ideleve"),rs.getString("nummat")
                        ,rs.getInt("idclass"), rs.getInt("idparent"),rs.getString("nomeleve"),
                        rs.getString("prenomeleve"),rs.getString("adresseeleve"),
                        rs.getDate("datenaiss"),rs.getString("genre"),
                        rs.getString("anneescolaire"),rs.getBoolean("ispassant"),
                        rs.getBoolean("examennational"),rs.getString("handicap")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return eleves;
    }

    public List<String> getDistinctAnnees() throws SQLException {
        List<String> distinctAnnees = new ArrayList<>();
        String sql = "SELECT DISTINCT anneescolaire FROM eleve";
        try (Connection conn  = Database.connect();Statement stmt = conn.createStatement();ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                distinctAnnees.add(rs.getString("anneescolaire"));
            }
        }
        return distinctAnnees;
    }

    public List<String> getDistinctClasses() throws SQLException {
        List<String> distinctClasses = new ArrayList<>();
        String sql = "SELECT DISTINCT designation FROM CLASSE";
        try (Connection conn  = Database.connect();Statement stmt = conn.createStatement();ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                distinctClasses.add(rs.getString("designation"));
            }
        }
        return distinctClasses;
    }

    public List<String> getDistinctSexe() throws SQLException {
        List<String> distinctSexes = new ArrayList<>();
        String sql = "SELECT DISTINCT genre FROM ELEVE";
        try (Connection conn  = Database.connect();Statement stmt = conn.createStatement();ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {
                distinctSexes.add(rs.getString("genre"));
            }
        }
        return distinctSexes;
    }

    public List<String> getDistinctHandicap() throws SQLException {
        List<String> distinctHandicap = new ArrayList<>();
        String sql = "SELECT DISTINCT handicap FROM ELEVE";
        try (Connection conn  = Database.connect();Statement stmt = conn.createStatement();ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {
                distinctHandicap.add(rs.getString("handicap"));
            }
        }
        return distinctHandicap;
    }

    public static String getDistinctClasse(int id) throws SQLException {
        String sql = "SELECT DISTINCT designation FROM CLASSE where idclass = ?";
        String result = "";
        try (Connection conn  = Database.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                result = rs.getString("designation");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public int getIdClass(String design) throws SQLException{
        String sql = "SELECT idclass FROM CLASSE WHERE designation = ?";
        int result = 1;
        try (Connection conn  = Database.connect();PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, design);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                result = rs.getInt("idclass");
            }
        }
        return result;
    }

    public void insertEleve(Eleve eleve) throws SQLException {
        String sql = "INSERT INTO ELEVE (ideleve, nummat,idclass, idparent, nomeleve, prenomeleve," +
                "adresseeleve, datenaiss, genre, anneescolaire, ispassant, examennational,handicap)" +
                " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?) ";
        try (Connection conn  = Database.connect();PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eleve.getIdeleve());
            stmt.setString(2, eleve.getNummat());
            stmt.setInt(3, eleve.getIdclass());
            stmt.setInt(4, eleve.getIdparent());
            stmt.setString(5, eleve.getNomeleve());
            stmt.setString(6, eleve.getPrenomeleve());
            stmt.setString(7, eleve.getAdresseeleve());
            stmt.setDate(8, (Date) eleve.getDatenaissance2());
            stmt.setString(9,eleve.getGenreeleve());
            stmt.setString(10,eleve.getAnneescolaire());
            stmt.setBoolean(11, Boolean.parseBoolean(eleve.getIspassant()));
            stmt.setBoolean(12, Boolean.parseBoolean(eleve.getExamennational()));
            stmt.setString(13, eleve.getHandicap());
            stmt.executeUpdate();
        }
    }

    public int nbrEleves() throws SQLException {
        String sql = "SELECT count(*) as cmd FROM ELEVE";
        try (Connection conn  = Database.connect();Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("cmd");
            }
        }
        return 0;
    }
}
