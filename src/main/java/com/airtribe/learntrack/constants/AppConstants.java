package com.airtribe.learntrack.constants;

/**
 * Application-wide constants used throughout LearnTrack.
 * <p>
 * Centralising these values makes it easy to change them in one place
 * without hunting through the whole codebase.
 * </p>
 */
public final class AppConstants {

    // -----------------------------------------------------------------------
    // Application meta
    // -----------------------------------------------------------------------

    /** Display name shown in the welcome banner. */
    public static final String APP_NAME = "LearnTrack";

    /** Current version of the application. */
    public static final String APP_VERSION = "1.0.0";

    // -----------------------------------------------------------------------
    // ID prefixes – kept in sync with IdGenerator
    // -----------------------------------------------------------------------

    /** Prefix prepended to every auto-generated student ID. */
    public static final String STUDENT_ID_PREFIX = "STUDENT-";

    /** Prefix prepended to every auto-generated course ID. */
    public static final String COURSE_ID_PREFIX = "COURSE-";

    /** Prefix prepended to every auto-generated enrollment ID. */
    public static final String ENROLLMENT_ID_PREFIX = "ENROLL-";

    // -----------------------------------------------------------------------
    // UI / formatting
    // -----------------------------------------------------------------------

    /** Separator line used in the console menu. */
    public static final String SEPARATOR =
            "============================================================";

    /** Short separator used for sub-sections. */
    public static final String THIN_SEPARATOR =
            "------------------------------------------------------------";

    // -----------------------------------------------------------------------
    // Validation limits
    // -----------------------------------------------------------------------

    /** Minimum allowed course duration in weeks. */
    public static final int MIN_COURSE_WEEKS = 1;

    /** Maximum allowed course duration in weeks. */
    public static final int MAX_COURSE_WEEKS = 104; // 2 years

    // -----------------------------------------------------------------------
    // Constructor guard
    // -----------------------------------------------------------------------

    /** Prevent instantiation of this utility class. */
    private AppConstants() {
        throw new UnsupportedOperationException("AppConstants is a utility class");
    }
}
