package com.vertex.ai;

import com.vertex.collector.CollectedItem;
import com.vertex.config.VertexProperties;
import com.vertex.item.Item;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GeminiSummarizer implements Summarizer {
    private final RestClient restClient;
    private final VertexProperties properties;

    public GeminiSummarizer(RestClient restClient, VertexProperties properties) {
        this.restClient = restClient;
        this.properties = properties;
    }

    @Override
    public String summarize(CollectedItem item, SummaryDepth depth) {
        if (properties.geminiApiKey() == null || properties.geminiApiKey().isBlank()) {
            return fallbackSummary(item, depth);
        }
        GeminiResponse response;
        try {
            response = restClient.post()
                    .uri("https://generativelanguage.googleapis.com/v1beta/models/{model}:generateContent?key={key}",
                            properties.geminiModel(), properties.geminiApiKey())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new GeminiRequest(List.of(new Content(List.of(new Part(prompt(item, depth)))))))
                    .retrieve()
                    .body(GeminiResponse.class);
        } catch (RestClientException exception) {
            return fallbackSummary(item, depth);
        }
        if (response == null || response.candidates() == null || response.candidates().isEmpty()) {
            return fallbackSummary(item, depth);
        }
        Candidate candidate = response.candidates().getFirst();
        if (candidate.content() == null || candidate.content().parts() == null || candidate.content().parts().isEmpty()) {
            return fallbackSummary(item, depth);
        }
        String text = candidate.content().parts().getFirst().text();
        if (text == null || text.isBlank()) {
            return fallbackSummary(item, depth);
        }
        return text.trim();
    }

    @Override
    public String summarizeBrief(List<Item> items) {
        if (items.isEmpty()) {
            return "No fresh intelligence items are available for today's brief yet.";
        }
        String sourceText = items.stream()
                .map(item -> "- %s (%s, score %.1f): %s".formatted(item.getTitle(), item.getSource(), item.getScore(), item.getSummary()))
                .collect(Collectors.joining("\n"));
        if (properties.geminiApiKey() == null || properties.geminiApiKey().isBlank()) {
            return fallbackBrief(items);
        }
        try {
            GeminiResponse response = restClient.post()
                    .uri("https://generativelanguage.googleapis.com/v1beta/models/{model}:generateContent?key={key}",
                            properties.geminiModel(), properties.geminiApiKey())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new GeminiRequest(List.of(new Content(List.of(new Part("""
                            Write one concise paragraph for a developer daily brief.
                            Mention the most important themes and why they matter.
                            Items:
                            %s
                            """.formatted(sourceText)))))))
                    .retrieve()
                    .body(GeminiResponse.class);
            if (response == null || response.candidates() == null || response.candidates().isEmpty()) {
                return fallbackBrief(items);
            }
            Candidate candidate = response.candidates().getFirst();
            if (candidate.content() == null || candidate.content().parts() == null || candidate.content().parts().isEmpty()) {
                return fallbackBrief(items);
            }
            String text = candidate.content().parts().getFirst().text();
            return text == null || text.isBlank() ? fallbackBrief(items) : text.trim();
        } catch (RestClientException exception) {
            return fallbackBrief(items);
        }
    }

    private String prompt(CollectedItem item, SummaryDepth depth) {
        String audience = depth == SummaryDepth.JUNIOR
                ? "Explain it for a junior developer with plain language and why it matters."
                : "Explain it for a senior developer with technical signal, tradeoffs, and likely impact.";
        return """
                Summarize this developer intelligence item in 2 concise sentences.
                %s
                Keep it factual.
                Title: %s
                Source: %s
                Text: %s
                """.formatted(audience, item.title(), item.source(), item.rawText());
    }

    private String fallbackSummary(CollectedItem item, SummaryDepth depth) {
        String text = item.rawText() == null || item.rawText().isBlank() ? item.title() : item.rawText();
        String cleaned = text.replaceAll("\\s+", " ").trim();
        String prefix = depth == SummaryDepth.JUNIOR ? "Plain take: " : "Signal: ";
        String limited = cleaned.length() <= 280 ? cleaned : cleaned.substring(0, 277) + "...";
        return prefix + limited;
    }

    private String fallbackBrief(List<Item> items) {
        String titles = items.stream().map(Item::getTitle).limit(5).collect(Collectors.joining("; "));
        return "Today's strongest developer signals are clustered around: " + titles + ". These ranked items are worth reviewing because they combine freshness, activity, and cross-source relevance.";
    }

    record GeminiRequest(List<Content> contents) {
    }

    record Content(List<Part> parts) {
    }

    record Part(String text) {
    }

    record GeminiResponse(List<Candidate> candidates) {
    }

    record Candidate(Content content) {
    }
}
