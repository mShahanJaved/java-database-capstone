package com.project.back_end.controller;

import com.project.back_end.models.Patient;
import com.project.back_end.models.User;
import com.project.back_end.repository.PatientRepository;
import com.project.back_end.repository.UserRepository;
import com.project.back_end.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired private PatientRepository patientRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private TokenService tokenService;

    @GetMapping("/all")
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(patientRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        return ResponseEntity.ok(patient);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Patient patient) {
        if (patientRepository.findByEmail(patient.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Email already registered"));
        }

        patient.setPassword(passwordEncoder.encode(patient.getPassword()));

        User user = new User();
        user.setUsername(patient.getEmail());
        user.setPassword(patient.getPassword());
        user.setRole(User.Role.PATIENT);
        userRepository.save(user);

        patientRepository.save(patient);

        return ResponseEntity.ok(Map.of("success", true, "message", "Signup successful"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        var patientOpt = patientRepository.findByEmail(email);
        if (patientOpt.isPresent()) {
            Patient patient = patientOpt.get();
            if (passwordEncoder.matches(password, patient.getPassword())) {
                String token = tokenService.generateToken(patient.getEmail());
                return ResponseEntity.ok(Map.of("success", true, "token", token, "message", "Login successful"));
            }
        }

        return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Invalid credentials"));
    }
}
