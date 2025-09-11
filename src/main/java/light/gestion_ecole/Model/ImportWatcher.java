package light.gestion_ecole.Model;


import light.gestion_ecole.DAO.Database;

import java.nio.file.*;
import java.io.IOException;

public class ImportWatcher {
    public static void startWatching() throws Exception {
        Path mailbox = MailBoxConfig.getMailboxPath();
        WatchService watchService = FileSystems.getDefault().newWatchService();
        mailbox.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

        System.out.println("👀 Surveillance du dossier : " + mailbox);

        while (true) {
            WatchKey key = watchService.take();
            for (WatchEvent<?> event : key.pollEvents()) {
                Path filename = (Path) event.context();
                String owner = System.getProperty("user.name");
                if (filename.toString().contains(owner)) {
                    continue; // ignorer
                }
                if (filename.toString().endsWith(".sql")) {
                    Path filePath = mailbox.resolve(filename);
                    System.out.println("📥 Nouveau fichier détecté : " + filePath);


                    try {
                        String sql = Files.readString(filePath);

                        // ⚠️ Exécuter sur la base locale
                        Database.execute(sql);

                        Files.move(filePath, mailbox.resolve("processed_" + filename));
                        System.out.println("✅ Importé et déplacé : " + filename);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            key.reset();
        }
    }
}
