package com.airtribe.learntrack.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Static utility class for generating unique entity IDs.
 */
public final class IdGenerator {

    private static final AtomicInteger studentCounter = new AtomicInteger(1);
    private static final AtomicInteger courseCounter = new AtomicInteger(1);
    private static final AtomicInteger enrollmentCounter = new AtomicInteger(1);

    // Private constructor to prevent instantiation
    private IdGenerator() {}

    /**
     * Generates a unique student ID (e.g., "STUDENT-1").
     */
    public static String generateStudentId() {
        return "STUDENT-" + studentCounter.getAndIncrement();
    }

    /**
     * Generates a unique course ID (e.g., "COURSE-1").
     */
    public static String generateCourseId() {
        return "COURSE-" + courseCounter.getAndIncrement();
    }

    /**
     * Generates a unique enrollment ID (e.g., "ENROLL-1").
     */
    public static String generateEnrollmentId() {
        return "ENROLL-" + enrollmentCounter.getAndIncrement();
    }
}
