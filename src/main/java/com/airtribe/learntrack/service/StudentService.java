package com.airtribe.learntrack.service;

import com.airtribe.learntrack.entity.Student;
import com.airtribe.learntrack.exception.EntityNotFoundException;
import com.airtribe.learntrack.exception.InvalidInputException;
import com.airtribe.learntrack.repository.StudentRepository;
import com.airtribe.learntrack.util.IdGenerator;
import com.airtribe.learntrack.util.InputValidator;

import java.util.List;

/**
 * Service layer for all Student-related business operations.
 * <p>
 * This class sits between the console UI ({@code Main}) and the
 * {@link StudentRepository}. It validates inputs, enforces business rules,
 * and delegates persistence to the repository.
 * </p>
 *
 * <p><b>Separation of concerns:</b> the UI must <em>never</em> touch the
 * repository directly – always go through this service.</p>
 */
public class StudentService {

    /** The repository that stores all Student objects in memory. */
    private final StudentRepository studentRepository;

    /**
     * Constructs a StudentService with the given repository.
     *
     * @param studentRepository the in-memory student store (must not be null)
     */
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // -----------------------------------------------------------------------
    // Create
    // -----------------------------------------------------------------------

    /**
     * Adds a new student with an email address.
     *
     * @param firstName first name of the student
     * @param lastName  last name of the student
     * @param email     email address (must be a valid email format)
     * @param batch     batch / cohort identifier (e.g. "Batch-2024")
     * @return the newly created {@link Student}
     * @throws InvalidInputException if any required field is blank or email is invalid
     */
    public Student addStudent(String firstName, String lastName, String email, String batch) {
        // Validate all required fields
        validateName(firstName, "First name");
        validateName(lastName, "Last name");
        validateBatch(batch);

        if (!InputValidator.isValidEmail(email)) {
            throw new InvalidInputException("Invalid email address: " + email);
        }

        // Generate a unique ID via the static IdGenerator utility
        String id = IdGenerator.generateStudentId();

        // Create and persist the student
        Student student = new Student(id, firstName, lastName, email, batch, true);
        studentRepository.save(student);
        return student;
    }

    /**
     * Adds a new student <em>without</em> an email address (constructor overloading demo).
     *
     * @param firstName first name of the student
     * @param lastName  last name of the student
     * @param batch     batch / cohort identifier
     * @return the newly created {@link Student}
     * @throws InvalidInputException if any required field is blank
     */
    public Student addStudent(String firstName, String lastName, String batch) {
        validateName(firstName, "First name");
        validateName(lastName, "Last name");
        validateBatch(batch);

        String id = IdGenerator.generateStudentId();
        // Uses the Student constructor that omits email
        Student student = new Student(id, firstName, lastName, batch, true);
        studentRepository.save(student);
        return student;
    }

    // -----------------------------------------------------------------------
    // Read
    // -----------------------------------------------------------------------

    /**
     * Returns all students currently stored in the system.
     *
     * @return a list of all students (may be empty, never null)
     */
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    /**
     * Finds a single student by their unique ID.
     *
     * @param studentId the ID to look up (e.g. "STUDENT-1")
     * @return the matching {@link Student}
     * @throws EntityNotFoundException if no student with that ID exists
     * @throws InvalidInputException   if the provided ID is blank
     */
    public Student getStudentById(String studentId) {
        if (!InputValidator.isNotNullOrEmpty(studentId)) {
            throw new InvalidInputException("Student ID must not be empty");
        }
        // Throws EntityNotFoundException internally if not found
        return studentRepository.findById(studentId);
    }

    // -----------------------------------------------------------------------
    // Update
    // -----------------------------------------------------------------------

    /**
     * Deactivates a student (soft-delete – sets {@code active = false}).
     * <p>
     * The student record is retained in memory so historical data (e.g.
     * enrollments) stays intact.
     * </p>
     *
     * @param studentId the ID of the student to deactivate
     * @throws EntityNotFoundException if the student does not exist
     * @throws InvalidInputException   if the provided ID is blank
     */
    public void deactivateStudent(String studentId) {
        // getStudentById already validates the ID and throws if not found
        Student student = getStudentById(studentId);
        student.setActive(false);
        studentRepository.save(student); // persist the change
        System.out.println("Student " + student.getDisplayName() + " has been deactivated.");
    }

    // -----------------------------------------------------------------------
    // Private helpers
    // -----------------------------------------------------------------------

    /**
     * Validates that a name field is not null or blank.
     *
     * @param name      the value to check
     * @param fieldName the human-readable field label used in the error message
     */
    private void validateName(String name, String fieldName) {
        if (!InputValidator.isNotNullOrEmpty(name)) {
            throw new InvalidInputException(fieldName + " must not be empty");
        }
    }

    /**
     * Validates that the batch identifier is not null or blank.
     *
     * @param batch the batch string to validate
     */
    private void validateBatch(String batch) {
        if (!InputValidator.isNotNullOrEmpty(batch)) {
            throw new InvalidInputException("Batch must not be empty");
        }
    }
}
