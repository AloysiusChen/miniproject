package vttp.batch5.ssf.miniproject.repositories;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import vttp.batch5.ssf.miniproject.models.FinancialMultiplier;

@Repository
public class FinancialMultiplierRepository {
    // Redis Key: Multiplier Hash
    private static final String MULTIPLIER_KEY = "profile:multiplier";

    @Autowired
    @Qualifier("redis-0")
    private RedisTemplate<String, String> template;

    public FinancialMultiplier getMultipliers() {
        Map<Object, Object> data = template.opsForHash().entries(MULTIPLIER_KEY);
        if (data.isEmpty()) {
            return null;
        }
        
        // Redis
        return new FinancialMultiplier(
            Integer.parseInt(data.get("emergencyMonths").toString()),
            Integer.parseInt(data.get("deathMultiplier").toString()),
            Integer.parseInt(data.get("ciMultiplier").toString()),
            Integer.parseInt(data.get("investmentPercentage").toString())
        );
    }

    // Save default multiplier values to Redis
    public void saveMultipliers(FinancialMultiplier multipliers) {
        Map<String, String> data = Map.of(
            "emergencyMonths", String.valueOf(multipliers.getEmergencyMonths()),
            "deathMultiplier", String.valueOf(multipliers.getDeathMultiplier()),
            "ciMultiplier", String.valueOf(multipliers.getCiMultiplier()),
            "investmentPercentage", String.valueOf(multipliers.getInvestmentPercentage())
        );
        template.opsForHash().putAll(MULTIPLIER_KEY, data);
    }
}