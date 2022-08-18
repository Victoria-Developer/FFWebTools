package mailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl {

    @Autowired
    private JavaEmailSender mailSender;

    public void sendSimpleMessage(
            String emailFrom, String senderName, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailFrom);
        message.setTo("victoriyashalygina@yandex.ru");
        message.setSubject("From: " + senderName + ", email is: " + emailFrom);
        message.setText(text);
        mailSender.send(message);
    }

}