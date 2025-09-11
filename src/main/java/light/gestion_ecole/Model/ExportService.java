package light.gestion_ecole.Model;



import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExportService {

    public static void exporterEtVider() {
        try {
            String sqlContent = QueryLogger.readAll();
            if (sqlContent.isEmpty()) return;

            Path mailbox = MailBoxConfig.getMailboxPath();
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            Path targetFile = mailbox.resolve("sync_" + timestamp + ".sql");

            Files.writeString(targetFile, sqlContent);
            System.out.println("✅ Fichier exporté : " + targetFile);

            QueryLogger.clear();
            System.out.println("📄 Fichier local vidé.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
