package com.interview.simulator.infrastructure.pdf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Service for filtering technical content from PDF text.
 * Identifies and filters content related to Java, Spring, DevOps, ML, and Cybersecurity.
 */
@Service
@Slf4j
public class ContentFilter {

    private static final Set<String> TECHNICAL_KEYWORDS = Set.of(
        "java", "spring", "spring boot", "hibernate", "jpa",
        "maven", "gradle", "devops", "docker", "kubernetes",
        "microservices", "rest", "api", "sql", "nosql",
        "postgresql", "mongodb", "redis", "machine learning",
        "neural network", "tensorflow", "cybersecurity", "encryption",
        "authentication", "authorization", "oauth", "jwt",
        "design patterns", "solid principles", "refactoring"
    );

    private static final Set<String> STOP_KEYWORDS = Set.of(
        "advertisement", "copyright", "disclaimer", "terms and conditions"
    );

    /**
     * Filter text to identify technical content.
     *
     * @param text raw text to filter
     * @return filtered technical content
     */
    public String filterTechnicalContent(String text) {
        log.debug("Filtering technical content from text (length: {})", text.length());
        
        String[] lines = text.split("\n");
        StringBuilder filtered = new StringBuilder();
        
        for (String line : lines) {
            if (isTechnicalLine(line) && !isStopContent(line)) {
                filtered.append(line).append("\n");
            }
        }
        
        String result = filtered.toString().trim();
        log.info("Filtered technical content (original: {}, filtered: {})", text.length(), result.length());
        return result;
    }

    /**
     * Check if a line contains technical content.
     */
    private boolean isTechnicalLine(String line) {
        String lowerLine = line.toLowerCase();
        return TECHNICAL_KEYWORDS.stream().anyMatch(lowerLine::contains);
    }

    /**
     * Check if a line should be filtered out.
     */
    private boolean isStopContent(String line) {
        String lowerLine = line.toLowerCase();
        return STOP_KEYWORDS.stream().anyMatch(lowerLine::contains) || line.trim().isEmpty();
    }
}
