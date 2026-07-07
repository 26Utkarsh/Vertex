package com.vertex.story;

import com.vertex.config.VertexProperties;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;

class EmbeddingServiceTest {
    @Test
    void lexicalFallbackProducesHighSimilarityForRelatedText() {
        EmbeddingService service = new EmbeddingService(RestClient.create(), new VertexProperties(
                "http://localhost:3000",
                "",
                "",
                "gemini-2.0-flash",
                "abcdefghijklmnopqrstuvwxyz123456",
                "internal",
                false,
                "http://localhost:3000"
        ));

        double similarity = service.cosine(
                service.embed("open source local llm agent framework"),
                service.embed("local llm agent framework for open source developers")
        );

        assertThat(similarity).isGreaterThan(0.85);
    }
}
