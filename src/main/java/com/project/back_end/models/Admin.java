package com.project.back_end.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * ADMIN MODEL — The simplest model in the system.
 * 
 * WHAT IT DOES:
 * Represents a system administrator who can manage doctors,
 * view patients, and monitor appointments.
 * 
 * WHY SO SIMPLE?
 * Admins don't need fancy profiles — they just need to log in.
 * The course requires only: id, username, password.
 * 
 * KEY ANNOTATIONS EXPLAINED:
 * 
 * @Entity — "Hey Hibernate, this class = a database table"
 *   → Hibernate creates a table called 'admin' with columns for each field
 * 
 * @Id — "This field is the PRIMARY KEY"
 *   → Every row in the table has a unique id
 * 
 * @GeneratedValue(strategy = IDENTITY) — "Let MySQL auto-increment the id"
 *   → MySQL handles creating new IDs (1, 2, 3...)
 * 
 * @NotNull — "This field CANNOT be empty"
 *   → If someone tries to save without a username, Hibernate throws an error
 * 
 * @JsonProperty(WRITE_ONLY) — "Allow this field in POST requests, but NEVER show it in GET responses"
 *   → This is a SECURITY feature! Passwords should never be sent back to the browser.
 *   → When you GET /api/admins, the password field will be missing from the JSON.
 *   → But when you POST /api/admins with a password, it WILL be accepted.
 */
@Entity
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * @NotNull — Username is REQUIRED.
     * message = "username cannot be null" — custom error message if validation fails.
     * 
     * WHY? An admin without a username can't log in!
     */
    @NotNull(message = "username cannot be null")
    private String username;

    /*
     * @JsonProperty(WRITE_ONLY) — The security annotation.
     * 
     * SECURITY EXPLANATION:
     * When a client sends: POST /api/admins {"username": "admin", "password": "secret123"}
     * → The password IS accepted and saved to the database.
     * 
     * When a client sends: GET /api/admins/1
     * → The response is: {"id": 1, "username": "admin"} — NO PASSWORD!
     * 
     * This prevents password leakage through API responses.
     * Even if a developer accidentally returns the admin object, the password is hidden.
     */
    @NotNull(message = "password cannot be null")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    /*
     * GETTERS AND SETTERS
     * 
     * These are REQUIRED for Spring Boot to work properly.
     * Spring uses getters/setters to:
     * 1. Read data from HTTP requests → set into your object
     * 2. Write your object data → into HTTP responses or database
     * 
     * Lombok @Data would generate these automatically, but the course
     * asks for explicit getters/setters for learning purposes.
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
