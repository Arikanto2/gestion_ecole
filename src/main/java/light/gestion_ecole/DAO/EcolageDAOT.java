package light.gestion_ecole.DAO;

import javafx.scene.chart.PieChart;
import light.gestion_ecole.Model.EcolageparmoiT;
import light.gestion_ecole.Model.Eleve;
import light.gestion_ecole.Model.QueryLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EcolageDAOT {
    public List<EcolageparmoiT> getEcolages(String id){
        List<EcolageparmoiT> ecolages = new ArrayList<EcolageparmoiT>();
        String sql = "SELECT p.ideleve, p.nummat, p.idecolage, p.statut, p.ecolagemoi,e.moiseco FROM PAYER p " +
                "JOIN ECOLAGEPARMOI e ON p.idecolage = e.idecolage WHERE ideleve = ?";
        try(Connection conn = Database.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1,id);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                ecolages.add(new EcolageparmoiT(rs.getString("ideleve"),rs.getString("nummat"),
                        rs.getInt("idecolage"),rs.getBoolean("statut"),rs.getDate("ecolagemoi"),rs.getString("moiseco")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ecolages;
    }
    public void insertEcolageparmoiT(EcolageparmoiT e) {
        int id = selectIdeco(e.getMoiseco());
        String val = selectMoiseco(id);

        if (Objects.equals(e.getMoiseco(), val)) {
            String sql1 = "INSERT INTO PAYER (ideleve, nummat, idecolage, statut, ecolagemoi) VALUES (?, ?, ?, ?, ?)";
            try (Connection conn = Database.connect();
                 PreparedStatement stmt1 = conn.prepareStatement(sql1)) {
                stmt1.setString(1, e.getIdeleve());
                stmt1.setString(2, e.getNummat());
                stmt1.setInt(3, id);
                stmt1.setBoolean(4, e.isStatut());
                stmt1.setDate(5, (Date) e.getecolagemoiAsDate());

                int rows = stmt1.executeUpdate();

                if (rows > 0) {
                    QueryLogger.append("-- Paiement existant\n" +
                            "INSERT INTO PAYER (ideleve, nummat, idecolage, statut, ecolagemoi) VALUES (" +
                            "'" + e.getIdeleve() + "', " +
                            "'" + e.getNummat() + "', " +
                            id + ", " +
                            e.isStatut() + ", " +
                            "'" + e.getecolagemoiAsDate() + "'" +
                            ");\n");
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            String sql2 = "INSERT INTO ECOLAGEPARMOI (moiseco) VALUES (?)";
            try (Connection conn = Database.connect();
                 PreparedStatement stmt2 = conn.prepareStatement(sql2)) {
                stmt2.setString(1, e.getMoiseco());

                int rows1 = stmt2.executeUpdate();

                if (rows1 > 0) {
                    QueryLogger.append("-- Nouveau mois inséré\n" +
                            "INSERT INTO ECOLAGEPARMOI (moiseco) VALUES ('" + e.getMoiseco() + "');\n");
                }

                int id2 = selectIdeco(e.getMoiseco());

                String sql3 = "INSERT INTO PAYER (ideleve, nummat, idecolage, statut, ecolagemoi) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement stmt3 = conn.prepareStatement(sql3)) {
                    stmt3.setString(1, e.getIdeleve());
                    stmt3.setString(2, e.getNummat());
                    stmt3.setInt(3, id2);
                    stmt3.setBoolean(4, e.isStatut());
                    stmt3.setDate(5, (Date) e.getecolagemoiAsDate());

                    int rows2 = stmt3.executeUpdate();

                    if (rows2 > 0) {
                        QueryLogger.append("-- Paiement nouveau mois\n" +
                                "INSERT INTO PAYER (ideleve, nummat, idecolage, statut, ecolagemoi) VALUES (" +
                                "'" + e.getIdeleve() + "', " +
                                "'" + e.getNummat() + "', " +
                                id2 + ", " +
                                e.isStatut() + ", " +
                                "'" + e.getecolagemoiAsDate() + "'" +
                                ");\n");
                    }
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public int selectIdeco(String moi) {
        int res = 0;
        String sql = "SELECT idecolage FROM ECOLAGEPARMOI WHERE moiseco = ?";
        try (Connection conn = Database.connect();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1,moi);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                res = rs.getInt("idecolage");
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return res;
    }
    public String selectMoiseco(int id) {
        String res = "";
        String sql= "SELECT moiseco FROM ECOLAGEPARMOI WHERE idecolage = ?";
        try(Connection conn = Database.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1,id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                res = rs.getString("moiseco");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }
    public List<String> getMoisPayes(String id) {
        List<String> res = new ArrayList<>();
        String sql = "SELECT moiseco FROM ECOLAGEPARMOI e JOIN PAYER p ON e.idecolage= p.idecolage WHERE p.ideleve = ?";
        try (Connection conn = Database.connect();
        PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1,id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                res.add(rs.getString("moiseco"));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return res;
    }
    public List<Eleve> getListNonPayer() throws SQLException {
        List<Eleve> eleves = new ArrayList<>();
        String sql = "SELECT e.nummat,(e.nomeleve ||' ' || e.prenomeleve) as nomeleve," +
                " STRING_AGG(en.moiseco, '-' ORDER BY en.idecolage) as Moi_Non_Payer, e.anneescolaire, e.idclass FROM " +
                "ELEVE e CROSS JOIN ECOLAGEPARMOI en LEFT JOIN PAYER p ON p.ideleve = e.ideleve AND en.idecolage= p.idecolage" +
                " WHERE p.idecolage IS NULL GROUP BY e.nummat,e.nomeleve,e.prenomeleve, e.anneescolaire, e.idclass";
        try (Connection conn = Database.connect();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                eleves.add(new Eleve(rs.getString("nummat"),rs.getString("nomeleve"),
                        rs.getString("Moi_Non_Payer"),rs.getString("anneescolaire"),rs.getInt("idclass")));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return eleves;
    }
}
