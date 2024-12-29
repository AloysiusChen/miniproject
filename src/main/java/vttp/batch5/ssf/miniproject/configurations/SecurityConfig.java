package vttp.batch5.ssf.miniproject.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import vttp.batch5.ssf.miniproject.services.UserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Autowired
        private UserDetailsService customUserDetailsService;

        // Configures HTTP security settings for the application, defining what URLs are
        // secured and how users log in and out
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(auth -> auth
                                                // Configures which URLs are publicly accessible without authentication
                                                .requestMatchers("/", "/home", "/register", "/login",
                                                                "/api/recommendations", "/api-docs")
                                                .permitAll()
                                                .requestMatchers("/dashboard", "/market-pulse", "/smart-moves",
                                                                "/portfolio-and-goals",
                                                                "/portfolio/**")
                                                .authenticated()
                                                // Requires authentication for any other requests
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                // Defines the login page and its behavior
                                                .loginPage("/login")
                                                // Redirects to the dashboard upon successful login
                                                .defaultSuccessUrl("/dashboard")
                                                // Redirects back to login page with error if login fails
                                                .failureUrl("/login?error=true"))
                                .logout(logout -> logout
                                                // Defines the behavior upon logout
                                                // Redirects to login page after logout
                                                .logoutSuccessUrl("/login?logout=true"));

                // Disable CSRF for the API endpoint
                http.csrf(csrf -> csrf.ignoringRequestMatchers("/api/recommendations"));

                // Returns the configured SecurityFilterChain object
                return http.build();
        }

        // Defines the password encoder to use for hashing and verifying passwords
        @Bean
        public PasswordEncoder passwordEncoder() {
                // Returns an instance of BCryptPasswordEncoder, which uses bcrypt hashing algorithm for passwords
                return new BCryptPasswordEncoder();
        }

        // Defines the AuthenticationManager bean that manages the authentication process
        @Bean
        public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
                // Create an AuthenticationManagerBuilder to build the AuthenticationManager
                AuthenticationManagerBuilder authenticationManagerBuilder = http
                                .getSharedObject(AuthenticationManagerBuilder.class);

                // Set the custom UserDetailsService and PasswordEncoder for authentication
                authenticationManagerBuilder
                                .userDetailsService(customUserDetailsService)
                                .passwordEncoder(passwordEncoder());

                // Returns the built AuthenticationManager
                return authenticationManagerBuilder.getOrBuild();
        }
}