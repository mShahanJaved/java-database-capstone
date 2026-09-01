package com.project.back_end.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/*
 * PRESCRIPTION MODEL — Stored in MongoDB, NOT MySQL!
 * 
 * WHY MONGODB FOR PRESCRIPTIONS?
 * Prescriptions are FLEXIBLE — different doctors write them differently:
 * - Some have 1 medicine, some have 10
 * - Dosage instructions vary wildly ("Take 1 tablet" vs "Apply cream 3x daily")
 * - Some have notes, some don't
 * 
 * MongoDB stores JSON-like documents, so we can have whatever fields we want
 * without altering a rigid database schema.
 * 
 * KEY DIFFERENCES FROM JPA MODELS:
 * 
 * 1. @Document instead of @Entity
 *    → @Entity = MySQL table
 *    → @Document = MongoDB collection
 * 
 * 2. @Id is from Spring Data MongoDB, not jakarta.persistence
 *    → MongoDB uses String IDs (ObjectId), not auto-increment Long
 * 
 * 3. No @GeneratedValue
 *    → MongoDB auto-generates IDs as ObjectId strings
 *    → Example: "64abc123456def789"
 * 
 * 4. No @ManyToOne relationships
 *    → MongoDB doesn't have foreign keys
 *    → We store appointmentId as a plain number (denormalization)
 */
@Document(collection = "prescriptions")
public class Prescription {

    /*
     * @Id — MongoDB's unique identifier.
     * 
     * HOW MONGODB IDs WORK:
     * MongoDB generates a 12-byte ObjectId automatically.
     * It looks like: "507f1f77bcf86cd799439011" (24 hex characters)
     * 
     * WHY String and not Long?
     * Because MongoDB's native ID format is ObjectId, not integer.
     * Spring Data MongoDB handles this conversion automatically.
     */
    @Id
    private String id;

    /*
     * patientName — Store the patient's name directly.
     * 
     * WHY store the name and not just the ID?
     * In MongoDB, it's common to DENORMALIZE data — store related info
     * directly in the document to avoid expensive lookups.
     * 
     * If we only stored patientId, we'd need to:
     * 1. Query MongoDB for the prescription
     * 2. Query MySQL for the patient name
     * 3. Combine the results
     * 
     * By storing the name directly, we only need one query!
     */
    @NotNull(message = "patientName cannot be null")
    @Size(min = 3, max = 100, message = "Patient name must be between 3 and 100 characters")
    private String patientName;

    /*
     * appointmentId — Links this prescription to a specific appointment.
     * 
     * WHY a plain Long and not a reference?
     * MongoDB doesn't have foreign keys like MySQL.
     * We manually link data in our service layer.
     * 
     * This is NORMAL for NoSQL — denormalization is expected!
     */
    @NotNull(message = "appointmentId cannot be null")
    private Long appointmentId;

    /*
     * medication — The name of the prescribed medicine.
     * 
     * WHY a single String and not a List?
     * The course specifies a single medication field.
     * Example: "Paracetamol", "Amoxicillin 500mg"
     * 
     * In a real app, you might use a List<String> for multiple medicines,
     * but we follow the course requirements exactly.
     */
    @NotNull(message = "medication cannot be null")
    @Size(min = 3, max = 100, message = "Medication must be between 3 and 100 characters")
    private String medication;

    /*
     * dosage — How much medicine to take.
     * 
     * Example: "500mg", "10ml", "2 tablets"
     * 
     * @Size(min = 3, max = 20) — Short but descriptive.
     */
    @NotNull(message = "dosage cannot be null")
    @Size(min = 3, max = 20, message = "Dosage must be between 3 and 20 characters")
    private String dosage;

    /*
     * doctorNotes — Optional notes from the doctor.
     * 
     * Example: "Take 1 tablet every 6 hours. Avoid alcohol."
     * 
     * WHY max = 200? Short and sweet. If a doctor needs to write an essay,
     * they should use a separate notes system.
     * 
     * NOTE: No @NotNull — this field is OPTIONAL!
     * Some prescriptions don't need extra notes.
     */
    @Size(max = 200, message = "Doctor notes must not exceed 200 characters")
    private String doctorNotes;

    /*
     * CONSTRUCTOR — Used for easy object creation.
     * 
     * WHY a constructor with parameters?
     * Makes it easy to create a Prescription in one line:
     * new Prescription("John Smith", 51L, "Paracetamol", "500mg", "Take every 6 hours")
     * 
     * vs. creating an empty object and calling 5 setters.
     */
    public Prescription() {
        // Default constructor (required by Spring Data MongoDB)
    }

    public Prescription(String patientName, Long appointmentId, String medication,
                        String dosage, String doctorNotes) {
        this.patientName = patientName;
        this.appointmentId = appointmentId;
        this.medication = medication;
        this.dosage = dosage;
        this.doctorNotes = doctorNotes;
    }

    /*
     * GETTERS AND SETTERS
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getMedication() {
        return medication;
    }

    public void setMedication(String medication) {
        this.medication = medication;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getDoctorNotes() {
        return doctorNotes;
    }

    public void setDoctorNotes(String doctorNotes) {
        this.doctorNotes = doctorNotes;
    }
}
