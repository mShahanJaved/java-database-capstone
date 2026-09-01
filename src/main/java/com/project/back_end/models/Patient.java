package com.project.back_end.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

/*
 * PATIENT MODEL — Represents a patient who books appointments.
 * 
 * WHAT IT DOES:
 * Stores patient info: name, email, password, phone, address.
 * Patients use these credentials to log in and book appointments.
 * 
 * DESIGN DECISION:
 * The course keeps Patient simple — just basic contact info.
 * No date_of_birth, no gender in the base model.
 * (You CAN add these as extensions, but the base requirement is simple.)
 * 
 * RELATIONSHIP:
 * Patient → Appointment: A patient has MANY appointments (@OneToMany in Appointment)
 * Patient → Prescription: Indirectly linked through Appointment
 */
@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * @NotNull + @Size — Standard string validation.
     * 
     * WHY these specific ranges?
     * min=3: "A" or "AB" are too short to be real names
     * max=100: Prevents absurdly long names from breaking the UI
     */
    @NotNull(message = "name cannot be null")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    @NotNull(message = "email cannot be null")
    @Email(message = "Must be a valid email address")
    private String email;

    /*
     * Patient password — NO @JsonProperty(WRITE_ONLY)!
     * 
     * WHY? The course doesn't specify it for Patient.
     * In a real app, you'd definitely add WRITE_ONLY for security.
     * For now, we follow the course requirements exactly.
     */
    @NotNull(message = "password cannot be null")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    /*
     * @Pattern(regexp = "\\d{10}") — Exactly 10 digits.
     * Same as Doctor phone validation.
     */
    @NotNull(message = "phone cannot be null")
    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phone;

    /*
     * @Size(max = 255) — Address can be long but not unlimited.
     * 
     * WHY 255? It's a common VARCHAR limit in MySQL.
     * Addresses like "123 Main St, Apt 4B, New York, NY 10001" fit easily.
     */
    @NotNull(message = "address cannot be null")
    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String address;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
