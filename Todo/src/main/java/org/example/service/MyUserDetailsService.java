package org.example.service;

// Imports
import org.example.model.UserPrincipal;
import org.example.model.Users; // Assuming this is the entity class
import org.example.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService { // Line 13

    @Autowired
    private UserRepo repo; // Repository for accessing user data

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // Line 17

        // Find user by username using the repository
        Users user = repo.findByUsername(username);

        if (user == null) {
            System.out.println("User not found");
            throw new UsernameNotFoundException("user not found"); // Line 24
        }

        // Wrap the custom Users object into a UserDetails object (UserPrincipal)
        return new UserPrincipal(user);
    }
}