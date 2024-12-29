package vttp.batch5.ssf.miniproject.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vttp.batch5.ssf.miniproject.models.user.User;
import vttp.batch5.ssf.miniproject.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String registerUser(User user) {
        if (userRepo.existsByEmail(user.getEmail())) {
            return "Email already exists";
        }

        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setId(UUID.randomUUID().toString());

        userRepo.saveUser(user);

        return "Registration successful!";
    }

    public boolean existsByEmail(String email) {
        return userRepo.existsByEmail(email);
    }

    public User findByEmail(String email) {
        return userRepo.findByEmail(email);
    }
}
