package com.vertex.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "vertex")
public record VertexProperties(
        @NotBlank String frontendUrl,
        String githubToken,
        String geminiApiKey,
        @NotBlank String geminiModel,
        @NotBlank String jwtSecret,
        @NotBlank String internalApiKey,
        boolean collectorEnabled,
        @NotBlank String allowedOrigins
) {
}

