-- ============================================================
-- STORED PROCEDURES for Smart Clinic Management System
-- ============================================================

-- 1. GetDailyAppointmentReportByDoctor
-- Shows all appointments for a specific doctor on a given date
DROP PROCEDURE IF EXISTS GetDailyAppointmentReportByDoctor;
DELIMITER //
CREATE PROCEDURE GetDailyAppointmentReportByDoctor(IN reportDate DATE)
BEGIN
    SELECT 
        d.name AS doctor_name,
        d.specialty,
        p.name AS patient_name,
        a.appointment_time,
        CASE a.status 
            WHEN 0 THEN 'Pending'
            WHEN 1 THEN 'Completed'
            WHEN 2 THEN 'Cancelled'
        END AS status
    FROM appointment a
    JOIN doctor d ON a.doctor_id = d.id
    JOIN patient p ON a.patient_id = p.id
    WHERE DATE(a.appointment_time) = reportDate
    ORDER BY a.appointment_time;
END //
DELIMITER ;

-- 2. GetDoctorWithMostPatientsByMonth
-- Shows which doctor had the most patients in a given month/year
DROP PROCEDURE IF EXISTS GetDoctorWithMostPatientsByMonth;
DELIMITER //
CREATE PROCEDURE GetDoctorWithMostPatientsByMonth(IN reportMonth INT, IN reportYear INT)
BEGIN
    SELECT 
        d.name AS doctor_name,
        d.specialty,
        COUNT(DISTINCT a.patient_id) AS total_patients,
        COUNT(a.id) AS total_appointments
    FROM appointment a
    JOIN doctor d ON a.doctor_id = d.id
    WHERE MONTH(a.appointment_time) = reportMonth 
      AND YEAR(a.appointment_time) = reportYear
    GROUP BY d.id, d.name, d.specialty
    ORDER BY total_patients DESC
    LIMIT 5;
END //
DELIMITER ;

-- 3. GetDoctorWithMostPatientsByYear
-- Shows which doctor had the most patients in a given year
DROP PROCEDURE IF EXISTS GetDoctorWithMostPatientsByYear;
DELIMITER //
CREATE PROCEDURE GetDoctorWithMostPatientsByYear(IN reportYear INT)
BEGIN
    SELECT 
        d.name AS doctor_name,
        d.specialty,
        COUNT(DISTINCT a.patient_id) AS total_patients,
        COUNT(a.id) AS total_appointments
    FROM appointment a
    JOIN doctor d ON a.doctor_id = d.id
    WHERE YEAR(a.appointment_time) = reportYear
    GROUP BY d.id, d.name, d.specialty
    ORDER BY total_patients DESC
    LIMIT 5;
END //
DELIMITER ;
