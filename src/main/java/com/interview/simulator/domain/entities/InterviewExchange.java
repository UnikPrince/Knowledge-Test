package com.interview.simulator.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "interview_exchanges")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewExchange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private InterviewSession session;

    @Column(nullable = false)
    private Integer exchangeIndex;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String question;

    @Column(columnDefinition = "TEXT")
    private String studentAnswer;

    @Column(columnDefinition = "TEXT")
    private String aiEvaluation;

    @Enumerated(EnumType.STRING)
    private AnswerQuality answerQuality;

    private Double relevanceScore;

    private Double clarityScore;

    private Double completenessScore;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public enum AnswerQuality {
        EXCELLENT,
        GOOD,
        SATISFACTORY,
        POOR,
        INCORRECT
    }
}
