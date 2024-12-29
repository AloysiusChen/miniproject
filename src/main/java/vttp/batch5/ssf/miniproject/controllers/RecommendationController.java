package vttp.batch5.ssf.miniproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vttp.batch5.ssf.miniproject.models.user.User;
import vttp.batch5.ssf.miniproject.services.RecommendationService;
import vttp.batch5.ssf.miniproject.services.UserService;

@Controller
@RequestMapping("/recommendation")
public class RecommendationController {
    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private UserService userService;

    @PostMapping("/status")
    public String updateRecommendationStatus(
            @RequestParam String recoId,
            @RequestParam String status,
            Authentication auth,
            RedirectAttributes ra) {

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User user = userService.findByEmail(userDetails.getUsername());

        try {
            recommendationService.updateRecommendationStatus(user.getId(), recoId, status);
            ra.addFlashAttribute("success", "Recommendation status updated successfully");
            return "redirect:/smart-moves";
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/smart-moves";
        }
    }
}