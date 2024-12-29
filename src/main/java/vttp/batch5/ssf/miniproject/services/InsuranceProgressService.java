package vttp.batch5.ssf.miniproject.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import vttp.batch5.ssf.miniproject.models.portfolio.Insurance;
import vttp.batch5.ssf.miniproject.models.InsuranceProgress;

@Service
public class InsuranceProgressService {
    public InsuranceProgress calculateDeathTpdProgress(List<Insurance> insurances, Map<String, BigDecimal> targets) {
        BigDecimal targetAmount = targets.get("deathTpdCoverage");
        BigDecimal currentAmount = calculateTotalCoverageByType(insurances, "Death & TPD");
        return new InsuranceProgress(targetAmount, currentAmount);
    }

    public InsuranceProgress calculateCiProgress(List<Insurance> insurances, Map<String, BigDecimal> targets) {
        BigDecimal targetAmount = targets.get("ciCoverage");
        BigDecimal currentAmount = calculateTotalCoverageByType(insurances, "Critical Illness");
        return new InsuranceProgress(targetAmount, currentAmount);
    }

    private BigDecimal calculateTotalCoverageByType(List<Insurance> insurances, String coverageType) {
        return insurances.stream()
            .filter(insurance -> coverageType.equals(insurance.getCoverageType()))
            .map(Insurance::getCoverageAmount)
            // Starts with BigDecimal.ZERO and sums up all coverage amounts in the stream
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
