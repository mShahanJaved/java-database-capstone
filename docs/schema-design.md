# Smart Clinic Management System — Architecture Design

## 1. System Overview

The Smart Clinic Management System (SCMS) is a full-stack web application designed for managing outpatient clinic operations. It supports three user roles: **Admin**, **Doctor**, and **Patient**, each with role-specific dashboards and capabilities.

## 2. Technology Stack

| Layer | Technology | Purpose |
|-------|-----------|---------|
| **Frontend** | HTML, CSS, JavaScript | Dynamic web pages |
| **Template Engine** | Thymeleaf | Server-side HTML rendering |
| **Backend Framework** | Spring Boot 3.2 | REST APIs, business logic, MVC |
| **Authentication** | Spring Security + JWT | Secure token-based authentication |
| **Relational DB** | MySQL 8 | Structured data (patients, doctors, appointments) |
| **Document DB** | MongoDB | Flexible data (prescriptions) |
| **ORM** | Hibernate (JPA) | Java ↔ Database mapping |
| **Containerization** | Docker | Consistent deployment |
| **CI/CD** | GitHub Actions | Automated build, test, lint |

## 3. System Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                         CLIENTS                                  │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐           │
│  │  Admin Portal │  │ Doctor Portal│  │Patient Portal│           │
│  │  (HTML/CSS/JS)│  │ (HTML/CSS/JS)│  │ (HTML/CSS/JS)│          │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘           │
└─────────┼─────────────────┼─────────────────┼──────────────────┘
          │  HTTP/HTTPS     │                 │
          ▼                 ▼                 ▼
┌─────────────────────────────────────────────────────────────────┐
│                    SPRING BOOT APPLICATION                       │
│                                                                  │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │              SECURITY FILTER CHAIN                       │    │
│  │  ┌─────────────┐  ┌──────────────┐  ┌───────────────┐  │    │
│  │  │ JWT Filter  │→ │ Auth Filter  │→ │ Role Filter   │  │    │
│  │  └─────────────┘  └──────────────┘  └───────────────┘  │    │
│  └─────────────────────────────────────────────────────────┘    │
│                              │                                   │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │                    CONTROLLERS                            │    │
│  │  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐     │    │
│  │  │AuthController│ │DoctorController│ │PatientController│  │    │
│  │  └──────┬───────┘ └──────┬───────┘ └──────┬───────┘     │    │
│  │  ┌──────┴───────┐ ┌──────┴───────┐ ┌──────┴───────┐     │    │
│  │  │Appointment-  │ │Prescription- │ │ Admin-       │     │    │
│  │  │Controller    │ │Controller    │ │Controller    │     │    │
│  │  └──────┬───────┘ └──────┬───────┘ └──────┬───────┘     │    │
│  └─────────┼────────────────┼────────────────┼──────────────┘    │
│            │                │                │                    │
│  ┌─────────▼────────────────▼────────────────▼──────────────┐    │
│  │                      SERVICES                              │    │
│  │  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐      │    │
│  │  │ DoctorService│ │PatientService│ │TokenService  │      │    │
│  │  └──────┬───────┘ └──────┬───────┘ └──────┬───────┘      │    │
│  │  ┌──────┴───────┐ ┌──────┴───────┐ ┌──────┴───────┐      │    │
│  │  │Appointment-  │ │Prescription- │ │UserDetailsService│    │    │
│  │  │Service       │ │Service       │ │(Spring Security)│   │    │
│  │  └──────┬───────┘ └──────┬───────┘ └──────────────┘      │    │
│  └─────────┼────────────────┼────────────────────────────┘    │
│            │                │                                   │
│  ┌─────────▼────────────────▼───────────────────────────┐     │
│  │                   REPOSITORIES                         │     │
│  │  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐  │     │
│  │  │DoctorRepo    │ │PatientRepo   │ │AppointmentRepo│  │     │
│  │  │(JPA/MySQL)   │ │(JPA/MySQL)   │ │(JPA/MySQL)   │  │     │
│  │  └──────────────┘ └──────────────┘ └──────────────┘  │     │
│  │  ┌──────────────┐ ┌──────────────┐                    │     │
│  │  │UserRepo      │ │PrescriptionRepo│                   │     │
│  │  │(JPA/MySQL)   │ │(MongoDB)      │                   │     │
│  │  └──────────────┘ └──────────────┘                    │     │
│  └───────────────┬─────────────────┬────────────────────┘     │
└──────────────────┼─────────────────┼──────────────────────────┘
                   │                 │
          ┌────────▼──────┐  ┌───────▼────────┐
          │   MySQL 8     │  │   MongoDB      │
          │  (Relational) │  │  (Document)    │
          └───────────────┘  └────────────────┘
```

## 4. Database Schema Design

### 4.1 MySQL Schema (Relational Data)

#### Table: `users`
Stores authentication credentials for ALL user types (admin, doctor, patient).

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PK, AUTO_INCREMENT | Unique user ID |
| username | VARCHAR(50) | UNIQUE, NOT NULL | Login username |
| password | VARCHAR(255) | NOT NULL | BCrypt hashed password |
| role | ENUM('ADMIN','DOCTOR','PATIENT') | NOT NULL | User role |
| enabled | BOOLEAN | DEFAULT TRUE | Account active flag |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Account creation time |
| updated_at | TIMESTAMP | ON UPDATE CURRENT_TIMESTAMP | Last update time |

#### Table: `doctors`
Stores doctor profile information.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PK, AUTO_INCREMENT | Unique doctor ID |
| user_id | BIGINT | FK → users.id, UNIQUE | Links to authentication account |
| name | VARCHAR(100) | NOT NULL | Full name |
| email | VARCHAR(100) | UNIQUE, NOT NULL | Professional email |
| phone | VARCHAR(20) | | Contact phone |
| specialization | VARCHAR(100) | NOT NULL | Medical specialty |
| available_days | VARCHAR(255) | | e.g., "MONDAY,WEDNESDAY,FRIDAY" |
| available_time_slots | VARCHAR(500) | | e.g., "09:00-10:00,10:00-11:00" |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | |

#### Table: `patients`
Stores patient profile information.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PK, AUTO_INCREMENT | Unique patient ID |
| user_id | BIGINT | FK → users.id, UNIQUE | Links to authentication account |
| name | VARCHAR(100) | NOT NULL | Full name |
| email | VARCHAR(100) | UNIQUE, NOT NULL | Contact email |
| phone | VARCHAR(20) | | Contact phone |
| date_of_birth | DATE | NOT NULL | Date of birth |
| gender | ENUM('MALE','FEMALE','OTHER') | NOT NULL | Gender |
| address | VARCHAR(255) | | Home address |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | |

#### Table: `appointments`
Stores appointment scheduling data.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PK, AUTO_INCREMENT | Unique appointment ID |
| doctor_id | BIGINT | FK → doctors.id, NOT NULL | Assigned doctor |
| patient_id | BIGINT | FK → patients.id, NOT NULL | Assigned patient |
| appointment_date | DATE | NOT NULL | Date of appointment |
| appointment_time | VARCHAR(20) | NOT NULL | Time slot (e.g., "09:00-10:00") |
| status | ENUM('SCHEDULED','COMPLETED','CANCELLED') | DEFAULT 'SCHEDULED' | Appointment status |
| notes | TEXT | | Doctor/patient notes |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | |
| updated_at | TIMESTAMP | ON UPDATE CURRENT_TIMESTAMP | |

#### Table: `admins`
Stores admin profile information.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PK, AUTO_INCREMENT | Unique admin ID |
| user_id | BIGINT | FK → users.id, UNIQUE | Links to authentication account |
| name | VARCHAR(100) | NOT NULL | Full name |
| email | VARCHAR(100) | UNIQUE, NOT NULL | Contact email |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | |

### 4.2 MongoDB Schema (Document Data)

#### Collection: `prescriptions`
Prescriptions are stored in MongoDB because they have variable structure — different medicines, dosages, and notes per prescription.

```json
{
  "_id": "ObjectId",
  "appointmentId": 131,
  "patientId": 26,
  "patientName": "John Smith",
  "doctorId": 5,
  "doctorName": "Dr. Emily Adams",
  "medicineNames": ["Vitamin C tablets", "Zinc supplements"],
  "dosageInstructions": "Twice a day with meals",
  "additionalNotes": "Take with food. Avoid dairy within 2 hours.",
  "createdAt": "2025-05-23T10:30:00Z"
}
```

## 5. Entity Relationship Diagram

```
┌──────────────┐       ┌──────────────┐       ┌──────────────┐
│    users     │       │   doctors    │       │   patients   │
├──────────────┤       ├──────────────┤       ├──────────────┤
│ id (PK)      │──1:1──│ id (PK)      │       │ id (PK)      │
│ username     │       │ user_id (FK) │       │ user_id (FK) │──1:1──users
│ password     │       │ name         │       │ name         │
│ role         │       │ email        │       │ email        │
│ enabled      │       │ phone        │       │ phone        │
│ created_at   │       │ specialization│      │ date_of_birth│
│ updated_at   │       │ available_days│      │ gender       │
└──────────────┘       │ avail. slots │       │ address      │
       │               │ created_at   │       │ created_at   │
       │               └──────┬───────┘       └──────┬───────┘
       │                      │                      │
       │                      │ 1:N                  │ 1:N
       │                      ▼                      ▼
       │               ┌──────────────────────────────┐
       │               │       appointments            │
       │               ├──────────────────────────────┤
       │               │ id (PK)                       │
       │               │ doctor_id (FK) → doctors      │
       │               │ patient_id (FK) → patients    │
       │               │ appointment_date              │
       │               │ appointment_time              │
       │               │ status                        │
       │               │ notes                         │
       │               │ created_at / updated_at       │
       │               └──────────────┬───────────────┘
       │                              │
       │                              │ 1:1 (by appointmentId)
       │                              ▼
       │               ┌──────────────────────────────┐
       │               │  prescriptions (MongoDB)      │
       │               ├──────────────────────────────┤
       │               │ _id                           │
       │               │ appointmentId                 │
       │               │ patientId / patientName       │
       │               │ doctorId / doctorName         │
       │               │ medicineNames[]               │
       │               │ dosageInstructions           │
       │               │ additionalNotes              │
       │               └──────────────────────────────┘
       │
       └──────────1:1──┌──────────────┐
                       │    admins    │
                       ├──────────────┤
                       │ id (PK)      │
                       │ user_id (FK) │
                       │ name         │
                       │ email        │
                       └──────────────┘
```

## 6. API Endpoints

### Authentication
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | /api/auth/login | Login and receive JWT token | No |
| POST | /api/auth/register | Register new user | No |

### Doctors
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | /api/doctors | List all doctors | Yes |
| GET | /api/doctors/{id} | Get doctor by ID | Yes |
| POST | /api/doctors | Create doctor (Admin only) | Admin |
| PUT | /api/doctors/{id} | Update doctor (Admin only) | Admin |
| DELETE | /api/doctors/{id} | Delete doctor (Admin only) | Admin |
| GET | /api/doctors/search?name=x | Search doctors by name | Yes |

### Patients
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | /api/patients | List all patients | Admin/Doctor |
| GET | /api/patients/{id} | Get patient by ID | Yes |
| POST | /api/patients | Create patient | Admin |
| PUT | /api/patients/{id} | Update patient | Admin/Patient |

### Appointments
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | /api/appointments | List appointments | Yes |
| GET | /api/appointments/{id} | Get appointment | Yes |
| POST | /api/appointments | Book appointment | Patient |
| PUT | /api/appointments/{id} | Update appointment | Yes |
| DELETE | /api/appointments/{id} | Cancel appointment | Yes |

### Prescriptions
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | /api/prescriptions/{appointmentId} | Get prescription | Yes |
| POST | /api/prescriptions | Create prescription | Doctor |
| PUT | /api/prescriptions/{id} | Update prescription | Doctor |

### Thymeleaf MVC Pages
| Route | Page | Auth Required |
|-------|------|---------------|
| / | Home (Role Selection) | No |
| /login | Login page | No |
| /admin/dashboard | Admin dashboard | Admin |
| /doctor/dashboard | Doctor dashboard | Doctor |
| /patient/dashboard | Patient dashboard | Patient |

## 7. Security Design

### Authentication Flow
```
1. User enters credentials on login page
2. Frontend sends POST /api/auth/login with {username, password}
3. Backend validates credentials against BCrypt-hashed password
4. If valid: Generate JWT token (contains userId, username, role)
5. Return JWT token to client
6. Client stores token in localStorage
7. Client includes token in Authorization header: "Bearer <token>"
8. JWT Filter validates token on every request
9. If valid: Extract user info, set SecurityContext
10. If invalid: Return 401 Unauthorized
```

### Role-Based Access Control
| Role | Can Do | Cannot Do |
|------|--------|-----------|
| **Admin** | Add/edit/delete doctors, view all patients, manage system | Write prescriptions |
| **Doctor** | View appointments, write prescriptions, view patient records | Add doctors, modify other doctors' data |
| **Patient** | Book appointments, view own records, search doctors | Add doctors, modify system settings |

## 8. Stored Procedures

### GetDailyAppointmentReportByDoctor
Returns all appointments for a specific doctor on a given date.

### GetDoctorWithMostPatientsByMonth
Returns the doctor with the most unique patients in a given month/year.

### GetDoctorWithMostPatientsByYear
Returns the doctor with the most unique patients in a given year.

## 9. Deployment Architecture

```
┌─────────────────────────────────────────┐
│              Docker Container            │
│  ┌─────────────────────────────────┐   │
│  │     Spring Boot Application      │   │
│  │     (Embedded Tomcat on 8080)   │   │
│  └─────────────────────────────────┘   │
│  ┌─────────────┐  ┌─────────────┐      │
│  │   MySQL 8    │  │  MongoDB    │      │
│  │  (Port 3306) │  │ (Port 27017)│      │
│  └─────────────┘  └─────────────┘      │
└─────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────┐
│          GitHub Actions CI              │
│  1. Compile Java code                   │
│  2. Run unit tests                      │
│  3. Build Docker image                  │
│  4. Push to container registry          │
└─────────────────────────────────────────┘
```
