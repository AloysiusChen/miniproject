package vttp.batch5.ssf.miniproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import vttp.batch5.ssf.miniproject.models.user.User;
import vttp.batch5.ssf.miniproject.services.UserService;

@Controller
public class DashboardController {
    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String getDashboard(Model model, Authentication auth) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User user = userService.findByEmail(userDetails.getUsername());
        model.addAttribute("user", user);
        return "dashboard";
    }
}