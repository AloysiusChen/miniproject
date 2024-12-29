package vttp.batch5.ssf.miniproject.controllers;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.batch5.ssf.miniproject.models.recommendation.Recommendation;
import vttp.batch5.ssf.miniproject.models.recommendation.RecommendationLink;
import vttp.batch5.ssf.miniproject.services.RecommendationService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/recommendations", produces = "application/json")
public class RecommendationRestController {

    @Autowired
    private RecommendationService recommendationService;

    @PostMapping(consumes = "application/json")
    public ResponseEntity<String> addRecommendation(@RequestBody String payload) {

        JsonObject body;
        try (InputStream is = new ByteArrayInputStream(payload.getBytes())) {
            JsonReader reader = Json.createReader(is);
            body = reader.readObject();

            Recommendation reco = new Recommendation();
            reco.setId(UUID.randomUUID().toString());
            reco.setTitle(body.getString("title"));
            reco.setContent(body.getString("content"));

            if (body.containsKey("ageMin"))
                reco.setAgeMin(body.getString("ageMin"));
            if (body.containsKey("ageMax"))
                reco.setAgeMax(body.getString("ageMax"));
            if (body.containsKey("dependents"))
                reco.setDependents(body.getString("dependents"));

            List<RecommendationLink> links = new ArrayList<>();
            if (body.containsKey("links")) {
                body.getJsonArray("links").forEach(linkJson -> {
                    JsonObject linkObj = linkJson.asJsonObject();
                    RecommendationLink link = new RecommendationLink(
                            linkObj.getString("title"),
                            linkObj.getString("url"));
                    links.add(link);
                });
            }
            reco.setLinks(links);

            // Save recommendation
            recommendationService.createRecommendation(reco, 50.0);

            // Success response
            JsonObject resp = Json.createObjectBuilder()
                    .add("message", "Recommendation added successfully")
                    .add("id", reco.getId())
                    .build();
            return ResponseEntity.ok(resp.toString());

        } catch (Exception ex) {
            // Error response
            JsonObject error = Json.createObjectBuilder()
                    .add("error", ex.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(error.toString());
        }
    }
}