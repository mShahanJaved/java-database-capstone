/*
 * ============================================================
 * APPOINTMENT RECORD SERVICE.JS — Appointment API Calls
 * ============================================================
 * All API operations related to appointments.
 * 
 * Used by:
 * - Doctor dashboard (view patient appointments)
 * - Patient dashboard (view my appointments)
 */

import { API_BASE_URL } from "../config/config.js";

const APPOINTMENT_API = API_BASE_URL + '/appointment';

/*
 * getAllAppointments(date, patientName, token) — Fetch appointments.
 * 
 * @param date — Filter by date (e.g., "2025-05-22")
 * @param patientName — Filter by patient name (or null)
 * @param token — JWT token for authentication
 * 
 * This function is DYNAMIC:
 * - Doctor calls it → returns their patients' appointments
 * - Patient calls it → returns their own appointments
 * 
 * The backend uses the token to determine who's asking.
 */
export async function getAllAppointments(date, patientName, token) {
    try {
        let url = `${APPOINTMENT_API}/all?token=${token}`;
        if (date) url += `&date=${encodeURIComponent(date)}`;
        if (patientName) url += `&patientName=${encodeURIComponent(patientName)}`;

        const response = await fetch(url);
        const data = await response.json();
        return data;
    } catch (error) {
        console.error("Error fetching appointments:", error);
        return [];
    }
}

/*
 * bookAppointment(data, token) — Create a new appointment.
 */
export async function bookAppointment(data, token) {
    try {
        const response = await fetch(`${APPOINTMENT_API}/book?token=${token}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        return await response.json();
    } catch (error) {
        console.error("Error booking appointment:", error);
        return null;
    }
}

/*
 * updateAppointment(id, data, token) — Update an appointment.
 */
export async function updateAppointment(id, data, token) {
    try {
        const response = await fetch(`${APPOINTMENT_API}/update/${id}?token=${token}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        return await response.json();
    } catch (error) {
        console.error("Error updating appointment:", error);
        return null;
    }
}
