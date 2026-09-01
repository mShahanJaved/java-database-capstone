package com.project.back_end.services;

import com.project.back_end.models.Admin;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repository.AdminRepository;
import com.project.back_end.repository.DoctorRepository;
import com.project.back_end.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired private AdminRepository adminRepository;
    @Autowired private DoctorRepository doctorRepository;
    @Autowired private PatientRepository patientRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Try ADMIN
        Admin admin = adminRepository.findByUsername(username);
        if (admin != null) {
            return new User(admin.getUsername(), admin.getPassword(), new ArrayList<>());
        }

        // Try DOCTOR
        Doctor doctor = doctorRepository.findByEmail(username);
        if (doctor != null) {
            return new User(doctor.getEmail(), doctor.getPassword(), new ArrayList<>());
        }

        // Try PATIENT
        var patientOpt = patientRepository.findByEmail(username);
        if (patientOpt.isPresent()) {
            Patient patient = patientOpt.get();
            return new User(patient.getEmail(), patient.getPassword(), new ArrayList<>());
        }

        throw new UsernameNotFoundException("User not found: " + username);
    }
}
