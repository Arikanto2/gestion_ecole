package light.gestion_ecole.Model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class QueryLogger {

    private static final Path localFile = Paths.get(System.getProperty("user.home"), "changes.sql");

    static {

        try {
            if (!Files.exists(localFile)) {
                Files.createFile(localFile);
                System.out.println("📄 Fichier changes.sql créé ici : " + localFile.toAbsolutePath());
            } else {
                System.out.println("📄 Fichier changes.sql déjà existant ici : " + localFile.toAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void append(String sql) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String line = "-- " + timestamp + System.lineSeparator() + sql + ";" + System.lineSeparator();
            Files.writeString(localFile, line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            System.out.println("✏️ Requête ajoutée dans " + localFile.toAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readAll() {
        try {
            if (!Files.exists(localFile)) return "";
            return Files.readString(localFile);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void clear() {
        try {
            Files.deleteIfExists(localFile);
            Files.createFile(localFile);
            System.out.println("🧹 Fichier changes.sql vidé et recréé : " + localFile.toAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Path getLocalFile() {
        return localFile;
    }
}
