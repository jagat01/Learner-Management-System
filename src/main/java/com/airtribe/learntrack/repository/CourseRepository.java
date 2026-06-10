package com.airtribe.learntrack.repository;

import com.airtribe.learntrack.entity.Course;
import com.airtribe.learntrack.exception.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository layer for managing Course entities in memory.
 */
public class CourseRepository {

    private final List<Course> courses = new ArrayList<>();

    /**
     * Stores a new course or updates an existing one if the ID matches.
     */
    public void save(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }
        if (course.getId() == null) {
            throw new IllegalArgumentException("Course ID cannot be null");
        }

        for (int i = 0; i < courses.size(); i++) {
            if (course.getId().equals(courses.get(i).getId())) {
                courses.set(i, course);
                return;
            }
        }
        courses.add(course);
    }

    /**
     * Finds a course by ID. Throws EntityNotFoundException if not found.
     */
    public Course findById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Course ID cannot be null or empty");
        }

        for (Course course : courses) {
            if (id.equals(course.getId())) {
                return course;
            }
        }
        throw new EntityNotFoundException("Course with ID " + id + " not found");
    }

    /**
     * Returns a copy of all stored courses.
     */
    public List<Course> findAll() {
        return new ArrayList<>(courses);
    }
}
