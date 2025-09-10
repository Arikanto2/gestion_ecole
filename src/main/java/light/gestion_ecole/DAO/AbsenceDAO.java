package light.gestion_ecole.DAO;

import light.gestion_ecole.Model.Absence;
import light.gestion_ecole.Model.Classe;
import light.gestion_ecole.Model.QueryLogger;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AbsenceDAO {
    public List<Absence> getAbsence(Classe classe) {
        List<Absence> absences = new ArrayList<>();
        String sql = "SELECT * FROM pointage p JOIN eleve e ON e.ideleve = p.ideleve WHERE e.idclass = ?  AND e.anneescolaire = ? ORDER BY p.dateabsence DESC";
        try(Connection conn = Database.connect();
            PreparedStatement ps = conn.prepareStatement(sql);){
            List<String> anneescolaire = StatDAO.getAnnescolaire();
            ps.setInt(1,classe.getIdClasse());
            ps.setString(2,anneescolaire.get(0));
            try(ResultSet rs = ps.executeQuery();){
                while(rs.next()){
                    Absence ab = new Absence();
                    ab.setNom(rs.getString("nomeleve"));
                    ab.setPrenom(rs.getString("prenomeleve"));
                    ab.setIdAbsence(rs.getInt("idpointage"));
                    ab.setNumero(rs.getString("nummat"));
                    ab.setDateAbsence(rs.getString("dateabsence"));
                    String dr = rs.getString("dateretour");
                    if (dr == null || dr.trim().isEmpty()) {
                        ab.setDateRetour("Encore absent(e)");
                    } else {
                        ab.setDateRetour(dr);
                    }
                    ab.setMotif(rs.getString("motif"));
                    absences.add(ab);
                }
            }

        }catch (Exception e){
            System.out.println("Erreur connexion");
        }
        return absences;
    }
    public void ajouterAbsence(Absence ab) throws SQLException {
        String sql = "INSERT INTO pointage (ideleve,nummat,dateabsence,dateretour,motif) VALUES(?,?,?,?,?)";
        List<String> anneescolaire = StatDAO.getAnnescolaire();
        try(Connection conn = Database.connect();
            PreparedStatement ps = conn.prepareStatement(sql);){

            ps.setString(1, ab.getNumero() + "-" + anneescolaire.get(0));
            ps.setString(2, ab.getNumero());
            ps.setDate(3, Date.valueOf(ab.getDateAbsence()));

            if (ab.getDateRetour() != null) {
                ps.setDate(4, Date.valueOf(ab.getDateRetour()));
            } else {
                ps.setNull(4, Types.DATE);
            }

            if (ab.getMotif() != null && !ab.getMotif().isEmpty()) {
                ps.setString(5, ab.getMotif());
            } else {
                ps.setNull(5, Types.VARCHAR);
            }

            int lignesAffectees = ps.executeUpdate();
            if (lignesAffectees > 0) {
                System.out.println("Insertion réussie !");
                // Enregistrer la requête pour synchronisation
                QueryLogger.append("INSERT INTO pointage (ideleve,nummat,dateabsence,dateretour,motif) VALUES('"
                        + ab.getNumero() + "-" + anneescolaire.get(0) + "','"
                        + ab.getNumero() + "','"
                        + ab.getDateAbsence() + "','"
                        + (ab.getDateRetour() != null ? ab.getDateRetour() : "") + "','"
                        + (ab.getMotif() != null ? ab.getMotif() : "") + "')");
            } else {
                System.out.println("Erreur : aucune ligne insérée !");
            }
        }
    }



    public static boolean isMatExiste(String nummat) throws SQLException {
        String sql = "SELECT ideleve FROM eleve WHERE nummat = ?";

        try(Connection conn = Database.connect();
            PreparedStatement ps = conn.prepareStatement(sql);){
            ps.setString(1,nummat);
            return  ps.executeQuery().next() && isElevePresent(nummat);



        }
    }
    public static boolean isElevePresent(String nummat) throws SQLException {
        String sql = "SELECT dateretour FROM pointage WHERE nummat = ? ORDER BY dateretour DESC LIMIT 1";

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nummat);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("dateretour") != null;
                }
            }
        }
        return true;
    }


    public void modifierAbsence(Absence ab) throws SQLException, IOException {
        String sql = "UPDATE  pointage SET dateabsence = ?, dateretour = ?, motif = ? WHERE idpointage = ?";
        try(Connection conn = Database.connect();
            PreparedStatement ps = conn.prepareStatement(sql);){


            ps.setInt(4, ab.getIdAbsence());
            ps.setDate(1, Date.valueOf(ab.getDateAbsence()));

            if (ab.getDateRetour() != null) {
                ps.setDate(2, Date.valueOf(ab.getDateRetour()));
            } else {
                ps.setNull(2, Types.DATE);
            }

            if (ab.getMotif() != null && !ab.getMotif().isEmpty()) {
                ps.setString(3, ab.getMotif());
            } else {
                ps.setNull(3, Types.VARCHAR);
            }

            int lignesAffectees = ps.executeUpdate();
            if (lignesAffectees > 0) {
                System.out.println("Modification Réussie !");
            } else {
                System.out.println("Erreur : aucune ligne insérée !");
            }
        }
    }
    public static void supprimerAbsence(int Abs) {
        String sql = "DELETE FROM pointage WHERE idpointage = ?";
        try (Connection conn = Database.connect();
        PreparedStatement ps = conn.prepareStatement(sql);){
            ps.setInt(1, Abs);
            int lignesAffectees = ps.executeUpdate();
            if (lignesAffectees > 0) {
                System.out.println("Supression effectué");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
