package app.service.mail;

import app.configuration.ConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Properties;

@Service
public class EmailService extends JavaMailSenderImpl {

    @Autowired
    ConfigProperties configProp;

    @PostConstruct
    public void init(){
        this.setHost("smtp.gmail.com");
        this.setPort(587);

        this.setUsername(configProp.getConfigValue("spring.mail.username"));
        this.setPassword(configProp.getConfigValue("spring.mail.password"));

        Properties props = this.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
    }

    public void sendSimpleMessage(String emailFrom, String senderName, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailFrom);
        message.setTo(configProp.getConfigValue("spring.mail.receiver.username"));
        message.setSubject("From: " + senderName + ", email is: " + emailFrom);
        message.setText(text);
        send(message);
    }

}