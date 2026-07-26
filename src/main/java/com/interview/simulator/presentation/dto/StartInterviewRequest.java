package com.interview.simulator.presentation.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StartInterviewRequest {
    private String studentName;
    private String topic;
    private Integer maxQuestions;
}
