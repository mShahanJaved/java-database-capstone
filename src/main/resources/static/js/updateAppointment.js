/*
 * UPDATE APPOINTMENT.JS — Handles appointment update form
 */
import { API_ENDPOINTS } from "./config/config.js";
import { makeRequest, showAlert } from "./util.js";

document.addEventListener("DOMContentLoaded", async () => {
    const params = new URLSearchParams(window.location.search);
    const appointmentId = params.get("appointmentId");

    if (appointmentId) {
        try {
            const apt = await makeRequest(`${API_ENDPOINTS.APPOINTMENTS}/${appointmentId}`);
            document.getElementById("patientName").value = apt.patient?.name || "";
            document.getElementById("doctorName").value = apt.doctor?.name || "";
            document.getElementById("appointmentDate").value = apt.appointmentTime
                ? new Date(apt.appointmentTime).toISOString().split("T")[0]
                : "";
        } catch (error) {
            console.error("Failed to load appointment:", error);
        }
    }

    const form = document.getElementById("updateAppointmentForm");
    if (form) {
        form.addEventListener("submit", async (e) => {
            e.preventDefault();
            const data = {
                appointmentDate: document.getElementById("appointmentDate").value,
                appointmentTime: document.getElementById("timeSlot").value,
            };

            try {
                await makeRequest(`${API_ENDPOINTS.APPOINTMENTS}/${appointmentId}`, {
                    method: "PUT",
                    body: JSON.stringify(data),
                });
                showAlert("Appointment updated successfully!", "success");
                setTimeout(() => window.history.back(), 1500);
            } catch (error) {
                showAlert("Failed to update appointment", "error");
            }
        });
    }
});
