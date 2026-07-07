package com.vertex.config;

import com.vertex.auth.GoogleOAuthSuccessHandler;
import com.vertex.auth.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, GoogleOAuthSuccessHandler successHandler) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/items/**", "/api/search", "/api/brief/**", "/api/stories/**", "/api/weekly/**").permitAll()
                        .requestMatchers("/api/internal/**").permitAll()
                        .requestMatchers("/oauth2/**", "/login/**", "/actuator/health").permitAll()
                        .anyRequest().authenticated())
                .headers(headers -> headers
                        .contentTypeOptions(Customizer.withDefaults())
                        .frameOptions(frameOptions -> frameOptions.deny())
                        .httpStrictTransportSecurity(hsts -> hsts.includeSubDomains(true).preload(true).maxAgeInSeconds(31_536_000))
                        .referrerPolicy(referrer -> referrer.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)))
                .oauth2Login(oauth -> oauth.successHandler(successHandler))
                .oauth2ResourceServer(oauth -> oauth.jwt(Customizer.withDefaults()));
        return http.build();
    }

    @Bean
    JwtDecoder jwtDecoder(VertexProperties properties) {
        SecretKeySpec key = new SecretKeySpec(
                properties.jwtSecret().getBytes(StandardCharsets.UTF_8),
                JwtService.HMAC_ALGORITHM
        );
        return NimbusJwtDecoder.withSecretKey(key).build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(VertexProperties properties) {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.stream(properties.allowedOrigins().split(","))
                .map(String::trim)
                .filter(origin -> !origin.isBlank())
                .toList());
        configuration.setAllowedMethods(List.of("GET", "POST", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Internal-Api-Key"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
