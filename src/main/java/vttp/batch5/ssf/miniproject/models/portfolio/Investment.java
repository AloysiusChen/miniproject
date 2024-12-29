package vttp.batch5.ssf.miniproject.models.portfolio;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Investment {
    private String id;

    @NotEmpty(message = "Investment name is required")
    @Size(min = 3, max = 50, message = "Investment name must be between 3 and 50 characters")
    private String investmentName;
    
    @NotEmpty(message = "Investment type is required")
    private String investmentType;

    @NotNull(message = "Amount invested is required")
    @Min(value = 0, message = "Amount invested must be a positive value")
    private BigDecimal amountInvested;

    @Min(value = 0, message = "Units must be a positive value")
    private BigDecimal units;

    @Size(max = 500, message = "Additional comments cannot be longer than 500 characters")
    private String additionalComments;
    
    public Investment() {
    }

    public Investment(String id, String investmentType, String investmentName, BigDecimal amountInvested, BigDecimal units,
            String additionalComments) {
        this.id = id;
        this.investmentType = investmentType;
        this.investmentName = investmentName;
        this.amountInvested = amountInvested;
        this.units = units;
        this.additionalComments = additionalComments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInvestmentType() {
        return investmentType;
    }

    public void setInvestmentType(String investmentType) {
        this.investmentType = investmentType;
    }

    public String getInvestmentName() {
        return investmentName;
    }

    public void setInvestmentName(String investmentName) {
        this.investmentName = investmentName;
    }

    public BigDecimal getAmountInvested() {
        return amountInvested;
    }

    public void setAmountInvested(BigDecimal amountInvested) {
        this.amountInvested = amountInvested;
    }

    public BigDecimal getUnits() {
        return units;
    }

    public void setUnits(BigDecimal units) {
        this.units = units;
    }

    public String getAdditionalComments() {
        return additionalComments;
    }

    public void setAdditionalComments(String additionalComments) {
        this.additionalComments = additionalComments;
    }
}
