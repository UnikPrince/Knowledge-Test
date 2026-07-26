package com.interview.simulator.application.services;

import com.interview.simulator.domain.entities.InterviewExchange;
import com.interview.simulator.domain.entities.InterviewSession;
import com.interview.simulator.infrastructure.ai.PromptEngineer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Response evaluator for student answers.
 * Evaluates accuracy, clarity, and completeness of responses.
 */
@Service
@Slf4j
public class ResponseEvaluator {

    @Autowired
    private PromptEngineer promptEngineer;

    /**
     * Evaluate student answer to interview question.
     *
     * @param session interview session
     * @param question interview question
     * @param answer student's answer
     * @return evaluated exchange with scores and feedback
     */
    public InterviewExchange evaluateAnswer(InterviewSession session, String question, String answer) {
        log.debug("Evaluating answer for session: {}", session.getId());
        
        String evaluationPrompt = promptEngineer.generateEvaluationPrompt(question, answer);
        // In production: call generativeModel API with evaluationPrompt
        
        InterviewExchange exchange = InterviewExchange.builder()
                .session(session)
                .exchangeIndex(session.getQuestionsAsked())
                .question(question)
                .studentAnswer(answer)
                .relevanceScore(0.75)
                .clarityScore(0.80)
                .completenessScore(0.70)
                .answerQuality(InterviewExchange.AnswerQuality.GOOD)
                .feedback("Good understanding of the topic. Consider providing more concrete examples.")
                .build();
        
        return exchange;
    }

    /**
     * Calculate overall answer quality score.
     *
     * @param relevance relevance score (0-1)
     * @param clarity clarity score (0-1)
     * @param completeness completeness score (0-1)
     * @return overall quality score
     */
    public double calculateQualityScore(double relevance, double clarity, double completeness) {
        return (relevance * 0.4 + clarity * 0.3 + completeness * 0.3);
    }

    /**
     * Determine answer quality level.
     */
    public InterviewExchange.AnswerQuality determineQuality(double qualityScore) {
        if (qualityScore >= 0.9) return InterviewExchange.AnswerQuality.EXCELLENT;
        if (qualityScore >= 0.75) return InterviewExchange.AnswerQuality.GOOD;
        if (qualityScore >= 0.6) return InterviewExchange.AnswerQuality.SATISFACTORY;
        if (qualityScore >= 0.4) return InterviewExchange.AnswerQuality.POOR;
        return InterviewExchange.AnswerQuality.INCORRECT;
    }
}
