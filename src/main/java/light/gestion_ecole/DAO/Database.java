package light.gestion_ecole.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String URL = "jdbc:postgresql://localhost:5432/gestion_ecole";
    private static final String USER = "postgres";
    private static final String PASSWORD = "rft3055";


    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void execute(String sql) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            String[] queries = sql.split(";");
            for (String q : queries) {
                if (!q.trim().isEmpty()) {
                    stmt.executeUpdate(q);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

