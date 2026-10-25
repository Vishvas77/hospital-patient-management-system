# Test Cases — Hospital Patient Management System

All test cases below were **actually executed** with the compiled program
(OpenJDK 17, `javac` / `java`). The exact inputs are stored in
`tests/input_runN.txt` and the exact outputs produced are stored in
`tests/output_runN.txt`. Each run starts in a **fresh folder** containing only
the compiled classes, so every run begins with no saved data — except Test
Run 5, whose two parts deliberately share one folder to prove that records
persist across restarts. To repeat a run:

```bash
cd src
javac *.java
# copy the .class files into a fresh empty folder, then in that folder:
java HospitalManagementSystem < ../tests/input_run1.txt   # (or run2 / run3 / run4 / run6)
```

## Test Run 1 — Normal workflow

Script: register two patients (Ravi Kumar, 35, Fever → ID 101; Meena Sharma, 28, Fracture → ID 102),
display all, search 101, search 999, display doctors, assign doctors (102 → Doctor 4; 101 → Doctor 2),
admit 102 to a Private room, discharge 102 after 3 days, display all, menu choice 99, exit.

| TC | Test Case | Expected Output | Actual Output | Result |
|----|-----------|-----------------|---------------|--------|
| TC01 | Register a new patient with valid details | "Patient registered successfully. Patient ID = 101" (then 102) | Same as expected | Pass |
| TC02 | Display all patients | Details of patients 101 and 102 | Same as expected | Pass |
| TC03 | Search an existing patient by ID (101) | Record of patient 101 displayed | Same as expected | Pass |
| TC04 | Search a non-existing patient by ID (999) | "Patient with ID 999 not found." | Same as expected | Pass |
| TC05 | Display all doctors | Table of 4 doctors with specializations | Same as expected | Pass |
| TC06 | Assign a doctor to a patient | "Doctor Dr. Arjun Mehta assigned to patient Meena Sharma." | Same as expected | Pass |
| TC07 | Admit (Private) and discharge after 3 days | Bill = 500 + (3 × 2500) = Rs. 8000 | "Total Bill : Rs. 8000", patient discharged | Pass |
| TC08 | Invalid menu choice (99) | "Invalid choice! Please try again." | Same as expected | Pass |

## Test Run 2 — Non-numeric age

Script: choose Register, enter name "Test Patient", enter age as the text `thirty`, then exit.

| TC | Test Case | Expected Output | Actual Output | Result |
|----|-----------|-----------------|---------------|--------|
| TC09 | Age entered as text instead of a number | "Invalid input! Age must be a number. Patient not registered." and the program continues (no crash) | Same as expected | Pass |

## Test Run 3 — Age out of range

Script: choose Register, enter name "Old Patient", enter age `150`, then exit.

| TC | Test Case | Expected Output | Actual Output | Result |
|----|-----------|-----------------|---------------|--------|
| TC10 | Age outside the valid range (150) | "Invalid age! Patient not registered." | Same as expected | Pass |

## Test Run 4 — Admission, discharge and ID edge cases

Script: register Suresh Verma (40, Diabetes → ID 101); admit 101 with room choice 9; display all;
admit 101 to General room; admit 101 again; discharge 101 with 0 days; discharge 101 with 2 days;
discharge 101 again; assign doctor to patient 999; search with ID `abc`; discharge patient 999; exit.

| TC | Test Case | Expected Output | Actual Output | Result |
|----|-----------|-----------------|---------------|--------|
| TC11 | Invalid room type choice (9) at admission | "Invalid room type choice! Admission cancelled." and the patient remains "Not Admitted" in the next display | Same as expected | Pass |
| TC12 | Admit the same patient a second time | "Patient is already admitted." | Same as expected | Pass |
| TC13 | Discharge with 0 days stayed | "Invalid number of days! Days must be at least 1. Discharge cancelled." and the patient stays admitted | Same as expected | Pass |
| TC14 | Discharge from General room after 2 days | Bill = 500 + (2 × 1000) = Rs. 2500 | "Total Bill : Rs. 2500", patient discharged | Pass |
| TC15 | Discharge a patient who is not admitted (second discharge) | "Patient is not admitted, so discharge is not possible." | Same as expected | Pass |
| TC16 | Assign a doctor to a non-existing patient (ID 999) | "Patient with ID 999 not found." | Same as expected | Pass |
| TC17 | Non-numeric patient ID in search (`abc`) | "Invalid input! Patient ID must be a number." and the program continues (no crash) | Same as expected | Pass |
| TC18 | Discharge a non-existing patient (ID 999) | "Patient with ID 999 not found." | Same as expected | Pass |

## Test Run 5 — Persistence across restarts (two parts, one shared folder)

Part A script: register Kavita Rao (30, Migraine → ID 101), admit 101 to a General room, exit.
Part B script (program started again in the same folder): display all, register Rohan Shah
(22, Sprain), view hospital summary, exit.

| TC | Test Case | Expected Output | Actual Output | Result |
|----|-----------|-----------------|---------------|--------|
| TC19 | Records survive a restart | In Part B, patient 101 (Kavita Rao) is still listed, still "Admitted (General, Room No. 201)" | Same as expected | Pass |
| TC20 | ID counters survive a restart | The new patient in Part B gets ID 102 (IDs are not reused) | "Patient registered successfully. Patient ID = 102" | Pass |

## Test Run 6 — Search by name, update, appointments, delete, summary

Script: register Anil Gupta (50, Back Pain → ID 101); search by name `anil gupta`; search by name
`Nobody`; update 101 (phone 9000011111, disease Slip Disc); display all; book appointment for 101
with Doctor 4 on 28-10-2026 at 10:30 AM; book appointment for patient 999; view appointments;
register Priya Nair (26, Cold → ID 102); admit 102 to a General room; delete 102; discharge 102
after 1 day; delete 102; view hospital summary; view appointments; exit.

| TC | Test Case | Expected Output | Actual Output | Result |
|----|-----------|-----------------|---------------|--------|
| TC21 | Search by name (different letter case) | Record of Anil Gupta displayed | Same as expected | Pass |
| TC22 | Search by a name that does not exist | "No patient found with name Nobody." | Same as expected | Pass |
| TC23 | Update patient details | "Patient details updated successfully." and the display shows phone 9000011111 and disease Slip Disc | Same as expected | Pass |
| TC24 | Book an appointment | "Appointment booked successfully. Appointment ID = 1" | Same as expected | Pass |
| TC25 | Book an appointment for a non-existing patient (999) | "Patient with ID 999 not found." | Same as expected | Pass |
| TC26 | View all appointments | Appointment 1 listed with patient name, doctor, date and time | Same as expected | Pass |
| TC27 | Delete an admitted patient | "Cannot delete an admitted patient. Discharge the patient first." | Same as expected | Pass |
| TC28 | Discharge from General room after 1 day | Bill = 500 + (1 × 1000) = Rs. 1500 | "Total Bill : Rs. 1500" | Pass |
| TC29 | Delete a discharged patient | "Patient record deleted successfully." | Same as expected | Pass |
| TC30 | Hospital summary after the above operations | 1 registered patient, 0 admitted, 1 appointment, revenue Rs. 1500 | Same as expected | Pass |

## Summary

**30 / 30 test cases passed.** All three bill amounts (Rs. 8000, Rs. 2500 and
Rs. 1500) were also checked by hand against the billing formula
`Total = 500 + (room charge per day × days)` and agree with the program output.
The data file written by Test Run 5 contained the `#META` counter line and both
patient records in the documented `|`-separated format.
