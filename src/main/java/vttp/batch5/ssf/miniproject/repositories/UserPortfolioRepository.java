package vttp.batch5.ssf.miniproject.repositories;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import vttp.batch5.ssf.miniproject.models.portfolio.Insurance;
import vttp.batch5.ssf.miniproject.models.portfolio.Investment;
import vttp.batch5.ssf.miniproject.models.portfolio.UserPortfolio;

@Repository
public class UserPortfolioRepository {
    private static final String INSURANCE_KEY = "insurance";
    private static final String INVESTMENT_KEY = "investment";
    private static final String PORTFOLIO_KEY = "portfolio";

    @Autowired
    @Qualifier("redis-0")
    private RedisTemplate<String, String> template;

    // Insurance methods
    public void saveInsurance(String userId, Insurance insurance) {
        try {
            String key = String.format("%s:%s:%s", INSURANCE_KEY, userId, insurance.getId());
            Map<String, String> fields = new HashMap<>();
            fields.put("planName", insurance.getPlanName());
            fields.put("company", insurance.getCompany());
            fields.put("coverageType", insurance.getCoverageType());
            fields.put("coverageAmount", insurance.getCoverageAmount().toString());
            fields.put("premiumAmount", insurance.getPremiumAmount().toString());
            fields.put("premiumFrequency", insurance.getPremiumFrequency());
            // Check if nextPaymentDate is null before calling toString
            if (insurance.getNextPaymentDate() != null) {
                fields.put("nextPaymentDate", insurance.getNextPaymentDate().toString());
            } else {
                fields.put("nextPaymentDate", "");
            }
            // Check if getCoverageEndDate is null before calling toString
            if (insurance.getCoverageEndDate() != null) {
                fields.put("coverageEndDate", insurance.getCoverageEndDate().toString());
            } else {
                fields.put("coverageEndDate", "");
            }

            template.opsForHash().putAll(key, fields);

            // Add to user's portfolio set
            String portfolioKey = String.format("%s:%s:insurance", PORTFOLIO_KEY, userId);
            template.opsForSet().add(portfolioKey, insurance.getId());
        } catch (Exception e) {
            throw new RuntimeException("Error saving insurance", e);
        }
    }

    public Optional<Insurance> findInsuranceById(String userId, String insuranceId) {
        String key = String.format("%s:%s:%s", INSURANCE_KEY, userId, insuranceId);
        Map<Object, Object> fields = template.opsForHash().entries(key);

        if (fields.isEmpty()) {
            return Optional.empty();
        }

        try {
            Insurance insurance = new Insurance();
            insurance.setId(insuranceId);
            insurance.setPlanName((String) fields.get("planName"));
            insurance.setCompany((String) fields.get("company"));
            insurance.setCoverageType((String) fields.get("coverageType"));
            insurance.setCoverageAmount(new BigDecimal((String) fields.get("coverageAmount")));
            insurance.setPremiumAmount(new BigDecimal((String) fields.get("premiumAmount")));
            insurance.setPremiumFrequency((String) fields.get("premiumFrequency"));
            String nextPaymentDateStr = (String) fields.get("nextPaymentDate");
            if (nextPaymentDateStr != null && !nextPaymentDateStr.isEmpty()) {
                insurance.setNextPaymentDate(LocalDate.parse(nextPaymentDateStr));
            } else {
                insurance.setNextPaymentDate(null);
            }

            String coverageEndDateStr = (String) fields.get("coverageEndDate");
            if (coverageEndDateStr != null && !coverageEndDateStr.isEmpty()) {
                insurance.setCoverageEndDate(LocalDate.parse(coverageEndDateStr));
            } else {
                insurance.setCoverageEndDate(null);
            }

            return Optional.of(insurance);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing insurance data", e);
        }
    }

    public List<Insurance> findAllInsurance(String userId) {
        String portfolioKey = String.format("%s:%s:insurance", PORTFOLIO_KEY, userId);
        Set<String> insuranceIds = template.opsForSet().members(portfolioKey);

        if (insuranceIds == null) {
            return new ArrayList<>();
        }

        return insuranceIds.stream()
                .map(id -> findInsuranceById(userId, id))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public void deleteInsurance(String userId, String insuranceId) {
        String key = String.format("%s:%s:%s", INSURANCE_KEY, userId, insuranceId);
        String portfolioKey = String.format("%s:%s:insurance", PORTFOLIO_KEY, userId);

        template.delete(key);
        template.opsForSet().remove(portfolioKey, insuranceId);
    }

    // Investment methods
    public void saveInvestment(String userId, Investment investment) {
        try {
            String key = String.format("%s:%s:%s", INVESTMENT_KEY, userId, investment.getId());
            Map<String, String> fields = new HashMap<>();
            fields.put("investmentType", investment.getInvestmentType());
            fields.put("investmentName", investment.getInvestmentName());
            fields.put("amountInvested", investment.getAmountInvested().toString());
            fields.put("units", investment.getUnits() != null ? investment.getUnits().toString() : "");
            fields.put("additionalComments",
                    investment.getAdditionalComments() != null ? investment.getAdditionalComments() : "");

            template.opsForHash().putAll(key, fields);

            // Add to user's portfolio set
            String portfolioKey = String.format("%s:%s:investment", PORTFOLIO_KEY, userId);
            template.opsForSet().add(portfolioKey, investment.getId());
        } catch (Exception e) {
            throw new RuntimeException("Error saving investment", e);
        }
    }

    public Optional<Investment> findInvestmentById(String userId, String investmentId) {
        String key = String.format("%s:%s:%s", INVESTMENT_KEY, userId, investmentId);
        Map<Object, Object> fields = template.opsForHash().entries(key);

        if (fields.isEmpty()) {
            return Optional.empty();
        }

        try {
            Investment investment = new Investment();
            investment.setId(investmentId);
            investment.setInvestmentType((String) fields.get("investmentType"));
            investment.setInvestmentName((String) fields.get("investmentName"));
            investment.setAmountInvested(new BigDecimal((String) fields.get("amountInvested")));
            
            String unitsStr = (String) fields.get("units");
            if (unitsStr != null && !unitsStr.isEmpty() && !unitsStr.equals("0")) {
                investment.setUnits(new BigDecimal(unitsStr));
            }
            
            investment.setAdditionalComments((String) fields.get("additionalComments"));

            return Optional.of(investment);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing investment data", e);
        }
    }

    public List<Investment> findAllInvestment(String userId) {
        String portfolioKey = String.format("%s:%s:investment", PORTFOLIO_KEY, userId);
        Set<String> investmentIds = template.opsForSet().members(portfolioKey);

        if (investmentIds == null) {
            return new ArrayList<>();
        }

        return investmentIds.stream()
                .map(id -> findInvestmentById(userId, id))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public void deleteInvestment(String userId, String investmentId) {
        String key = String.format("%s:%s:%s", INVESTMENT_KEY, userId, investmentId);
        String portfolioKey = String.format("%s:%s:investment", PORTFOLIO_KEY, userId);

        template.delete(key);
        template.opsForSet().remove(portfolioKey, investmentId);
    }

    // Portfolio summary methods
    public UserPortfolio getUserPortfolio(String userId) {
        UserPortfolio portfolio = new UserPortfolio(userId);
        portfolio.setInsurancePolicies(findAllInsurance(userId));
        portfolio.setInvestments(findAllInvestment(userId));
        return portfolio;
    }
}