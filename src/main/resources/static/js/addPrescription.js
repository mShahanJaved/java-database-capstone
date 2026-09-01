/*
 * ADD PRESCRIPTION.JS — Handles prescription creation form
 */
import { API_ENDPOINTS } from "./config/config.js";
import { makeRequest, showAlert } from "./util.js";

document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("prescriptionForm");
    if (!form) return;

    // Get appointment ID from URL params
    const params = new URLSearchParams(window.location.search);
    const appointmentId = params.get("appointmentId");

    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        const prescription = {
            appointmentId: parseInt(appointmentId),
            patientName: document.getElementById("patientName").value,
            medication: document.getElementById("medication").value,
            dosage: document.getElementById("dosage").value,
            doctorNotes: document.getElementById("doctorNotes").value,
        };

        try {
            await makeRequest(API_ENDPOINTS.PRESCRIPTIONS, {
                method: "POST",
                body: JSON.stringify(prescription),
            });
            showAlert("Prescription saved successfully!", "success");
            setTimeout(() => window.history.back(), 1500);
        } catch (error) {
            showAlert("Failed to save prescription", "error");
        }
    });
});
