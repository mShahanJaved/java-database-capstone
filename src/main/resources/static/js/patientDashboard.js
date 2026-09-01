/*
 * ============================================================
 * PATIENT DASHBOARD.JS — Patient Portal Logic
 * ============================================================
 * Handles:
 * 1. Loading all doctors on page load
 * 2. Searching/filtering doctors
 * 3. Patient signup (register new account)
 * 4. Patient login (existing account)
 * 5. Booking appointments
 * 
 * IMPORTS:
 * - createDoctorCard → Creates doctor card HTML
 * - openModal → Opens modal popups
 * - getDoctors, filterDoctors → Doctor API calls
 * - patientLogin, patientSignup → Patient API calls
 */

import { createDoctorCard } from "./components/doctorCard.js";
import { openModal } from "./components/modals.js";
import { getDoctors, filterDoctors } from "./services/doctorServices.js";
import { patientLogin, patientSignup } from "./services/patientServices.js";

/*
 * LOAD DOCTOR CARDS ON PAGE LOAD:
 * When the patient dashboard loads, we fetch all doctors
 * and display them as cards.
 */
document.addEventListener("DOMContentLoaded", () => {
    loadDoctorCards();
});

/*
 * SIGNUP BUTTON:
 * Opens the patient signup modal.
 */
const signupBtn = document.getElementById("patientSignup");
if (signupBtn) {
    signupBtn.addEventListener("click", () => openModal("patientSignup"));
}

/*
 * LOGIN BUTTON:
 * Opens the patient login modal.
 */
const loginBtn = document.getElementById("patientLogin");
if (loginBtn) {
    loginBtn.addEventListener("click", () => openModal("patientLogin"));
}

/*
 * SEARCH AND FILTER:
 * When the patient types in the search bar or selects a filter,
 * we fetch filtered results from the API.
 */
document.getElementById("searchBar").addEventListener("input", filterDoctorsOnChange);
document.getElementById("filterTime").addEventListener("change", filterDoctorsOnChange);
document.getElementById("filterSpecialty").addEventListener("change", filterDoctorsOnChange);

/*
 * filterDoctorsOnChange() — Filter doctors based on search/filters.
 * 
 * Reads current values from search bar and filter dropdowns.
 * Sends them to the API and re-renders the results.
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
 * loadDoctorCards() — Fetch all doctors and display them.
 */
async function loadDoctorCards() {
    try {
        const doctors = await getDoctors();
        renderDoctorCards(doctors);
    } catch (error) {
        console.error("Error loading doctors:", error);
    }
}

/*
 * PATIENT SIGNUP HANDLER:
 * Called when the patient submits the signup form.
 * 
 * Collects: name, email, password, phone, address
 * Sends to backend via patientSignup()
 * On success: Shows alert, closes modal, reloads page
 */
window.signupPatient = async function () {
    const name = document.getElementById("signupName").value;
    const email = document.getElementById("signupEmail").value;
    const password = document.getElementById("signupPassword").value;
    const phone = document.getElementById("signupPhone").value;
    const address = document.getElementById("signupAddress").value;

    const data = { name, email, password, phone, address };

    try {
        const result = await patientSignup(data);

        if (result.success) {
            alert(result.message || "Signup successful!");
            document.getElementById("modal").style.display = "none";
            window.location.reload();
        } else {
            alert(result.message || "Signup failed");
        }
    } catch (error) {
        alert("Signup failed. Please try again.");
    }
};

/*
 * PATIENT LOGIN HANDLER:
 * Called when the patient submits the login form.
 * 
 * Collects: email, password
 * Sends to backend via patientLogin()
 * On success: Saves token → Redirects to logged patient dashboard
 */
window.loginPatient = async function () {
    const email = document.getElementById("loginEmail").value;
    const password = document.getElementById("loginPassword").value;

    const data = { email, password };

    try {
        const response = await patientLogin(data);

        if (response.ok) {
            const result = await response.json();
            localStorage.setItem("token", result.token);
            localStorage.setItem("userRole", "loggedPatient");
            window.location.href = "/pages/loggedPatientDashboard.html";
        } else {
            alert("Invalid credentials!");
        }
    } catch (error) {
        alert("Login failed. Please try again.");
    }
};
