package com.project.back_end.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/*
 * DOCTOR MODEL — Represents a healthcare provider in the clinic.
 * 
 * WHAT IT DOES:
 * Stores doctor info: name, specialty, email, password, phone, and available time slots.
 * Links to Appointments via @OneToMany (a doctor has many appointments).
 * 
 * KEY CONCEPTS:
 * 
 * 1. VALIDATION ANNOTATIONS — These protect data quality at the MODEL level.
 *    Even if the frontend validates input, someone could send a direct API request.
 *    Server-side validation is your LAST LINE OF DEFENSE.
 * 
 * 2. @ElementCollection — Stores a LIST of simple values (like time strings).
 *    Example: ["09:00-10:00", "10:00-11:00", "14:00-15:00"]
 *    Hibernate creates a SEPARATE table called 'doctor_available_times'
 *    with columns: doctor_id, available_times
 * 
 *    WHY not just a comma-separated string?
 *    Because with a List, you can:
 *    - Query: "Find all doctors available at 09:00"
 *    - Add/remove slots without string manipulation
 *    - Type-safe: can't accidentally add "09:00,10:00" as one slot
 */
@Entity
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * @NotNull — Can't be empty
     * @Size(min = 3, max = 100) — Must be 3-100 characters
     * 
     * WHY min = 3? "Dr" is only 2 characters — too short to be a real name.
     * WHY max = 100? Prevents garbage data like 10,000 characters.
     */
    @NotNull(message = "name cannot be null")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    /*
     * specialty — NOT "specialization" (course uses "specialty")
     * Examples: "Cardiologist", "Neurologist", "Orthopedist"
     */
    @NotNull(message = "specialty cannot be null")
    @Size(min = 3, max = 50, message = "Specialty must be between 3 and 50 characters")
    private String specialty;

    /*
     * @Email — Validates format: must contain @ and a domain
     * "dr.smith@" → INVALID
     * "dr.smith@clinic.com" → VALID
     * 
     * WHY validate email at the model level?
     * Because garbage emails break password reset, notifications, etc.
     */
    @NotNull(message = "email cannot be null")
    @Email(message = "Must be a valid email address")
    private String email;

    /*
     * @Size(min = 6) — Password must be at least 6 characters.
     * 
     * @JsonProperty(WRITE_ONLY) — Security!
     * Password is accepted in POST but never returned in GET responses.
     * 
     * WHY min = 6? Industry minimum. "12345" is too easy to guess.
     * Real apps use 8+ with uppercase, lowercase, numbers, symbols.
     */
    @NotNull(message = "password cannot be null")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    /*
     * @Pattern(regexp = "\\d{10}") — Phone must be EXACTLY 10 digits.
     * 
     * \\d = any digit (0-9)
     * {10} = exactly 10 times
     * 
     * Examples:
     * "1234567890" → VALID
     * "123-456-7890" → INVALID (has dashes)
     * "123456789" → INVALID (only 9 digits)
     * 
     * WHY not allow dashes/parentheses?
     * The course specifically says \\d{10}. In production, you'd use a
     * more flexible regex or a phone number library.
     */
    @NotNull(message = "phone cannot be null")
    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phone;

    /*
     * @ElementCollection — Stores a LIST of strings in a separate table.
     * 
     * HOW IT WORKS:
     * Your 'doctor' table has: id, name, specialty, email, password, phone
     * Hibernate creates a SECOND table 'doctor_available_times':
     *   doctor_id | available_times
     *   1         | "09:00-10:00"
     *   1         | "10:00-11:00"
     *   1         | "14:00-15:00"
     *   2         | "09:00-10:00"
     * 
     * WHY? Because a doctor can have MULTIPLE time slots.
     * You can't store a List in a single column (well, you could with JSON,
     * but @ElementCollection is the proper JPA way).
     * 
     * Course example: ["09:00 -10:00", "10:00 -11:00", "14:00 -15:00"]
     */
    @ElementCollection
    private List<String> availableTimes;

    /*
     * GETTERS AND SETTERS
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<String> getAvailableTimes() {
        return availableTimes;
    }

    public void setAvailableTimes(List<String> availableTimes) {
        this.availableTimes = availableTimes;
    }
}
