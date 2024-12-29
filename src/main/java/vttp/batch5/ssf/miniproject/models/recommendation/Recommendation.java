package vttp.batch5.ssf.miniproject.models.recommendation;

import java.util.List;

public class Recommendation{
    // Fields
    private String id;
    private String title;
    private String content;
    private List<RecommendationLink> links;
    // Fields for condition
    private String ageMin;
    private String ageMax;
    private String dependents;

    // Getters and Setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public List<RecommendationLink> getLinks() {
        return links;
    }
    public void setLinks(List<RecommendationLink> links) {
        this.links = links;
    }
    public String getAgeMin() {
        return ageMin;
    }
    public void setAgeMin(String ageMin) {
        this.ageMin = ageMin;
    }
    public String getAgeMax() {
        return ageMax;
    }
    public void setAgeMax(String ageMax) {
        this.ageMax = ageMax;
    }
    public String getDependents() {
        return dependents;
    }
    public void setDependents(String dependents) {
        this.dependents = dependents;
    }    
}
