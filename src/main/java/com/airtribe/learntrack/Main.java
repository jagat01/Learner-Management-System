package com.airtribe.learntrack;

import com.airtribe.learntrack.constants.AppConstants;
import com.airtribe.learntrack.constants.MenuOptions;
import com.airtribe.learntrack.entity.Course;
import com.airtribe.learntrack.entity.Enrollment;
import com.airtribe.learntrack.entity.Student;
import com.airtribe.learntrack.exception.EntityNotFoundException;
import com.airtribe.learntrack.exception.InvalidInputException;
import com.airtribe.learntrack.repository.CourseRepository;
import com.airtribe.learntrack.repository.EnrollmentRepository;
import com.airtribe.learntrack.repository.StudentRepository;
import com.airtribe.learntrack.service.CourseService;
import com.airtribe.learntrack.service.EnrollmentService;
import com.airtribe.learntrack.service.StudentService;

import java.util.List;
import java.util.Scanner;

/**
 * Entry point for the LearnTrack console application.
 *
 * <p><b>Responsibilities of Main:</b>
 * <ul>
 *     <li>Display menus to the user.</li>
 *     <li>Read and parse user input.</li>
 *     <li>Delegate all business logic to the appropriate service class.</li>
 *     <li>Handle and display errors without crashing the program.</li>
 * </ul>
 * </p>
 *
 * <p>No business logic lives here – that is the job of the service layer.</p>
 */
public class Main {

    // -----------------------------------------------------------------------
    // Bootstrap – wire up repositories and services
    // -----------------------------------------------------------------------

    /** Shared scanner for reading console input throughout the application. */
    private static final Scanner scanner = new Scanner(System.in);

    // Repositories (in-memory storage)
    private static final StudentRepository    studentRepository    = new StudentRepository();
    private static final CourseRepository     courseRepository     = new CourseRepository();
    private static final EnrollmentRepository enrollmentRepository = new EnrollmentRepository();

    // Services (business logic + validation)
    private static final StudentService    studentService    = new StudentService(studentRepository);
    private static final CourseService     courseService     = new CourseService(courseRepository);
    private static final EnrollmentService enrollmentService = new EnrollmentService(
            enrollmentRepository, studentService, courseService);

    // -----------------------------------------------------------------------
    // Application entry point
    // -----------------------------------------------------------------------

    /**
     * Launches the LearnTrack console application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        printBanner();

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case MenuOptions.MAIN_STUDENT_MENU:
                    studentMenu();
                    break;
                case MenuOptions.MAIN_COURSE_MENU:
                    courseMenu();
                    break;
                case MenuOptions.MAIN_ENROLLMENT_MENU:
                    enrollmentMenu();
                    break;
                case MenuOptions.MAIN_EXIT:
                    System.out.println("\nThank you for using " + AppConstants.APP_NAME + ". Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("[!] Invalid option. Please choose 0-3.");
            }
        }

        scanner.close();
    }

    // -----------------------------------------------------------------------
    // Main menu
    // -----------------------------------------------------------------------

    /** Displays the top-level main menu. */
    private static void printMainMenu() {
        System.out.println("\n" + AppConstants.SEPARATOR);
        System.out.println("  " + AppConstants.APP_NAME + " v" + AppConstants.APP_VERSION + " — Main Menu");
        System.out.println(AppConstants.SEPARATOR);
        System.out.println("  " + MenuOptions.MAIN_STUDENT_MENU    + ". Student Management");
        System.out.println("  " + MenuOptions.MAIN_COURSE_MENU     + ". Course Management");
        System.out.println("  " + MenuOptions.MAIN_ENROLLMENT_MENU + ". Enrollment Management");
        System.out.println("  " + MenuOptions.MAIN_EXIT            + ". Exit");
        System.out.println(AppConstants.SEPARATOR);
    }

    // -----------------------------------------------------------------------
    // Student sub-menu
    // -----------------------------------------------------------------------

    /** Displays and handles the Student management sub-menu. */
    private static void studentMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n" + AppConstants.THIN_SEPARATOR);
            System.out.println("  Student Management");
            System.out.println(AppConstants.THIN_SEPARATOR);
            System.out.println("  " + MenuOptions.STU_ADD           + ". Add New Student");
            System.out.println("  " + MenuOptions.STU_VIEW_ALL      + ". View All Students");
            System.out.println("  " + MenuOptions.STU_SEARCH_BY_ID  + ". Search Student by ID");
            System.out.println("  " + MenuOptions.STU_DEACTIVATE    + ". Deactivate Student");
            System.out.println("  " + MenuOptions.STU_BACK          + ". Back to Main Menu");
            System.out.println(AppConstants.THIN_SEPARATOR);

            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case MenuOptions.STU_ADD:
                    handleAddStudent();
                    break;
                case MenuOptions.STU_VIEW_ALL:
                    handleViewAllStudents();
                    break;
                case MenuOptions.STU_SEARCH_BY_ID:
                    handleSearchStudentById();
                    break;
                case MenuOptions.STU_DEACTIVATE:
                    handleDeactivateStudent();
                    break;
                case MenuOptions.STU_BACK:
                    inMenu = false;
                    break;
                default:
                    System.out.println("[!] Invalid option. Please choose 0-4.");
            }
        }
    }

    /**
     * Prompts the user for student details and adds a new student.
     * Supports adding with or without an email (demonstrates constructor overloading).
     */
    private static void handleAddStudent() {
        System.out.println("\n-- Add New Student --");
        String firstName = readString("First Name: ");
        String lastName  = readString("Last Name : ");
        String batch     = readString("Batch     : ");

        System.out.print("Email (press Enter to skip): ");
        String email = scanner.nextLine().trim();

        try {
            Student student;
            if (email.isEmpty()) {
                // Uses overloaded method without email
                student = studentService.addStudent(firstName, lastName, batch);
            } else {
                student = studentService.addStudent(firstName, lastName, email, batch);
            }
            System.out.println("[✓] Student added successfully!");
            printStudentDetails(student);
        } catch (InvalidInputException e) {
            System.out.println("[!] " + e.getMessage());
        }
    }

    /** Lists all students in the system. */
    private static void handleViewAllStudents() {
        List<Student> students = studentService.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("[i] No students found.");
            return;
        }
        System.out.println("\n-- All Students (" + students.size() + ") --");
        for (Student s : students) {
            printStudentDetails(s);
        }
    }

    /** Searches for a student by their unique ID. */
    private static void handleSearchStudentById() {
        String id = readString("Enter Student ID: ");
        try {
            Student student = studentService.getStudentById(id);
            printStudentDetails(student);
        } catch (EntityNotFoundException | InvalidInputException e) {
            System.out.println("[!] " + e.getMessage());
        }
    }

    /** Deactivates a student (soft-delete). */
    private static void handleDeactivateStudent() {
        String id = readString("Enter Student ID to deactivate: ");
        try {
            studentService.deactivateStudent(id);
        } catch (EntityNotFoundException | InvalidInputException e) {
            System.out.println("[!] " + e.getMessage());
        }
    }

    // -----------------------------------------------------------------------
    // Course sub-menu
    // -----------------------------------------------------------------------

    /** Displays and handles the Course management sub-menu. */
    private static void courseMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n" + AppConstants.THIN_SEPARATOR);
            System.out.println("  Course Management");
            System.out.println(AppConstants.THIN_SEPARATOR);
            System.out.println("  " + MenuOptions.CRS_ADD        + ". Add New Course");
            System.out.println("  " + MenuOptions.CRS_VIEW_ALL   + ". View All Courses");
            System.out.println("  " + MenuOptions.CRS_ACTIVATE   + ". Activate Course");
            System.out.println("  " + MenuOptions.CRS_DEACTIVATE + ". Deactivate Course");
            System.out.println("  " + MenuOptions.CRS_BACK       + ". Back to Main Menu");
            System.out.println(AppConstants.THIN_SEPARATOR);

            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case MenuOptions.CRS_ADD:
                    handleAddCourse();
                    break;
                case MenuOptions.CRS_VIEW_ALL:
                    handleViewAllCourses();
                    break;
                case MenuOptions.CRS_ACTIVATE:
                    handleSetCourseStatus(true);
                    break;
                case MenuOptions.CRS_DEACTIVATE:
                    handleSetCourseStatus(false);
                    break;
                case MenuOptions.CRS_BACK:
                    inMenu = false;
                    break;
                default:
                    System.out.println("[!] Invalid option. Please choose 0-4.");
            }
        }
    }

    /** Prompts the user for course details and creates a new course. */
    private static void handleAddCourse() {
        System.out.println("\n-- Add New Course --");
        String name        = readString("Course Name  : ");
        String description = readString("Description  : ");
        int    weeks       = readInt("Duration (weeks): ");

        try {
            Course course = courseService.addCourse(name, description, weeks);
            System.out.println("[✓] Course added successfully!");
            printCourseDetails(course);
        } catch (InvalidInputException e) {
            System.out.println("[!] " + e.getMessage());
        }
    }

    /** Lists all courses in the system. */
    private static void handleViewAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        if (courses.isEmpty()) {
            System.out.println("[i] No courses found.");
            return;
        }
        System.out.println("\n-- All Courses (" + courses.size() + ") --");
        for (Course c : courses) {
            printCourseDetails(c);
        }
    }

    /**
     * Activates or deactivates a course based on the {@code activate} flag.
     *
     * @param activate {@code true} to activate, {@code false} to deactivate
     */
    private static void handleSetCourseStatus(boolean activate) {
        String action = activate ? "activate" : "deactivate";
        String id = readString("Enter Course ID to " + action + ": ");
        try {
            if (activate) {
                courseService.activateCourse(id);
            } else {
                courseService.deactivateCourse(id);
            }
        } catch (EntityNotFoundException | InvalidInputException e) {
            System.out.println("[!] " + e.getMessage());
        }
    }

    // -----------------------------------------------------------------------
    // Enrollment sub-menu
    // -----------------------------------------------------------------------

    /** Displays and handles the Enrollment management sub-menu. */
    private static void enrollmentMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n" + AppConstants.THIN_SEPARATOR);
            System.out.println("  Enrollment Management");
            System.out.println(AppConstants.THIN_SEPARATOR);
            System.out.println("  " + MenuOptions.ENR_ENROLL           + ". Enroll Student in Course");
            System.out.println("  " + MenuOptions.ENR_VIEW_BY_STUDENT  + ". View Enrollments by Student");
            System.out.println("  " + MenuOptions.ENR_MARK_COMPLETED   + ". Mark Enrollment as Completed");
            System.out.println("  " + MenuOptions.ENR_MARK_CANCELLED   + ". Cancel an Enrollment");
            System.out.println("  " + MenuOptions.ENR_BACK             + ". Back to Main Menu");
            System.out.println(AppConstants.THIN_SEPARATOR);

            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case MenuOptions.ENR_ENROLL:
                    handleEnrollStudent();
                    break;
                case MenuOptions.ENR_VIEW_BY_STUDENT:
                    handleViewEnrollmentsByStudent();
                    break;
                case MenuOptions.ENR_MARK_COMPLETED:
                    handleMarkEnrollmentCompleted();
                    break;
                case MenuOptions.ENR_MARK_CANCELLED:
                    handleCancelEnrollment();
                    break;
                case MenuOptions.ENR_BACK:
                    inMenu = false;
                    break;
                default:
                    System.out.println("[!] Invalid option. Please choose 0-4.");
            }
        }
    }

    /** Enrolls a student in a course. */
    private static void handleEnrollStudent() {
        String studentId = readString("Enter Student ID : ");
        String courseId  = readString("Enter Course ID  : ");
        try {
            Enrollment enrollment = enrollmentService.enrollStudent(studentId, courseId);
            System.out.println("[✓] Enrollment successful!");
            printEnrollmentDetails(enrollment);
        } catch (EntityNotFoundException | InvalidInputException e) {
            System.out.println("[!] " + e.getMessage());
        }
    }

    /** Displays all enrollments for a given student. */
    private static void handleViewEnrollmentsByStudent() {
        String studentId = readString("Enter Student ID: ");
        try {
            List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudent(studentId);
            if (enrollments.isEmpty()) {
                System.out.println("[i] No enrollments found for student: " + studentId);
                return;
            }
            System.out.println("\n-- Enrollments for " + studentId + " (" + enrollments.size() + ") --");
            for (Enrollment e : enrollments) {
                printEnrollmentDetails(e);
            }
        } catch (EntityNotFoundException | InvalidInputException e) {
            System.out.println("[!] " + e.getMessage());
        }
    }

    /** Marks an enrollment as COMPLETED. */
    private static void handleMarkEnrollmentCompleted() {
        String enrollmentId = readString("Enter Enrollment ID to mark as COMPLETED: ");
        try {
            enrollmentService.markCompleted(enrollmentId);
        } catch (EntityNotFoundException | InvalidInputException e) {
            System.out.println("[!] " + e.getMessage());
        }
    }

    /** Cancels an enrollment. */
    private static void handleCancelEnrollment() {
        String enrollmentId = readString("Enter Enrollment ID to CANCEL: ");
        try {
            enrollmentService.markCancelled(enrollmentId);
        } catch (EntityNotFoundException | InvalidInputException e) {
            System.out.println("[!] " + e.getMessage());
        }
    }

    // -----------------------------------------------------------------------
    // Display helpers
    // -----------------------------------------------------------------------

    /** Prints a formatted summary of a Student. */
    private static void printStudentDetails(Student s) {
        System.out.println("  ID      : " + s.getId());
        System.out.println("  Name    : " + s.getDisplayName());
        System.out.println("  Email   : " + (s.getEmail().isEmpty() ? "(none)" : s.getEmail()));
        System.out.println("  Batch   : " + s.getBatch());
        System.out.println("  Active  : " + (s.isActive() ? "Yes" : "No"));
        System.out.println(AppConstants.THIN_SEPARATOR);
    }

    /** Prints a formatted summary of a Course. */
    private static void printCourseDetails(Course c) {
        System.out.println("  ID      : " + c.getId());
        System.out.println("  Name    : " + c.getCourseName());
        System.out.println("  Desc    : " + c.getDescription());
        System.out.println("  Weeks   : " + c.getDurationInWeeks());
        System.out.println("  Active  : " + (c.isActive() ? "Yes" : "No"));
        System.out.println(AppConstants.THIN_SEPARATOR);
    }

    /** Prints a formatted summary of an Enrollment. */
    private static void printEnrollmentDetails(Enrollment e) {
        System.out.println("  ID        : " + e.getId());
        System.out.println("  Student   : " + e.getStudentId());
        System.out.println("  Course    : " + e.getCourseId());
        System.out.println("  Date      : " + e.getEnrollmentDate());
        System.out.println("  Status    : " + e.getStatus());
        System.out.println(AppConstants.THIN_SEPARATOR);
    }

    // -----------------------------------------------------------------------
    // Input helpers
    // -----------------------------------------------------------------------

    /**
     * Reads a non-empty string from the console.
     *
     * @param prompt the text to display before reading
     * @return the trimmed string entered by the user (loops until non-empty)
     */
    private static String readString(String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("[!] Input cannot be empty. Please try again.");
            }
        } while (input.isEmpty());
        return input;
    }

    /**
     * Reads an integer from the console.
     * Loops and shows an error message if the input is not a valid integer.
     *
     * @param prompt the text to display before reading
     * @return the integer entered by the user
     */
    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String raw = scanner.nextLine().trim();
            try {
                return Integer.parseInt(raw);
            } catch (NumberFormatException e) {
                // Catches non-numeric input gracefully – program does not crash
                System.out.println("[!] \"" + raw + "\" is not a valid number. Please try again.");
            }
        }
    }

    // -----------------------------------------------------------------------
    // Banner
    // -----------------------------------------------------------------------

    /** Prints the startup welcome banner. */
    private static void printBanner() {
        System.out.println(AppConstants.SEPARATOR);
        System.out.println("   Welcome to " + AppConstants.APP_NAME
                + " — Student & Course Management System");
        System.out.println("   Version : " + AppConstants.APP_VERSION);
        System.out.println("   Built with Core Java | In-Memory Storage");
        System.out.println(AppConstants.SEPARATOR);
    }
}
