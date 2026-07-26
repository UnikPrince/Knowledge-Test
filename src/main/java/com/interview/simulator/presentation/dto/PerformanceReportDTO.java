package com.interview.simulator.presentation.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerformanceReportDTO {
    private Double overallScore;
    private Double technicalKnowledge;
    private Double conceptClarity;
    private Double communicationSkills;
    private Double confidenceLevel;
    private String strengthAreas;
    private String weakAreas;
    private String recommendations;
    private Integer readinessLevel;
}
