package vttp.batch5.ssf.miniproject.models.portfolio;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Insurance {
    private String id;

    @NotEmpty(message = "Plan name is required")
    @Size(max = 50, message = "Plan name cannot be longer than 50 characters")
    private String planName;

    @NotEmpty(message = "Company name is required")
    @Size(max = 25, message = "Company name cannot be longer than 25 characters")
    private String company;

    @NotEmpty(message = "Coverage type is required")
    private String coverageType;

    @NotNull(message = "Coverage amount is required")
    private BigDecimal coverageAmount;

    @NotNull(message = "Premium amount is required")
    @DecimalMin(value = "0.01", message = "Premium amount must be greater than 0")
    private BigDecimal premiumAmount;

    @NotEmpty(message = "Premium frequency is required")
    private String premiumFrequency;

    @Future(message = "Next payment date must be in the future")
    private LocalDate nextPaymentDate;

    @Future(message = "Coverage end date must be in the future")
    private LocalDate coverageEndDate;

    public Insurance() {
    }

    public Insurance(String id, String planName, String company, String coverageType, BigDecimal coverageAmount,
            BigDecimal premiumAmount, String premiumFrequency, LocalDate nextPaymentDate, LocalDate coverageEndDate) {
        this.id = id;
        this.planName = planName;
        this.company = company;
        this.coverageType = coverageType;
        this.coverageAmount = coverageAmount;
        this.premiumAmount = premiumAmount;
        this.premiumFrequency = premiumFrequency;
        this.nextPaymentDate = nextPaymentDate;
        this.coverageEndDate = coverageEndDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCoverageType() {
        return coverageType;
    }

    public void setCoverageType(String coverageType) {
        this.coverageType = coverageType;
    }

    public BigDecimal getCoverageAmount() {
        return coverageAmount;
    }

    public void setCoverageAmount(BigDecimal coverageAmount) {
        this.coverageAmount = coverageAmount;
    }

    public BigDecimal getPremiumAmount() {
        return premiumAmount;
    }

    public void setPremiumAmount(BigDecimal premiumAmount) {
        this.premiumAmount = premiumAmount;
    }

    public String getPremiumFrequency() {
        return premiumFrequency;
    }

    public void setPremiumFrequency(String premiumFrequency) {
        this.premiumFrequency = premiumFrequency;
    }

    public LocalDate getNextPaymentDate() {
        return nextPaymentDate;
    }

    public void setNextPaymentDate(LocalDate nextPaymentDate) {
        this.nextPaymentDate = nextPaymentDate;
    }

    public LocalDate getCoverageEndDate() {
        return coverageEndDate;
    }

    public void setCoverageEndDate(LocalDate coverageEndDate) {
        this.coverageEndDate = coverageEndDate;
    }
}
