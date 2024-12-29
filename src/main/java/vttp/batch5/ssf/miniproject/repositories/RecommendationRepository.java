package vttp.batch5.ssf.miniproject.repositories;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.batch5.ssf.miniproject.models.recommendation.Recommendation;
import vttp.batch5.ssf.miniproject.models.recommendation.RecommendationLink;

import java.util.Optional;

@Repository
public class RecommendationRepository {
    private static final String RECO_DETAILS = "recommendation:details:";
    private static final String RECO_ORDER = "recommendation:order";
    private static final String USER_RECO_STATUS = "user:recommendation:status:";

    @Autowired
    @Qualifier("redis-0")
    private RedisTemplate<String, String> template;

    // Get all recommendations
    public List<Recommendation> getAllRecommendations() {
        Set<String> recoIds = template.opsForZSet().reverseRange(RECO_ORDER, 0, -1);
        List<Recommendation> recos = new ArrayList<>();

        for (String id : recoIds) {
            Map<Object, Object> recoData = template.opsForHash().entries(RECO_DETAILS + id);
            recos.add(mapToRecommendation(id, recoData));
        }

        return recos;
    }

    // Get User recommendation statuses
    public Map<String, String> getUserRecommendationStatuses(String userId) {
        return convertMapToStringString(
                template.opsForHash().entries(USER_RECO_STATUS + userId));
    }

    // Update User recommendation status
    public void updateUserRecommendationStatus(String userId, String recoId, String status) {
        template.opsForHash().put(USER_RECO_STATUS + userId, recoId, status);
    }

    // Save recommendation to Redis
    public void saveRecommendation(Recommendation reco, double score) {
        String id = reco.getId();
        if (id == null) {
            id = UUID.randomUUID().toString();
            reco.setId(id);
        }

        Map<String, String> recoMap = new HashMap<>();
        recoMap.put("title", reco.getTitle());
        recoMap.put("content", reco.getContent());
        recoMap.put("links", convertLinksToJson(reco.getLinks()));

        if (reco.getAgeMin() != null)
            recoMap.put("ageMin", reco.getAgeMin());
        if (reco.getAgeMax() != null)
            recoMap.put("ageMax", reco.getAgeMax());
        if (reco.getDependents() != null)
            recoMap.put("dependents", reco.getDependents());

        template.opsForHash().putAll(RECO_DETAILS + id, recoMap);
        template.opsForZSet().add(RECO_ORDER, id, score);
    }

    // Delete recommendation from Redis
    public void deleteRecommendation(String id) {
        template.delete(RECO_DETAILS + id);
        template.opsForZSet().remove(RECO_ORDER, id);
    }

    // Update recommendation score in Redis
    public void updateRecommendationScore(String id, double newScore) {
        template.opsForZSet().add(RECO_ORDER, id, newScore);
    }

    // HELPER METHODS
    // Create Recommendation object from Redis data
    private Recommendation mapToRecommendation(String id, Map<Object, Object> data) {
        Recommendation reco = new Recommendation();
        reco.setId(id);
        reco.setTitle((String) data.get("title"));
        reco.setContent((String) data.get("content"));
        reco.setAgeMin((String) data.get("ageMin"));
        reco.setAgeMax((String) data.get("ageMax"));
        reco.setDependents((String) data.get("dependents"));

        String linksJson = (String) data.get("links");
        reco.setLinks(parseLinks(linksJson));

        return reco;
    }

    // Parse Json to get Recommendation Links
    private List<RecommendationLink> parseLinks(String linksJson) {
        List<RecommendationLink> links = new ArrayList<>();
        if (linksJson == null || linksJson.isEmpty()) {
            return links;
        }

        try (JsonReader reader = Json.createReader(new StringReader(linksJson))) {
            JsonArray jsonArray = reader.readArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject linkObject = jsonArray.getJsonObject(i);
                RecommendationLink link = new RecommendationLink();
                link.setTitle(linkObject.getString("title"));
                link.setUrl(linkObject.getString("url"));
                links.add(link);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return links;
    }

    // Convert Map<Object, Object> to Map<String, String>
    private Map<String, String> convertMapToStringString(Map<Object, Object> map) {
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            result.put(entry.getKey().toString(), entry.getValue().toString());
        }
        return result;
    }

    // Convert Links to JSON
    private String convertLinksToJson(List<RecommendationLink> links) {
        if (links == null || links.isEmpty()) {
            return "[]";
        }

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (RecommendationLink link : links) {

            String title = Optional.ofNullable(link.getTitle()).orElse("");
            String url = Optional.ofNullable(link.getUrl()).orElse("");

            JsonObject jsonObject = Json.createObjectBuilder()
                    .add("title", title)
                    .add("url", url)
                    .build();
            arrayBuilder.add(jsonObject);
        }
        return arrayBuilder.build().toString();
    }
}

