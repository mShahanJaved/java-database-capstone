package com.project.back_end.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/*
 * APPOINTMENT MODEL — The core business entity of the clinic.
 * 
 * WHAT IT DOES:
 * Links a Doctor to a Patient at a specific time.
 * This is where the clinic's money comes from — appointments!
 * 
 * KEY CONCEPTS:
 * 
 * 1. @ManyToOne — "Many appointments can belong to ONE doctor"
 *    → Multiple patients can book with the same doctor
 *    → Each appointment has exactly ONE doctor
 * 
 * 2. @Future — "Appointment time must be in the future"
 *    → You can't book an appointment for yesterday!
 *    → Hibernate validates this automatically
 * 
 * 3. @Transient — "Don't save this method's result to the database"
 *    → Helper methods like getEndTime() are calculated on-the-fly
 *    → They don't need their own database column
 * 
 * 4. status as int — 0 = Scheduled, 1 = Completed
 *    → Simple numeric status (course requirement)
 *    → In production, you'd use an Enum for type safety
 */
@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * @ManyToOne — MANY appointments → ONE doctor
     * 
     * @JoinColumn(name = "doctor_id") — Creates a foreign key column
     * in the appointments table that points to doctors.id
     * 
     * WHY @NotNull? An appointment WITHOUT a doctor is meaningless!
     * "Hey, I have an appointment... with nobody!" 🤷
     */
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    @NotNull(message = "doctor cannot be null")
    private Doctor doctor;

    /*
     * @ManyToOne — MANY appointments → ONE patient
     * Same concept as doctor above.
     */
    @ManyToOne
    @JoinColumn(name = "patient_id")
    @NotNull(message = "patient cannot be null")
    private Patient patient;

    /*
     * @Future — Appointment time MUST be in the future.
     * 
     * HOW IT WORKS:
     * If someone tries to save an appointment with a past date:
     * → Hibernate throws ConstraintViolationException
     * → Spring returns HTTP 400 Bad Request
     * 
     * WHY LocalDateTime and not just LocalDate?
     * Because appointments need BOTH date AND time.
     * "May 22, 2025 at 09:00" not just "May 22, 2025"
     * 
     * EXAMPLE: LocalDateTime.of(2025, 5, 22, 9, 0)
     */
    @Future(message = "Appointment time must be in the future")
    private LocalDateTime appointmentTime;

    /*
     * status — Simple integer status:
     * 0 = Scheduled (default)
     * 1 = Completed
     * 
     * WHY int and not Enum?
     * The course specifies int. In production, use an Enum:
     * public enum Status { SCHEDULED, COMPLETED, CANCELLED }
     * Enums are type-safe — you can't accidentally set status = 99
     */
    @NotNull(message = "status cannot be null")
    private int status; // 0 = Scheduled, 1 = Completed

    /*
     * HELPER METHODS — These are @Transient (not saved to database)
     * 
     * @Transient means: "This is a Java method, not a database column"
     * Without @Transient, Hibernate would try to create a column for each
     * method that returns a value — that would be chaos!
     */

    /**
     * getEndTime() — Returns the end time of the appointment (1 hour after start).
     * 
     * WHY? Every appointment is 1 hour long (course requirement).
     * This method calculates when it ends.
     * 
     * EXAMPLE:
     * appointmentTime = 2025-05-22T09:00:00
     * getEndTime() returns = 2025-05-22T10:00:00
     * 
     * The frontend can use this to display "09:00 - 10:00"
     */
    @Transient
    public LocalDateTime getEndTime() {
        if (appointmentTime == null) {
            return null;
        }
        return appointmentTime.plusHours(1);
    }

    /**
     * getAppointmentDate() — Returns ONLY the date portion.
     * 
     * WHY? The "Patient Record" page shows dates like "2025-05-23"
     * without the time. This method extracts just the date.
     * 
     * EXAMPLE:
     * appointmentTime = 2025-05-22T09:00:00
     * getAppointmentDate() returns = 2025-05-22
     */
    @Transient
    public java.time.LocalDate getAppointmentDate() {
        if (appointmentTime == null) {
            return null;
        }
        return appointmentTime.toLocalDate();
    }

    /**
     * getAppointmentTimeOnly() — Returns ONLY the time portion.
     * 
     * WHY? The frontend displays time slots like "09:00-10:00".
     * This method extracts just the time from the LocalDateTime.
     * 
     * EXAMPLE:
     * appointmentTime = 2025-05-22T09:00:00
     * getAppointmentTimeOnly() returns = 09:00
     */
    @Transient
    public String getAppointmentTimeOnly() {
        if (appointmentTime == null) {
            return null;
        }
        return appointmentTime.toLocalTime().toString();
    }

    /*
     * GETTERS AND SETTERS
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalDateTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
