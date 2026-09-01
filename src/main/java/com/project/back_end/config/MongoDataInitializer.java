package com.project.back_end.config;

import com.project.back_end.models.Prescription;
import com.project.back_end.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MongoDataInitializer implements CommandLineRunner {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Override
    public void run(String... args) throws Exception {
        // Only insert if collection is empty
        if (prescriptionRepository.count() == 0) {
            prescriptionRepository.save(new Prescription("Jane Doe", 1L, "Aspirin", "100mg", "Take once daily after meals"));
            prescriptionRepository.save(new Prescription("John Smith", 2L, "Amoxicillin", "500mg", "Take 3 times daily for 7 days"));
            prescriptionRepository.save(new Prescription("Emily Rose", 3L, "Ibuprofen", "200mg", "Take as needed for pain, max 3 per day"));
            prescriptionRepository.save(new Prescription("Michael Jordan", 4L, "Paracetamol", "500mg", "Take every 6 hours if fever persists"));
            prescriptionRepository.save(new Prescription("Olivia Moon", 5L, "Metformin", "850mg", "Take twice daily with meals"));
            System.out.println("=== 5 sample prescriptions inserted into MongoDB! ===");
        } else {
            System.out.println("=== Prescriptions already exist, skipping insert ===");
        }
    }
}
