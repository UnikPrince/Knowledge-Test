package com.interview.simulator.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "interview_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String sessionId;

    @Column(nullable = false)
    private String studentName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InterviewStatus status;

    private String interviewTopic;

    private Integer totalQuestions;

    private Integer questionsAsked;

    @Enumerated(EnumType.STRING)
    private DifficultyLevel currentDifficulty;

    @Column(name = "started_at", nullable = false, updatable = false)
    private LocalDateTime startedAt;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<InterviewExchange> exchanges = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "performance_report_id")
    private PerformanceReport performanceReport;

    @PrePersist
    protected void onCreate() {
        startedAt = LocalDateTime.now();
        status = InterviewStatus.ACTIVE;
        questionsAsked = 0;
        currentDifficulty = DifficultyLevel.MEDIUM;
    }

    public enum InterviewStatus {
        PENDING,
        ACTIVE,
        PAUSED,
        COMPLETED,
        ABANDONED
    }

    public enum DifficultyLevel {
        EASY,
        MEDIUM,
        HARD,
        EXPERT
    }
}
