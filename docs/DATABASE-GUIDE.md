# 🗄️ Database Setup Guide — Step by Step

Don't stress! This is just **copy-paste** work. Follow these steps exactly.

---

## Prerequisites

Make sure MySQL and MongoDB are running on your machine.

---

## Step 1: Create the MySQL Database

Open **MySQL CLI** and run:

```sql
CREATE DATABASE IF NOT EXISTS cms;
USE cms;
```

**What this does:** Creates a database called `cms` (Clinic Management System).

---

## Step 2: Let Spring Boot Create the Tables

When you run `mvn spring-boot:run`, Spring Boot automatically creates the tables for you!

```bash
cd java-database-capstone/app
mvn spring-boot:run
```

Wait for it to start, then press `Ctrl+C` to stop it.

**What this does:** Hibernate (JPA) reads your `@Entity` classes and creates matching MySQL tables:
- `admin`
- `doctor`
- `doctor_available_times`
- `patient`
- `appointment`

---

## Step 3: Verify Tables Were Created

Open MySQL CLI again:

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

---

## Step 4: Insert Sample Data

Copy-paste the entire contents of `docs/sample-data.sql` into MySQL CLI.

**What this does:** Populates your database with 25 doctors, 25 patients, 100+ appointments, and 1 admin.

---

## Step 5: Verify Data Was Inserted

Run these commands in MySQL CLI:

```sql
-- Check doctors
SELECT * FROM doctor LIMIT 5;

-- Check patients
SELECT * FROM patient LIMIT 5;

-- Check appointments
SELECT * FROM appointment ORDER BY appointment_time LIMIT 5;

-- Check admin
SELECT * FROM admin;
```

---

## Step 6: Set Up MongoDB (Prescriptions)

Open **MongoDB CLI** (mongosh or mongo) and paste the contents of `docs/mongodb-prescriptions.js`.

**What this does:** Creates 24 prescription documents in the `prescriptions` collection.

---

## Step 7: Verify MongoDB Data

```javascript
use prescriptions;
db.prescriptions.find().limit(5).pretty();
```

---

## Step 8: Create Stored Procedures

Copy-paste the entire contents of `docs/stored-procedures.sql` into MySQL CLI.

---

## Step 9: Test Stored Procedures

```sql
-- Daily report for April 15, 2025
CALL GetDailyAppointmentReportByDoctor('2025-04-15');

-- Doctor with most patients in April 2025
CALL GetDoctorWithMostPatientsByMonth(4, 2025);

-- Doctor with most patients in 2025
CALL GetDoctorWithMostPatientsByYear(2025);
```

**Save the output!** You need to submit these for the final assignment.

---

## Quick Reference — Files to Copy-Paste

| File | Where to Run | What It Does |
|------|-------------|--------------|
| `docs/sample-data.sql` | MySQL CLI | Inserts sample data |
| `docs/stored-procedures.sql` | MySQL CLI | Creates 3 reporting procedures |
| `docs/mongodb-prescriptions.js` | MongoDB CLI | Inserts prescription documents |

---

## Troubleshooting

**"Table doesn't exist"** → Run `mvn spring-boot:run` first to let Hibernate create tables.

**"Duplicate entry"** → You already inserted data. Either delete it first or ignore the error.

**"Access denied"** → Check your MySQL username/password in `application.properties`.

**MongoDB connection error** → Make sure MongoDB is running on port 27017.
