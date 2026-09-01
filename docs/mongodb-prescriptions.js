// Run this with: mongosh < docs/mongodb-prescriptions.js
db = db.getSiblingDB('prescriptions');

db.prescriptions.insertMany([
  {
    patientName: "Jane Doe",
    appointmentId: 1,
    medication: "Aspirin",
    dosage: "100mg",
    doctorNotes: "Take once daily after meals"
  },
  {
    patientName: "John Smith",
    appointmentId: 2,
    medication: "Amoxicillin",
    dosage: "500mg",
    doctorNotes: "Take 3 times daily for 7 days"
  },
  {
    patientName: "Emily Rose",
    appointmentId: 3,
    medication: "Ibuprofen",
    dosage: "200mg",
    doctorNotes: "Take as needed for pain, max 3 per day"
  },
  {
    patientName: "Michael Jordan",
    appointmentId: 4,
    medication: "Paracetamol",
    dosage: "500mg",
    doctorNotes: "Take every 6 hours if fever persists"
  },
  {
    patientName: "Olivia Moon",
    appointmentId: 5,
    medication: "Metformin",
    dosage: "850mg",
    doctorNotes: "Take twice daily with meals"
  }
]);

print("5 sample prescriptions inserted into prescriptions database!");
