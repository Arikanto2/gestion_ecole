package light.gestion_ecole.Model;

import java.nio.file.*;
import java.io.File;

public class MailBoxConfig {
    public static Path getMailboxPath() {
        File[] roots = File.listRoots(); // liste tous les lecteurs disponibles (C:\, D:\, E:\, ...)
        for (File root : roots) {
            Path path = Paths.get(root.getAbsolutePath(), "Google Drive", "mailbox_sync");
            if (Files.exists(path)) return path;

            path = Paths.get(root.getAbsolutePath(), "Mon Drive", "mailbox_sync");
            if (Files.exists(path)) return path;
        }
        throw new RuntimeException("⚠️ Dossier mailbox_sync introuvable sur aucun disque !");
    }
}
