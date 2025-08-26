package light.gestion_ecole.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class StatDAO {

    public static List<String> getAnnescolaire() {
        String sql = "SELECT DISTINCT anneescolaire FROM eleve ORDER BY anneescolaire DESC;";
        List<String> list = new ArrayList<>();
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("anneescolaire"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public static int getNBEleve(String anneescolaire) {
        String sql = "SELECT COUNT(*) AS nb FROM eleve WHERE anneescolaire = ? AND avertissement IS DISTINCT FROM 'renvoyé'";
        int nb = 0;
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, anneescolaire);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    nb = rs.getInt("nb");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return nb;
    }
    public static int[] getNBEleveAnnee(String anneescolaire) {
        String sql = "SELECT \n" +
                "    COUNT(CASE WHEN genre = 'Fille'  AND (avertissement IS NULL OR avertissement <> 'renvoyé') THEN 1 END)  AS nb_filles,\n" +
                "    COUNT(CASE WHEN genre = 'Garçon' AND (avertissement IS NULL OR avertissement <> 'renvoyé') THEN 1 END)  AS nb_garcons,\n" +
                "    COUNT(CASE WHEN avertissement = 'renvoyé' THEN 1 END) AS nb_renvoyes,\n" +
                "    COUNT(CASE WHEN handicap = 'Audition'     AND ( avertissement IS NULL OR avertissement <> 'renvoyé') THEN 1 END) AS nb_audition,\n" +
                "    COUNT(CASE WHEN handicap = 'Vision'       AND ( avertissement IS NULL OR avertissement <> 'renvoyé') THEN 1 END) AS nb_vision,\n" +
                "    COUNT(CASE WHEN handicap = 'Intelectuel'  AND (avertissement IS NULL OR  avertissement <> 'renvoyé')THEN 1 END) AS nb_intelectuel,\n" +
                "    COUNT(CASE WHEN handicap = 'Psychomoteur' AND (avertissement IS NULL OR avertissement <> 'renvoyé') THEN 1 END) AS nb_psychomoteur\n" +
                "FROM eleve\n" +
                "WHERE anneescolaire = ?;\n";
        int[] nb = new int[7];
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, anneescolaire);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    nb[0] = rs.getInt("nb_filles");
                    nb[1] = rs.getInt("nb_garcons");
                    nb[2] = rs.getInt("nb_renvoyes");
                    nb[3] = rs.getInt("nb_audition");
                    nb[4] = rs.getInt("nb_vision");
                    nb[5] = rs.getInt("nb_intelectuel");
                    nb[6] = rs.getInt("nb_psychomoteur");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return nb;
    }



    public static double getMG(String anneescolaire) throws SQLException {
        String sql = "SELECT SUM(enseigner.note) AS notEG, SUM(enseigner.coefficient) AS COEFF " +
                "FROM enseigner " +
                "JOIN eleve e ON e.ideleve = enseigner.ideleve " +
                "WHERE e.ideleve LIKE ? AND e.avertissement IS DISTINCT FROM 'renvoyé'";
        double notEG = 0;
        double coeff = 0;
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%-" + anneescolaire);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    notEG = rs.getDouble("notEG");
                    coeff = rs.getDouble("COEFF");
                }
            }
        }
        return (coeff == 0) ? 0 : Math.round((notEG / coeff) * 100.0) / 100.0;
    }

    public static String getTauxReussite(String anneescolaire) {
        LocalDate today = LocalDate.now();
        String[] annees = anneescolaire.split("-");
        int anneeFin = Integer.parseInt(annees[1]);
        LocalDate finAnneeScolaire = LocalDate.of(anneeFin, Month.JUNE, 28);

        if (today.isBefore(finAnneeScolaire)) {
            return "En cours";
        } else {
            String sql = "SELECT COUNT(*) AS nbpassant FROM eleve WHERE anneescolaire = ? AND ispassant = true AND avertissement IS DISTINCT FROM 'renvoyé'";
            int nbpassant = 0;
            try (Connection conn = Database.connect();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, anneescolaire);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        nbpassant = rs.getInt("nbpassant");
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            int nbeleve = getNBEleve(anneescolaire);
            if (nbeleve == 0) return "0.00 % (0/0)";

            double tauxreussite = ((double) nbpassant / nbeleve) * 100;
            tauxreussite = Math.round(tauxreussite * 100.0) / 100.0;

            return String.format("%.2f%%-%d/%d", tauxreussite, nbpassant, nbeleve);
        }
    }

    public static int getParticipation(String anneescolaire) {
        String sql = "SELECT a.participation, COUNT(*) AS total " +
                "FROM attitude a " +
                "JOIN eleve e ON e.ideleve = a.ideleve " +
                "WHERE e.ideleve LIKE ? AND e.avertissement IS DISTINCT FROM 'renvoyé' " +
                "GROUP BY a.participation;";
        int nbExcellente = 0;
        int nbMoyenne = 0;

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%-" + anneescolaire);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String participation = rs.getString("participation");
                    int total = rs.getInt("total");

                    if ("Excellente".equalsIgnoreCase(participation)) {
                        nbExcellente = total;
                    } else if ("Moyenne".equalsIgnoreCase(participation)) {
                        nbMoyenne = total;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return nbExcellente * 2 + nbMoyenne;
    }

    public static int getComportement(String anneescolaire) {
        String sql = "SELECT a.comportement, COUNT(*) AS total " +
                "FROM attitude a " +
                "JOIN eleve e ON e.ideleve = a.ideleve " +
                "WHERE e.ideleve LIKE ? AND e.avertissement IS DISTINCT FROM 'renvoyé' " +
                "GROUP BY a.comportement;";
        int nbExcellente = 0;
        int nbCorrect = 0;

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%-" + anneescolaire);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String comportement = rs.getString("comportement");
                    int total = rs.getInt("total");

                    if ("Excellente".equalsIgnoreCase(comportement)) {
                        nbExcellente = total;
                    } else if ("Correct".equalsIgnoreCase(comportement)) {
                        nbCorrect = total;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return nbExcellente * 2 + nbCorrect;
    }

    public static int getRetards(String anneescolaire) {
        String sql = "SELECT a.retard, COUNT(*) AS total " +
                "FROM attitude a " +
                "JOIN eleve e ON e.ideleve = a.ideleve " +
                "WHERE e.ideleve LIKE ? AND e.avertissement IS DISTINCT FROM 'renvoyé' " +
                "GROUP BY a.retard;";
        int nbZero = 0;
        int nbUnTrois = 0;

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%-" + anneescolaire);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int retard = rs.getInt("retard");
                    int total = rs.getInt("total");

                    if (retard == 0) {
                        nbZero += total;
                    } else if (retard >= 1 && retard <= 3) {
                        nbUnTrois += total;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return nbZero * 3 + nbUnTrois;
    }

    public static int getScoreAbsence(String anneescolaire) {
        String sql = "SELECT e.ideleve, " +
                "CASE " +
                "    WHEN COUNT(p.ideleve) = 0 THEN 3 " +
                "    WHEN COUNT(p.ideleve) BETWEEN 1 AND 3 THEN 1 " +
                "    ELSE 0 " +
                "END AS score " +
                "FROM eleve e " +
                "LEFT JOIN pointage p ON p.ideleve = e.ideleve " +
                "WHERE e.ideleve LIKE ? AND e.avertissement IS DISTINCT FROM 'renvoyé' " +
                "GROUP BY e.ideleve;";

        int totalScore = 0;

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%-" + anneescolaire);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int score = rs.getInt("score");
                    totalScore += score;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return totalScore;
    }

    public static double getScoreAssiduiteMoyen(String anneescolaire) {
        int totalScore = getScoreAbsence(anneescolaire)
                + getRetards(anneescolaire)
                + getComportement(anneescolaire)
                + getParticipation(anneescolaire);

        int nEleve = getNBEleve(anneescolaire);
        if (nEleve == 0) return 0;

        return Math.round(((double) totalScore / nEleve)*100.0)/100.0;
    }
    public static String getExamenNat(String anneescolaire) {
        LocalDate today = LocalDate.now();
        String[] annees = anneescolaire.split("-");
        int anneeFin = Integer.parseInt(annees[1]);
        LocalDate finAnneeScolaire = LocalDate.of(anneeFin, Month.AUGUST, 20);
        if(today.isBefore(finAnneeScolaire)) {
            return "En cours";
        }else{
            String sql = "SELECT examennational, COUNT(*) AS nb " +
                    "FROM eleve " +
                    "WHERE anneescolaire = ? " +
                    "GROUP BY examennational;";

            double afaka = 0;
            double tsafaka = 0;

            try (Connection conn = Database.connect();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, anneescolaire);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        boolean val = rs.getBoolean("examennational");
                        if (!rs.wasNull()) {
                            int count = rs.getInt("nb");
                            if (val) {
                                afaka = count;
                            } else {
                                tsafaka = count;
                            }
                        }
                    }
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            double total = afaka + tsafaka;
            String pourcentage = (total == 0)
                    ? "0-0/0"
                    : String.format("%.2f%%-%d/%d", (afaka / total) * 100, (int)afaka, (int)total);

            return pourcentage;
        }
    }
    public static double[] getMGParTrimestre(String anneescolaire) {
        double[] moyenne = new double[6];
        String sql = "SELECT SUM(note) as NOTE ,SUM(coefficient) as COE, typeevaluation FROM enseigner WHERE ideleve LIKE ? GROUP BY typeevaluation ";
        try(Connection conn = Database.connect();
        PreparedStatement stmt = conn.prepareStatement(sql);){
            stmt.setString(1, "%-" + anneescolaire);
            try(ResultSet rs = stmt.executeQuery()){
                while(rs.next()){
                    if(rs.getString("typeevaluation").equals("Interro1")){
                        moyenne[0] = rs.getDouble("NOTE")/rs.getInt("COE");
                    }else if(rs.getString("typeevaluation").equals("Interro2")){
                        moyenne[2] = rs.getDouble("NOTE")/rs.getInt("COE");
                    }else if(rs.getString("typeevaluation").equals("Interro3")){
                        moyenne[4] = rs.getDouble("NOTE")/rs.getInt("COE");
                    }else if(rs.getString("typeevaluation").equals("Examen1")){
                        moyenne[1] = rs.getDouble("NOTE")/rs.getInt("COE");
                    }else if(rs.getString("typeevaluation").equals("Examen2")){
                        moyenne[3] = rs.getDouble("NOTE")/rs.getInt("COE");
                    }else if(rs.getString("typeevaluation").equals("Examen3")){
                        moyenne[5] = rs.getDouble("NOTE")/rs.getInt("COE");
                    }
                }
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return moyenne;
    }


}
