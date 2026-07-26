package com.interview.simulator.presentation.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluationResponse {
    private Double relevanceScore;
    private Double clarityScore;
    private Double completenessScore;
    private String answerQuality;
    private String feedback;
    private String nextQuestion;
}
