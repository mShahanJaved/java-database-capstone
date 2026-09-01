/*
 * APPOINTMENT ROW.JS — Table row component for appointments
 */

/*
 * createAppointmentRow(appointment) — Create a table row for an appointment.
 * 
 * Used in patient's appointment list and doctor's appointment view.
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
