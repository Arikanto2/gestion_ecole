package light.gestion_ecole.Model;

import javafx.stage.FileChooser;
import light.gestion_ecole.DAO.Database;


import java.io.*;


public class ImportWatcher {

    public static void importerFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélection de fichier SQL");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SQL", "*.sql"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            StringBuilder currentQuery = new StringBuilder();
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    line = line.trim();
                    if (line.startsWith("--") || line.startsWith("/*") || line.isEmpty()) {
                        continue;
                    }

                    currentQuery.append(line).append(" ");
                    if (line.endsWith(";")) {
                        String sql = currentQuery.toString().trim();
                        Database.execute(sql.substring(0, sql.length() - 1)); // enlever le ;
                        currentQuery.setLength(0);
                    }
                }
            }
            Notification.showSuccess("Base de données mise à jour !");
        } else {
            Notification.showWarning("Veuillez sélectionner un fichier.");
        }
    }


}
