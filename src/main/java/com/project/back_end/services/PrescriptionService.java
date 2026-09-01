package com.project.back_end.services;

import com.project.back_end.models.Prescription;
import com.project.back_end.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PrescriptionService {

    @Autowired(required = false)
    private PrescriptionRepository prescriptionRepository;

    public ResponseEntity<Map<String, String>> savePrescription(Prescription prescription) {
        if (prescriptionRepository == null) {
            return ResponseEntity.status(503).body(Map.of("error", "MongoDB not available"));
        }
        try {
            prescriptionRepository.save(prescription);
            return ResponseEntity.status(201).body(Map.of("message", "Prescription saved"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to save prescription"));
        }
    }

    public ResponseEntity<Map<String, Object>> getPrescription(Long appointmentId) {
        if (prescriptionRepository == null) {
            return ResponseEntity.ok(Map.of("message", "MongoDB not available"));
        }
        try {
            List<Prescription> prescriptions = prescriptionRepository.findByAppointmentId(appointmentId);
            if (!prescriptions.isEmpty()) {
                return ResponseEntity.ok(Map.of("prescription", prescriptions.get(0)));
            }
            return ResponseEntity.ok(Map.of("message", "No prescription found"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to fetch prescription"));
        }
    }
}
