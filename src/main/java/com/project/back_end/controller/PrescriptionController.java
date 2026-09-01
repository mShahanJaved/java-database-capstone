package com.project.back_end.controller;

import com.project.back_end.models.Prescription;
import com.project.back_end.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/prescription")
public class PrescriptionController {

    @Autowired(required = false)
    private PrescriptionRepository prescriptionRepository;

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<?> getPrescriptionByAppointment(@PathVariable Long appointmentId) {
        if (prescriptionRepository == null) {
            return ResponseEntity.ok(Map.of("message", "MongoDB not available"));
        }
        List<Prescription> prescriptions = prescriptionRepository.findByAppointmentId(appointmentId);
        if (prescriptions.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(prescriptions.get(0));
    }

    @GetMapping("/patient/{patientName}")
    public ResponseEntity<List<Prescription>> getPrescriptionsByPatient(@PathVariable String patientName) {
        if (prescriptionRepository == null) {
            return ResponseEntity.ok(List.of());
        }
        List<Prescription> prescriptions = prescriptionRepository.findByPatientName(patientName);
        return ResponseEntity.ok(prescriptions);
    }

    @PostMapping
    public ResponseEntity<?> createPrescription(@RequestBody Prescription prescription) {
        if (prescriptionRepository == null) {
            return ResponseEntity.ok(Map.of("success", false, "message", "MongoDB not available"));
        }
        Prescription saved = prescriptionRepository.save(prescription);
        return ResponseEntity.ok(Map.of("success", true, "message", "Prescription saved", "prescription", saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePrescription(@PathVariable String id, @RequestBody Prescription prescription) {
        if (prescriptionRepository == null) {
            return ResponseEntity.ok(Map.of("success", false, "message", "MongoDB not available"));
        }
        Prescription existing = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));
        existing.setMedication(prescription.getMedication());
        existing.setDosage(prescription.getDosage());
        existing.setDoctorNotes(prescription.getDoctorNotes());
        prescriptionRepository.save(existing);
        return ResponseEntity.ok(Map.of("success", true, "message", "Prescription updated"));
    }
}
