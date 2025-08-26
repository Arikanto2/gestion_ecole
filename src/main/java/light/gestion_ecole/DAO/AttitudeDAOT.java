package light.gestion_ecole.DAO;

import light.gestion_ecole.Model.AttitudeT;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AttitudeDAOT {
    public List<AttitudeT> getAttitudes(String id) throws SQLException {
        List<AttitudeT> attitudeTS = new ArrayList<>();
        String sql = "SELECT * FROM ATTITUDE WHERE nummat = ?";
        try (Connection conn  = Database.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                attitudeTS.add(new AttitudeT(rs.getString("ideleve"),rs.getString("nummat"),rs.getString("participation"),
                        rs.getString("comportement"),rs.getDate("dateattitude"),rs.getInt("retard")));
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return attitudeTS;
    }
}
