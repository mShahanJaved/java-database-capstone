/*
 * ============================================================
 * CONFIG.JS — API Configuration
 * ============================================================
 * Central place for ALL API URLs.
 * 
 * WHY? If the backend URL changes (dev → production),
 * you only change it HERE, not in 10 different files.
 */

/*
 * API_BASE_URL — The root URL of the Spring Boot backend.
 * 
 * http://localhost:8080 → Default Spring Boot port
 * 
 * Every API call starts with this URL.
 * Example: API_BASE_URL + "/doctor" = "http://localhost:8080/doctor"
 */
export const API_BASE_URL = "http://localhost:8080";
