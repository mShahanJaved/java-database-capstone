/*
 * ============================================================
 * UTIL.JS — Utility Functions
 * ============================================================
 * Helper functions used across ALL pages.
 * 
 * WHY separate utilities?
 * 1. DRY — Don't Repeat Yourself
 * 2. One place to fix bugs (if getToken() has a bug, fix it once)
 * 3. Clean, readable code in other files
 */

/*
 * getToken() — Retrieve the JWT token from localStorage.
 * 
 * localStorage = Browser's built-in key-value storage.
 * Data persists even after closing the browser.
 * 
 * We store the JWT token here after login so we can
 * include it in every API request.
 * 
 * RETURNS: The token string, or null if not logged in.
 */
export function getToken() {
    return localStorage.getItem("token");
}

/*
 * getUserRole() — Get the current user's role.
 * 
 * RETURNS: "admin", "doctor", "patient", or null.
 * 
 * This determines which dashboard to show and what
 * buttons/links are visible.
 */
export function getUserRole() {
    return localStorage.getItem("userRole");
}

/*
 * setToken(token) — Save the JWT token to localStorage.
 * 
 * Called after successful login.
 * The token is included in API requests for authentication.
 */
export function setToken(token) {
    localStorage.setItem("token", token);
}

/*
 * setUserRole(role) — Save the user's role to localStorage.
 * 
 * Called after role selection and successful login.
 */
export function setUserRole(role) {
    localStorage.setItem("userRole", role);
}

/*
 * logout() — Clear all session data and redirect to home.
 * 
 * Removes both token and role from localStorage.
 * Then redirects to the landing page.
 */
export function logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("userRole");
    window.location.href = "/";
}

/*
 * showAlert(message, type) — Show a styled alert message.
 * 
 * type = "success" → Green alert
 * type = "error" → Red alert
 * 
 * In production, you'd use a toast library like Toastify.
 * For a capstone project, a simple alert works.
 */
export function showAlert(message, type = "success") {
    const alertDiv = document.createElement("div");
    alertDiv.className = `alert alert-${type}`;
    alertDiv.textContent = message;
    alertDiv.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 15px 25px;
        border-radius: 8px;
        color: white;
        font-weight: 600;
        z-index: 9999;
        background-color: ${type === "success" ? "#27ae60" : "#e74c3c"};
        animation: fadeIn 0.3s ease;
    `;
    document.body.appendChild(alertDiv);

    // Auto-remove after 3 seconds
    setTimeout(() => {
        alertDiv.remove();
    }, 3000);
}

/*
 * formatDate(dateString) — Format a date for display.
 * 
 * Input: "2025-05-22T10:00:00"
 * Output: "2025-05-22"
 * 
 * Useful for showing clean dates in tables and cards.
 */
export function formatDate(dateString) {
    if (!dateString) return "";
    const date = new Date(dateString);
    return date.toISOString().split("T")[0];
}

/*
 * makeRequest(url, options) — Wrapper around fetch() with JWT token.
 * 
 * Instead of writing this everywhere:
 *   fetch(url, {
 *       headers: { "Authorization": `Bearer ${token}` }
 *   })
 * 
 * You just write:
 *   makeRequest(url)
 * 
 * This automatically:
 * 1. Adds the JWT token to the request
 * 2. Sets Content-Type to JSON
 * 3. Handles errors gracefully
 */
export async function makeRequest(url, options = {}) {
    const token = getToken();

    const defaultHeaders = {
        "Content-Type": "application/json",
    };

    // Add JWT token if available
    if (token) {
        defaultHeaders["Authorization"] = `Bearer ${token}`;
    }

    const response = await fetch(url, {
        ...options,
        headers: {
            ...defaultHeaders,
            ...options.headers,
        },
    });

    // Handle HTTP errors
    if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        throw new Error(errorData.message || `HTTP error ${response.status}`);
    }

    return response.json();
}
