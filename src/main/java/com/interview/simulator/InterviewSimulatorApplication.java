package com.interview.simulator;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot Application entry point for AI Interview Simulator.
 */
@SpringBootApplication
public class InterviewSimulatorApplication {

    public static void main(String[] args) {
        loadEnvironmentVariables();
        SpringApplication.run(InterviewSimulatorApplication.class, args);
    }

    private static void loadEnvironmentVariables() {
        try {
            Dotenv dotenv = Dotenv.load();
            dotenv.entries().forEach(entry -> 
                System.setProperty(entry.getKey(), entry.getValue())
            );
        } catch (Exception e) {
            System.out.println("No .env file found. Using system properties or environment variables.");
        }
    }
}
