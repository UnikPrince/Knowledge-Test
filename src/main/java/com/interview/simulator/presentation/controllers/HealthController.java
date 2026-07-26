package com.interview.simulator.presentation.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/v1/health")
public class HealthController {

    @GetMapping
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(new HealthStatus("UP", "AI Interview Simulator is running"));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HealthStatus {
        private String status;
        private String message;
    }
}
