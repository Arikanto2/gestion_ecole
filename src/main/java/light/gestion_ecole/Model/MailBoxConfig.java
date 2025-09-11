package light.gestion_ecole.Model;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MailBoxConfig {
    public static Path getMailboxPath() {
        String userHome = System.getProperty("user.home");

        Path path1 = Paths.get(userHome, "Google Drive", "mailbox_sync");
        Path path2 = Paths.get(userHome, "My Drive", "mailbox_sync");
        Path path3 = Paths.get(userHome, "OneDrive", "Documents", "mailbox_sync");


        if (Files.exists(path1)) return path1;
        if (Files.exists(path2)) return path2;
        if (Files.exists(path3)) return path3;

        throw new RuntimeException("⚠️ Dossier mailbox_sync introuvable dans Google Drive !");
    }
}



