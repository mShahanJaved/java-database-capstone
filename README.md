# 🏥 Smart Clinic Management System

A full-stack web application for managing outpatient clinic operations, built with **Java Spring Boot**, **MySQL**, **MongoDB**, and **Docker**. This system supports three user roles — **Admin**, **Doctor**, and **Patient** — each with role-specific dashboards and capabilities.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.5-green)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![MongoDB](https://img.shields.io/badge/MongoDB-7.0-green)
![Docker](https://img.shields.io/badge/Docker-24.0-blue)
![License](https://img.shields.io/badge/License-MIT-yellow)

---

## 📋 Table of Contents

- [About the Project](#about-the-project)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Features](#features)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [API Endpoints](#api-endpoints)
- [Database Design](#database-design)
- [Screenshots](#screenshots)
- [Documentation](#documentation)

---

## 🎯 About the Project

SmartCare Solutions needed a modern system to replace spreadsheets and outdated tools used by small-to-mid-sized clinics. This **Smart Clinic Management System** provides:

- **Admin Portal** — Manage doctors, view patients, monitor appointments
- **Doctor Portal** — View schedules, manage appointments, write prescriptions
- **Patient Portal** — Search doctors, book appointments, view prescriptions

Built as part of the **IBM Java Developer Professional Certificate** capstone project.

---

## 🏗️ Architecture

The system follows a **three-tier architecture**:

```
┌─────────────────────────────────────────┐
│         Presentation Layer               │
│   HTML/CSS/JS + Thymeleaf Templates      │
│   (Admin, Doctor, Patient Portals)       │
└──────────────────┬──────────────────────┘
                   │
┌──────────────────▼──────────────────────┐
│         Application Layer                │
│   Spring Boot 3.2 + Spring Security     │
│   Controllers → Services → Repositories  │
│   JWT Authentication + RBAC             │
└──────────────────┬──────────────────────┘
                   │
┌──────────────────▼──────────────────────┐
│           Data Layer                     │
│   MySQL 8 (relational) + MongoDB (docs)  │
│   JPA/Hibernate + Spring Data MongoDB    │
└─────────────────────────────────────────┘
```

For detailed architecture documentation, see [docs/schema-architecture.md](docs/schema-architecture.md).

---

## 🛠️ Tech Stack

| Layer | Technology | Purpose |
|-------|-----------|---------|
| **Frontend** | HTML5, CSS3, JavaScript | Dynamic web pages |
| **Template Engine** | Thymeleaf | Server-side rendering |
| **Backend** | Spring Boot 3.2.5 | REST APIs + MVC |
| **Security** | Spring Security + JWT | Authentication & authorization |
| **Relational DB** | MySQL 8.0 | Structured data (users, doctors, patients, appointments) |
| **Document DB** | MongoDB | Flexible data (prescriptions) |
| **ORM** | Hibernate (JPA) | Java ↔ Database mapping |
| **Container** | Docker | Consistent deployment |
| **CI/CD** | GitHub Actions | Automated build & test |
| **Build Tool** | Maven | Dependency management |

---

## ✨ Features

### Admin Portal
- 🔐 Secure login with JWT authentication
- 👨‍⚕️ Add, edit, and delete doctors
- 👥 View all patients and their records
- 📅 Monitor all appointments across the clinic

### Doctor Portal
- 📋 View daily and weekly appointment schedules
- ✅ Mark appointments as completed or cancelled
- 💊 Write prescriptions with medicine names, dosage, and notes
- 📖 View patient medical history

### Patient Portal
- 🔍 Search doctors by name or specialization
- 📅 Book appointments with available time slots
- 📋 View appointment history and status
- 💊 View prescriptions from completed appointments

---

## 📁 Project Structure

```
java-database-capstone/
├── schema-design.md              # MySQL + MongoDB schema design
├── user_stories.md               # User stories (Admin, Doctor, Patient)
├── docs/
│   └── schema-architecture.md    # Architecture design document
├── src/
│   └── main/
│       ├── java/com/smartcare/smartclinic/
│       │   ├── SmartClinicApplication.java   # Main entry point
│       │   ├── config/                       # Security configuration
│       │   ├── entity/                       # JPA entities + MongoDB documents
│       │   │   ├── User.java
│       │   │   ├── Doctor.java
│       │   │   ├── Patient.java
│       │   │   ├── Appointment.java
│       │   │   ├── Prescription.java
│       │   │   └── Admin.java
│       │   ├── repository/                   # Data access layer
│       │   │   ├── UserRepository.java
│       │   │   ├── DoctorRepository.java
│       │   │   ├── PatientRepository.java
│       │   │   ├── AppointmentRepository.java
│       │   │   └── PrescriptionRepository.java
│       │   ├── service/                      # Business logic layer
│       │   │   ├── DoctorService.java
│       │   │   ├── TokenService.java
│       │   │   └── ...
│       │   └── controller/                   # HTTP request handlers
│       │       └── ...
│       └── resources/
│           ├── application.properties        # App configuration
│           ├── templates/                    # Thymeleaf HTML templates
│           │   ├── index.html
│           │   ├── login.html
│           │   ├── admin/
│           │   ├── doctor/
│           │   └── patient/
│           ├── static/                       # CSS, JS, images
│           └── data.sql                      # Sample data
├── Dockerfile                                # Docker containerization
├── .github/workflows/ci.yml                 # GitHub Actions CI
├── pom.xml                                   # Maven dependencies
└── README.md                                 # This file
```

---

## 🚀 Getting Started

### Prerequisites

- **Java 17** or higher
- **Maven 3.8+**
- **MySQL 8.0**
- **MongoDB 7.0**
- **Docker** (optional, for containerized deployment)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/java-database-capstone.git
   cd java-database-capstone
   ```

2. **Set up MySQL database**
   ```sql
   CREATE DATABASE smart_clinic;
   ```

3. **Configure database credentials** in `src/main/resources/application.properties`

4. **Build and run**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

5. **Access the application**
   Open [http://localhost:8080](http://localhost:8080)

### Docker Deployment

```bash
docker build -t smart-clinic .
docker run -p 8080:8080 smart-clinic
```

---

## 📡 API Endpoints

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/api/auth/login` | Login and receive JWT | No |
| GET | `/api/doctors` | List all doctors | Yes |
| GET | `/api/doctors/search?name=x` | Search doctors | Yes |
| POST | `/api/doctors` | Create doctor | Admin |
| GET | `/api/patients` | List all patients | Admin/Doctor |
| POST | `/api/appointments` | Book appointment | Patient |
| GET | `/api/appointments` | List appointments | Yes |
| POST | `/api/prescriptions` | Write prescription | Doctor |

---

## 🗄️ Database Design

### MySQL Tables
- **users** — Authentication credentials (username, password, role)
- **doctors** — Doctor profiles (name, email, specialization, availability)
- **patients** — Patient profiles (name, email, DOB, gender, address)
- **appointments** — Scheduling data (doctor, patient, date, time, status)
- **admins** — Admin profiles (name, email)

### MongoDB Collections
- **prescriptions** — Flexible prescription documents (medicines, dosage, notes)

For full schema details, see [docs/schema-design.md](docs/schema-design.md).

---

## 📸 Screenshots

> Screenshots of the application will be added after deployment.

- Role Selection Page
- Admin Portal — Adding a Doctor
- Patient Portal — Searching Doctors
- Doctor Portal — Viewing Appointments
- Patient Portal — View Prescription

---

## 📚 Documentation

| Document | Description |
|----------|-------------|
| [Architecture Design](docs/schema-architecture.md) | Three-tier architecture, request flow, dual DB integration |
| [User Stories](user_stories.md) | 17 user stories for Admin, Doctor, and Patient roles |
| [Schema Design](schema-design.md) | MySQL tables, MongoDB collections, ER diagram |

---

## 🧪 CI/CD

This project uses **GitHub Actions** for continuous integration:

- ✅ Compile Java code
- ✅ Run unit tests
- ✅ Lint check
- ✅ Build Docker image

Workflow file: [`.github/workflows/ci.yml`](.github/workflows/ci.yml)

---

## 📄 License

This project is part of the **IBM Java Developer Professional Certificate** capstone.

---

## 🙏 Acknowledgments

- IBM Java Developer Professional Certificate
- Spring Boot Documentation
- Spring Data JPA
- Spring Data MongoDB
