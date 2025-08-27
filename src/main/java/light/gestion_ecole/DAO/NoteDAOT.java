package light.gestion_ecole.DAO;

import light.gestion_ecole.Model.NoteT;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NoteDAOT {
    public List<NoteT> getNotes(String id) {
        List<NoteT> notes = new ArrayList<NoteT>();
        String sql = "SELECT * FROM ENSEIGNER WHERE ideleve = ?";
        try (Connection conn = Database.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1,id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                notes.add(new NoteT(rs.getString("ideleve"),rs.getString("nummat"),rs.getInt("idprof"),
                        rs.getString("matiere"),rs.getInt("note"),rs.getDouble("coefficient"),
                        rs.getString("commentaire"),rs.getString("typeevaluation")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return notes;
    }
}
