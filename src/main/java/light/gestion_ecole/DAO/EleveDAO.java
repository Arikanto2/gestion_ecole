package light.gestion_ecole.DAO;


import light.gestion_ecole.Model.Eleve;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EleveDAO {
    public List<Eleve> getEleves() throws SQLException {
        List<Eleve> eleves = new ArrayList<>();
        String sql = "SELECT ideleve, nummat, idclass, idparent, (nomeleve ||' ' || prenomeleve) as nomeleve, prenomeleve, adresseeleve," +
                "datenaiss, genre, anneescolaire, handicap FROM eleve ORDER BY nummat";

        try (Connection conn  = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                eleves.add(new Eleve(rs.getString("ideleve"),rs.getString("nummat")
                        ,rs.getInt("idclass"), rs.getInt("idparent"),rs.getString("nomeleve"),
                        rs.getString("prenomeleve"),rs.getString("adresseeleve"),
                        rs.getDate("datenaiss"),rs.getString("genre"),
                        rs.getString("anneescolaire"),rs.getString("handicap")));
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
                "adresseeleve, datenaiss, genre, anneescolaire,handicap)" +
                " VALUES (?,?,?,?,?,?,?,?,?,?,?) ";
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
            stmt.setString(11, eleve.getHandicap());
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
    public List<Eleve> filtreAnnee(String annee) throws SQLException {
        List<Eleve> eleves = new ArrayList<>();
        String sql = "SELECT ideleve, nummat, idclass, idparent, (nomeleve ||' ' || prenomeleve) as nomeleve, prenomeleve, adresseeleve," +
                "datenaiss, genre, anneescolaire, handicap FROM eleve where anneescolaire = ? ORDER BY nummat";
        try (Connection conn  = Database.connect();PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setString(1, annee);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                eleves.add(new Eleve(rs.getString("ideleve"),rs.getString("nummat")
                        ,rs.getInt("idclass"), rs.getInt("idparent"),rs.getString("nomeleve"),
                        rs.getString("prenomeleve"),rs.getString("adresseeleve"),
                        rs.getDate("datenaiss"),rs.getString("genre"),
                        rs.getString("anneescolaire"),rs.getString("handicap")));
            }
        }
        return eleves;
    }
    public List<Eleve> filtreClasse(int classe) throws SQLException {
        List<Eleve> eleves = new ArrayList<>();
        String sql = "SELECT ideleve, nummat, idclass, idparent, (nomeleve ||' ' || prenomeleve) as nomeleve, prenomeleve, adresseeleve," +
                "datenaiss, genre, anneescolaire, handicap from ELEVE where idClass = ? ORDER BY nummat";
        try (Connection conn  = Database.connect();PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setInt(1, classe);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                eleves.add(new Eleve(rs.getString("ideleve"),rs.getString("nummat")
                        ,rs.getInt("idclass"), rs.getInt("idparent"),rs.getString("nomeleve"),
                        rs.getString("prenomeleve"),rs.getString("adresseeleve"),
                        rs.getDate("datenaiss"),rs.getString("genre"),
                        rs.getString("anneescolaire"),rs.getString("handicap")));
            }
        }
        return eleves;
    }
    public List<Eleve> filtreDeuxCombo(String annee,int classe) throws SQLException {
        List<Eleve> eleves = new ArrayList<>();
        String sql = "SELECT ideleve, nummat, idclass, idparent, (nomeleve ||' ' || prenomeleve) as nomeleve, prenomeleve, adresseeleve," +
                "datenaiss, genre, anneescolaire, handicap from ELEVE where anneescolaire = ? AND idclass = ? ORDER BY nummat";
        try (Connection conn  = Database.connect();PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setString(1, annee);
            stmt.setInt(2, classe);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                eleves.add(new Eleve(rs.getString("ideleve"),rs.getString("nummat")
                        ,rs.getInt("idclass"), rs.getInt("idparent"),rs.getString("nomeleve"),
                        rs.getString("prenomeleve"),rs.getString("adresseeleve"),
                        rs.getDate("datenaiss"),rs.getString("genre"),
                        rs.getString("anneescolaire"),rs.getString("handicap")));
            }
        }
        return eleves;
    }

    public void updateEleve(Eleve eleve) throws SQLException {
        String sql = "UPDATE ELEVE SET idclass = ?, idparent = ?, nomeleve = ?, prenomeleve = ?,adresseeleve =?, datenaiss = ?, genre =?, " +
                "anneescolaire = ?,handicap = ? WHERE ideleve = ?";
        try (Connection conn  = Database.connect();PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, eleve.getIdclass());
            stmt.setInt(2, eleve.getIdparent());
            stmt.setString(3, eleve.getNomeleve());
            stmt.setString(4, eleve.getPrenomeleve());
            stmt.setString(5, eleve.getAdresseeleve());
            stmt.setDate(6, (Date) eleve.getDatenaissance2());
            stmt.setString(7, eleve.getGenreeleve());
            stmt.setString(8, eleve.getAnneescolaire());
            stmt.setString(9, eleve.getHandicap());
            stmt.setString(10, eleve.getIdeleve());
            stmt.executeUpdate();
        }
    }
    public List<Eleve> getNumNom(int classe,String Annee) throws SQLException {
        List<Eleve> eleves = new ArrayList<>();
        String sql = "SELECT nummat,nomeleve || ' ' || prenomeleve as nomeleve from ELEVE WHERE idclass = ? AND anneescolaire = ? ORDER BY nummat";
        try (Connection conn  = Database.connect();PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setInt(1, classe);
            stmt.setString(2, Annee);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                eleves.add(new Eleve(rs.getString("nummat"),rs.getString("nomeleve")));
            }
        }
        return eleves;
    }

    public String getIdEleve(String nummat, String annee) {
        String ideleve = "";
        String sql = "SELECT ideleve FROM ELEVE WHERE nummat = ? AND anneescolaire = ?";
        try (Connection conn = Database.connect(); PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setString(1, nummat);
            stmt.setString(2, annee);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ideleve = rs.getString("ideleve");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ideleve;
    }
    // pour pdf eleve

    public List<Eleve> getalleleveinclasse(String designation) throws SQLException {
        List<Eleve> ele_pdf = new ArrayList<>();
        String sql = "SELECT Row_number() Over (Order by e.nomeleve ASC ) AS numero, e.nomeleve, e.prenomeleve FROM classe c " +
                "join eleve e on c.idclass = e.idclass " +
                "where c.designation = ? " +
                "Order by e.nomeleve Asc";
        try (Connection conn = Database.connect();
        PreparedStatement ps = conn.prepareStatement(sql); ){
            ps.setString(1,designation);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ele_pdf.add(new Eleve(
                   rs.getInt("numero"),
                   rs.getString("nomeleve"),
                   rs.getString("prenomeleve")
                ));
            }
        }
        return ele_pdf;
    }
}
