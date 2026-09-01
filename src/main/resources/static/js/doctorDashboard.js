/*
 * ============================================================
 * DOCTOR DASHBOARD.JS — Doctor Portal Logic
 * ============================================================
 * Handles:
 * 1. Loading patient appointments for this doctor
 * 2. Searching by patient name
 * 3. Filtering by date (today or custom)
 * 4. Viewing/adding prescriptions
 * 
 * IMPORTS:
 * - getAllAppointments → Fetches appointments from API
 * - createPatientRow → Creates table row for each patient
 */

import { getAllAppointments } from "./services/appointmentRecordService.js";
import { createPatientRow } from "./components/patientRows.js";

/*
 * GLOBAL VARIABLES:
 * These track the current state of the dashboard.
 */
const patientTableBody = document.getElementById("patientTableBody");
let selectedDate = new Date().toISOString().split('T')[0]; // Today's date
let token = localStorage.getItem("token");
let patientName = null;

/*
 * SEARCH BAR:
 * When the doctor types a patient name, we filter the results.
 * If the search is empty, we reset to show all appointments.
 */
document.getElementById("searchBar").addEventListener("input", (e) => {
    patientName = e.target.value || null;
    loadAppointments();
});

/*
 * TODAY'S APPOINTMENTS BUTTON:
 * Resets the date filter to today.
 */
document.getElementById("todayButton").addEventListener("click", () => {
    selectedDate = new Date().toISOString().split('T')[0];
    document.getElementById("datePicker").value = selectedDate;
    loadAppointments();
});

/*
 * DATE PICKER:
 * When the doctor selects a different date,
 * we load appointments for that date.
 */
document.getElementById("datePicker").addEventListener("change", (e) => {
    selectedDate = e.target.value;
    loadAppointments();
});

/*
 * loadAppointments() — Fetch and display appointments.
 * 
 * This is the CORE function of the doctor dashboard.
 * It:
 * 1. Calls the API with current filters (date, patient name)
 * 2. Clears the table
 * 3. Creates a row for each appointment
 * 4. Shows "No appointments" if empty
 */
async function loadAppointments() {
    try {
        const appointments = await getAllAppointments(selectedDate, patientName, token);

        // Clear existing rows
        patientTableBody.innerHTML = "";

        if (!appointments || appointments.length === 0) {
            // Show "no records" message
            patientTableBody.innerHTML = `
                <tr>
                    <td colspan="5" class="noPatientRecord">
                        No Appointments found for today
                    </td>
                </tr>
            `;
            return;
        }

        // Create a row for each appointment
        appointments.forEach((appointment) => {
            const row = createPatientRow(appointment);
            patientTableBody.appendChild(row);
        });
    } catch (error) {
        console.error("Error loading appointments:", error);
        patientTableBody.innerHTML = `
            <tr>
                <td colspan="5" class="noPatientRecord">
                    Error loading appointments
                </td>
            </tr>
        `;
    }
}

/*
 * INITIALIZE:
 * Load today's appointments when the page loads.
 */
loadAppointments();
