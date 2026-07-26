package com.interview.simulator.presentation.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmitAnswerRequest {
    private String answer;
}
