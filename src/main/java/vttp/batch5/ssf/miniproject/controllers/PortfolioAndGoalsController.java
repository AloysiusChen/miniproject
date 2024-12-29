package vttp.batch5.ssf.miniproject.controllers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import vttp.batch5.ssf.miniproject.models.InsuranceProgress;
import vttp.batch5.ssf.miniproject.models.portfolio.Insurance;
import vttp.batch5.ssf.miniproject.models.portfolio.Investment;
import vttp.batch5.ssf.miniproject.models.user.User;
import vttp.batch5.ssf.miniproject.models.user.UserProfile;
import vttp.batch5.ssf.miniproject.services.FinancialMultiplierService;
import vttp.batch5.ssf.miniproject.services.InsuranceProgressService;
import vttp.batch5.ssf.miniproject.services.UserPortfolioService;
import vttp.batch5.ssf.miniproject.services.UserService;

@Controller
public class PortfolioAndGoalsController {
    @Autowired
    private UserService userService;
    
    @Autowired
    private FinancialMultiplierService financialService;
    
    @Autowired
    private UserPortfolioService portfolioService;

    @Autowired
    private InsuranceProgressService insuranceProgressService;

    @GetMapping("/portfolio-and-goals")
    public String getPortfolioAndGoals(Model model, Authentication auth) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User user = userService.findByEmail(userDetails.getUsername());
        
        Map<String, BigDecimal> targets = financialService.calculateTargets(user.getProfile());
        List<Insurance> insurances = portfolioService.getAllInsurance(user.getId());
        List<Investment> investments = portfolioService.getAllInvestments(user.getId());

        UserProfile profile = user.getProfile();
        // Age 65 and above does not show Insurance in Money Goals
        if (profile.getAge() < 65) {
            InsuranceProgress deathTpdProgress = insuranceProgressService.calculateDeathTpdProgress(insurances, targets);
            InsuranceProgress ciProgress = insuranceProgressService.calculateCiProgress(insurances, targets);

            model.addAttribute("deathTpdProgress", deathTpdProgress);
            model.addAttribute("ciProgress", ciProgress);
        }

        model.addAttribute("user", user);
        model.addAttribute("financialTargets", targets);
        model.addAttribute("insurances", insurances);
        model.addAttribute("investments", investments);

        return "portfolio-and-goals";
    }
}
