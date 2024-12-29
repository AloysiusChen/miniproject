package vttp.batch5.ssf.miniproject.services;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vttp.batch5.ssf.miniproject.models.recommendation.Recommendation;
import vttp.batch5.ssf.miniproject.models.user.UserProfile;
import vttp.batch5.ssf.miniproject.repositories.RecommendationRepository;

@Service
public class RecommendationService {
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_COMPLETED = "COMPLETED";
    public static final String STATUS_NOT_APPLICABLE = "NOT_APPLICABLE";

    @Autowired
    private RecommendationRepository recoRepo;

    public List<Recommendation> getRecommendationsByStatus(UserProfile profile, String status) {

        Map<String, String> userStatuses = recoRepo.getUserRecommendationStatuses(profile.getUserId());
        List<Recommendation> allRecos = recoRepo.getAllRecommendations();

        return allRecos.stream()
                .filter(reco -> matchesProfile(reco, profile))
                .filter(reco -> status.equals(userStatuses.getOrDefault(reco.getId(), STATUS_ACTIVE)))
                .collect(Collectors.toList());
    }

    // Business logic for profile matching
    private boolean matchesProfile(Recommendation reco, UserProfile profile) {
        // Check if user's age falls within recommendation's age range
        boolean ageMatch = true;
        if (reco.getAgeMin() != null) {
            ageMatch = profile.getAge() >= Integer.parseInt(reco.getAgeMin());
        }
        if (reco.getAgeMax() != null && ageMatch) {
            ageMatch = profile.getAge() <= Integer.parseInt(reco.getAgeMax());
        }
    
        // Check if user's dependent status matches recommendation's requirements
        boolean dependentMatch = true;
        if (reco.getDependents() != null) {
            String userDependentType = profile.getDependentType();
    
            // Legacy planning is shown to all users above 35
            if (reco.getId().equals("DEFAULT_LEGACY_PLANNING") && profile.getAge() >= 35) {
                dependentMatch = true;
            } else if ("NONE".equals(userDependentType)) {
                // If user has no dependents, only show recommendations for "NONE"
                dependentMatch = "NONE".equals(reco.getDependents());
            } else if (!"NONE".equals(userDependentType)) {
                // For users with dependents, check if they match the recommendation
                String[] recoDependents = reco.getDependents().split(",");
                dependentMatch = false;
                for (String recoDep : recoDependents) {
                    if (recoDep.equals(userDependentType)) {
                        dependentMatch = true;
                        break;
                    }
                }
            }
        }
    
        return ageMatch && dependentMatch;
    }

    // Convenience methods
    public List<Recommendation> getActiveRecommendations(UserProfile profile) {
        return getRecommendationsByStatus(profile, STATUS_ACTIVE);
    }

    public List<Recommendation> getCompletedRecommendations(UserProfile profile) {
        return getRecommendationsByStatus(profile, STATUS_COMPLETED);
    }

    public List<Recommendation> getNotApplicableRecommendations(UserProfile profile) {
        return getRecommendationsByStatus(profile, STATUS_NOT_APPLICABLE);
    }

    // Update recommendation status for User
    public void updateRecommendationStatus(String userId, String recoId, String status) {
        if (!Arrays.asList(STATUS_ACTIVE, STATUS_COMPLETED, STATUS_NOT_APPLICABLE).contains(status)) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }

        recoRepo.updateUserRecommendationStatus(userId, recoId, status);
    }

    // Create/ Delete Recommedation, Update Recommendation Order
    public void createRecommendation(Recommendation reco, double score) {
        recoRepo.saveRecommendation(reco, score);
    }

    public void deleteRecommendation(String id) {
        recoRepo.deleteRecommendation(id);
    }

    public void updateRecommendationOrder(String id, double newScore) {
        recoRepo.updateRecommendationScore(id, newScore);
    }
}
