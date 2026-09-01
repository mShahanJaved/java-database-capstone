/*
 * ============================================================
 * PRESCRIPTION SERVICES.JS — Prescription API Calls
 * ============================================================
 * All API operations related to prescriptions (stored in MongoDB).
 */

import { API_ENDPOINTS } from "../config/config.js";
import { makeRequest } from "../util.js";

/*
 * getPrescriptionByAppointment(appointmentId) — Get prescription for an appointment.
 * 
 * Each appointment can have at most ONE prescription.
 */
export async function getPrescriptionByAppointment(appointmentId) {
    return makeRequest(`${API_ENDPOINTS.PRESCRIPTIONS}/appointment/${appointmentId}`);
}

/*
 * getPrescriptionsByPatient(patientId) — Get all prescriptions for a patient.
 * 
 * A patient can have MULTIPLE prescriptions (one per appointment).
 */
export async function getPrescriptionsByPatient(patientId) {
    return makeRequest(`${API_ENDPOINTS.PRESCRIPTIONS}/patient/${patientId}`);
}

/*
 * createPrescription(prescriptionData) — Create a new prescription.
 * 
 * @param prescriptionData — Object with:
 *   - patientName, appointmentId, medication, dosage, doctorNotes
 */
export async function createPrescription(prescriptionData) {
    return makeRequest(API_ENDPOINTS.PRESCRIPTIONS, {
        method: "POST",
        body: JSON.stringify(prescriptionData),
    });
}

/*
 * updatePrescription(id, data) — Update an existing prescription.
 */
export async function updatePrescription(id, data) {
    return makeRequest(`${API_ENDPOINTS.PRESCRIPTIONS}/${id}`, {
        method: "PUT",
        body: JSON.stringify(data),
    });
}
