package com.vertex.item;

import com.vertex.item.dto.ItemResponse;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ItemMapperTest {
    @Test
    void mapsItemToApiResponse() {
        Item item = new Item(
                SourceType.GITHUB,
                "owner/repo",
                "https://github.com/owner/repo",
                "Summary",
                List.of("github", "java"),
                88.2,
                Instant.parse("2026-07-07T00:00:00Z"),
                Map.of("stars", 42)
        );

        ItemResponse response = new ItemMapper().toResponse(item);

        assertThat(response.title()).isEqualTo("owner/repo");
        assertThat(response.tags()).containsExactly("github", "java");
        assertThat(response.metadata()).containsEntry("stars", 42);
    }
}

