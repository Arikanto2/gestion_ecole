package light.gestion_ecole.Model;

import java.io.File;
import java.nio.file.Path;

public class MailBoxConfig {

    public static Path getMailboxPath() {
        File[] roots = File.listRoots(); // tous les lecteurs (C:\, D:\, etc.)
        for (File root : roots) {
            Path found = findMailbox(root);
            if (found != null) return found;
        }
        throw new RuntimeException("⚠️ Dossier mailbox_sync introuvable sur aucun disque !");
    }

    private static Path findMailbox(File folder) {
        if (folder == null || !folder.isDirectory() || !folder.canRead()) return null;

        File[] files = folder.listFiles();
        if (files == null) return null;

        for (File f : files) {
            if (f.isDirectory()) {
                if (f.getName().equalsIgnoreCase("mailbox_sync")) {
                    return f.toPath();
                } else {
                    Path found = findMailbox(f); // recherche récursive
                    if (found != null) return found;
                }
            }
        }
        return null;
    }
}
