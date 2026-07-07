package com.vertex.item.dto;

import com.vertex.item.SourceType;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record ItemResponse(
        UUID id,
        SourceType source,
        String title,
        String url,
        String summary,
        List<String> tags,
        double score,
        Instant createdAt,
        Instant updatedAt,
        Map<String, Object> metadata
) {
}

