package vttp.batch5.ssf.miniproject.models.user;

import java.io.Serializable;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class User implements Serializable {
    private String id;

    @NotEmpty(message = "Email is required")
    @Email(message = "Must be a valid email")
    private String email;

    @NotEmpty(message = "Password is required")
    @Size(min=8, max=20, message = "Password must be between 8 and 20 characters")
    private String password;

    @Valid
    private UserProfile profile;

    // Getter and Setter for profile
    public UserProfile getProfile() {
        return profile;
    }
    
    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

    // Getters and setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
