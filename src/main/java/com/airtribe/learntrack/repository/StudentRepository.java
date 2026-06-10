package com.airtribe.learntrack.repository;

import com.airtribe.learntrack.entity.Student;
import com.airtribe.learntrack.exception.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository layer for managing Student entities in memory.
 */
public class StudentRepository {

    private final List<Student> students = new ArrayList<>();

    /**
     * Stores a new student or updates an existing one if the ID matches.
     */
    public void save(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        if (student.getId() == null) {
            throw new IllegalArgumentException("Student ID cannot be null");
        }

        // Check if student already exists and update in-place
        for (int i = 0; i < students.size(); i++) {
            if (student.getId().equals(students.get(i).getId())) {
                students.set(i, student);
                return;
            }
        }
        students.add(student);
    }

    /**
     * Finds a student by ID. Throws EntityNotFoundException if not found.
     */
    public Student findById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID cannot be null or empty");
        }

        for (Student student : students) {
            if (id.equals(student.getId())) {
                return student;
            }
        }
        throw new EntityNotFoundException("Student with ID " + id + " not found");
    }

    /**
     * Returns a copy of all stored students.
     */
    public List<Student> findAll() {
        return new ArrayList<>(students);
    }
}
