/*
 * ============================================================
 * PATIENT SERVICES.JS — Patient API Communication
 * ============================================================
 * All API calls related to patients are HERE.
 * 
 * FUNCTIONS:
 * - patientSignup(data) → Register a new patient
 * - patientLogin(data) → Login as a patient
 * - getPatientData(token) → Get logged-in patient's info
 * - getPatientAppointments(id, token, user) → Get appointments
 * - filterAppointments(condition, name, token) → Filter appointments
 */

import { API_BASE_URL } from "../config/config.js";

/*
 * PATIENT API ENDPOINT:
 * All patient-related calls start with this URL.
 */
const PATIENT_API = API_BASE_URL + '/patient';

/*
 * patientSignup(data) — Register a new patient.
 * 
 * @param data — Object with name, email, password, phone, address
 * 
 * API Call: POST /patient/signup
 * Returns: { success: boolean, message: string }
 * 
 * Called when a new patient clicks "Sign Up" and fills the form.
 */
export async function patientSignup(data) {
    try {
        const response = await fetch(`${PATIENT_API}/signup`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        const result = await response.json();
        return { success: true, message: result.message || "Signup successful" };
    } catch (error) {
        console.error("Signup error:", error);
        return { success: false, message: "Signup failed" };
    }
}

/*
 * patientLogin(data) — Login as a patient.
 * 
 * @param data — Object with email and password
 * 
 * API Call: POST /patient/login
 * Returns: The full fetch response (so frontend can extract token)
 * 
 * Returns the full response so the frontend can:
 * 1. Check if response.ok
 * 2. Extract the JWT token
 * 3. Save it to localStorage
 */
export async function patientLogin(data) {
    try {
        console.log("Patient login data:", data);
        const response = await fetch(`${PATIENT_API}/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        return response;
    } catch (error) {
        console.error("Login error:", error);
        throw error;
    }
}

/*
 * getPatientData(token) — Get the logged-in patient's profile.
 * 
 * @param token — JWT token from localStorage
 * 
 * API Call: GET /patient/me
 * Returns: Patient object (name, id, email, phone, etc.)
 * 
 * Used when:
 * - Booking an appointment (need patient ID)
 * - Displaying patient info on the dashboard
 */
export async function getPatientData(token) {
    try {
        const response = await fetch(`${PATIENT_API}/me`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        const data = await response.json();
        return data;
    } catch (error) {
        console.error("Error fetching patient data:", error);
        return null;
    }
}

/*
 * getPatientAppointments(id, token, user) — Get appointments.
 * 
 * @param id — Patient's ID
 * @param token — JWT token
 * @param user — "patient" or "doctor" (determines which API endpoint)
 * 
 * This function is DYNAMIC — it works for both:
 * - Patient dashboard (user="patient") → shows MY appointments
 * - Doctor dashboard (user="doctor") → shows MY patients' appointments
 * 
 * The backend uses the 'user' parameter to determine what data to return.
 */
export async function getPatientAppointments(id, token, user) {
    try {
        const response = await fetch(
            `${PATIENT_API}/appointments/${id}?token=${token}&user=${user}`
        );
        const data = await response.json();
        return data;
    } catch (error) {
        console.error("Error fetching appointments:", error);
        return null;
    }
}

/*
 * filterAppointments(condition, name, token) — Filter appointments.
 * 
 * @param condition — Filter like "pending" or "consulted"
 * @param name — Patient name to search for
 * @param token — JWT token
 * 
 * Used by the doctor dashboard to filter patient records.
 */
export async function filterAppointments(condition, name, token) {
    try {
        let url = `${PATIENT_API}/appointments/filter?token=${token}`;
        if (condition) url += `&condition=${encodeURIComponent(condition)}`;
        if (name) url += `&name=${encodeURIComponent(name)}`;

        const response = await fetch(url);
        const data = await response.json();
        return data;
    } catch (error) {
        console.error("Error filtering appointments:", error);
        alert("Error filtering appointments");
        return [];
    }
}
