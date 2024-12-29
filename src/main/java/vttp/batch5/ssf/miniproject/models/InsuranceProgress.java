package vttp.batch5.ssf.miniproject.models;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class InsuranceProgress {
    private BigDecimal targetAmount;
    private BigDecimal currentAmount;
    private BigDecimal shortfall;
    private int progressPercentage;

    public InsuranceProgress(BigDecimal targetAmount, BigDecimal currentAmount) {
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.shortfall = targetAmount.subtract(currentAmount);
        this.progressPercentage = calculateProgressPercentage();
    }
    
    // Helper Method: Calculate progress percentage
    private int calculateProgressPercentage() {
        if (targetAmount.compareTo(BigDecimal.ZERO) == 0) {
            return 0;
        }
        return currentAmount.multiply(BigDecimal.valueOf(100))
            .divide(targetAmount, 0, RoundingMode.DOWN)
            .min(BigDecimal.valueOf(100))
            .intValue();
    }

    // Getters only
    public BigDecimal getTargetAmount() {
        return targetAmount;
    }

    public BigDecimal getCurrentAmount() {
        return currentAmount;
    }

    public BigDecimal getShortfall() {
        return shortfall;
    }

    public int getProgressPercentage() {
        return progressPercentage;
    }    
}
