package com.project.back_end.services;

import com.project.back_end.repository.AdminRepository;
import com.project.back_end.repository.DoctorRepository;
import com.project.back_end.repository.PatientRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/*
 * TOKEN SERVICE — JWT token generation, extraction, and validation.
 * 
 * This service handles all JWT operations:
 * 1. generateToken() — Create a new token for a user
 * 2. extractIdentifier() — Get the username/email from a token
 * 3. validateToken() — Check if a token is valid for a user type
 * 4. getSigningKey() — Get the cryptographic key for signing
 */
@Component
public class TokenService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Autowired private AdminRepository adminRepository;
    @Autowired private DoctorRepository doctorRepository;
    @Autowired private PatientRepository patientRepository;

    /*
     * generateToken — Create a JWT token for a user.
     * 
     * @param identifier — Username (admin) or email (doctor/patient)
     * @return Signed JWT token string
     * 
     * Token expires in 7 days.
     */
    public String generateToken(String identifier) {
        return Jwts.builder()
                .subject(identifier)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000))
                .signWith(getSigningKey())
                .compact();
    }

    // Overloaded version that accepts role (for AuthController)
    public String generateToken(String identifier, String role) {
        return Jwts.builder()
                .subject(identifier)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000))
                .signWith(getSigningKey())
                .compact();
    }

    /*
     * extractIdentifier — Get the username/email from a JWT token.
     * 
     * @param token — The JWT token
     * @return The subject (username or email) embedded in the token
     */
    public String extractIdentifier(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /*
     * validateToken — Check if a token is valid for a given user type.
     * 
     * @param token — The JWT token
     * @param user — "admin", "doctor", or "patient"
     * @return true if valid, false if invalid
     * 
     * This checks:
     * 1. Is the token valid (not expired, not tampered)?
     * 2. Does the user exist in the database?
     */
    public boolean validateToken(String token, String user) {
        try {
            String identifier = extractIdentifier(token);

            switch (user.toLowerCase()) {
                case "admin":
                    return adminRepository.findByUsername(identifier) != null;
                case "doctor":
                    return doctorRepository.findByEmail(identifier) != null;
                case "patient":
                    return patientRepository.findByEmail(identifier) != null;
                default:
                    return false;
            }
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /*
     * getSigningKey — Create the cryptographic key from our secret.
     * 
     * The same key used to SIGN tokens is used to VERIFY them.
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
}
