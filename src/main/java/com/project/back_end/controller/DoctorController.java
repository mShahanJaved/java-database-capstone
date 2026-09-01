package com.project.back_end.controller;

import com.project.back_end.models.Doctor;
import com.project.back_end.services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/*
 * DOCTOR CONTROLLER — REST API for doctor operations.
 * 
 * ENDPOINTS:
 * - GET  /doctor/all → List all doctors (public)
 * - GET  /doctor/{id} → Get doctor by ID
 * - GET  /doctor/filter → Search/filter doctors (public)
 * - POST /doctor/add → Add a new doctor (admin only)
 * - DELETE /doctor/delete/{id} → Delete a doctor (admin only)
 */
@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /*
     * GET ALL DOCTORS — Public endpoint.
     * 
     * Used by:
     * - Patient dashboard (browse all doctors)
     * - Admin dashboard (manage doctors)
     * 
     * No authentication required!
     */
    @GetMapping("/all")
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }

    /*
     * GET DOCTOR BY ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        Doctor doctor = doctorService.getDoctorById(id);
        return ResponseEntity.ok(doctor);
    }

    /*
     * FILTER DOCTORS — Public endpoint.
     * 
     * Used by search bar and filter dropdowns.
     * Accepts optional query parameters:
     * - name → Search by doctor name
     * - time → Filter by available time (AM/PM)
     * - specialty → Filter by specialty
     */
    @GetMapping("/filter")
    public ResponseEntity<List<Doctor>> filterDoctors(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String time,
            @RequestParam(required = false) String specialty
    ) {
        List<Doctor> doctors;

        // If name is provided, search by name or specialty
        if (name != null && !name.isEmpty()) {
            doctors = doctorService.searchDoctors(name);
        }
        // If specialty is provided, filter by specialty
        else if (specialty != null && !specialty.isEmpty()) {
            doctors = doctorService.getDoctorsBySpecialization(specialty);
        }
        // Otherwise, return all doctors
        else {
            doctors = doctorService.getAllDoctors();
        }

        // Apply time filter if provided
        if (time != null && !time.isEmpty()) {
            doctors = doctors.stream()
                    .filter(d -> d.getAvailableTimes() != null &&
                            d.getAvailableTimes().stream()
                                    .anyMatch(t -> t.toUpperCase().contains(time.toUpperCase())))
                    .toList();
        }

        return ResponseEntity.ok(doctors);
    }

    /*
     * ADD DOCTOR — Admin only.
     * 
     * Request: POST /doctor/add?token={jwt_token}
     * Body: { "name": "Dr. Smith", "email": "dr.smith@clinic.com", ... }
     */
    @PostMapping("/add")
    public ResponseEntity<?> addDoctor(
            @RequestBody Doctor doctor,
            @RequestParam(required = false) String token
    ) {
        try {
            // Hash the password before saving
            if (doctor.getPassword() != null) {
                doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
            }
            Doctor savedDoctor = doctorService.createDoctor(doctor);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Doctor added successfully",
                    "doctor", savedDoctor
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    /*
     * DELETE DOCTOR — Admin only.
     * 
     * Request: DELETE /doctor/delete/{id}?token={jwt_token}
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDoctor(
            @PathVariable Long id,
            @RequestParam(required = false) String token
    ) {
        try {
            doctorService.deleteDoctor(id);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Doctor deleted successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
}
