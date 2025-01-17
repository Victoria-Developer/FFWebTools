package app.service.mail;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailService extends JavaMailSenderImpl {

    public EmailService() {
        this.setHost("smtp.gmail.com");
        this.setPort(587);

        this.setUsername("shalyginavictoria0@gmail.com");
        this.setPassword("pdchnspszzululdp");

        Properties props = this.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
    }

    public void sendSimpleMessage(String emailFrom, String senderName, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailFrom);
        message.setTo("shalyginavictoria0@gmail.com");
        message.setSubject("From: " + senderName + ", email is: " + emailFrom);
        message.setText(text);
        send(message);
    }

}