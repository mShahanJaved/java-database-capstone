package com.project.back_end.controller;

import com.project.back_end.models.*;
import com.project.back_end.repository.*;
import com.project.back_end.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private AdminRepository adminRepository;
    @Autowired private DoctorRepository doctorRepository;
    @Autowired private PatientRepository patientRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private TokenService tokenService;
    @Autowired private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        // Try ADMIN login
        Admin admin = adminRepository.findByUsername(username);
        if (admin != null && passwordEncoder.matches(password, admin.getPassword())) {
            String token = tokenService.generateToken(username, "ADMIN");
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("role", "ADMIN");
            response.put("username", username);
            return ResponseEntity.ok(response);
        }

        // Try DOCTOR login
        Doctor doctor = doctorRepository.findByEmail(username);
        if (doctor != null && passwordEncoder.matches(password, doctor.getPassword())) {
            String token = tokenService.generateToken(username, "DOCTOR");
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("role", "DOCTOR");
            response.put("username", username);
            return ResponseEntity.ok(response);
        }

        // Try PATIENT login
        var patientOpt = patientRepository.findByEmail(username);
        if (patientOpt.isPresent()) {
            Patient patient = patientOpt.get();
            if (passwordEncoder.matches(password, patient.getPassword())) {
                String token = tokenService.generateToken(username, "PATIENT");
                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                response.put("role", "PATIENT");
                response.put("username", username);
                return ResponseEntity.ok(response);
            }
        }

        return ResponseEntity.badRequest().body(Map.of("error", "Invalid credentials"));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Patient patient) {
        if (patientRepository.findByEmail(patient.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email already registered"));
        }

        patient.setPassword(passwordEncoder.encode(patient.getPassword()));

        User user = new User();
        user.setUsername(patient.getEmail());
        user.setPassword(patient.getPassword());
        user.setRole(User.Role.PATIENT);
        userRepository.save(user);

        patientRepository.save(patient);

        return ResponseEntity.ok(Map.of("message", "Registration successful"));
    }
}
