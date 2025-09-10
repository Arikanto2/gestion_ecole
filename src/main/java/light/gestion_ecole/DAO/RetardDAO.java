package light.gestion_ecole.DAO;


import light.gestion_ecole.Model.AttitudeT;
import light.gestion_ecole.Model.Retard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RetardDAO {
    public static List<String> anne = StatDAO.getAnnescolaire();
    public static List<Retard> getAbsences() throws SQLException {
        List<Retard> retards = new ArrayList<>();
        String sql = "SELECT e.nummat, e.nomeleve, e.prenomeleve, SUM(a.retard) as NBR " +
                "FROM eleve e " +
                "LEFT JOIN attitude a ON e.nummat = a.nummat " +
                "WHERE e.anneescolaire = ? " +
                "GROUP BY e.nummat, e.nomeleve, e.prenomeleve " +
                "ORDER BY e.nummat";

        try (Connection conn = Database.connect();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, anne.get(0));
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    String numero = rs.getString("nummat");
                    String nom_prenom = rs.getString("nomeleve") + " " + rs.getString("prenomeleve");
                    int NBR = 0;
                    if(rs.getInt("NBR") > 0){
                        NBR = rs.getInt("NBR");
                    }
                    Retard re = new Retard(numero, nom_prenom, NBR);
                    retards.add(re);
                }
            }
        }
        return retards;
    }
    public int isElvedansAtt(String nummat) throws SQLException {
        String sql = "SELECT idattitude FROM attitude WHERE nummat = ? AND ideleve = ? ORDER BY dateattitude DESC LIMIT 1";
        try (Connection conn = Database.connect();
        PreparedStatement statement = conn.prepareStatement(sql);) {
            statement.setString(1, nummat);
            statement.setString(2, nummat + "-"+ anne.get(0));
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("idattitude");
                }else  {
                    return 0;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    public void ajout(String nummat, int a) throws SQLException {
        int idattitude = isElvedansAtt(nummat);
        if (idattitude != 0) {
            String sql = "UPDATE attitude SET retard = retard + ? WHERE idattitude = ?";
            try (Connection conn = Database.connect();
                 PreparedStatement statement = conn.prepareStatement(sql);) {
                statement.setInt(1,a );
                statement.setInt(2, idattitude );
                int resultat =  statement.executeUpdate();
                if (resultat > 0) {
                    System.out.println(1);
                }else  {
                    System.out.println(0);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else {
            AttitudeT at = new AttitudeT();
            at.setNummat(nummat);
            at.setIdeleve(nummat+"-"+ anne.get(0));
            at.setRetard(a);
            LocalDate dateAtt = LocalDate.now();
            java.sql.Date sqlDate = java.sql.Date.valueOf(dateAtt);
            at.setDateattitude(sqlDate);
            at.setComportement("");
            at.setParticipation("");
            AttitudeDAOT DAO = new AttitudeDAOT();
            DAO.InsertAttitude(at);
        }


    }


}
