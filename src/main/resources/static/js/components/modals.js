/*
 * ============================================================
 * MODALS.JS — Reusable Modal Component
 * ============================================================
 * Creates and manages modal popups for:
 * - Admin login
 * - Doctor login
 * - Patient login
 * - Patient signup
 * - Add doctor (admin only)
 * 
 * The course uses openModal('adminLogin') pattern.
 * This function generates the appropriate form HTML
 * and shows the modal.
 */

/*
 * openModal(type) — Show a modal based on the type.
 * 
 * @param type — "adminLogin", "doctorLogin", "patientLogin",
 *               "patientSignup", "addDoctor"
 * 
 * Each type generates a different form inside the modal.
 */
export function openModal(type) {
    const modal = document.getElementById("modal");
    const modalBody = document.getElementById("modal-body");

    if (!modal || !modalBody) return;

    let html = "";

    switch (type) {
        case "adminLogin":
            html = `
                <div class="modal-form">
                    <h3>Admin Login</h3>
                    <div class="form-group">
                        <label>Username</label>
                        <input type="text" id="adminUsername" class="input-field" placeholder="Enter username">
                    </div>
                    <div class="form-group">
                        <label>Password</label>
                        <input type="password" id="adminPassword" class="input-field" placeholder="Enter password">
                    </div>
                    <button class="submit-btn" onclick="adminLoginHandler()">Login</button>
                </div>
            `;
            break;

        case "doctorLogin":
            html = `
                <div class="modal-form">
                    <h3>Doctor Login</h3>
                    <div class="form-group">
                        <label>Email</label>
                        <input type="email" id="doctorEmail" class="input-field" placeholder="Enter email">
                    </div>
                    <div class="form-group">
                        <label>Password</label>
                        <input type="password" id="doctorPassword" class="input-field" placeholder="Enter password">
                    </div>
                    <button class="submit-btn" onclick="doctorLoginHandler()">Login</button>
                </div>
            `;
            break;

        case "patientLogin":
            html = `
                <div class="modal-form">
                    <h3>Patient Login</h3>
                    <div class="form-group">
                        <label>Email</label>
                        <input type="email" id="loginEmail" class="input-field" placeholder="Enter email">
                    </div>
                    <div class="form-group">
                        <label>Password</label>
                        <input type="password" id="loginPassword" class="input-field" placeholder="Enter password">
                    </div>
                    <button class="submit-btn" onclick="loginPatient()">Login</button>
                </div>
            `;
            break;

        case "patientSignup":
            html = `
                <div class="modal-form">
                    <h3>Patient Sign Up</h3>
                    <div class="form-group">
                        <label>Name</label>
                        <input type="text" id="signupName" class="input-field" placeholder="Your name">
                    </div>
                    <div class="form-group">
                        <label>Email</label>
                        <input type="email" id="signupEmail" class="input-field" placeholder="Your email">
                    </div>
                    <div class="form-group">
                        <label>Password</label>
                        <input type="password" id="signupPassword" class="input-field" placeholder="Your password">
                    </div>
                    <div class="form-group">
                        <label>Phone</label>
                        <input type="text" id="signupPhone" class="input-field" placeholder="Your phone">
                    </div>
                    <div class="form-group">
                        <label>Address</label>
                        <input type="text" id="signupAddress" class="input-field" placeholder="Your address">
                    </div>
                    <button class="submit-btn" onclick="signupPatient()">Sign Up</button>
                </div>
            `;
            break;

        case "addDoctor":
            html = `
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
                    <div class="form-group">
                        <label>Password</label>
                        <input type="password" id="docPassword" class="input-field" placeholder="Password for doctor">
                    </div>
                    <button class="submit-btn" onclick="adminAddDoctor()">Add Doctor</button>
                </div>
            `;
            break;
    }

    modalBody.innerHTML = html;
    modal.style.display = "flex";

    // Close handlers
    document.getElementById("closeModal").onclick = () => {
        modal.style.display = "none";
    };
    window.onclick = (e) => {
        if (e.target === modal) modal.style.display = "none";
    };
}
