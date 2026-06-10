package com.airtribe.learntrack.util;

import java.util.regex.Pattern;

/**
 * Static utility class for validating user input data.
 */
public final class InputValidator {

    // RFC 5322 compliant regex pattern for standard email validation
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    // Private constructor to prevent instantiation
    private InputValidator() {}

    /**
     * Validates if a string matches standard email formatting.
     */
    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validates if a string is non-null and not empty/blank.
     */
    public static boolean isNotNullOrEmpty(String input) {
        return input != null && !input.trim().isEmpty();
    }

    /**
     * Validates if a value is non-negative (>= 0).
     */
    public static boolean isNonNegative(int value) {
        return value >= 0;
    }

    /**
     * Validates if a value is positive (> 0).
     */
    public static boolean isPositive(int value) {
        return value > 0;
    }

    /**
     * Validates if an input string is a valid active flag ("true" or "false").
     */
    public static boolean isValidActiveFlag(String activeStr) {
        return "true".equalsIgnoreCase(activeStr) || "false".equalsIgnoreCase(activeStr);
    }
}
