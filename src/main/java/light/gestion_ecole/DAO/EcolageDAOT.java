package light.gestion_ecole.DAO;

import light.gestion_ecole.Model.EcolageparmoiT;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EcolageDAOT {
    public List<EcolageparmoiT> getEcolages(String id){
        List<EcolageparmoiT> ecolages = new ArrayList<EcolageparmoiT>();
        String sql = "SELECT p.ideleve, p.nummat, p.idecolage, p.statut, e.ecolagemoi FROM PAYER p " +
                "JOIN ECOLAGEPARMOI e ON p.idecolage = e.idecolage WHERE ideleve = ?";
        try(Connection conn = Database.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1,id);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                ecolages.add(new EcolageparmoiT(rs.getString("ideleve"),rs.getString("nummat"),
                        rs.getInt("idecolage"),rs.getBoolean("statut"),rs.getDate("ecolagemoi")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ecolages;
    }
}
