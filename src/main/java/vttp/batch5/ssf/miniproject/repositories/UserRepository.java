package vttp.batch5.ssf.miniproject.repositories;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import vttp.batch5.ssf.miniproject.models.user.User;
import vttp.batch5.ssf.miniproject.models.user.UserProfile;

@Repository
public class UserRepository {

    @Autowired
    @Qualifier("redis-0")
    private RedisTemplate<String, String> template;

    private static final String USER_PREFIX = "user:";

    // Save user into a Redis hash
    public void saveUser(User user) {
        template.opsForHash().put(USER_PREFIX + user.getId(), "email", user.getEmail());
        template.opsForHash().put(USER_PREFIX + user.getId(), "password", user.getPassword());
        // Index for email lookup
        template.opsForValue().set("email:" + user.getEmail(), user.getId());

        if (user.getProfile() != null) {
            user.getProfile().setUserId(user.getId()); 
            String profileKey = USER_PREFIX + user.getId() + ":profile";
            template.opsForHash().put(profileKey, "age", String.valueOf(user.getProfile().getAge()));
            template.opsForHash().put(profileKey, "dependentType", user.getProfile().getDependentType());
            template.opsForHash().put(profileKey, "monthlyExpenses", user.getProfile().getMonthlyExpenses().toString());
            template.opsForHash().put(profileKey, "monthlyIncomeBeforeCPF", user.getProfile().getMonthlyIncomeBeforeCPF().toString());
            template.opsForHash().put(profileKey, "monthlyIncomeAfterCPF", user.getProfile().getMonthlyIncomeAfterCPF().toString());
        }
    }

    public boolean existsByEmail(String email) {
        return template.hasKey("email:" + email);
    }

    public User findByEmail(String email) {
        String userId = template.opsForValue().get("email:" + email);
        if (userId == null) {
            return null;
        }

        User user = new User();
        user.setId(userId);
        user.setEmail((String) template.opsForHash().get(USER_PREFIX + userId, "email"));
        user.setPassword((String) template.opsForHash().get(USER_PREFIX + userId, "password"));

        // Add profile
        String profileKey = USER_PREFIX + userId + ":profile";
        Map<Object, Object> profileData = template.opsForHash().entries(profileKey);
        if (!profileData.isEmpty()) {
            UserProfile profile = new UserProfile();
            profile.setUserId(userId);
            profile.setAge(Integer.parseInt((String) profileData.get("age")));
            profile.setDependentType((String) profileData.get("dependentType"));
            profile.setMonthlyExpenses(new BigDecimal((String) profileData.get("monthlyExpenses")));
            profile.setMonthlyIncomeBeforeCPF(new BigDecimal((String) profileData.get("monthlyIncomeBeforeCPF")));
            profile.setMonthlyIncomeAfterCPF(new BigDecimal((String) profileData.get("monthlyIncomeAfterCPF")));
            user.setProfile(profile);
        }

        return user;
    }
}