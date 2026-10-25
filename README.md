# Hospital Patient Management System

A menu-driven **console application in Java** that computerises the patient-record work of a hospital: patient registration, doctor assignment, appointments, room admission, and discharge with automatic billing. It replaces manual paper registers, in which searching for a record is slow and billing mistakes are common. **All records are saved to text files automatically, so data is not lost when the program is closed.**

Developed as a B.Tech Course End Project for the **Object Oriented Programming Laboratory (A9602)**, Vardhaman College of Engineering, Hyderabad. The code uses only basic-to-intermediate Java and the Java standard library — no database server, no GUI, no external dependencies.

## Features

1. **Register a new patient** — name, age, gender, phone number and disease are recorded; the program assigns a unique patient ID automatically (starting from 101). Age is validated (must be a number between 1 and 120).
2. **Display all registered patients** with their complete details and current status.
3. **Search a patient by ID** — prints the record, or a clear "not found" message for a missing ID.
4. **Search a patient by name** — case-insensitive name search.
5. **Update patient details** — change a patient's phone number and disease/problem.
6. **Delete a patient record** — refused while the patient is admitted; the patient's appointments are removed along with the record.
7. **Display all doctors** with their specializations (four sample doctors are loaded at start-up).
8. **Assign a doctor to a patient** by patient ID and doctor ID.
9. **Book an appointment** — patient, doctor, date and time; every booking gets a unique appointment ID.
10. **View all appointments** with patient and doctor names.
11. **Admit a patient** into a General, Private or ICU room; the next room number is assigned automatically (starting from 201). An invalid room-type choice cancels the admission, and a patient who is already admitted cannot be admitted again.
12. **Discharge a patient and generate the bill** (formula below). Discharge is refused for patients who are not admitted, and the number of days must be at least 1.
13. **Hospital summary** — total registered patients, patients admitted now, total appointments, and total revenue collected from discharge bills.
14. **Persistent storage** — patients, appointments, ID counters and revenue are saved to `patients.txt` and `appointments.txt` after every change and reloaded at start-up, so IDs are never reused and records survive restarts.
15. **Safe input handling and exit** — non-numeric input at any prompt is rejected with a message (the program never crashes), invalid menu choices are reported, and option 14 exits cleanly.

### Billing formula

```
Room charge per day : General = Rs. 1,000 | Private = Rs. 2,500 | ICU = Rs. 5,000
Consultation fee    : Rs. 500 (fixed)
Total bill          = 500 + (room charge per day × number of days stayed)
```

Example: Private room, 3 days → 500 + (3 × 2500) = **Rs. 8000** (verified in Test Run 1).

## Prerequisites

- Java Development Kit (JDK) 8 or newer (developed and tested on OpenJDK 17)
- A terminal / command prompt
- Any text editor or IDE (VS Code, IntelliJ IDEA, Eclipse) — optional

## Folder Structure

```
Hospital-Patient-Management-System/
├── README.md                          ← this file
├── TEST_CASES.md                      ← test cases with actual results
├── src/
│   ├── Person.java                    ← base class (name, age, gender, phone)
│   ├── Patient.java                   ← extends Person; admission + billing
│   ├── Doctor.java                    ← extends Person; specialization
│   ├── Appointment.java               ← one appointment booking
│   └── HospitalManagementSystem.java  ← main class: menu, file handling, all operations
├── screenshots/                       ← screenshots of actual program execution
└── tests/
    ├── input_run*.txt                 ← scripted inputs used for testing
    └── output_run*.txt                ← actual outputs produced
```

When the program runs, it creates two data files in the working directory:
`patients.txt` (first line `#META|nextPatientId|nextRoomNo|nextAppointmentId|totalRevenue`, then one patient per line, fields separated by `|`) and `appointments.txt` (one appointment per line). Delete these files to start again with empty records.

## How to Compile and Run

```bash
cd src
javac *.java
java HospitalManagementSystem
```

To repeat the recorded tests, copy the compiled classes into a fresh folder (so each run starts with no saved data — Run 5 is the exception: its two parts share one folder to prove persistence) and run:

```bash
java HospitalManagementSystem < ../tests/input_run1.txt
```

## Menu Options

| Choice | Operation |
|--------|-----------|
| 1 | Register New Patient |
| 2 | Display All Patients |
| 3 | Search Patient by ID |
| 4 | Search Patient by Name |
| 5 | Update Patient Details |
| 6 | Delete Patient Record |
| 7 | Display All Doctors |
| 8 | Assign Doctor to Patient |
| 9 | Book Appointment |
| 10 | View All Appointments |
| 11 | Admit Patient |
| 12 | Discharge Patient and Generate Bill |
| 13 | View Hospital Summary |
| 14 | Exit |

## Sample Output (actual, from Test Run 1)

```
---------- DISCHARGE BILL ----------
Patient ID   : 102
Name         : Meena Sharma
Room Type    : Private (Room No. 201)
Days Stayed  : 3
Consultation Fee         : Rs. 500
Room Charges (3 x 2500) : Rs. 7500
Total Bill               : Rs. 8000
--------------------------------------
Patient discharged successfully.
```

## Execution Screenshots

Actual terminal runs of the program (inputs typed at the prompts are visible):

**Patient registration and patient list**

![Patient registration and patient list](screenshots/execution_registration.png)

**Doctor list and doctor assignment**

![Doctor list and doctor assignment](screenshots/execution_doctors.png)

**Admission and discharge bill (Private room, 3 days - Rs. 8000)**

![Admission and discharge bill](screenshots/execution_billing.png)

## Important Implementation Decisions

- **Inheritance:** `Person` is the base class holding the details common to everyone (name, age, gender, phone). `Patient` and `Doctor` extend it, so common code is written once.
- **Encapsulation:** all data members are `private`; access is through getter/setter methods.
- **Collections:** records are stored in `ArrayList<Patient>`, `ArrayList<Doctor>` and `ArrayList<Appointment>`, so any number of records can be kept without fixing a size in advance. Searches are simple linear searches.
- **Exception handling:** a small helper, `readInt()`, reads integers inside a `try-catch` for `InputMismatchException` and returns `-1` for non-numeric input; every calling method rejects `-1` with a message and returns to the menu. Age is additionally range-checked.
- **File handling:** `BufferedReader`/`BufferedWriter` save and load the records in a simple `|`-separated text format. Saving happens after every change (register, update, delete, assign, admit, discharge, book), so even a sudden closing of the program loses nothing after the last completed operation. The `#META` line keeps the ID counters and the total revenue, so patient IDs and room numbers are never reused after a restart.
- **State checks before operations:** admission is refused if the patient is already admitted or the room-type choice is invalid; discharge is refused if the patient is not admitted or the days are less than 1; deletion is refused for an admitted patient. No invalid operation is performed silently.
- **Known limitations:** the data files use a simple text format (field values must not contain the `|` character), the application is single-user and console-based, and there is no login facility. Storing the same data in a database through JDBC and adding a Swing GUI are natural future enhancements.
