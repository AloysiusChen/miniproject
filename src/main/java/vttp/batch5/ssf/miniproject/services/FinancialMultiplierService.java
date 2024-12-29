package vttp.batch5.ssf.miniproject.services;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vttp.batch5.ssf.miniproject.models.FinancialMultiplier;
import vttp.batch5.ssf.miniproject.models.user.UserProfile;
import vttp.batch5.ssf.miniproject.repositories.FinancialMultiplierRepository;

@Service
public class FinancialMultiplierService {
    @Autowired
    private FinancialMultiplierRepository multiplierRepo;

    // Method to calculate Emergency fund, Insurance and Investment targets
    public Map<String, BigDecimal> calculateTargets(UserProfile profile) {
        FinancialMultiplier multipliers = multiplierRepo.getMultipliers();

        if (multipliers == null) {
            multipliers = getDefaultMultipliers();
            multiplierRepo.saveMultipliers(multipliers);
        }

        Map<String, BigDecimal> targets = new HashMap<>();

        // Emergency Fund
        targets.put("emergencyFund", profile.getMonthlyExpenses()
                .multiply(BigDecimal.valueOf(multipliers.getEmergencyMonths())));

        // Skip insurance/investment if age >= 65
        if (profile.getAge() < 65) {
            BigDecimal annualIncome = profile.getMonthlyIncomeBeforeCPF()
                    .multiply(BigDecimal.valueOf(12));

            targets.put("deathTpdCoverage", annualIncome
                    .multiply(BigDecimal.valueOf(multipliers.getDeathMultiplier())));

            targets.put("ciCoverage", annualIncome
                    .multiply(BigDecimal.valueOf(multipliers.getCiMultiplier())));

            targets.put("monthlyInvestment", profile.getMonthlyIncomeAfterCPF()
                    .multiply(BigDecimal.valueOf(multipliers.getInvestmentPercentage()))
                    .divide(BigDecimal.valueOf(100)));
        }

        return targets;
    }

    private FinancialMultiplier getDefaultMultipliers() {
        return new FinancialMultiplier(6, 9, 4, 10);
    }
}
