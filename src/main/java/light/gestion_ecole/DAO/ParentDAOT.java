package light.gestion_ecole.DAO;

import light.gestion_ecole.Model.ParentT;
import light.gestion_ecole.Model.QueryLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParentDAOT {
    public ParentT getParents(int id) throws SQLException {
        List<ParentT> parentTS = new ArrayList<ParentT>();
        String query = "select * from PARENT WHERE idparent = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)){
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return (new ParentT(rs.getInt("idparent"),rs.getString("nompere"),rs.getString("professionpere"),
                        rs.getString("nommere"),rs.getString("professionmere"),rs.getString("tuteur"),
                        rs.getString("professiontuteur"),rs.getString("contact"),rs.getString("emailparent")));
            }
        } catch (SQLException ex) {
            throw new SQLException(ex.getMessage());
        }
        return null;
    }
    public void insertParents(ParentT parentT) throws SQLException {
        String sql = "INSERT INTO PARENT (nompere, professionpere, nommere, professionmere,tuteur, professiontuteur, contact, emailparent)VALUES (?,?,?,?,?,?,?,?)";
        try (Connection conn = Database.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, parentT.getNompere());
            pstmt.setString(2, parentT.getProfessionpere());
            pstmt.setString(3, parentT.getNommere());
            pstmt.setString(4, parentT.getProfessionmere());
            pstmt.setString(5, parentT.getTuteur());
            pstmt.setString(6, parentT.getProfessiontuteur());
            pstmt.setString(7, parentT.getContact());
            pstmt.setString(8, parentT.getEmailparent());
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                QueryLogger.append("INSERT INTO PARENT (nompere, professionpere, nommere, professionmere, tuteur, professiontuteur, contact, emailparent) VALUES (" +
                        "'" + parentT.getNompere() + "', " +
                        "'" + parentT.getProfessionpere() + "', " +
                        "'" + parentT.getNommere() + "', " +
                        "'" + parentT.getProfessionmere() + "', " +
                        "'" + parentT.getTuteur() + "', " +
                        "'" + parentT.getProfessiontuteur() + "', " +
                        "'" + parentT.getContact() + "', " +
                        "'" + parentT.getEmailparent() + "'" +
                        ");");
            }
        }
    }
    public int getIdParents(String contact) throws SQLException {
        int res = 0;
        String query = "select idparent from PARENT where contact = ?";
        try (Connection conn = Database.connect();
        PreparedStatement pstmt = conn.prepareStatement(query)){
            pstmt.setString(1, contact);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                res = rs.getInt("idparent");
            }
        }
        return res;
    }
    public List<ParentT> getAllParents() throws SQLException {
        List<ParentT>parent = new ArrayList<>();
        String sql = "SELECT DISTINCT p.*, " +
                     "CASE WHEN c.designation IS NOT NULL THEN c.designation ELSE 'Aucune classe' END as classe " +
                     "FROM PARENT p " +
                     "LEFT JOIN ELEVE e ON p.idparent = e.idparent " +
                     "LEFT JOIN CLASSE c ON e.idclass = c.idclass " +
                     "ORDER BY p.idparent";
        try (Connection conn = Database.connect();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {
                parent.add(new ParentT(rs.getInt("idparent"),rs.getString("nompere"),
                        rs.getString("professionpere"),rs.getString("nommere"),rs.getString("professionmere"),
                        rs.getString("tuteur"),rs.getString("professiontuteur"),rs.getString("contact"),
                        rs.getString("emailparent"), rs.getString("classe")));
            }
        } catch (SQLException ex) {
            throw new SQLException(ex.getMessage());
        }
        return parent;
    }
    public void deleteParents(int id) throws SQLException {
        String query = "delete from PARENT where idparent = ?";
        try (Connection conn = Database.connect();
        PreparedStatement pstmt = conn.prepareStatement(query)){
            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                QueryLogger.append("DELETE FROM PARENT WHERE idparent = " + id + ";");
            }
        }
    }
    public void updateParents(ParentT parentT) throws SQLException {
        String sql = "update PARENT SET nompere = ?, professionpere = ?, nommere =?, professionmere =?," +
                "tuteur =?, professiontuteur =?, contact =?, emailparent = ? WHERE idparent = ?";
        try (Connection conn = Database.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, parentT.getNompere());
            pstmt.setString(2, parentT.getProfessionpere());
            pstmt.setString(3, parentT.getNommere());
            pstmt.setString(4, parentT.getProfessionmere());
            pstmt.setString(5, parentT.getTuteur());
            pstmt.setString(6, parentT.getProfessiontuteur());
            pstmt.setString(7, parentT.getContact());
            pstmt.setString(8, parentT.getEmailparent());
            pstmt.setInt(9, parentT.getIdparent());
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                QueryLogger.append("UPDATE PARENT SET nompere = '" + parentT.getNompere() +
                        "', professionpere = '" + parentT.getProfessionpere() +
                        "', nommere = '" + parentT.getNommere() +
                        "', professionmere = '" + parentT.getProfessionmere() +
                        "', tuteur = '" + parentT.getTuteur() +
                        "', professiontuteur = '" + parentT.getProfessiontuteur() +
                        "', contact = '" + parentT.getContact() +
                        "', emailparent = '" + parentT.getEmailparent() +
                        "' WHERE idparent = " + parentT.getIdparent() + ";");
            }
        }
    }
}
