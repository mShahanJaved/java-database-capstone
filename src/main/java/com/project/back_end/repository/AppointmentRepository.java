package com.project.back_end.repository;

import com.project.back_end.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Find appointments for a doctor within a time range
    @Query("SELECT a FROM Appointment a LEFT JOIN FETCH a.doctor LEFT JOIN FETCH a.patient WHERE a.doctor.id = :doctorId AND a.appointmentTime BETWEEN :start AND :end")
    List<Appointment> findByDoctorIdAndAppointmentTimeBetween(
            @Param("doctorId") Long doctorId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    // Filter by doctor ID, patient name (partial, case-insensitive), and time range
    @Query("SELECT a FROM Appointment a LEFT JOIN FETCH a.doctor LEFT JOIN FETCH a.patient WHERE a.doctor.id = :doctorId AND LOWER(a.patient.name) LIKE LOWER(CONCAT('%', :patientName, '%')) AND a.appointmentTime BETWEEN :start AND :end")
    List<Appointment> findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
            @Param("doctorId") Long doctorId,
            @Param("patientName") String patientName,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    // Delete all appointments for a doctor
    @Modifying
    @Transactional
    @Query("DELETE FROM Appointment a WHERE a.doctor.id = :doctorId")
    void deleteAllByDoctorId(@Param("doctorId") Long doctorId);

    // Find all appointments for a patient
    List<Appointment> findByPatientId(Long patientId);

    // Find patient appointments by status
    List<Appointment> findByPatient_IdAndStatusOrderByAppointmentTimeAsc(Long patientId, int status);

    // Filter by doctor name and patient ID
    @Query("SELECT a FROM Appointment a LEFT JOIN FETCH a.doctor LEFT JOIN FETCH a.patient WHERE LOWER(a.doctor.name) LIKE LOWER(CONCAT('%', :doctorName, '%')) AND a.patient.id = :patientId")
    List<Appointment> filterByDoctorNameAndPatientId(
            @Param("doctorName") String doctorName,
            @Param("patientId") Long patientId);

    // Filter by doctor name, patient ID, and status
    @Query("SELECT a FROM Appointment a LEFT JOIN FETCH a.doctor LEFT JOIN FETCH a.patient WHERE LOWER(a.doctor.name) LIKE LOWER(CONCAT('%', :doctorName, '%')) AND a.patient.id = :patientId AND a.status = :status")
    List<Appointment> filterByDoctorNameAndPatientIdAndStatus(
            @Param("doctorName") String doctorName,
            @Param("patientId") Long patientId,
            @Param("status") int status);
}
