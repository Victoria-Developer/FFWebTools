package mailService;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class JavaEmailSender extends JavaMailSenderImpl {

    public JavaEmailSender() {
        this.setHost("smtp.gmail.com");
        this.setPort(587);

        this.setUsername("beatokaine@gmail.com");
        this.setPassword("pdchnspszzululdp");

        Properties props = this.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
    }
}
