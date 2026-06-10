package com.airtribe.learntrack.exception;

/**
 * Exception thrown when an operation receives invalid or malformed input data.
 * <p>
 * Use this exception in service or validation layers when the input does not
 * meet the expected constraints (e.g., empty name, invalid email format, negative weeks).
 * </p>
 */
public class InvalidInputException extends RuntimeException {

    /**
     * Constructs a new InvalidInputException with the specified detail message.
     *
     * @param message the detail message describing what was invalid
     */
    public InvalidInputException(String message) {
        super(message);
    }
}
