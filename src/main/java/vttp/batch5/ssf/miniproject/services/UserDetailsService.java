package vttp.batch5.ssf.miniproject.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import vttp.batch5.ssf.miniproject.models.user.User;
import vttp.batch5.ssf.miniproject.repositories.UserRepository;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    // The method to load user details by email, required by Spring Security's UserDetailsService interface
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        // Returning a Spring Security User object with the email, password, and roles
        // AuthorityUtils.createAuthorityList creates a list of roles for the user
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), 
                user.getPassword(), 
                AuthorityUtils.createAuthorityList("ROLE_USER")
        );
    }
}