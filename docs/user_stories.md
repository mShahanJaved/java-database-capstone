# User Stories — Smart Clinic Management System

## What Are User Stories?

User stories are short, simple descriptions of a feature told from the perspective of the person who wants it. They follow the format:

> **As a** [type of user], **I want to** [perform some action], **so that** [I can achieve some goal/benefit].

User stories help us:
1. **Stay focused on users** — We build what people actually need, not what we think is cool
2. **Prioritize work** — We know what's essential vs. nice-to-have
3. **Write acceptance criteria** — We know when a feature is "done"
4. **Communicate with stakeholders** — Non-technical people can understand these

---

## Admin User Stories

### Story 1: Admin Login
**As an** Admin, **I want to** log into the system with my credentials, **so that** I can access the admin dashboard and manage the clinic.

**Acceptance Criteria:**
- Admin can enter username and password on the login page
- System validates credentials against the database
- On success, admin is redirected to the admin dashboard
- On failure, an error message is displayed
- JWT token is generated and stored for authenticated requests

---

### Story 2: Admin Adds a Doctor
**As an** Admin, **I want to** add a new doctor to the system with their name, email, phone, specialization, and availability, **so that** patients can find and book appointments with them.

**Acceptance Criteria:**
- Admin can fill out a form with doctor details (name, email, phone, specialization, available days, time slots)
- System validates all required fields (name, email, specialization)
- Email must be unique — system rejects duplicate emails
- Doctor profile is saved in MySQL
- A corresponding user account is created with role DOCTOR
- Default password is assigned (doctor can change later)
- Success confirmation is displayed

---

### Story 3: Admin Views All Doctors
**As an** Admin, **I want to** see a list of all registered doctors, **so that** I can manage the clinic's medical staff.

**Acceptance Criteria:**
- Dashboard displays a table/list of all doctors
- Each entry shows: name, email, specialization, availability
- Admin can edit or delete any doctor from this list

---

### Story 4: Admin Views All Patients
**As an** Admin, **I want to** see a list of all registered patients, **so that** I can monitor clinic usage and manage patient records.

**Acceptance Criteria:**
- Dashboard displays a table of all patients
- Each entry shows: name, email, phone, date of birth, gender
- Admin can view patient details

---

### Story 5: Admin Views All Appointments
**As an** Admin, **I want to** view all appointments in the system, **so that** I can monitor clinic operations and generate reports.

**Acceptance Criteria:**
- Dashboard shows a list of all appointments
- Each entry shows: patient name, doctor name, date, time, status
- Admin can filter by date range, doctor, or status

---

## Patient User Stories

### Story 6: Patient Registration
**As a** Patient, **I want to** create an account with my personal details, **so that** I can book appointments and manage my health records.

**Acceptance Criteria:**
- Patient can fill out registration form (name, email, phone, date of birth, gender, address)
- System validates all required fields
- Email must be unique
- Date of birth must be in the past
- A user account is created with role PATIENT
- Patient is redirected to login after successful registration

---

### Story 7: Patient Login
**As a** Patient, **I want to** log into my account, **so that** I can access my patient portal.

**Acceptance Criteria:**
- Patient enters username (email) and password
- System validates credentials and returns JWT token
- On success, patient is redirected to the patient portal
- Portal shows available doctors, appointments, and records

---

### Story 8: Patient Searches for Doctors
**As a** Patient, **I want to** search for doctors by name or specialization, **so that** I can find the right doctor for my needs.

**Acceptance Criteria:**
- Search bar is prominently displayed on the patient portal
- Typing a name filters doctors by name (e.g., "Emily" finds Dr. Emily Adams)
- Typing a specialization filters by specialty (e.g., "Cardio" finds Cardiologists)
- Results display as cards showing: doctor name, specialization, email, available time slots
- A "Filter by Specialty" dropdown provides quick filtering

---

### Story 9: Patient Books an Appointment
**As a** Patient, **I want to** book an appointment with a doctor by selecting a date and time slot, **so that** I can receive medical care.

**Acceptance Criteria:**
- Patient clicks "Book Now" on a doctor's card
- A booking form appears with available dates and time slots
- Patient selects a date and time slot
- System checks for scheduling conflicts (no double-booking)
- Appointment is saved with status SCHEDULED
- Patient sees a confirmation message

---

### Story 10: Patient Views Appointments
**As a** Patient, **I want to** see all my upcoming and past appointments, **so that** I can track my medical visits.

**Acceptance Criteria:**
- Patient portal shows a "Patient Record" page
- Table displays: Date, Appointment ID, Patient ID, Prescription status
- Past and upcoming appointments are listed
- Patient can click the prescription icon to view prescription details

---

### Story 11: Patient Views Prescription
**As a** Patient, **I want to** view the prescription details for a completed appointment, **so that** I know what medicines to take and dosage instructions.

**Acceptance Criteria:**
- Clicking the prescription icon opens the "View Prescription" page
- Shows: Patient Name, Medicine Names, Dosage Instructions, Additional Notes
- Patient can read but not edit the prescription
- A Cancel/Back button returns to the appointment list

---

## Doctor User Stories

### Story 12: Doctor Login
**As a** Doctor, **I want to** log into the system, **so that** I can access my doctor portal and manage my appointments.

**Acceptance Criteria:**
- Doctor enters credentials on the login page
- System validates and returns JWT token with DOCTOR role
- On success, doctor is redirected to the doctor portal
- Portal shows today's appointments and patient list

---

### Story 13: Doctor Views Patient Appointments
**As a** Doctor, **I want to** see a list of all patient appointments assigned to me, **so that** I can prepare for and manage my daily schedule.

**Acceptance Criteria:**
- Doctor portal displays a table of appointments
- Shows: Date, Appointment ID, Patient ID, Patient Name, Status
- Appointments are sorted by date (newest first)
- Doctor can view appointment details

---

### Story 14: Doctor Updates Appointment Status
**As a** Doctor, **I want to** mark an appointment as completed or cancelled, **so that** the system reflects accurate scheduling information.

**Acceptance Criteria:**
- Doctor can change status from SCHEDULED to COMPLETED or CANCELLED
- Status change is saved to the database
- Appointment list updates to reflect the new status

---

### Story 15: Doctor Writes a Prescription
**As a** Doctor, **I want to** write a prescription for a patient after an appointment, **so that** the patient knows what medicines to take.

**Acceptance Criteria:**
- After completing an appointment, doctor can click "Write Prescription"
- Form includes: Patient Name (auto-filled), Medicine Names (list), Dosage Instructions, Additional Notes
- Prescription is saved in MongoDB (flexible document format)
- Prescription is linked to the appointment via appointmentId
- Patient can view the prescription from their portal

---

### Story 16: Doctor Views Patient History
**As a** Doctor, **I want to** view a patient's past appointments and prescriptions, **so that** I can make informed medical decisions.

**Acceptance Criteria:**
- Doctor can click on a patient's name to view their history
- Shows all past appointments with dates and statuses
- Shows all prescriptions written for this patient
- Information is read-only for the doctor

---

## Summary Table

| # | Role | Story | Priority |
|---|------|-------|----------|
| 1 | Admin | Admin Login | Must Have |
| 2 | Admin | Add a Doctor | Must Have |
| 3 | Admin | View All Doctors | Must Have |
| 4 | Admin | View All Patients | Should Have |
| 5 | Admin | View All Appointments | Should Have |
| 6 | Patient | Patient Registration | Must Have |
| 7 | Patient | Patient Login | Must Have |
| 8 | Patient | Search for Doctors | Must Have |
| 9 | Patient | Book an Appointment | Must Have |
| 10 | Patient | View Appointments | Must Have |
| 11 | Patient | View Prescription | Must Have |
| 12 | Doctor | Doctor Login | Must Have |
| 13 | Doctor | View Patient Appointments | Must Have |
| 14 | Doctor | Update Appointment Status | Should Have |
| 15 | Doctor | Write a Prescription | Must Have |
| 16 | Doctor | View Patient History | Should Have |
