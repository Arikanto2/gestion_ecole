package light.gestion_ecole.DAO;

import light.gestion_ecole.Model.ParentT;

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
            pstmt.executeUpdate();
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
}
