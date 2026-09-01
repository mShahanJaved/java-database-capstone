package com.project.back_end.security;

import com.project.back_end.services.CustomUserDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.ArrayList;

/*
 * JWT AUTHENTICATION FILTER — The security guard at every door.
 * 
 * WHAT IT DOES:
 * Every HTTP request passes through this filter.
 * It checks:
 * 1. Is there a JWT token in the request header?
 * 2. Is the token valid (not expired, not tampered)?
 * 3. If valid → Set the user's authentication info
 * 4. If invalid → Let it through (other filters will reject it)
 * 
 * HOW IT WORKS:
 * 1. Request comes in: GET /api/doctors
 * 2. Filter extracts token from: Authorization: Bearer eyJhbGci...
 * 3. Validates the token's signature and expiration
 * 4. Extracts the username from the token
 * 5. Loads the user's details from the database
 * 6. Creates a SecurityContext with the user's info
 * 7. Request proceeds to the controller
 * 
 * WHY extends OncePerRequestFilter?
 * Ensures this filter runs ONCE per request (not multiple times
 * if there are internal forwards/redirects).
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String jwtSecret;

    /*
     * getSigningKey() — Create the cryptographic key from our secret.
     * Same key used to SIGN the token is used to VERIFY it.
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /*
     * doFilterInternal() — The main filter method.
     * 
     * Called for EVERY HTTP request.
     * 
     * @param request — The incoming HTTP request
     * @param response — The outgoing HTTP response
     * @param filterChain — Passes the request to the next filter
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        /*
         * STEP 1: Extract the token from the Authorization header.
         * 
         * Header format: "Bearer eyJhbGciOiJIUzI1NiJ9..."
         * We need to:
         * 1. Get the full header value
         * 2. Remove "Bearer " prefix
         * 3. Get just the token
         */
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Remove "Bearer " (7 characters)
        }

        /*
         * Also check for token in query parameter (for page redirects).
         * 
         * WHY? When Thymeleaf redirects to /adminDashboard/{token},
         * the token is in the URL, not the header.
         */
        if (token == null) {
            String uri = request.getRequestURI();
            String[] parts = uri.split("/");
            if (parts.length > 0) {
                String lastPart = parts[parts.length - 1];
                if (lastPart.length() > 20) { // JWT tokens are long strings
                    token = lastPart;
                }
            }
        }

        /*
         * STEP 2: Validate the token.
         * 
         * If the token is valid, extract the username.
         * If invalid, let it pass (other filters will handle rejection).
         */
        if (token != null) {
            try {
                Claims claims = Jwts.parser()
                        .verifyWith(getSigningKey())
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();

                username = claims.getSubject(); // Get the username
            } catch (JwtException | IllegalArgumentException e) {
                // Token is invalid — let it pass, other filters will reject
                logger.error("JWT Token validation failed: " + e.getMessage());
            }
        }

        /*
         * STEP 3: If we have a valid username, set the authentication.
         * 
         * SecurityContextHolder stores the current user's authentication info.
         * Controllers can access it to know WHO is making the request.
         */
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (userDetails != null) {
                /*
                 * Create authentication token:
                 * - Principal: The user details
                 * - Credentials: Empty (we don't store passwords in the context)
                 * - Authorities: The user's roles (ADMIN, DOCTOR, PATIENT)
                 */
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                /*
                 * Set details (IP address, session ID, etc.)
                 * This is mostly for logging/audit purposes.
                 */
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                /*
                 * Set the authentication in the SecurityContext.
                 * Now Spring Security knows WHO is making this request.
                 * Controllers can access this info via:
                 * SecurityContextHolder.getContext().getAuthentication()
                 */
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        /*
         * STEP 4: Pass the request to the next filter in the chain.
         * 
         * The filter chain continues until it reaches the controller.
         */
        filterChain.doFilter(request, response);
    }
}
