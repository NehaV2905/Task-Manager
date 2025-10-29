package org.example.service;

// Imports (Partial)
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service // Line 19
public class JWTService {

    private String secretKey = ""; // Line 22

    // Constructor to generate and store the secret key
    public JWTService() {
        try {
            // Line 25
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGen.generateKey();
            // Store the key as a Base64 encoded String
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // --- Token Generation Methods ---

    /**
     * Generates a JWT for a given username.
     */
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>(); // Empty claims map

        // Expiration time is set to 30 minutes (60 * 60 * 30 seconds of milliseconds)
        long expirationTimeMillis = System.currentTimeMillis() + (60 * 60 * 30);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(expirationTimeMillis))
                .signWith(getSecretKey())
                .compact();
    }

    // --- Key and Claim Extraction Methods ---

    /**
     * Reconstructs the SecretKey object from the Base64-encoded secret key string.
     */
    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extracts the username (subject) from the token.
     */
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a specific claim from the token using a Claims resolver function.
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Parses the signed JWT, verifies the signature, and returns all claims.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSecretKey()) // 'verifyWith(getSecretKey())' may be used in newer Jwts versions
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // --- Token Validation Methods ---

    /**
     * Validates a token against the user details and checks expiration.
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Checks if the token has expired.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from the token.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
