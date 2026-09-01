package com.project.back_end.controller;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repository.AppointmentRepository;
import com.project.back_end.repository.DoctorRepository;
import com.project.back_end.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    @Autowired private AppointmentRepository appointmentRepository;
    @Autowired private DoctorRepository doctorRepository;
    @Autowired private PatientRepository patientRepository;

    @GetMapping("/all")
    public ResponseEntity<List<Appointment>> getAllAppointments(
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String patientName,
            @RequestParam(required = false) String token
    ) {
        List<Appointment> appointments;

        if (date != null && !date.isEmpty()) {
            LocalDate appointmentDate = LocalDate.parse(date);
            LocalDateTime start = appointmentDate.atStartOfDay();
            LocalDateTime end = appointmentDate.atTime(LocalTime.MAX);
            appointments = appointmentRepository.findAll().stream()
                    .filter(a -> a.getAppointmentTime() != null &&
                            !a.getAppointmentTime().isBefore(start) &&
                            !a.getAppointmentTime().isAfter(end))
                    .toList();
        } else {
            appointments = appointmentRepository.findAll();
        }

        if (patientName != null && !patientName.isEmpty()) {
            appointments = appointments.stream()
                    .filter(a -> a.getPatient() != null &&
                            a.getPatient().getName().toLowerCase().contains(patientName.toLowerCase()))
                    .toList();
        }

        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        return ResponseEntity.ok(appointment);
    }

    @PostMapping("/book")
    public ResponseEntity<?> bookAppointment(@RequestBody Map<String, String> request) {
        try {
            Appointment appointment = new Appointment();

            String doctorName = request.get("doctorName");
            if (doctorName != null) {
                List<Doctor> doctors = doctorRepository.findByNameContainingIgnoreCase(doctorName);
                if (!doctors.isEmpty()) {
                    appointment.setDoctor(doctors.get(0));
                }
            }

            List<Patient> patients = patientRepository.findAll();
            if (!patients.isEmpty()) {
                appointment.setPatient(patients.get(0));
            }

            if (request.get("appointmentDate") != null && request.get("appointmentTime") != null) {
                LocalDate date = LocalDate.parse(request.get("appointmentDate"));
                String timeStr = request.get("appointmentTime");
                // Handle time ranges like "09:00-10:00" — extract start time
                if (timeStr.contains("-")) {
                    timeStr = timeStr.split("-")[0];
                }
                LocalTime time = LocalTime.parse(timeStr);
                appointment.setAppointmentTime(LocalDateTime.of(date, time));
            }
            appointment.setStatus(0);

            Appointment saved = appointmentRepository.save(appointment);
            return ResponseEntity.ok(Map.of("success", true, "message", "Appointment booked", "appointment", saved));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAppointment(@PathVariable Long id, @RequestBody Map<String, String> request) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (request.containsKey("appointmentDate") && request.containsKey("appointmentTime")) {
            LocalDate date = LocalDate.parse(request.get("appointmentDate"));
            LocalTime time = LocalTime.parse(request.get("appointmentTime"));
            appointment.setAppointmentTime(LocalDateTime.of(date, time));
        }
        if (request.containsKey("status")) {
            appointment.setStatus(Integer.parseInt(request.get("status")));
        }

        appointmentRepository.save(appointment);
        return ResponseEntity.ok(Map.of("success", true, "message", "Appointment updated"));
    }

    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<?> cancelAppointment(@PathVariable Long id) {
        appointmentRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("success", true, "message", "Appointment cancelled"));
    }
}
