package vttp.batch5.ssf.miniproject.models;

public class FinancialMultiplier {
    // Fields
    private int emergencyMonths;
    private int deathMultiplier;
    private int ciMultiplier; 
    private int investmentPercentage;

    // Constructors
    public FinancialMultiplier() {
    }
    
    public FinancialMultiplier(int emergencyMonths, int deathMultiplier, int ciMultiplier, int investmentPercentage) {
        this.emergencyMonths = emergencyMonths;
        this.deathMultiplier = deathMultiplier;
        this.ciMultiplier = ciMultiplier;
        this.investmentPercentage = investmentPercentage;
    }

    // Getters and Setters
    public int getEmergencyMonths() {
        return emergencyMonths;
    }
    public void setEmergencyMonths(int emergencyMonths) {
        this.emergencyMonths = emergencyMonths;
    }
    public int getDeathMultiplier() {
        return deathMultiplier;
    }
    public void setDeathMultiplier(int deathMultiplier) {
        this.deathMultiplier = deathMultiplier;
    }
    public int getCiMultiplier() {
        return ciMultiplier;
    }
    public void setCiMultiplier(int ciMultiplier) {
        this.ciMultiplier = ciMultiplier;
    }
    public int getInvestmentPercentage() {
        return investmentPercentage;
    }
    public void setInvestmentPercentage(int investmentPercentage) {
        this.investmentPercentage = investmentPercentage;
    }
}
