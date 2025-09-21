package light.gestion_ecole.DAO;

import light.gestion_ecole.Model.ExamenNa;
import light.gestion_ecole.Model.QueryLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExamnatDAO {

    public static List<String> getClasseEXAMEN() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT c.designation FROM classe c JOIN examnat e ON c.idclass = e.idclass";
        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(rs.getString("designation"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public static List<String> getNonClasseEXAMEN() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT designation FROM classe WHERE idclass NOT IN (SELECT idclass FROM examnat)";
        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(rs.getString("designation"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public static List<ExamenNa> getExamenNa(String an, String designation) throws SQLException {
        List<ExamenNa> list = new ArrayList<>();
        String sql = "SELECT e.nummat, e.nomeleve, e.prenomeleve, e.examennational " +
                "FROM classe c JOIN eleve e ON c.idclass = e.idclass " +
                "WHERE c.designation = ? AND e.anneescolaire = ?";
        try (Connection conn = Database.connect();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, designation);
            statement.setString(2, an);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    ExamenNa e = new ExamenNa();
                    e.setNumeroMatricule(rs.getString("nummat"));
                    e.setNomEleve(rs.getString("nomeleve") + " " + rs.getString("prenomeleve"));
                    e.setAdmission(rs.getBoolean("examennational") ? "Admis(e)" : "Non admis(e)");
                    list.add(e);
                }
            }
        }
        return list;
    }

    public static void UpdateExamen(List<String> ideleve, String classe) throws SQLException {
        String sql = "UPDATE eleve " +
                "SET examennational = CASE WHEN ideleve = ANY(?) THEN FALSE ELSE TRUE END " +
                "WHERE idclass IN (SELECT idclass FROM classe WHERE designation = ?)";
        try (Connection conn = Database.connect();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            String[] ids = ideleve.toArray(new String[0]);
            Array sqlArray = conn.createArrayOf("text", ids);

            statement.setArray(1, sqlArray);
            statement.setString(2, classe);

            int rows = statement.executeUpdate();

            if (rows > 0) {
                String id = ideleve.stream()
                        .map(s -> "'" + s + "'")
                        .reduce((a, b) -> a + "," + b)
                        .orElse("");


                QueryLogger.append(
                        "UPDATE eleve SET examennational = CASE WHEN ideleve IN ("+id+") THEN FALSE ELSE TRUE END " +
                        "WHERE idclass IN (SELECT idclass FROM classe WHERE designation = '" + classe + "')");

            }

        }
    }

    public static void AjouterExamen(String classe) throws SQLException {
        String sql = "INSERT INTO examnat (idclass) SELECT idclass FROM classe WHERE designation = ?";
        try (Connection conn = Database.connect();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, classe);
            int rows = statement.executeUpdate();
            if (rows > 0) {
                QueryLogger.append("INSERT INTO examnat (idclass) SELECT idclass FROM classe WHERE designation = '" + classe + "'");
            }
        }
    }

    public static void supprimerClasseExamen(String classe) throws SQLException {
        String sql = "DELETE FROM examnat WHERE idclass = (SELECT idclass FROM classe WHERE designation = ?)";
        try (Connection conn = Database.connect();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, classe);
            int rows = statement.executeUpdate();
            if (rows > 0) {
                QueryLogger.append("DELETE FROM examnat WHERE idclass = (SELECT idclass FROM classe WHERE designation = '" + classe + "')");
            }
        }
    }


}
