package com.project.back_end.services;

import com.project.back_end.dto.AppointmentDTO;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Patient;
import com.project.back_end.repository.AppointmentRepository;
import com.project.back_end.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PatientService {

    @Autowired private PatientRepository patientRepository;
    @Autowired private AppointmentRepository appointmentRepository;
    @Autowired private TokenService tokenService;

    public int createPatient(Patient patient) {
        try {
            patientRepository.save(patient);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public ResponseEntity<Map<String, Object>> getPatientAppointment(Long id, String token) {
        String email = tokenService.extractIdentifier(token);
        var patientOpt = patientRepository.findByEmail(email);

        if (patientOpt.isEmpty() || !patientOpt.get().getId().equals(id)) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }

        List<Appointment> appointments = appointmentRepository.findByPatientId(id);
        List<AppointmentDTO> dtos = convertToDTOs(appointments);

        return ResponseEntity.ok(Map.of("appointments", dtos));
    }

    public ResponseEntity<Map<String, Object>> filterByCondition(String condition, Long id) {
        int status = "past".equalsIgnoreCase(condition) ? 1 : 0;
        List<Appointment> appointments = appointmentRepository.findByPatient_IdAndStatusOrderByAppointmentTimeAsc(id, status);
        return ResponseEntity.ok(Map.of("appointments", convertToDTOs(appointments)));
    }

    public ResponseEntity<Map<String, Object>> filterByDoctor(String name, Long patientId) {
        List<Appointment> appointments = appointmentRepository.filterByDoctorNameAndPatientId(name, patientId);
        return ResponseEntity.ok(Map.of("appointments", convertToDTOs(appointments)));
    }

    public ResponseEntity<Map<String, Object>> filterByDoctorAndCondition(String condition, String name, long patientId) {
        int status = "past".equalsIgnoreCase(condition) ? 1 : 0;
        List<Appointment> appointments = appointmentRepository.filterByDoctorNameAndPatientIdAndStatus(name, patientId, status);
        return ResponseEntity.ok(Map.of("appointments", convertToDTOs(appointments)));
    }

    public ResponseEntity<Map<String, Object>> getPatientDetails(String token) {
        String email = tokenService.extractIdentifier(token);
        var patientOpt = patientRepository.findByEmail(email);

        if (patientOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Patient not found"));
        }

        Patient patient = patientOpt.get();
        return ResponseEntity.ok(Map.of(
                "id", patient.getId(),
                "name", patient.getName(),
                "email", patient.getEmail(),
                "phone", patient.getPhone(),
                "address", patient.getAddress()
        ));
    }

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
