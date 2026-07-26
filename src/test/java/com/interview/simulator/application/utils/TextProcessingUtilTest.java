package com.interview.simulator.application.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextProcessingUtilTest {

    @Test
    void testEmailValidation() {
        assertTrue(TextProcessingUtil.isValidEmail("user@example.com"));
        assertTrue(TextProcessingUtil.isValidEmail("john.doe@company.co.uk"));
        assertFalse(TextProcessingUtil.isValidEmail("invalid-email"));
        assertFalse(TextProcessingUtil.isValidEmail(null));
    }

    @Test
    void testUrlValidation() {
        assertTrue(TextProcessingUtil.isValidUrl("https://www.example.com"));
        assertTrue(TextProcessingUtil.isValidUrl("http://example.com"));
        assertFalse(TextProcessingUtil.isValidUrl("not a url"));
    }

    @Test
    void testHtmlSanitization() {
        String input = "<p>Hello <b>World</b></p>";
        String expected = "Hello World";
        assertEquals(expected, TextProcessingUtil.sanitizeHtml(input));
    }

    @Test
    void testTextTruncation() {
        String input = "This is a very long text that needs truncation";
        String result = TextProcessingUtil.truncate(input, 20);
        assertTrue(result.contains("..."));
        assertTrue(result.length() <= 23);
    }

    @Test
    void testSimilarityCalculation() {
        double similarity = TextProcessingUtil.calculateSimilarity("hello", "hello");
        assertEquals(1.0, similarity);

        similarity = TextProcessingUtil.calculateSimilarity("cat", "dog");
        assertTrue(similarity < 1.0);
    }
}
