package vttp.batch5.ssf.miniproject.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import vttp.batch5.ssf.miniproject.models.Quote;
import vttp.batch5.ssf.miniproject.models.user.User;
import vttp.batch5.ssf.miniproject.services.FinnhubService;
import vttp.batch5.ssf.miniproject.services.UserService;

@Controller
public class MarketController {
    @Autowired
    private FinnhubService finnhubService;

    @Autowired
    private UserService userService;

    @GetMapping("/market-pulse")
    public String getMarket(Model model, Authentication auth) {
        // Get current user 
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User user = userService.findByEmail(userDetails.getUsername());
        model.addAttribute("user", user);

        // Market data
        List<Quote> quotes = finnhubService.getQuotes();
        boolean isMarketOpen = finnhubService.isMarketOpen();
        LocalDateTime lastCacheUpdate = finnhubService.getLastCacheUpdate();
        String lastCacheUpdateFormatted = lastCacheUpdate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

        model.addAttribute("quotes", quotes);
        model.addAttribute("isMarketOpen", isMarketOpen);
        model.addAttribute("lastCacheUpdate", lastCacheUpdateFormatted);

        return "market-pulse";
    }
}