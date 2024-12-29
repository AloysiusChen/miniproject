package vttp.batch5.ssf.miniproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import vttp.batch5.ssf.miniproject.models.portfolio.Insurance;
import vttp.batch5.ssf.miniproject.models.portfolio.Investment;
import vttp.batch5.ssf.miniproject.models.user.User;
import vttp.batch5.ssf.miniproject.services.UserPortfolioService;
import vttp.batch5.ssf.miniproject.services.UserService;

@Controller
@RequestMapping("/portfolio")
public class PortfolioController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserPortfolioService portfolioService;

    // Insurance Management
    @GetMapping("/insurance/add")
    public String showAddInsuranceForm(Model model, Authentication auth) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User user = userService.findByEmail(userDetails.getUsername());
        model.addAttribute("user", user);
        model.addAttribute("insurance", new Insurance());
        return "portfolio/insurance-form";
    }

    @PostMapping("/insurance/add")
    public String addInsurance(
            @Valid @ModelAttribute Insurance insurance,
            BindingResult result,
            Authentication auth,
            RedirectAttributes ra,
            Model model) {

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User user = userService.findByEmail(userDetails.getUsername());
        model.addAttribute("user", user);

        if (result.hasErrors()) {
            return "portfolio/insurance-form";
        }

        portfolioService.addInsurance(user.getId(), insurance);
        ra.addFlashAttribute("success", "Insurance policy added successfully");
        return "redirect:/portfolio-and-goals";
    }

    @GetMapping("/insurance/{id}/edit")
    public String showEditInsuranceForm(@PathVariable String id, Model model, Authentication auth) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User user = userService.findByEmail(userDetails.getUsername());

        Insurance insurance = portfolioService.getInsurance(user.getId(), id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Insurance not found"));

        model.addAttribute("user", user);
        model.addAttribute("insurance", insurance);
        return "portfolio/insurance-form";
    }

    @PostMapping("/insurance/{id}/edit")
    public String updateInsurance(
            @PathVariable String id,
            @Valid @ModelAttribute Insurance insurance,
            BindingResult result,
            Authentication auth,
            RedirectAttributes ra,
            Model model) {

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User user = userService.findByEmail(userDetails.getUsername());
        model.addAttribute("user", user);

        if (result.hasErrors()) {
            return "portfolio/insurance-form";
        }

        portfolioService.updateInsurance(user.getId(), id, insurance);
        ra.addFlashAttribute("success", "Insurance policy updated successfully");
        return "redirect:/portfolio-and-goals";
    }

    @PostMapping("/insurance/{id}/delete")
    public String deleteInsurance(
            @PathVariable String id,
            Authentication auth,
            RedirectAttributes ra) {

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User user = userService.findByEmail(userDetails.getUsername());

        portfolioService.deleteInsurance(user.getId(), id);
        ra.addFlashAttribute("success", "Insurance policy deleted successfully");
        return "redirect:/portfolio-and-goals";
    }

    // Investment Management
    @GetMapping("/investment/add")
    public String showAddInvestmentForm(Model model, Authentication auth) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User user = userService.findByEmail(userDetails.getUsername());
        model.addAttribute("user", user);
        model.addAttribute("investment", new Investment());
        return "portfolio/investment-form";
    }

    @PostMapping("/investment/add")
    public String addInvestment(
            @Valid @ModelAttribute Investment investment,
            BindingResult result,
            Authentication auth,
            RedirectAttributes ra,
            Model model) {

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User user = userService.findByEmail(userDetails.getUsername());
        model.addAttribute("user", user);

        if (result.hasErrors()) {
            return "portfolio/investment-form";
        }

        portfolioService.addInvestment(user.getId(), investment);
        ra.addFlashAttribute("success", "Investment added successfully");
        return "redirect:/portfolio-and-goals";
    }

    @GetMapping("/investment/{id}/edit")
    public String showEditInvestmentForm(@PathVariable String id, Model model, Authentication auth) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User user = userService.findByEmail(userDetails.getUsername());

        Investment investment = portfolioService.getInvestment(user.getId(), id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Investment not found"));

        model.addAttribute("user", user);
        model.addAttribute("investment", investment);
        return "portfolio/investment-form";
    }

    @PostMapping("/investment/{id}/edit")
    public String updateInvestment(
            @PathVariable String id,
            @Valid @ModelAttribute Investment investment,
            BindingResult result,
            Authentication auth,
            RedirectAttributes ra,
            Model model) {

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User user = userService.findByEmail(userDetails.getUsername());
        model.addAttribute("user", user);

        if (result.hasErrors()) {
            return "portfolio/investment-form";
        }

        portfolioService.updateInvestment(user.getId(), id, investment);
        ra.addFlashAttribute("success", "Investment updated successfully");
        return "redirect:/portfolio-and-goals";
    }

    @PostMapping("/investment/{id}/delete")
    public String deleteInvestment(
            @PathVariable String id,
            Authentication auth,
            RedirectAttributes ra) {

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User user = userService.findByEmail(userDetails.getUsername());

        portfolioService.deleteInvestment(user.getId(), id);
        ra.addFlashAttribute("success", "Investment deleted successfully");
        return "redirect:/portfolio-and-goals";
    }
}