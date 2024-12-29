package vttp.batch5.ssf.miniproject.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import vttp.batch5.ssf.miniproject.models.recommendation.Recommendation;
import vttp.batch5.ssf.miniproject.models.user.User;
import vttp.batch5.ssf.miniproject.services.RecommendationService;
import vttp.batch5.ssf.miniproject.services.UserService;

@Controller
public class SmartMovesController {
    @Autowired
    private UserService userService;
    
    @Autowired
    private RecommendationService recommendationService;

    @GetMapping("/smart-moves")
    public String getSmartMoves(Model model, Authentication auth) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User user = userService.findByEmail(userDetails.getUsername());
        
        List<Recommendation> activeRecos = recommendationService.getActiveRecommendations(user.getProfile());
        List<Recommendation> completedRecos = recommendationService.getCompletedRecommendations(user.getProfile());
        List<Recommendation> notApplicableRecos = recommendationService.getNotApplicableRecommendations(user.getProfile());

        model.addAttribute("user", user);
        model.addAttribute("activeRecommendations", activeRecos);
        model.addAttribute("completedRecommendations", completedRecos);
        model.addAttribute("notApplicableRecommendations", notApplicableRecos);

        return "smart-moves";
    }
}