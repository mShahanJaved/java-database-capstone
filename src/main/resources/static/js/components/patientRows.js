/*
 * ============================================================
 * PATIENT ROWS.JS — Patient Table Row Component
 * ============================================================
 * Creates table rows for the doctor's appointment list.
 * Each row shows: Patient ID, Name, Phone, Email, Prescription button.
 */

/*
 * createPatientRow(appointment) — Create a single table row.
 * 
 * @param appointment — Object with patient info and appointment details
 * @returns DOM <tr> element
 */
export function createPatientRow(appointment) {
    const row = document.createElement("tr");

    const patientName = appointment.patient?.name || "N/A";
    const patientId = appointment.patient?.id || "N/A";
    const patientPhone = appointment.patient?.phone || "N/A";
    const patientEmail = appointment.patient?.email || "N/A";

    row.innerHTML = `
        <td>${patientId}</td>
        <td>${patientName}</td>
        <td>${patientPhone}</td>
        <td>${patientEmail}</td>
        <td>
            <button class="prescription-btn" onclick="viewPrescription(${appointment.id})">
                📋
            </button>
        </td>
    `;

    return row;
}

/*
 * createAppointmentRow(appointment) — Create a row for the patient's appointment list.
 * 
 * Used by the patient portal to show their own appointments.
 * Columns: Date, Appointment ID, Patient ID, Prescription icon
 */
export function createAppointmentRow(appointment) {
    const row = document.createElement("tr");

    const date = appointment.appointmentTime
        ? new Date(appointment.appointmentTime).toISOString().split("T")[0]
        : "N/A";

    row.innerHTML = `
        <td>${date}</td>
        <td>${appointment.id}</td>
        <td>${appointment.patient?.id || "N/A"}</td>
        <td>
            <button class="prescription-btn" onclick="viewPrescription(${appointment.id})">
                📋
            </button>
        </td>
    `;

    return row;
}
