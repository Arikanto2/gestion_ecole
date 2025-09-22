package light.gestion_ecole.DAO;

import light.gestion_ecole.Model.AttitudeT;
import light.gestion_ecole.Model.QueryLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttitudeDAOT {
    public List<AttitudeT> getAttitudes(String id) throws SQLException {
        List<AttitudeT> attitudeTS = new ArrayList<>();
        String sql = "SELECT * FROM ATTITUDE WHERE ideleve = ?";
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

    public void InsertAttitude(AttitudeT attitudeT) throws SQLException {
        String sql = "INSERT INTO ATTITUDE (ideleve,nummat,dateattitude,comportement,participation,retard) VALUES (?,?,?,?,?,?)";
        try (Connection conn = Database.connect();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, attitudeT.getIdeleve());
            stmt.setString(2, attitudeT.getNummat());
            stmt.setDate(3, (Date) attitudeT.getDateattitudeAsDate());
            stmt.setString(4, attitudeT.getComportement());
            stmt.setString(5, attitudeT.getParticipation());
            stmt.setInt(6,attitudeT.getRetard());
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                QueryLogger.append("-- Insertion Attitude\n" +
                        "INSERT INTO ATTITUDE (ideleve, nummat, dateattitude, comportement, participation, retard) VALUES (" +
                        "'" + attitudeT.getIdeleve() + "', " +
                        "'" + attitudeT.getNummat() + "', " +
                        "'" + attitudeT.getDateattitudeAsDate() + "', " +
                        "'" + attitudeT.getComportement() + "', " +
                        "'" + attitudeT.getParticipation() + "', " +
                        attitudeT.getRetard() +
                        ");\n");
            }
        }
    }
}
