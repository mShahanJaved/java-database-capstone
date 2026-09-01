import { API_BASE_URL } from "../config/config.js";

const ADMIN_API = API_BASE_URL + '/api/auth/login';
const DOCTOR_API = API_BASE_URL + '/api/auth/login';

// ============================================================
// MODAL FUNCTIONS (inlined to avoid module import issues)
// ============================================================
function openModal(type) {
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
                    <button class="submit-btn" onclick="window.adminLoginHandler()">Login</button>
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
                    <button class="submit-btn" onclick="window.doctorLoginHandler()">Login</button>
                </div>
            `;
            break;
    }

    modalBody.innerHTML = html;
    modal.style.display = "flex";

    document.getElementById("closeModal").onclick = () => { modal.style.display = "none"; };
    window.onclick = (e) => { if (e.target === modal) modal.style.display = "none"; };
}

// ============================================================
// SETUP — Run when DOM is ready
// ============================================================
document.addEventListener('DOMContentLoaded', function () {
    const adminBtn = document.getElementById('adminLogin');
    if (adminBtn) {
        adminBtn.addEventListener('click', () => openModal('adminLogin'));
    }

    const doctorBtn = document.getElementById('doctorLogin');
    if (doctorBtn) {
        doctorBtn.addEventListener('click', () => openModal('doctorLogin'));
    }
});

// ============================================================
// LOGIN HANDLERS
// ============================================================
window.adminLoginHandler = async function () {
    const username = document.getElementById('adminUsername').value;
    const password = document.getElementById('adminPassword').value;

    try {
        const response = await fetch(ADMIN_API, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });

        if (response.ok) {
            const data = await response.json();
            localStorage.setItem('token', data.token);
            selectRole('admin');
        } else {
            alert('Invalid credentials!');
        }
    } catch (error) {
        alert('Invalid credentials!');
    }
};

window.doctorLoginHandler = async function () {
    const email = document.getElementById('doctorEmail').value;
    const password = document.getElementById('doctorPassword').value;

    try {
        const response = await fetch(DOCTOR_API, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username: email, password })
        });

        if (response.ok) {
            const data = await response.json();
            localStorage.setItem('token', data.token);
            selectRole('doctor');
        } else {
            alert('Invalid credentials!');
        }
    } catch (error) {
        alert('Invalid credentials!');
    }
};

function selectRole(role) {
    localStorage.setItem('userRole', role);
    switch (role) {
        case 'admin': window.location.href = '/admin/dashboard'; break;
        case 'doctor': window.location.href = '/doctor/dashboard'; break;
        case 'patient': window.location.href = '/patient/dashboard'; break;
        default: window.location.href = '/';
    }
}
window.selectRole = selectRole;
