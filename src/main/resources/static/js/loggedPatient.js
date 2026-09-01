/*
 * LOGGED PATIENT.JS — Logged-in patient dashboard logic
 * 
 * Similar to patientDashboard.js but for authenticated patients.
 * Patients can book appointments and view their records.
 */
import { API_ENDPOINTS } from "./config/config.js";
import { makeRequest, showAlert, getToken } from "./util.js";

let allDoctors = [];

window.renderContent = async function () {
    const token = getToken();
    const role = localStorage.getItem("userRole");

    if (role !== "loggedPatient" || !token) {
        window.location.href = "/";
        return;
    }

    await loadDoctors();
    setupSearchAndFilters();
    setupBooking();
};

async function loadDoctors() {
    try {
        allDoctors = await makeRequest(API_ENDPOINTS.DOCTORS);
        renderDoctorCards(allDoctors);
    } catch (error) {
        console.error("Failed to load doctors:", error);
    }
}

function renderDoctorCards(doctors) {
    const content = document.getElementById("content");
    if (!content) return;
    content.innerHTML = "";

    doctors.forEach((doctor) => {
        const card = document.createElement("div");
        card.classList.add("doctor-card");
        const times = doctor.availableTimes ? doctor.availableTimes.join(", ") : "N/A";

        card.innerHTML = `
            <div class="doctor-info">
                <h3>${doctor.name}</h3>
                <p><strong>Specialization:</strong> ${doctor.specialty}</p>
                <p><strong>Email:</strong> ${doctor.email}</p>
                <p><strong>Available:</strong> ${times}</p>
            </div>
            <div class="card-actions">
                <button class="book-btn" data-doctor='${JSON.stringify(doctor)}'>Book Now</button>
            </div>
        `;

        card.querySelector(".book-btn").addEventListener("click", () => openBookingModal(doctor));
        content.appendChild(card);
    });
}

function setupSearchAndFilters() {
    document.getElementById("searchBar")?.addEventListener("input", applyFilters);
    document.getElementById("filterTime")?.addEventListener("change", applyFilters);
    document.getElementById("filterSpecialty")?.addEventListener("change", applyFilters);
}

function applyFilters() {
    const search = document.getElementById("searchBar")?.value.toLowerCase() || "";
    const time = document.getElementById("filterTime")?.value || "";
    const spec = document.getElementById("filterSpecialty")?.value || "";

    const filtered = allDoctors.filter((d) => {
        const matchSearch = !search || d.name.toLowerCase().includes(search) || d.specialty.toLowerCase().includes(search);
        const matchTime = !time || (d.availableTimes && d.availableTimes.some((t) => t.includes(time)));
        const matchSpec = !spec || d.specialty === spec;
        return matchSearch && matchTime && matchSpec;
    });
    renderDoctorCards(filtered);
}

function openBookingModal(doctor) {
    const modal = document.getElementById("bookingModal");
    document.getElementById("doctorName").value = doctor.name;

    const timeSelect = document.getElementById("timeSlot");
    timeSelect.innerHTML = '<option value="">Select a time slot</option>';
    (doctor.availableTimes || []).forEach((slot) => {
        const opt = document.createElement("option");
        opt.value = slot;
        opt.textContent = slot;
        timeSelect.appendChild(opt);
    });

    modal.classList.add("active");
}

function setupBooking() {
    document.getElementById("confirmBooking")?.addEventListener("click", async () => {
        const date = document.getElementById("appointmentDate")?.value;
        const time = document.getElementById("timeSlot")?.value;
        if (!date || !time) { alert("Please select date and time."); return; }

        try {
            await makeRequest(API_ENDPOINTS.APPOINTMENTS, {
                method: "POST",
                body: JSON.stringify({ doctorName: document.getElementById("doctorName").value, appointmentDate: date, appointmentTime: time }),
            });
            showAlert("Appointment booked!", "success");
            document.getElementById("bookingModal").classList.remove("active");
        } catch (e) {
            showAlert("Booking failed", "error");
        }
    });
}
