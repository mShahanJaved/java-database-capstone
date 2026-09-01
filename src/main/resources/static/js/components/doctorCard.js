/*
 * ============================================================
 * DOCTOR CARD.JS — Reusable Doctor Card Component
 * ============================================================
 * Creates a dynamic doctor card that works for:
 * - Admin dashboard (with Delete button)
 * - Patient dashboard (with Book Now button)
 * 
 * WHY a separate component?
 * 1. Same card design on multiple pages
 * 2. One place to maintain the card layout
 * 3. Easy to add new features (like ratings, reviews)
 */

/*
 * openBookingModal(doctor) — Open the booking modal with doctor info.
 */
function openBookingModal(doctor) {
    const modal = document.getElementById("bookingModal");
    if (!modal) return;

    // Fill doctor name
    const doctorNameInput = document.getElementById("doctorName");
    if (doctorNameInput) doctorNameInput.value = doctor.name;

    // Fill patient name from localStorage or default
    const patientNameInput = document.getElementById("patientName");
    if (patientNameInput) {
        const patientName = localStorage.getItem("patientName") || "";
        patientNameInput.value = patientName;
        patientNameInput.removeAttribute("readonly");
    }

    // Populate time slots
    const timeSlotSelect = document.getElementById("timeSlot");
    if (timeSlotSelect && doctor.availableTimes) {
        timeSlotSelect.innerHTML = '<option value="">Select a time slot</option>';
        doctor.availableTimes.forEach(time => {
            const option = document.createElement("option");
            option.value = time;
            option.textContent = time;
            timeSlotSelect.appendChild(option);
        });
    }

    // Show the modal
    modal.classList.add("active");

    // Set up confirm booking button
    const confirmBtn = document.getElementById("confirmBooking");
    if (confirmBtn) {
        confirmBtn.onclick = async () => {
            const date = document.getElementById("appointmentDate").value;
            const time = timeSlotSelect ? timeSlotSelect.value : "";

            if (!date || !time) {
                alert("Please select date and time slot");
                return;
            }

            try {
                const response = await fetch("/appointment/book", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({
                        doctorName: doctor.name,
                        appointmentDate: date,
                        appointmentTime: time
                    })
                });

                const result = await response.json();
                if (result.success) {
                    alert("Appointment booked successfully!");
                    modal.classList.remove("active");
                } else {
                    alert("Failed to book: " + (result.message || "Unknown error"));
                }
            } catch (error) {
                alert("Error booking appointment: " + error.message);
            }
        };
    }

    // Close button
    const closeBtn = document.getElementById("closeModal");
    if (closeBtn) {
        closeBtn.onclick = () => modal.classList.remove("active");
    }
}

/*
 * createDoctorCard(doctor) — Create a doctor card DOM element.
 * 
 * @param doctor — Object with name, specialty, email, availableTimes
 * @returns DOM element (not a string!)
 * 
 * RETURNS: A <div class="doctor-card"> element ready to append.
 */
export function createDoctorCard(doctor) {
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
        // Admin sees: Delete button
        const removeBtn = document.createElement("button");
        removeBtn.textContent = "Delete";
        removeBtn.classList.add("delete-btn");
        removeBtn.addEventListener("click", async () => {
            if (confirm(`Delete Dr. ${doctor.name}?`)) {
                try {
                    const { makeRequest } = await import("../util.js");
                    const { API_ENDPOINTS } = await import("../config/config.js");
                    await makeRequest(`${API_ENDPOINTS.DOCTORS}/${doctor.id}`, {
                        method: "DELETE",
                    });
                    card.remove();
                    alert("Doctor deleted successfully");
                } catch (error) {
                    alert("Failed to delete doctor");
                }
            }
        });
        actionsDiv.appendChild(removeBtn);
    } else {
        // Patient (logged in or not): Book Now (opens booking modal)
        const bookNow = document.createElement("button");
        bookNow.textContent = "Book Now";
        bookNow.classList.add("book-btn");
        bookNow.addEventListener("click", () => {
            openBookingModal(doctor);
        });
        actionsDiv.appendChild(bookNow);
    }

    card.appendChild(infoDiv);
    card.appendChild(actionsDiv);

    return card;
}
