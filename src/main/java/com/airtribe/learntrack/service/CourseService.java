package com.airtribe.learntrack.service;

import com.airtribe.learntrack.constants.AppConstants;
import com.airtribe.learntrack.entity.Course;
import com.airtribe.learntrack.exception.EntityNotFoundException;
import com.airtribe.learntrack.exception.InvalidInputException;
import com.airtribe.learntrack.repository.CourseRepository;
import com.airtribe.learntrack.util.IdGenerator;
import com.airtribe.learntrack.util.InputValidator;

import java.util.List;

/**
 * Service layer for all Course-related business operations.
 * <p>
 * Validates inputs, enforces constraints (e.g. valid week range), and
 * delegates storage to {@link CourseRepository}.
 * </p>
 */
public class CourseService {

    /** The repository that holds all Course objects in memory. */
    private final CourseRepository courseRepository;

    /**
     * Constructs a CourseService with the provided repository.
     *
     * @param courseRepository the in-memory course store (must not be null)
     */
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    // -----------------------------------------------------------------------
    // Create
    // -----------------------------------------------------------------------

    /**
     * Adds a brand-new course to the system.
     *
     * @param courseName      display name of the course (must not be blank)
     * @param description     short description of what the course covers
     * @param durationInWeeks length of the course in weeks
     * @return the newly created and persisted {@link Course}
     * @throws InvalidInputException if any field fails validation
     */
    public Course addCourse(String courseName, String description, int durationInWeeks) {
        // Field-level validation
        if (!InputValidator.isNotNullOrEmpty(courseName)) {
            throw new InvalidInputException("Course name must not be empty");
        }
        if (!InputValidator.isNotNullOrEmpty(description)) {
            throw new InvalidInputException("Course description must not be empty");
        }
        if (durationInWeeks < AppConstants.MIN_COURSE_WEEKS
                || durationInWeeks > AppConstants.MAX_COURSE_WEEKS) {
            throw new InvalidInputException(
                    "Duration must be between " + AppConstants.MIN_COURSE_WEEKS
                    + " and " + AppConstants.MAX_COURSE_WEEKS + " weeks");
        }

        // Generate unique ID using the static IdGenerator utility
        String id = IdGenerator.generateCourseId();

        // New courses start in an active state
        Course course = new Course(id, courseName, description, durationInWeeks, true);
        courseRepository.save(course);
        return course;
    }

    // -----------------------------------------------------------------------
    // Read
    // -----------------------------------------------------------------------

    /**
     * Returns all courses currently stored in the system.
     *
     * @return a list of all courses (may be empty, never null)
     */
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    /**
     * Retrieves a course by its unique ID.
     *
     * @param courseId the ID to look up (e.g. "COURSE-1")
     * @return the matching {@link Course}
     * @throws EntityNotFoundException if no course with that ID exists
     * @throws InvalidInputException   if the provided ID is blank
     */
    public Course getCourseById(String courseId) {
        if (!InputValidator.isNotNullOrEmpty(courseId)) {
            throw new InvalidInputException("Course ID must not be empty");
        }
        return courseRepository.findById(courseId);
    }

    // -----------------------------------------------------------------------
    // Update
    // -----------------------------------------------------------------------

    /**
     * Activates a course, making it available for enrollments.
     *
     * @param courseId the ID of the course to activate
     * @throws EntityNotFoundException if the course does not exist
     * @throws InvalidInputException   if the provided ID is blank
     */
    public void activateCourse(String courseId) {
        Course course = getCourseById(courseId);
        course.setActive(true);
        courseRepository.save(course);
        System.out.println("Course \"" + course.getCourseName() + "\" is now ACTIVE.");
    }

    /**
     * Deactivates a course so no new enrollments can be made against it.
     *
     * @param courseId the ID of the course to deactivate
     * @throws EntityNotFoundException if the course does not exist
     * @throws InvalidInputException   if the provided ID is blank
     */
    public void deactivateCourse(String courseId) {
        Course course = getCourseById(courseId);
        course.setActive(false);
        courseRepository.save(course);
        System.out.println("Course \"" + course.getCourseName() + "\" is now INACTIVE.");
    }
}
