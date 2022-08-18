package webapp;

import mailService.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@ComponentScan(basePackages = {"mailService"})
public class ContactsController {
    @Autowired
    private EmailServiceImpl esi;

    @GetMapping("/contact")
    public String contactsPage() {
        return "contacts";
    }

    @PostMapping(value = "/contact", params = {"emailFrom", "senderName", "message"})
    public
    @ResponseBody
    void sendMail(@RequestParam(name = "emailFrom") String emailFrom,
                  @RequestParam(name = "senderName") String senderName,
                  @RequestParam(name = "message") String message) {
        esi.sendSimpleMessage(emailFrom, senderName, message);
    }
}
