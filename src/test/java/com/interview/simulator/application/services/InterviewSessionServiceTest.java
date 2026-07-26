package com.interview.simulator.application.services;

import com.interview.simulator.domain.entities.InterviewSession;
import com.interview.simulator.infrastructure.persistence.InterviewSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class InterviewSessionServiceTest {

    private InterviewSessionService service;

    @Mock
    private InterviewSessionRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new InterviewSessionService();
    }

    @Test
    void testCreateSession() {
        // Given
        String sessionId = "test-session-123";
        String studentName = "John Doe";
        String topic = "Spring Boot";
        int maxQuestions = 10;

        InterviewSession mockSession = InterviewSession.builder()
                .sessionId(sessionId)
                .studentName(studentName)
                .interviewTopic(topic)
                .totalQuestions(maxQuestions)
                .status(InterviewSession.InterviewStatus.PENDING)
                .build();

        when(repository.save(any(InterviewSession.class))).thenReturn(mockSession);

        // When
        InterviewSession result = service.createSession(sessionId, studentName, topic, maxQuestions);

        // Then
        assertNotNull(result);
        assertEquals(sessionId, result.getSessionId());
        assertEquals(studentName, result.getStudentName());
        assertEquals(topic, result.getInterviewTopic());
    }

    @Test
    void testCreateSessionWithValidData() {
        assertDoesNotThrow(() -> {
            service.createSession("session-1", "Alice", "Java", 5);
        });
    }
}
