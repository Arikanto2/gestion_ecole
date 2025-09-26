package light.gestion_ecole.DAO;

import light.gestion_ecole.Model.Eleve;
import light.gestion_ecole.Model.NoteT;
import light.gestion_ecole.Model.QueryLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NoteDAOT {
    public List<NoteT> getNotes(String id,String eval) {
        List<NoteT> notes = new ArrayList<NoteT>();
        String sql = "SELECT * FROM ENSEIGNER WHERE ideleve = ? AND typeevaluation = ?";
        try (Connection conn = Database.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1,id);
            stmt.setString(2,eval);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                notes.add(new NoteT(rs.getString("ideleve"),rs.getString("nummat"),rs.getInt("idprof"),
                        rs.getString("matiere"),rs.getDouble("note"),rs.getDouble("coefficient"),
                        rs.getString("commentaire"),rs.getString("typeevaluation")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return notes;
    }

    public double getTotalCoef(String id,String eval) {
        double coef = 0.0;
        String sql = "SELECT SUM(coefficient) FROM ENSEIGNER WHERE ideleve = ? AND typeevaluation = ?";
        try (Connection conn = Database.connect();PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1,id);
            stmt.setString(2,eval);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                coef = rs.getDouble(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return coef;
    }
    public double getTotalNote(String id,String eval) {
        double note = 0.0;
        String sql = "SELECT SUM(note) FROM ENSEIGNER WHERE ideleve = ? AND typeevaluation = ?";
        try (Connection conn = Database.connect();PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1,id);
            stmt.setString(2,eval);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                note = rs.getDouble(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return note;
    }
    public double getMoyenne(String id,String eval) {
        double moyenne = 0.0;
        String sql = "SELECT SUM(note)/SUM(coefficient) FROM ENSEIGNER WHERE ideleve = ? AND typeevaluation = ?";
        try (Connection conn = Database.connect();PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1,id);
            stmt.setString(2,eval);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                moyenne = rs.getDouble(1);
            }
        } catch (SQLException e) {
            throw  new RuntimeException(e);
        }
        return Math.round(moyenne * 100.0)/100.0;
    }
    public int getRang(String id, int idclass, String eval, String annee) {
        int rng = 0;
        String sql = "SELECT rang FROM " +
                "(SELECT e.ideleve,RANK() OVER (PARTITION BY en.typeevaluation,e.idclass ORDER BY (SUM(note*coefficient)/SUM(coefficient)) DESC)" +
                " AS rang FROM ELEVE e JOIN ENSEIGNER en ON e.ideleve = en.ideleve WHERE en.typeevaluation = ? AND e.idclass = ? AND e.anneescolaire = ? " +
                "GROUP BY e.ideleve,en.typeevaluation,e.idclass) AS classement WHERE ideleve = ? ";
        try (Connection conn = Database.connect();PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1,eval);
            stmt.setInt(2,idclass);
            stmt.setString(3,annee);
            stmt.setString(4,id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                rng = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rng;
    }
    public int getNbrEleve(int idclass, String annee) {
        int nbrEleve = 0;
        String sql = "SELECT COUNT(ideleve) FROM ELEVE WHERE idclass = ? AND anneescolaire = ?";
        try (Connection conn = Database.connect();PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1,idclass);
            stmt.setString(2,annee);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                nbrEleve = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return  nbrEleve;
    }
    public NoteT getOrCreateNoteForEleve(Eleve e,String typeevaluation,String mat) {
        String sql = "SELECT * FROM ENSEIGNER WHERE ideleve = ? AND typeevaluation = ? AND matiere = ?";
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, e.getIdeleve());
            stmt.setString(2, typeevaluation);
            stmt.setString(3, mat);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                NoteT note = new NoteT(e);
                note.setNote(rs.getDouble("note"));
                note.setCommentaire(rs.getString("commentaire"));
                return note;
            } else {
                return new NoteT(e);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return new NoteT(e);
        }
    }

    public void saveOrUpdate(NoteT note, String typeevaluation, String mat, int idprof, Double coeff, String id) throws SQLException {
        String check = "SELECT 1 FROM ENSEIGNER WHERE ideleve = ? AND typeevaluation = ? AND matiere = ?";
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(check)) {

            stmt.setString(1, note.getIdeleve());
            stmt.setString(2, typeevaluation);
            stmt.setString(3, mat);
            ResultSet rs = stmt.executeQuery();

            String commentaire = (note.getCommentaire() == null || note.getCommentaire().isEmpty())
                    ? generateCommentaire(note.getNote(), coeff)
                    : note.getCommentaire();

            if (rs.next()) {
                // UPDATE
                String sql = "UPDATE ENSEIGNER SET note = ?, commentaire = ? WHERE ideleve = ? AND typeevaluation = ? AND matiere = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setDouble(1, note.getNote());
                    ps.setString(2, commentaire);
                    ps.setString(3, note.getIdeleve());
                    ps.setString(4, typeevaluation);
                    ps.setString(5, mat);
                    int rows = ps.executeUpdate();
                    if (rows > 0) {
                        QueryLogger.append(
                                "UPDATE ENSEIGNER SET note = " + note.getNote() +
                                        ", commentaire = " + (commentaire == null ? "NULL" : "'" + commentaire + "'") +
                                        " WHERE nummat = '" + note.getNumnat() +
                                        "' AND typeevaluation = '" + typeevaluation +
                                        "' AND matiere = '" + mat + "';"
                        );
                    }
                }
            } else {
                // INSERT
                String sql = "INSERT INTO ENSEIGNER (ideleve,nummat,idprof, coefficient, note, commentaire, typeevaluation, matiere) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, id);
                    ps.setString(2, note.getNumnat());
                    ps.setInt(3, idprof);
                    ps.setDouble(4, coeff);
                    ps.setDouble(5, note.getNote() != null ? note.getNote() : 0.0);
                    ps.setString(6, commentaire);
                    ps.setString(7, typeevaluation);
                    ps.setString(8, mat);
                    int rows = ps.executeUpdate();
                    if (rows > 0) {
                        QueryLogger.append(
                                "INSERT INTO ENSEIGNER (ideleve, nummat, idprof, coefficient, note, commentaire, typeevaluation, matiere) VALUES (" +
                                        "'" + id + "', " +
                                        "'" + note.getNumnat() + "', " +
                                        idprof + ", " +
                                        coeff + ", " +
                                        (note.getNote() != null ? note.getNote() : 0.0) + ", " +
                                        (commentaire == null ? "NULL" : "'" + commentaire + "'") + ", " +
                                        "'" + typeevaluation + "', " +
                                        "'" + mat + "'" +
                                        ");"
                        );
                    }
                }
            }
        }
    }

    private String generateCommentaire(Double note, Double coefficient) {
        if (note == null || coefficient == null || coefficient == 0) {
            return "Non évalué";
        }
        double n = note / coefficient;

        if (n >= 18) return "Honorable";
        else if (n >= 16) return "Très Bien";
        else if (n >= 14) return "Bien";
        else if (n >= 12) return "Assez-Bien";
        else if (n >= 10) return "Passable";
        else return "Faible";
    }

    /////// pour les rangs dans classe //////////////////
    public List<NoteT> rang_classe (String evalu, int idclass, String annee) throws SQLException {
        List<NoteT> rang = new ArrayList<>();
        String sql = "SELECT c.rang, c.nomeleve, c.prenomeleve, c.moyenne FROM " +
                "(" +
                "SELECT e.ideleve, e.nomeleve,e.prenomeleve, " +
                "SUM(en.note) / SUM(en.coefficient) As moyenne, " +
                "RANK() OVER( " +
                " PARTITION BY en.typeevaluation, e.idclass " +
                " ORDER BY SUM(en.note * en.coefficient) / SUM(en.coefficient) DESC " +
                "  ) AS rang " +
                "FROM eleve e " +
                "JOIN enseigner en ON e.ideleve=en.ideleve " +
                "WHERE en.typeevaluation = ? " +
                " AND e.idclass = ? " +
                " AND e.anneescolaire = ? " +
                " GROUP BY e.ideleve, e.nomeleve, e.prenomeleve, en.typeevaluation, e.idclass " +
                ") As c " +
                "ORDER BY c.rang";

        try(Connection conn = Database.connect();){
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, evalu);
            stmt.setInt(2, idclass);
            stmt.setString(3, annee);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                rang.add(new NoteT(
                        rs.getInt("rang"),
                        rs.getString("nomeleve"),
                        rs.getString("prenomeleve"),
                        rs.getDouble("moyenne")
                ));
            }
        }
    return rang;
    }
    public List<String> getevaluation() throws SQLException {
        List<String> evaluation = new ArrayList<>();
        String sql = "SELECT DISTINCT typeevaluation FROM enseigner ORDER BY typeevaluation ASC";
        try(Connection conn = Database.connect();){
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                evaluation.add(rs.getString("typeevaluation"));
            }
        }
        return evaluation;
    }
    /// ///////////////////////////////////////////////////

}
