# User Story Template

**Title:**
_As a [user role], I want [feature/goal], so that [reason]._

**Acceptance Criteria:**
1. [Criteria 1]
2. [Criteria 2]
3. [Criteria 3]

**Priority:** [High/Medium/Low]
**Story Points:** [Estimated Effort in Points]
**Notes:**
- [Additional information or edge cases]

---

## Admin User Stories

### Story 1: Admin Login
**Title:**
_As an admin, I want to log into the portal with my username and password, so that I can manage the platform securely._

**Acceptance Criteria:**
1. Admin can enter username and password on the login page
2. System validates credentials against the database
3. On success, admin is redirected to the admin dashboard
4. On failure, an error message is displayed
5. JWT token is generated for authenticated requests

**Priority:** High
**Story Points:** 3
**Notes:**
- Passwords must be stored as BCrypt hashes
- Login page should show "Invalid credentials" on failure (not which field is wrong)

---

### Story 2: Admin Logout
**Title:**
_As an admin, I want to log out of the portal, so that I can protect system access._

**Acceptance Criteria:**
1. Logout button is visible in the admin portal navigation
2. Clicking logout clears the JWT token from the client
3. Admin is redirected to the login page
4. Subsequent requests with the old token are rejected

**Priority:** High
**Story Points:** 2
**Notes:**
- Token should be removed from localStorage on logout
- Server-side token invalidation is optional for JWT (stateless)

---

### Story 3: Add Doctors
**Title:**
_As an admin, I want to add doctors to the portal, so that patients can find and book appointments with them._

**Acceptance Criteria:**
1. Admin can access an "Add Doctor" form from the dashboard
2. Form includes: name, email, phone, specialization, available days, time slots
3. System validates all required fields (name, email, specialization)
4. Email must be unique — system rejects duplicate emails
5. A corresponding user account is created with role DOCTOR
6. Default password is assigned for the new doctor
7. Success confirmation is displayed after saving

**Priority:** High
**Story Points:** 5
**Notes:**
- Doctor email is used as their login username
- Default password should be documented for the doctor to change later

---

### Story 4: Delete Doctor Profile
**Title:**
_As an admin, I want to delete a doctor's profile from the portal, so that I can manage the clinic's medical staff._

**Acceptance Criteria:**
1. Admin can see a delete button next to each doctor in the list
2. System shows a confirmation dialog before deletion
3. Deleting a doctor also removes their user account
4. Doctor's existing appointments are preserved (not deleted)
5. Success message is shown after deletion

**Priority:** Medium
**Story Points:** 3
**Notes:**
- Consider soft-delete (mark as inactive) vs hard-delete
- Future improvement: prevent deletion if doctor has upcoming appointments

---

### Story 5: Run Stored Procedure for Appointment Statistics
**Title:**
_As an admin, I want to run a stored procedure in MySQL CLI to get the number of appointments per month, so that I can track usage statistics._

**Acceptance Criteria:**
1. Stored procedure `GetDailyAppointmentReportByDoctor` exists in MySQL
2. Stored procedure accepts doctor_id and returns appointment counts
3. Admin can execute the procedure via MySQL CLI
4. Results show appointment counts grouped by date
5. Output is readable and well-formatted

**Priority:** Medium
**Story Points:** 3
**Notes:**
- This is a backend task — no UI required
- Procedure should be tested with sample data
- Additional procedures: `GetDoctorWithMostPatientsByMonth`, `GetDoctorWithMostPatientsByYear`

---

## Patient User Stories

### Story 6: View Doctors Without Login
**Title:**
_As a patient, I want to view a list of doctors without logging in, so that I can explore options before registering._

**Acceptance Criteria:**
1. Home page displays a list of all doctors
2. Each doctor card shows: name, specialization, email, available time slots
3. No authentication required to view this page
4. Search bar allows filtering by name or specialization
5. "Filter by Specialty" dropdown is available

**Priority:** High
**Story Points:** 3
**Notes:**
- This is the public-facing page (patient portal landing)
- Matches the screenshot: teal theme, card layout, search bar

---

### Story 7: Patient Sign Up
**Title:**
_As a patient, I want to sign up using my email and password, so that I can book appointments._

**Acceptance Criteria:**
1. Sign Up link is visible on the login page
2. Registration form includes: name, email, phone, date of birth, gender, address
3. System validates all required fields
4. Email must be unique — system rejects duplicates
5. Date of birth must be in the past
6. A user account is created with role PATIENT
7. Patient is redirected to login after successful registration

**Priority:** High
**Story Points:** 5
**Notes:**
- Password should meet minimum strength requirements (8+ chars)
- Consider email format validation

---

### Story 8: Patient Login
**Title:**
_As a patient, I want to log into the portal, so that I can manage my bookings._

**Acceptance Criteria:**
1. Patient enters email and password on the login page
2. System validates credentials and returns JWT token
3. On success, patient is redirected to the patient portal
4. Portal shows available doctors, appointments, and search functionality
5. On failure, error message is displayed

**Priority:** High
**Story Points:** 3
**Notes:**
- Same login page as admin/doctor — role determines redirect destination

---

### Story 9: Patient Logout
**Title:**
_As a patient, I want to log out of the portal, so that I can secure my account._

**Acceptance Criteria:**
1. Logout button is visible in the patient portal navigation
2. Clicking logout clears the JWT token
3. Patient is redirected to the home/login page

**Priority:** High
**Story Points:** 2
**Notes:**
- Same logout behavior as admin

---

### Story 10: Book Appointment
**Title:**
_As a patient, I want to log in and book an hour-long appointment to consult with a doctor, so that I can receive medical care._

**Acceptance Criteria:**
1. Patient clicks "Book Now" on a doctor's card
2. Available dates and time slots are displayed
3. Patient selects a date and time slot (1-hour duration)
4. System checks for scheduling conflicts (no double-booking)
5. Appointment is saved with status SCHEDULED
6. Patient sees a confirmation message

**Priority:** High
**Story Points:** 5
**Notes:**
- Time slots are pre-defined by the doctor (e.g., 09:00-10:00)
- Each slot is exactly 1 hour

---

### Story 11: View Upcoming Appointments
**Title:**
_As a patient, I want to view my upcoming appointments, so that I can prepare accordingly._

**Acceptance Criteria:**
1. Patient portal shows a "Patient Record" or "My Appointments" page
2. Table displays: Date, Appointment ID, Patient ID, Prescription status
3. Upcoming and past appointments are listed
4. Patient can click the prescription icon to view prescription details

**Priority:** High
**Story Points:** 3
**Notes:**
- Matches the screenshot: table with Date, Appointment ID, Patient ID, Prescription columns

---

## Doctor User Stories

### Story 12: Doctor Login
**Title:**
_As a doctor, I want to log into the portal, so that I can manage my appointments._

**Acceptance Criteria:**
1. Doctor enters credentials on the login page
2. System validates and returns JWT token with DOCTOR role
3. On success, doctor is redirected to the doctor portal
4. Portal shows today's appointments and patient list

**Priority:** High
**Story Points:** 3
**Notes:**
- Doctor uses email as username (set during admin-created account)

---

### Story 13: Doctor Logout
**Title:**
_As a doctor, I want to log out of the portal, so that I can protect my data._

**Acceptance Criteria:**
1. Logout button is visible in the doctor portal navigation
2. Clicking logout clears the JWT token
3. Doctor is redirected to the login page

**Priority:** High
**Story Points:** 2
**Notes:**
- Same logout behavior as other roles

---

### Story 14: View Appointment Calendar
**Title:**
_As a doctor, I want to view my appointment calendar, so that I can stay organized._

**Acceptance Criteria:**
1. Doctor portal displays a list/table of appointments
2. Shows: Date, Appointment ID, Patient ID, Patient Name, Status
3. Appointments are sorted by date (newest first or today first)
4. Doctor can distinguish between upcoming and past appointments

**Priority:** High
**Story Points:** 3
**Notes:**
- Matches the screenshot: table with Date, Appointment ID, Patient ID, Prescription columns

---

### Story 15: Mark Unavailability
**Title:**
_As a doctor, I want to mark my unavailability, so that patients only see available slots._

**Acceptance Criteria:**
1. Doctor can access availability settings from the portal
2. Doctor can select which days they are available
3. Doctor can define time slots for each available day
4. Changes are saved and reflected in patient-facing booking
5. Patients cannot book during unavailable times

**Priority:** Medium
**Story Points:** 5
**Notes:**
- Availability is stored as comma-separated days and time slots
- Example: availableDays = "MONDAY,WEDNESDAY,FRIDAY"

---

### Story 16: Update Doctor Profile
**Title:**
_As a doctor, I want to update my profile with specialization and contact information, so that patients have up-to-date information._

**Acceptance Criteria:**
1. Doctor can access profile settings from the portal
2. Editable fields: name, phone, specialization
3. System validates updated information
4. Changes are saved and reflected in patient-facing doctor cards
5. Email cannot be changed (used as login username)

**Priority:** Medium
**Story Points:** 3
**Notes:**
- Email is the login identifier — changing it would break authentication

---

### Story 17: View Patient Details
**Title:**
_As a doctor, I want to view patient details for upcoming appointments, so that I can be prepared._

**Acceptance Criteria:**
1. Doctor can click on a patient's name in the appointment list
2. Shows: patient name, email, phone, date of birth, gender
3. Shows past appointments and prescriptions for this patient
4. Information is read-only for the doctor

**Priority:** Medium
**Story Points:** 3
**Notes:**
- This supports clinical decision-making
- Future improvement: add medical history notes
