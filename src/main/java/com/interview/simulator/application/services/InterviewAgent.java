package com.interview.simulator.application.services;

import com.google.ai.client.generativeai.GenerativeModel;
import com.interview.simulator.domain.entities.InterviewExchange;
import com.interview.simulator.domain.entities.InterviewSession;
import com.interview.simulator.infrastructure.ai.PromptEngineer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

/**
 * Main interview agent orchestrating the interview flow.
 * Manages question generation, answer evaluation, and session progression.
 */
@Service
@Slf4j
public class InterviewAgent {

    @Autowired
    private GenerativeModel generativeModel;

    @Autowired
    private PromptEngineer promptEngineer;

    @Autowired
    private QuestionGenerator questionGenerator;

    @Autowired
    private ResponseEvaluator responseEvaluator;

    @Autowired
    private ConversationManager conversationManager;

    @Autowired
    private InterviewSessionService interviewSessionService;

    /**
     * Start a new interview session.
     *
     * @param studentName name of the student
     * @param topic interview topic
     * @param maxQuestions maximum questions in session
     * @return created interview session
     */
    public InterviewSession startInterview(String studentName, String topic, int maxQuestions) {
        log.info("Starting interview for student: {} on topic: {}", studentName, topic);
        
        String sessionId = UUID.randomUUID().toString();
        InterviewSession session = interviewSessionService.createSession(sessionId, studentName, topic, maxQuestions);
        
        conversationManager.initializeSession(session.getId());
        log.debug("Interview session initialized with ID: {}", sessionId);
        
        return session;
    }

    /**
     * Generate next interview question.
     *
     * @param sessionId interview session ID
     * @return generated question
     */
    public String generateNextQuestion(String sessionId) {
        log.debug("Generating next question for session: {}", sessionId);
        
        InterviewSession session = interviewSessionService.getSessionBySessionId(sessionId);
        
        if (session.getQuestionsAsked() >= session.getTotalQuestions()) {
            throw new IllegalStateException("Maximum questions reached for this session");
        }
        
        String question = questionGenerator.generateQuestion(
            session.getInterviewTopic(),
            session.getCurrentDifficulty()
        );
        
        session.setQuestionsAsked(session.getQuestionsAsked() + 1);
        interviewSessionService.updateSession(session);
        
        return question;
    }

    /**
     * Process student answer and generate follow-up if needed.
     *
     * @param sessionId interview session ID
     * @param question current question
     * @param studentAnswer student's response
     * @return evaluation and potential follow-up question
     */
    public InterviewExchange processAnswer(String sessionId, String question, String studentAnswer) {
        log.debug("Processing answer for session: {}", sessionId);
        
        InterviewSession session = interviewSessionService.getSessionBySessionId(sessionId);
        
        // Evaluate answer
        InterviewExchange exchange = responseEvaluator.evaluateAnswer(
            session,
            question,
            studentAnswer
        );
        
        // Save exchange
        interviewSessionService.saveExchange(session.getId(), exchange);
        
        // Adapt difficulty based on performance
        adaptDifficulty(session, exchange);
        
        return exchange;
    }

    /**
     * Adapt interview difficulty based on answer quality.
     */
    private void adaptDifficulty(InterviewSession session, InterviewExchange exchange) {
        Double relevanceScore = exchange.getRelevanceScore();
        
        if (relevanceScore != null) {
            if (relevanceScore > 0.8) {
                session.setCurrentDifficulty(InterviewSession.DifficultyLevel.HARD);
            } else if (relevanceScore < 0.4) {
                session.setCurrentDifficulty(InterviewSession.DifficultyLevel.EASY);
            }
            interviewSessionService.updateSession(session);
        }
    }

    /**
     * End interview and generate performance report.
     *
     * @param sessionId interview session ID
     * @return interview session with completed status
     */
    public InterviewSession endInterview(String sessionId) {
        log.info("Ending interview session: {}", sessionId);
        
        InterviewSession session = interviewSessionService.getSessionBySessionId(sessionId);
        session.setStatus(InterviewSession.InterviewStatus.COMPLETED);
        interviewSessionService.updateSession(session);
        
        conversationManager.closeSession(session.getId());
        
        return session;
    }
}
