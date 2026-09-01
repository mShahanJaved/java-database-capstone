# Schema Architecture — Smart Clinic Management System

## Section 1: Architecture Summary

The Smart Clinic Management System follows a **three-tier architecture** pattern, which separates the application into three distinct layers: **Presentation**, **Application**, and **Data**. This separation ensures that each layer has a single responsibility, making the system easier to develop, test, and maintain.

**Presentation Layer** is what the user sees and interacts with. It consists of HTML, CSS, and JavaScript pages that run in the browser. The application uses two approaches for the frontend: **Thymeleaf templates** for server-side rendered pages (like the login screen and dashboards) and **REST API calls** from JavaScript for dynamic data operations (like searching doctors or booking appointments). Thymeleaf generates HTML on the server before sending it to the browser, while REST APIs return raw JSON data that JavaScript uses to update the page without reloading.

**Application Layer** is the brain of the system, built with **Spring Boot**. It contains three sub-layers organized vertically:

- **Controllers** handle incoming HTTP requests and decide what to do with them. **MVC Controllers** (using `@Controller`) return Thymeleaf HTML pages for the portals. **REST Controllers** (using `@RestController`) return JSON data for API consumers. Both types are organized by domain — `DoctorController` handles doctor operations, `AppointmentController` handles appointments, and so on.

- **Services** contain the business logic and rules. For example, `DoctorService` enforces that a doctor must have a name and specialization before being created. `TokenService` handles JWT token generation and validation. Services sit between controllers and repositories, ensuring data is processed correctly before being saved or returned.

- **Repositories** communicate directly with the databases using **Spring Data JPA** for MySQL and **Spring Data MongoDB** for MongoDB. They translate Java method calls into database queries automatically — no raw SQL needed for most operations.

**Data Layer** uses a **polyglot persistence** strategy, meaning different databases are used for different types of data:

- **MySQL** stores relational, structured data: Users (authentication), Doctors, Patients, Appointments, and Admins. These entities have fixed relationships (a doctor has many appointments, an appointment links one doctor to one patient) that fit perfectly in a relational model.

- **MongoDB** stores flexible, document-based data: Prescriptions. Each prescription can have a variable number of medicines, different dosage formats, and optional notes. MongoDB's schema-less design accommodates this flexibility without requiring junction tables or nullable columns.

**Authentication** is handled by **Spring Security** with **JWT (JSON Web Tokens)**. When a user logs in, the server validates credentials, creates a signed token containing the user's role (Admin, Doctor, or Patient), and sends it back. The browser stores this token and includes it in every subsequent request. The server verifies the token's signature and extracts the user's role to enforce access control — Admins can add doctors, Doctors can write prescriptions, and Patients can book appointments.

## Section 2: Request/Response Flow

Below is the numbered flow for a typical request — a **Patient searching for doctors by name**:

```
Step 1: User Action
   The patient types "Emily" into the search bar on the Patient Portal
   and presses Enter.

Step 2: JavaScript Intercepts
   The frontend JavaScript captures the search input and constructs
   a GET request: GET /api/doctors/search?name=Emily
   It includes the JWT token in the header: Authorization: Bearer <token>

Step 3: HTTP Request Travels to Server
   The browser sends the HTTP request over the network to the
   Spring Boot application running on port 8080.

Step 4: Security Filter Chain (Application Layer)
   The JWT Authentication Filter intercepts the request BEFORE it
   reaches the controller. It extracts the token from the header,
   validates the signature using the secret key, checks expiration,
   and loads the user's role. If valid, the request proceeds.
   If invalid, a 401 Unauthorized response is returned immediately.

Step 5: DispatcherServlet Routes to Controller (Application Layer)
   Spring Boot's DispatcherServlet reads the URL pattern (/api/doctors/search)
   and routes the request to the matching handler method in
   DoctorController.java.

Step 6: Controller Processes Request (Application Layer)
   DoctorController's searchDoctors() method extracts the "name" parameter
   ("Emily") and calls DoctorService.searchDoctors("Emily").

Step 7: Service Applies Business Logic (Application Layer)
   DoctorService validates the input (ensures it's not empty),
   then calls DoctorRepository.findByNameContainingIgnoreCaseOrSpecializationContainingIgnoreCase("Emily", "Emily").

Step 8: Repository Queries Database (Data Layer)
   Spring Data JPA translates the method name into SQL:
   SELECT * FROM doctors WHERE LOWER(name) LIKE '%emily%'
   OR LOWER(specialization) LIKE '%emily%'
   The query is sent to MySQL via JDBC.

Step 9: MySQL Returns Results (Data Layer)
   MySQL executes the query and returns matching rows:
   Doctor "Dr. Emily Adams" (Cardiologist, dr.adams@example.com)
   The ResultSet is mapped back to Doctor Java objects by Hibernate.

Step 10: Response Flows Back Up the Stack
   DoctorRepository returns List<Doctor> → DoctorService returns it →
   DoctorController serializes the list to JSON and returns it
   with HTTP status 200 OK.

Step 11: Frontend Renders Results (Presentation Layer)
   The JavaScript receives the JSON response, creates HTML card
   elements for each doctor (showing name, specialization, email,
   available time slots, and a "Book Now" button), and injects them
   into the page DOM.
```

### Dual Database Integration Flow

For operations involving **Prescriptions** (stored in MongoDB):

```
Step 1: Doctor clicks prescription icon on a patient's appointment record

Step 2: JavaScript sends GET /api/prescriptions/appointment/{appointmentId}

Step 3: Request passes through JWT security filter (same as above)

Step 4: PrescriptionController calls PrescriptionService.findByAppointmentId()

Step 5: PrescriptionService calls PrescriptionRepository (MongoDB)
        db.prescriptions.find({ "appointmentId": 131 })

Step 6: MongoDB returns the prescription document (JSON-like structure)
        containing medicineNames[], dosageInstructions, additionalNotes

Step 7: Response flows back and JavaScript renders the "View Prescription"
        form showing Patient Name, Medicine Names, Dosage Instructions,
        and Additional Notes.
```

### Key Architectural Decisions

| Decision | Choice | Rationale |
|----------|--------|-----------|
| Frontend rendering | Thymeleaf + JavaScript | Thymeleaf for server-rendered pages (login), JavaScript for dynamic data |
| API style | RESTful JSON | Standard, widely supported, easy to test with Postman |
| Authentication | JWT tokens | Stateless, scalable, works across services |
| Relational DB | MySQL | ACID compliance, strong consistency for appointments |
| Document DB | MongoDB | Flexible schema for variable prescription structures |
| ORM | Hibernate (JPA) | Automatic table mapping, reduces boilerplate SQL |
| Containerization | Docker | Consistent environments across development and production |
