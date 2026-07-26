package com.interview.simulator.presentation.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewResponse {
    private String sessionId;
    private String question;
    private Integer questionsRemaining;
    private String status;
}
