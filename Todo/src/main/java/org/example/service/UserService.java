package org.example.service;


import org.example.model.Users;
import org.example.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service // 2 usages
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authManager;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12); // Strength: 12


    public Users register(Users user) {
        // Encode the plaintext password
        user.setPassword(encoder.encode(user.getPassword()));
        // Save the user to the repository
        return userRepo.save(user);
    }

    /**
     * Verifies user credentials and generates a JWT upon successful authentication.
     * @param user The Users object containing the username and plaintext password for verification.
     * @return The generated JWT token string on success, or "Failure" on authentication failure.
     */
    public String verify(Users user) {

        // Attempt to authenticate the user credentials
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );

        // Check if authentication was successful
        if (authentication.isAuthenticated()) {
            // If authenticated, generate a JWT token
            return jwtService.generateToken(user.getUsername());
        }

        // Return failure message
        return "Failure";
    }
}