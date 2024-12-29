package vttp.batch5.ssf.miniproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import vttp.batch5.ssf.miniproject.models.user.User;
import vttp.batch5.ssf.miniproject.models.user.UserProfile;
import vttp.batch5.ssf.miniproject.services.UserService;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginForm(Authentication auth) {
        if (auth != null && auth.isAuthenticated() &&
                !(auth instanceof AnonymousAuthenticationToken)) {
            return "redirect:/dashboard";
        }
        return "login";
    }

    // GET /register
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        User user = new User();
        user.setProfile(new UserProfile());
        model.addAttribute("user", user);
        return "register";
    }

    // POST /register
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult,
            RedirectAttributes ra, Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        String registrationResult = userService.registerUser(user);

        if ("Email already exists".equals(registrationResult)) {
            ra.addFlashAttribute("error", registrationResult);
            return "redirect:/register";
        }

        ra.addFlashAttribute("success", registrationResult);
        return "redirect:/login";
    }
}