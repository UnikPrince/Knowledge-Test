package com.interview.simulator.application.utils;

import lombok.extern.slf4j.Slf4j;
import java.util.regex.Pattern;

/**
 * Utility class for text processing and validation.
 */
@Slf4j
public class TextProcessingUtil {

    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    
    private static final Pattern URL_PATTERN = 
        Pattern.compile("https?://(?:www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b(?:[-a-zA-Z0-9()@:%_\\+.~#?&/=]*)");

    /**
     * Validate email format.
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validate URL format.
     */
    public static boolean isValidUrl(String url) {
        return url != null && URL_PATTERN.matcher(url).matches();
    }

    /**
     * Sanitize text by removing HTML tags.
     */
    public static String sanitizeHtml(String text) {
        if (text == null) return "";
        return text.replaceAll("<[^>]*>", "");
    }

    /**
     * Truncate text to specified length.
     */
    public static String truncate(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength) + "...";
    }

    /**
     * Calculate text similarity score (Levenshtein distance).
     */
    public static double calculateSimilarity(String text1, String text2) {
        if (text1 == null || text2 == null) return 0.0;
        
        int maxLen = Math.max(text1.length(), text2.length());
        if (maxLen == 0) return 1.0;
        
        return 1.0 - (double) levenshteinDistance(text1, text2) / maxLen;
    }

    /**
     * Calculate Levenshtein distance between two strings.
     */
    private static int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];
        
        for (int i = 0; i <= s1.length(); i++) {
            dp[i][0] = i;
        }
        
        for (int j = 0; j <= s2.length(); j++) {
            dp[0][j] = j;
        }
        
        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                int cost = s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1;
                dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), 
                                     dp[i - 1][j - 1] + cost);
            }
        }
        
        return dp[s1.length()][s2.length()];
    }
}
