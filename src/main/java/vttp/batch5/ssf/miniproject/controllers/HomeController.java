package vttp.batch5.ssf.miniproject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // GET /
    @GetMapping("/")
    public String showHomePage() {
        return "home";
    }

    // GET /api-docs
    @GetMapping("/api-docs")
    public String showApiDocs() {
        return "api-docs";
    }
}
