package com.project.back_end.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/*
 * APPOINTMENT DTO — Data Transfer Object
 * 
 * WHAT IS A DTO?
 * A DTO is a simplified version of an entity used for API responses.
 * 
 * WHY use DTOs instead of returning the entity directly?
 * 1. SECURITY — Don't expose sensitive fields (passwords, internal IDs)
 * 2. FLEXIBILITY — Combine data from multiple tables into one object
 * 3. CLEAN API — Frontend gets exactly what it needs, nothing extra
 * 
 * EXAMPLE:
 * Entity Appointment has: doctor (full object), patient (full object), timestamps
 * DTO has: doctorName, patientName, patientEmail — just what the UI needs
 * 
 * This class has NO @Entity annotation — it's NOT stored in the database.
 * It's only used for sending data to the frontend.
 */
public class AppointmentDTO {

    private Long id;
    private Long doctorId;
    private String doctorName;
    private Long patientId;
    private String patientName;
    private String patientEmail;
    private String patientPhone;
    private String patientAddress;
    private LocalDateTime appointmentTime;
    private int status;
    private LocalDate appointmentDate;
    private LocalTime appointmentTimeOnly;
    private LocalDateTime endTime;

    /*
     * CONSTRUCTOR — Automatically computes derived fields.
     * 
     * When you create an AppointmentDTO, it automatically:
     * 1. Extracts the date from appointmentTime
     * 2. Extracts the time from appointmentTime
     * 3. Calculates endTime as appointmentTime + 1 hour
     * 
     * This saves the frontend from doing date math!
     */
    public AppointmentDTO(Long id, Long doctorId, String doctorName,
                          Long patientId, String patientName, String patientEmail,
                          String patientPhone, String patientAddress,
                          LocalDateTime appointmentTime, int status) {
        this.id = id;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.patientId = patientId;
        this.patientName = patientName;
        this.patientEmail = patientEmail;
        this.patientPhone = patientPhone;
        this.patientAddress = patientAddress;
        this.appointmentTime = appointmentTime;
        this.status = status;

        // Auto-compute derived fields
        if (appointmentTime != null) {
            this.appointmentDate = appointmentTime.toLocalDate();
            this.appointmentTimeOnly = appointmentTime.toLocalTime();
            this.endTime = appointmentTime.plusHours(1);
        }
    }

    // GETTERS
    public Long getId() { return id; }
    public Long getDoctorId() { return doctorId; }
    public String getDoctorName() { return doctorName; }
    public Long getPatientId() { return patientId; }
    public String getPatientName() { return patientName; }
    public String getPatientEmail() { return patientEmail; }
    public String getPatientPhone() { return patientPhone; }
    public String getPatientAddress() { return patientAddress; }
    public LocalDateTime getAppointmentTime() { return appointmentTime; }
    public int getStatus() { return status; }
    public LocalDate getAppointmentDate() { return appointmentDate; }
    public LocalTime getAppointmentTimeOnly() { return appointmentTimeOnly; }
    public LocalDateTime getEndTime() { return endTime; }
}
