package com.interview.simulator.application.services;

import com.interview.simulator.infrastructure.ai.PromptEngineer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Question generator for interview sessions.
 * Generates contextual questions based on topic and difficulty level.
 */
@Service
@Slf4j
public class QuestionGenerator {

    @Autowired
    private PromptEngineer promptEngineer;

    /**
     * Generate interview question based on topic and difficulty.
     *
     * @param topic interview topic
     * @param difficulty difficulty level
     * @return generated question
     */
    public String generateQuestion(String topic, Object difficulty) {
        log.debug("Generating question for topic: {} at difficulty: {}", topic, difficulty);
        
        String prompt = promptEngineer.generateInitialQuestionPrompt(topic);
        // In production: use generativeModel to call API
        // For now, return a mock question
        return "What are the key benefits of using Spring Boot in microservices architecture?";
    }

    /**
     * Generate follow-up question.
     *
     * @param originalQuestion original question
     * @param studentAnswer student's answer
     * @return follow-up question
     */
    public String generateFollowUp(String originalQuestion, String studentAnswer) {
        log.debug("Generating follow-up question");
        
        String prompt = promptEngineer.generateFollowUpPrompt(originalQuestion, studentAnswer);
        // In production: use generativeModel to call API
        return "Can you elaborate on how dependency injection improves testability?";
    }
}
