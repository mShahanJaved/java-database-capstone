# 🚀 Complete Guide — Run the Smart Clinic Management System

## What You Need Installed

| Tool | How to Install | Check If Installed |
|------|---------------|-------------------|
| **Java 17** | https://adoptium.net/ | `java -version` |
| **Maven** | https://maven.apache.org/download.cgi | `mvn -version` |
| **MySQL 8** | https://dev.mysql.com/downloads/mysql/ | `mysql --version` |
| **MongoDB** | https://www.mongodb.com/try/download/community | `mongod --version` |
| **Git** | https://git-scm.com/ | `git --version` |
| **Docker** (optional) | https://docs.docker.com/get-docker/ | `docker --version` |

---

## STEP 1: Create the Project Folder Structure

Open your terminal and run these commands one by one:

```bash
# Create the main project folder
mkdir java-database-capstone
cd java-database-capstone

# Create the app folder (Spring Boot lives here)
mkdir -p app/src/main/java/com/project/back_end/{models,repository,services,controller,mvc,config,security,dto}
mkdir -p app/src/main/resources/{static/{assets/{css,images/{logo,edit,defineRole,addPrescriptionIcon}},js/{components,config,services},pages},templates/{admin,doctor}}
mkdir -p app/src/test/java
mkdir -p docs
mkdir -p .github/workflows
```

Your folder structure should look like this:

```
java-database-capstone/
├── app/
│   └── src/
│       └── main/
│           ├── java/com/project/back_end/
│           │   ├── models/          ← Entity classes
│           │   ├── repository/      ← Database queries
│           │   ├── services/        ← Business logic
│           │   ├── controller/      ← REST API endpoints
│           │   ├── mvc/             ← Thymeleaf controllers
│           │   ├── config/          ← Security config
│           │   ├── security/        ← JWT filter
│           │   └── dto/             ← Data transfer objects
│           └── resources/
│               ├── static/          ← Frontend files
│               │   ├── assets/css/  ← Stylesheets
│               │   ├── js/          ← JavaScript
│               │   └── pages/       ← HTML pages
│               └── templates/       ← Thymeleaf templates
├── docs/                            ← Documentation
├── .github/workflows/               ← CI/CD pipelines
├── Dockerfile
└── pom.xml
```

---

## STEP 2: Copy All Files Into the Structure

Now you need to copy all the files I created into this structure. Here's exactly what goes where:

### Java Files → `app/src/main/java/com/project/back_end/`

| File | Destination |
|------|-------------|
| `Admin.java` | `models/Admin.java` |
| `Doctor.java` | `models/Doctor.java` |
| `Patient.java` | `models/Patient.java` |
| `Appointment.java` | `models/Appointment.java` |
| `Prescription.java` | `models/Prescription.java` |
| `AdminRepository.java` | `repository/AdminRepository.java` |
| `DoctorRepository.java` | `repository/DoctorRepository.java` |
| `PatientRepository.java` | `repository/PatientRepository.java` |
| `AppointmentRepository.java` | `repository/AppointmentRepository.java` |
| `PrescriptionRepository.java` | `repository/PrescriptionRepository.java` |
| `TokenService.java` | `services/TokenService.java` |
| `Service.java` | `services/Service.java` |
| `DoctorService.java` | `services/DoctorService.java` |
| `AppointmentService.java` | `services/AppointmentService.java` |
| `PatientService.java` | `services/PatientService.java` |
| `PrescriptionService.java` | `services/PrescriptionService.java` |
| `CustomUserDetailsService.java` | `services/CustomUserDetailsService.java` |
| `AuthController.java` | `controller/AuthController.java` |
| `DoctorController.java` | `controller/DoctorController.java` |
| `PatientController.java` | `controller/PatientController.java` |
| `AppointmentController.java` | `controller/AppointmentController.java` |
| `PrescriptionController.java` | `controller/PrescriptionController.java` |
| `DashboardController.java` | `mvc/DashboardController.java` |
| `SecurityConfig.java` | `config/SecurityConfig.java` |
| `JwtAuthenticationFilter.java` | `security/JwtAuthenticationFilter.java` |
| `AppointmentDTO.java` | `dto/AppointmentDTO.java` |
| `Login.java` | `dto/Login.java` |

### Frontend Files → `app/src/main/resources/`

| File | Destination |
|------|-------------|
| `index.html` | `static/index.html` |
| `adminDashboard.html` | `templates/admin/adminDashboard.html` |
| `doctorDashboard.html` | `templates/doctor/doctorDashboard.html` |
| `patientDashboard.html` | `static/pages/patientDashboard.html` |
| `loggedPatientDashboard.html` | `static/pages/loggedPatientDashboard.html` |
| `patientAppointments.html` | `static/pages/patientAppointments.html` |
| `patientRecord.html` | `static/pages/patientRecord.html` |
| `addPrescription.html` | `static/pages/addPrescription.html` |
| `updateAppointment.html` | `static/pages/updateAppointment.html` |
| `style.css` | `static/assets/css/style.css` |
| `index.css` | `static/assets/css/index.css` |
| `adminDashboard.css` | `static/assets/css/adminDashboard.css` |
| `doctorDashboard.css` | `static/assets/css/doctorDashboard.css` |
| `patientDashboard.css` | `static/assets/css/patientDashboard.css` |
| `addPrescription.css` | `static/assets/css/addPrescription.css` |
| `updateAppointment.css` | `static/assets/css/updateAppointment.css` |
| All `.js` files | `static/js/` (matching subfolders) |

### Config & Build Files → `app/`

| File | Destination |
|------|-------------|
| `pom.xml` | `app/pom.xml` |
| `application.properties` | `app/src/main/resources/application.properties` |

### Documentation → Root & `docs/`

| File | Destination |
|------|-------------|
| `README.md` | `README.md` (root) |
| `schema-design.md` | `schema-design.md` (root) |
| `schema-architecture.md` | `docs/schema-architecture.md` |
| `user_stories.md` | `user_stories.md` (root) |
| `sample-data.sql` | `docs/sample-data.sql` |
| `stored-procedures.sql` | `docs/stored-procedures.sql` |
| `mongodb-prescriptions.js` | `docs/mongodb-prescriptions.js` |

### CI/CD & Docker → Root

| File | Destination |
|------|-------------|
| `Dockerfile` | `Dockerfile` (root) |
| `ci.yml` | `.github/workflows/ci.yml` |
| `lint-frontend.yml` | `.github/workflows/lint-frontend.yml` |
| `lint-backend.yml` | `.github/workflows/lint-backend.yml` |
| `compile-backend.yml` | `.github/workflows/compile-backend.yml` |
| `lint-docker.yml` | `.github/workflows/lint-docker.yml` |

---

## STEP 3: Set Up MySQL Database

### 3.1 Start MySQL

```bash
# Login to MySQL
mysql -u root -p
# Enter your password when prompted
```

### 3.2 Create Database

```sql
CREATE DATABASE IF NOT EXISTS cms;
USE cms;
```

### 3.3 Verify Spring Boot Creates Tables

When you run the app for the first time, Hibernate automatically creates the tables. But first, let's run the app briefly:

```bash
cd app
mvn spring-boot:run
```

Wait for it to start, then press `Ctrl+C` to stop it. Now check the tables:

```sql
USE cms;
SHOW TABLES;
```

You should see:
```
+------------------------+
| Tables_in_cms          |
+------------------------+
| admin                  |
| appointment            |
| doctor                 |
| doctor_available_times |
| patient                |
+------------------------+
```

### 3.4 Insert Sample Data

Open a new terminal and run:

```bash
mysql -u root -p cms < ../docs/sample-data.sql
```

Or copy-paste the contents of `docs/sample-data.sql` into the MySQL CLI.

### 3.5 Create Stored Procedures

```bash
mysql -u root -p cms < ../docs/stored-procedures.sql
```

### 3.6 Test Stored Procedures

```sql
USE cms;

-- Daily report
CALL GetDailyAppointmentReportByDoctor('2025-04-15');

-- Doctor with most patients in April 2025
CALL GetDoctorWithMostPatientsByMonth(4, 2025);

-- Doctor with most patients in 2025
CALL GetDoctorWithMostPatientsByYear(2025);
```

**Save this output!** You submit these for the final assignment.

---

## STEP 4: Set Up MongoDB

### 4.1 Start MongoDB

```bash
mongod
```

### 4.2 Insert Prescription Data

Open a new terminal and run:

```bash
mongosh < ../docs/mongodb-prescriptions.js
```

Or copy-paste the contents of `docs/mongodb-prescriptions.js` into the MongoDB CLI.

### 4.3 Verify Data

```javascript
mongosh
use prescriptions
db.prescriptions.find().limit(5).pretty()
```

---

## STEP 5: Run the Application

### 5.1 Make Sure MySQL and MongoDB Are Running

```bash
# Check MySQL
mysql -u root -p -e "SELECT 1"

# Check MongoDB
mongosh --eval "db.runCommand({ ping: 1 })"
```

### 5.2 Start Spring Boot

```bash
cd app
mvn spring-boot:run
```

Wait for this line:
```
Started SmartClinicApplication in X seconds
```

### 5.3 Open in Browser

Go to: **http://localhost:8080**

You should see the "Select Your Role" page with Admin, Patient, and Doctor buttons!

---

## STEP 6: Test the Application

### 6.1 Test with curl (from the course)

```bash
# Get all doctors
curl http://localhost:8080/doctor

# Filter doctors by specialty and time
curl -X GET http://localhost:8080/doctor/filter/null/09:00-10:00/Cardiologist

# Sign up a patient
curl -X POST http://localhost:8080/patient \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"test@example.com","phone":"1234567890","password":"pass123","address":"123 Main St"}'

# Login as admin (use credentials from sample data)
curl -X POST http://localhost:8080/admin \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin@1234"}'
```

### 6.2 Test in Browser

1. **Admin Login**: Click "Admin" → Enter `admin` / `admin@1234`
2. **Doctor Login**: Click "Doctor" → Enter `dr.adams@example.com` / `pass12345`
3. **Patient**: Click "Patient" → Browse doctors (no login needed)

---

## STEP 7: Push to GitHub

### 7.1 Initialize Git (if not already)

```bash
cd java-database-capstone
git init
```

### 7.2 Add All Files

```bash
git add .
```

### 7.3 Commit

```bash
git commit -m "Complete Smart Clinic Management System"
```

### 7.4 Connect to Your GitHub Repo

```bash
# Replace YOUR_USERNAME with your GitHub username
git remote add origin https://github.com/YOUR_USERNAME/java-database-capstone.git
```

### 7.5 Push

```bash
git branch -M main
git push -u origin main
```

---

## STEP 8: Create GitHub Issues (User Stories)

Go to your repo on GitHub → Issues → New Issue

Create 17 issues using the template from `user_stories.md`. Each issue should have:
- Title: The story title
- Description: The full user story with acceptance criteria
- Labels: `admin`, `patient`, or `doctor`

---

## STEP 9: Take Screenshots

You need these screenshots for the final submission:

| Screenshot | How to Get It |
|------------|---------------|
| Admin Portal Login | Click "Admin" → Show the login modal |
| Doctor Portal Login | Click "Doctor" → Show the login modal |
| Patient Portal Login | Click "Patient" → Show the login page |
| Admin Adding Doctor | Login as admin → Click "Add Doctor" → Show the form |
| Patient Searching Doctor | Go to patient dashboard → Type in search bar |
| Doctor Viewing Appointments | Login as doctor → Show the appointments table |

---

## Troubleshooting

### "Port 8080 already in use"
```bash
# Find what's using port 8080
lsof -i :8080
# Kill it
kill -9 <PID>
```

### "MySQL connection refused"
```bash
# Start MySQL
sudo service mysql start
# Or on Mac
brew services start mysql
```

### "MongoDB connection refused"
```bash
# Start MongoDB
mongod
# Or on Mac
brew services start mongodb-community
```

### "Table doesn't exist"
Run the app once (`mvn spring-boot:run`) to let Hibernate create tables, then stop it and insert sample data.

### "Password mismatch"
Make sure you're using the BCrypt-encoded passwords from the sample data, not plain text.

---

## Quick Reference — Commands

```bash
# Start everything
mysql -u root -p                          # Terminal 1: MySQL
mongod                                     # Terminal 2: MongoDB
cd app && mvn spring-boot:run             # Terminal 3: Spring Boot

# Database setup
mysql -u root -p cms < docs/sample-data.sql
mysql -u root -p cms < docs/stored-procedures.sql
mongosh < docs/mongodb-prescriptions.js

# Test
curl http://localhost:8080/doctor

# Push to GitHub
git add . && git commit -m "Complete" && git push
```

---

## You're Done! 🎉

If you can see the "Select Your Role" page at http://localhost:8080, everything is working!

**Next steps:**
1. Test all three portals (Admin, Doctor, Patient)
2. Take screenshots
3. Run stored procedures and save output
4. Push to GitHub
5. Create GitHub Issues
6. Submit your deliverables
