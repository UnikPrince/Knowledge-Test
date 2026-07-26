package com.interview.simulator.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "performance_reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerformanceReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double overallScore;

    @Column(nullable = false)
    private Double technicalKnowledge;

    @Column(nullable = false)
    private Double conceptClarity;

    @Column(nullable = false)
    private Double communicationSkills;

    @Column(nullable = false)
    private Double confidenceLevel;

    @Column(nullable = false)
    private Double logicalReasoning;

    @Column(nullable = false)
    private Double problemSolvingAbility;

    @Column(nullable = false)
    private Double answerAccuracy;

    @Column(columnDefinition = "TEXT")
    private String strengthAreas;

    @Column(columnDefinition = "TEXT")
    private String weakAreas;

    @Column(columnDefinition = "TEXT")
    private String studyRecommendations;

    @Column(columnDefinition = "TEXT")
    private String personalizedFeedback;

    private Integer estimatedReadinessLevel;

    @Column(name = "generated_at", nullable = false, updatable = false)
    private LocalDateTime generatedAt;

    @PrePersist
    protected void onCreate() {
        generatedAt = LocalDateTime.now();
    }
}
