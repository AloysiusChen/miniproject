package vttp.batch5.ssf.miniproject.configurations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import vttp.batch5.ssf.miniproject.models.recommendation.Recommendation;
import vttp.batch5.ssf.miniproject.models.recommendation.RecommendationLink;
import vttp.batch5.ssf.miniproject.services.FinnhubService;
import vttp.batch5.ssf.miniproject.services.RecommendationService;

@Configuration
public class AppConfig {
    @Autowired
    private RecommendationService recommendationService; 

    @Bean
    // A simple RestTemplate for global use
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    // This code runs when the app starts
    // Call the method to initialize ETF symbols
    public CommandLineRunner commandLineRunner(FinnhubService finnhubService) {
        return _ -> {
            finnhubService.initialiseETFSymbols();
            initializeRecommendations(recommendationService);
        };
    }

    private void initializeRecommendations(RecommendationService recommendationService) {
        // Health Insurance
        Recommendation medishield = new Recommendation();
        medishield.setId("DEFAULT_HEALTH_INSURANCE");
        medishield.setTitle("Is Your MediShield Life Enough? Here’s What You Need to Know");
        medishield.setContent(
                "Medical bills can be overwhelming. While MediShield Life provides basic coverage, an Integrated Shield Plan could give you more options for your healthcare needs and better peace of mind. Learn if upgrading makes sense for you.");
        medishield.setLinks(List.of(
                new RecommendationLink("Is MediShield Life Enough?",
                        "https://www.straitstimes.com/singapore/askst-is-medishield-life-enough-or-do-i-need-an-integrated-shield-plan"),
                new RecommendationLink("Compare IP Plans", "https://www.go.gov.sg/compareip")));
        recommendationService.createRecommendation(medishield, 50);

        // First Home
        Recommendation firstHDB = new Recommendation();
        firstHDB.setId("DEFAULT_FIRST_HDB");
        firstHDB.setTitle("How to Budget for Your First HDB Flat");
        firstHDB.setContent(
                "Ready for your first home? Planning your finances can make the process smoother. Use these tools and tips to help you budget for your HDB flat purchase.");
        firstHDB.setAgeMax("35");
        firstHDB.setLinks(List.of(
                new RecommendationLink("Working Out Your Flat Budget", "https://www.go.gov.sg/buyhdb")));
        recommendationService.createRecommendation(firstHDB, 40);

        // CPF Planning
        Recommendation cpfPlanning = new Recommendation();
        cpfPlanning.setId("DEFAULT_CPF_PLANNING");
        cpfPlanning.setTitle("Maximize Your CPF: Your Key to Retirement");
        cpfPlanning.setContent(
                "Your CPF accounts are powerful tools for retirement. Learn how to optimize them through top-ups and get more from your retirement savings. Small actions today can mean a big difference tomorrow.");
        cpfPlanning.setAgeMax("54");
        cpfPlanning.setLinks(List.of(
                new RecommendationLink("CPF Top-ups/ Transfer Guide", "https://www.go.gov.sg/cpftopups"),
                new RecommendationLink("CPF Retirement Payout Planner", "https://www.go.gov.sg/retireplan")));
        recommendationService.createRecommendation(cpfPlanning, 45);

        // Dependants' Protection
        Recommendation dpsInsurance = new Recommendation();
        dpsInsurance.setId("DEFAULT_DPS_INSURANCE");
        dpsInsurance.setTitle("Affordable Protection for Your Family with DPS");
        dpsInsurance.setContent(
                "The Dependants' Protection Scheme is an affordable term-life insurance that provides basic protection for your family. Find out how this low-cost safety net can help protect your loved ones.");
        dpsInsurance.setLinks(List.of(
                new RecommendationLink("About DPS", "https://www.go.gov.sg/dps")));
        recommendationService.createRecommendation(dpsInsurance, 15);

        // CareShield Life
        Recommendation careShield = new Recommendation();
        careShield.setId("DEFAULT_CARESHIELD");
        careShield.setTitle("Long-term Care Protection: CareShield Life");
        careShield.setContent(
                "You never know when you might need long-term care. CareShield Life provides monthly payouts if you need long-term care. Consider additional coverage for better protection.");
        careShield.setAgeMin("30");
        careShield.setLinks(List.of(
                new RecommendationLink("About CareShield Life", "https://www.go.gov.sg/csl"),
                new RecommendationLink("Supplement Your Coverage", "https://www.go.gov.sg/cslsup")));
        recommendationService.createRecommendation(careShield, 55);

        // Legacy Planning
        Recommendation legacyPlanning = new Recommendation();
        legacyPlanning.setId("DEFAULT_LEGACY_PLANNING");
        legacyPlanning.setTitle("Protect Your Loved Ones: Plan Your Legacy");
        legacyPlanning.setContent(
                "Ensure your assets go to the right people. Making a will and CPF nomination doesn't have to be complicated. Take these simple steps to protect your loved ones' future.");
        legacyPlanning.setDependents("CHILDREN,PARENTS,BOTH");
        legacyPlanning.setLinks(List.of(
                new RecommendationLink("Legacy Planning Guide", "https://www.go.gov.sg/legacyplan")));
        recommendationService.createRecommendation(legacyPlanning, 60);

        // Property Monetisation
        Recommendation propertyMonetise = new Recommendation();
        propertyMonetise.setId("DEFAULT_PROPERTY_MONETISE");
        propertyMonetise.setTitle("Unlock the Value of Your Property for Retirement");
        propertyMonetise.setContent(
                "Your home can be more than just a roof over your head. Discover ways to get extra retirement income from your property while maintaining a comfortable living arrangement.");
        propertyMonetise.setAgeMin("55");
        propertyMonetise.setLinks(List.of(
                new RecommendationLink("Property Monetisation Options", "https://www.go.gov.sg/monetise")));
        recommendationService.createRecommendation(propertyMonetise, 40);

        // SRS
        Recommendation srsAccount = new Recommendation();
        srsAccount.setId("DEFAULT_SRS");
        srsAccount.setTitle("Save Taxes and Build Your Retirement with SRS");
        srsAccount.setContent(
                "The Supplementary Retirement Scheme (SRS) helps you save on taxes while building your retirement nest egg. Learn how to make SRS work for you through simple investment strategies.");
        srsAccount.setLinks(List.of(
                new RecommendationLink("SRS Guide",
                        "https://www.iras.gov.sg/taxes/individual-income-tax/basics-of-individual-income-tax/special-tax-schemes/srs-contributions")));
        recommendationService.createRecommendation(srsAccount, 80);

        // Parents' Retirement
        Recommendation parentsRetirement = new Recommendation();
        parentsRetirement.setId("DEFAULT_PARENTS_RETIREMENT");
        parentsRetirement.setTitle("Boost Parents' Retirement: CPF Top-ups, Healthcare Financing & CareShield Upgrade");
        parentsRetirement.setContent("Help your parents strengthen their retirement planning with CPF top-ups, use your MediSave to cover their healthcare costs, and secure their long-term care with an upgrade to CareShield Life for better protection and peace of mind.");
        parentsRetirement.setLinks(List.of(
                new RecommendationLink("CPF Top up (Parents)", "https://www.cpf.gov.sg/member/growing-your-savings/saving-more-with-cpf/top-up-to-enjoy-higher-retirement-payouts/top-ups-for-loved-ones-employees-and-others"),
                new RecommendationLink("Using your MediSave savings", "https://www.cpf.gov.sg/member/healthcare-financing/using-your-medisave-savings"),
                new RecommendationLink("CareShield Life Upgrade", "https://www.cpf.gov.sg/member/infohub/educational-resources/why-you-should-consider-careshield-life-over-eldershield")));
        parentsRetirement.setDependents("PARENTS,BOTH");
        recommendationService.createRecommendation(parentsRetirement, 40);

        // CPF LIFE Planning
        Recommendation cpfLife = new Recommendation();
        cpfLife.setId("DEFAULT_CPF_LIFE");
        cpfLife.setTitle("Estimate Your Retirement Income with CPF LIFE");
        cpfLife.setContent(
                "How much monthly retirement income do you need? Use CPF's calculator to estimate your needs and understand how to reach your retirement goals through CPF LIFE.");
        cpfLife.setLinks(List.of(
                new RecommendationLink("CPF LIFE Payout Estimator", "https://www.cpf.gov.sg/member/tools-and-services/calculators/monthly-payout-estimator")));
        cpfLife.setAgeMin("55");
        recommendationService.createRecommendation(cpfLife, 50);
    }
}