package com.project.back_end.services;

import com.project.back_end.dto.Login;
import com.project.back_end.models.*;
import com.project.back_end.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CentralService {

    @Autowired private TokenService tokenService;
    @Autowired private AdminRepository adminRepository;
    @Autowired private DoctorRepository doctorRepository;
    @Autowired private PatientRepository patientRepository;
    @Autowired private AppointmentRepository appointmentRepository;
    @Autowired private DoctorService doctorService;
    @Autowired private PatientService patientService;
    @Autowired private PasswordEncoder passwordEncoder;

    public ResponseEntity<Map<String, String>> validateToken(String token, String user) {
        if (!tokenService.validateToken(token, user)) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Unauthorized: Invalid or expired token");
            return ResponseEntity.status(401).body(error);
        }
        return null;
    }

    public ResponseEntity<Map<String, String>> validateAdmin(Admin receivedAdmin) {
        Admin admin = adminRepository.findByUsername(receivedAdmin.getUsername());

        if (admin != null && passwordEncoder.matches(receivedAdmin.getPassword(), admin.getPassword())) {
            String token = tokenService.generateToken(admin.getUsername());
            return ResponseEntity.ok(Map.of("token", token));
        }

        return ResponseEntity.badRequest().body(Map.of("error", "Invalid credentials"));
    }

    public Map<String, Object> filterDoctor(String name, String specialty, String time) {
        List<Doctor> doctors;

        if (name != null && specialty != null) {
            doctors = doctorService.filterDoctorsByNameSpecilityandTime(name, specialty, time);
        } else if (name != null) {
            doctors = doctorService.filterDoctorByNameAndTime(name, time);
        } else if (specialty != null) {
            doctors = doctorService.filterDoctorByTimeAndSpecility(specialty, time);
        } else if (time != null) {
            doctors = doctorService.filterDoctorsByTime(time);
        } else {
            doctors = doctorService.getDoctors();
        }

        return Map.of("doctors", doctors);
    }

    public int validateAppointment(Appointment appointment) {
        if (appointment.getDoctor() == null) return -1;

        Long doctorId = appointment.getDoctor().getId();
        if (doctorRepository.findById(doctorId).isEmpty()) return -1;

        List<String> availableSlots = doctorService.getDoctorAvailability(doctorId, appointment.getAppointmentDate());
        String requestedTime = appointment.getAppointmentTime() != null ?
                appointment.getAppointmentTime().toLocalTime().toString() : null;

        if (requestedTime != null && availableSlots.contains(requestedTime)) {
            return 1;
        }
        return 0;
    }

    public boolean validatePatient(Patient patient) {
        Patient existing = patientRepository.findByEmailOrPhone(patient.getEmail(), patient.getPhone());
        return existing == null;
    }

    public ResponseEntity<Map<String, String>> validatePatientLogin(Login login) {
        var patientOpt = patientRepository.findByEmail(login.getIdentifier());

        if (patientOpt.isPresent()) {
            Patient patient = patientOpt.get();
            if (passwordEncoder.matches(login.getPassword(), patient.getPassword())) {
                String token = tokenService.generateToken(patient.getEmail());
                return ResponseEntity.ok(Map.of("token", token));
            }
        }

        return ResponseEntity.badRequest().body(Map.of("error", "Invalid credentials"));
    }

    public ResponseEntity<Map<String, Object>> filterPatient(String condition, String name, String token) {
        String email = tokenService.extractIdentifier(token);
        var patientOpt = patientRepository.findByEmail(email);

        if (patientOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Patient not found"));
        }

        return patientService.filterByCondition(condition, patientOpt.get().getId());
    }
}
