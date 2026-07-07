package com.vertex.config;

import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
public class HttpClientConfig {
    @Bean
    RestClient restClient(RestClient.Builder builder) {
        return builder
                .requestFactory(new JdkClientHttpRequestFactory(HttpClient.newBuilder()
                        .connectTimeout(Duration.ofSeconds(10))
                        .build()))
                .build();
    }

    @Bean
    RestClientCustomizer restClientCustomizer() {
        return builder -> builder.defaultHeader("User-Agent", "Vertex/1.0");
    }
}

