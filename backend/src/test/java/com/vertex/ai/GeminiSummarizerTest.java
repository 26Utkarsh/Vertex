package com.vertex.ai;

import com.vertex.collector.CollectedItem;
import com.vertex.config.VertexProperties;
import com.vertex.item.SourceType;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class GeminiSummarizerTest {
    @Test
    void usesDeterministicSummaryWhenGeminiKeyIsUnavailable() {
        VertexProperties properties = new VertexProperties(
                "http://localhost:3000",
                "",
                "",
                "gemini-2.0-flash",
                "abcdefghijklmnopqrstuvwxyz123456",
                "internal",
                false,
                "http://localhost:3000"
        );
        GeminiSummarizer summarizer = new GeminiSummarizer(RestClient.create(), properties);

        String summary = summarizer.summarize(new CollectedItem(
                SourceType.GITHUB,
                "owner/repo",
                "https://github.com/owner/repo",
                "A focused testing utility for Java developers.",
                List.of("github"),
                Instant.now(),
                10,
                1,
                Map.of()
        ), SummaryDepth.SENIOR);

        assertThat(summary).isEqualTo("Signal: A focused testing utility for Java developers.");
    }
}
