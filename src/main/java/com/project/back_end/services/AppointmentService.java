package com.project.back_end.services;

import com.project.back_end.dto.AppointmentDTO;
import com.project.back_end.models.Appointment;
import com.project.back_end.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    @Autowired private AppointmentRepository appointmentRepository;
    @Autowired private TokenService tokenService;

    // Book a new appointment
    public int bookAppointment(Appointment appointment) {
        try {
            appointmentRepository.save(appointment);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    // Update an existing appointment
    public ResponseEntity<Map<String, String>> updateAppointment(Appointment appointment) {
        Optional<Appointment> existing = appointmentRepository.findById(appointment.getId());
        if (existing.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Appointment not found"));
        }
        appointmentRepository.save(appointment);
        return ResponseEntity.ok(Map.of("message", "Appointment updated"));
    }

    // Cancel an appointment
    public ResponseEntity<Map<String, String>> cancelAppointment(long id, String token) {
        Optional<Appointment> appointment = appointmentRepository.findById(id);
        if (appointment.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Appointment not found"));
        }
        appointmentRepository.delete(appointment.get());
        return ResponseEntity.ok(Map.of("message", "Appointment cancelled"));
    }

    // Get appointments for a doctor on a specific date
    public Map<String, Object> getAppointment(String pname, LocalDate date, String token) {
        String email = tokenService.extractIdentifier(token);
        // Find doctor by email from token
        // For now, use a simplified approach
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        List<Appointment> appointments = appointmentRepository.findAll().stream()
                .filter(a -> a.getAppointmentDate() != null && a.getAppointmentDate().equals(date))
                .collect(Collectors.toList());

        // Filter by patient name if provided
        if (pname != null && !pname.isEmpty()) {
            appointments = appointments.stream()
                    .filter(a -> a.getPatient() != null &&
                            a.getPatient().getName().toLowerCase().contains(pname.toLowerCase()))
                    .collect(Collectors.toList());
        }

        Map<String, Object> response = new HashMap<>();
        response.put("appointments", convertToDTOs(appointments));
        return response;
    }

    // Convert entities to DTOs
    private List<AppointmentDTO> convertToDTOs(List<Appointment> appointments) {
        return appointments.stream().map(a -> new AppointmentDTO(
                a.getId(),
                a.getDoctor() != null ? a.getDoctor().getId() : null,
                a.getDoctor() != null ? a.getDoctor().getName() : null,
                a.getPatient() != null ? a.getPatient().getId() : null,
                a.getPatient() != null ? a.getPatient().getName() : null,
                a.getPatient() != null ? a.getPatient().getEmail() : null,
                a.getPatient() != null ? a.getPatient().getPhone() : null,
                a.getPatient() != null ? a.getPatient().getAddress() : null,
                a.getAppointmentTime(),
                a.getStatus()
        )).collect(Collectors.toList());
    }
}
