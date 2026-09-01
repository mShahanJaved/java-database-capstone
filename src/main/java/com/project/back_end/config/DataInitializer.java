package com.project.back_end.config;

import com.project.back_end.models.Admin;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repository.AdminRepository;
import com.project.back_end.repository.DoctorRepository;
import com.project.back_end.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private AdminRepository adminRepository;
    @Autowired private DoctorRepository doctorRepository;
    @Autowired private PatientRepository patientRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Hash admin passwords if they're plain text (not starting with $2a$)
        for (Admin admin : adminRepository.findAll()) {
            if (admin.getPassword() != null && !admin.getPassword().startsWith("$2a$")) {
                admin.setPassword(passwordEncoder.encode(admin.getPassword()));
                adminRepository.save(admin);
            }
        }

        // Hash doctor passwords
        for (Doctor doctor : doctorRepository.findAll()) {
            if (doctor.getPassword() != null && !doctor.getPassword().startsWith("$2a$")) {
                doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
                doctorRepository.save(doctor);
            }
        }

        // Hash patient passwords
        for (Patient patient : patientRepository.findAll()) {
            if (patient.getPassword() != null && !patient.getPassword().startsWith("$2a$")) {
                patient.setPassword(passwordEncoder.encode(patient.getPassword()));
                patientRepository.save(patient);
            }
        }

        System.out.println("=== All passwords hashed successfully! ===");
    }
}
