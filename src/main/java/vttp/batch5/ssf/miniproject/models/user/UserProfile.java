package vttp.batch5.ssf.miniproject.models.user;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class UserProfile {
    private String userId;

    @Min(value = 18, message = "This app is designed for fresh grads and beyond. You must be 18+ to use this app.")
    @NotNull(message = "Age is required")
    private int age;

    @NotEmpty(message = "Dependent type is required")
    private String dependentType;

    @NotNull(message = "Monthly expenses is required")
    @DecimalMin(value = "0.01", message = "Monthly expenses must be greater than 0")
    private BigDecimal monthlyExpenses;

    @NotNull(message = "Monthly income before CPF is required")
    private BigDecimal monthlyIncomeBeforeCPF;

    @NotNull(message = "Monthly income after CPF is required")
    private BigDecimal monthlyIncomeAfterCPF;
    
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getDependentType() {
        return dependentType;
    }
    public void setDependentType(String dependentType) {
        this.dependentType = dependentType;
    }
    public BigDecimal getMonthlyExpenses() {
        return monthlyExpenses;
    }
    public void setMonthlyExpenses(BigDecimal monthlyExpenses) {
        this.monthlyExpenses = monthlyExpenses;
    }
    public BigDecimal getMonthlyIncomeBeforeCPF() {
        return monthlyIncomeBeforeCPF;
    }
    public void setMonthlyIncomeBeforeCPF(BigDecimal monthlyIncomeBeforeCPF) {
        this.monthlyIncomeBeforeCPF = monthlyIncomeBeforeCPF;
    }
    public BigDecimal getMonthlyIncomeAfterCPF() {
        return monthlyIncomeAfterCPF;
    }
    public void setMonthlyIncomeAfterCPF(BigDecimal monthlyIncomeAfterCPF) {
        this.monthlyIncomeAfterCPF = monthlyIncomeAfterCPF;
    }
}
