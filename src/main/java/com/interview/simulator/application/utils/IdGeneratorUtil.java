package com.interview.simulator.application.utils;

import lombok.extern.slf4j.Slf4j;
import java.util.UUID;

/**
 * Utility class for generating unique identifiers.
 */
@Slf4j
public class IdGeneratorUtil {

    /**
     * Generate UUID string.
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * Generate compact UUID (without hyphens).
     */
    public static String generateCompactUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * Generate session ID with prefix.
     */
    public static String generateSessionId(String prefix) {
        return prefix + "_" + System.currentTimeMillis() + "_" + 
               (int)(Math.random() * 1000);
    }
}
