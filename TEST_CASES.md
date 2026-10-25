# Test Cases — Hospital Patient Management System

All test cases below were **actually executed** with the compiled program
(OpenJDK 17, `javac` / `java`). The exact inputs are stored in
`tests/input_runN.txt` and the exact outputs produced are stored in
`tests/output_runN.txt`, so every case can be repeated with:

```bash
cd src
javac *.java
java HospitalManagementSystem < ../tests/input_run1.txt   # (or run2 / run3 / run4)
```

## Test Run 1 — Normal workflow

Script: register two patients (Ravi Kumar, 35, Fever → ID 101; Meena Sharma, 28, Fracture → ID 102),
display all, search 101, search 999, display doctors, assign doctors (102 → Doctor 4; 101 → Doctor 2),
admit 102 to a Private room, discharge 102 after 3 days, display all, menu choice 9, exit.

| TC | Test Case | Expected Output | Actual Output | Result |
|----|-----------|-----------------|---------------|--------|
| TC01 | Register a new patient with valid details | "Patient registered successfully. Patient ID = 101" (then 102) | Same as expected | Pass |
| TC02 | Display all patients | Details of patients 101 and 102 | Same as expected | Pass |
| TC03 | Search an existing patient (ID 101) | Record of patient 101 displayed | Same as expected | Pass |
| TC04 | Search a non-existing patient (ID 999) | "Patient with ID 999 not found." | Same as expected | Pass |
| TC05 | Display all doctors | Table of 4 doctors with specializations | Same as expected | Pass |
| TC06 | Assign a doctor to a patient | "Doctor Dr. Arjun Mehta assigned to patient Meena Sharma." | Same as expected | Pass |
| TC07 | Admit (Private) and discharge after 3 days | Bill = 500 + (3 × 2500) = Rs. 8000 | "Total Bill : Rs. 8000", patient discharged | Pass |
| TC08 | Invalid menu choice (9) | "Invalid choice! Please try again." | Same as expected | Pass |

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

## Summary

**18 / 18 test cases passed.** Both bill amounts (Rs. 8000 and Rs. 2500) were
also checked by hand against the billing formula
`Total = 500 + (room charge per day × days)` and agree with the program output.
