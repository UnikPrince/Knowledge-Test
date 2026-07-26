package com.interview.simulator.application.utils;

import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for date/time operations.
 */
@Slf4j
public class DateTimeUtil {

    private static final DateTimeFormatter FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    private static final DateTimeFormatter ISO_FORMATTER = 
        DateTimeFormatter.ISO_DATE_TIME;

    /**
     * Format LocalDateTime to string.
     */
    public static String format(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(FORMATTER) : "";
    }

    /**
     * Parse string to LocalDateTime.
     */
    public static LocalDateTime parse(String dateString) {
        try {
            return LocalDateTime.parse(dateString, FORMATTER);
        } catch (Exception e) {
            log.warn("Failed to parse date: {}", dateString);
            return null;
        }
    }

    /**
     * Get elapsed time in seconds.
     */
    public static long getElapsedSeconds(LocalDateTime startTime) {
        if (startTime == null) return 0;
        return java.time.temporal.ChronoUnit.SECONDS.between(startTime, LocalDateTime.now());
    }

    /**
     * Get elapsed time as formatted string.
     */
    public static String getElapsedTimeFormatted(LocalDateTime startTime) {
        long seconds = getElapsedSeconds(startTime);
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }
}
