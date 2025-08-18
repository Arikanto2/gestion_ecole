package light.gestion_ecole.DAO;


import light.gestion_ecole.Model.Eleve;
import light.gestion_ecole.DAO.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
                eleves.add(new Eleve(/*rs.getString("ideleve"),*/rs.getString("nummat"),
                        rs.getInt("idattitude"),rs.getInt("idclass"),
                        rs.getInt("idparent"),rs.getString("nomeleve"),
                        rs.getString("prenomeleve"),rs.getString("adresseeleve"),
                        rs.getDate("datenaiss"),rs.getString("genre"),
                        rs.getString("anneescolaire"),rs.getBoolean("ispassant"),
                        rs.getBoolean("examennational")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return eleves;
    }
}
