/*
 * ============================================================
 * HEADER.JS — Dynamic Navigation Header
 * ============================================================
 * The header changes based on WHO is logged in:
 * 
 * - Landing page: No header (clean role selection)
 * - Admin: "Add Doctor" button + Logout
 * - Doctor: "Home" + Logout
 * - Patient (not logged in): "Login" + "Sign Up"
 * - Patient (logged in): "Home" + "Appointments" + Logout
 * 
 * WHY dynamic?
 * Instead of creating 4 different headers, we create ONE
 * that adapts. This is DRY (Don't Repeat Yourself) principle.
 */

/*
 * renderHeader() — Main function that builds and injects the header.
 * 
 * Called on EVERY page that needs a header.
 * Reads the user's role from localStorage to decide what to show.
 */
function renderHeader() {
    const headerDiv = document.getElementById("header");
    if (!headerDiv) return;

    /*
     * LANDING PAGE CHECK:
     * If the URL ends with "/" (the landing page), we clear
     * any stored role and token. This ensures a fresh start.
     */
    if (window.location.pathname.endsWith("/") || window.location.pathname.endsWith("/index.html")) {
        localStorage.removeItem("userRole");
        localStorage.removeItem("token");
        return; // No header on the landing page
    }

    const role = localStorage.getItem("userRole");
    const token = localStorage.getItem("token");

    /*
     * SESSION VALIDATION:
     * If a user has a role but NO token, their session expired.
     * Redirect them back to login.
     */
    if ((role === "loggedPatient" || role === "admin" || role === "doctor") && !token) {
        localStorage.removeItem("userRole");
        alert("Session expired or invalid login. Please log in again.");
        window.location.href = "/";
        return;
    }

    let headerContent = `
        <div class="header">
            <div class="logo">🏥 Hospital CMS</div>
            <div class="nav-items">
    `;

    /*
     * ROLE-BASED NAVIGATION:
     * Each role gets different buttons/links.
     */
    if (role === "admin") {
        // Admin sees: Add Doctor button + Logout
        headerContent += `
            <button id="addDocBtn" class="adminBtn" onclick="openModal('addDoctor')">Add Doctor</button>
            <a href="#" onclick="logout()">Logout</a>
        `;
    } else if (role === "doctor") {
        // Doctor sees: Home + Logout
        headerContent += `
            <a href="/doctor/dashboard">Home</a>
            <a href="#" onclick="logout()">Logout</a>
        `;
    } else if (role === "patient") {
        // Patient (not logged in) sees: Login + Sign Up
        headerContent += `
            <a href="/" onclick="localStorage.setItem('userRole', 'patient')">Login</a>
            <a href="/" onclick="localStorage.setItem('userRole', 'patient')">Sign Up</a>
        `;
    } else if (role === "loggedPatient") {
        // Logged-in patient sees: Home + Appointments + Logout
        headerContent += `
            <a href="/patient/dashboard">Home</a>
            <a href="/patient/appointments">Appointments</a>
            <a href="#" onclick="logoutPatient()">Logout</a>
        `;
    }

    headerContent += `
            </div>
        </div>
    `;

    headerDiv.innerHTML = headerContent;

    // Attach event listeners after DOM is updated
    attachHeaderButtonListeners();
}

/*
 * attachHeaderButtonListeners() — Wire up dynamic buttons.
 * 
 * Since the header is generated dynamically, we need to
 * manually attach event listeners after injecting the HTML.
 */
function attachHeaderButtonListeners() {
    const addDocBtn = document.getElementById("addDocBtn");
    if (addDocBtn) {
        addDocBtn.addEventListener("click", () => {
            openModal("addDoctor");
        });
    }
}

/*
 * logout() — Clear session and go to landing page.
 * 
 * Removes token and role from localStorage.
 * Redirects to "/" (landing page).
 */
function logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("userRole");
    window.location.href = "/";
}

/*
 * logoutPatient() — Logout but keep "patient" role.
 * 
 * WHY keep "patient" role?
 * Because patients can browse doctors WITHOUT logging in.
 * After logout, they should see the patient dashboard (browse mode)
 * instead of the landing page.
 */
function logoutPatient() {
    localStorage.removeItem("token");
    localStorage.setItem("userRole", "patient");
    window.location.href = "/patient/dashboard";
}

/*
 * openModal(type) — Show the appropriate modal.
 * 
 * type = "addDoctor" → Shows the Add Doctor form (admin only)
 * type = "login" → Shows the login form
 */
function openModal(type) {
    const modal = document.getElementById("modal");
    const modalBody = document.getElementById("modal-body");

    if (!modal || !modalBody) return;

    if (type === "addDoctor") {
        modalBody.innerHTML = `
            <div class="modal-form">
                <h3>Add New Doctor</h3>
                <div class="form-group">
                    <label>Name</label>
                    <input type="text" id="docName" class="input-field" placeholder="Dr. John Smith">
                </div>
                <div class="form-group">
                    <label>Email</label>
                    <input type="email" id="docEmail" class="input-field" placeholder="doctor@clinic.com">
                </div>
                <div class="form-group">
                    <label>Phone</label>
                    <input type="text" id="docPhone" class="input-field" placeholder="1234567890">
                </div>
                <div class="form-group">
                    <label>Specialty</label>
                    <input type="text" id="docSpecialty" class="input-field" placeholder="Cardiologist">
                </div>
                <button class="submit-btn" id="submitDoctor">Add Doctor</button>
            </div>
        `;
    }

    modal.style.display = "flex";

    // Close modal when X is clicked
    const closeBtn = document.getElementById("closeModal");
    if (closeBtn) {
        closeBtn.onclick = () => {
            modal.style.display = "none";
        };
    }

    // Close modal when clicking outside
    window.onclick = (event) => {
        if (event.target === modal) {
            modal.style.display = "none";
        }
    };
}

/*
 * INITIALIZE:
 * Call renderHeader() when this script loads.
 * This ensures the header is ready on every page.
 */
renderHeader();
