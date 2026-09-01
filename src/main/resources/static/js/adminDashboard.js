/*
 * ============================================================
 * ADMIN DASHBOARD.JS — Admin Portal Logic
 * ============================================================
 * This file handles ALL admin dashboard functionality:
 * 
 * 1. Loading doctor cards on page load
 * 2. Searching doctors by name
 * 3. Filtering by time (AM/PM) and specialty
 * 4. Adding new doctors via modal form
 * 5. Deleting doctors
 * 
 * IMPORTS:
 * - openModal → Opens/closes modal popups
 * - getDoctors → Fetches all doctors from API
 * - filterDoctors → Searches/filters doctors
 * - saveDoctor → Adds a new doctor
 * - createDoctorCard → Creates the HTML for each doctor card
 */

import { openModal } from "./components/modals.js";
import { getDoctors, filterDoctors, saveDoctor } from "./services/doctorServices.js";
import { createDoctorCard } from "./components/doctorCard.js";

/*
 * ADD DOCTOR BUTTON:
 * When admin clicks "Add Doctor" in the header,
 * this opens the modal form.
 */
document.getElementById('addDocBtn').addEventListener('click', () => {
    openModal('addDoctor');
});

/*
 * LOAD DOCTOR CARDS:
 * Fetches all doctors from the API and displays them.
 * 
 * Called when the page first loads.
 * Also called after adding/deleting a doctor to refresh the list.
 */
async function loadDoctorCards() {
    try {
        const doctors = await getDoctors();
        const contentDiv = document.getElementById("content");
        contentDiv.innerHTML = "";

        if (doctors && doctors.length > 0) {
            doctors.forEach((doctor) => {
                const card = createDoctorCard(doctor);
                contentDiv.appendChild(card);
            });
        } else {
            contentDiv.innerHTML = "<p>No doctors found.</p>";
        }
    } catch (error) {
        console.error("Error loading doctors:", error);
    }
}

/*
 * SEARCH AND FILTER:
 * When the admin types in the search bar or selects a filter,
 * we fetch filtered results from the API.
 */
document.getElementById("searchBar").addEventListener("input", filterDoctorsOnChange);
document.getElementById("filterTime").addEventListener("change", filterDoctorsOnChange);
document.getElementById("filterSpecialty").addEventListener("change", filterDoctorsOnChange);

/*
 * filterDoctorsOnChange() — Called whenever a filter changes.
 * 
 * Reads current values from:
 * - Search bar (doctor name)
 * - Time filter (AM/PM)
 * - Specialty filter (Cardiologist, Neurologist, etc.)
 * 
 * Sends these to the API and re-renders the results.
 */
async function filterDoctorsOnChange() {
    const name = document.getElementById("searchBar").value;
    const time = document.getElementById("filterTime").value;
    const specialty = document.getElementById("filterSpecialty").value;

    try {
        const doctors = await filterDoctors(
            name || null,
            time || null,
            specialty || null
        );
        renderDoctorCards(doctors);
    } catch (error) {
        console.error("Error filtering doctors:", error);
    }
}

/*
 * renderDoctorCards(doctors) — Display a list of doctor cards.
 * 
 * Clears the content area and creates a card for each doctor.
 * Reused by both loadDoctorCards() and filterDoctorsOnChange().
 */
function renderDoctorCards(doctors) {
    const contentDiv = document.getElementById("content");
    contentDiv.innerHTML = "";

    if (doctors && doctors.length > 0) {
        doctors.forEach((doctor) => {
            const card = createDoctorCard(doctor);
            contentDiv.appendChild(card);
        });
    } else {
        contentDiv.innerHTML = "<p>No doctors found with the given filters.</p>";
    }
}

/*
 * ADD DOCTOR FORM SUBMISSION:
 * When the admin fills out the "Add Doctor" form and clicks submit,
 * this function collects the data and sends it to the backend.
 */
window.adminAddDoctor = async function () {
    // Collect form values
    const name = document.getElementById("docName").value;
    const email = document.getElementById("docEmail").value;
    const phone = document.getElementById("docPhone").value;
    const specialty = document.getElementById("docSpecialty").value;
    const password = document.getElementById("docPassword").value;

    // Get the admin's token for authentication
    const token = localStorage.getItem("token");

    if (!token) {
        alert("Please login first.");
        return;
    }

    // Build the doctor object
    const doctor = { name, email, phone, specialty, password };

    try {
        const result = await saveDoctor(doctor, token);

        if (result.success) {
            // Close the modal
            document.getElementById("modal").style.display = "none";

            // Reload the doctor list
            await loadDoctorCards();

            // Show success message
            alert("Doctor added successfully!");
        } else {
            alert(result.message || "Failed to add doctor");
        }
    } catch (error) {
        alert("Failed to add doctor");
    }
};

/*
 * INITIALIZE:
 * Load doctor cards when the page first loads.
 */
loadDoctorCards();
