/*
 * ============================================================
 * RENDER.JS — Dynamic Content Rendering
 * ============================================================
 * Functions that create HTML elements dynamically.
 * 
 * WHY render dynamically?
 * Instead of hardcoding HTML for every doctor/patient/appointment,
 * we create templates that work with ANY data.
 * 
 * Example: renderDoctorCard(doctor) works for ALL 25 doctors.
 * Just pass different data, get different cards.
 */

/*
 * renderDoctorCards(doctors, containerId) — Render a list of doctor cards.
 * 
 * @param doctors — Array of doctor objects from the API
 * @param containerId — The DOM element ID to inject cards into
 * 
 * This function:
 * 1. Clears the container (removes old cards)
 * 2. Creates a card for each doctor
 * 3. Appends all cards to the container
 */
export function renderDoctorCards(doctors, containerId = "content") {
    const container = document.getElementById(containerId);
    if (!container) return;

    container.innerHTML = "";

    if (!doctors || doctors.length === 0) {
        container.innerHTML = '<p class="noPatientRecord">No doctors found.</p>';
        return;
    }

    doctors.forEach((doctor) => {
        const card = createDoctorCardElement(doctor);
        container.appendChild(card);
    });
}

/*
 * createDoctorCardElement(doctor) — Create a single doctor card.
 * 
 * Returns a DOM element (not a string).
 * This is better than innerHTML because:
 * 1. Event listeners work immediately
 * 2. No XSS vulnerabilities (cross-site scripting)
 * 3. Browser handles the DOM efficiently
 */
function createDoctorCardElement(doctor) {
    const card = document.createElement("div");
    card.classList.add("doctor-card");

    const role = localStorage.getItem("userRole");

    // Doctor info section
    const infoDiv = document.createElement("div");
    infoDiv.classList.add("doctor-info");

    const name = document.createElement("h3");
    name.textContent = doctor.name;

    const specialty = document.createElement("p");
    specialty.innerHTML = `<strong>Specialization:</strong> ${doctor.specialty}`;

    const email = document.createElement("p");
    email.innerHTML = `<strong>Email:</strong> ${doctor.email}`;

    const availability = document.createElement("p");
    const times = doctor.availableTimes
        ? doctor.availableTimes.join(", ")
        : "Not available";
    availability.innerHTML = `<strong>Available:</strong> ${times}`;

    infoDiv.appendChild(name);
    infoDiv.appendChild(specialty);
    infoDiv.appendChild(email);
    infoDiv.appendChild(availability);

    // Action buttons section
    const actionsDiv = document.createElement("div");
    actionsDiv.classList.add("card-actions");

    if (role === "admin") {
        const deleteBtn = document.createElement("button");
        deleteBtn.textContent = "Delete";
        deleteBtn.classList.add("delete-btn");
        deleteBtn.addEventListener("click", async () => {
            if (confirm(`Delete Dr. ${doctor.name}?`)) {
                try {
                    const { makeRequest } = await import("./util.js");
                    const { API_ENDPOINTS } = await import("./config/config.js");
                    await makeRequest(`${API_ENDPOINTS.DOCTORS}/${doctor.id}`, {
                        method: "DELETE",
                    });
                    card.remove();
                    showAlert("Doctor deleted successfully", "success");
                } catch (error) {
                    showAlert("Failed to delete doctor", "error");
                }
            }
        });
        actionsDiv.appendChild(deleteBtn);
    } else if (role === "patient" || !role) {
        const bookBtn = document.createElement("button");
        bookBtn.textContent = "Book Now";
        bookBtn.classList.add("book-btn");
        bookBtn.addEventListener("click", () => {
            alert("Please login first to book an appointment.");
        });
        actionsDiv.appendChild(bookBtn);
    } else if (role === "loggedPatient") {
        const bookBtn = document.createElement("button");
        bookBtn.textContent = "Book Now";
        bookBtn.classList.add("book-btn");
        bookBtn.addEventListener("click", () => {
            // Will be connected to booking modal in patientDashboard.js
            showBookingModal(doctor);
        });
        actionsDiv.appendChild(bookBtn);
    }

    card.appendChild(infoDiv);
    card.appendChild(actionsDiv);

    return card;
}

/*
 * renderPatientTable(patients, containerId) — Render patient records in a table.
 * 
 * Used by the doctor dashboard to show patient appointments.
 */
export function renderPatientTable(patients, containerId = "patientTableBody") {
    const tbody = document.getElementById(containerId);
    if (!tbody) return;

    tbody.innerHTML = "";

    if (!patients || patients.length === 0) {
        tbody.innerHTML =
            '<tr><td colspan="5" class="noPatientRecord">No patient records found.</td></tr>';
        return;
    }

    patients.forEach((record) => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${record.patientId || "N/A"}</td>
            <td>${record.patientName || "N/A"}</td>
            <td>${record.phone || "N/A"}</td>
            <td>${record.email || "N/A"}</td>
            <td>
                <button class="prescription-btn" onclick="viewPrescription(${record.appointmentId})">
                    📋
                </button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

/*
 * Helper: showBookingModal(doctor) — Show the booking overlay.
 * This will be implemented in patientDashboard.js
 */
function showBookingModal(doctor) {
    // Placeholder — will be connected to the booking modal
    console.log("Booking modal for:", doctor.name);
}
