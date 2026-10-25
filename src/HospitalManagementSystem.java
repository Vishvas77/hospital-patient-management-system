// HospitalManagementSystem.java - Main class with the menu-driven program
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class HospitalManagementSystem {

    static ArrayList<Patient> patients = new ArrayList<>();
    static ArrayList<Doctor> doctors = new ArrayList<>();
    static ArrayList<Appointment> appointments = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);
    static int nextPatientId = 101;
    static int nextRoomNo = 201;
    static int nextAppointmentId = 1;
    static int totalRevenue = 0;

    static final String PATIENT_FILE = "patients.txt";
    static final String APPOINTMENT_FILE = "appointments.txt";

    public static void main(String[] args) {
        loadSampleDoctors();
        loadData();
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
                case 1:  registerPatient();    break;
                case 2:  displayAllPatients(); break;
                case 3:  searchPatientById();  break;
                case 4:  searchPatientByName(); break;
                case 5:  updatePatient();      break;
                case 6:  deletePatient();      break;
                case 7:  displayAllDoctors();  break;
                case 8:  assignDoctor();       break;
                case 9:  bookAppointment();    break;
                case 10: viewAppointments();   break;
                case 11: admitPatient();       break;
                case 12: dischargePatient();   break;
                case 13: viewSummary();        break;
                case 14: System.out.println("Thank you. Exiting the system."); break;
                default: System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 14);
    }

    static void displayMenu() {
        System.out.println("\n===== HOSPITAL PATIENT MANAGEMENT SYSTEM =====");
        System.out.println("1. Register New Patient");
        System.out.println("2. Display All Patients");
        System.out.println("3. Search Patient by ID");
        System.out.println("4. Search Patient by Name");
        System.out.println("5. Update Patient Details");
        System.out.println("6. Delete Patient Record");
        System.out.println("7. Display All Doctors");
        System.out.println("8. Assign Doctor to Patient");
        System.out.println("9. Book Appointment");
        System.out.println("10. View All Appointments");
        System.out.println("11. Admit Patient");
        System.out.println("12. Discharge Patient and Generate Bill");
        System.out.println("13. View Hospital Summary");
        System.out.println("14. Exit");
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

    // -------------------- File handling --------------------
    // Records are saved in simple text files, one record per line,
    // with the fields separated by the '|' character. The first line
    // of patients.txt stores the counters and the total revenue.

    static void savePatients() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(PATIENT_FILE))) {
            bw.write("#META|" + nextPatientId + "|" + nextRoomNo + "|"
                     + nextAppointmentId + "|" + totalRevenue);
            bw.newLine();
            for (Patient p : patients) {
                bw.write(p.getPatientId() + "|" + p.getName() + "|" + p.getAge()
                        + "|" + p.getGender() + "|" + p.getPhone() + "|" + p.getDisease()
                        + "|" + p.getDoctorName() + "|" + p.isAdmitted()
                        + "|" + p.getRoomType() + "|" + p.getRoomNo());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Warning: could not save patient data.");
        }
    }

    static void saveAppointments() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(APPOINTMENT_FILE))) {
            for (Appointment a : appointments) {
                bw.write(a.getAppointmentId() + "|" + a.getPatientId() + "|"
                        + a.getDoctorName() + "|" + a.getDate() + "|" + a.getTime());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Warning: could not save appointment data.");
        }
    }

    static void saveAll() {
        savePatients();
        saveAppointments();
    }

    static void loadData() {
        // Load patients and counters
        try (BufferedReader br = new BufferedReader(new FileReader(PATIENT_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] f = line.split("\\|");
                if (f[0].equals("#META")) {
                    nextPatientId = Integer.parseInt(f[1]);
                    nextRoomNo = Integer.parseInt(f[2]);
                    nextAppointmentId = Integer.parseInt(f[3]);
                    totalRevenue = Integer.parseInt(f[4]);
                } else {
                    Patient p = new Patient(Integer.parseInt(f[0]), f[1],
                            Integer.parseInt(f[2]), f[3], f[4], f[5]);
                    p.setDoctorName(f[6]);
                    if (Boolean.parseBoolean(f[7])) {
                        p.admit(f[8], Integer.parseInt(f[9]));
                    }
                    patients.add(p);
                }
            }
        } catch (IOException e) {
            // File not found on the first run: start with no records
        } catch (Exception e) {
            System.out.println("Warning: saved patient data could not be read fully.");
        }
        // Load appointments
        try (BufferedReader br = new BufferedReader(new FileReader(APPOINTMENT_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] f = line.split("\\|");
                appointments.add(new Appointment(Integer.parseInt(f[0]),
                        Integer.parseInt(f[1]), f[2], f[3], f[4]));
            }
        } catch (IOException e) {
            // File not found on the first run: no appointments yet
        } catch (Exception e) {
            System.out.println("Warning: saved appointment data could not be read fully.");
        }
    }

    // -------------------- Patient operations --------------------

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
            saveAll();
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

    static void searchPatientById() {
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

    static void searchPatientByName() {
        System.out.print("Enter patient name to search: ");
        String name = scanner.nextLine();
        boolean found = false;
        for (Patient p : patients) {
            if (p.getName().equalsIgnoreCase(name)) {
                p.displayPatient();
                found = true;
            }
        }
        if (!found) {
            System.out.println("No patient found with name " + name + ".");
        }
    }

    static void updatePatient() {
        System.out.print("Enter patient ID to update: ");
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
        System.out.print("Enter new phone number: ");
        String phone = scanner.nextLine();
        System.out.print("Enter new disease / problem: ");
        String disease = scanner.nextLine();
        p.setPhone(phone);
        p.setDisease(disease);
        saveAll();
        System.out.println("Patient details updated successfully.");
    }

    static void deletePatient() {
        System.out.print("Enter patient ID to delete: ");
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
            System.out.println("Cannot delete an admitted patient. Discharge the patient first.");
            return;
        }
        patients.remove(p);
        // Appointments of a deleted patient are also removed
        appointments.removeIf(a -> a.getPatientId() == id);
        saveAll();
        System.out.println("Patient record deleted successfully.");
    }

    // -------------------- Doctor operations --------------------

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
                saveAll();
                System.out.println("Doctor " + d.getName() + " assigned to patient " + p.getName() + ".");
                return;
            }
        }
        System.out.println("Doctor with ID " + did + " not found.");
    }

    // -------------------- Appointment operations --------------------

    static void bookAppointment() {
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
        System.out.print("Enter doctor ID for the appointment: ");
        int did = readInt();
        if (did == -1) {
            System.out.println("Invalid input! Doctor ID must be a number.");
            return;
        }
        Doctor chosen = null;
        for (Doctor d : doctors) {
            if (d.getDoctorId() == did) {
                chosen = d;
            }
        }
        if (chosen == null) {
            System.out.println("Doctor with ID " + did + " not found.");
            return;
        }
        System.out.print("Enter appointment date (e.g. 28-10-2026): ");
        String date = scanner.nextLine();
        System.out.print("Enter appointment time (e.g. 10:30 AM): ");
        String time = scanner.nextLine();
        Appointment a = new Appointment(nextAppointmentId, pid,
                                        chosen.getName(), date, time);
        appointments.add(a);
        System.out.println("Appointment booked successfully. Appointment ID = " + nextAppointmentId);
        nextAppointmentId++;
        saveAll();
    }

    static void viewAppointments() {
        if (appointments.isEmpty()) {
            System.out.println("No appointments booked yet.");
            return;
        }
        System.out.println("\n---------- LIST OF APPOINTMENTS ----------");
        for (Appointment a : appointments) {
            Patient p = findPatient(a.getPatientId());
            String patientName = (p == null) ? "Unknown" : p.getName();
            a.displayAppointment(patientName);
        }
    }

    // -------------------- Admission, discharge, summary --------------------

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
        saveAll();
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
        totalRevenue = totalRevenue + total;
        saveAll();
        System.out.println("Patient discharged successfully.");
    }

    static void viewSummary() {
        int admittedCount = 0;
        for (Patient p : patients) {
            if (p.isAdmitted()) {
                admittedCount++;
            }
        }
        System.out.println("\n---------- HOSPITAL SUMMARY ----------");
        System.out.println("Total Registered Patients : " + patients.size());
        System.out.println("Patients Admitted Now     : " + admittedCount);
        System.out.println("Total Appointments Booked : " + appointments.size());
        System.out.println("Total Revenue Collected   : Rs. " + totalRevenue);
        System.out.println("----------------------------------------");
    }
}
