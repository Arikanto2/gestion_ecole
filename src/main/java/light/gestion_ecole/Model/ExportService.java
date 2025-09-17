package light.gestion_ecole.Model;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.activation.*;
import java.io.File;
import java.util.Properties;

public class ExportService {

    public static void envoyerParEmail(String destinataire) {
        try {
            File fichier = QueryLogger.getLocalFile().toFile();
            if (!fichier.exists() || fichier.length() == 0) {
                System.out.println("⚠️ Aucun fichier à envoyer.");
                return;
            }

            String expediteur = "fahazavanaandriantsoa@gmail.com";
            String motDePasse = "hhqn hnvy uttd dvgg";

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(expediteur, motDePasse);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(expediteur));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinataire));
            message.setSubject("Exportation des requêtes SQL");
            message.setText("Bonjour,\n\nVeuillez trouver ci-joint le fichier des requêtes SQL exportées our mettre à jour votre base de données.\n\nCordialement.");

            MimeBodyPart corpsTexte = new MimeBodyPart();
            corpsTexte.setText("Voici le fichier SQL généré par l'application.");

            MimeBodyPart pieceJointe = new MimeBodyPart();
            pieceJointe.attachFile(fichier);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(corpsTexte);
            multipart.addBodyPart(pieceJointe);

            message.setContent(multipart);

            Transport.send(message);
            System.out.println("📧 Email envoyé avec succès à " + destinataire);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
