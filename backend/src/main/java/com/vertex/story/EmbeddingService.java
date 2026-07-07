package com.vertex.story;

import com.vertex.config.VertexProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class EmbeddingService {
    private final RestClient restClient;
    private final VertexProperties properties;

    public EmbeddingService(RestClient restClient, VertexProperties properties) {
        this.restClient = restClient;
        this.properties = properties;
    }

    public double[] embed(String text) {
        if (properties.geminiApiKey() == null || properties.geminiApiKey().isBlank()) {
            return lexicalEmbedding(text);
        }
        try {
            EmbeddingResponse response = restClient.post()
                    .uri("https://generativelanguage.googleapis.com/v1beta/models/text-embedding-004:embedContent?key={key}", properties.geminiApiKey())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new EmbeddingRequest("RETRIEVAL_DOCUMENT", new Content(List.of(new Part(text)))))
                    .retrieve()
                    .body(EmbeddingResponse.class);
            if (response == null || response.embedding() == null || response.embedding().values() == null || response.embedding().values().isEmpty()) {
                return lexicalEmbedding(text);
            }
            return response.embedding().values().stream().mapToDouble(Double::doubleValue).toArray();
        } catch (RestClientException exception) {
            return lexicalEmbedding(text);
        }
    }

    public double cosine(double[] left, double[] right) {
        int length = Math.min(left.length, right.length);
        double dot = 0.0;
        double leftNorm = 0.0;
        double rightNorm = 0.0;
        for (int index = 0; index < length; index++) {
            dot += left[index] * right[index];
            leftNorm += left[index] * left[index];
            rightNorm += right[index] * right[index];
        }
        if (leftNorm == 0.0 || rightNorm == 0.0) {
            return 0.0;
        }
        return dot / (Math.sqrt(leftNorm) * Math.sqrt(rightNorm));
    }

    private double[] lexicalEmbedding(String text) {
        double[] vector = new double[128];
        Map<String, Integer> terms = new HashMap<>();
        for (String token : text.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9 ]", " ").split("\\s+")) {
            if (token.length() > 2) {
                terms.merge(token, 1, Integer::sum);
            }
        }
        for (Map.Entry<String, Integer> entry : terms.entrySet()) {
            int bucket = Math.floorMod(entry.getKey().hashCode(), vector.length);
            vector[bucket] += entry.getValue();
        }
        return vector;
    }

    record EmbeddingRequest(String taskType, Content content) {
    }

    record Content(List<Part> parts) {
    }

    record Part(String text) {
    }

    record EmbeddingResponse(Embedding embedding) {
    }

    record Embedding(List<Double> values) {
    }
}

