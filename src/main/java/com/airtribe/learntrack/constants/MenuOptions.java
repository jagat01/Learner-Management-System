package com.airtribe.learntrack.constants;

/**
 * Numeric menu-option constants used by the console UI (Main.java).
 * <p>
 * Grouping all menu choices here makes the switch statements in Main.java
 * self-documenting and easy to extend without touching business logic.
 * </p>
 */
public final class MenuOptions {

    // -----------------------------------------------------------------------
    // Main menu
    // -----------------------------------------------------------------------

    /** Navigate to the Student Management sub-menu. */
    public static final int MAIN_STUDENT_MENU = 1;

    /** Navigate to the Course Management sub-menu. */
    public static final int MAIN_COURSE_MENU = 2;

    /** Navigate to the Enrollment Management sub-menu. */
    public static final int MAIN_ENROLLMENT_MENU = 3;

    /** Exit the application. */
    public static final int MAIN_EXIT = 0;

    // -----------------------------------------------------------------------
    // Student sub-menu
    // -----------------------------------------------------------------------

    /** Add a new student. */
    public static final int STU_ADD = 1;

    /** View / list all students. */
    public static final int STU_VIEW_ALL = 2;

    /** Search for a student by their ID. */
    public static final int STU_SEARCH_BY_ID = 3;

    /** Deactivate a student (soft-delete). */
    public static final int STU_DEACTIVATE = 4;

    /** Return to the main menu from the student sub-menu. */
    public static final int STU_BACK = 0;

    // -----------------------------------------------------------------------
    // Course sub-menu
    // -----------------------------------------------------------------------

    /** Add a new course. */
    public static final int CRS_ADD = 1;

    /** View / list all courses. */
    public static final int CRS_VIEW_ALL = 2;

    /** Activate a course. */
    public static final int CRS_ACTIVATE = 3;

    /** Deactivate a course. */
    public static final int CRS_DEACTIVATE = 4;

    /** Return to the main menu from the course sub-menu. */
    public static final int CRS_BACK = 0;

    // -----------------------------------------------------------------------
    // Enrollment sub-menu
    // -----------------------------------------------------------------------

    /** Enroll a student in a course. */
    public static final int ENR_ENROLL = 1;

    /** View all enrollments for a student. */
    public static final int ENR_VIEW_BY_STUDENT = 2;

    /** Mark an enrollment as COMPLETED. */
    public static final int ENR_MARK_COMPLETED = 3;

    /** Mark an enrollment as CANCELLED. */
    public static final int ENR_MARK_CANCELLED = 4;

    /** Return to the main menu from the enrollment sub-menu. */
    public static final int ENR_BACK = 0;

    // -----------------------------------------------------------------------
    // Constructor guard
    // -----------------------------------------------------------------------

    /** Prevent instantiation of this constants class. */
    private MenuOptions() {
        throw new UnsupportedOperationException("MenuOptions is a constants class");
    }
}
