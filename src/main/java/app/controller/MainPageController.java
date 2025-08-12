package app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainPageController {

    @RequestMapping("/")
    public String root() {
        return "index.csr.html";
    }

    @RequestMapping(value = "/{path:^(?!.*\\.).*$}")
    public String noExtension() {
        return "index.csr.html";
    }
}
