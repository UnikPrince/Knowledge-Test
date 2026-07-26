package com.interview.simulator.presentation.controllers;

import com.interview.simulator.application.services.InterviewAgent;
import com.interview.simulator.application.services.InterviewSessionService;
import com.interview.simulator.application.services.ResponseEvaluator;
import com.interview.simulator.domain.entities.InterviewExchange;
import com.interview.simulator.domain.entities.InterviewSession;
import com.interview.simulator.presentation.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/interviews")
@Slf4j
@CrossOrigin(origins = {"localhost:3000", "localhost:3001"})
public class InterviewController {

    @Autowired
    private InterviewAgent interviewAgent;

    @Autowired
    private InterviewSessionService sessionService;

    @Autowired
    private ResponseEvaluator responseEvaluator;

    /**
     * Start a new interview session.
     */
    @PostMapping("/start")
    public ResponseEntity<?> startInterview(@RequestBody StartInterviewRequest request) {
        try {
            log.info("Starting interview for: {}", request.getStudentName());
            
            int maxQuestions = request.getMaxQuestions() != null ? request.getMaxQuestions() : 10;
            InterviewSession session = interviewAgent.startInterview(
                request.getStudentName(),
                request.getTopic(),
                maxQuestions
            );
            
            String question = interviewAgent.generateNextQuestion(session.getSessionId());
            
            InterviewResponse response = InterviewResponse.builder()
                    .sessionId(session.getSessionId())
                    .question(question)
                    .questionsRemaining(session.getTotalQuestions() - session.getQuestionsAsked())
                    .status("ACTIVE")
                    .build();
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error starting interview", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Failed to start interview: " + e.getMessage()));
        }
    }

    /**
     * Get current question for a session.
     */
    @GetMapping("/{sessionId}/question")
    public ResponseEntity<?> getCurrentQuestion(@PathVariable String sessionId) {
        try {
            InterviewSession session = sessionService.getSessionBySessionId(sessionId);
            String question = interviewAgent.generateNextQuestion(sessionId);
            
            InterviewResponse response = InterviewResponse.builder()
                    .sessionId(sessionId)
                    .question(question)
                    .questionsRemaining(session.getTotalQuestions() - session.getQuestionsAsked())
                    .status(session.getStatus().toString())
                    .build();
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting question for session: {}", sessionId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Session not found"));
        }
    }

    /**
     * Submit student answer for evaluation.
     */
    @PostMapping("/{sessionId}/submit")
    public ResponseEntity<?> submitAnswer(
            @PathVariable String sessionId,
            @RequestBody SubmitAnswerRequest request) {
        try {
            log.debug("Processing answer for session: {}", sessionId);
            
            InterviewSession session = sessionService.getSessionBySessionId(sessionId);
            String question = "Current question"; // Retrieve from session context
            
            InterviewExchange exchange = interviewAgent.processAnswer(sessionId, question, request.getAnswer());
            
            EvaluationResponse response = EvaluationResponse.builder()
                    .relevanceScore(exchange.getRelevanceScore())
                    .clarityScore(exchange.getClarityScore())
                    .completenessScore(exchange.getCompletenessScore())
                    .answerQuality(exchange.getAnswerQuality().toString())
                    .feedback(exchange.getFeedback())
                    .nextQuestion(session.getQuestionsAsked() < session.getTotalQuestions() 
                            ? "Next question available" : "Interview completed")
                    .build();
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error processing answer", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error processing answer: " + e.getMessage()));
        }
    }

    /**
     * End interview session.
     */
    @PostMapping("/{sessionId}/end")
    public ResponseEntity<?> endInterview(@PathVariable String sessionId) {
        try {
            log.info("Ending interview session: {}", sessionId);
            InterviewSession session = interviewAgent.endInterview(sessionId);
            return ResponseEntity.ok(new ApiResponse("Interview completed successfully"));
        } catch (Exception e) {
            log.error("Error ending interview", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error ending interview"));
        }
    }

    /**
     * Get interview session details.
     */
    @GetMapping("/{sessionId}")
    public ResponseEntity<?> getSessionDetails(@PathVariable String sessionId) {
        try {
            InterviewSession session = sessionService.getSessionBySessionId(sessionId);
            return ResponseEntity.ok(session);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Session not found"));
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApiResponse {
        private String message;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorResponse {
        private String error;
    }
}
