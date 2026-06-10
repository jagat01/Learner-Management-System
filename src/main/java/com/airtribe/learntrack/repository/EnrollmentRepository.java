package com.airtribe.learntrack.repository;

import com.airtribe.learntrack.entity.Enrollment;
import com.airtribe.learntrack.exception.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository layer for managing Enrollment entities in memory.
 */
public class EnrollmentRepository {

    private final List<Enrollment> enrollments = new ArrayList<>();

    /**
     * Stores a new enrollment or updates an existing one if the ID matches.
     */
    public void save(Enrollment enrollment) {
        if (enrollment == null) {
            throw new IllegalArgumentException("Enrollment cannot be null");
        }
        if (enrollment.getId() == null) {
            throw new IllegalArgumentException("Enrollment ID cannot be null");
        }

        for (int i = 0; i < enrollments.size(); i++) {
            if (enrollment.getId().equals(enrollments.get(i).getId())) {
                enrollments.set(i, enrollment);
                return;
            }
        }
        enrollments.add(enrollment);
    }

    /**
     * Finds an enrollment by ID. Throws EntityNotFoundException if not found.
     */
    public Enrollment findById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Enrollment ID cannot be null or empty");
        }

        for (Enrollment enrollment : enrollments) {
            if (id.equals(enrollment.getId())) {
                return enrollment;
            }
        }
        throw new EntityNotFoundException("Enrollment with ID " + id + " not found");
    }

    /**
     * Returns a copy of all stored enrollments.
     */
    public List<Enrollment> findAll() {
        return new ArrayList<>(enrollments);
    }

    /**
     * Returns a list of all enrollments for a specific student ID.
     */
    public List<Enrollment> findByStudentId(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID cannot be null or empty");
        }

        List<Enrollment> result = new ArrayList<>();
        for (Enrollment enrollment : enrollments) {
            if (studentId.equals(enrollment.getStudentId())) {
                result.add(enrollment);
            }
        }
        return result;
    }
}
