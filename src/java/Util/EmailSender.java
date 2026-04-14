package Util;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {

    private final String remitente = "soporte.dogix@gmail.com";
    private final String clave = "mgwhrixyoxvzcpuc";

    public boolean enviarToken(String destino, String token) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(remitente, clave);
                }
            });

            Message mensaje = new MimeMessage(session);
            mensaje.setFrom(new InternetAddress(remitente));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destino));
            mensaje.setSubject("Recuperacion de contraseña - Dogix");
            mensaje.setText("Tu token es: " + token);

            Transport.send(mensaje);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}