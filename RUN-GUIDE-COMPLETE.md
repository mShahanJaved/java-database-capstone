# 🚀 COMPLETE GUIDE: Push to GitHub & Run the App

## WHAT YOU NEED TO DO (in order)

### Step 1: Install Maven (5 minutes)

Maven is the tool that builds and runs Java projects. You MUST install it.

**How to install on Windows:**

1. Go to: https://maven.apache.org/download.cgi
2. Download "apache-maven-3.9.x-bin.zip" (the Binary zip link)
3. Extract the zip to `C:\maven` (so you have `C:\maven\apache-maven-3.9.x\`)
4. Open PowerShell and run:

```powershell
# Add Maven to PATH (run this ONCE)
[Environment]::SetEnvironmentVariable("Path", $env:Path + ";C:\maven\apache-maven-3.9.x\bin", "User")
```

5. **CLOSE PowerShell and OPEN a NEW PowerShell window**
6. Verify it works:

```powershell
mvn -version
```

You should see something like "Apache Maven 3.9.x". If yes, you're good!

---

### Step 2: Initialize Git & Push to GitHub (5 minutes)

Open PowerShell in your project folder:

```powershell
cd "C:\Users\sshah\OneDrive\Documents\Study&Work\freebuff"
```

Run these commands ONE BY ONE:

```powershell
# Initialize git
git init

# Add all files
git add .

# Commit
git commit -m "Complete Smart Clinic Management System"

# Connect to your GitHub repo (replace YOUR_TOKEN with your actual token)
git remote add origin https://mShahanJaved:YOUR_TOKEN@github.com/mShahanJaved/java-database-capstone.git

# Push
git push -u origin main
```

**To get your GitHub token:**
1. Go to https://github.com/settings/tokens
2. Click "Generate new token (classic)"
3. Name it "capstone"
4. Check the "repo" box
5. Click "Generate token"
6. Copy the token (starts with `ghp_`)

---

### Step 3: Create GitHub Issues (10 minutes)

Go to: https://github.com/mShahanJaved/java-database-capstone/issues

Click "New Issue" and create these 17 issues:

**ISSUE 1:**
- Title: `Admin Login`
- Description:
```
_As an admin, I want to log into the portal with my username and password, so that I can manage the platform securely._

**Acceptance Criteria:**
1. Admin can enter username and password on the login page
2. System validates credentials against the database
3. On success, admin is redirected to the admin dashboard
4. On failure, an error message is displayed
5. JWT token is generated for authenticated requests

**Priority:** High
**Story Points:** 3
```

**ISSUE 2:**
- Title: `Admin Logout`
- Description:
```
_As an admin, I want to log out of the portal, so that I can protect system access._

**Acceptance Criteria:**
1. Logout button is visible in the admin portal navigation
2. Clicking logout clears the JWT token from the client
3. Admin is redirected to the login page
4. Subsequent requests with the old token are rejected

**Priority:** High
**Story Points:** 2
```

**ISSUE 3:**
- Title: `Add Doctors`
- Description:
```
_As an admin, I want to add doctors to the portal, so that patients can find and book appointments with them._

**Acceptance Criteria:**
1. Admin can access an "Add Doctor" form from the dashboard
2. Form includes: name, email, phone, specialization, available days, time slots
3. System validates all required fields
4. Email must be unique
5. A corresponding user account is created with role DOCTOR

**Priority:** High
**Story Points:** 5
```

**ISSUE 4:**
- Title: `Delete Doctor Profile`
- Description:
```
_As an admin, I want to delete a doctor's profile from the portal, so that I can manage the clinic's medical staff._

**Acceptance Criteria:**
1. Admin can see a delete button next to each doctor
2. System prompts for confirmation before deleting
3. On confirmation, doctor profile is removed from the database
4. Associated appointments are also deleted

**Priority:** High
**Story Points:** 3
```

**ISSUE 5:**
- Title: `Run Stored Procedure`
- Description:
```
_As an admin, I want to run a stored procedure to generate reports on doctor performance and appointment summaries._

**Acceptance Criteria:**
1. Admin can access a "Generate Report" section
2. Can select report type (daily, monthly, yearly)
3. Report displays doctor performance metrics
4. Results are displayed in a table format

**Priority:** Medium
**Story Points:** 3
```

**ISSUE 6:**
- Title: `Patient View Doctors`
- Description:
```
_As a patient, I want to view the list of available doctors, so that I can choose one for an appointment._

**Acceptance Criteria:**
1. Patient can browse all doctors without logging in
2. Doctor cards show name, specialty, availability
3. Patient can filter doctors by specialty and availability time
4. Patient can search doctors by name

**Priority:** High
**Story Points:** 3
```

**ISSUE 7:**
- Title: `Patient Sign Up`
- Description:
```
_As a patient, I want to sign up for an account, so that I can book appointments._

**Acceptance Criteria:**
1. Patient can fill in name, email, password, phone, address
2. System validates all fields (email format, required fields)
3. Password is stored securely (hashed)
4. On success, patient is redirected to login

**Priority:** High
**Story Points:** 3
```

**ISSUE 8:**
- Title: `Patient Login`
- Description:
```
_As a patient, I want to log into the portal, so that I can book and manage appointments._

**Acceptance Criteria:**
1. Patient can enter email and password
2. System validates credentials
3. On success, JWT token is generated
4. Patient is redirected to their dashboard

**Priority:** High
**Story Points:** 3
```

**ISSUE 9:**
- Title: `Patient Logout`
- Description:
```
_As a patient, I want to log out of the portal, so that I can protect my personal information._

**Acceptance Criteria:**
1. Logout button is visible in the patient portal
2. Clicking logout clears the JWT token
3. Patient is redirected to the home page

**Priority:** High
**Story Points:** 2
```

**ISSUE 10:**
- Title: `Book Appointment`
- Description:
```
_As a patient, I want to book an appointment with a doctor, so that I can receive medical consultation._

**Acceptance Criteria:**
1. Patient can select a doctor and time slot
2. System checks if the slot is available
3. Appointment is created with status "pending"
4. Confirmation message is displayed

**Priority:** High
**Story Points:** 5
```

**ISSUE 11:**
- Title: `View Appointments`
- Description:
```
_As a patient, I want to view my upcoming and past appointments, so that I can track my medical visits._

**Acceptance Criteria:**
1. Patient can see a list of all their appointments
2. Appointments show date, time, doctor name, status
3. Patient can filter by past/future appointments

**Priority:** Medium
**Story Points:** 3
```

**ISSUE 12:**
- Title: `Doctor Login`
- Description:
```
_As a doctor, I want to log into the portal, so that I can manage my appointments and patients._

**Acceptance Criteria:**
1. Doctor can enter email and password
2. System validates credentials
3. JWT token is generated on success
4. Doctor is redirected to their dashboard

**Priority:** High
**Story Points:** 3
```

**ISSUE 13:**
- Title: `Doctor Logout`
- Description:
```
_As a doctor, I want to log out of the portal, so that I can protect patient data._

**Acceptance Criteria:**
1. Logout button is visible
2. Token is cleared on logout
3. Doctor is redirected to home page

**Priority:** High
**Story Points:** 2
```

**ISSUE 14:**
- Title: `View Calendar`
- Description:
```
_As a doctor, I want to view my appointment calendar, so that I can see my schedule._

**Acceptance Criteria:**
1. Doctor can see all appointments for today
2. Can select a different date to view
3. Each appointment shows patient name, time, status

**Priority:** Medium
**Story Points:** 3
```

**ISSUE 15:**
- Title: `Mark Unavailability`
- Description:
```
_As a doctor, I want to mark time slots as unavailable, so that patients cannot book during those times._

**Acceptance Criteria:**
1. Doctor can view available time slots
2. Can mark specific slots as unavailable
3. Unavailable slots are not shown to patients

**Priority:** Medium
**Story Points:** 3
```

**ISSUE 16:**
- Title: `Update Profile`
- Description:
```
_As a doctor, I want to update my profile information, so that my details are current._

**Acceptance Criteria:**
1. Doctor can edit phone, specialty, availability
2. Changes are saved to the database
3. Updated information is reflected immediately

**Priority:** Medium
**Story Points:** 3
```

**ISSUE 17:**
- Title: `View Patient Details`
- Description:
```
_As a doctor, I want to view patient details and past prescriptions, so that I can provide informed care._

**Acceptance Criteria:**
1. Doctor can see patient name, email, phone, DOB, gender
2. Can view past appointments and prescriptions
3. Information is read-only

**Priority:** Medium
**Story Points:** 3
```

---

### Step 4: Run the App in Cloud IDE (10 minutes)

The course provides a **Cloud IDE** with MySQL and MongoDB already installed. Use that!

1. Go to your course platform (the lab environment)
2. Open the Cloud IDE
3. Clone your repo:

```bash
git clone https://github.com/mShahanJaved/java-database-capstone.git
cd java-database-capstone
```

4. Open MySQL CLI and create the database:

```sql
CREATE DATABASE cms;
EXIT;
```

5. Start the app:

```bash
mvn spring-boot:run
```

6. The app will start at `http://localhost:8080`

7. Open another terminal for MySQL:

```bash
mysql -u root -p
USE cms;
```

8. Insert sample data:

```sql
-- Copy-paste the contents of docs/sample-data.sql
```

9. Insert MongoDB data:

```bash
mongosh
use prescriptions;
-- Copy-paste the contents of docs/mongodb-prescriptions.js
```

10. Test the app:
- Go to `http://localhost:8080`
- Click "Admin" → Login: `admin` / `admin@1234`
- Click "Doctor" → Login: `dr.adams@example.com` / `pass12345`
- Click "Patient" → Browse doctors

---

### Step 5: Take Screenshots (5 minutes)

Take screenshots of:
1. Landing page (role selection)
2. Admin login modal
3. Admin dashboard with doctors
4. Doctor login modal
5. Doctor dashboard with appointments
6. Patient dashboard with doctor cards

Save these for your final submission.

---

### Step 6: Run Stored Procedures & Save Output (5 minutes)

In the MySQL CLI:

```sql
CALL GetDailyAppointmentReportByDoctor('2025-04-15');
-- Screenshot this output

CALL GetDoctorWithMostPatientsByMonth(4, 2025);
-- Screenshot this output

CALL GetDoctorWithMostPatientsByYear(2025);
-- Screenshot this output
```

---

## COMPLETE FILE STRUCTURE

```
java-database-capstone/
├── app/                                    # (if using Cloud IDE's app/ folder)
│   ├── pom.xml                            # Maven build file
│   ├── Dockerfile                         # Docker config
│   ├── src/main/java/com/
│   │   ├── project/back_end/
│   │   │   ├── BackEndApplication.java    # Main app entry point
│   │   │   ├── config/SecurityConfig.java
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── DoctorController.java
│   │   │   │   ├── PatientController.java
│   │   │   │   ├── AppointmentController.java
│   │   │   │   └── PrescriptionController.java
│   │   │   ├── dto/
│   │   │   │   ├── AppointmentDTO.java
│   │   │   │   └── Login.java
│   │   │   ├── models/
│   │   │   │   ├── Admin.java
│   │   │   │   ├── Doctor.java
│   │   │   │   ├── Patient.java
│   │   │   │   ├── Appointment.java
│   │   │   │   └── Prescription.java
│   │   │   ├── mvc/DashboardController.java
│   │   │   ├── repository/
│   │   │   │   ├── AdminRepository.java
│   │   │   │   ├── DoctorRepository.java
│   │   │   │   ├── PatientRepository.java
│   │   │   │   ├── AppointmentRepository.java
│   │   │   │   └── PrescriptionRepository.java
│   │   │   ├── security/JwtAuthenticationFilter.java
│   │   │   └── services/
│   │   │       ├── AppointmentService.java
│   │   │       ├── DoctorService.java
│   │   │       ├── PatientService.java
│   │   │       ├── PrescriptionService.java
│   │   │       ├── TokenService.java
│   │   │       └── CustomUserDetailsService.java
│   │   └── smartcare/smartclinic/         # Course template code
│   ├── src/main/resources/
│   │   ├── application.properties
│   │   ├── static/
│   │   │   ├── index.html
│   │   │   ├── assets/css/ (7 CSS files)
│   │   │   ├── js/ (21 JS files)
│   │   │   └── pages/ (6 HTML pages)
│   │   └── templates/
│   │       ├── admin/adminDashboard.html
│   │       └── doctor/doctorDashboard.html
├── docs/
│   ├── sample-data.sql
│   ├── stored-procedures.sql
│   └── mongodb-prescriptions.js
├── .github/workflows/
│   ├── lint-frontend.yml
│   ├── lint-backend.yml
│   ├── compile-backend.yml
│   └── lint-docker.yml
├── README.md
├── schema-design.md
├── user_stories.md
└── RUN-GUIDE.md
```

---

## TROUBLESHOOTING

**Problem: "mvn is not recognized"**
→ You need to install Maven. See Step 1.

**Problem: "Cannot connect to MySQL"**
→ Make sure MySQL is running. In Cloud IDE, click the MySQL button.

**Problem: "Cannot connect to MongoDB"**
→ Make sure MongoDB is running. In Cloud IDE, click the MongoDB button.

**Problem: "Port 8080 already in use"**
→ Kill the process using that port:
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <process_id> /F
```

**Problem: "Compilation errors"**
→ Make sure you're in the right directory:
```bash
cd app
mvn clean compile
mvn spring-boot:run
```

---

## FINAL CHECKLIST

- [ ] Maven installed and working (`mvn -version`)
- [ ] Git repo initialized and pushed to GitHub
- [ ] 17 GitHub Issues created
- [ ] App runs in Cloud IDE (`mvn spring-boot:run`)
- [ ] Admin login works (admin / admin@1234)
- [ ] Doctor login works (dr.adams@example.com / pass12345)
- [ ] Patient can browse doctors
- [ ] Screenshots taken (6 minimum)
- [ ] Stored procedures run and output saved
- [ ] All files committed and pushed

---

## WHAT TO SUBMIT

| Deliverable | Where to Find |
|-------------|---------------|
| GitHub Issues link | https://github.com/mShahanJaved/java-database-capstone/issues |
| schema-design.md | Root of repo |
| Doctor.java | app/src/main/java/com/project/back_end/models/Doctor.java |
| Appointment.java | app/src/main/java/com/project/back_end/models/Appointment.java |
| DoctorController.java | app/src/main/java/com/project/back_end/controller/DoctorController.java |
| AppointmentService.java | app/src/main/java/com/project/back_end/services/AppointmentService.java |
| PrescriptionController.java | app/src/main/java/com/project/back_end/controller/PrescriptionController.java |
| PatientRepository.java | app/src/main/java/com/project/back_end/repository/PatientRepository.java |
| TokenService.java | app/src/main/java/com/project/back_end/services/TokenService.java |
| DoctorService.java | app/src/main/java/com/project/back_end/services/DoctorService.java |
| Dockerfile | Root of repo |
| CI Workflow | .github/workflows/compile-backend.yml |
| 6 Screenshots | Your screenshots folder |
| 3 SQL outputs | Stored procedure results |
