package vttp.batch5.ssf.miniproject.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vttp.batch5.ssf.miniproject.models.portfolio.Insurance;
import vttp.batch5.ssf.miniproject.models.portfolio.Investment;
import vttp.batch5.ssf.miniproject.repositories.UserPortfolioRepository;

@Service
public class UserPortfolioService {
    @Autowired
    private UserPortfolioRepository userPortfolioRepository;

    // Insurance
    public void addInsurance(String userId, Insurance insurance) {
        insurance.setId(UUID.randomUUID().toString());
        userPortfolioRepository.saveInsurance(userId, insurance);
    }

    public void updateInsurance(String userId, String insuranceId, Insurance insurance) {
        insurance.setId(insuranceId);
        userPortfolioRepository.saveInsurance(userId, insurance);
    }

    public Optional<Insurance> getInsurance(String userId, String insuranceId) {
        return userPortfolioRepository.findInsuranceById(userId, insuranceId);
    }

    public List<Insurance> getAllInsurance(String userId) {
        return userPortfolioRepository.findAllInsurance(userId);
    }

    public void deleteInsurance(String userId, String insuranceId) {
        userPortfolioRepository.deleteInsurance(userId, insuranceId);
    }

    //Investment
    public void addInvestment(String userId, Investment investment) {
        investment.setId(UUID.randomUUID().toString());
        userPortfolioRepository.saveInvestment(userId, investment);
    }

    public void updateInvestment(String userId, String investmentId, Investment investment) {
        investment.setId(investmentId);
        userPortfolioRepository.saveInvestment(userId, investment);
    }

    public Optional<Investment> getInvestment(String userId, String investmentId) {
        return userPortfolioRepository.findInvestmentById(userId, investmentId);
    }

    public List<Investment> getAllInvestments(String userId) {
        return userPortfolioRepository.findAllInvestment(userId);
    }

    public void deleteInvestment(String userId, String investmentId) {
        userPortfolioRepository.deleteInvestment(userId, investmentId);
    }
}