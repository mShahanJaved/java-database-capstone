package com.project.back_end.dto;

/*
 * LOGIN DTO — Receives login credentials from the frontend.
 * 
 * Fields:
 * - identifier: Email (for doctor/patient) or username (for admin)
 * - password: The user's password
 * 
 * This is used in @RequestBody parameters inside controller methods.
 * It's NOT stored in the database — just used for authentication input.
 */
public class Login {

    private String identifier;
    private String password;

    public Login() {}

    public Login(String identifier, String password) {
        this.identifier = identifier;
        this.password = password;
    }

    public String getIdentifier() { return identifier; }
    public void setIdentifier(String identifier) { this.identifier = identifier; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
