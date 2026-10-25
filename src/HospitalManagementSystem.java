// HospitalManagementSystem.java - Main class with the menu-driven program
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class HospitalManagementSystem {

    static ArrayList<Patient> patients = new ArrayList<>();
    static ArrayList<Doctor> doctors = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);
    static int nextPatientId = 101;
    static int nextRoomNo = 201;

    public static void main(String[] args) {
        loadSampleDoctors();
        int choice = 0;
        do {
            displayMenu();
            try {
                choice = scanner.nextInt();
                scanner.nextLine();   // clear the pending newline
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                scanner.nextLine();
                continue;
            }
            switch (choice) {
                case 1: registerPatient();   break;
                case 2: displayAllPatients(); break;
                case 3: searchPatient();     break;
                case 4: displayAllDoctors(); break;
                case 5: assignDoctor();      break;
                case 6: admitPatient();      break;
                case 7: dischargePatient();  break;
                case 8: System.out.println("Thank you. Exiting the system."); break;
                default: System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 8);
    }

    static void displayMenu() {
        System.out.println("\n===== HOSPITAL PATIENT MANAGEMENT SYSTEM =====");
        System.out.println("1. Register New Patient");
        System.out.println("2. Display All Patients");
        System.out.println("3. Search Patient by ID");
        System.out.println("4. Display All Doctors");
        System.out.println("5. Assign Doctor to Patient");
        System.out.println("6. Admit Patient");
        System.out.println("7. Discharge Patient and Generate Bill");
        System.out.println("8. Exit");
        System.out.print("Enter your choice: ");
    }

    // Reads one integer safely. Returns -1 when the input is not a
    // number, so the calling method can reject it and return to the menu.
    static int readInt() {
        try {
            int value = scanner.nextInt();
            scanner.nextLine();   // clear the pending newline
            return value;
        } catch (InputMismatchException e) {
            scanner.nextLine();   // discard the wrong input
            return -1;
        }
    }

    // A few doctors are added in advance so that patients can be assigned
    static void loadSampleDoctors() {
        doctors.add(new Doctor(1, "Dr. Ananya Rao", 38, "Female", "9000000001", "Cardiology"));
        doctors.add(new Doctor(2, "Dr. Vikram Reddy", 45, "Male", "9000000002", "General Medicine"));
        doctors.add(new Doctor(3, "Dr. Sneha Patel", 33, "Female", "9000000003", "Pediatrics"));
        doctors.add(new Doctor(4, "Dr. Arjun Mehta", 41, "Male", "9000000004", "Orthopedics"));
    }

    static void registerPatient() {
        try {
            System.out.print("Enter patient name: ");
            String name = scanner.nextLine();
            System.out.print("Enter age: ");
            int age = scanner.nextInt();
            scanner.nextLine();
            if (age <= 0 || age > 120) {
                System.out.println("Invalid age! Patient not registered.");
                return;
            }
            System.out.print("Enter gender: ");
            String gender = scanner.nextLine();
            System.out.print("Enter phone number: ");
            String phone = scanner.nextLine();
            System.out.print("Enter disease / problem: ");
            String disease = scanner.nextLine();

            Patient p = new Patient(nextPatientId, name, age, gender, phone, disease);
            patients.add(p);
            System.out.println("Patient registered successfully. Patient ID = " + nextPatientId);
            nextPatientId++;
        } catch (InputMismatchException e) {
            System.out.println("Invalid input! Age must be a number. Patient not registered.");
            scanner.nextLine();
        }
    }

    static void displayAllPatients() {
        if (patients.isEmpty()) {
            System.out.println("No patients registered yet.");
            return;
        }
        System.out.println("\n---------- LIST OF PATIENTS ----------");
        for (Patient p : patients) {
            p.displayPatient();
        }
    }

    // Returns the patient with the given ID, or null if not found
    static Patient findPatient(int id) {
        for (Patient p : patients) {
            if (p.getPatientId() == id) {
                return p;
            }
        }
        return null;
    }

    static void searchPatient() {
        System.out.print("Enter patient ID to search: ");
        int id = readInt();
        if (id == -1) {
            System.out.println("Invalid input! Patient ID must be a number.");
            return;
        }
        Patient p = findPatient(id);
        if (p == null) {
            System.out.println("Patient with ID " + id + " not found.");
        } else {
            p.displayPatient();
        }
    }

    static void displayAllDoctors() {
        System.out.println("\n---------- LIST OF DOCTORS ----------");
        System.out.printf("%-4s %-20s %-18s %s%n", "ID", "Name", "Specialization", "Phone");
        for (Doctor d : doctors) {
            d.displayDoctor();
        }
    }

    static void assignDoctor() {
        System.out.print("Enter patient ID: ");
        int pid = readInt();
        if (pid == -1) {
            System.out.println("Invalid input! Patient ID must be a number.");
            return;
        }
        Patient p = findPatient(pid);
        if (p == null) {
            System.out.println("Patient with ID " + pid + " not found.");
            return;
        }
        displayAllDoctors();
        System.out.print("Enter doctor ID to assign: ");
        int did = readInt();
        if (did == -1) {
            System.out.println("Invalid input! Doctor ID must be a number.");
            return;
        }
        for (Doctor d : doctors) {
            if (d.getDoctorId() == did) {
                p.setDoctorName(d.getName());
                System.out.println("Doctor " + d.getName() + " assigned to patient " + p.getName() + ".");
                return;
            }
        }
        System.out.println("Doctor with ID " + did + " not found.");
    }

    static void admitPatient() {
        System.out.print("Enter patient ID to admit: ");
        int id = readInt();
        if (id == -1) {
            System.out.println("Invalid input! Patient ID must be a number.");
            return;
        }
        Patient p = findPatient(id);
        if (p == null) {
            System.out.println("Patient with ID " + id + " not found.");
            return;
        }
        if (p.isAdmitted()) {
            System.out.println("Patient is already admitted.");
            return;
        }
        System.out.println("Select room type: 1. General  2. Private  3. ICU");
        System.out.print("Enter room type choice: ");
        int roomChoice = readInt();
        String roomType;
        if (roomChoice == 1) {
            roomType = "General";
        } else if (roomChoice == 2) {
            roomType = "Private";
        } else if (roomChoice == 3) {
            roomType = "ICU";
        } else {
            // Any other choice is wrong: cancel the admission
            System.out.println("Invalid room type choice! Admission cancelled.");
            return;
        }
        p.admit(roomType, nextRoomNo);
        System.out.println("Patient admitted. Room No. = " + nextRoomNo
                           + " (" + roomType + ")");
        nextRoomNo++;
    }

    static void dischargePatient() {
        System.out.print("Enter patient ID to discharge: ");
        int id = readInt();
        if (id == -1) {
            System.out.println("Invalid input! Patient ID must be a number.");
            return;
        }
        Patient p = findPatient(id);
        if (p == null) {
            System.out.println("Patient with ID " + id + " not found.");
            return;
        }
        if (!p.isAdmitted()) {
            System.out.println("Patient is not admitted, so discharge is not possible.");
            return;
        }
        System.out.print("Enter number of days stayed: ");
        int days = readInt();
        if (days == -1) {
            System.out.println("Invalid input! Number of days must be a number. Discharge cancelled.");
            return;
        }
        if (days <= 0) {
            System.out.println("Invalid number of days! Days must be at least 1. Discharge cancelled.");
            return;
        }
        int roomCharges = p.roomChargePerDay() * days;
        int total = p.calculateBill(days);
        System.out.println("\n---------- DISCHARGE BILL ----------");
        System.out.println("Patient ID   : " + p.getPatientId());
        System.out.println("Name         : " + p.getName());
        System.out.println("Room Type    : " + p.getRoomType() + " (Room No. " + p.getRoomNo() + ")");
        System.out.println("Days Stayed  : " + days);
        System.out.println("Consultation Fee         : Rs. 500");
        System.out.println("Room Charges (" + days + " x " + p.roomChargePerDay() + ") : Rs. " + roomCharges);
        System.out.println("Total Bill               : Rs. " + total);
        System.out.println("--------------------------------------");
        p.discharge();
        System.out.println("Patient discharged successfully.");
    }
}
