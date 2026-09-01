package com.project.back_end.services;

import com.project.back_end.dto.Login;
import com.project.back_end.models.Doctor;
import com.project.back_end.repository.AppointmentRepository;
import com.project.back_end.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    @Autowired private DoctorRepository doctorRepository;
    @Autowired private AppointmentRepository appointmentRepository;
    @Autowired private TokenService tokenService;
    @Autowired private PasswordEncoder passwordEncoder;

    public List<Doctor> getDoctors() {
        return doctorRepository.findAll();
    }

    // Alias used by DoctorController
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
    }

    public Doctor createDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public List<Doctor> searchDoctors(String name) {
        return doctorRepository.findByNameLike(name);
    }

    public List<Doctor> getDoctorsBySpecialization(String specialty) {
        return doctorRepository.findBySpecialtyIgnoreCase(specialty);
    }

    public List<String> getDoctorAvailability(Long doctorId, LocalDate date) {
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
        if (doctor == null) return Collections.emptyList();

        List<String> allSlots = doctor.getAvailableTimes() != null ?
                new ArrayList<>(doctor.getAvailableTimes()) : new ArrayList<>();

        // Get booked slots for this date
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);
        var booked = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctorId, start, end);

        // Extract just the time strings from booked appointments
        List<String> bookedTimes = booked.stream()
                .map(a -> {
                    if (a.getAppointmentTime() != null) {
                        return a.getAppointmentTime().toLocalTime().toString();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        allSlots.removeAll(bookedTimes);
        return allSlots;
    }

    public int saveDoctor(Doctor doctor) {
        if (doctorRepository.findByEmail(doctor.getEmail()) != null) {
            return -1;
        }
        try {
            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public int updateDoctor(Doctor doctor) {
        if (doctorRepository.findById(doctor.getId()).isEmpty()) {
            return -1;
        }
        try {
            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public int deleteDoctor(long id) {
        if (doctorRepository.findById(id).isEmpty()) {
            return -1;
        }
        try {
            appointmentRepository.deleteAllByDoctorId(id);
            doctorRepository.deleteById(id);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public ResponseEntity<Map<String, String>> validateDoctor(Login login) {
        Doctor doctor = doctorRepository.findByEmail(login.getIdentifier());
        if (doctor != null && passwordEncoder.matches(login.getPassword(), doctor.getPassword())) {
            String token = tokenService.generateToken(doctor.getEmail());
            return ResponseEntity.ok(Map.of("token", token));
        }
        return ResponseEntity.badRequest().body(Map.of("error", "Invalid credentials"));
    }

    public Map<String, Object> findDoctorByName(String name) {
        List<Doctor> doctors = doctorRepository.findByNameLike(name);
        return Map.of("doctors", doctors);
    }

    public List<Doctor> filterDoctorsByNameSpecilityandTime(String name, String specialty, String amOrPm) {
        List<Doctor> doctors = doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
        return filterByTime(doctors, amOrPm);
    }

    public List<Doctor> filterDoctorByNameAndTime(String name, String amOrPm) {
        List<Doctor> doctors = doctorRepository.findByNameLike(name);
        return filterByTime(doctors, amOrPm);
    }

    public List<Doctor> filterDoctorByNameAndSpecility(String name, String specialty) {
        return doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
    }

    public List<Doctor> filterDoctorByTimeAndSpecility(String specialty, String amOrPm) {
        List<Doctor> doctors = doctorRepository.findBySpecialtyIgnoreCase(specialty);
        return filterByTime(doctors, amOrPm);
    }

    public List<Doctor> filterDoctorBySpecility(String specialty) {
        return doctorRepository.findBySpecialtyIgnoreCase(specialty);
    }

    public List<Doctor> filterDoctorsByTime(String amOrPm) {
        List<Doctor> doctors = doctorRepository.findAll();
        return filterByTime(doctors, amOrPm);
    }

    private List<Doctor> filterByTime(List<Doctor> doctors, String amOrPm) {
        if (amOrPm == null || amOrPm.isEmpty()) return doctors;
        return doctors.stream()
                .filter(d -> d.getAvailableTimes() != null &&
                        d.getAvailableTimes().stream()
                                .anyMatch(t -> t.toUpperCase().contains(amOrPm.toUpperCase())))
                .collect(Collectors.toList());
    }
}
