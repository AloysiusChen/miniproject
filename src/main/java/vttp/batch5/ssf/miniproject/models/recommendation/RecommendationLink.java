package vttp.batch5.ssf.miniproject.models.recommendation;

public class RecommendationLink {
    // Fields
    private String title;
    private String url;

    // Constructors
    public RecommendationLink() {
    }
    public RecommendationLink(String title, String url) {
        this.title = title;
        this.url = url;
    }


    // Getters and Setters
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}
