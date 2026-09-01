/*
 * ============================================================
 * DOCTOR SERVICES.JS — Doctor API Communication
 * ============================================================
 * All API calls related to doctors are HERE.
 * 
 * WHY separate service files?
 * 1. Clean code — dashboard files don't need to know API details
 * 2. Reusable — admin dashboard AND patient dashboard use these
 * 3. Easy to test — mock this file to test without a server
 * 4. Single source of truth — if API changes, update ONE file
 * 
 * FUNCTIONS:
 * - getDoctors() → Fetch all doctors
 * - deleteDoctor(id, token) → Remove a doctor
 * - saveDoctor(doctor, token) → Add a new doctor
 * - filterDoctors(name, time, specialty) → Search/filter doctors
 */

import { API_BASE_URL } from "../config/config.js";

/*
 * DOCTOR API ENDPOINT:
 * All doctor-related calls start with this URL.
 * 
 * Example: DOCTOR_API + "/all" = "http://localhost:8080/doctor/all"
 */
const DOCTOR_API = API_BASE_URL + '/doctor';

/*
 * getDoctors() — Fetch ALL doctors from the database.
 * 
 * API Call: GET /doctor/all
 * Returns: Array of doctor objects
 * 
 * Used by:
 * - Admin dashboard (to display all doctors)
 * - Patient dashboard (to browse available doctors)
 * 
 * ERROR HANDLING:
 * If the API fails, returns an empty array [].
 * This prevents the frontend from crashing.
 */
export async function getDoctors() {
    try {
        const response = await fetch(`${DOCTOR_API}/all`);
        const data = await response.json();
        return data;
    } catch (error) {
        console.error("Error fetching doctors:", error);
        return [];
    }
}

/*
 * deleteDoctor(id, token) — Remove a doctor from the system.
 * 
 * @param id — The doctor's unique ID
 * @param token — Admin's JWT token (for authentication)
 * 
 * API Call: DELETE /doctor/delete/{id}?token={token}
 * Returns: { success: boolean, message: string }
 * 
 * ONLY ADMINS can delete doctors!
 * The token proves the requester is an admin.
 */
export async function deleteDoctor(id, token) {
    try {
        const response = await fetch(`${DOCTOR_API}/delete/${id}?token=${token}`, {
            method: 'DELETE'
        });
        const data = await response.json();
        return { success: true, message: data.message || "Doctor deleted" };
    } catch (error) {
        console.error("Error deleting doctor:", error);
        return { success: false, message: "Failed to delete doctor" };
    }
}

/*
 * saveDoctor(doctor, token) — Add a new doctor to the system.
 * 
 * @param doctor — Object with name, email, specialty, phone, availableTimes
 * @param token — Admin's JWT token (for authentication)
 * 
 * API Call: POST /doctor/add?token={token}
 * Returns: { success: boolean, message: string }
 * 
 * The modal form collects doctor details, and this function
 * sends them to the backend to create a new doctor record.
 */
export async function saveDoctor(doctor, token) {
    try {
        const response = await fetch(`${DOCTOR_API}/add?token=${token}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(doctor)
        });
        const data = await response.json();
        return { success: true, message: data.message || "Doctor saved" };
    } catch (error) {
        console.error("Error saving doctor:", error);
        return { success: false, message: "Failed to save doctor" };
    }
}

/*
 * filterDoctors(name, time, specialty) — Search/filter doctors.
 * 
 * @param name — Doctor name to search for (or null)
 * @param time — Time filter like "AM" or "PM" (or null)
 * @param specialty — Specialty filter like "Cardiologist" (or null)
 * 
 * API Call: GET /doctor/filter?name={name}&time={time}&specialty={specialty}
 * Returns: Array of matching doctor objects
 * 
 * Used by:
 * - Admin dashboard (search bar + filter dropdowns)
 * - Patient dashboard (search bar + specialty dropdown)
 */
export async function filterDoctors(name, time, specialty) {
    try {
        // Build the URL with query parameters
        let url = `${DOCTOR_API}/filter?`;
        if (name) url += `name=${encodeURIComponent(name)}&`;
        if (time) url += `time=${encodeURIComponent(time)}&`;
        if (specialty) url += `specialty=${encodeURIComponent(specialty)}&`;

        const response = await fetch(url);
        const data = await response.json();
        return data;
    } catch (error) {
        console.error("Error filtering doctors:", error);
        return [];
    }
}
