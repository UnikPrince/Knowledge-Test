package com.interview.simulator.config;

import com.google.ai.client.generativeai.GenerativeModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class GoogleAiConfig {

    @Value("${google.api.key:#{null}}")
    private String apiKey;

    @Value("${google.model.id:gemini-2.0-flash}")
    private String modelId;

    @Bean
    public GenerativeModel generativeModel() {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalArgumentException(
                "Google API key is not configured."
            );
        }
        log.info("Initializing Google Generative AI with model: {}", modelId);
        return new GenerativeModel(modelId, apiKey);
    }
}
