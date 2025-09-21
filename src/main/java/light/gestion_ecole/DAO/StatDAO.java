package light.gestion_ecole.DAO;

import light.gestion_ecole.Model.Eleve;

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
                "    COUNT(CASE WHEN handicap LIKE '%Audition%'     AND ( avertissement IS NULL OR avertissement <> 'renvoyé') THEN 1 END) AS nb_audition,\n" +
                "    COUNT(CASE WHEN handicap LIKE '%Vision%'       AND ( avertissement IS NULL OR avertissement <> 'renvoyé') THEN 1 END) AS nb_vision,\n" +
                "    COUNT(CASE WHEN handicap LIKE '%Intelectuel%'  AND (avertissement IS NULL OR  avertissement <> 'renvoyé')THEN 1 END) AS nb_intelectuel,\n" +
                "    COUNT(CASE WHEN handicap LIKE '%Psychomoteur%' AND (avertissement IS NULL OR avertissement <> 'renvoyé') THEN 1 END) AS nb_psychomoteur\n" +
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
    public static int[] getParticipation(Eleve eleve) {
        String sql = "SELECT attitude.participation as participation, COUNT(*) AS total " +
                "FROM attitude JOIN eleve ON eleve.ideleve = attitude.ideleve "+
                "WHERE eleve.ideleve = ? AND eleve.avertissement IS DISTINCT FROM 'renvoyé'  " +
                "GROUP BY participation;";
        int nbExcellente = 0;
        int nbMoyenne = 0;
        int nbJamais = 0;

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eleve.getIdeleve());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String participation = rs.getString("participation");
                    int total = rs.getInt("total");

                    if ("Excellente".equalsIgnoreCase(participation)) {
                        nbExcellente = total;
                    } else if ("Moyenne".equalsIgnoreCase(participation)) {
                        nbMoyenne = total;
                    }else if ("Jamais".equalsIgnoreCase(participation)) {
                        nbJamais = total;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        int[] note = new int[3];
        note[0] = nbExcellente;
        note[1] = nbMoyenne;
        note[2] = nbJamais;
        return note;
    }
    public static int[] getParticipationGlobal(String mat) {
        String sql = "SELECT attitude.participation as participation, COUNT(*) AS total " +
                "FROM attitude JOIN eleve ON eleve.ideleve = attitude.ideleve "+
                "WHERE eleve.anneescolaire = ? AND eleve.avertissement IS DISTINCT FROM 'renvoyé' " +
                "GROUP BY participation;";
        int nbExcellente = 0;
        int nbMoyenne = 0;
        int nbJamais = 0;

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, mat);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String participation = rs.getString("participation");
                    int total = rs.getInt("total");

                    if ("Excellente".equalsIgnoreCase(participation)) {
                        nbExcellente = total;
                    } else if ("Moyenne".equalsIgnoreCase(participation)) {
                        nbMoyenne = total;
                    }else if ("Jamais".equalsIgnoreCase(participation)) {
                        nbJamais = total;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        int[] note = new int[3];
        note[0] = nbExcellente;
        note[1] = nbMoyenne;
        note[2] = nbJamais;
        return note;
    }
    public static int[] getParticipationGlobalClasse(int idclasse, String annee) {
        String sql = "SELECT attitude.participation as participation, COUNT(*) AS total " +
                "FROM attitude JOIN eleve ON eleve.ideleve = attitude.ideleve "+
                "WHERE eleve.anneescolaire = ? AND eleve.avertissement IS DISTINCT FROM 'renvoyé' AND eleve.idclass = ? " +
                "GROUP BY participation;";
        int nbExcellente = 0;
        int nbMoyenne = 0;
        int nbJamais = 0;

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, annee);
            stmt.setInt(2, idclasse);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String participation = rs.getString("participation");
                    int total = rs.getInt("total");

                    if ("Excellente".equalsIgnoreCase(participation)) {
                        nbExcellente = total;
                    } else if ("Moyenne".equalsIgnoreCase(participation)) {
                        nbMoyenne = total;
                    }else if ("Jamais".equalsIgnoreCase(participation)) {
                        nbJamais = total;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        int[] note = new int[3];
        note[0] = nbExcellente;
        note[1] = nbMoyenne;
        note[2] = nbJamais;
        return note;
    }
    public static int[] getComportement(Eleve eleve) {
        String sql = "SELECT attitude.comportement, COUNT(*) AS total " +
                "FROM attitude JOIN eleve ON eleve.ideleve = attitude.ideleve " +
                "WHERE eleve.ideleve = ? AND eleve.avertissement IS DISTINCT FROM 'renvoyé' " +
                "GROUP BY comportement;";
        int nbExcellente = 0;
        int nbCorrect = 0;
        int nbMauvais = 0;
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eleve.getIdeleve());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String comportement = rs.getString("comportement");
                    int total = rs.getInt("total");

                    if ("Excellent".equalsIgnoreCase(comportement)) {
                        nbExcellente = total;
                    } else if ("Correct".equalsIgnoreCase(comportement)) {
                        nbCorrect = total;
                    } else if ("Mauvais".equalsIgnoreCase(comportement)) {
                        nbMauvais = total;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        int[] note = new int[3];
        note[0] = nbExcellente;
        note[1] = nbCorrect;
        note[2] = nbMauvais;
        return note;
    }
    public static int[] getComportementGlobal(String mat) {
        String sql = "SELECT attitude.comportement, COUNT(*) AS total " +
                "FROM attitude JOIN eleve ON eleve.ideleve = attitude.ideleve " +
                "WHERE eleve.anneescolaire = ? AND eleve.avertissement IS DISTINCT FROM 'renvoyé' " +
                "GROUP BY comportement;";
        int nbExcellente = 0;
        int nbCorrect = 0;
        int nbMauvais = 0;
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, mat);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String comportement = rs.getString("comportement");
                    int total = rs.getInt("total");

                    if ("Excellent".equalsIgnoreCase(comportement)) {
                        nbExcellente = total;
                    } else if ("Correct".equalsIgnoreCase(comportement)) {
                        nbCorrect = total;
                    } else if ("Mauvais".equalsIgnoreCase(comportement)) {
                        nbMauvais = total;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        int[] note = new int[3];
        note[0] = nbExcellente;
        note[1] = nbCorrect;
        note[2] = nbMauvais;
        return note;
    }
    public static int[] getComportementGlobalClasse(int idclasse, String anne) {
        String sql = "SELECT attitude.comportement, COUNT(*) AS total " +
                "FROM attitude JOIN eleve ON eleve.ideleve = attitude.ideleve " +
                "WHERE eleve.anneescolaire = ? AND eleve.avertissement IS DISTINCT FROM 'renvoyé' AND eleve.idclass = ? " +
                "GROUP BY comportement;";
        int nbExcellente = 0;
        int nbCorrect = 0;
        int nbMauvais = 0;
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, anne);
            stmt.setInt(2, idclasse);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String comportement = rs.getString("comportement");
                    int total = rs.getInt("total");

                    if ("Excellent".equalsIgnoreCase(comportement)) {
                        nbExcellente = total;
                    } else if ("Correct".equalsIgnoreCase(comportement)) {
                        nbCorrect = total;
                    } else if ("Mauvais".equalsIgnoreCase(comportement)) {
                        nbMauvais = total;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        int[] note = new int[3];
        note[0] = nbExcellente;
        note[1] = nbCorrect;
        note[2] = nbMauvais;
        return note;
    }
    public static int getRetards(String mat) {
        String sql = "SELECT SUM(a.retard) AS total " +
                "FROM attitude a " +
                "JOIN eleve e ON e.ideleve = a.ideleve " +
                "WHERE e.ideleve = ? AND e.avertissement IS DISTINCT FROM 'renvoyé' ";

        int note = 0;
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, mat);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                note = rs.getInt("total");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return note;

    }
    public static int getRetardsGlobal(String anneescolaire) {
        String sql = "SELECT SUM(a.retard) AS total " +
                "FROM attitude a " +
                "JOIN eleve e ON e.ideleve = a.ideleve " +
                "WHERE e.anneescolaire = ? AND e.avertissement IS DISTINCT FROM 'renvoyé' ";

        int note = 0;
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, anneescolaire);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                note = rs.getInt("total");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return note;

    }
    public static int getRetardsParClasse(int idClasse, String anne) {
        String sql = "SELECT SUM(a.retard) AS total " +
                "FROM attitude a " +
                "JOIN eleve e ON e.ideleve = a.ideleve " +
                "WHERE e.anneescolaire = ? AND e.avertissement IS DISTINCT FROM 'renvoyé' AND e.idclass = ?";

        int note = 0;
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, anne);
            stmt.setInt(2, idClasse);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                note = rs.getInt("total");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return note;

    }
    public static int getScoreAbsence(String mat) {
        String sql = "SELECT COUNT(*) AS total FROM pointage JOIN eleve ON eleve.ideleve = pointage.ideleve WHERE eleve.ideleve = ? AND eleve.avertissement IS DISTINCT FROM 'renvoyé'";

        int  note = 0;

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, mat);

            try (ResultSet rs = stmt.executeQuery()) {
                if(rs.next()){
                    note = rs.getInt("total");

                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return note;
    }
    public static int getScoreAbsenceGlobal(String mat) {
        String sql = "SELECT COUNT(*) AS total FROM pointage JOIN eleve ON eleve.ideleve = pointage.ideleve WHERE eleve.anneescolaire = ? AND eleve.avertissement IS DISTINCT FROM 'renvoyé' ";

        int  note = 0;

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, mat);

            try (ResultSet rs = stmt.executeQuery()) {
                if(rs.next()){
                    note = rs.getInt("total");

                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return note;
    }
    public static int getScoreAbsenceParClasse(int idclasse, String anne) {

        String sql = "SELECT COUNT(*) AS total FROM pointage JOIN eleve ON eleve.ideleve = pointage.ideleve WHERE eleve.anneescolaire = ? AND eleve.avertissement IS DISTINCT FROM 'renvoyé' AND eleve.idclass = ?";

        int  note = 0;

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, anne);
            stmt.setInt(2, idclasse);

            try (ResultSet rs = stmt.executeQuery()) {
                if(rs.next()){
                    note = rs.getInt("total");

                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return note;
    }
    public static int[] getNBrenvoyéHandic(int idClasse, String anne) throws SQLException {
        int[] nb = new int[2];

        String sql = "SELECT " +
                "COUNT(CASE WHEN avertissement = 'renvoyé' THEN 1 END) AS nb_renvoyes, " +
                "COUNT(CASE WHEN (handicap IS NOT NULL AND handicap <> '' AND handicap <> 'NULL') " +
                "              AND (avertissement IS NULL OR avertissement <> 'renvoyé') " +
                "         THEN 1 END) AS nb_handicap " +
                "FROM eleve " +
                "WHERE anneescolaire = ? AND idclass = ?";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, anne);
            stmt.setInt(2, idClasse);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    nb[0] = rs.getInt("nb_renvoyes");
                    nb[1] = rs.getInt("nb_handicap");
                }
            }
        }

        return nb;
    }
    public static double getMGclasse(int idClasse, String annee) throws SQLException {
        String sql = "SELECT SUM(enseigner.note) AS notEG, SUM(enseigner.coefficient) AS COEFF " +
                "FROM enseigner " +
                "JOIN eleve e ON e.ideleve = enseigner.ideleve " +
                "WHERE  e.avertissement IS DISTINCT FROM 'renvoyé' AND e.idclass = ? AND e.anneescolaire = ?";
        double notEG = 0;
        double coeff = 0;
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(2, annee);
            stmt.setInt(1, idClasse);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    notEG = rs.getDouble("notEG");
                    coeff = rs.getDouble("COEFF");
                }
            }
        }
        return (coeff == 0) ? 0 : Math.round((notEG / coeff) * 100.0) / 100.0;
    }
    public static double getMGElve(Eleve eleve) throws SQLException {
        List<String> anneescolaire = getAnnescolaire();
        String sql = "SELECT SUM(enseigner.note) AS notEG, SUM(enseigner.coefficient) AS COEFF " +
                "FROM enseigner " +
                "JOIN eleve e ON e.ideleve = enseigner.ideleve " +
                "WHERE e.anneescolaire = ? AND e.avertissement IS DISTINCT FROM 'renvoyé' AND e.ideleve = ?";
        double notEG = 0;
        double coeff = 0;
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eleve.getAnneescolaire());
            stmt.setString(2, eleve.getIdeleve());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    notEG = rs.getDouble("notEG");
                    coeff = rs.getDouble("COEFF");
                }
            }
        }
        return (coeff == 0) ? 0 : Math.round((notEG / coeff) * 100.0) / 100.0;
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
                    if(rs.getString("typeevaluation").equals("Interro 1")){
                        moyenne[0] = rs.getDouble("NOTE")/rs.getInt("COE");
                    }else if(rs.getString("typeevaluation").equals("Interro 2")){
                        moyenne[2] = rs.getDouble("NOTE")/rs.getInt("COE");
                    }else if(rs.getString("typeevaluation").equals("Interro 3")){
                        moyenne[4] = rs.getDouble("NOTE")/rs.getInt("COE");
                    }else if(rs.getString("typeevaluation").equals("Examen I")){
                        moyenne[1] = rs.getDouble("NOTE")/rs.getInt("COE");
                    }else if(rs.getString("typeevaluation").equals("Examen II")){
                        moyenne[3] = rs.getDouble("NOTE")/rs.getInt("COE");
                    }else if(rs.getString("typeevaluation").equals("Examen III")){
                        moyenne[5] = rs.getDouble("NOTE")/rs.getInt("COE");
                    }
                }
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return moyenne;
    }
    public static List<List<String>> getEXAMNATPardes(String anneescolaire) {
        List<List<String>> ex = new ArrayList<>();
        String sql = "SELECT COUNT(*) as nb, eleve.examennational as exam, classe.designation as classe " +
                "FROM eleve " +
                "JOIN classe ON eleve.idclass = classe.idclass " +
                "WHERE eleve.anneescolaire = ? AND eleve.examennational IS NOT NULL " +
                "GROUP BY eleve.examennational, classe.designation";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, anneescolaire);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    List<String> ligne = new ArrayList<>();
                    String classe = rs.getString("classe").trim();
                    ligne.add(classe);

                    boolean exam = rs.getBoolean("exam");
                    int nb = rs.getInt("nb");

                    if (exam) {
                        ligne.add("A-" + nb);
                    } else {
                        ligne.add("T-" + nb);
                    }

                    ex.add(ligne);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return ex;
    }
    public static int getNBmauvais(int idclasse, String anne){
        int nb = 0;
        String sql = "SELECT COUNT(*) as nb FROM attitude JOIN eleve ON eleve.ideleve = attitude.ideleve WHERE eleve.idclass = ? AND eleve.anneescolaire = ? AND attitude.comportement = 'Mauvais'";
        try(Connection conn = Database.connect();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idclasse);
            stmt.setString(2, anne);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                nb = rs.getInt("nb");

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return nb;
    }
    public static double[] getMGParTrimestreTri(int idclasse, String ann) {
        double[] moyenne = new double[6];
        String sql = "SELECT SUM(enseigner.note) as NOTE ,SUM(enseigner.coefficient) as COE, typeevaluation FROM enseigner JOIN eleve ON enseigner.ideleve = eleve.ideleve WHERE eleve.anneescolaire = ? AND eleve.idclass = ? GROUP BY typeevaluation ";
        try(Connection conn = Database.connect();
            PreparedStatement stmt = conn.prepareStatement(sql);){
            stmt.setString(1, ann);
            stmt.setInt(2, idclasse);
            try(ResultSet rs = stmt.executeQuery()){
                while(rs.next()){
                    if(rs.getString("typeevaluation").equals("Interro 1")){
                        moyenne[0] = rs.getDouble("NOTE")/rs.getInt("COE");
                    }else if(rs.getString("typeevaluation").equals("Interro 2")){
                        moyenne[2] = rs.getDouble("NOTE")/rs.getInt("COE");
                    }else if(rs.getString("typeevaluation").equals("Interro 3")){
                        moyenne[4] = rs.getDouble("NOTE")/rs.getInt("COE");
                    }else if(rs.getString("typeevaluation").equals("Examen I")){
                        moyenne[1] = rs.getDouble("NOTE")/rs.getInt("COE");
                    }else if(rs.getString("typeevaluation").equals("Examen II")){
                        moyenne[3] = rs.getDouble("NOTE")/rs.getInt("COE");
                    }else if(rs.getString("typeevaluation").equals("Examen III")){
                        moyenne[5] = rs.getDouble("NOTE")/rs.getInt("COE");
                    }
                }
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return moyenne;
    }
    public static double[] getMGParTrimestreTriEl(Eleve eleve) {

        double[] moyenne = new double[6];
        String sql = "SELECT SUM(enseigner.note) as NOTE ,SUM(enseigner.coefficient) as COE, typeevaluation FROM enseigner JOIN eleve ON enseigner.ideleve = eleve.ideleve WHERE eleve.ideleve LIKE ?  AND eleve.ideleve = ? GROUP BY typeevaluation ";
        try(Connection conn = Database.connect();
            PreparedStatement stmt = conn.prepareStatement(sql);){
            stmt.setString(1, "%-" + eleve.getAnneescolaire());
            stmt.setString(2, eleve.getIdeleve());
            try(ResultSet rs = stmt.executeQuery()){
                while(rs.next()){
                    if(rs.getString("typeevaluation").equals("Interro 1")){
                        moyenne[0] = rs.getDouble("NOTE")/rs.getInt("COE");
                    }else if(rs.getString("typeevaluation").equals("Interro 2")){
                        moyenne[2] = rs.getDouble("NOTE")/rs.getInt("COE");
                    }else if(rs.getString("typeevaluation").equals("Interro 3")){
                        moyenne[4] = rs.getDouble("NOTE")/rs.getInt("COE");
                    }else if(rs.getString("typeevaluation").equals("Examen I")){
                        moyenne[1] = rs.getDouble("NOTE")/rs.getInt("COE");
                    }else if(rs.getString("typeevaluation").equals("Examen II")){
                        moyenne[3] = rs.getDouble("NOTE")/rs.getInt("COE");
                    }else if(rs.getString("typeevaluation").equals("Examen III")){
                        moyenne[5] = rs.getDouble("NOTE")/rs.getInt("COE");
                    }
                }
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return moyenne;
    }
    public static String getExamenNatParClasse(String anneescolaire, String designation) {
        LocalDate today = LocalDate.now();
        String[] annees = anneescolaire.split("-");
        int anneeFin = Integer.parseInt(annees[1]);
        LocalDate finAnneeScolaire = LocalDate.of(anneeFin, Month.AUGUST, 20);

        if (today.isBefore(finAnneeScolaire)) {
            return "En cours";
        } else {
            String sql = "SELECT e.examennational, COUNT(*) AS nb " +
                    "FROM eleve e JOIN classe c ON c.idclass = e.idclass " +
                    "WHERE e.anneescolaire = ? AND c.designation = ? " +
                    "GROUP BY e.examennational";

            double afaka = 0;
            double tsafaka = 0;

            try (Connection conn = Database.connect();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, anneescolaire);
                stmt.setString(2, designation);

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
            if (total == 0) {
                return "0% (0/0)";
            } else {
                return String.format("%.2f%% (%d/%d)", (afaka / total) * 100, (int) afaka, (int) total);
            }
        }
    }

}
