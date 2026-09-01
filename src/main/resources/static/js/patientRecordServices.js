/*
 * PATIENT RECORD SERVICES.JS — Loads patient appointment history
 */
import { API_ENDPOINTS } from "./config/config.js";
import { makeRequest } from "./util.js";

document.addEventListener("DOMContentLoaded", async () => {
    const tbody = document.getElementById("patientRecordBody");
    if (!tbody) return;

    try {
        const params = new URLSearchParams(window.location.search);
        const patientId = params.get("patientId");
        const appointments = await makeRequest(
            `${API_ENDPOINTS.APPOINTMENTS}?patientId=${patientId}`
        );

        if (!appointments || appointments.length === 0) {
            tbody.innerHTML = '<tr><td colspan="4" class="noPatientRecord">No records found.</td></tr>';
            return;
        }

        appointments.forEach((apt) => {
            const row = document.createElement("tr");
            const date = apt.appointmentTime
                ? new Date(apt.appointmentTime).toISOString().split("T")[0]
                : "N/A";

            row.innerHTML = `
                <td>${date}</td>
                <td>${apt.id}</td>
                <td>${apt.patient?.id || "N/A"}</td>
                <td>
                    <button class="prescription-btn" onclick="viewPrescription(${apt.id})">
                        📋
                    </button>
                </td>
            `;
            tbody.appendChild(row);
        });
    } catch (error) {
        tbody.innerHTML = '<tr><td colspan="4" class="noPatientRecord">Failed to load records.</td></tr>';
    }
});

window.viewPrescription = async function (appointmentId) {
    const modal = document.getElementById("modal");
    const modalBody = document.getElementById("modal-body");
    if (!modal || !modalBody) return;

    try {
        const rx = await makeRequest(`${API_ENDPOINTS.PRESCRIPTIONS}/appointment/${appointmentId}`);
        modalBody.innerHTML = `
            <div class="modal-form">
                <h3>View <span style="color:#c0392b;">Prescription</span></h3>
                <div class="form-group"><label>Patient Name</label><input class="input-field" value="${rx.patientName}" readonly /></div>
                <div class="form-group"><label>Medicine Names</label><input class="input-field" value="${rx.medication}" readonly /></div>
                <div class="form-group"><label>Dosage Instructions</label><textarea class="input-field" readonly>${rx.dosage}</textarea></div>
                <div class="form-group"><label>Additional Notes</label><textarea class="input-field" readonly>${rx.doctorNotes || "NA"}</textarea></div>
                <button class="submit-btn" onclick="document.getElementById('modal').style.display='none'">Cancel</button>
            </div>
        `;
    } catch {
        modalBody.innerHTML = '<div class="modal-form"><h3>No prescription found</h3><button class="submit-btn" onclick="document.getElementById(\'modal\').style.display=\'none\'">Cancel</button></div>';
    }

    modal.style.display = "flex";
    document.getElementById("closeModal").onclick = () => modal.style.display = "none";
};
