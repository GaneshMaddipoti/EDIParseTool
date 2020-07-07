package in.jtechy.EDIParseTool.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String start() {
        return "home";
    }

    @RequestMapping("/home")
    public String home() {
        return "home";
    }

    @RequestMapping("/input")
    public String input() {
        return "input";
    }

    @RequestMapping("/result")
    public String dashboard() {
        return "result";
    }

}
