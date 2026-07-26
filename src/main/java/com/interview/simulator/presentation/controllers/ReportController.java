package com.interview.simulator.presentation.controllers;

import com.interview.simulator.application.services.InterviewSessionService;
import com.interview.simulator.domain.entities.PerformanceReport;
import com.interview.simulator.infrastructure.persistence.PerformanceReportRepository;
import com.interview.simulator.presentation.dto.PerformanceReportDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/reports")
@Slf4j
@CrossOrigin(origins = {"localhost:3000", "localhost:3001"})
public class ReportController {

    @Autowired
    private PerformanceReportRepository reportRepository;

    @Autowired
    private InterviewSessionService sessionService;

    /**
     * Get performance report for an interview session.
     */
    @GetMapping("/{sessionId}")
    public ResponseEntity<?> getPerformanceReport(@PathVariable String sessionId) {
        try {
            log.info("Fetching performance report for session: {}", sessionId);
            
            var session = sessionService.getSessionBySessionId(sessionId);
            if (session.getPerformanceReport() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("No performance report available yet"));
            }
            
            PerformanceReport report = session.getPerformanceReport();
            PerformanceReportDTO dto = PerformanceReportDTO.builder()
                    .overallScore(report.getOverallScore())
                    .technicalKnowledge(report.getTechnicalKnowledge())
                    .conceptClarity(report.getConceptClarity())
                    .communicationSkills(report.getCommunicationSkills())
                    .confidenceLevel(report.getConfidenceLevel())
                    .strengthAreas(report.getStrengthAreas())
                    .weakAreas(report.getWeakAreas())
                    .recommendations(report.getStudyRecommendations())
                    .readinessLevel(report.getEstimatedReadinessLevel())
                    .build();
            
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error fetching performance report", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error fetching report: " + e.getMessage()));
        }
    }

    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class ErrorResponse {
        private String error;
    }
}
