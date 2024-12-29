package vttp.batch5.ssf.miniproject.models.portfolio;

import java.util.ArrayList;
import java.util.List;

public class UserPortfolio {
    private String userId;
    private List<Insurance> insurancePolicies = new ArrayList<>();
    private List<Investment> investments = new ArrayList<>();

    // Helper Methods
    public void addInsurance(Insurance insurance) {
        this.insurancePolicies.add(insurance);
    }
    
    public void removeInsurance(String insuranceId) {
        this.insurancePolicies.removeIf(i -> i.getId().equals(insuranceId));
    }
    
    public void addInvestment(Investment investment) {
        this.investments.add(investment);
    }
    
    public void removeInvestment(String investmentId) {
        this.investments.removeIf(i -> i.getId().equals(investmentId));
    }

    // Constructors 
    public UserPortfolio() {
    }

    public UserPortfolio(String userId, List<Insurance> insurancePolicies, List<Investment> investments) {
        this.userId = userId;
        this.insurancePolicies = insurancePolicies;
        this.investments = investments;
    }

    public UserPortfolio(String userId) {
    }

    // Getters & Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Insurance> getInsurancePolicies() {
        return insurancePolicies;
    }

    public void setInsurancePolicies(List<Insurance> insurancePolicies) {
        this.insurancePolicies = insurancePolicies;
    }

    public List<Investment> getInvestments() {
        return investments;
    }

    public void setInvestments(List<Investment> investments) {
        this.investments = investments;
    }    
}

