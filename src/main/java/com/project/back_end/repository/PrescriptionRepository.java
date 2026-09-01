package com.project.back_end.repository;

import com.project.back_end.models.Prescription;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * PRESCRIPTION REPOSITORY — MongoDB operations.
 * 
 * Extends MongoRepository (not JpaRepository) because
 * prescriptions are stored in MongoDB, not MySQL.
 */
@Repository
public interface PrescriptionRepository extends MongoRepository<Prescription, String> {

    /*
     * findByAppointmentId — Find prescriptions for a specific appointment.
     * 
     * MongoDB generates the query automatically from the method name.
     */
    List<Prescription> findByAppointmentId(Long appointmentId);

    // Find prescriptions by patient name
    List<Prescription> findByPatientName(String patientName);
}
