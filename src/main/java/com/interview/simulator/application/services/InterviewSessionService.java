package com.interview.simulator.application.services;

import com.interview.simulator.domain.entities.InterviewExchange;
import com.interview.simulator.domain.entities.InterviewSession;
import com.interview.simulator.infrastructure.persistence.InterviewExchangeRepository;
import com.interview.simulator.infrastructure.persistence.InterviewSessionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for managing interview sessions.
 * CRUD operations and session queries.
 */
@Service
@Slf4j
public class InterviewSessionService {

    @Autowired
    private InterviewSessionRepository sessionRepository;

    @Autowired
    private InterviewExchangeRepository exchangeRepository;

    /**
     * Create a new interview session.
     */
    @Transactional
    public InterviewSession createSession(String sessionId, String studentName, String topic, int maxQuestions) {
        log.info("Creating interview session for: {}", studentName);
        
        InterviewSession session = InterviewSession.builder()
                .sessionId(sessionId)
                .studentName(studentName)
                .interviewTopic(topic)
                .totalQuestions(maxQuestions)
                .status(InterviewSession.InterviewStatus.PENDING)
                .build();
        
        return sessionRepository.save(session);
    }

    /**
     * Get session by session ID.
     */
    public InterviewSession getSessionBySessionId(String sessionId) {
        return sessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found: " + sessionId));
    }

    /**
     * Update interview session.
     */
    @Transactional
    public InterviewSession updateSession(InterviewSession session) {
        return sessionRepository.save(session);
    }

    /**
     * Save interview exchange (Q&A pair).
     */
    @Transactional
    public InterviewExchange saveExchange(Long sessionId, InterviewExchange exchange) {
        return exchangeRepository.save(exchange);
    }

    /**
     * Get all exchanges for a session.
     */
    public List<InterviewExchange> getSessionExchanges(Long sessionId) {
        return exchangeRepository.findBySessionIdOrderByExchangeIndexAsc(sessionId);
    }

    /**
     * End session and update status.
     */
    @Transactional
    public void endSession(String sessionId) {
        InterviewSession session = getSessionBySessionId(sessionId);
        session.setStatus(InterviewSession.InterviewStatus.COMPLETED);
        session.setEndedAt(LocalDateTime.now());
        sessionRepository.save(session);
        log.info("Session ended: {}", sessionId);
    }
}
