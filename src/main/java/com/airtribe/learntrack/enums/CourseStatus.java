package com.airtribe.learntrack.enums;

/**
 * Represents the possible status values of a Course.
 * <ul>
 *     <li>{@code ACTIVE}   - The course is currently open and accepting enrollments.</li>
 *     <li>{@code INACTIVE} - The course is paused or no longer accepting enrollments.</li>
 *     <li>{@code ARCHIVED} - The course is permanently closed and only kept for records.</li>
 * </ul>
 */
public enum CourseStatus {
    /** Course is live and accepting students. */
    ACTIVE,

    /** Course is temporarily or permanently closed. */
    INACTIVE,

    /** Course is archived and no longer modifiable. */
    ARCHIVED
}
