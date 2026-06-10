package com.airtribe.learntrack.service;

import com.airtribe.learntrack.entity.Course;
import com.airtribe.learntrack.entity.Enrollment;
import com.airtribe.learntrack.entity.Student;
import com.airtribe.learntrack.enums.EnrollmentStatus;
import com.airtribe.learntrack.exception.EntityNotFoundException;
import com.airtribe.learntrack.exception.InvalidInputException;
import com.airtribe.learntrack.repository.EnrollmentRepository;
import com.airtribe.learntrack.util.IdGenerator;
import com.airtribe.learntrack.util.InputValidator;

import java.time.LocalDate;
import java.util.List;

/**
 * Service layer for all Enrollment-related business operations.
 * <p>
 * Validates that both the student and the course exist before creating an
 * enrollment, then delegates persistence to {@link EnrollmentRepository}.
 * Status transitions (e.g. ENROLLED → COMPLETED) are enforced here.
 * </p>
 */
public class EnrollmentService {

    /** Repository that stores all Enrollment objects in memory. */
    private final EnrollmentRepository enrollmentRepository;

    /** Used to validate that the student being enrolled actually exists. */
    private final StudentService studentService;

    /** Used to validate that the course being enrolled into actually exists and is active. */
    private final CourseService courseService;

    /**
     * Constructs an EnrollmentService with the required dependencies.
     *
     * @param enrollmentRepository the in-memory enrollment store
     * @param studentService       the student service for student validation
     * @param courseService        the course service for course validation
     */
    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                             StudentService studentService,
                             CourseService courseService) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentService = studentService;
        this.courseService = courseService;
    }

    // -----------------------------------------------------------------------
    // Create
    // -----------------------------------------------------------------------

    /**
     * Enrolls a student into a course.
     * <p>
     * Business rules enforced:
     * <ul>
     *     <li>The student must exist and be active.</li>
     *     <li>The course must exist and be active.</li>
     *     <li>The student must not already be enrolled in the same course with ENROLLED status.</li>
     * </ul>
     * </p>
     *
     * @param studentId the ID of the student to enroll
     * @param courseId  the ID of the course to enroll them in
     * @return the newly created {@link Enrollment}
     * @throws EntityNotFoundException if the student or course is not found
     * @throws InvalidInputException   if IDs are blank, student is inactive, or course is inactive
     */
    public Enrollment enrollStudent(String studentId, String courseId) {
        // Validate IDs
        if (!InputValidator.isNotNullOrEmpty(studentId)) {
            throw new InvalidInputException("Student ID must not be empty");
        }
        if (!InputValidator.isNotNullOrEmpty(courseId)) {
            throw new InvalidInputException("Course ID must not be empty");
        }

        // Ensure the student exists and is active
        Student student = studentService.getStudentById(studentId);
        if (!student.isActive()) {
            throw new InvalidInputException(
                    "Cannot enroll inactive student: " + student.getDisplayName());
        }

        // Ensure the course exists and is active
        Course course = courseService.getCourseById(courseId);
        if (!course.isActive()) {
            throw new InvalidInputException(
                    "Cannot enroll into inactive course: " + course.getCourseName());
        }

        // Prevent duplicate active enrollments
        List<Enrollment> existing = enrollmentRepository.findByStudentId(studentId);
        for (Enrollment e : existing) {
            if (e.getCourseId().equals(courseId)
                    && e.getStatus() == EnrollmentStatus.ENROLLED) {
                throw new InvalidInputException(
                        "Student is already enrolled in course: " + course.getCourseName());
            }
        }

        // Generate ID and create enrollment with today's date
        String id = IdGenerator.generateEnrollmentId();
        Enrollment enrollment = new Enrollment(id, studentId, courseId, LocalDate.now());
        enrollmentRepository.save(enrollment);
        return enrollment;
    }

    // -----------------------------------------------------------------------
    // Read
    // -----------------------------------------------------------------------

    /**
     * Returns all enrollments for a specific student.
     *
     * @param studentId the student whose enrollments to retrieve
     * @return a list of enrollments (may be empty, never null)
     * @throws InvalidInputException if the ID is blank
     */
    public List<Enrollment> getEnrollmentsByStudent(String studentId) {
        if (!InputValidator.isNotNullOrEmpty(studentId)) {
            throw new InvalidInputException("Student ID must not be empty");
        }
        // Confirm the student actually exists before querying
        studentService.getStudentById(studentId);
        return enrollmentRepository.findByStudentId(studentId);
    }

    // -----------------------------------------------------------------------
    // Update – status transitions
    // -----------------------------------------------------------------------

    /**
     * Marks an existing enrollment as {@link EnrollmentStatus#COMPLETED}.
     *
     * @param enrollmentId the ID of the enrollment to update
     * @throws EntityNotFoundException if the enrollment does not exist
     * @throws InvalidInputException   if the ID is blank or the enrollment is not currently ENROLLED
     */
    public void markCompleted(String enrollmentId) {
        Enrollment enrollment = getEnrollmentById(enrollmentId);
        if (enrollment.getStatus() != EnrollmentStatus.ENROLLED) {
            throw new InvalidInputException(
                    "Only ENROLLED enrollments can be marked COMPLETED. Current status: "
                    + enrollment.getStatus());
        }
        enrollment.setStatus(EnrollmentStatus.COMPLETED);
        enrollmentRepository.save(enrollment);
        System.out.println("Enrollment " + enrollmentId + " marked as COMPLETED.");
    }

    /**
     * Marks an existing enrollment as {@link EnrollmentStatus#CANCELLED}.
     *
     * @param enrollmentId the ID of the enrollment to cancel
     * @throws EntityNotFoundException if the enrollment does not exist
     * @throws InvalidInputException   if the ID is blank or the enrollment is already CANCELLED
     */
    public void markCancelled(String enrollmentId) {
        Enrollment enrollment = getEnrollmentById(enrollmentId);
        if (enrollment.getStatus() == EnrollmentStatus.CANCELLED) {
            throw new InvalidInputException("Enrollment is already CANCELLED.");
        }
        enrollment.setStatus(EnrollmentStatus.CANCELLED);
        enrollmentRepository.save(enrollment);
        System.out.println("Enrollment " + enrollmentId + " has been CANCELLED.");
    }

    // -----------------------------------------------------------------------
    // Private helpers
    // -----------------------------------------------------------------------

    /**
     * Retrieves an enrollment by ID, throwing if it does not exist.
     *
     * @param enrollmentId the enrollment ID to look up
     * @return the matching {@link Enrollment}
     * @throws InvalidInputException   if the ID is blank
     * @throws EntityNotFoundException if the enrollment is not found
     */
    private Enrollment getEnrollmentById(String enrollmentId) {
        if (!InputValidator.isNotNullOrEmpty(enrollmentId)) {
            throw new InvalidInputException("Enrollment ID must not be empty");
        }
        return enrollmentRepository.findById(enrollmentId);
    }
}
