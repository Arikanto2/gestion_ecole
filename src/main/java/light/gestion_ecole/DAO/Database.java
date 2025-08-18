package light.gestion_ecole.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String URL = "jdbc:postgresql://localhost:5432/gestion_ecole";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Clap your hand1#";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
